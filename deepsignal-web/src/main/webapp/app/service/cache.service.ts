import axios from 'axios';

export class CacheService {
  private BASE_URL = 'api/cache/';

  public checkNotification(userId) {
    return axios.get(this.BASE_URL + 'checkNotification/' + userId);
  }
}
