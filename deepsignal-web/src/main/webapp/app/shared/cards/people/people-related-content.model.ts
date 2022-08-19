import { PeopleRankingModel, SocialPeople } from '@/shared/cards/people/people-ranking/people-ranking.model';
import { PeopleResponse } from '@/core/home/people/people.model';
import { CarouselPeopleModel } from '@/shared/cards/people/feeds-carousel/carousel-people.model';

export class PeopleRelatedContentModel extends PeopleResponse {
  title: string;
  peopleRanking: PeopleRankingModel[];
  peopleCarouselCurrent: CarouselPeopleModel;
  indexPeopleCurrent: number;
  socialPeoples: SocialPeople[];
  isHidden: boolean;

  constructor(props, index?) {
    if (!props) return;
    super(props);
    this.peopleRanking = this.companyAndPeople;
    this.indexPeopleCurrent = index ? index : 0;
    // this.socialPeoples = this.company[this.indexPeopleCurrent].social;
    this.isHidden = false;
  }

  chosePeopleCarousel(item: CarouselPeopleModel, index?) {
    this.peopleCarouselCurrent = item;
    this.title = this.peopleRanking[index ? index : this.indexPeopleCurrent].title;
    // this.socialPeoples = this.company[index].social;
  }
}

export enum TypePeopleRelated {
  PEOPLE = 'PEOPLE',
  COMPANY = 'COMPANY',
}
