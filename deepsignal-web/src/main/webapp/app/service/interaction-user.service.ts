import axios from 'axios';
import { appendParamToRequest } from '@/util/ds-util';

const BASE_URL = 'api/interaction-user';

export class InteractionUserService {
  public saveLike(feedId, isDelete?) {
    const mapKeyValue = new Map();
    mapKeyValue.set('feedId', feedId);
    mapKeyValue.set('isDelete', !!isDelete);
    return axios.post(BASE_URL + '/like'.concat(appendParamToRequest(mapKeyValue)));
  }

  public saveDislike(feedId, isDelete?) {
    const mapKeyValue = new Map();
    mapKeyValue.set('feedId', feedId);
    mapKeyValue.set('isDelete', !!isDelete);
    return axios.post(BASE_URL + '/dislike'.concat(appendParamToRequest(mapKeyValue)));
  }

  public saveShare(feedId, isDelete?) {
    const mapKeyValue = new Map();
    mapKeyValue.set('feedId', feedId);
    mapKeyValue.set('isDelete', !!isDelete);
    return axios.post(BASE_URL + '/share'.concat(appendParamToRequest(mapKeyValue)));
  }

  public saveBookmark(feedId, isDelete?) {
    const mapKeyValue = new Map();
    mapKeyValue.set('feedId', feedId);
    mapKeyValue.set('isDelete', !!isDelete);
    return axios.post(BASE_URL + '/bookmark'.concat(appendParamToRequest(mapKeyValue)));
  }

  public saveDelete(feedId, isDelete?) {
    const mapKeyValue = new Map();
    mapKeyValue.set('feedId', feedId);
    mapKeyValue.set('isDelete', !!isDelete);
    return axios.post(BASE_URL + '/delete'.concat(appendParamToRequest(mapKeyValue)));
  }

  public saveComment(feedId, isDelete?) {
    const mapKeyValue = new Map();
    mapKeyValue.set('feedId', feedId);
    mapKeyValue.set('isDelete', !!isDelete);
    return axios.post(BASE_URL + '/comment'.concat(appendParamToRequest(mapKeyValue)));
  }

  public statistic(feedId) {
    const mapKeyValue = new Map();
    mapKeyValue.set('feedId', feedId);
    return axios.get(BASE_URL + '/statistic'.concat(appendParamToRequest(mapKeyValue)));
  }

  public handleShare(id, platform, connectomeId?) {
    return axios.get('api/connectome-feed/sharingCard', {
      params: {
        id: id,
        platform: platform,
        connectomeId: connectomeId,
      },
    });
  }
}
