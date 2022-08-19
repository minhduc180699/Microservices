import axios from 'axios';

const BASE_URL = 'api/usersetting';

export class UserSettingService {
  public saveSetting(userSetting, connectomeId) {
    return axios.post(BASE_URL + '/saveSetting', userSetting);
  }

  public getSettingByUserId(connectomeId) {
    return axios.get(BASE_URL + '/getByUserId/' + connectomeId);
  }
}
