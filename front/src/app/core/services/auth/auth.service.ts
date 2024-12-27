import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import {
  BehaviorSubject,
  catchError,
  map,
  Observable,
  tap,
  throwError,
} from 'rxjs';
import { CookieService } from 'ngx-cookie-service';
import { AuthenticatedUser } from '../../interfaces/authenticatedUser.interface';
import { LoginRequest } from '../../interfaces/loginRequest.interface';
import { RefreshTokenRequest } from '../../interfaces/refreshTokenRequest.interface';
import { UserRequest } from '../../interfaces/userRequest.interface';
import { MessageService } from 'primeng/api';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private userSession$ = new BehaviorSubject<AuthenticatedUser | null>(null);

  constructor(
    private router: Router,
    private http: HttpClient,
    private cookieService: CookieService,
    private messageService: MessageService
  ) {}

  login(loginRequest: LoginRequest) {
    return this.http.post<AuthenticatedUser>('/auth/login', loginRequest).pipe(
      tap((response) => {
        this.saveTokens(
          response.accessToken,
          response.refreshToken,
          response.userId
        );
        this.userSession$.next(response);
      }),
      catchError((error) => {
        let message = error.message;
        if (error.status === 401) {
          message = 'Identifiants incorrects';
        }
        return throwError(() => new Error(message));
      })
    );
  }

  register(userRequest: UserRequest) {
    return this.http
      .post<AuthenticatedUser>('/auth/register', userRequest)
      .pipe(
        tap((response) => {
          this.saveTokens(
            response.accessToken,
            response.refreshToken,
            response.userId
          );
          this.userSession$.next(response);
        }),
        catchError((error) => {
          return throwError(() => error);
        })
      );
  }

  refreshToken(refreshTokenRequest: RefreshTokenRequest) {
    return this.http
      .post<AuthenticatedUser>('/auth/refresh', refreshTokenRequest)
      .pipe(
        tap((response) => {
          this.saveTokens(
            response.accessToken,
            response.refreshToken,
            response.userId
          );
          this.userSession$.next(response);
        }),
        catchError((error) => {
          this.logout();
          this.messageService.add({
            severity: 'error',
            summary: 'Une erreur est survenue',
            detail: 'Vous avez été déconnecté',
          });
          return throwError(() => error.message);
        })
      );
  }

  /**
   * Restore the session after refresh
   */
  restoreSession(): Promise<void> {
    return new Promise((resolve) => {
      const refreshToken = this.getRefreshToken();
      const userId = this.getUserId();
      if (refreshToken && userId) {
        const refreshTokenRequest: RefreshTokenRequest = {
          refreshToken,
          userId,
        };
        this.refreshToken(refreshTokenRequest).subscribe({
          next: () => {
            resolve();
          },
          error: () => {
            this.logout();
            resolve();
          },
        });
      } else {
        this.clearTokens();
        resolve();
      }
    });
  }

  logout() {
    this.clearTokens();
    this.userSession$.next(null);
    this.router.navigate(['/']);
  }

  getAccessToken(): string | null {
    return this.cookieService.get('accessToken') || null;
  }

  getRefreshToken(): string | null {
    return this.cookieService.get('refreshToken') || null;
  }

  getUserId(): string | null {
    return this.cookieService.get('userId') || null;
  }

  getUserSession() {
    return this.userSession$.asObservable();
  }

  isLoggedIn(): Observable<boolean> {
    return this.userSession$
      .asObservable()
      .pipe(map((session) => session !== null));
  }

  updateUserSession(updates: Partial<AuthenticatedUser>) {
    const currentUser = this.userSession$.getValue();
    if(currentUser) {
      this.userSession$.next({ ...currentUser, ...updates });
    }
  }

  private saveTokens(
    accessToken: string,
    refreshToken: string,
    userId: string
  ) {
    const currentDate = new Date();
    this.cookieService.set('accessToken', accessToken, {
      path: '/',
      secure: true,
      expires: new Date(currentDate.getTime() + 5 * 60 * 1000),
    });
    this.cookieService.set('refreshToken', refreshToken, {
      path: '/',
      secure: true,
      expires: new Date(currentDate.getTime() + 7 * 24 * 60 * 60 * 1000),
    });
    this.cookieService.set('userId', userId, {
      path: '/',
      secure: true,
      expires: new Date(currentDate.getTime() + 7 * 24 * 60 * 60 * 1000),
    });
  }

  private clearTokens() {
    this.cookieService.delete('accessToken', '/');
    this.cookieService.delete('refreshToken', '/');
    this.cookieService.delete('userId', '/');
  }
}
