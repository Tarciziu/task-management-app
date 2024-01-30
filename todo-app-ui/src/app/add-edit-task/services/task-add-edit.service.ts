import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from '../../../environment/environment';
import { TaskCreationRequestModel } from '../../shared/models/task/task-creation-request.model';
import { Observable } from 'rxjs';
import { TaskItemResponseModel } from '../../shared/models/task/task-item-response.model';

@Injectable({ providedIn: 'any' })
export class TaskAddEditService {
  private readonly headers = new HttpHeaders({
    'Content-Type': 'application/json',
  });
  private readonly BASE_TASKS_URI: string = `${environment.BASE_API_URL}${environment.TASK_MGMT_SERVICE_API_URL}/tasks`;

  constructor(private httpClient: HttpClient) {}

  getTask(id: string): Observable<TaskItemResponseModel> {
    return this.httpClient.get<TaskItemResponseModel>(
      `${this.BASE_TASKS_URI}/${id}`
    );
  }

  addTask(data: TaskCreationRequestModel): Observable<void> {
    return this.httpClient.post<void>(this.BASE_TASKS_URI, data, {
      observe: 'body',
      responseType: 'json',
      headers: this.headers,
    });
  }

  updateTask(
    id: string,
    data: TaskCreationRequestModel
  ): Observable<TaskItemResponseModel> {
    return this.httpClient.put<TaskItemResponseModel>(
      `${this.BASE_TASKS_URI}/${id}`,
      data,
      {
        observe: 'body',
        responseType: 'json',
        headers: this.headers,
      }
    );
  }
}
