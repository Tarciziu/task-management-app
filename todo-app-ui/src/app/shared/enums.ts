export enum NotificationType {
  SUCCESS = 'SUCCESS',
  ERROR = 'ERROR',
}

export enum OperationStatus {
  IDLE = 'IDLE',
  LOADING = 'LOADING',
  SUCCESS = 'SUCCESS',
  ERROR = 'ERROR',
}

export enum UserRoleEnum {
  ADMIN = 'ADMIN',
  USER = 'USER',

  // not an app role
  ANONYMOUS = 'ANONYMOUS',
}

export enum TaskPriority {
  LOW = 'LOW',
  MEDIUM = 'MEDIUM',
  HIGH = 'HIGH',
  URGENT = 'URGENT',

  // not a real priority
  ALL = 'ALL',
}

export enum TaskStatus {
  NEW = 'NEW',
  IN_PROGRESS = 'IN_PROGRESS',
  DONE = 'DONE',

  // not a real status
  ALL = 'ALL',
}

export const allTaskPriorities: string[] = Object.keys(TaskPriority);
export const allTaskPrioritiesForCreation: string[] = Object.keys(
  TaskPriority
).filter(value => value !== TaskPriority.ALL);

export const allTaskStatuses: string[] = Object.keys(TaskStatus);
export const allTaskStatusesForCreation: string[] = Object.keys(
  TaskStatus
).filter(value => value !== TaskStatus.ALL);
