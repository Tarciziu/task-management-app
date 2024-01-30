import {
  AfterViewInit,
  Component,
  EventEmitter,
  Input,
  Output,
  ViewChild,
} from '@angular/core';
import { BaseComponent } from '../../../shared/components/base/base.component';
import {
  DEFAULT_ITEMS_PER_PAGE,
  DEFAULT_PAGINATION_OPTIONS,
} from '../../../shared/models/general.constants';
import {
  MatPaginator,
  MatPaginatorModule,
  PageEvent,
} from '@angular/material/paginator';
import { PagingMetadata } from '../../../shared/models/paging.model';
import { tap } from 'rxjs';
import { TaskItemResponseModel } from '../../../shared/models/task/task-item-response.model';
import { SharedModule } from '../../../shared/shared.module';
import { MatTableModule } from '@angular/material/table';
import { CommonModule } from '@angular/common';
import { MatTooltipModule } from '@angular/material/tooltip';

@Component({
  selector: 'app-tasks-overview-list',
  standalone: true,
  imports: [
    CommonModule,
    SharedModule,
    MatTableModule,
    MatPaginatorModule,
    MatTooltipModule,
  ],
  templateUrl: './tasks-overview-list.component.html',
  styleUrl: './tasks-overview-list.component.scss',
})
export class TasksOverviewListComponent
  extends BaseComponent
  implements AfterViewInit
{
  DEFAULT_ITEMS_PER_PAGE = DEFAULT_ITEMS_PER_PAGE;
  PAGINATION_OPTIONS = DEFAULT_PAGINATION_OPTIONS;

  displayedColumns = [
    'name',
    'description',
    'priority',
    'createdAt',
    'deadline',
    'updatedAt',
    'status',
    'actions',
  ];

  @Output()
  private pageChanged = new EventEmitter<PageEvent>();

  @Output()
  private taskDeleted = new EventEmitter<string>();

  @Output()
  private taskEdited = new EventEmitter<string>();

  _data!: TaskItemResponseModel[];
  _pagingMetadata?: PagingMetadata;

  _loading!: boolean;

  @ViewChild(MatPaginator)
  paginator!: MatPaginator;

  @Input()
  set data(value: TaskItemResponseModel[]) {
    this._data = value;
  }

  @Input()
  set loading(value: boolean) {
    this._loading = value;
  }

  @Input()
  set pagingMetadata(value: PagingMetadata) {
    this._pagingMetadata = value;
  }

  ngAfterViewInit(): void {
    this.paginator.page
      .pipe(tap(info => this.pageChanged.emit(info)))
      .subscribe();
  }

  onDeleteTask(id: string) {
    this.taskDeleted.emit(id);
  }

  onEditTask(id: string) {
    this.taskEdited.emit(id);
  }
}
