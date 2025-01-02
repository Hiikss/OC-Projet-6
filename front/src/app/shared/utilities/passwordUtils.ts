import { FormGroup } from '@angular/forms';

/**
 * Form field validator for multiple regex patterns
 * @param patterns
 */
export function multiplePatternValidator(patterns: { pattern: RegExp; errorKey: string }[]) {
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

/**
 * Password matching validator between password field and confirmation field
 * @param form
 */
export function passwordMatchValidator(form: FormGroup) {
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
