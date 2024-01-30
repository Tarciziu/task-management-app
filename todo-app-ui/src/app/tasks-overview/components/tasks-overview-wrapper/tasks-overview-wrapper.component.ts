import { Component, OnInit } from '@angular/core';
import { BaseComponent } from '../../../shared/components/base/base.component';
import { TasksOverviewState } from '../../redux/tasks-overview/tasks-overview.state';
import { Observable, takeUntil } from 'rxjs';
import { Actions, ofActionCompleted, Select, Store } from '@ngxs/store';
import { PagingMetadata } from '../../../shared/models/paging.model';
import {
  DEFAULT_ITEMS_PER_PAGE,
  DEFAULT_PAGE,
} from '../../../shared/models/general.constants';
import { TaskItemResponseModel } from '../../../shared/models/task/task-item-response.model';
import { TasksFilterRequestModel } from '../../models/tasks-filter-request.model';
import {
  DeleteTask,
  FetchTasks,
} from '../../redux/tasks-overview/tasks-overview.actions';
import { PageEvent } from '@angular/material/paginator';
import {
  OperationStatus,
  TaskPriority,
  TaskStatus,
} from '../../../shared/enums';
import { ConfirmationDialogService } from '../../../shared/components/confirmation-dialog/confirmation-dialog.service';
import { pathConstants } from '../../../shared/path.constants';
import { Navigate } from '@ngxs/router-plugin';

@Component({
  selector: 'app-tasks-overview-wrapper',
  templateUrl: './tasks-overview-wrapper.component.html',
  styleUrl: './tasks-overview-wrapper.component.scss',
})
export class TasksOverviewWrapperComponent
  extends BaseComponent
  implements OnInit
{
  protected readonly OperationStatus = OperationStatus;
  protected readonly pathConstants = pathConstants;

  @Select(TasksOverviewState.getOperationStatus)
  operationStatus$!: Observable<OperationStatus>;
  operationStatus!: OperationStatus;

  @Select(TasksOverviewState.getTasks)
  private currentPageTasks$!: Observable<TaskItemResponseModel[]>;
  currentPageTasks!: TaskItemResponseModel[];

  @Select(TasksOverviewState.getPagingMetadata)
  private currentPagingMetadata$!: Observable<PagingMetadata>;
  currentPagingMetadata!: PagingMetadata;

  currentFilters: TasksFilterRequestModel = {
    page: DEFAULT_PAGE,
    pageSize: DEFAULT_ITEMS_PER_PAGE,
  };

  constructor(
    private store: Store,
    private confirmationDialogService: ConfirmationDialogService,
    private actions$: Actions
  ) {
    super();
  }

  ngOnInit(): void {
    this.watchForDataChanged();
    this.store.dispatch(new FetchTasks(this.currentFilters));
  }

  onFilterChanged($event: Partial<TasksFilterRequestModel>) {
    this.dispatchNewSearch($event, true);
  }

  onPageChanged(event: PageEvent): void {
    this.dispatchNewSearch(
      {
        page: event.pageIndex,
        pageSize: event.pageSize,
      },
      this.currentFilters?.pageSize !== event.pageSize
    );
  }

  onTaskEdited($event: string) {
    this.store.dispatch(
      new Navigate([`/${pathConstants.addEditTasks}/${$event}`])
    );
  }

  onTaskDeleted($event: string) {
    this.confirmationDialogService.openConfirmationDialog(
      {
        translateTitle: 'tasks-overview.actions.delete.dialog.title',
        translateDescription:
          'tasks-overview.actions.delete.dialog.description',
        translateOkButton: 'tasks-overview.actions.delete.dialog.ok',
        translateCancelButton: 'tasks-overview.actions.delete.dialog.cancel',
      },
      {
        confirmationFn: () => {
          this.store.dispatch(new DeleteTask($event));

          this.actions$
            .pipe(ofActionCompleted(DeleteTask), takeUntil(this.unsubscribe$))
            .subscribe(() => {
              this.dispatchNewSearch(
                {
                  page: DEFAULT_PAGE,
                  pageSize: DEFAULT_ITEMS_PER_PAGE,
                },
                true
              );
            });
        },
        cancelFn: () => {
          // action canceled
        },
      }
    );
  }

  private watchForDataChanged(): void {
    this.subscribeToDefined(
      this.currentPagingMetadata$,
      (data: PagingMetadata) => (this.currentPagingMetadata = data)
    );

    this.subscribeToDefined(
      this.currentPageTasks$,
      (data: TaskItemResponseModel[]) => (this.currentPageTasks = data)
    );

    this.subscribeToDefined(
      this.operationStatus$,
      (operationStatus: OperationStatus) =>
        (this.operationStatus = operationStatus)
    );
  }

  private dispatchNewSearch(
    event: Partial<TasksFilterRequestModel>,
    forceReset = false
  ): void {
    this.currentFilters = {
      ...this.currentFilters,
      ...event,
    };

    if (this.currentFilters.priority === TaskPriority.ALL) {
      delete this.currentFilters.priority;
    }

    if (this.currentFilters.status === TaskStatus.ALL) {
      delete this.currentFilters.status;
    }

    this.store.dispatch(new FetchTasks(this.currentFilters, forceReset));
  }
}
