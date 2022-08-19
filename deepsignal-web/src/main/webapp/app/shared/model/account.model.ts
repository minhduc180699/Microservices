// export interface AccountModel {
//   id: number;
//   login: string;
// }

export class Account {
  id: number;
  login: string;

  constructor(id: number, login: string) {
    this.id = id;
    this.login = login;
  }
}
