import { Component, OnInit } from '@angular/core';
import { Card } from 'primeng/card';
import {
  FormBuilder,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { InputText } from 'primeng/inputtext';
import { Select } from 'primeng/select';
import { Topic } from '../../../core/interfaces/topic.interface';
import { TopicService } from '../../../core/services/topic/topic.service';
import { PostService } from '../../../core/services/post/post.service';
import { Button } from 'primeng/button';
import { PostRequest } from '../../../core/interfaces/postRequest.interface';
import { Router, RouterLink } from '@angular/router';
import { MessageService } from 'primeng/api';
import { Textarea } from 'primeng/textarea';

@Component({
  selector: 'app-post-creation',
  standalone: true,
  imports: [
    Card,
    FormsModule,
    InputText,
    ReactiveFormsModule,
    Select,
    Button,
    Textarea,
    RouterLink,
  ],
  templateUrl: './post-creation.component.html',
  styleUrl: './post-creation.component.scss',
})
export class PostCreationComponent implements OnInit {
  postForm: FormGroup;
  topics: Topic[] = [];

  constructor(
    private fb: FormBuilder,
    private topicService: TopicService,
    private postService: PostService,
    private router: Router,
    private messageService: MessageService
  ) {
    this.postForm = this.fb.group({
      title: ['', [Validators.required]],
      topic: ['', [Validators.required]],
      content: ['', [Validators.required]],
    });
  }

  ngOnInit(): void {
    this.topicService.getAllTopics(1, 100).subscribe({
      next: (topics) => {
        this.topics = topics;
      },
    });
  }

  onSubmit() {
    if (this.postForm.valid) {
      const postRequest: PostRequest = {
        title: this.postForm.get('title')?.value,
        content: this.postForm.get('content')?.value,
        topicId: this.postForm.get('topic')?.value.id,
      };
      this.postService.createPost(postRequest).subscribe({
        next: (post) => {
          this.messageService.add({
            severity: 'success',
            summary: 'Succès',
            detail: "L'article a bien été crée",
          });
          this.router.navigate(['/post', post.id]);
        },
        error: (err) => {
          this.messageService.add({
            severity: 'error',
            summary: 'Erreur',
            detail: "Une erreur est survenue lors de la création de l'article",
          });
        },
      });
    }
  }
}
