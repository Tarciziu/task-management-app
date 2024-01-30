import { TaskCreationRequestModel } from '../../../shared/models/task/task-creation-request.model';

export class FetchTask {
  static readonly type = '[AddEditTask] Fetch Task';

  constructor(public id: string) {}
}

export class AddTask {
  static readonly type = '[AddEditTask] Add Task';

  constructor(public data: TaskCreationRequestModel) {}
}

export class UpdateTask {
  static readonly type = '[AddEditTask] Update Task';

  constructor(
    public id: string,
    public data: TaskCreationRequestModel
  ) {}
}
