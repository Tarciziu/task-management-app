import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Select, Store } from '@ngxs/store';
import { Observable } from 'rxjs';
import { BaseComponent } from '../../../shared/components/base/base.component';
import { hasFormControlError } from '../../../shared/util/form.util';
import { OperationStatus } from '../../../shared/enums';
import { Register } from '../../redux/register/register.actions';
import { StateResetAll } from 'ngxs-reset-plugin';
import { RegisterState } from '../../redux/register/register.state';

interface RegisterForm {
  firstName: FormControl<string>;
  lastName: FormControl<string>;
  email: FormControl<string>;
  password: FormControl<string>;
}

@Component({
  selector: 'app-sign-up',
  templateUrl: './sign-up.component.html',
  styleUrl: './sign-up.component.scss',
})
export class SignUpComponent extends BaseComponent implements OnInit {
  registerForm: FormGroup = new FormGroup<RegisterForm>({
    firstName: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required],
    }),
    lastName: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required],
    }),
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
  protected readonly OperationStatus = OperationStatus;

  @Select(RegisterState.getOperation)
  registerOperationStatus$!: Observable<OperationStatus>;

  constructor(private store: Store) {
    super();
  }

  ngOnInit(): void {
    this.store.dispatch(new StateResetAll());
  }

  register(): void {
    if (this.registerForm.valid) {
      this.store.dispatch(
        new Register({
          firstName: this.registerForm.value.firstName,
          lastName: this.registerForm.value.lastName,
          email: this.registerForm.value.email,
          password: this.registerForm.value.password,
        })
      );
    }
  }
}
