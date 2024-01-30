import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { User } from '../models/user.model';
import { Observable } from 'rxjs';
import { LoginRequestModel } from '../models/login-request.model';
import { environment } from '../../../environment/environment';
import { RegisterRequestModel } from '../../sign-up/models/register-request.model';

@Injectable({ providedIn: 'any' })
export class AuthService {
  private readonly headers = new HttpHeaders({
    'Content-Type': 'application/json',
  });

  private readonly AUTH_LOGIN_URI = `${environment.BASE_API_URL}${environment.USER_SERVICE_API_URL}/auth/login`;
  private readonly AUTH_LOGOUT_URI = `${environment.BASE_API_URL}${environment.USER_SERVICE_API_URL}/auth/logout`;
  private readonly AUTH_PROFILE_URI = `${environment.BASE_API_URL}${environment.USER_SERVICE_API_URL}/auth/me`;

  constructor(private httpClient: HttpClient) {}

  public login(data: LoginRequestModel) {
    return this.httpClient.post<void>(
      this.AUTH_LOGIN_URI,
      { email: data.email, password: data.password },
      {
        observe: 'body',
        responseType: 'json',
        headers: this.headers,
      }
    );
  }

  public getProfile(): Observable<User> {
    return this.httpClient.get<User>(this.AUTH_PROFILE_URI);
  }

  public logout(): Observable<Object> {
    return this.httpClient.post(this.AUTH_LOGOUT_URI, {});
  }
}
