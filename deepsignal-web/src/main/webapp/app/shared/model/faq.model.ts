import { ICategoryModel } from '@/shared/model/category.model';

export interface IFaqModel {
  faqId?: number;
  category?: ICategoryModel;
  title?: string;
  question?: string;
  answer?: string;
  file?: string;
  viewCount?: number;
  createdDate?: Date;
  note?: string;
}

export class FaqModel {
  faq?: IFaqModel;
  isActive?: boolean;
  constructor(faq: IFaqModel, isActive: boolean) {
    this.faq = faq;
    this.isActive = isActive;
  }
}

// export class FaqModel implements IFaqModel {
//   constructor(
//     public faqId?: number,
//     public categoryCode?: string,
//     public title?: string,
//     public question?: string,
//     public answer?: string,
//     public file?: string,
//     public viewCount?: number,
//     public createdDate?: Date,
//     public note?: string,
//   ) {}
// }
