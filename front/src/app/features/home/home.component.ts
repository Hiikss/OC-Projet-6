import { Component, OnInit } from '@angular/core';
import { Button } from 'primeng/button';
import { PostService } from '../../core/services/post/post.service';
import { Post } from '../../core/interfaces/post.interface';
import { DatePipe, NgForOf } from '@angular/common';
import { InfiniteScrollDirective } from 'ngx-infinite-scroll';
import { Card } from 'primeng/card';
import { RouterLink } from '@angular/router';
import { DropdownModule } from 'primeng/dropdown';
import { TopicService } from '../../core/services/topic/topic.service';
import { Topic } from '../../core/interfaces/topic.interface';
import { firstValueFrom } from 'rxjs';
import { Drawer } from 'primeng/drawer';
import { RadioButton } from 'primeng/radiobutton';
import { FormsModule } from '@angular/forms';
import { Divider } from 'primeng/divider';
import { Checkbox } from 'primeng/checkbox';
import { ToggleButton } from 'primeng/togglebutton';
import { MultiSelect } from 'primeng/multiselect';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    Button,
    NgForOf,
    InfiniteScrollDirective,
    Card,
    DatePipe,
    RouterLink,
    DropdownModule,
    Drawer,
    RadioButton,
    FormsModule,
    Divider,
    Checkbox,
    ToggleButton,
    MultiSelect,
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss',
})
export class HomeComponent implements OnInit {
  isFilterDrawerOpen = false;
  posts: Post[] = [];
  userTopics: Topic[] = [];
  selectedTopics: Topic[] = [];
  page = 1;
  size = 10;
  loading = false;
  sortDir: string = 'desc';
  sortAsc: boolean = false;

  constructor(
    private postService: PostService,
    private topicService: TopicService
  ) {
  }

  ngOnInit(): void {
    this.loadTopics();
  }

  async loadTopics() {
    this.userTopics = await firstValueFrom(this.topicService.getUserTopics());
    for(let i=0; i<30; i++) {
      const topic: Topic = {
        id: 'id',
        title: 'title' + i,
        description: 'description',
      }
      this.userTopics.push(topic)
    }
    this.loadPosts();
  }

  loadPosts(reset?: boolean) {
    if (this.loading) return;

    this.loading = true;

    this.postService
      .getPosts(
        this.page,
        this.size,
        this.selectedTopics.length > 0 ? this.selectedTopics : this.userTopics,
        this.sortAsc ? 'asc' : 'desc'
      )
      .subscribe({
        next: (posts) => {
          if (reset) {
            this.posts = posts;
          } else {
            this.posts = [...this.posts, ...posts];
          }
          this.page++;
          this.loading = false;
        },
        error: () => {
          this.loading = false;
        },
      });
  }

  loadPostsByFilters() {
    this.page = 1;
    this.loadPosts(true);
  }

  onDrawerSortingChange() {}
}
