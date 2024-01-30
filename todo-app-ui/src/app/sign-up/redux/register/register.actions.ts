import { RegisterRequestModel } from '../../models/register-request.model';

export class Register {
  static readonly type = '[Auth] Register';

  constructor(public credentials: RegisterRequestModel) {}
}
