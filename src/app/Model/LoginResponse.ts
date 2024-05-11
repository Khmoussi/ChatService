import { LoginRequest } from './LoginRequest';
export class LoginResponse {
  firstname: string;
  lastname: string;
  constructor(firstname: string, lastname: string) {
    this.firstname = firstname
    this.lastname = lastname
  }
}
