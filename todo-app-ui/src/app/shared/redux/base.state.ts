import { NotificationService } from '../components/notification/notification.service';
import { inject } from '@angular/core';

export class BaseState {
  protected readonly notificationService = inject(NotificationService);
}
