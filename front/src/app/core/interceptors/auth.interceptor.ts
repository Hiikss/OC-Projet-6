import { inject } from '@angular/core';
import { HttpInterceptorFn } from '@angular/common/http';
import { catchError, switchMap, throwError } from 'rxjs';
import { AuthService } from '../services/auth.service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);

  const excludedUrls = [
    '/auth/login',
    '/auth/register',
    '/auth/refresh',
  ];

  const shouldSkip = excludedUrls.some((url) => req.url.includes(url));
  if (shouldSkip) {
    return next(req);
  }

  const accessToken = authService.getAccessToken();
  if (accessToken) {
    req = req.clone({
      setHeaders: {
        Authorization: `Bearer ${accessToken}`,
      },
    });
  }

  return next(req).pipe(
    catchError((error) => {
      if (error.status === 401) {
        const refreshToken = authService.getRefreshToken();
        const userId = authService.getUserId();
        if (refreshToken && userId) {
          return authService.refreshToken({ refreshToken, userId }).pipe(
            switchMap((response) => {
              const newAccessToken = response.accessToken;
              const clonedReq = req.clone({
                setHeaders: {
                  Authorization: `Bearer ${newAccessToken}`,
                },
              });
              return next(clonedReq);
            }),
          );
        } else {
          authService.logout();
          return throwError(() => error);
        }
      }
      return throwError(() => error);
    }),
  );

};
