import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AddEditTaskWrapperComponent } from './components/add-edit-task-wrapper/add-edit-task-wrapper.component';

const routes: Routes = [
  {
    path: '',
    component: AddEditTaskWrapperComponent,
  },
  {
    path: ':id',
    component: AddEditTaskWrapperComponent,
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class AddEditTaskRoutingModule {}
