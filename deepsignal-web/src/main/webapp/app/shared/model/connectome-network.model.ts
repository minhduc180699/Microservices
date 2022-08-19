import { ConnectomeNetworkEdge } from './connectome-network-edge.model';
import { ConnectomeNetworkStatus } from './connectome-network-status.model';
import { ConnectomeNetworkVertex } from './connectome-network-vertex.model';

export interface IConnectomeNetwork {
  connectomeId?: string;
  connectomeStatus?: ConnectomeNetworkStatus;
  vertices?: Array<ConnectomeNetworkVertex>;
  edges?: Array<ConnectomeNetworkEdge>;
}

export class ConnectomeNetwork implements IConnectomeNetwork {
  constructor(
    public connectomeId?: string,
    public connectomeStatus?: ConnectomeNetworkStatus,
    public vertices?: Array<ConnectomeNetworkVertex>,
    public edges?: Array<ConnectomeNetworkEdge>
  ) {}
}
