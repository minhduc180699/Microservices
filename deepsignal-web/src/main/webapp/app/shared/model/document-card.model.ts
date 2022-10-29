import { ConnectomeNode } from './connectome-node.model';

export class documentCard {
  [x: string]: any;
  id: string = null;
  author: string = null;
  title: string = null;
  content: string = null;
  keyword: string = null;
  type: string = null;
  group: Array<string> = new Array<string>();
  tags: Array<string> = new Array<string>();
  isGroup = false;
  addedAt: any = null;
  modifiedAt: any = null;
  url: string = null;
  isAdded = false;
  style = null;
  totalDocuments: number = null;
  images: Array<string> = new Array<string>();
  favicon: string;
  constructor() {
    this.content = '';
    this.title = '';
  }
}
