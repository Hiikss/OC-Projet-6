import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth/auth.service';
import { map } from 'rxjs';

/**
 * Redirect the user to the homepage if the user is authenticated
 * @param route
 * @param state
 */
export const redirectGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  return authService.isLoggedIn().pipe(
    map((isLoggedIn) => {
      if (isLoggedIn) {
        router.navigate(['/home']);
        return false;
      }
      return true;
    }),
  );

};
