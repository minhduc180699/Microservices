import axios from 'axios';
import buildPaginationQueryOpts from '@/shared/sort/sorts';

export default class UserManagementService {
  public get(login): Promise<any> {
    return axios.get(`api/admin/users/${login}`);
  }

  public getUserInfo(login) {
    return axios.get('api/admin/users/info', {
      params: {
        login: login,
      },
    });
  }

  public create(user): Promise<any> {
    return axios.post('api/admin/users', user);
  }

  public update(user): Promise<any> {
    return axios.put('api/admin/users/management', user);
  }

  public sendEmailNoticeActivation(id) {
    return axios.get('api/email/noticeActivateAccount', {
      params: {
        id: id,
      },
    });
  }

  public remove(userId: number): Promise<any> {
    return axios.delete(`api/admin/users/${userId}`);
  }

  public retrieve(req?: any): Promise<any> {
    return axios.get(`api/admin/users?${buildPaginationQueryOpts(req)}`);
  }

  public retrieveAuthorities(): Promise<any> {
    return axios.get('api/authorities');
  }

  public searchUser(keyword, page?) {
    return axios.get('api/admin/users/search', {
      params: {
        keyword: keyword,
        page: page,
      },
    });
  }

  public deleteUserByAdmin(id) {
    return axios.delete('api/admin/users/delete', {
      params: {
        id: id,
      },
    });
  }
}
