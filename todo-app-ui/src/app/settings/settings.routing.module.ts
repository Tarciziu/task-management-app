import { RouterModule, Routes } from '@angular/router';
import { NgModule } from '@angular/core';
import { SettingsWrapperComponent } from './components/settings-wrapper/settings-wrapper.component';

const routes: Routes = [
  {
    path: '',
    component: SettingsWrapperComponent,
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class SettingsRoutingModule {}
