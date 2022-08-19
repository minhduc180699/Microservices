export class ConnectomeNetworkEdge {
  label: string = null;
  from: string = null;
  to: string = null;
  curvature: number;

  constructor(element: any) {
    if (element) {
      this.label = element.label;
      this.to = element.to;
      this.from = element.from;
      //this.curvature = Math.random() * (0.7 - 0.3) + 0.3;
      this.curvature = 0.45;
    }
  }
}
