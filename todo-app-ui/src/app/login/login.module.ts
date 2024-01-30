import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LoginWrapperComponent } from './components/login-wrapper/login-wrapper.component';
import { LoginRoutingModule } from './login.routing.module';
import { SharedModule } from '../shared/shared.module';
import { TranslateModule } from '@ngx-translate/core';

@NgModule({
  declarations: [LoginWrapperComponent],
  imports: [CommonModule, LoginRoutingModule, SharedModule, TranslateModule],
})
export class LoginModule {}
