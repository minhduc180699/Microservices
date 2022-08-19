import axios from 'axios';

export const API_PATH = 'api/phone';

export default class PhoneService {
  public send(formModel: any): Promise<any> {
    return axios.post(API_PATH + '/send', formModel);
  }

  public confirm(formModel: any): Promise<any> {
    return axios.post(API_PATH + '/verify', formModel);
  }

  public createAccount(formModel: any): Promise<any> {
    return axios.post(API_PATH + '/register', formModel);
  }

  public checkUserExisted(data) {
    return axios({
      method: 'GET',
      url: 'api/public/checkUserIsExisted',
      params: {
        phoneNumber: data,
      },
    });
  }
}
