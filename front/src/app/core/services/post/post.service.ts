import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Post } from '../../interfaces/post.interface';
import { Topic } from '../../interfaces/topic.interface';

@Injectable({
  providedIn: 'root',
})
export class PostService {
  constructor(private http: HttpClient) {}

  getPosts(
    page: number,
    size: number,
    userTopics: Topic[],
    sortDir: string
  ): Observable<Post[]> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('topicTitles', userTopics.map((topic) => topic.title).toString())
      .set('sortBy', 'createdAt')
      .set('sortDir', sortDir);

    return this.http.get<Post[]>('/posts', { params });
  }

  getPostById(id: string): Observable<Post> {
    const params = new HttpParams().set('id', id);

    return this.http.get<Post>(`/posts/${id}`, { params });
  }
}
