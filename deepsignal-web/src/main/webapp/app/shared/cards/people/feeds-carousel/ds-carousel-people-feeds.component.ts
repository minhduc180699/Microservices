import { Component, Inject, Prop, PropSync, Vue, Watch } from 'vue-property-decorator';
import DsCardDefault from '@/shared/cards/default/ds-card-default.vue';
import DsCardTrendPeople from '@/shared/cards/trend-people/ds-card-trend-people.vue';
import FeedService from '@/core/home/feed/feed.service';
import { CarouselPeopleModel } from '@/shared/cards/people/feeds-carousel/carousel-people.model';
import { DOCUMENT_TYPE } from '@/shared/constants/ds-constants';
import { CardModel } from '@/shared/cards/card.model';
import { PeopleRankingModel } from '@/shared/cards/people/people-ranking/people-ranking.model';
import { LearnedDocumentService } from '@/service/learned-document.service';

@Component({
  components: {
    'ds-card-default': DsCardDefault,
    'ds-card-trend-people': DsCardTrendPeople,
  },
})
export default class DsPeopleFeeds extends Vue {
  size = 4;
  @Inject('feedService') private feedService: () => FeedService;
  @Inject('learnedDocumentService') private learnedDocumentService: () => LearnedDocumentService;
  pageableCarouselPeople = new CarouselPeopleModel([]);
  @Prop(Object) readonly cardPeople: CardModel;
  isShow = false;
  goToLearningCenter = false;
  dataSizeCs = '2x1';

  @Watch('cardPeople')
  detectPeopleCarouselChange(data) {
    this.pageableCarouselPeople.cardPeople = data;
    // this.goToLearningCenter = false;
  }

  getCardsInPeopleRanking(peopleRankingCurrent: PeopleRankingModel) {
    this.isShow = true;
    this.goToLearningCenter = false;
    this.pageableCarouselPeople.contents = [];
    if (!peopleRankingCurrent || !peopleRankingCurrent.feedIds) return;
    if (!peopleRankingCurrent.feedIds) return;
    this.$getFeedById(peopleRankingCurrent.feedIds, true)
      .then((res: any) => {
        let cardModels = [];
        if (res && res.data.connectomeFeeds) {
          cardModels = this.convertToCardModel(res.data.connectomeFeeds);
        }
        // this.pageableCarouselPeople.dfCnt = peopleRankingCurrent.dfCnt;
        this.getDocumentByIds(peopleRankingCurrent.documentIds, cardModels, peopleRankingCurrent.dfCnt);
      })
      .catch(() => {
        this.goToLearningCenter = true;
      });
  }

  getDocumentByIds(ids: string[], cardFeeds?, dfCnt?) {
    if ((!ids || ids.length == 0) && cardFeeds) {
      this.pageableCarouselPeople = new CarouselPeopleModel([...cardFeeds], this.size, dfCnt);
      this.setSizeCardLearnMore();
      return;
    }
    if (!ids || ids.length == 0) {
      this.setSizeCardLearnMore();
      return;
    }
    this.learnedDocumentService()
      .getByIds(ids, true)
      .then(res => {
        let cardModels = [];
        if (res.data && res.data.connectomePersonalDocuments) {
          cardModels = this.convertToCardFromDocuments(res.data.connectomePersonalDocuments);
        }
        this.pageableCarouselPeople = new CarouselPeopleModel([...cardModels, ...cardFeeds], this.size, dfCnt);
        this.setSizeCardLearnMore();
      })
      .catch(() => {
        this.goToLearningCenter = true;
        this.isShow = false;
      });
  }

  isCheckPage = false;
  setSizeCardLearnMore() {
    this.isCheckPage = false;
    if (this.pageableCarouselPeople.totalElements == 0) {
      this.dataSizeCs = '2x1';
      this.goToLearningCenter = true;
    } else {
      if (this.pageableCarouselPeople.totalElements % 2 != 0) this.dataSizeCs = '1x1';
      else {
        if (this.pageableCarouselPeople.totalElements % 4 == 0) {
          this.pageableCarouselPeople.contentsOrigin.length += 1;
          this.pageableCarouselPeople.totalElements += 1;
          this.pageableCarouselPeople.totalPages += 1;
          this.isCheckPage = true;
        }
        this.dataSizeCs = '2x1';
      }
      if (this.pageableCarouselPeople.totalElements < 4) this.goToLearningCenter = true;
    }
    this.isShow = false;
  }

  async $getFeedById(ids: string[], isDeleted?) {
    if (!ids || ids.length == 0) return;
    return new Promise((resolve, reject) => {
      this.feedService()
        .getFeedByIds(ids, isDeleted)
        .then(res => resolve(res))
        .catch(err => reject(err));
    });
  }

  public convertToCardFromDocuments(items) {
    if (!items) return;
    const cards = [];
    items.forEach(item => {
      item.documentType = DOCUMENT_TYPE.PEOPLE;
      item.imageLinks = item.imageUrl ? item.imageUrl : [];
      item.writerName = item.author;
      item.recommendDate = item.originDate ? item.originDate : item.publishedAt;
      item.favicon = item.faviconUrl ? item.faviconUrl : item.faviconBase64;
      const cardItem = new CardModel(item as any, 'people');
      cardItem.dataSize = '1x1';
      cards.push(cardItem);
    });
    return cards;
  }

  public convertToCardModel(items) {
    if (!items) return;
    const card = [];
    items.forEach((item, index) => {
      item.documentType = DOCUMENT_TYPE.PEOPLE;
      const cardItem = new CardModel(item as any, 'people');
      cardItem.dataSize = '1x1';
      card.push(cardItem);
    });
    return card;
  }

  changePage(isNext) {
    this.goToLearningCenter = false;
    if (
      this.pageableCarouselPeople.page + 1 === this.pageableCarouselPeople.totalPages ||
      this.pageableCarouselPeople.page === this.pageableCarouselPeople.totalPages
    ) {
      if (isNext) {
        if (this.isCheckPage) {
          this.pageableCarouselPeople.nextPage(true);
          this.pageableCarouselPeople.contents = [];
        } else this.pageableCarouselPeople.nextPage();

        this.goToLearningCenter = true;
      } else {
        this.pageableCarouselPeople.previousPage();
        if (this.pageableCarouselPeople.contents?.length < 4) this.goToLearningCenter = true;
      }
    } else isNext ? this.pageableCarouselPeople.nextPage() : this.pageableCarouselPeople.previousPage();
  }

  hiddenCard(item) {
    this.$store.commit('setShow', false);
    // if (this.dataTransfer[this.num].card.length / 6 < this.next && this.next > 1) {
    //   this.next--;
    // }
    // this.getCard();
    this.pageableCarouselPeople.removeCard(item);
  }

  handleActivity(item, activity) {
    this.$emit('handleActivity', item, activity, this.pageableCarouselPeople);
  }

  learningKeyword() {
    this.$router.push('/my-ai/learning-center');
  }
}
