import { OperationStatus } from '../../../shared/enums';
import { Action, Selector, State, StateContext, Store } from '@ngxs/store';
import { Injectable } from '@angular/core';
import { BaseState } from '../../../shared/redux/base.state';
import { AuthStateModel } from '../../../shared/redux/auth/auth.state';
import { UserRegisterService } from '../../services/user-register.service';
import { Register } from './register.actions';
import { catchError, of, tap } from 'rxjs';
import { Navigate } from '@ngxs/router-plugin';
import { pathConstants } from '../../../shared/path.constants';

export interface RegisterStateModel {
  operation: OperationStatus;
}

const defaults: RegisterStateModel = {
  operation: OperationStatus.IDLE,
};

@State<RegisterStateModel>({
  name: 'Register',
  defaults,
})
@Injectable()
export class RegisterState extends BaseState {
  constructor(
    private readonly userRegisterService: UserRegisterService,
    private readonly store: Store
  ) {
    super();
  }

  // selectors
  @Selector()
  static getOperation(state: RegisterStateModel): OperationStatus {
    return state.operation;
  }

  // actions
  @Action(Register)
  standardLogin(
    { patchState }: StateContext<RegisterStateModel>,
    { credentials }: Register
  ) {
    patchState({
      operation: OperationStatus.LOADING,
    });
    return this.userRegisterService.register(credentials).pipe(
      tap(_ => {
        patchState({
          operation: OperationStatus.SUCCESS,
        });
        this.notificationService.showSuccessMessage(
          'notifications.success.register-account'
        );
        this.store.dispatch(new Navigate([pathConstants.login]));
      }),
      catchError(err => {
        patchState(defaults);
        this.notificationService.showErrorMessage(
          'notifications.error.register-account'
        );
        return of(err);
      })
    );
  }
}
