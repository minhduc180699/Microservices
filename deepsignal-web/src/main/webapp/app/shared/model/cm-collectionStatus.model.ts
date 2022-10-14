import { getVertexTypeInNetwork, TYPE_VERTEX } from '@/shared/constants/ds-constants';
import { ConnectomeNode } from './connectome-node.model';

export class CmCollectionStatus {
  code = 0;
  message: string = null;

  constructor(element: any) {
    if (element) {
      this.code = element.code;
      this.message = element.message;
    }
  }
}
