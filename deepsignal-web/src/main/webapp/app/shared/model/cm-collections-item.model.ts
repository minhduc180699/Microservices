import { getVertexTypeInNetwork, TYPE_VERTEX } from '@/shared/constants/ds-constants';
import { ConnectomeNode } from './connectome-node.model';

export class CmCollectionsItem {
  collectionId: string = null;
  documentIdList: Array<string> = [];
  modified: Date = null;

  constructor(element: any) {
    if (element) {
      this.collectionId = element.collectionId;
      this.documentIdList = element.documentIdList?.map(x => x);
      this.modified = element.modified;
    }
  }
}
