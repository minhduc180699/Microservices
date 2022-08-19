export interface IConnectomeModel {
  connectomeId?: string;
  connectomeName?: string;
  connectomeJob?: string;
  description?: string;
  user?: any;
}

export class ConnectomeModel implements IConnectomeModel {
  constructor(
    public connectomeId?: string,
    public connectomeName?: string,
    public connectomeJob?: string,
    public description?: string,
    public user?: any
  ) {}
}
