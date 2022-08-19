import { PageableModel } from '@/shared/model/pageable.model';

export class DetailDoc {
  author: string;
  favicon: string;
  id: string;
  internalsearch: boolean;
  keyword: string;
  lang: string;
  link: string;
  org_date: any;
  searchType: string;
  serviceType: string;
  title: string;
}

export class SwiperDetailRelated {
  class = 'col-sm-6 col-md-4';
  contents: DetailDoc[];

  init(detailRelatedModel: DetailDoc[]): SwiperDetailRelated[] {
    const end = Math.ceil(detailRelatedModel.length / 3);
    const swipers = [];
    let dataClone = detailRelatedModel;
    for (let i = 0; i < end; i++) {
      const swiper = new SwiperDetailRelated();
      const [a, b, c, ...rest] = dataClone;
      swiper.contents = [a, b, c].filter(item => item); // filter item null or undefined when destructuring
      dataClone = rest;
      swipers.push(swiper);
    }
    return swipers;
  }
}
