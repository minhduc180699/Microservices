import { getVertexTypeInNetwork, TYPE_VERTEX } from '@/shared/constants/ds-constants';

export class MapNode {
  entityLink: string = null;
  label: string = null;
  size = 10;
  color: string = null;
  nodesLinked: Array<string> = new Array<string>();
  isDisable = false;
}
