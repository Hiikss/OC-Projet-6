import { Component, OnDestroy, OnInit } from '@angular/core';
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
import { NgClass, NgForOf, NgIf } from '@angular/common';
import { Button } from 'primeng/button';
import { InputText } from 'primeng/inputtext';
import { Message } from 'primeng/message';
import {
  multiplePatternValidator,
  passwordMatchValidator,
} from '../../../shared/utilities/passwordUtils';
import { UserService } from '../../../core/services/user/user.service';
import { MessageService, PrimeTemplate } from 'primeng/api';
import { Subject, take, takeUntil } from 'rxjs';
import { Password } from 'primeng/password';

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
    Password,
    PrimeTemplate,
    NgClass,
  ],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.scss',
})
export class ProfileComponent implements OnInit, OnDestroy {
  userForm: FormGroup;
  passwordForm: FormGroup;
  userTopics: Topic[] = [];

  private destroy$ = new Subject<void>();

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
    this.passwordForm
      .get('password')
      ?.valueChanges.pipe(takeUntil(this.destroy$))
      .subscribe(() => {
        this.passwordForm.get('confirmPassword')?.updateValueAndValidity();
      });
  }

  ngOnInit(): void {
    this.authService
      .getUserSession()
      .pipe(take(1))
      .subscribe((user) => {
        if (user) {
          this.userForm.setValue(
            {
              email: user.email,
              username: user.username,
            },
            { emitEvent: false }
          );
        }
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

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  /**
   * Update user's profile
   */
  updateProfile() {
    if (this.userForm.valid) {
      const { email, username } = this.userForm.value;
      this.userService.updateUser({ email, username }).subscribe({
        next: () => {
          this.messageService.add({
            severity: 'success',
            summary: 'Succès',
            detail: 'Votre profil a bien été mis à jour',
          });
        },
        error: (err) => {
          const errorMessage = this.getErrorMessage(err);
          this.messageService.add({
            severity: 'error',
            summary: 'Erreur',
            detail: errorMessage,
          });
        },
      });
    }
  }

  /**
   * Update user's password
   */
  updatePassword() {
    if (this.passwordForm.valid) {
      const { password } = this.passwordForm.value;
      this.userService.updateUser({ password }).subscribe({
        next: () => {
          this.messageService.add({
            severity: 'success',
            summary: 'Succès',
            detail: 'Le mot de passe a bien été mis à jour',
          });
        },
        error: () => {
          this.messageService.add({
            severity: 'error',
            summary: 'Erreur',
            detail:
              'Une erreur est survenue lors de la mise à jour du mot de passe',
          });
        },
      });
    }
  }

  /**
   * Unsubscribe user from topic
   * @param topicId
   */
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

  /**
   * Check if password form contains error with error key
   * @param errorKey
   */
  isRequirementMet(errorKey: string): boolean {
    return (
      this.passwordForm.get('password')?.errors?.[errorKey] === undefined &&
      this.passwordForm.get('password')?.value
    );
  }

  /**
   * Get error message based on error response
   * @param err
   * @private
   */
  private getErrorMessage(err: any): string {
    if (err.status === 409 && err.error?.message) {
      const message = err.error.message.toLowerCase();
      if (message.includes('email')) {
        return 'Cet email est déjà utilisé';
      } else if (message.includes('username')) {
        return "Ce nom d'utilisateur est déjà utilisé";
      }
    }
    return 'Une erreur est survenue lors de la mise à jour du profil';
  }
}
