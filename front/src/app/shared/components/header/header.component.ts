import { Component, OnInit } from '@angular/core';
import { AsyncPipe, NgIf, NgOptimizedImage } from '@angular/common';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { Drawer } from 'primeng/drawer';
import { Button } from 'primeng/button';
import { AuthService } from '../../../core/services/auth/auth.service';
import { Observable } from 'rxjs';
import { AuthenticatedUser } from '../../../core/interfaces/authenticatedUser.interface';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [
    NgOptimizedImage,
    RouterLink,
    RouterLinkActive,
    Drawer,
    Button,
    AsyncPipe,
    NgIf,
  ],
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss',
})
export class HeaderComponent implements OnInit {
  isDrawerOpen: boolean = false;
  userSession$!: Observable<AuthenticatedUser | null>;

  constructor(private authService: AuthService) {}

  ngOnInit(): void {
    this.userSession$ = this.authService.getUserSession();
  }

  openDrawer() {
    this.isDrawerOpen = true;
  }

  logout() {
    this.authService.logout();
  }
}
