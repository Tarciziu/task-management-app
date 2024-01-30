import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from '../../../environment/environment';
import { RegisterRequestModel } from '../models/register-request.model';

@Injectable({ providedIn: 'any' })
export class UserRegisterService {
  private readonly headers = new HttpHeaders({
    'Content-Type': 'application/json',
  });

  private readonly AUTH_SIGN_IN_URI = `${environment.BASE_API_URL}${environment.USER_SERVICE_API_URL}/auth/sign-in`;

  constructor(private httpClient: HttpClient) {}

  public register(data: RegisterRequestModel) {
    return this.httpClient.post<void>(this.AUTH_SIGN_IN_URI, data, {
      observe: 'body',
      responseType: 'json',
      headers: this.headers,
    });
  }
}
