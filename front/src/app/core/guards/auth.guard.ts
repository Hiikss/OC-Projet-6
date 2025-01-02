import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth/auth.service';
import { map } from 'rxjs';

/**
 * Redirect the user to landing page if user is not authenticated
 * @param route
 * @param state
 */
export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  return authService.isLoggedIn().pipe(
    map((isLoggedIn) => {
      if (!isLoggedIn) {
        router.navigate(['/']);
        return false;
      }
      return true;
    }),
  );

};
