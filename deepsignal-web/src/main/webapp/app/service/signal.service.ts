import axios from 'axios';
import buildPaginationQueryOpts from '@/shared/sort/sorts';
const BASE_URL = 'api/signal-issue';
const BASE_URL_SIGNAL_KEYWORD = 'api/signal-keywords';

export class SignalService {
  public paging(connectomeId, workDay, signalType, pageable?) {
    let req = {};
    if (pageable) {
      req = {
        page: pageable.page ? pageable.page - 1 : 0,
        size: pageable.size ? pageable.size : 10,
        sort: pageable.sort ? pageable.sort : ['displayOrder,desc'],
      };
    }
    return axios.get(
      BASE_URL + '/paging/' + connectomeId + '?work_day=' + workDay + '&' + 'signalType=' + signalType + '&' + buildPaginationQueryOpts(req)
    );
  }

  public saveSignalKeywords(connectomeId, signalKeywords, signalType?) {
    if (!signalKeywords.type) {
      signalKeywords.type = signalType;
    }
    return axios.post(BASE_URL_SIGNAL_KEYWORD + '/' + connectomeId + '/save', signalKeywords);
  }

  public getKeywordSignalsByConnectomeId(connectomeId) {
    return axios.get(BASE_URL_SIGNAL_KEYWORD + '/' + connectomeId + '/getByConnectomeId');
  }

  public deleteById(connectomeId, signalId, id) {
    return axios.delete(BASE_URL_SIGNAL_KEYWORD + '/' + connectomeId + '/' + signalId + '/' + id);
  }

  public getTimeSeries(body) {
    return axios.post(BASE_URL + '/' + 'time-series', body);
  }
}
