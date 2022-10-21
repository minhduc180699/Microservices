import { ConnectomeNode } from './connectome-node.model';

export class documentCard {
  id: string = null;
  author: string = null;
  title: string = null;
  content: string = null;
  keyword: string = null;
  type: string = null;
  group: Array<string> = new Array<string>();
  tags: Array<string> = new Array<string>();
  addedAt: Date = null;
  modifiedAt: Date = null;
  url: string = null;
  isAdded = false;
  style = null;
}
