import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthorizationExceptionInterceptor } from './interceptors/authorization-exception-interceptor.service';
import { HTTP_INTERCEPTORS, HttpClient } from '@angular/common/http';
import { NotificationService } from './components/notification/notification.service';
import { NotificationComponent } from './components/notification/notification.component';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { ReactiveFormsModule } from '@angular/forms';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatButtonModule } from '@angular/material/button';
import { TranslateLoader, TranslateModule } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { BasePageComponent } from './components/base-page/base-page.component';
import { NavComponent } from './components/nav/nav.component';
import { ConfirmationDialogComponent } from './components/confirmation-dialog/confirmation-dialog.component';
import { MatDialogModule } from '@angular/material/dialog';

@NgModule({
  declarations: [
    NotificationComponent,
    BasePageComponent,
    ConfirmationDialogComponent,
  ],
  imports: [
    CommonModule,
    TranslateModule.forChild({
      loader: {
        provide: TranslateLoader,
        useFactory: (http: HttpClient) => new TranslateHttpLoader(http),
        deps: [HttpClient],
      },
    }),

    // angular material
    MatIconModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatProgressSpinnerModule,
    MatButtonModule,
    MatDialogModule,

    // base page
    NavComponent,
  ],
  exports: [
    NotificationComponent,
    BasePageComponent,
    NavComponent,

    TranslateModule,

    // angular modules
    MatIconModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatProgressSpinnerModule,
    MatButtonModule,
  ],
  providers: [
    NotificationService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthorizationExceptionInterceptor,
      multi: true,
    },
    ConfirmationDialogComponent,
  ],
})
export class SharedModule {}
