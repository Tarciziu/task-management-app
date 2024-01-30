import { AbstractControl } from '@angular/forms';

export const hasFormControlError = (
  control: AbstractControl | undefined,
  errorKey: string
): boolean => {
  if (control) {
    return control.touched && control?.hasError(errorKey);
  }

  return false;
};
