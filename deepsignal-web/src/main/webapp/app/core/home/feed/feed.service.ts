import axios, { AxiosResponse } from 'axios';
import { appendBaseParamRequest, buildRequestParamBase } from '@/util/ds-util';

export default class FeedService {
  constructor() {}

  getFeedByIds(ids, isDeleted?: any) {
    let url = 'api/connectome-feed/getByIds';
    if (isDeleted !== null && isDeleted !== undefined) url += '?isDeleted=' + isDeleted;
    return axios.post(url, ids);
  }

  getAllConnectomeFeed(page = 0, size = 20, sortBy = 'score', sortDirection = 'desc'): Promise<any> {
    return new Promise<any>((resolve, reject) => {
      axios
        .get('api/connectome-feed/getAll'.concat(appendBaseParamRequest(page, size, sortBy, sortDirection)))
        .then(res => resolve(res))
        .catch(err => reject(err));
    });
  }

  getListFeed(connectomeId: string, request_id, keyword, from, until, page, size, search_type, channels, type: string, lang: string) {
    const Language = localStorage.getItem('currentLanguage') || 'en';
    const params = {
      page: page,
      size: size,
      lang: Language,
      type: type
    };
    if (keyword) {
      Object.assign(params, { keyword: keyword });
    }
    if (search_type) {
      Object.assign(params, { search_type: search_type });
    }
    if (from && until) {
      Object.assign(params, { from: from, until: until });
    }
    return new Promise<any>((resolve, reject) => {
      axios
        .get('/api/connectome-feeds/getListFeeds/'.concat(connectomeId), { params: params })
        .then(res => resolve(res))
        .catch(err => reject(err));
    });
  }

  getAllConnectomeFeedByConnectomeId(
    connectomeId: string,
    page,
    size,
    orderBy,
    sortDirection = 'desc',
    topic,
    expected,
    keyword?,
    listFilter?
  ) {
    const params = {
      page: page,
      size: size,
      orderBy: orderBy,
      sortDirection: sortDirection,
      topic: topic,
      excepted: expected,
    };
    if (keyword) {
      Object.assign(params, { keyword: keyword });
    }
    //add 'lang' to api
    const lang = localStorage.getItem('currentLanguage') || 'en';
    if (!listFilter.some(ele => ele.field === 'lang'))
      listFilter.push({
        field: 'lang',
        value: "'" + lang + "'",
      });
    else
      for (const i in listFilter) {
        if (listFilter[i].field === 'lang') {
          listFilter[i].value = "'" + lang + "'";
          break;
        }
      }
    // connectomeId = 'ALL_20211213'; // Fixed connectomeId for demo  METAVERSE_GMUSE
    return new Promise<any>((resolve, reject) => {
      axios
        .post('api/connectome-feed/getListFeeds/'.concat(connectomeId), listFilter, { params: params })
        .then(res => resolve(res))
        .catch(err => reject(err));
    });
  }

  getConnectomeFeedByDocId(docId?: string): Promise<AxiosResponse<any>> {
    return axios.get<any>('api/connectome-feed/getFeed/'.concat(docId));
  }

  getStock(stockCode: any) {
    return axios.post('api/connectome-feed/getStock', stockCode);
  }

  updateFeed(connectomeId: string) {
    return axios.get('api/connectome-feed/updateFeed/' + connectomeId);
  }

  trainingConnectome(connectomeId, data) {
    if (!connectomeId || !data) {
      return;
    }
    return axios.post('api/connectome-feed/training/' + connectomeId, data);
  }

  getFeedById(connectomeId: string, id: string) {
    return axios.get('/api/connectome-feed/getDetailCard/' + connectomeId, { params: { id: id } });
  }

  getDetailFeed(connectomeId: string, requestId: string, docId: string){
    return axios.get('/api/connectome-feeds/getDetailFeed/' + connectomeId, {params: { requestId: requestId, docId: docId }})
  }

  getFeedByRecommendDate(connectomeId, recommendDate) {
    const lang = localStorage.getItem('currentLanguage') || 'en';
    return axios.get('api/connectome-feed/getByRecommendDate/' + connectomeId + '?lang=' + lang + '&recommend_date=' + recommendDate);
  }

  getActivity(connectomeId, page, size, orderBy, sortDirection = 'desc', listFilter?) {
    const params = {
      page: page,
      size: size,
      orderBy: orderBy,
      sortDirection: sortDirection,
    };
    return axios.post('api/connectome-feed/getActivity/' + connectomeId, listFilter, { params: params });
  }

  handleActivityLike(requestId, connectomeId, docId, liked, isBookmarked?, isDeleted?) {
    const params = {
      requestId   : "requestId",
      connectomeId: connectomeId,
      docId       : docId,
      liked       : liked,
      isBookmarked: null,
      isDeleted   : null
    };
    return axios.put('api/connectome-feeds/activityLike', params);
  }

  handleActivityBookmark(requestId, connectomeId, docId, liked, isBookmarked, isDeleted?) {
    const params = {
      requestId   : "requestId",
      connectomeId: connectomeId,
      docId       : docId,
      liked       : 0,
      isBookmarked: isBookmarked,
      isDeleted   : null
    };
    return axios.put('api/connectome-feeds/activityBookmark', params);
  }

  handleHideFeed(requestId, connectomeId, docId, liked, isBookmarked, isDeleted) {
    const params = {
      requestId: "requestId",
      connectomeId: connectomeId,
      docId       : docId,
      liked       : 0,
      isBookmarked: null,
      isDeleted   : isDeleted
    };
    return axios.put('api/connectome-feeds/hideFeed', params)
  }
}
