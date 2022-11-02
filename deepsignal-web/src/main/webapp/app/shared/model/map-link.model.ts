import { getVertexTypeInNetwork, TYPE_VERTEX } from '@/shared/constants/ds-constants';

export class MapLink {
  entityLink: string = null;
  from: string = null;
  to: string = null;
  label: string = null;
  size = 2;
  color: string = null;
  isDisable = false;
}
