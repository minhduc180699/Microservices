export const EXCLUDE_API_LOADER = [
  'api/learning/',
  'api/connectome-feed/getStock/',
  'api/',
  'api/connectome/people/getImage',
  'api/connectome/updated/get/',
  'api/connectome/network/updated/get/',
];

export const FILE_TYPE = {
  EXCEL: ['csv', 'xlsx', 'xls'],
  IMAGE: ['jpg', 'png', 'jpeg', 'svg'],
  COMPRESSED: ['zip', 'rar'],
  PPT: ['ppt', 'pptx'],
  DOC: ['doc', 'docx'],
  PDF: ['pdf'],
};

export const UPLOAD_FILE_SUPPORT = ['.xlsx', '.xls', '.doc', '.docx', '.ppt', '.pptx', '.pdf'];

//Connectome Map constants
export const INTERVAL_CONNECTOME_CHECK_UPDATE = 5000; //in ms
export const MAP_BACKGROUND_COLOR = '#00000f'; //in ms
export const MINI_MAP_BACKGROUND_COLOR = '#ffffff';
export const SIZE_NODE_IN_MAP = {
  ROOT: 0,
  MAIN_TOPIC: 50,
  TOPIC: 25,
  CLUSTER: 15,
  BLACK_ENTITY: 1,
  ENTITY: 5,
};

export const FONTSIZE_NODE_IN_MAP = {
  ROOT: 0,
  MAIN_TOPIC: 24,
  CLUSTER: 20,
  TOPIC: 18,
  BLACK_ENTITY: 5,
  ENTITY: 12,
};

export const FONTCOLOR_NODE_IN_MAP = {
  BLACK_ENTITY_STROKE: '#cacaca',
  BLACK_ENTITY: '#212121',
  COMMON_STROKE: '#000000',
  COMMON: '#ffffff',
};

export enum TYPE_NODE_IN_MAP {
  ROOT = 'root',
  TOPIC = 'topic',
  MAIN_TOPIC = 'maintopic',
  CLUSTER = 'c_cluster',
  ENTITY = 'c_entity',
}

export enum TYPE_VERTEX {
  CLUSTER = 'CLUSTER',
  BLACK_ENTITY = 'BLACK_ENTITY',
  ENTITY = 'ENTITY',
  ROOT = 'ROOT',
}

export function getVertexTypeInNetwork(elementType: string) {
  switch (elementType) {
    case TYPE_VERTEX.CLUSTER:
      return TYPE_VERTEX.CLUSTER;
    case TYPE_VERTEX.ENTITY:
      return TYPE_VERTEX.ENTITY;
    case TYPE_VERTEX.BLACK_ENTITY:
      return TYPE_VERTEX.BLACK_ENTITY;
    case TYPE_VERTEX.ROOT:
      return TYPE_VERTEX.ROOT;
    default:
      return TYPE_VERTEX.BLACK_ENTITY;
  }
}

export function getVertexSizeInNetwork(elementType: string) {
  switch (elementType) {
    case TYPE_VERTEX.CLUSTER:
      return SIZE_NODE_IN_MAP.CLUSTER;
    case TYPE_VERTEX.ENTITY:
      return SIZE_NODE_IN_MAP.ENTITY;
    case TYPE_VERTEX.BLACK_ENTITY:
      return SIZE_NODE_IN_MAP.BLACK_ENTITY;
    case TYPE_VERTEX.ROOT:
      return SIZE_NODE_IN_MAP.ROOT;
    default:
      return SIZE_NODE_IN_MAP.BLACK_ENTITY;
  }
}

export function getVertexFontSizeInNetwork(elementType: string) {
  switch (elementType) {
    case TYPE_VERTEX.CLUSTER:
      return FONTSIZE_NODE_IN_MAP.CLUSTER;
    case TYPE_VERTEX.ENTITY:
      return FONTSIZE_NODE_IN_MAP.ENTITY;
    case TYPE_VERTEX.BLACK_ENTITY:
      return FONTSIZE_NODE_IN_MAP.BLACK_ENTITY;
    case TYPE_VERTEX.ROOT:
      return FONTSIZE_NODE_IN_MAP.ROOT;
    default:
      return FONTSIZE_NODE_IN_MAP.BLACK_ENTITY;
  }
}

export const CONNECTOME_STATUS = {
  COMPLETED: 'COMPLETED',
  ENTITY_LINKING: 'ENTITY_LINKING',
};

export enum TYPE_CONNECTOME_MAP_DATA {
  MAP = 'map',
  NETWORK = 'network',
}

export const TYPE_NODE_DESC = {
  WIKI: 'wiki',
  KB: 'kb',
};

export function getNodeTypeInNetwork(elementType: string) {
  switch (elementType) {
    case TYPE_NODE_IN_MAP.CLUSTER:
      return TYPE_NODE_IN_MAP.CLUSTER;
    case TYPE_NODE_IN_MAP.ENTITY:
      return TYPE_NODE_IN_MAP.ENTITY;
    default:
      return TYPE_NODE_IN_MAP.ENTITY;
  }
}

export const ROTATION_STEP = (2 * Math.PI) / 180;

export const IS_SHOW_NOTIFICATION = 'isShowNotification';

export const FILE_TYPE_UPLOAD = {
  URL: 'URL',
  FILE: 'FILE',
  DOWNLOAD: 'DOWNLOAD',
  WEB: 'WEB',
};

export const DATA_FAKE = [
  { key: 1, value: 'a' },
  { key: 2, value: 'b' },
];

export enum GOOGLE_CONFIG {
  CLIENT_ID = '282196142246-bmgl02ms6oohg6r2rslsvuui721se15e.apps.googleusercontent.com',
  API_KEY = 'AIzaSyAEggr0yC_XlgbAykZpKPJk4KSOHvhQf9Q',
  APP_ID = '282196142246', // project number
  DISCOVERY_DOCS = 'https://www.googleapis.com/discovery/v1/apis/drive/v3/rest',
  SCOPES = 'https://www.googleapis.com/auth/drive.readonly',
  MIME_TYPES_SUPPORT = 'application/vnd.openxmlformats-officedocument.presentationml.presentation,application/vnd.ms-powerpoint,application/msword,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet,application/vnd.ms-excel,application/vnd.openxmlformats-officedocument.wordprocessingml.document,application/pdf',
}

export const PAGE = {
  FEED: { name: 'Feed', link: '/feed' },
  PEOPLE: { name: 'People', link: '/people' },
  SIGNALS: { name: 'Signal', link: '/signals' },
  CONNECTOME: { name: 'My Connectome', link: '/my-ai/connectome/2dnetwork' },
  MYAI: { name: 'My AI', link: '/my-ai/learning-center' },
};

export const SESSION_STORAGE_CONSTANTS = {
  FEED_STATUS_TRAINING: 'FEED_STATUS_TRAINING',
  WEATHER: 'WEATHER',
};

export const LOCAL_STORAGE_CONSTANTS = {
  IP: 'IP',
};

export const DOCUMENT_TYPE = {
  FEED: 'Feed',
  PEOPLE: 'People',
  PERSONAL_DOCUMENT: 'PersonalDocument',
};

export function getCunrentTimeZoneOffset() {
  const d = new Date().getTimezoneOffset();
  const minutes = Math.abs(d) % 60;
  const hours = Math.floor(Math.abs(d) / 60);
  const calculation = d > 0 ? '-' : '+';
  return `${calculation}${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}`;
}

//utc +0
import moment from 'moment';
export function cvLocaldateToUtcDate(localDate: Date) {
  return moment(new Date(localDate.getTime() + new Date().getTimezoneOffset() * 60000)).format('yyyyMMDD');
}

export class ResponseCode {
  public static readonly SUCCESS_CODE: string = '000';
  public static readonly FAIL_CODE: string = '044';
}

export class Setting {
  public static readonly DEFAULT_SETTING: any = {
    language: 'US',
    weather: {
      showWeatherCard: true,
      alwaysDetectLocation: true,
      temperatureUnit: 'C',
    },
    stock: {
      showStockCards: true,
      cardStyle: 'View by chart',
    },
  };
}

export const TEMPERATURE_UNIT = {
  F: 'imperial',
  C: 'metric',
};

export const CHART_STOCK_STYLE = {
  LIST: 'View by list',
  CHART: 'View by chart',
};

export const COLLECTION_SERVER = 'http://192.168.9.181:8081/';
