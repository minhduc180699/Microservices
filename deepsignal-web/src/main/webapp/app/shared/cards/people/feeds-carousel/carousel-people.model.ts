import { PageableModel } from '@/shared/model/pageable.model';
import { CardModel } from '@/shared/cards/card.model';
import { CARD_SIZE, CARD_TYPE } from '@/shared/constants/feed-constants';

export class CarouselPeopleModel extends PageableModel {
  cardPeople: CardModel;
  cardModels: CardModel[];
  feedIds: string[];
  documentIds: string[];
  dfCnt = 0;

  constructor(cardModels?: CardModel[], size?, dfCnt?) {
    super(cardModels, 1, size ? size : 4);
    this.cardModels = cardModels;
    this.dfCnt = dfCnt ? dfCnt : 0;
    // this.cardPeople = this.initCardPeople();
  }

  removeCard(cardModel: CardModel) {
    this.cardModels = this.cardModels.filter(item => item.id !== cardModel.id);
    this.contentsOrigin = this.cardModels;
    this.paging();
  }

  initCardPeople() {
    return {
      dataTemplate: CARD_TYPE.PEOPLE,
      dataSize: CARD_SIZE._1_2,
    } as CardModel;
  }
}
