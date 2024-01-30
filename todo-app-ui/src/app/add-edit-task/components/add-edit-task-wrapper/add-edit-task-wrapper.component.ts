import { Component, OnInit } from '@angular/core';
import { AsyncPipe, NgForOf, NgIf } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { SharedModule } from '../../../shared/shared.module';
import { TranslateModule } from '@ngx-translate/core';
import { Select, Store } from '@ngxs/store';
import { Observable } from 'rxjs';
import { StateReset } from 'ngxs-reset-plugin';
import { BaseComponent } from '../../../shared/components/base/base.component';
import { hasFormControlError } from '../../../shared/util/form.util';
import {
  allTaskPrioritiesForCreation,
  allTaskStatusesForCreation,
  OperationStatus,
  TaskPriority,
  TaskStatus,
} from '../../../shared/enums';
import { MatNativeDateModule, MatOptionModule } from '@angular/material/core';
import { MatSelectModule } from '@angular/material/select';
import { MatDatepickerModule } from '@angular/material/datepicker';
import {
  AddTask,
  FetchTask,
  UpdateTask,
} from '../../redux/add-edit-task/add-edit-task.actions';
import { AddEditTaskState } from '../../redux/add-edit-task/add-edit-task.state';
import { TaskCreationRequestModel } from '../../../shared/models/task/task-creation-request.model';
import { TaskItemResponseModel } from '../../../shared/models/task/task-item-response.model';
import { TasksOverviewState } from '../../../tasks-overview/redux/tasks-overview/tasks-overview.state';
import { Navigate } from '@ngxs/router-plugin';
import { defaultPath } from '../../../shared/path.constants';

interface AddTaskForm {
  name: FormControl<string>;
  description: FormControl<string>;
  priority: FormControl<TaskPriority>;
  deadline?: FormControl<Date | null>;
  status: FormControl<TaskStatus>;
}

export enum AddEditOperation {
  CREATE = 'CREATE',
  UPDATE = 'UPDATE',
}

@Component({
  selector: 'app-add-edit-task-wrapper',
  standalone: true,
  imports: [
    AsyncPipe,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatProgressSpinnerModule,
    NgIf,
    ReactiveFormsModule,
    RouterLink,
    SharedModule,
    TranslateModule,
    MatOptionModule,
    MatSelectModule,
    NgForOf,
    MatDatepickerModule,
    MatNativeDateModule,
  ],
  templateUrl: './add-edit-task-wrapper.component.html',
  styleUrl: './add-edit-task-wrapper.component.scss',
})
export class AddEditTaskWrapperComponent
  extends BaseComponent
  implements OnInit
{
  addTaskForm: FormGroup = new FormGroup<AddTaskForm>({
    name: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required],
    }),
    description: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required],
    }),
    priority: new FormControl(TaskPriority.MEDIUM, {
      nonNullable: true,
      validators: [Validators.required],
    }),
    deadline: new FormControl(null, {
      nonNullable: false,
    }),
    status: new FormControl(TaskStatus.NEW, {
      nonNullable: true,
      validators: [Validators.required],
    }),
  });

  protected readonly hasFormControlError = hasFormControlError;
  protected readonly OperationStatus = OperationStatus;
  protected readonly allTaskPriorities = allTaskPrioritiesForCreation;
  protected readonly allTaskStatuses = allTaskStatusesForCreation;
  protected readonly calendarMinDate = new Date();
  protected readonly AddEditOperation = AddEditOperation;
  protected operation: AddEditOperation = AddEditOperation.CREATE;
  protected id!: string;

  @Select(AddEditTaskState.getAddEditTaskOperationStatus)
  addEditTaskOperationStatus$!: Observable<OperationStatus>;

  @Select(AddEditTaskState.getExistingTask)
  existingTask$!: Observable<TaskItemResponseModel>;
  existingTask!: TaskItemResponseModel;

  constructor(
    private activatedRoute: ActivatedRoute,
    private store: Store
  ) {
    super();
  }

  ngOnInit(): void {
    this.watchValues();

    if (this.activatedRoute.snapshot.paramMap.has('id')) {
      this.id = this.activatedRoute.snapshot.paramMap.get('id') as string;
      this.operation = AddEditOperation.UPDATE;
      this.store.dispatch(new FetchTask(this.id));
    }
  }

  addTask(): void {
    if (this.addTaskForm.valid) {
      const info: TaskCreationRequestModel = {
        name: this.addTaskForm.value.name as string,
        description: this.addTaskForm.value.description as string,
        priority: this.addTaskForm.value.priority as TaskPriority,
        deadline: this.addTaskForm.value.deadline as Date,
        status: this.addTaskForm.value.status as TaskStatus,
      };
      this.store.dispatch(
        this.operation === AddEditOperation.CREATE
          ? new AddTask(info)
          : new UpdateTask(this.id, info)
      );
    }
  }

  private watchValues() {
    this.subscribeToDefined(
      this.addEditTaskOperationStatus$,
      (addEditTaskOperationStatus: OperationStatus) => {
        switch (addEditTaskOperationStatus) {
          case OperationStatus.SUCCESS: {
            this.store.dispatch(
              new StateReset(AddEditTaskState, TasksOverviewState)
            ); // reset old states
            this.store.dispatch(new Navigate([defaultPath]));
            break;
          }
        }
      }
    );

    this.subscribeToDefined(
      this.existingTask$,
      (existingTask: TaskItemResponseModel) => {
        this.addTaskForm.patchValue({
          name: existingTask.name,
          description: existingTask.description,
          priority: existingTask.priority,
          deadline: existingTask.deadline,
          status: existingTask.status,
        });
      }
    );
  }
}
