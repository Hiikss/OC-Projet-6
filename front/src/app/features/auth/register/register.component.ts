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
import { AuthService } from '../../../core/services/auth.service';
import { Divider } from 'primeng/divider';
import { PrimeTemplate } from 'primeng/api';
import { Subject, takeUntil } from 'rxjs';

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

  private destroy$ = new Subject<void>();

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
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
            this.multiplePatternValidator([
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
        validators: this.passwordMatchValidator,
      }
    );
    this.registerForm
      .get('password')
      ?.valueChanges.pipe(takeUntil(this.destroy$))
      .subscribe(() => {
        this.registerForm.get('confirmPassword')?.updateValueAndValidity();
      });
  }

  multiplePatternValidator(patterns: { pattern: RegExp; errorKey: string }[]) {
    return (control: any) => {
      if (!control.value) return null;
      const errors: any = {};
      patterns.forEach(({ pattern, errorKey }) => {
        if (!pattern.test(control.value)) {
          errors[errorKey] = true;
        }
      });
      return Object.keys(errors).length ? errors : null;
    };
  }

  passwordMatchValidator(form: FormGroup) {
    const password = form.get('password');
    const confirmPassword = form.get('confirmPassword');

    if (
      password &&
      confirmPassword &&
      password.value !== confirmPassword.value
    ) {
      confirmPassword.setErrors({ passwordMismatch: true });
    }

    return null;
  }

  isRequirementMet(errorKey: string): boolean {
    return (
      this.registerForm.get('password')?.errors?.[errorKey] === undefined &&
      this.registerForm.get('password')?.value
    );
  }

  onSubmit() {
    if (this.registerForm.valid) {
      const { email, username, password } = this.registerForm.value;
      this.authService.register({ email, username, password }).subscribe({
        next: () => this.router.navigate(['/home']),
        error: (err) => {
          this.authError = true;
        },
      });
    }
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
