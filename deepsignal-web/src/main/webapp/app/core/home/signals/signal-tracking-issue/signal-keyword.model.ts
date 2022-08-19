import { PageableModel } from '@/shared/model/pageable.model';

// This class use to define object which map with view of component, like: input, chips...
class SignalKeywordModelShowing {
  messageKeyword: string;
  messageMainKeyword: string;
  isEdit: boolean;
  keywordsToShow: string[];
  signalKeywordCloneDeep: SignalKeywordModel; // to save data when user clicked to edit
  tabNameActive: string;
  isValidKeyword = false;
  isValidMainKeyword = false;
  minNumKeyword = 2;
  maxNumKeyword = 10;

  constructor() {
    this.keywordsToShow = [];
  }
}

class ClusterDocuments {
  connectomeId: string;
  content: string;
  docId: string;
  favicon: string;
  id: string;
  imageLinks: string[];
  lang: string;
  sourceId: string;
  title: string;
  words: string;
  writerName: string;
}

// This class use to map with response from API
export class SignalKeywordModel extends SignalKeywordModelShowing {
  id: number;
  keywords: string;
  mainKeyword: string;
  language: string;
  type: SIGNAL_KEYWORD_TYPE;
  displayOrder: number;
  lastModifiedDate: string;
  signalId: number;
  neuronNetworkChart: any;
  wordClouds: any;
  clusterDocuments: ClusterDocuments[];
  pageableClusterDocuments: PageableModel;
  timeSeries: any;
  createdDate: any;
  status: any;

  constructor(language?: string) {
    super();
    this.language = language ? language : 'en';
  }

  init(signalKeywordModel: SignalKeywordModel) {
    // can using Object.assign()
    this.id = signalKeywordModel.id;
    this.keywords = signalKeywordModel.keywords;
    this.mainKeyword = signalKeywordModel.mainKeyword;
    this.language = signalKeywordModel.language;
    this.type = signalKeywordModel.type;
    this.displayOrder = signalKeywordModel.displayOrder;
    this.lastModifiedDate = signalKeywordModel.lastModifiedDate;
    this.signalId = signalKeywordModel.signalId;
    this.neuronNetworkChart = signalKeywordModel.neuronNetworkChart;
    this.wordClouds = signalKeywordModel.wordClouds;
    this.clusterDocuments = signalKeywordModel.clusterDocuments;
    this.pageableClusterDocuments = new PageableModel(signalKeywordModel.clusterDocuments, 1, 4);
    this.isEdit = false;
    this.tabNameActive = 'home' + this.id;
    this.keywordsToShow = this.getKeywordsShowFromKeywords(signalKeywordModel.keywords);
    this.timeSeries = this.initTimeSeries(signalKeywordModel.timeSeries, signalKeywordModel.keywords);
    this.setSignalKeywordCloneDeep(signalKeywordModel);
  }

  getKeywordsShowFromKeywords(keywords: string) {
    if (!keywords) {
      return;
    }
    return keywords.split(CHARACTER_SPLIT_KEYWORD);
  }

  setSignalKeywordCloneDeep(signalKeyword: SignalKeywordModel) {
    this.signalKeywordCloneDeep = signalKeyword;
  }

  getSignalKeywordCloneDeep(): SignalKeywordModel {
    return this.signalKeywordCloneDeep;
  }

  static prepareSignalModelToSave(signalKeywordModel: SignalKeywordModel): SignalKeywordModel {
    if (this.isValidSave(signalKeywordModel)) {
      return;
    }
    delete signalKeywordModel.signalKeywordCloneDeep;
    signalKeywordModel.keywords = signalKeywordModel.keywordsToShow.join(CHARACTER_SPLIT_KEYWORD + ' ');
    signalKeywordModel.type = signalKeywordModel.type || SIGNAL_KEYWORD_TYPE.IT;
    signalKeywordModel.id = signalKeywordModel.signalId;
    signalKeywordModel.status = 0;
    //get time UTC +0
    signalKeywordModel.createdDate = new Date(new Date().getTime() + new Date().getTimezoneOffset() * 60000);
    return signalKeywordModel;
  }

  static isDuplicateKeyword(signalKeywordModel: SignalKeywordModel): boolean {
    return signalKeywordModel.keywordsToShow.includes(signalKeywordModel.messageKeyword);
  }

  static isValidSave(signalModel: SignalKeywordModel): boolean {
    signalModel.isValidMainKeyword = false;
    signalModel.isValidKeyword = false;
    if (
      !signalModel.keywordsToShow ||
      signalModel.keywordsToShow.length < signalModel.minNumKeyword ||
      signalModel.keywordsToShow.length > signalModel.maxNumKeyword
    ) {
      signalModel.isValidKeyword = true;
      return true;
    }
    if (!signalModel.mainKeyword) {
      signalModel.isValidMainKeyword = true;
      return true;
    }
    return false;
  }

  initTimeSeries(value, keys) {
    const initValue = {
      x: [],
      y: [],
      legend: [],
    };

    const keyArray = keys.split(',');

    keyArray.forEach(item => {
      initValue.legend.push(item.trimStart());
    });

    for (let i = 0; i < value.timelineData[0].value.length; i++) {
      const detail = {
        name: '',
        type: 'line',
        stack: '',
        data: [],
      };
      detail.name = initValue.legend[i];
      value.timelineData.forEach((item, index) => {
        detail.data.push(item.value[i]);
        if (i < 1) {
          initValue.x.push(this.convertTimeStamps(item.time));
        }
      });
      initValue.y.push(detail);
    }
    console.log('value-issue-tracking', initValue);
    return initValue;
  }

  convertTimeStamps = time => {
    const date = new Date(time * 1000);
    const year = date.getFullYear();
    const month = date.getMonth();
    const day = date.getDate();
    return year + '-' + (month + 1) + '-' + day;
  };
}

export enum SIGNAL_KEYWORD_TYPE {
  IT = 'IT',
  AD = 'AD',
  TI = 'TI',
}

export const CHARACTER_SPLIT_KEYWORD = ',';
