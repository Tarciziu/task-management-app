import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { SettingsModel } from '../models/settings.model';
import { environment } from '../../../environment/environment';

@Injectable({ providedIn: 'any' })
export class SettingsService {
  private readonly USER_SETTINGS_URI = `${environment.BASE_API_URL}${environment.USER_SERVICE_API_URL}/user-settings`;

  constructor(private httpClient: HttpClient) {}

  public getSettings(): Observable<SettingsModel> {
    return this.httpClient.get<SettingsModel>(this.USER_SETTINGS_URI);
  }

  public updateSettings(data: SettingsModel): Observable<SettingsModel> {
    return this.httpClient.put<SettingsModel>(this.USER_SETTINGS_URI, data);
  }
}
