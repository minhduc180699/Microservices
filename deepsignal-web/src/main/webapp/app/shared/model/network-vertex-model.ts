import {
  FONTSIZE_NODE_IN_MAP,
  getVertexFontSizeInNetwork,
  getVertexSizeInNetwork,
  getVertexTypeInNetwork,
  TYPE_VERTEX,
} from '@/shared/constants/ds-constants';

export class NetworkVertex {
  label: string = null;
  dfCnt = 0;
  type: string = TYPE_VERTEX.ENTITY;
  _mainCluster: string = null;
  _entities: Array<string> = [];
  _clusters: Array<string> = [];
  mainCluster: NetworkVertex = null;
  entitiesLinked: Array<NetworkVertex> = [];
  clustersLinked: Array<NetworkVertex> = [];
  description: string = null;
  imageUrl: string = null;
  fontSize: number = FONTSIZE_NODE_IN_MAP.CLUSTER;
  _color = '#40ff11';
  _textColor = '#40ff11';
  _size = 1;

  constructor(element: any, colorSet?: any, labelColorSet?: any) {
    if (element) {
      this.label = element.label;
      this.dfCnt = element.dfCnt;
      this.type = getVertexTypeInNetwork(element.type);
      this.description = element.description;
      this.imageUrl = element.imageUrl;
      this._mainCluster = new String(element.mainCluster).toString();
      this._entities = element.entities.map(x => x);
      this._clusters = element.clusters.map(x => x);
      this._size = getVertexSizeInNetwork(this.type);
      this.fontSize = getVertexFontSizeInNetwork(this.type);
      if (this._mainCluster && colorSet && labelColorSet) {
        this._color = colorSet(this._mainCluster);
        this._textColor = labelColorSet(this._mainCluster);
      }
    }
  }

  setVertexRelationship(NetworkVertex: Array<NetworkVertex>) {
    this.mainCluster = null;
    this.entitiesLinked = [];
    this.clustersLinked = [];

    if (this._mainCluster) {
      this.mainCluster = NetworkVertex.find(element => element.label === this._mainCluster);
    }

    if (this._entities && this._entities.length > 0) {
      this._entities.forEach(item => this.entitiesLinked.push(NetworkVertex.find(element => element.label === item)));
    }

    if (this._clusters && this._clusters.length > 0) {
      this._clusters.forEach(item => this.clustersLinked.push(NetworkVertex.find(element => element.label === item)));
    }
  }

  update(element: any, colorSet?: any, labelColorSet?: any) {
    if (element) {
      this.dfCnt = element.dfCnt;
      this.type = getVertexTypeInNetwork(element.type);
      this.description = element.description;
      this.imageUrl = element.imageUrl;
      this._mainCluster = new String(element.mainCluster).toString();
      this._entities = element.entities.map(x => x);
      this._clusters = element.clusters.map(x => x);
      this._size = getVertexSizeInNetwork(this.type);
      this.fontSize = getVertexFontSizeInNetwork(this.type);
      if (this._mainCluster && colorSet && labelColorSet) {
        this._color = colorSet(this._mainCluster);
        this._textColor = labelColorSet(this._mainCluster);
      }
    }
  }
}
