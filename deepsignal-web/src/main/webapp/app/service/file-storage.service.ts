import axios from 'axios';
import buildPaginationQueryOpts from '@/shared/sort/sorts';
import { FILE_TYPE_UPLOAD } from '@/shared/constants/ds-constants';

export const API_PATH_FILE = 'api/file-storage';

export class FileStorageService {
  public uploadDoc(param: string, formData: any) {
    return axios.post(API_PATH_FILE.concat('/upload/').concat(param), formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    });
  }

  public getAllDoc(userId, pageable?) {
    let req = {};
    if (pageable) {
      req = {
        page: pageable.page ? pageable.page - 1 : 0,
        size: pageable.size ? pageable.size : 10,
        sort: pageable.sort ? pageable.sort : ['createdDate,desc'],
      };
    }
    return axios.get(API_PATH_FILE.concat('/getAllDoc/').concat(userId).concat('?').concat(buildPaginationQueryOpts(req)));
  }

  public deleteAllDoc(userId) {
    if (!userId) {
      return;
    }
    const type = FILE_TYPE_UPLOAD.FILE;
    return axios.delete(API_PATH_FILE.concat('/deleteAll/').concat(userId).concat('/').concat(type));
  }

  public deleteDocById(id) {
    if (!id) {
      return;
    }
    return axios.delete(API_PATH_FILE.concat('/deleteData/').concat(id));
  }

  public downloadDocById(id) {
    if (!id) {
      return;
    }
    return axios.get(API_PATH_FILE.concat('/downloadDoc/').concat(id), {
      responseType: 'blob',
    });
  }
}
