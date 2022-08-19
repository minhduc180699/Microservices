import { getNodeTypeInNetwork, TYPE_NODE_IN_MAP, SIZE_NODE_IN_MAP, FONTSIZE_NODE_IN_MAP } from '@/shared/constants/ds-constants';

export class MapNode {
  id: string = null;
  name: string = null;
  type: TYPE_NODE_IN_MAP = TYPE_NODE_IN_MAP.ENTITY;
  level: number = 1;
  parent: MapNode = null;
  children: Array<MapNode> = [];
  Entities: Array<any> = [];
  fontSize: number = FONTSIZE_NODE_IN_MAP.ENTITY;
  path: Array<MapNode> = [];
  _color = '#aa00bb';
  _textColor = '#aa00bb';
  _size = SIZE_NODE_IN_MAP.ENTITY;

  constructor(element: any) {
    if (element) {
      this.id = element.topic_id;
      this.name = element.label;

      this.type = element.entities && element.entities.length === 1 ? TYPE_NODE_IN_MAP.ENTITY : TYPE_NODE_IN_MAP.CLUSTER;
      this._size = this.type === TYPE_NODE_IN_MAP.CLUSTER ? SIZE_NODE_IN_MAP.TOPIC : SIZE_NODE_IN_MAP.ENTITY;
      this.fontSize = this.type === TYPE_NODE_IN_MAP.CLUSTER ? FONTSIZE_NODE_IN_MAP.TOPIC : FONTSIZE_NODE_IN_MAP.ENTITY;
    }
  }
}
