import { Component, OnDestroy } from '@angular/core';
import { Button } from 'primeng/button';
import { Card } from 'primeng/card';
import {
  FormBuilder,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { InputText } from 'primeng/inputtext';
import { Message } from 'primeng/message';
import { NgClass, NgIf } from '@angular/common';
import { Password } from 'primeng/password';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../core/services/auth/auth.service';
import { Divider } from 'primeng/divider';
import { MessageService, PrimeTemplate } from 'primeng/api';
import { Subject, takeUntil } from 'rxjs';
import { multiplePatternValidator, passwordMatchValidator } from '../../../shared/utilities/passwordUtils';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [
    Button,
    Card,
    FormsModule,
    InputText,
    Message,
    NgIf,
    Password,
    ReactiveFormsModule,
    RouterLink,
    Divider,
    PrimeTemplate,
    NgClass,
  ],
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss',
})
export class RegisterComponent implements OnDestroy {
  registerForm: FormGroup;
  authError: boolean = false;
  errorMessage: string = '';

  private destroy$ = new Subject<void>();

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private messageService: MessageService
  ) {
    this.registerForm = this.fb.group(
      {
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
    this.registerForm
      .get('password')
      ?.valueChanges.pipe(takeUntil(this.destroy$))
      .subscribe(() => {
        this.registerForm.get('confirmPassword')?.updateValueAndValidity();
      });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  /**
   * Check if register form has error key in its errors
   * @param errorKey
   */
  isRequirementMet(errorKey: string): boolean {
    return (
      this.registerForm.get('password')?.errors?.[errorKey] === undefined &&
      this.registerForm.get('password')?.value
    );
  }

  /**
   * Submit register form and redirect to homepage if success
   */
  onSubmit() {
    if (this.registerForm.valid) {
      const { email, username, password } = this.registerForm.value;
      this.authService.register({ email, username, password }).subscribe({
        next: () => {
          this.messageService.add({
            severity: 'success',
            summary: 'Succès',
            detail: 'Le compte à bien été créé',
          });
          this.router.navigate(['/home']);
        },
        error: (err) => {
          this.errorMessage = this.getErrorMessage(err);
          this.authError = true;
        },
      });
    }
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
    return 'Une erreur est survenue';
  }
}
