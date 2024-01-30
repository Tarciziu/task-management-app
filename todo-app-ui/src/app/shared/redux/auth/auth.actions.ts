import { LoginRequestModel } from '../../models/login-request.model';
import { RegisterRequestModel } from '../../../sign-up/models/register-request.model';

export class Login {
  static readonly type = '[Auth] Login';

  constructor(public credentials: LoginRequestModel) {}
}

export class FetchAuthInfo {
  static readonly type = '[Auth] Fetch Auth Info';

  constructor() {}
}

export class Logout {
  static readonly type = '[Auth] Logout';
}
