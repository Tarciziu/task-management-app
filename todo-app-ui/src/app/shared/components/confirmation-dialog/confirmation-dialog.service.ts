import { Injectable } from '@angular/core';
import { ConfirmationDialogComponent } from './confirmation-dialog.component';
import { MatDialog } from '@angular/material/dialog';
import {
  ConfirmationDialogActions,
  ConfirmationDialogInputLabels,
} from '../../models/confirmation-dialog.model';

@Injectable({
  providedIn: 'root',
})
export class ConfirmationDialogService {
  constructor(private dialog: MatDialog) {}

  public openConfirmationDialog(
    labels: ConfirmationDialogInputLabels,
    actions: ConfirmationDialogActions
  ) {
    const dialogRef = this.dialog.open(ConfirmationDialogComponent, {
      data: { ...labels },
      autoFocus: false,
    });

    dialogRef.afterClosed().subscribe(operation => {
      if (operation) {
        actions.confirmationFn();
      } else {
        actions.cancelFn?.();
      }
    });
  }
}
