import axios from 'axios';
import { appendParamToRequest } from '@/util/ds-util';

const BASE_URL = 'api/learning';

export class LearningService {
  public learning(userId: string, connectomeId: string, doc, language, formData) {
    const mapKeyValue = new Map();
    mapKeyValue.set('docs', doc);
    mapKeyValue.set('lang', language);
    return axios.post(
      BASE_URL + '/learning-center/'.concat(userId).concat('/').concat(connectomeId).concat(appendParamToRequest(mapKeyValue)),
      formData
    );
  }
}
