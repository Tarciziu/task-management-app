import { TaskPriority, TaskStatus } from '../../enums';

export interface TaskCreationRequestModel {
  name: string;
  description: string;
  priority: TaskPriority;
  deadline?: Date;
  status: TaskStatus;
}
