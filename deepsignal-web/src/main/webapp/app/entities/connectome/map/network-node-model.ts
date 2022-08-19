import { FONTSIZE_NODE_IN_MAP, getNodeTypeInNetwork, SIZE_NODE_IN_MAP, TYPE_NODE_IN_MAP } from '@/shared/constants/ds-constants';

export class NetworkNode {
  id: string = null;
  name: string = null;
  type: TYPE_NODE_IN_MAP = TYPE_NODE_IN_MAP.ENTITY;
  fontSize: number = FONTSIZE_NODE_IN_MAP.CLUSTER;
  _color = '#40ff11';
  _textColor = '#40ff11';
  _size = 1;
  serverSize = 1;
  mainCluster = null;
  clusterSet: Array<string> = [];
  constructor(element: any, colorSet?: any, labelColorSet?: any) {
    if (element) {
      this.id = element.id;
      this.name = element.label;
      this.type = getNodeTypeInNetwork(element.p);
      this._size = this.type === TYPE_NODE_IN_MAP.ENTITY ? SIZE_NODE_IN_MAP.ENTITY : SIZE_NODE_IN_MAP.CLUSTER;
      this.fontSize = this.type === TYPE_NODE_IN_MAP.ENTITY ? FONTSIZE_NODE_IN_MAP.ENTITY : FONTSIZE_NODE_IN_MAP.CLUSTER;
      this.serverSize = this._size;
      if (element.clusterSet && element.clusterSet.length > 0) {
        this.clusterSet = element.clusterSet;
        this.mainCluster = element.clusterSet[0];
        if (colorSet && labelColorSet) {
          this._color = colorSet(this.mainCluster);
          this._textColor = labelColorSet(this.mainCluster);
        }
      }
    }
  }

  update(element: any, colorSet?: any, labelColorSet?: any) {
    if (element) {
      this.name = element.label;
      this.type = getNodeTypeInNetwork(element.p);
      this._size = this.type === TYPE_NODE_IN_MAP.ENTITY ? SIZE_NODE_IN_MAP.ENTITY : SIZE_NODE_IN_MAP.CLUSTER;
      this.fontSize = this.type === TYPE_NODE_IN_MAP.ENTITY ? FONTSIZE_NODE_IN_MAP.ENTITY : FONTSIZE_NODE_IN_MAP.CLUSTER;
      this.serverSize = element.size;
      if (element.clusterSet && element.clusterSet.length > 0) {
        this.clusterSet = element.clusterSet;
        this.mainCluster = element.clusterSet[0];
        if (colorSet && labelColorSet) {
          this._color = colorSet(this.mainCluster);
          this._textColor = labelColorSet(this.mainCluster);
        }
      }
    }
  }
}
