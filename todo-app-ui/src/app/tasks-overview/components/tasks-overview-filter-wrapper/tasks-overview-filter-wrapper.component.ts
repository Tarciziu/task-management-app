import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import {
  allTaskPriorities,
  allTaskStatuses,
  TaskPriority,
  TaskStatus,
} from '../../../shared/enums';
import { SharedModule } from '../../../shared/shared.module';
import { NgForOf, NgIf } from '@angular/common';
import { MatSelectModule } from '@angular/material/select';
import { TasksFilterRequestModel } from '../../models/tasks-filter-request.model';
import { takeUntil } from 'rxjs';
import { BaseComponent } from '../../../shared/components/base/base.component';

interface TaskOverviewFilterForm {
  name: FormControl<string | null>;
  priority: FormControl<TaskPriority>;
  status: FormControl<TaskStatus>;
}

@Component({
  selector: 'app-tasks-overview-filter-wrapper',
  standalone: true,
  imports: [SharedModule, MatSelectModule],
  templateUrl: './tasks-overview-filter-wrapper.component.html',
  styleUrl: './tasks-overview-filter-wrapper.component.scss',
})
export class TasksOverviewFilterWrapperComponent
  extends BaseComponent
  implements OnInit
{
  filterForm: FormGroup = new FormGroup<TaskOverviewFilterForm>({
    name: new FormControl('', {
      nonNullable: false,
    }),
    priority: new FormControl(TaskPriority.ALL, {
      nonNullable: true,
      validators: [Validators.required],
    }),
    status: new FormControl(TaskStatus.ALL, {
      nonNullable: true,
      validators: [Validators.required],
    }),
  });

  allTaskPriorities = allTaskPriorities;
  allTaskStatuses = allTaskStatuses;

  @Output()
  filterChanged: EventEmitter<Partial<TasksFilterRequestModel>> =
    new EventEmitter();

  ngOnInit(): void {
    this.watchFormChanges();
  }

  private watchFormChanges() {
    this.filterForm.valueChanges
      .pipe(takeUntil(this.unsubscribe$))
      .subscribe(value => {
        this.filterChanged.emit({ ...value });
      });
  }
}
