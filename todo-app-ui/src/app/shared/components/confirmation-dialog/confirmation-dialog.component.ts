import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { ConfirmationDialogInputLabels } from '../../models/confirmation-dialog.model';

@Component({
  selector: 'app-confirmation-dialog',
  templateUrl: './confirmation-dialog.component.html',
})
export class ConfirmationDialogComponent {
  public translateTitle: string;
  public translateDescription: string;
  public translateCancelButton: string;
  public translateOkButton: string;

  constructor(
    private dialogRef: MatDialogRef<ConfirmationDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: ConfirmationDialogInputLabels
  ) {
    this.translateTitle = data.translateTitle;
    this.translateDescription = data.translateDescription ?? '';
    this.translateCancelButton =
      data.translateCancelButton ?? 'general.buttons.cancel';
    this.translateOkButton = data.translateOkButton ?? 'general.buttons.ok';
  }
  onConfirm() {
    this.dialogRef.close('Confirm');
  }
  onCancel() {
    this.dialogRef.close();
  }
}
