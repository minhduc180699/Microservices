import { NetworkNode } from './network-node-model';

export class NetworkLink {
  id: string = null;
  name: string = null;
  from: string = null;
  to: string = null;
  source: string = null;
  target: string = null;
  curvature: number;
  _color = '#aa00bb';
  _size = 1;
  _length = 100;
  constructor(element: any, sourceNode: NetworkNode, targetNode: NetworkNode) {
    if (element) {
      this.id = element.from + '=>' + element.to;
      this.to = element.to;
      this.from = element.from;
      this.target = element.to;
      this.source = element.from;
      this._color = sourceNode._color;
      this._size = (sourceNode._size + targetNode._size) * 0.25;
      this._length = (1 / targetNode._size) * 1000 + 30;
      this.curvature = Math.random() * (0.7 - 0.3) + 0.3;
    }
  }

  update(sourceNode: NetworkNode, targetNode: NetworkNode) {
    this._color = sourceNode._color;
    this._size = (sourceNode._size + targetNode._size) * 0.25;
    this._length = (1 / targetNode._size) * 1000 + 30;
  }
}
