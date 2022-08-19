import { CarouselPeopleModel } from '@/shared/cards/people/feeds-carousel/carousel-people.model';
import { TypePeopleRelated } from '@/shared/cards/people/people-related-content.model';

export class PeopleRankingModel extends CarouselPeopleModel {
  title: string;
  twitter: string;
  documentIds: string[];
  feedIds: string[];
  social: SocialPeople[];
  isActive = false;
  imageUrl: string;
  type: TypePeopleRelated;
  dfCnt: number;
  index: number;
}

export class PeopleAndCompanyModel {
  googleInfobox: GoogleInfoBox;
  imageUrl = '';
  label = '';
  lang: string;
  social: any;
  wikiText: string;
  wikiUrl: string;
  wikipediaInfobox: any;
  type: TypePeopleRelated;

  constructor(props?) {
    if (!props) return;
    Object.assign(this, props);
    if (!this.googleInfobox) {
      this.googleInfobox = this.wikipediaInfobox;
    }
    if (!this.googleInfobox.founders) {
      this.googleInfobox.founders = this.googleInfobox.founder || '';
    }
    this.wikiText = this.wikiText
      ? this.wikiText
      : this.googleInfobox && this.googleInfobox.description
      ? this.googleInfobox.description
      : '';
  }
}

export class GoogleInfoBox {
  advertising: string;
  ceo: string;
  content_license: string;
  description: string;
  description_url: string;
  founded: string;
  founder: string;
  founders: string;
  headquarters: string;
  full_name = '';
  id: string;
  education = '';
  logo: string;
  parent_organization: string;
  people_also_search_for: any[];
  social: any;
  subtitle: string;
  title: string;
  born: string;
  website: string;
}

export class SocialPeople {
  author: string;
  authorId: string;
  content: string;
  description: string;
  favicon: string;
  id: string;
  lang: string;
  link: string;
  orgDate: any;
  serviceType: string;
}
