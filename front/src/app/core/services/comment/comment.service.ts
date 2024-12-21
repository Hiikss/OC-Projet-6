import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Comment } from '../../interfaces/comment.interface';

@Injectable({
  providedIn: 'root',
})
export class CommentService {
  constructor(private http: HttpClient) {}

  getPostComments(postId: string): Observable<Comment[]> {
    return this.http.get<Comment[]>(`/posts/${postId}/comments`);
  }

  addCommentToPost(postId: string, commentContent: string): Observable<any> {
    return this.http
      .post(`/posts/${postId}/comments`, {
        content: commentContent,
      });
  }
}
