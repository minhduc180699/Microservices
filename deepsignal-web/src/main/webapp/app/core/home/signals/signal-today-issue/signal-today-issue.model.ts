import { SIGNAL_KEYWORD_TYPE } from '@/core/home/signals/signal-tracking-issue/signal-keyword.model';

export class SignalTodayIssueModel {
  clusterDocuments: ClusterDocument[];
  connectomeId: string;
  displayOrder: number;
  id: string;
  isDelete: any;
  keywords: string;
  mainKeyword: string;
  neuronNetworkChart: any;
  signalId: number;
  signalType: SIGNAL_KEYWORD_TYPE;
  wordClouds: [];
  workDay: string;
}

export class ClusterDocument {
  connectomeId: string;
  content: string;
  docId: string;
  favicon: string;
  id: string;
  imageLinks: any;
  lang: string;
  sourceId: string;
  title: string;
  words: string;
  writerName: string;
}
