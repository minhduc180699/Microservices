import { getVertexTypeInNetwork, TYPE_VERTEX } from '@/shared/constants/ds-constants';

export class ConnectomeNode {
  id: string = null;
  label: string = null;
  weight = 0;
  relatedDocuments: Array<string> = [];
  keywordList: Array<string> = [];
  linkedNodes: Array<string> = [];
  favorite = false;
  disable = false;

  constructor(element: any) {
    if (element) {
      this.id = element.id;
      this.label = element.label;
      this.relatedDocuments = element.relatedDocuments?.map(x => x);
      if (element.keywordList) this.keywordList = element.keywordList?.map(x => x);
      this.linkedNodes = element.linkedNodes?.map(x => x);
      this.weight = element.weight;
    }
  }
}
