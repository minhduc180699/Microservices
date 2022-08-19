import { StockCodeModel } from '@/shared/model/connectome-feed.model';
import { PeopleRelatedContentModel, TypePeopleRelated } from '@/shared/cards/people/people-related-content.model';
import { PeopleRankingModel } from '@/shared/cards/people/people-ranking/people-ranking.model';

export interface IPeople {
  relatedContent: PeopleRelatedContentModel;
  socialNetwork: any; // Chart Network Social
}

export class PeopleModel implements IPeople {
  relatedContent: PeopleRelatedContentModel;
  socialNetwork: any;
}

export class PeopleResponse {
  id: string;
  connectomeId: string;
  company: PeopleRankingModel[];
  people: PeopleRankingModel[];
  stock: StockCodeModel;
  createdDate: any;
  companyAndPeople: PeopleRankingModel[]; // mixed 2 array company and people

  constructor(props) {
    Object.assign(this, props);
    this.people.map(item => (item.type = TypePeopleRelated.PEOPLE));
    this.company.map(item => (item.type = TypePeopleRelated.COMPANY));
    this.companyAndPeople = [...this.people, ...this.company];
  }
}
