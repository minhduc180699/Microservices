import { getVertexTypeInNetwork, TYPE_VERTEX } from '@/shared/constants/ds-constants';
import { CmCollectionStatus } from './cm-collectionStatus.model';
import { ConnectomeNode } from './connectome-node.model';

export class CmCollection {
  collectionId: string = null;
  connectomeId: string = null;
  lang: string = null;
  nodeList: Array<ConnectomeNode> = [];
  requestsList: Array<ConnectomeNode> = [];
  documentIdList: Array<string> = [];
  modifiedDate: Date = null;
  status: CmCollectionStatus = null;

  constructor(element: any) {
    if (element) {
      this.collectionId = element.collectionId;
      this.connectomeId = element.connectomeId;
      this.lang = element.lang;
      this.nodeList = element.connectomeNodeList?.map(x => new ConnectomeNode(x));
      this.requestsList = element.connectomeNodeList?.map(x => new ConnectomeNode(x));
      this.documentIdList = element.documentIdList?.map(x => x);
      this.modifiedDate = element.modifiedDate;
      this.status = new CmCollectionStatus(element.status);
    }
  }
}
