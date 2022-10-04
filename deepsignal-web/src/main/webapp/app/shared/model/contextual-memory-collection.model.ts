import { getVertexTypeInNetwork, TYPE_VERTEX } from '@/shared/constants/ds-constants';
import { ConnectomeNode } from './connectome-node.model';

export class ContextualMemoryCollection {
  collectionId: string = null;
  connectomeId: string = null;
  connectomeNodeList: Array<ConnectomeNode> = [];
  documentIdList: Array<string> = [];
  keywordList: Array<string> = [];
  lang: string = null;

  constructor(element: any) {
    if (element) {
      this.collectionId = element.collectionId;
      this.connectomeId = element.connectomeId;
      this.connectomeNodeList = element.connectomeNodeList?.map(x => new ConnectomeNode(x));
      this.documentIdList = element.documentIdList?.map(x => x);
      this.keywordList = element.keywordList?.map(x => x);
      this.lang = element.lang;
    }
  }
}
