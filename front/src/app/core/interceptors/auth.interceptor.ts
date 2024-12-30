import { inject } from '@angular/core';
import { HttpInterceptorFn } from '@angular/common/http';
import {
  BehaviorSubject,
  catchError,
  filter,
  switchMap,
  take,
  throwError,
} from 'rxjs';
import { AuthService } from '../services/auth/auth.service';

let isRefreshing = false;
const refreshTokenSubject = new BehaviorSubject<string | null>(null);

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);

  const excludedUrls = ['/auth/login', '/auth/register', '/auth/refresh'];

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
        if (!isRefreshing) {
          isRefreshing = true;
          refreshTokenSubject.next(null);

          const refreshToken = authService.getRefreshToken();
          const userId = authService.getUserId();

          if (refreshToken && userId) {
            return authService.refreshToken({ refreshToken, userId }).pipe(
              switchMap((response) => {
                isRefreshing = false;
                refreshTokenSubject.next(response.accessToken);

                const clonedReq = req.clone({
                  setHeaders: {
                    Authorization: `Bearer ${response.accessToken}`,
                  },
                });
                return next(clonedReq);
              }),
              catchError((refreshError) => {
                isRefreshing = false;
                authService.logout();
                return throwError(() => refreshError);
              })
            );
          } else {
            authService.logout();
            return throwError(() => error);
          }
        } else {
          return refreshTokenSubject.pipe(
            filter((token) => token != null),
            take(1),
            switchMap((token) => {
              const clonedReq = req.clone({
                setHeaders: {
                  Authorization: `Bearer ${token}`,
                },
              });
              return next(clonedReq);
            })
          );
        }
      }
      return throwError(() => error);
    })
  );
};
