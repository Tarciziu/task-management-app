import { Component, Inject } from '@angular/core';
import { NotificationType } from '../../enums';
import { MAT_SNACK_BAR_DATA } from '@angular/material/snack-bar';

@Component({
  selector: 'app-notification',
  templateUrl: './notification.component.html',
  styleUrls: ['./notification.component.scss'],
})
export class NotificationComponent {
  public translateMessage: string;
  public type: NotificationType;

  constructor(
    @Inject(MAT_SNACK_BAR_DATA)
    private data: { translateMessage: string; type: NotificationType }
  ) {
    this.translateMessage = data.translateMessage;
    this.type = data.type;
  }
}
