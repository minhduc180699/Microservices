import axios from 'axios';
import { appendParamToRequest } from '@/util/ds-util';

export class DetailFeedService {
  private BASE_URL_RELATED_CONTENT = 'api/related-content';

  getRelatedDocs(connectomeId, feedDocId) {
    const mapKeyValue = new Map();
    mapKeyValue.set('connectomeId', connectomeId);
    mapKeyValue.set('feedDocId', feedDocId);
    return axios.get(this.BASE_URL_RELATED_CONTENT + '/docs'.concat(appendParamToRequest(mapKeyValue)));
  }
}
