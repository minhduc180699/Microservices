export interface ICategoryModel {
  code?: string;
  type?: number;
}

export class CategoryModel {
  category: ICategoryModel;
  isActive: boolean;

  constructor(category: ICategoryModel, isActive: boolean) {
    this.category = category;
    this.isActive = isActive;
  }
}
