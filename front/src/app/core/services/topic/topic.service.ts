import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, switchMap } from 'rxjs';
import { Topic } from '../../interfaces/topic.interface';
import { AuthService } from '../auth/auth.service';

@Injectable({
  providedIn: 'root',
})
export class TopicService {
  constructor(
    private http: HttpClient,
    private authService: AuthService
  ) {}

  getUserTopics(): Observable<Topic[]> {
    return this.authService.getUserSession().pipe(
      switchMap((session) => {
        if (!session) {
          throw new Error('Not authenticated');
        }
        return this.http.get<Topic[]>(`/users/${session.userId}/topics`);
      })
    );
  }
}
