import { Routes } from '@angular/router';
import { LandingComponent } from './features/landing/landing.component';
import { redirectGuard } from './core/guards/redirect.guard';
import { LoginComponent } from './features/auth/login/login.component';
import { RegisterComponent } from './features/auth/register/register.component';
import { authGuard } from './core/guards/auth.guard';
import { HomeComponent } from './features/home/home.component';
import { TopicComponent } from './features/topic/topic.component';
import { PostDetailsComponent } from './features/post/post-details/post-details.component';
import { NotFoundComponent } from './features/not-found/not-found.component';

export const routes: Routes = [
  { path: '', component: LandingComponent, canActivate: [redirectGuard] },
  { path: 'login', component: LoginComponent, canActivate: [redirectGuard] },
  {
    path: 'register',
    component: RegisterComponent,
    canActivate: [redirectGuard],
  },
  { path: 'home', component: HomeComponent, canActivate: [authGuard] },
  { path: 'themes', component: TopicComponent, canActivate: [authGuard] },
  {
    path: 'post/:id',
    component: PostDetailsComponent,
    canActivate: [authGuard],
  },
  { path: '**', component: NotFoundComponent },
];
