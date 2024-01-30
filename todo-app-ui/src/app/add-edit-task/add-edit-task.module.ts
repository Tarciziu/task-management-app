import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AddEditTaskRoutingModule } from './add-edit-task.routing.module';
import { NgxsModule } from '@ngxs/store';
import { RegisterState } from '../sign-up/redux/register/register.state';
import { AddEditTaskState } from './redux/add-edit-task/add-edit-task.state';

@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    AddEditTaskRoutingModule,
    NgxsModule.forFeature([AddEditTaskState]),
  ],
})
export class AddEditTaskModule {}
