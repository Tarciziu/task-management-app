import { OperationStatus } from '../../../shared/enums';
import { Action, Selector, State, StateContext } from '@ngxs/store';
import { Injectable } from '@angular/core';
import { BaseState } from '../../../shared/redux/base.state';
import { TaskItemResponseModel } from '../../../shared/models/task/task-item-response.model';
import { TaskAddEditService } from '../../services/task-add-edit.service';
import { AddTask, FetchTask, UpdateTask } from './add-edit-task.actions';
import { catchError, of, tap } from 'rxjs';

export interface AddEditTaskStateModel {
  addEditTaskOperationStatus?: OperationStatus;
  getExistingTaskOperationStatus?: OperationStatus;
  existingTask?: TaskItemResponseModel;
}

const defaults: AddEditTaskStateModel = {
  addEditTaskOperationStatus: OperationStatus.IDLE,
};

@State<AddEditTaskStateModel>({
  name: 'AddEditTask',
  defaults,
})
@Injectable()
export class AddEditTaskState extends BaseState {
  constructor(private readonly taskAddEditService: TaskAddEditService) {
    super();
  }

  // selectors
  @Selector()
  static getAddEditTaskOperationStatus(
    state: AddEditTaskStateModel
  ): OperationStatus | undefined {
    return state.addEditTaskOperationStatus;
  }

  @Selector()
  static getGetExistingTaskOperationStatus(
    state: AddEditTaskStateModel
  ): OperationStatus | undefined {
    return state.getExistingTaskOperationStatus;
  }

  @Selector()
  static getExistingTask(
    state: AddEditTaskStateModel
  ): TaskItemResponseModel | undefined {
    return state.existingTask;
  }

  // actions
  @Action(AddTask)
  addTask(
    { patchState }: StateContext<AddEditTaskStateModel>,
    { data }: AddTask
  ) {
    patchState({
      addEditTaskOperationStatus: OperationStatus.LOADING,
    });

    return this.taskAddEditService.addTask(data).pipe(
      tap(response => {
        patchState({
          addEditTaskOperationStatus: OperationStatus.SUCCESS,
        });

        this.notificationService.showErrorMessage(
          'notifications.success.add-task'
        );
      }),
      catchError(err => {
        patchState({
          addEditTaskOperationStatus: OperationStatus.ERROR,
        });
        this.notificationService.showErrorMessage(
          'notifications.error.add-task'
        );
        return of(err);
      })
    );
  }

  @Action(FetchTask)
  getTask(
    { patchState }: StateContext<AddEditTaskStateModel>,
    { id }: FetchTask
  ) {
    patchState({
      getExistingTaskOperationStatus: OperationStatus.LOADING,
    });

    return this.taskAddEditService.getTask(id).pipe(
      tap(response => {
        patchState({
          getExistingTaskOperationStatus: OperationStatus.SUCCESS,
          existingTask: response,
        });
      }),
      catchError(err => {
        patchState({
          getExistingTaskOperationStatus: OperationStatus.ERROR,
        });
        return of(err);
      })
    );
  }

  @Action(UpdateTask)
  updateTask(
    { patchState }: StateContext<AddEditTaskStateModel>,
    { id, data }: UpdateTask
  ) {
    patchState({
      addEditTaskOperationStatus: OperationStatus.LOADING,
    });

    return this.taskAddEditService.updateTask(id, data).pipe(
      tap(response => {
        patchState({
          addEditTaskOperationStatus: OperationStatus.SUCCESS,
        });

        this.notificationService.showErrorMessage(
          'notifications.success.update-task'
        );
      }),
      catchError(err => {
        patchState({
          addEditTaskOperationStatus: OperationStatus.ERROR,
        });
        this.notificationService.showErrorMessage(
          'notifications.error.update-task'
        );
        return of(err);
      })
    );
  }
}
