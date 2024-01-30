import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SignUpRoutingModule } from './sign-up.routing.module';
import { SignUpComponent } from './components/sign-up/sign-up.component';
import { SharedModule } from '../shared/shared.module';
import { NgxsModule } from '@ngxs/store';
import { RegisterState } from './redux/register/register.state';

@NgModule({
  declarations: [SignUpComponent],
  imports: [
    CommonModule,
    SignUpRoutingModule,
    SharedModule,
    NgxsModule.forFeature([RegisterState]),
  ],
})
export class SignUpModule {}
