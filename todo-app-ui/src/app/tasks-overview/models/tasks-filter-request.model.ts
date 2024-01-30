import { TaskPriority, TaskStatus } from '../../shared/enums';
import { PagingRequest } from '../../shared/models/paging.model';

export interface TasksFilterRequestModel extends PagingRequest {
  name?: string;
  priority?: TaskPriority;
  status?: TaskStatus;
}
