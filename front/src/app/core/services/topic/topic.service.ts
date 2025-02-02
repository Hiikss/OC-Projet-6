import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
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

  /**
   * Get authenticated user's topics list
   */
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

  /**
   * Get all topics by a pagination
   * @param page
   * @param size
   */
  getAllTopics(page: number, size: number): Observable<Topic[]> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    return this.http.get<Topic[]>(`/topics`, { params });
  }

  /**
   * Add a topic to the authenticated user
   * @param topicId
   */
  addTopicToUser(topicId: string): Observable<Topic[]> {
    return this.authService.getUserSession().pipe(
      switchMap((session) => {
        if (!session) {
          throw new Error('Not authenticated');
        }
        return this.http.put<Topic[]>(`/users/${session.userId}/topics/${topicId}`, null);
      })
    );
  }

  /**
   * Remove a topic from the authenticated user
   * @param topicId
   */
  removeTopicFromUser(topicId: string): Observable<Topic[]> {
    return this.authService.getUserSession().pipe(
      switchMap((session) => {
        if (!session) {
          throw new Error('Not authenticated');
        }
        return this.http.delete<Topic[]>(`/users/${session.userId}/topics/${topicId}`);
      })
    );
  }
}
