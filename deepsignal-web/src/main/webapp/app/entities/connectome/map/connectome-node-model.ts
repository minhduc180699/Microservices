import { TYPE_CONNECTOME_MAP_DATA, TYPE_NODE_IN_MAP } from '@/shared/constants/ds-constants';
import { MapNode } from './map-node-model';

export class ConnectomeNode {
  public origin: TYPE_CONNECTOME_MAP_DATA = null;
  public type: TYPE_NODE_IN_MAP = null;
  public label: string = null;
  public id: string = null;
  public parents: Array<MapNode> = [];
}
