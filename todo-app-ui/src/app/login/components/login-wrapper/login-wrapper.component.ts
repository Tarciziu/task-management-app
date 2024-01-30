import { Component } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Select, Store } from '@ngxs/store';
import { AuthState } from '../../../shared/redux/auth/auth.state';
import { Observable } from 'rxjs';
import { OperationStatus } from '../../../shared/enums';
import { BaseComponent } from '../../../shared/components/base/base.component';
import { hasFormControlError } from '../../../shared/util/form.util';
import { Login } from '../../../shared/redux/auth/auth.actions';

interface LoginForm {
  email: FormControl<string>;
  password: FormControl<string>;
}

@Component({
  selector: 'app-login-wrapper',
  templateUrl: './login-wrapper.component.html',
  styleUrl: './login-wrapper.component.scss',
})
export class LoginWrapperComponent extends BaseComponent {
  loginForm: FormGroup = new FormGroup<LoginForm>({
    email: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required],
    }),
    password: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required],
    }),
  });

  protected readonly hasFormControlError = hasFormControlError;

  @Select(AuthState.getLoginOperationStatus)
  loginOperationStatus$!: Observable<OperationStatus>;

  constructor(private store: Store) {
    super();
  }

  login(): void {
    if (this.loginForm.valid) {
      this.store.dispatch(
        new Login({
          email: this.loginForm.value.email,
          password: this.loginForm.value.password,
        })
      );
    }
  }

  protected readonly OperationStatus = OperationStatus;
}
