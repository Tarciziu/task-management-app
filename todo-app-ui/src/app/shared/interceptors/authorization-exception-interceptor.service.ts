import { Injectable } from '@angular/core';
import {
  HttpErrorResponse,
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest,
} from '@angular/common/http';
import { catchError, Observable, throwError } from 'rxjs';
import { Store } from '@ngxs/store';
import { Logout } from '../redux/auth/auth.actions';
import { environment } from '../../../environment/environment';

interface RestApiDefinition {
  method: 'GET' | 'POST' | 'PUT' | 'DELETE';
  path: string;
  matchType: 'endsWith' | 'startsWith' | 'contains' | 'exact';
}

@Injectable()
export class AuthorizationExceptionInterceptor implements HttpInterceptor {
  private readonly APIS_TO_IGNORE: RestApiDefinition[] = [
    {
      method: 'POST',
      path: `${environment.BASE_API_URL}${environment.USER_SERVICE_API_URL}/auth/login`,
      matchType: 'exact',
    },
    {
      method: 'GET',
      path: `${environment.BASE_API_URL}${environment.USER_SERVICE_API_URL}/auth/me`,
      matchType: 'exact',
    },
    {
      method: 'POST',
      path: `${environment.BASE_API_URL}/auth/logout`,
      matchType: 'exact',
    },
  ];

  constructor(private store: Store) {}

  intercept(
    req: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    return next.handle(req).pipe(
      catchError((err: HttpErrorResponse) => {
        if (
          !this.shouldRequestBeIgnored(req) &&
          (err.status === 401 || err.status === 403)
        ) {
          this.store.dispatch(new Logout());
        }

        return throwError(() => err);
      })
    );
  }

  private shouldRequestBeIgnored(req: HttpRequest<any>): boolean {
    return this.APIS_TO_IGNORE.some((apiDefinition: RestApiDefinition) => {
      const methodMatched: boolean = apiDefinition.method === req.method;
      let pathMatched: boolean = false;

      switch (apiDefinition.matchType) {
        case 'startsWith':
          pathMatched = req.url.startsWith(apiDefinition.path);
          break;
        case 'endsWith':
          pathMatched = req.url.endsWith(apiDefinition.path);
          break;
        case 'contains':
          pathMatched = req.url.includes(apiDefinition.path);
          break;
        case 'exact':
          pathMatched = req.url === apiDefinition.path;
          break;
      }

      return methodMatched && pathMatched;
    });
  }
}
