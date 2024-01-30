import { Injectable } from '@angular/core';
import { Store } from '@ngxs/store';
import { defaultPath, pathConstants } from '../path.constants';
import { Navigate } from '@ngxs/router-plugin';
import { AuthState } from '../redux/auth/auth.state';
import { filter, map, Observable, of, switchMap } from 'rxjs';
import { notNullOrUndefined } from '../util/object.util';

@Injectable({
  providedIn: 'root',
})
export class AuthGuard {
  constructor(private store: Store) {}

  /**
   * Check user is logged in already
   * If it is logged in, redirect it to the default path, otherwise to the login
   */
  canLoginActivate(): Observable<boolean> {
    return this.store.select(AuthState.getIsLoggedIn).pipe(
      filter(notNullOrUndefined<boolean>),
      map(value => !!value),
      switchMap((loggedIn: boolean) => {
        if (loggedIn) {
          this.store.dispatch(new Navigate([defaultPath]));
          return of(false);
        }

        return of(true);
      })
    );
  }

  /**
   * Check user is logged in and can access page
   * If it is not logged in, redirect it to the login page
   */
  canActivate(): Observable<boolean> {
    return this.store.select(AuthState.getIsLoggedIn).pipe(
      filter(notNullOrUndefined<boolean>),
      map(value => !!value),
      switchMap((loggedIn: boolean) => {
        if (loggedIn) {
          return of(true);
        }

        this.store.dispatch(new Navigate([`/${pathConstants.login}`]));
        return of(false);
      })
    );
  }
}
