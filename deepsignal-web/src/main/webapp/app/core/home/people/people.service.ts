import { CardModel } from '@/shared/cards/card.model';
import { POST_TYPE } from '@/shared/constants/feed-constants';
import axios from 'axios';
import { data } from 'jquery';
import buildPaginationQueryOpts from '@/shared/sort/sorts';

export default class PeopleService {
  constructor() {
    this.init();
  }

  public init() {}

  getAllFeedFromPeople(id: string): Promise<any> {
    const param = id;
    return new Promise<any>((resolve, reject) => {
      axios
        .get('content/data/dummy/' + param)
        .then(res => {
          resolve(res);
          const cards = [];
          for (const cardFake of res.data.documents) {
            const cardItem = new CardModel(cardFake);
            cardItem.id = cardFake.metadata.doc_id;
            cardItem.sourceUri = cardFake.metadata.source_uri;
            cardItem.commentId = cardFake.metadata.comment_id;
            cardItem.replyId = cardFake.metadata.reply_id;
            //cardItem.crawlerId = '';
            cardItem.webSourceId = cardFake.metadata.web_source_id;
            cardItem.webSourceCategory = cardFake.metadata.web_source_category;
            cardItem.collectedAt = cardFake.metadata.collected_at;
            cardItem.serviceType = cardFake.metadata.service_type;
            //cardItem. = cardFake.metadata.image_links ? cardFake.metadata.image_links[0] : undefined;
            cardItem.publishedAt = cardFake.metadata.published_at;
            cardItem.title = cardFake.metadata.title;
            cardItem.content = cardFake.metadata.content.substring(0, 256);
            //cardItem.name = cardFake.metadata.title;
            //cardItem.desc = cardFake.metadata.content.substring(0, 256);
            //cardItem. = 'unknow';
            cards.push(cardItem);
          }
          res.data.documents = cards;
        })
        .catch(err => reject(err));
    });
  }

  getNetworkChart(title: string, language: string) {
    return axios.get('/api/people/getNetworkChart', {
      params: {
        title: title,
        language: language,
      },
    });
  }

  getPeopleAndCompany(peopleAndCompany: any) {
    return axios.post('/api/people/company', peopleAndCompany);
  }

  getMarketName(code: string) {
    return axios.get('/api/people/market/name?code=' + code);
  }

  public getEntityInfo(title: string, language: string, connectomeId?: string) {
    return axios.get('/api/people/get/entity?title=' + title + '&language=' + language + '&connectomeId=' + connectomeId);
  }

  public getPeopleDocInfo(connectomeId: string, page: number, size: number, orderBy: string, sortDirection: string, isGetStock: boolean) {
    return axios.get('api/people/' + connectomeId, {
      params: {
        page: page,
        size: size,
        orderBy: orderBy,
        sortDirection: sortDirection,
        isGetStock: isGetStock,
      },
    });
  }

  public findByConnectomeId(connectomeId: string, pageable?, isGetStock?) {
    const req = {
      page: pageable && pageable.page ? pageable.page - 1 : 0,
      size: pageable && pageable.size ? pageable.size : 10,
      sort: pageable && pageable.sort ? pageable.sort : ['id,desc'],
    };
    const lang = localStorage.getItem('currentLanguage') || 'en';
    return axios.get('api/people/' + connectomeId + '?' + buildPaginationQueryOpts(req), {
      params: {
        isGetStock: true,
        lang: lang,
      },
    });
  }

  hiddenPeople(id, title, type, lang, deleted) {
    return axios.get('api/people/hidden/' + id, {
      params: {
        title: title,
        type: type,
        lang: lang,
        deleted: deleted,
      },
    });
  }
}
