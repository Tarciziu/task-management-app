import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TasksOverviewWrapperComponent } from './components/tasks-overview-wrapper/tasks-overview-wrapper.component';
import { TasksOverviewRoutingModule } from './tasks-overview.routing.module';
import { SharedModule } from '../shared/shared.module';
import { TasksOverviewFilterWrapperComponent } from './components/tasks-overview-filter-wrapper/tasks-overview-filter-wrapper.component';
import { TasksOverviewListComponent } from './components/tasks-overview-list/tasks-overview-list.component';
import { NgxsModule } from '@ngxs/store';
import { TasksOverviewState } from './redux/tasks-overview/tasks-overview.state';

@NgModule({
  declarations: [TasksOverviewWrapperComponent],
  imports: [
    CommonModule,
    TasksOverviewRoutingModule,
    SharedModule,
    TasksOverviewFilterWrapperComponent,
    TasksOverviewListComponent,

    NgxsModule.forFeature([TasksOverviewState]),
  ],
})
export class TasksOverviewModule {}
