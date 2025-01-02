import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import {
  BehaviorSubject,
  catchError,
  distinctUntilChanged,
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

  /**
   * Login user
   * @param loginRequest
   */
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

  /**
   * Register new user
   * @param userRequest
   */
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

  /**
   * Refresh authenticated user
   * @param refreshTokenRequest
   */
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
   * Restore the session by refreshing it
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

  /**
   * Logout the user
   */
  logout() {
    this.clearTokens();
    this.userSession$.next(null);
    this.router.navigate(['/']);
  }

  /**
   * Get the access token from the cookie
   */
  getAccessToken(): string | null {
    return this.cookieService.get('accessToken') || null;
  }

  /**
   * Get the refresh token from the cookie
   */
  getRefreshToken(): string | null {
    return this.cookieService.get('refreshToken') || null;
  }

  /**
   * Get the user id from the cookie
   */
  getUserId(): string | null {
    return this.cookieService.get('userId') || null;
  }

  /**
   * Get the session in observable
   */
  getUserSession() {
    return this.userSession$.asObservable().pipe(distinctUntilChanged());
  }

  /**
   * Return if the user is logged in a boolean observable
   */
  isLoggedIn(): Observable<boolean> {
    return this.userSession$
      .asObservable()
      .pipe(map((session) => session !== null));
  }

  /**
   * Update sessions values
   * @param updates
   */
  updateUserSession(updates: Partial<AuthenticatedUser>) {
    const currentUser = this.userSession$.getValue();
    if (currentUser) {
      const updatedUser = { ...currentUser, ...updates };

      if (JSON.stringify(currentUser) !== JSON.stringify(updatedUser)) {
        this.userSession$.next(updatedUser);
      }
    }
  }

  /**
   * Save values in cookies
   * @param accessToken
   * @param refreshToken
   * @param userId
   * @private
   */
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

  /**
   * Clear cookies
   * @private
   */
  private clearTokens() {
    this.cookieService.delete('accessToken', '/');
    this.cookieService.delete('refreshToken', '/');
    this.cookieService.delete('userId', '/');
  }
}
