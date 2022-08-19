import { MapNode } from './map-node-model';

export class MapLink {
  id: string = null;
  from: string = null;
  to: string = null;
  source: string = null;
  target: string = null;
  targetNode: MapNode = null;
  _color: string = '#ffffff';
  _textColor: string = '#ffffff';

  constructor(element: any, _targetNode: MapNode) {
    if (element) {
      this.id = element.source + '=>' + element.target;
      this.from = element.source;
      this.to = element.target;
      this.source = element.source;
      this.target = element.target;
      this.targetNode = _targetNode;
    }
  }
}
