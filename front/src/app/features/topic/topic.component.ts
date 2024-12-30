import { Component, OnInit } from '@angular/core';
import { Button } from 'primeng/button';
import { Card } from 'primeng/card';
import { NgForOf, NgIf } from '@angular/common';
import { TopicService } from '../../core/services/topic/topic.service';
import { Topic } from '../../core/interfaces/topic.interface';
import { take } from 'rxjs';
import { PrimeTemplate } from 'primeng/api';

@Component({
  selector: 'app-topic',
  standalone: true,
  imports: [Button, Card, NgForOf, NgIf, PrimeTemplate],
  templateUrl: './topic.component.html',
  styleUrl: './topic.component.scss',
})
export class TopicComponent implements OnInit {
  topics: Topic[] = [];
  userTopics: Topic[] = [];

  constructor(private topicService: TopicService) {}

  ngOnInit(): void {
    this.topicService
      .getAllTopics(1, 100)
      .pipe(take(1))
      .subscribe({
        next: (topics) => {
          this.topics = topics;
        },
      });
    this.topicService
      .getUserTopics()
      .pipe(take(1))
      .subscribe({
        next: (topics) => {
          this.userTopics = topics;
        },
      });
  }

  subscribeToTopic(topicId: string) {
    this.topicService
      .addTopicToUser(topicId)
      .pipe(take(1))
      .subscribe({
        next: (topics) => {
          this.userTopics = topics;
        },
      });
  }

  unsubscribeFromTopic(topicId: string) {
    this.topicService
      .removeTopicFromUser(topicId)
      .pipe(take(1))
      .subscribe({
        next: (topics) => {
          this.userTopics = topics;
        },
      });
  }

  isSubscribedToTopic(topicId: string) {
    return this.userTopics.some((topic) => topic.id === topicId);
  }
}
