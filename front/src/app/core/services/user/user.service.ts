import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { AuthService } from '../auth/auth.service';
import { switchMap } from 'rxjs';
import { Topic } from '../../interfaces/topic.interface';
import { AuthenticatedUser } from '../../interfaces/authenticatedUser.interface';
import { User } from '../../interfaces/user.interface';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(
    private http: HttpClient,
    private authService: AuthService
  ) { }

  updateUser(updatedFields: any) {
    return this.authService.getUserSession().pipe(
      switchMap((session) => {
        if (!session) {
          throw new Error('Not authenticated');
        }
        return this.http.patch<User>(`/users/${session.userId}`, updatedFields);
      })
    );
  }
}
