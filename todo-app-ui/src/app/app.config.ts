import { ApplicationConfig, importProvidersFrom } from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import { TranslateLoader, TranslateModule } from '@ngx-translate/core';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NgxsResetPluginModule } from 'ngxs-reset-plugin';
import { NgxsRouterPluginModule } from '@ngxs/router-plugin';
import { environment } from '../environment/environment';
import { NgxsLoggerPluginModule } from '@ngxs/logger-plugin';
import { NgxsReduxDevtoolsPluginModule } from '@ngxs/devtools-plugin';
import { AuthState } from './shared/redux/auth/auth.state';
import { NgxsModule } from '@ngxs/store';
import { SharedModule } from './shared/shared.module';

const provideTranslation = () => ({
  loader: {
    provide: TranslateLoader,
    useFactory: (httpClient: HttpClient) => new TranslateHttpLoader(httpClient),
    deps: [HttpClient],
  },
});

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    importProvidersFrom([
      BrowserAnimationsModule,
      HttpClientModule,
      SharedModule,
      TranslateModule.forRoot(provideTranslation()),
      NgxsModule.forRoot([AuthState], {
        developmentMode: !environment.production,
      }),
      NgxsReduxDevtoolsPluginModule.forRoot({
        disabled: environment.production,
      }),
      NgxsLoggerPluginModule.forRoot({ disabled: environment.production }),
      NgxsRouterPluginModule.forRoot(),
      NgxsResetPluginModule.forRoot(),
    ]),
  ],
};
