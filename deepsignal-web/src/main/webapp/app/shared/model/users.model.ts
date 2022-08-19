export interface IUserModel {
  id?: any;
  login?: string;
  password?: string;
  firstName?: string;
  lastName?: string;
  email?: string;
  phoneCountry?: string;
  phoneNumber?: string;
  imageUrl?: string;
  activated?: number;
  langKey?: string;
  activationKey?: string;
  resetKey?: string;
  createdBy?: string;
  createdDate?: Date;
  resetDate?: Date;
  lastModifiedBy?: string;
  lastModifiedDate?: Date;
  termOfService?: number;
  receiveNews?: number;
}

export class UserModel implements IUserModel {
  constructor(
    id?: any,
    login?: string,
    password?: string,
    firstName?: string,
    lastName?: string,
    email?: string,
    phoneCountry?: string,
    phoneNumber?: string,
    imageUrl?: string,
    activated?: number,
    langKey?: string,
    activationKey?: string,
    resetKey?: string,
    createdBy?: string,
    createdDate?: Date,
    resetDate?: Date,
    lastModifiedBy?: string,
    lastModifiedDate?: Date,
    termOfService?: number,
    receiveNews?: number
  ) {}
}
