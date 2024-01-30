import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { TasksFilterRequestModel } from '../models/tasks-filter-request.model';
import { Observable } from 'rxjs';
import { PagingResponse } from '../../shared/models/paging.model';
import { TaskItemResponseModel } from '../../shared/models/task/task-item-response.model';
import { HttpUtil } from '../../shared/util/http.util';
import { environment } from '../../../environment/environment';

@Injectable({ providedIn: 'any' })
export class TaskService {
  constructor(private httpClient: HttpClient) {}

  public getTasks(
    taskFilter: TasksFilterRequestModel
  ): Observable<PagingResponse<TaskItemResponseModel>> {
    return this.httpClient.get<PagingResponse<TaskItemResponseModel>>(
      `${environment.BASE_API_URL}${environment.TASK_MGMT_SERVICE_API_URL}/tasks`,
      {
        params: HttpUtil.buildUrlParams(taskFilter),
      }
    );
  }

  public deleteTask(taskId: string) {
    return this.httpClient.delete(
      `${environment.BASE_API_URL}${environment.TASK_MGMT_SERVICE_API_URL}/tasks/${taskId}`
    );
  }
}
