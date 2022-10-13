export const CARD_SIZE = {
  _1_1: '1x1',
  _1_2: '1x2',
  _2_1: '2x1',
  _2_2: '2x2',
  _4_2: '4x2',
};

export const CARD_SIZES = ['1x1', '1x2', '2x1', '2x2'];

export const CARD_TYPE = {
  STOCK: 'stock',
  WEATHER: 'wheather',
  TOP_10: 'top-10',
  YOU_TUBE: 'youtube',
  SURVEY: 'survey',
  MAP: 'map',
  CONTENTS_GROUP: 'contents-group',
  NEWS: 'news',
  SOCIAL: 'social',
  BLOG: 'blog',
  DOCS: 'docs',
  CHART: 'chart',
  RISING_PEOPLE: 'rising-people',
  RISING_COMPANY: 'rising-company',
  PEOPLE: 'people',
  COMPANY: 'company',
  PEOPLE_NETWORK: 'people-network',
  PEOPLE_RANKING: 'people-ranking',
  SIGNAL: 'signal',
  SOCIAL_NETWORK_ANALYSIS: 'social-network-analysis',
};

export const SERVICE_TYPE = {
  COMMUNITY: 'COMMUNITY',
  WEATHER: 'wheather',
  TOP_10: 'top-10',
  YOU_TUBE: 'youtube',
  SURVEY: 'survey',
  MAP: 'map',
  CONTENTS_GROUP: 'contents-group',
  NEWS: 'news',
  SOCIAL: 'social',
  BLOG: 'BLOG',
  DOCS: 'docs',
  DART: 'DART',
  INVESTING: 'INVESTING',
};

export const CARD_CLASS_LOGO = {
  MEDIA_NATE: 'media-nate',
  MEDIA_JTBC: 'media-jtbc',
  MEDIA_NBC: 'media-nbc',
  MEDIA_MBN: 'media-mbn',
  MEDIA_TWITTER: 'media-twitter',
  MEDIA_BLOG: 'media-blog',
  MEDIA_EXCEL: 'media-excel',
};

export const CARD_GROUPS_1 = [CARD_TYPE.CONTENTS_GROUP, CARD_TYPE.NEWS, CARD_TYPE.SOCIAL, CARD_TYPE.BLOG];

export const POST_TYPE = {
  PERSON: 410,
  ORGANIZATION: 411,
  LOCATION: 412,
  BLOG: 413,
  ARTICLE: 510,
  FILE: 511,
  MOVIE: 515,
  MUSIC: 516,
  TABLE: 520,
  TERM_LIST: 521,
  WORD_CLOUD: 522,
  ENTITY: 540,
  TOP_10: 560,
  LINE_GRAPH: 610,
  VERTICAL_BAR_GRAPH: 611,
  HORIZONTAL_BAR_GRAPH: 612,
  PIE_CHART: 613,
  STACK: 700,
  WEATHER: 720,
  MAP: 740,
};

export const DATA_TYPE_CARDS = [{ _01: '01' }, { _02: '02' }, { _03: '03' }];

export const SOURCE_URI = {
  BLOG_NAVER: 'blog.naver.com',
};

export const SYMBOL_CRYPTO = ['BNB-USD', 'BTC-USD', 'DOGE-USD', 'ETH-USD', 'HEX-USD', 'SOL-USD', 'USDT-USD', 'XRP-USD'];

export const SORT_BY = [
  // { text: 'Bookmarked', interaction: { field: 'bookmarked', value: true }, selected: false },
  { text: 'Liked', interaction: { field: 'liked', value: 1 }, selected: false },
  { text: 'Shared', interaction: { field: 'shared', value: true }, selected: false },
  { text: 'Bookmark', interaction: { field: 'bookmarked', value: true }, selected: false },
  { text: 'Disliked', interaction: { field: 'liked', value: 2 }, selected: false },
];

export const SORT_DIRECTIONS = [
  // { text: 'Recommended', value: '', selected: false },
  { text: 'latest', value: 'desc', selected: true },
  { text: 'oldest', value: 'asc', selected: false },
];

export const PLATFORM = { facebook: 'FACEBOOK', twitter: 'TWITTER', linkedIn: 'LINKEDIN', link: 'LINK' };

export const ACTIVITY = { bookmark: 'bookmark', delete: 'delete', like: 'like' };

export const DOCUMENT = { feed: 'feed', personal: 'personal-document' };

export const FEED_FILTER = {
  recommendDate: 'recommendDate',
  searchType: 'searchType',
};

export const FEED_FILTER_PERIOD = [
  { text: 'all', filter: { field: FEED_FILTER.recommendDate, value: null }, selected: true },
  { text: '1year', filter: { field: FEED_FILTER.recommendDate, value: getPeriod(1, 'year') }, selected: false },
  { text: '6months', filter: { field: FEED_FILTER.recommendDate, value: getPeriod(6, 'month') }, selected: false },
  { text: '3months', filter: { field: FEED_FILTER.recommendDate, value: getPeriod(3, 'month') }, selected: false },
  { text: '1month', filter: { field: FEED_FILTER.recommendDate, value: getPeriod(1, 'month') }, selected: false },
  { text: '1week', filter: { field: FEED_FILTER.recommendDate, value: getPeriod(7, 'date') }, selected: false },
  { text: '1day', filter: { field: FEED_FILTER.recommendDate, value: getPeriod(1, 'date') }, selected: false },
  // { text: 'chooseDate', filter: { field: 'recommendDate', value: {} }, selected: false },
];

function getPeriod(number: number, type: string) {
  const from = new Date();
  switch (type) {
    case 'date':
      from.setUTCDate(from.getUTCDate() - number);
      break;
    case 'month':
      from.setUTCMonth(from.getUTCMonth() - number);
      break;
    default:
      from.setUTCFullYear(from.getUTCFullYear() - number);
      break;
  }

  return {
    from: from.toISOString(),
    to: new Date().toISOString(),
  };
}

export const FEED_FILTER_CATEGORY = [
  { text: 'all', filter: { field: FEED_FILTER.searchType, value: null }, selected: true },
  { text: 'article', filter: { field: FEED_FILTER.searchType, value: 'News' }, selected: false },
  // { text: 'social', filter: { field: FEED_FILTER.searchType, value: 'searchSocial' }, selected: false },
  { text: 'video', filter: { field: FEED_FILTER.searchType, value: 'Video' }, selected: false },
  { text: 'document', filter: { field: FEED_FILTER.searchType, value: 'File' }, selected: false },
];

export default function checkMobile() {
  if (/Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent)) {
    return true;
  } else {
    return false;
  }
}

export const MESSAGES_FEEDBACK = {
  NOT_INTERESTING: 'Not interesting!',
  DO_NOT_LIKE: "I don't like this story",
  REPORT: 'Report this story',
  CANCEL: 'Cancel',
  REPORT_SEND: 'Report sent',
};
