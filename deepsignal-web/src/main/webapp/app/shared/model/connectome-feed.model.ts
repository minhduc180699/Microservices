import { ClusterDocument } from '@/core/home/signals/signal-today-issue/signal-today-issue.model';

export class ConnectomeFeedModel {
  id: string;
  connectomeId: string;
  docId: string;
  timestamp: any;
  topic: string;
  score: any;
  topicSims: TopicSimModel[];
  title: string;
  content: string;
  writerName: string;
  serviceType: string;
  stockCodes: StockCodeModel[];
  publishedAt: any;
  imageLinks: string[];
  sourceId: string;
  favicon: string;
  liked: number;
  shared: SharingMethodModel;
  bookmarked: boolean;
  deleted: boolean;
  memo: number;
  lang: string;
  orgDate: string;
  recommendDate: string;
  searchKeyword: string;
  searchType: string;
  entityLinking: EntityLinkingModel[];
  recommendationType: string;
  clusterDocuments: ClusterDocument[];
  wordClouds: WordCloudModel[];
  neuronNetworkChart: any;
  htmlContent: string;
}

export class TopicSimModel {
  topic: string;
  score: any;
}

export class StockCodeModel {
  market: string;
  stockCode: string;
  isin: string;
  freq: number;
}

export class SharingMethodModel {
  facebook: boolean;
  twitter: boolean;
  linkedIn: boolean;
  link: boolean;
}

export class EntityLinkingModel {
  keyword: any;
  candidates: any[];
}

export class WordCloudModel {
  wordName: string;
  wordCount: number;
}
