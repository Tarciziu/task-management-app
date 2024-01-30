import { TaskCreationRequestModel } from './task-creation-request.model';

export interface TaskItemResponseModel extends TaskCreationRequestModel {
  id: string;
  createdAt: Date;
  updatedAt: Date;
  deadline?: Date;
}
