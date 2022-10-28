import { PostModel } from '@/shared/model/post.model';
import {
  CARD_CLASS_LOGO,
  CARD_SIZE,
  CARD_TYPE,
  DATA_TYPE_CARDS,
  POST_TYPE,
  SERVICE_TYPE,
  SOURCE_URI,
} from '@/shared/constants/feed-constants';
import { getImageSizeByUrl } from '@/util/ds-util';
import { buildCardSizeByImageSize, checkNewsCardTypeWithoutSwipers, isFitContentGroupsCardType } from '@/util/ds-feed-util';
import { randomEleInArray, separateArr } from '@/util/array-util';

export class CardModel extends PostModel {
  id: string;
  dataTemplate: string;
  dataSize: string;
  dataType: string;
  classLogo: string;
  og_image_base64: string;
  og_image_url: any;
  swipers: any;
  writer_search: string;
  favicon_url: string;
  favicon_base64: string;
  search_type: string;
  notUseful = false;
  notUseful2 = false;
  name: string;
  image: string;
  stockCode: string;
  source: string;
  liked: boolean;
  bookmarked: boolean;
  deleted: boolean;
  data: any;
  type: any;

  constructor(cardModel?: CardModel, component?: string) {
    super(cardModel);
    if (cardModel) {
      this.id = cardModel.id;
      this.favicon_url = this.favicon_url ? this.favicon_url : this.getFavicon(this.url);
      this.favicon_base64 = this.favicon_base64 ? this.favicon_base64 : this.getFavicon(this.url);
      this.buildCardByPostType(component);
    }
  }

  buildCardByPostType(component) {
    switch (this.serviceType) {
      case SERVICE_TYPE.DART:
      case SERVICE_TYPE.COMMUNITY:
      case SERVICE_TYPE.BLOG:
        this.buildDefaultCard(component);
        break;
      // case SERVICE_TYPE.INVESTING:
      // if (this.stockCodes && this.stockCodes.length > 0) {
      //   this.buildStockCard();
      //   return;
      // }
      // break;
      default:
        this.buildDefaultCard(component);
        break;
    }
    // this.buildDefaultCard();
  }

  buildClassLogoByUri(uri: string) {
    if (!uri) {
      return;
    }
    if (uri.includes(SOURCE_URI.BLOG_NAVER)) {
      return 'media-blog';
    }
  }

  async buildCardSizeByImageUrl(url: string) {
    if (!url) {
      return;
    }
    const imgSize = (await getImageSizeByUrl(url)) as any;
    return buildCardSizeByImageSize(imgSize.width, imgSize.height);
  }

  buildDefaultCard(component) {
    let cardSize = '';
    if (component != 'metaSearch') {
      cardSize = this.generateCardSize(component);
    } else {
      cardSize = CARD_SIZE._1_1;
    }

    this.dataTemplate = randomEleInArray([CARD_TYPE.BLOG, CARD_TYPE.NEWS]);
    this.dataSize = cardSize;
    this.dataType = randomEleInArray([DATA_TYPE_CARDS[0]._01, DATA_TYPE_CARDS[0]._02]);
    this.classLogo = CARD_CLASS_LOGO.MEDIA_BLOG;
    // if (this.publishedAt == undefined) {
    //   this.publishedAt = new PublishedAt();
    //   this.publishedAt.seconds = new Date().getUTCMilliseconds();
    // }
  }

  generateCardSize(component) {
    if (component === 'people') {
      return CARD_SIZE._1_2;
    }

    // component = feed
    if (this.writer_search && this.writer_search.toLowerCase().includes('youtube')) {
      return randomEleInArray([CARD_SIZE._2_2, CARD_SIZE._1_2]);
    } else {
      if ((this.og_image_base64 && this.og_image_base64?.length > 0) || (this.og_image_url && this.og_image_url?.length > 0)) {
        if (this.isImageBase64(this?.og_image_base64) || this.isImageBase64(this?.og_image_url)) {
          console.log(this.og_image_base64, this.og_image_url);
          return CARD_SIZE._1_1;
        }
        if (this.search_type && this.search_type === 'VIDEO') {
          return CARD_SIZE._1_1;
        } else {
          console.log(this.og_image_url);
          return this.og_image_base64 || this.og_image_url ? randomEleInArray([CARD_SIZE._2_2, CARD_SIZE._1_2]) : CARD_SIZE._1_2;
        }
      } else {
        return CARD_SIZE._1_1;
      }
    }
  }

  isImageBase64(link: string) {
    if (!link) {
      return;
    }
    if (!link.includes('/') || !link.includes(';')) {
      return false;
    }
    return link.split('/')[1].split(';')[1] === 'base64,';
  }

  buildChartCard() {
    this.dataTemplate = CARD_TYPE.CHART;
    this.dataSize = randomEleInArray([CARD_SIZE._1_1, CARD_SIZE._2_1]);
  }

  buildWeatherCard() {
    this.dataTemplate = CARD_TYPE.WEATHER;
    this.dataSize = CARD_SIZE._1_1;
  }

  buildMapCard() {
    this.dataTemplate = CARD_TYPE.MAP;
    this.dataSize = CARD_SIZE._1_1;
  }

  buildStockCard() {
    this.dataTemplate = CARD_TYPE.CHART;
    this.dataSize = randomEleInArray([CARD_SIZE._1_2, CARD_SIZE._2_2]);
    this.dataType = DATA_TYPE_CARDS[0]._01;
  }

  buildSurveyCard() {
    this.dataTemplate = CARD_TYPE.SURVEY;
    this.dataSize = CARD_SIZE._1_2;
  }

  static buildTopTenCard(type?: string) {
    const cardPeopleType = {} as any;
    if (type) {
      if (type == CARD_PEOPLE_TYPE.IS_CONTENT) {
        cardPeopleType.isCardTopTenContent = true;
      } else if (type == CARD_PEOPLE_TYPE.IS_TITLE) {
        cardPeopleType.isCardTopTenTitle = true;
      } else {
        return;
      }
    }
    return {
      ...cardPeopleType,
      dataTemplate: CARD_TYPE.TOP_10,
      dataSize: CARD_SIZE._1_2,
    };
  }

  buildVideoCard() {
    this.dataTemplate = CARD_TYPE.YOU_TUBE;
    this.dataSize = CARD_SIZE._1_2;
  }

  buildSocialCard() {
    this.dataTemplate = CARD_TYPE.SOCIAL;
    this.dataSize = CARD_SIZE._1_2;
    this.dataType = randomEleInArray([DATA_TYPE_CARDS[0]._02, DATA_TYPE_CARDS[0]._03]);
  }

  static getCompanyCards(cardItems): CardModel[] {
    if (!cardItems || cardItems.length == 0) {
      return;
    }
    const result = [];
    const topTenCard = cardItems.find(item => item.dataTemplate == CARD_TYPE.TOP_10);
    if (topTenCard && topTenCard.companies) {
      const companies = topTenCard.companies;
      for (const company of companies) {
        // const cardCompany = new CardModel();
        const cardCompany = company;
        cardCompany.dataTemplate = CARD_TYPE.PEOPLE;
        cardCompany.dataSize = CARD_SIZE._1_2;
        cardCompany.dataType = DATA_TYPE_CARDS[0]._01;
        cardCompany.name = company.label;
        cardCompany.image = company.imageUrl || (company.googleInfobox && company.googleInfobox.logo) || '';
        cardCompany.content =
          (company.googleInfobox && company.googleInfobox.description) ||
          (company.wikipediaInfobox && company.wikipediaInfobox.title) ||
          '';
        (cardCompany.source = company.website || company.wikiUrl || ''), result.push(cardCompany);
      }
    }
    return result;
  }

  buildPeopleNetworkCard() {
    this.dataTemplate = CARD_TYPE.PEOPLE_NETWORK;
    this.dataSize = CARD_SIZE._4_2;
  }

  buildPeopleRankingCard() {
    this.dataTemplate = CARD_TYPE.PEOPLE_RANKING;
    this.dataSize = CARD_SIZE._4_2;
  }

  buildOtherCard() {
    this.dataTemplate = CARD_TYPE.PEOPLE_NETWORK;
    this.dataSize = CARD_SIZE._1_2;
  }

  getFavicon(url) {
    if (url == undefined || url == '' || !url.includes('http')) return '';

    const domain = new URL(url);
    domain.hostname.replace('www.', '');
    return domain.protocol + '//' + domain.hostname + '/favicon.ico';
  }

  // separate card appended and add card new from card initial
  static autoAppendCardNew(cards: CardModel[], cardsNew: CardModel[], cardsPreAppend?: CardModel[]) {
    if (!cardsNew) {
      return cards;
    }
    if (!cardsPreAppend || (cardsPreAppend && cardsPreAppend.length == 0)) {
      const [arrWeatherAndStock, arrRest] = separateArr(
        item =>
          item.dataTemplate == CARD_TYPE.CHART ||
          item.dataTemplate == CARD_TYPE.WEATHER ||
          item.dataTemplate == CARD_TYPE.TOP_10 ||
          item.dataTemplate == CARD_TYPE.SIGNAL,
        cards
      );
      return [...arrWeatherAndStock, ...cardsNew, ...arrRest];
    } else {
      const dataTemplates = cardsPreAppend.map(item => item.dataTemplate);
      const [arrWeatherAndStock, arrRest] = separateArr(item => dataTemplates.includes(item.dataTemplate), cards);
      return [...arrWeatherAndStock, ...cardsNew, ...arrRest];
    }
  }
}

export const CARD_PEOPLE_TYPE = {
  IS_TITLE: 'title',
  IS_CONTENT: 'content',
  DEFAULT: 'default',
};

export class ImagesInfo {
  width: number;
  height: number;
  url: string;
}

export const CARD_PATTERN = {
  STOCK: {
    dataTemplate: CARD_TYPE.STOCK,
    // dataType: DATA_TYPE_CARD._01,
  },
  FILE: {
    dataTemplate: CARD_TYPE.DOCS,
    // dataType: DATA_TYPE_CARD._01,
  },
  TOP_TEN: {
    dataTemplate: CARD_TYPE.TOP_10,
    // dataType: DATA_TYPE_CARD._01,
  },
  LINE_GRAPH: {
    dataTemplate: CARD_TYPE.STOCK,
    // dataType: DATA_TYPE_CARD._01,
  },
  BLOG: {
    dataTemplate: CARD_TYPE.BLOG,
    dataSize: CARD_SIZE._1_2,
  },
  NEW: {
    dataTemplate: CARD_TYPE.NEWS,
  },
  CONTENTS_GROUP: {
    dataTemplate: CARD_TYPE.CONTENTS_GROUP,
    dataSize: CARD_SIZE._2_2,
    dataType: null,
  },
};
