import axios from 'axios';

export class asideService {
  public getByDocId(documents) {
    return axios.post('http://192.168.9.181:8081/api/personal-documents/getByDocIds', documents);
  }
}
