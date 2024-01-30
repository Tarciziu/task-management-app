import { OperationStatus } from '../../../shared/enums';
import { SettingsModel } from '../../models/settings.model';
import { Action, Selector, State, StateContext } from '@ngxs/store';
import { Injectable } from '@angular/core';
import { BaseState } from '../../../shared/redux/base.state';
import { SettingsService } from '../../services/settings.service';
import { FetchUserSettings, UpdateUserSettings } from './settings.actions';
import { catchError, of, tap } from 'rxjs';
import { FetchTask } from '../../../add-edit-task/redux/add-edit-task/add-edit-task.actions';

export interface SettingsStateModel {
  loadSettingsOperationStatus: OperationStatus;
  updateSettingsOperationStatus: OperationStatus;
  settings?: SettingsModel;
}

const defaults: SettingsStateModel = {
  loadSettingsOperationStatus: OperationStatus.IDLE,
  updateSettingsOperationStatus: OperationStatus.IDLE,
};

@State<SettingsStateModel>({
  name: 'Settings',
  defaults,
})
@Injectable()
export class SettingsState extends BaseState {
  constructor(private readonly settingsService: SettingsService) {
    super();
  }

  // selectors
  @Selector()
  static getLoadSettingsOperationStatus(
    state: SettingsStateModel
  ): OperationStatus {
    return state.loadSettingsOperationStatus;
  }

  @Selector()
  static getUpdateSettingsOperationStatus(
    state: SettingsStateModel
  ): OperationStatus {
    return state.updateSettingsOperationStatus;
  }

  @Selector()
  static getSettings(state: SettingsStateModel): SettingsModel | undefined {
    return state.settings;
  }

  // actions
  @Action(FetchUserSettings)
  fetchUserSettings({ patchState }: StateContext<SettingsStateModel>) {
    patchState({
      loadSettingsOperationStatus: OperationStatus.LOADING,
    });

    return this.settingsService.getSettings().pipe(
      tap((response: SettingsModel) => {
        patchState({
          loadSettingsOperationStatus: OperationStatus.SUCCESS,
          settings: response,
        });
      }),
      catchError(err => {
        this.notificationService.showErrorMessage(
          'notifications.error.fetch-settings'
        );

        patchState({
          loadSettingsOperationStatus: OperationStatus.ERROR,
        });

        return of(err);
      })
    );
  }

  @Action(UpdateUserSettings)
  updateUserSettings(
    { patchState }: StateContext<SettingsStateModel>,
    { data }: UpdateUserSettings
  ) {
    patchState({
      updateSettingsOperationStatus: OperationStatus.LOADING,
    });

    return this.settingsService.updateSettings(data).pipe(
      tap((response: SettingsModel) => {
        patchState({
          updateSettingsOperationStatus: OperationStatus.SUCCESS,
          settings: response,
        });

        this.notificationService.showSuccessMessage(
          'notifications.success.update-settings'
        );
      }),
      catchError(err => {
        this.notificationService.showErrorMessage(
          'notifications.error.update-settings'
        );

        patchState({
          updateSettingsOperationStatus: OperationStatus.SUCCESS,
        });

        return of(err);
      })
    );
  }
}
