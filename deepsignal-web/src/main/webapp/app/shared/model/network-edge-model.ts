import { NetworkVertex } from './network-vertex-model';

export class NetworkEdge {
  label: string = null;
  _from: string = null;
  _to: string = null;
  source: string = null;
  target: string = null;
  from: NetworkVertex = null;
  to: NetworkVertex = null;
  _color = '#aa00bb';
  _size = 1;
  _length = 100;
  curvature: number;

  constructor(element: any) {
    if (element) {
      this.label = element.label;
      this._to = element.to;
      this._from = element.from;
      this.target = element.to;
      this.source = element.from;
    }
  }

  setVertexRelationship(NetworkVertex: Array<NetworkVertex>) {
    this.from = null;
    this.to = null;

    if (this._from) {
      this.from = NetworkVertex.find(element => element.label === this._from);
    }

    if (this._to) {
      this.to = NetworkVertex.find(element => element.label === this._to);
    }

    if (this.to && this.from) {
      this._color = this.to._color;
      this._size = (this.from._size + this.to._size) * 0.25;
      this._length = (1 / this.to._size) * 1000 + 30;
    }
    this.curvature = Math.random() * (0.7 - 0.3) + 0.3;
  }
}
