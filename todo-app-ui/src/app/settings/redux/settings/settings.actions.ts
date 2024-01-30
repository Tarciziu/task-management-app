import { SettingsModel } from '../../models/settings.model';

export class FetchUserSettings {
  static readonly type = '[Settings] Fetch User Settings';

  constructor() {}
}

export class UpdateUserSettings {
  static readonly type = '[Settings] Update User Settings';

  constructor(public data: SettingsModel) {}
}
