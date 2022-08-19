import { getVertexTypeInNetwork, TYPE_VERTEX } from '@/shared/constants/ds-constants';

export class ConnectomeNetworkVertex {
  label: string = null;
  type: string = TYPE_VERTEX.ENTITY;
  mainCluster: string = null;
  entities: Array<string> = [];
  favorite = false;
  disable = false;

  constructor(element: any) {
    if (element) {
      this.label = element.label;
      this.type = getVertexTypeInNetwork(element.type);
      this.mainCluster = new String(element.mainCluster).toString();
      this.entities = element.entities.map(x => x);
      this.favorite = element.favorite;
      this.disable = element.disable;
    }
  }

  public update(element: any) {
    if (element) {
      this.type = getVertexTypeInNetwork(element.type);
      this.mainCluster = new String(element.mainCluster).toString();
      this.entities = element.entities.map(x => x);
      this.favorite = element.favorite;
      this.disable = element.disable;
    }
  }

  public updateOldVertex(element: any) {
    if (element[0]) {
      element[0].type = this.type;
      element[0].mainCluster = this.mainCluster;
      element[0].entities = this.entities.map(x => x);
      element[0].favorite = this.favorite;
      element[0].disable = this.disable;
    }
    return element[0];
  }
}
