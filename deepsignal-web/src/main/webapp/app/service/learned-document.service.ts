import axios from 'axios';

const BASE_URL = 'api/personal-documents/';

export class LearnedDocumentService {
  public getPersonalDocumentById(connectomeId: string, personalDocumentId: string) {
    return axios.get(BASE_URL.concat(connectomeId) + `?id=${personalDocumentId}`);
  }

  public getByIds(ids: string[], isDeleted?: any) {
    let url = 'getByIds';
    if (isDeleted !== null && isDeleted !== undefined) url += '?isDeleted=' + isDeleted;
    return axios.post(BASE_URL.concat(url), ids);
  }
}
