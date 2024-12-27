import { Component, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { Divider } from 'primeng/divider';
import { AuthService } from '../../../core/services/auth/auth.service';
import { TopicService } from '../../../core/services/topic/topic.service';
import { Topic } from '../../../core/interfaces/topic.interface';
import { Card } from 'primeng/card';
import { NgForOf, NgIf } from '@angular/common';
import { Button } from 'primeng/button';
import { InputText } from 'primeng/inputtext';
import { Message } from 'primeng/message';
import { FloatLabel } from 'primeng/floatlabel';
import { AuthenticatedUser } from '../../../core/interfaces/authenticatedUser.interface';
import {
  multiplePatternValidator,
  passwordMatchValidator,
} from '../../../shared/utilities/passwordUtils';
import { UserService } from '../../../core/services/user/user.service';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [
    Divider,
    Card,
    NgForOf,
    Button,
    InputText,
    Message,
    NgIf,
    ReactiveFormsModule,
    FloatLabel,
  ],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.scss',
})
export class ProfileComponent implements OnInit {
  userForm: FormGroup;
  passwordForm: FormGroup;
  userTopics: Topic[] = [];

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private topicService: TopicService,
    private userService: UserService,
    private messageService: MessageService
  ) {
    this.userForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      username: [
        '',
        [
          Validators.required,
          Validators.minLength(6),
          Validators.maxLength(30),
          Validators.pattern('^\\w*$'),
        ],
      ],
    });

    this.passwordForm = this.fb.group(
      {
        password: [
          '',
          [
            Validators.required,
            multiplePatternValidator([
              { pattern: /[a-z]/, errorKey: 'lowercase' },
              { pattern: /[A-Z]/, errorKey: 'uppercase' },
              { pattern: /\d/, errorKey: 'digit' },
              { pattern: /[@#$%^&+=!?*\-]/, errorKey: 'special' },
              { pattern: /^[\s\S]{8,64}$/, errorKey: 'length' },
            ]),
          ],
        ],
        confirmPassword: ['', []],
      },
      {
        validators: passwordMatchValidator,
      }
    );
  }

  ngOnInit(): void {
    this.authService
      .getUserSession()
      .subscribe((user: AuthenticatedUser | null) => {
        if (user) {
          this.userForm.setValue({
            email: user.email,
            username: user.username,
          });
        }
      });
    this.topicService.getUserTopics().subscribe({
      next: (topics) => {
        this.userTopics = topics;
      },
    });
  }

  updateProfile() {
    if (this.userForm.valid) {
      const { email, username } = this.userForm.value;
      this.userService.updateUser({ email, username }).subscribe({
        next: (user) => {
          this.authService.updateUserSession({ email: user.email, username: user.username });
          this.messageService.add({
            severity: 'success',
            summary: 'Succès',
            detail: 'Votre profil a bien été mis à jour',
          });
        },
        error: () => {
          this.messageService.add({
            severity: 'error',
            summary: 'Erreur',
            detail: 'Une erreur est survenue lors de la mise à jour du profil',
          });
        },
      });
    }
  }

  unsubscribe(topicId: string) {
    this.topicService.removeTopicFromUser(topicId).subscribe({
      next: (topics) => {
        this.userTopics = topics;
      },
      error: (err) => {
        console.log(err);
      },
    });
  }
}
