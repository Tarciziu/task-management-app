import { Component, OnInit } from '@angular/core';
import { BaseComponent } from '../../../shared/components/base/base.component';
import { Select, Store } from '@ngxs/store';
import {
  FetchUserSettings,
  UpdateUserSettings,
} from '../../redux/settings/settings.actions';
import { SettingsState } from '../../redux/settings/settings.state';
import { Observable } from 'rxjs';
import { OperationStatus } from '../../../shared/enums';
import {
  NotificationsSettings,
  SettingsModel,
} from '../../models/settings.model';
import { FormControl, FormGroup } from '@angular/forms';
import { ConfirmationDialogService } from '../../../shared/components/confirmation-dialog/confirmation-dialog.service';

interface SettingsForm {
  notifications: FormGroup<NotificationByNotificationTypeForm>;
}

interface NotificationByNotificationTypeForm {
  [key: string]: FormGroup<NotificationChannelByNotificationType>;
}

interface NotificationChannelByNotificationType {
  [key: string]: FormControl;
}

@Component({
  selector: 'app-settings-wrapper',
  templateUrl: './settings-wrapper.component.html',
  styleUrl: './settings-wrapper.component.scss',
})
export class SettingsWrapperComponent extends BaseComponent implements OnInit {
  @Select(SettingsState.getLoadSettingsOperationStatus)
  loadSettingsOperationStatus$!: Observable<OperationStatus>;
  loadSettingsOperationStatus!: OperationStatus;

  @Select(SettingsState.getUpdateSettingsOperationStatus)
  updateSettingsOperationStatus$!: Observable<OperationStatus>;
  updateSettingsOperationStatus!: OperationStatus;

  @Select(SettingsState.getSettings)
  settings$!: Observable<SettingsModel>;
  settings!: SettingsModel;

  settingsForm: FormGroup = new FormGroup<SettingsForm>({
    notifications: new FormGroup<any>({}),
  });

  protected readonly OperationStatus = OperationStatus;

  constructor(
    private readonly store: Store,
    private readonly confirmationDialogService: ConfirmationDialogService
  ) {
    super();
  }

  ngOnInit(): void {
    this.store.dispatch(new FetchUserSettings());

    this.watchForValues();
  }

  getFormGroup(notificationType: string): FormGroup {
    return this.settingsForm
      .get('notifications')
      ?.get(notificationType) as FormGroup;
  }

  onUpdateSettings() {
    const formData = this.settingsForm.value;
    console.log('FormData = ', formData);

    const notifications: NotificationsSettings = Object.keys(
      formData.notifications
    ).reduce((result, notificationType) => {
      result[notificationType] = Object.keys(
        formData.notifications[notificationType]
      ).map(channel => ({
        channel,
        enabled: formData.notifications[notificationType][channel],
      }));

      return result;
    }, {} as NotificationsSettings);

    const settings: SettingsModel = {
      notifications,
    };

    this.confirmationDialogService.openConfirmationDialog(
      {
        translateTitle: 'settings.actions.update.dialog.title',
        translateDescription: 'settings.actions.update.dialog.description',
        translateOkButton: 'settings.actions.update.dialog.ok',
        translateCancelButton: 'settings.actions.update.dialog.cancel',
      },
      {
        confirmationFn: () => {
          this.store.dispatch(new UpdateUserSettings(settings));
        },
        cancelFn: () => {
          // action canceled
        },
      }
    );
  }

  private watchForValues() {
    this.subscribeToDefined(
      this.loadSettingsOperationStatus$,
      (loadSettingsOperationStatus: OperationStatus) =>
        (this.loadSettingsOperationStatus = loadSettingsOperationStatus)
    );

    this.subscribeToDefined(this.settings$, (settings: SettingsModel) => {
      const notifications: FormGroup<NotificationByNotificationTypeForm> =
        this.settingsForm.get(
          'notifications'
        ) as FormGroup<NotificationByNotificationTypeForm>;

      for (const [key, value] of Object.entries(settings.notifications)) {
        const notificationTypeFormGroup =
          new FormGroup<NotificationChannelByNotificationType>({});

        for (let settingsByNotificationType of value) {
          notificationTypeFormGroup.addControl(
            settingsByNotificationType.channel,
            new FormControl(settingsByNotificationType.enabled)
          );
        }

        notifications.addControl(key, notificationTypeFormGroup);
      }

      this.settingsForm.patchValue({
        notifications: notifications,
      });

      this.settings = settings;
    });
  }
}
