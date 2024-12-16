import { Component } from '@angular/core';
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
export class RegisterComponent {
  registerForm: FormGroup;
  authError: boolean = false;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.registerForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      username: ['', [Validators.required]],
      password: [
        '',
        [
          this.multiplePatternValidator([
            { pattern: /[a-z]/, errorKey: 'lowercase' },
            { pattern: /[A-Z]/, errorKey: 'uppercase' },
            { pattern: /\d/, errorKey: 'digit' },
            { pattern: /[@#$%^&+=!?*\-]/, errorKey: 'special' },
            { pattern: /^[\s\S]{8,64}$/, errorKey: 'length' },
          ]),
        ],
      ],
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
        error: () => {
          this.authError = true;
        },
      });
    }
  }
}
