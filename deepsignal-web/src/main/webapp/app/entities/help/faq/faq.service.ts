import axios, { AxiosResponse } from 'axios';
import { IFaqModel } from '@/shared/model/faq.model';
import { ICategoryModel } from '@/shared/model/category.model';
import { PageableModel } from '@/shared/model/pageable.model';

export const API_PATH_FAQ = 'api/faqs';
export const API_PATH_CATEGORY = 'api/category';

export default {
  findAllFaq(pageable?: PageableModel) {
    if (pageable) {
      return axios.get(API_PATH_FAQ + '/findAll?size=' + pageable.size);
    } else {
      return axios.get(API_PATH_FAQ + '/findAll');
    }
  },

  getAllCategoryFaq(type: number): Promise<AxiosResponse<ICategoryModel[]>> {
    return axios.get<ICategoryModel[]>(API_PATH_CATEGORY + '/getCategoryFaq?type=' + type);
  },

  findByCategory(pageable?: PageableModel, category?: string) {
    return axios.get(API_PATH_FAQ + '/findByCategory?size=' + pageable.size + '&code=' + category);
  },

  findByKeyWord(pageable?: PageableModel, keyWord?: string, category?: string) {
    return axios.post(API_PATH_FAQ + '/search?size=' + pageable.size + '&keyWord=' + keyWord + '&code=' + category);
  },
};
