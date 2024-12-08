import { Component } from '@angular/core';
import { AuthService } from '../../core/services/auth.service';
import { Observable } from 'rxjs';
import { AuthenticatedUser } from '../../core/interfaces/authenticatedUser.interface';
import { AsyncPipe, NgIf } from '@angular/common';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    NgIf,
    AsyncPipe,
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent {
  userSession$: Observable<AuthenticatedUser | null>;

  constructor(private authService: AuthService) {
    this.userSession$ = this.authService.getUserSession();
  }

  logOut() {
    this.authService.logout();
  }
}
