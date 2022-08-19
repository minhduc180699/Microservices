export class NodeNetworkChart {
  id: string;
  name: string;
  symbolSize: string;
  x: number;
  y: number;
  value: number;
  category?: number;
}

export class LinkNetworkChart {
  source: string;
  target: string;
}

export class CategoriesNetworkChart {
  name: string;
}
