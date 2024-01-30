import { UserRoleEnum } from '../enums';

export interface User {
  id: string;
  firstName: string;
  lastName: string;
  email: string;
  roles: UserRoleEnum[];
}
