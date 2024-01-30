import { Injectable } from '@angular/core';
import { MatSnackBar, MatSnackBarConfig } from '@angular/material/snack-bar';
import { NotificationComponent } from './notification.component';
import { NotificationType } from '../../enums';

@Injectable()
export class NotificationService {
  private defaultSnackbarConfiguration: MatSnackBarConfig = {
    duration: 5000,
    direction: 'ltr',
    horizontalPosition: 'center',
    verticalPosition: 'bottom',
  };

  constructor(private readonly snackBar: MatSnackBar) {}

  public showSuccessMessage(message: string): void {
    this.showMessage({ message, type: NotificationType.SUCCESS });
  }

  public showErrorMessage(message: string): void {
    this.showMessage({ message, type: NotificationType.ERROR });
  }

  private showMessage(notificationItem: {
    message: string;
    type: NotificationType;
  }): void {
    this.snackBar.openFromComponent(NotificationComponent, {
      ...this.defaultSnackbarConfiguration,
      data: {
        translateMessage: notificationItem.message,
        type: notificationItem.type,
      },
    });
  }
}
