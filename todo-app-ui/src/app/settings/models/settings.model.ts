export interface SettingsModel {
  notifications: NotificationsSettings;
}

export interface NotificationsSettings {
  [notificationType: string]: NotificationSettingsByNotificationType[];
}

export interface NotificationSettingsByNotificationType {
  channel: string;
  enabled: boolean;
}
