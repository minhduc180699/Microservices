export interface IConnectomeNetworkStatus {
  connectomeId?: string;
  status?: string;
  lang?: string;
  whenLearningStarted?: string;
  lastUpdatedAt?: string;
}
//  firstCreatedDate?: string;
//  numberOfTimesConnectomeWasUpdated?: number;
//  numberOfDocuments?: number;
//  numberOfUniqueEntities?: number;
export class ConnectomeNetworkStatus implements IConnectomeNetworkStatus {
  constructor(
    public connectomeId?: string,
    public status?: string,
    public lang?: string,

    public whenLearningStarted?: string,
    public lastUpdatedAt?: string
  ) {}
  //    public firstCreatedDate?: string,
  //    public numberOfTimesConnectomeWasUpdated?: number,
  //    public numberOfDocuments?: number,
  //    public numberOfUniqueEntities?: number
}
