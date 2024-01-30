export interface ConfirmationDialogInputLabels {
  translateTitle: string;
  translateDescription?: string;
  translateOkButton?: string;
  translateCancelButton?: string;
}

export interface ConfirmationDialogActions {
  confirmationFn: () => void;
  cancelFn?: () => void;
}
