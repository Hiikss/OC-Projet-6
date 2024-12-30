import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { AuthService } from '../auth/auth.service';
import { catchError, switchMap, take, tap, throwError } from 'rxjs';
import { User } from '../../interfaces/user.interface';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  constructor(
    private http: HttpClient,
    private authService: AuthService
  ) {}

  updateUser(updatedFields: any) {
    return this.authService.getUserSession().pipe(
      take(1),
      switchMap((session) => {
        if (!session) {
          throw new Error('Not authenticated');
        }
        return this.http
          .patch<User>(`/users/${session.userId}`, updatedFields)
          .pipe(
            tap((updatedUser) => {
              this.authService.updateUserSession({
                email: updatedUser.email,
                username: updatedUser.username,
              });
            }),
            catchError((error) => {
              return throwError(() => error);
            })
          );
      })
    );
  }
}
