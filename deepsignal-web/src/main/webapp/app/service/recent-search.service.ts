import axios from 'axios';
import { appendBaseParamRequest, appendParamToRequest } from '@/util/ds-util';

const BASE_URL = 'api/recent-search';

export class RecentSearchService {
  public getAll(userId) {
    const mapKeyValue = new Map();
    mapKeyValue.set('userId', userId);
    return axios.get(BASE_URL + '/getAll'.concat(appendParamToRequest(mapKeyValue)));
  }

  public save(recentSearch) {
    if (!recentSearch) {
      return;
    }
    return axios.post(BASE_URL + '/save', recentSearch);
  }

  public deleteById(id) {
    if (!id) {
      return;
    }
    return axios.delete(BASE_URL + '/' + id);
  }

  public deleteAllByUserId(userId: string) {
    return axios.delete(BASE_URL + '/deleteAll/' + userId);
  }
}
