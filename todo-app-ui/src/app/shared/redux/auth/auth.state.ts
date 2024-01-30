import { Injectable } from '@angular/core';
import { Navigate } from '@ngxs/router-plugin';
import {
  Action,
  NgxsOnInit,
  Selector,
  State,
  StateContext,
  Store,
} from '@ngxs/store';
import { catchError, finalize, of, switchMap, tap } from 'rxjs';
import { FetchAuthInfo, Login, Logout } from './auth.actions';
import { defaultPath, pathConstants } from '../../path.constants';
import { OperationStatus, UserRoleEnum } from '../../enums';
import { User } from '../../models/user.model';
import { BaseState } from '../base.state';
import { AuthService } from '../../services/auth.service';
import { StateResetAll } from 'ngxs-reset-plugin';

export interface AuthStateModel {
  loginOperationStatus: OperationStatus;
  authInfoOperationStatus: OperationStatus;
  isLoggedIn: boolean;
  user?: User;
}

const defaults: AuthStateModel = {
  loginOperationStatus: OperationStatus.IDLE,
  authInfoOperationStatus: OperationStatus.IDLE,
  isLoggedIn: false,
};

@State<AuthStateModel>({
  name: 'Auth',
  defaults,
})
@Injectable()
export class AuthState extends BaseState implements NgxsOnInit {
  constructor(
    private readonly authService: AuthService,
    private readonly store: Store
  ) {
    super();
  }

  // selectors
  @Selector()
  static getEmail(state: AuthStateModel): string | undefined {
    return state.user?.email;
  }

  @Selector()
  static getFullName(state: AuthStateModel): string | undefined {
    return state.user
      ? `${state.user.firstName} ${state.user.lastName}`
      : undefined;
  }

  @Selector()
  static getRole(state: AuthStateModel): UserRoleEnum {
    return state.user?.roles[0] ?? UserRoleEnum.ANONYMOUS;
  }

  @Selector()
  static getLoginOperationStatus(state: AuthStateModel): OperationStatus {
    return state.loginOperationStatus;
  }

  @Selector()
  static getAuthInfoOperationStatus(state: AuthStateModel): OperationStatus {
    return state.authInfoOperationStatus;
  }

  @Selector()
  static getIsLoggedIn(state: AuthStateModel): boolean | undefined {
    return state.isLoggedIn;
  }

  ngxsOnInit(ctx: StateContext<AuthStateModel>) {
    ctx.dispatch(new FetchAuthInfo());
  }

  // actions
  @Action(Login)
  standardLogin(
    { patchState }: StateContext<AuthStateModel>,
    { credentials }: Login
  ) {
    patchState({
      loginOperationStatus: OperationStatus.LOADING,
      authInfoOperationStatus: OperationStatus.LOADING,
    });
    return this.authService.login(credentials).pipe(
      switchMap(() => {
        return this.authService.getProfile();
      }),
      tap((response: User) => {
        patchState({
          loginOperationStatus: OperationStatus.SUCCESS,
          authInfoOperationStatus: OperationStatus.SUCCESS,
          isLoggedIn: true,
          user: response,
        });
        this.store.dispatch(new Navigate([defaultPath]));
      }),
      catchError(err => {
        patchState(defaults);
        this.notificationService.showErrorMessage(
          'notifications.error.invalid-credentials'
        );
        return of(err);
      })
    );
  }

  @Action(FetchAuthInfo)
  fetchAuthInfo({ patchState }: StateContext<AuthStateModel>) {
    patchState({
      authInfoOperationStatus: OperationStatus.LOADING,
    });

    return this.authService.getProfile().pipe(
      tap((response: User) => {
        patchState({
          authInfoOperationStatus: OperationStatus.SUCCESS,
          user: response,
          isLoggedIn: true,
        });
      }),
      catchError(err => {
        if (err.status === 401 || err.status === 500) {
          // invalid token
          patchState({
            authInfoOperationStatus: OperationStatus.SUCCESS,
            user: undefined,
            isLoggedIn: false,
          });
          return of();
        } else {
          patchState(defaults);
          this.notificationService.showErrorMessage(
            'notifications.error.invalid-credentials'
          );
          return of(err);
        }
      })
    );
  }

  @Action(Logout)
  logout({ setState }: StateContext<AuthStateModel>) {
    return this.authService.logout().pipe(
      finalize(() => {
        setState({
          ...defaults,
          authInfoOperationStatus: OperationStatus.IDLE,
          isLoggedIn: false,
        });

        // reset all states except AuthState (since it was cleaned up)
        this.store.dispatch(new StateResetAll(AuthState));

        // redirect to login page
        this.store.dispatch(new Navigate([pathConstants.login]));
      })
    );
  }
}
