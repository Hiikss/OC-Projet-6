import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Post } from '../../interfaces/post.interface';
import { Topic } from '../../interfaces/topic.interface';
import { PostRequest } from '../../interfaces/postRequest.interface';

@Injectable({
  providedIn: 'root',
})
export class PostService {
  constructor(private http: HttpClient) {}

  /**
   * Get posts array based on pagination and filters
   * @param page
   * @param size
   * @param userTopics
   * @param sortDir
   */
  getPosts(
    page: number,
    size: number,
    userTopics: Topic[],
    sortDir: string
  ): Observable<Post[]> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set(
        'topicTitles',
        userTopics.map((topic) => encodeURIComponent(topic.title)).toString()
      )
      .set('sortBy', 'createdAt')
      .set('sortDir', sortDir);

    return this.http.get<Post[]>('/posts', { params });
  }

  /**
   * Get a post by its id
   * @param id
   */
  getPostById(id: string): Observable<Post> {
    const params = new HttpParams().set('id', id);

    return this.http.get<Post>(`/posts/${id}`, { params });
  }

  /**
   * Create a new post
   * @param postRequest
   */
  createPost(postRequest: PostRequest): Observable<Post> {
    return this.http.post<Post>(`/posts`, postRequest);
  }
}
