import { OperationStatus } from '../../../shared/enums';
import {
  PagingMetadata,
  PagingResponse,
} from '../../../shared/models/paging.model';
import { TasksFilterRequestModel } from '../../models/tasks-filter-request.model';
import { NumericKeyGenericDictionary } from '../../../shared/models/generic.model';
import { TaskItemResponseModel } from '../../../shared/models/task/task-item-response.model';
import { DEFAULT_PAGE } from '../../../shared/models/general.constants';
import { Injectable } from '@angular/core';
import { Action, Selector, State, StateContext } from '@ngxs/store';
import { BaseState } from '../../../shared/redux/base.state';
import { DeleteTask, FetchTasks } from './tasks-overview.actions';
import { catchError, of, switchMap, tap, throwError } from 'rxjs';
import { TaskService } from '../../services/task.service';

export interface TasksOverviewStateModel {
  operationStatus: OperationStatus;
  filters?: TasksFilterRequestModel;
  selectedPage: number;
  dataPerPage: NumericKeyGenericDictionary<TaskItemResponseModel[]>;
  pagingMetadata?: PagingMetadata;
}

export const defaults: TasksOverviewStateModel = {
  operationStatus: OperationStatus.IDLE,
  dataPerPage: {},
  selectedPage: DEFAULT_PAGE,
};

@State<TasksOverviewStateModel>({
  name: 'TasksOverview',
  defaults,
})
@Injectable()
export class TasksOverviewState extends BaseState {
  constructor(private readonly taskService: TaskService) {
    super();
  }

  // selectors
  @Selector()
  static getOperationStatus(state: TasksOverviewStateModel): OperationStatus {
    return state.operationStatus;
  }

  @Selector()
  static getTasks(
    state: TasksOverviewStateModel
  ): TaskItemResponseModel[] | undefined {
    return state.dataPerPage[state.selectedPage];
  }

  @Selector()
  static getPagingMetadata(
    state: TasksOverviewStateModel
  ): PagingMetadata | undefined {
    return state.pagingMetadata;
  }

  // actions
  @Action(FetchTasks)
  filterLocations(
    { patchState, setState, getState }: StateContext<TasksOverviewStateModel>,
    { filters, forceReset }: FetchTasks
  ) {
    if (forceReset) {
      // reset to default + loading and fetch new data
      setState({
        ...defaults,
      });
    }

    if (getState().dataPerPage?.[filters.page]) {
      patchState({
        dataPerPage: {
          ...getState().dataPerPage,
          [filters.page]: getState().dataPerPage?.[filters.page],
        },
        selectedPage: filters.page,
      });
      return of(getState().dataPerPage?.[filters.page]);
    }

    patchState({
      operationStatus: OperationStatus.LOADING,
    });

    return this.taskService.getTasks(filters).pipe(
      tap((response: PagingResponse<TaskItemResponseModel>) => {
        patchState({
          operationStatus: OperationStatus.SUCCESS,
          filters,
          dataPerPage: {
            ...getState().dataPerPage,
            [filters.page]: response.data,
          },
          pagingMetadata: response.metadata,
          selectedPage: filters.page,
        });
      }),
      catchError(err => {
        patchState({
          operationStatus: OperationStatus.ERROR,
        });
        this.notificationService.showErrorMessage(
          'notifications.error.fetch-tasks'
        );
        return throwError(() => new Error(err));
      })
    );
  }

  // actions
  @Action(DeleteTask)
  deleteTask(
    { patchState, setState, getState }: StateContext<TasksOverviewStateModel>,
    { id }: DeleteTask
  ) {
    return this.taskService.deleteTask(id).pipe(
      tap(response => {}),
      catchError(err => {
        this.notificationService.showErrorMessage(
          'notifications.error.delete-task'
        );
        return throwError(() => new Error(err));
      })
    );
  }
}
