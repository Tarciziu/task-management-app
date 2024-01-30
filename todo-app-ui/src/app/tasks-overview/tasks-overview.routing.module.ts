import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { TasksOverviewWrapperComponent } from './components/tasks-overview-wrapper/tasks-overview-wrapper.component';

const routes: Routes = [
  {
    path: '',
    component: TasksOverviewWrapperComponent,
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class TasksOverviewRoutingModule {}
