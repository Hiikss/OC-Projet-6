import { Component, OnInit } from '@angular/core';
import { Button } from 'primeng/button';
import { PostService } from '../../core/services/post/post.service';
import { Post } from '../../core/interfaces/post.interface';
import { DatePipe, NgForOf, NgIf } from '@angular/common';
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
import { ProgressSpinner } from 'primeng/progressspinner';

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
    NgIf,
    ProgressSpinner,
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss',
})
export class HomeComponent implements OnInit {
  private fetching = false;

  isFilterDrawerOpen = false;
  posts: Post[] = [];
  userTopics: Topic[] = [];
  selectedTopics: Topic[] = [];
  page = 1;
  size = 10;
  sortDir: string = 'desc';
  sortAsc: boolean = false;
  isLoading: boolean = true;

  constructor(
    private postService: PostService,
    private topicService: TopicService
  ) {}

  ngOnInit(): void {
    this.loadTopics();
  }

  /**
   * Get authenticated user's topics and load posts
   * @see loadPosts
   */
  async loadTopics() {
    this.userTopics = await firstValueFrom(this.topicService.getUserTopics());
    this.loadPosts();
  }

  /**
   * Get posts by pagination and filters
   * @param reset reset pagination
   */
  loadPosts(reset?: boolean) {
    if (this.fetching) return;

    this.fetching = true;

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
          this.fetching = false;
          this.isLoading = false;
        },
        error: () => {
          this.fetching = false;
          this.isLoading = false;
        },
      });
  }

  /**
   * Reset pagination and load posts with new filters
   * @see loadPosts
   */
  loadPostsByFilters() {
    this.page = 1;
    this.loadPosts(true);
  }
}
