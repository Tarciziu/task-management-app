import { TasksFilterRequestModel } from '../../models/tasks-filter-request.model';

export class FetchTasks {
  static readonly type = '[Tasks-Overview] Fetch Tasks';

  constructor(
    public filters: TasksFilterRequestModel,
    public forceReset: boolean = false
  ) {}
}

export class DeleteTask {
  static readonly type = '[Tasks-Overview] Delete Task';

  constructor(public id: string) {}
}
