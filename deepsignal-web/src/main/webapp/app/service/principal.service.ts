import { ConnectomeModel } from '@/shared/model/connectome.model';

export class PrincipalService {
  getUserInfo() {
    return this.getConnectomeInfo().user;
  }

  getConnectomeInfo(): ConnectomeModel {
    if (localStorage.getItem('ds-connectome')) {
      return JSON.parse(localStorage.getItem('ds-connectome'));
    }
    return {};
  }

  getToken(): string {
    return localStorage.getItem('ds-authenticationToken') || sessionStorage.getItem('ds-authenticationToken');
  }
}
