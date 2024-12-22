import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Divider } from 'primeng/divider';
import { AuthService } from '../../../core/services/auth/auth.service';
import { TopicService } from '../../../core/services/topic/topic.service';
import { Topic } from '../../../core/interfaces/topic.interface';
import { Card } from 'primeng/card';
import { NgForOf } from '@angular/common';
import { Button } from 'primeng/button';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [Divider, Card, NgForOf, Button],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.scss',
})
export class ProfileComponent implements OnInit {
  userTopics: Topic[] = [];

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private topicService: TopicService
  ) {}

  ngOnInit(): void {
    this.topicService.getUserTopics().subscribe({
      next: (topics) => {
        this.userTopics = topics;
      },
    });
  }

  unsubscribe(topicId: string) {
    this.topicService.removeTopicFromUser(topicId).subscribe({
      next: (topics) => {
        this.userTopics = topics;
      },
      error: err => {
        console.log(err);
      }
    });
  }
}
