import { ICategoryModel } from '@/shared/model/category.model';
import { IUserModel } from '@/shared/model/users.model';
import { Account } from '@/shared/model/account.model';

export interface IInquiryQuestionModel {
  id?: number;
  category?: ICategoryModel;
  title?: string;
  content?: string;
  emailName?: string;
  name?: string;
  file?: string;
  // status?: number;
  public?: boolean;
  email?: number;
  user?: Account;
  createdDate?: Date;
}

export class InquiryQuestionModel implements IInquiryQuestionModel {
  constructor(
    id?: number,
    category?: ICategoryModel,
    title?: string,
    content?: string,
    emailName?: string,
    name?: string,
    file?: string,
    // status?: number,
    isPublic?: boolean,
    email?: number,
    user?: Account,
    createdDate?: Date
  ) {}
}
