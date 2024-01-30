import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SettingsWrapperComponent } from './components/settings-wrapper/settings-wrapper.component';
import { SettingsRoutingModule } from './settings.routing.module';
import { SharedModule } from '../shared/shared.module';
import { NgxsModule } from '@ngxs/store';
import { SettingsState } from './redux/settings/settings.state';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatCardModule } from '@angular/material/card';

@NgModule({
  declarations: [SettingsWrapperComponent],
  imports: [
    CommonModule,
    SettingsRoutingModule,
    SharedModule,
    NgxsModule.forFeature([SettingsState]),
    MatSlideToggleModule,
    MatCardModule,
  ],
})
export class SettingsModule {}
