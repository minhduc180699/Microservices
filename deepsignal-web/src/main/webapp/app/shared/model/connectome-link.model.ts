import { ConnectomeNode } from './connectome-node.model';

export class ConnectomeLink {
  label: string = null;
  from: string = null;
  to: string = null;
  curvature: number;
  weight: number;

  constructor(source: string, target: string) {
    this.label = source + '=>' + target;
    this.to = target;
    this.from = source;
    //this.curvature = Math.random() * (0.7 - 0.3) + 0.3;
    this.curvature = 0.45;
    this.weight = 1;
  }
}
