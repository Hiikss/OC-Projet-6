import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { PostService } from '../../../core/services/post/post.service';
import { Post } from '../../../core/interfaces/post.interface';
import { Comment } from '../../../core/interfaces/comment.interface';
import { throwError } from 'rxjs';
import { DatePipe, NgForOf, NgIf } from '@angular/common';
import { Divider } from 'primeng/divider';
import { CommentService } from '../../../core/services/comment/comment.service';
import { Card } from 'primeng/card';
import { Button } from 'primeng/button';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { FloatLabel } from 'primeng/floatlabel';
import { Textarea } from 'primeng/textarea';
import { ProgressSpinner } from 'primeng/progressspinner';

@Component({
  selector: 'app-post-details',
  standalone: true,
  imports: [
    NgIf,
    DatePipe,
    Divider,
    NgForOf,
    Card,
    Button,
    ReactiveFormsModule,
    FloatLabel,
    Textarea,
    ProgressSpinner,
  ],
  templateUrl: './post-details.component.html',
  styleUrl: './post-details.component.scss',
})
export class PostDetailsComponent implements OnInit {
  private postId!: string;
  commentForm: FormGroup;
  post!: Post;
  comments!: Comment[];

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private fb: FormBuilder,
    private postService: PostService,
    private commentService: CommentService
  ) {
    this.commentForm = this.fb.group({
      commentContent: ['', Validators.required],
    });
  }

  ngOnInit(): void {
    this.postId = this.route.snapshot.params['id'];
    this.postService.getPostById(this.postId).subscribe({
      next: (post) => {
        this.post = post;
        this.getComments();
      },
      error: (err) => {
        if (err.status === 404) {
          this.router.navigate(['/**'], { skipLocationChange: true });
        }
        return throwError(() => err);
      },
    });
  }

  sendComment() {
    if (this.commentForm.valid) {
      this.commentService
        .addCommentToPost(
          this.postId,
          this.commentForm.get('commentContent')?.value
        )
        .subscribe({
          next: () => {
            this.commentForm.reset();
            this.getComments();
          },
        });
    }
  }

  private getComments() {
    this.commentService.getPostComments(this.postId).subscribe({
      next: (comments) =>
        (this.comments = comments.sort(
          (a, b) => b.createdAt.valueOf() - a.createdAt.valueOf()
        )),
    });
  }
}
