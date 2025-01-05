import { HttpInterceptorFn } from '@angular/common/http';
import { environment } from '../../../environments/environment';

/**
 * Set the api base url when requesting api
 * @param req
 * @param next
 */
export const apiInterceptor: HttpInterceptorFn = (req, next) => {

  if (!req.url.startsWith('http')) {
    const apiReq = req.clone({
      url: `${environment.apiUrl}${req.url}`,
    });
    return next(apiReq);
  }
  return next(req);
};
