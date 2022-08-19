import { Component, Inject, PropSync, Vue, Watch } from 'vue-property-decorator';
import DsCardFooter from '@/shared/cards/footer/ds-card-footer.vue';
import DsCardPeopleRanking from '@/shared/cards/people/people-ranking/ds-card-people-ranking.vue';
import DsCarouSelPeopleFeeds from '@/shared/cards/people/feeds-carousel/ds-carousel-people-feeds.vue';
import axios from 'axios';
import { ACTIVITY } from '@/shared/constants/feed-constants';
import DsCardSocialNetworkAnalysis from '@/shared/cards/social-network-analysis/ds-card-social-network-analysis.vue';
import FeedService from '@/core/home/feed/feed.service';
import { PeopleResponse } from '@/core/home/people/people.model';
import { PeopleRelatedContentModel } from '@/shared/cards/people/people-related-content.model';
import { CarouselPeopleModel } from '@/shared/cards/people/feeds-carousel/carousel-people.model';
import PeopleService from '@/core/home/people/people.service';
import { PeopleAndCompanyModel, PeopleRankingModel, SocialPeople } from '@/shared/cards/people/people-ranking/people-ranking.model';
import { CardModel } from '@/shared/cards/card.model';
const mapNetworkStore = namespace('mapNetworkStore');
import { namespace } from 'vuex-class';
const networkStore = namespace('connectomeNetworkStore');

import DsCardPeopleSocial from '@/shared/cards/people/people-social/ds-card-people-social.vue';
import { ConnectomeNetworkVertex } from '@/shared/model/connectome-network-vertex.model';

const TOTAL_CARD_PORTION = 6;

@Component({
  components: {
    'ds-card-footer': DsCardFooter,
    'ds-card-people-ranking': DsCardPeopleRanking,
    'ds-carousel-people-feeds': DsCarouSelPeopleFeeds,
    DsCardSocialNetworkAnalysis,
    DsCardPeopleSocial,
  },
})
export default class DsCardPeople extends Vue {
  private page = 0;
  private size = 10;
  private orderBy = '_id';
  private sortDirection = 'desc';
  private firstPage = [];
  CARD_PORTION = 0;
  hashIndex = 0;
  @Inject('feedService') private feedService: () => FeedService;
  peopleRelated: PeopleRelatedContentModel = new PeopleRelatedContentModel(null);
  peopleRanking: PeopleRankingModel[] = [];
  cardPeople = {};
  hashPeople = '';
  socialPeoples: SocialPeople[] = [];
  goToLearningCenter = false;

  peopleHidden: PeopleRankingModel[] = [];
  peopleHiddenDetail: PeopleRankingModel = new PeopleRankingModel(null);

  @networkStore.Getter
  public lang!: string;

  @mapNetworkStore.Action
  public setSelectedEntity!: (payload: { title: string; srcLang: string }) => void;

  @mapNetworkStore.Action
  public clearSelectedEntity!: () => void;

  @networkStore.Action
  public toggleEntityFavorite!: (payload: { title: string }) => Promise<any>;

  @networkStore.Action
  public toggleEntityDisability!: (payload: { title: string }) => Promise<any>;

  @networkStore.Getter
  public labelHidden;
  isHidden = false;

  @Inject('peopleService')
  private peopleService: () => PeopleService;

  peopleOrCompany: any = {};

  @Watch('$route.hash')
  change(value) {
    if (value) {
      let index = 0;
      if (value.includes('#people:')) {
        this.hashPeople = decodeURI(this.$route.hash.split('#people:')[1]);
        this.peopleRelated.peopleRanking.forEach((item, ind) => {
          if (this.hashPeople.toLowerCase() === item.title.toLowerCase()) {
            index = ind;
          }
        });
      }
      this.handleChangeFromHash(this.peopleRelated.peopleRanking[index], index);
    }
  }

  @Watch('labelHidden')
  onVerticesUpdated(data: any) {
    this.peopleRelated.peopleRanking = this.peopleRelated.peopleRanking.filter(item => item.title != data);
    this.peopleRanking = this.peopleRelated.peopleRanking;
    this.handleChangeFromHash(this.peopleRelated.peopleRanking[this.hashIndex], this.hashIndex);
  }

  created() {
    this.$root.$on('update-new-lang', this.loadPeople);
  }

  mounted() {
    if (this.$route.hash.includes('#people:')) {
      this.hashPeople = decodeURI(this.$route.hash.split('#people:')[1]);
    } else {
      this.hashIndex = 0;
    }
    this.loadPeople();
  }

  loadPeople(newLang?) {
    this.goToLearningCenter = true;
    if (this.getParam()) {
      axios
        .get(this.getParam(), {
          params: {
            page: this.page,
            size: this.size,
            orderBy: this.orderBy,
            sortDirection: this.sortDirection,
            isGetStock: false,
            lang: newLang ? newLang : localStorage.getItem('currentLanguage') || 'en',
          },
        })
        .then(res => {
          const data = res.data.results as PeopleResponse;
          this.peopleRelated = new PeopleRelatedContentModel(data, this.hashIndex);
          this.peopleRelated.companyAndPeople.sort((a, b) => (a.dfCnt > b.dfCnt ? -1 : 1));
          if (this.hashPeople && this.hashPeople != '') {
            this.peopleRelated.companyAndPeople.forEach((item, index) => {
              if (this.hashPeople.toLowerCase() === item.title.toLowerCase()) {
                this.hashIndex = index;
              }
            });
          }
          this.peopleRelated.peopleCarouselCurrent = {
            feedIds: this.peopleRelated.companyAndPeople[this.hashIndex].feedIds,
            documentIds: this.peopleRelated.companyAndPeople[this.hashIndex].documentIds,
            dfCnt: this.peopleRelated.companyAndPeople[this.hashIndex].dfCnt,
          } as CarouselPeopleModel;
          this.peopleRelated.chosePeopleCarousel(this.peopleRelated.peopleCarouselCurrent, this.hashIndex);
          // @ts-ignore
          this.$refs['carouselPeople'].getCardsInPeopleRanking(this.peopleRelated.peopleCarouselCurrent);
          this.getRisingPeopleCompany(this.peopleRelated.companyAndPeople.map(item => item.title));
          this.goToLearningCenter = false;
        })
        .catch(() => {
          this.goToLearningCenter = true;
        });
    }
  }

  getRisingPeopleCompany(titles: string[]) {
    const peopleAndCompany = {
      sourceLang: this.$store.getters.currentLanguage.toUpperCase(),
      titles: titles,
      getOnlyImages: false,
    };
    const result = this.peopleService().getPeopleAndCompany(peopleAndCompany);
    if (!result) {
      return;
    }

    result.then(res => {
      if (!res.data || res.data.length <= 0) {
        return;
      }
      const datas = res.data as PeopleAndCompanyModel[];
      // Fetch image url of people here, so must to set again people ranking array
      this.peopleRelated.peopleRanking.map((item, i) => {
        const currentData = datas[i];
        item.imageUrl = currentData.imageUrl;
        item.cardPeople = currentData as unknown as CardModel;
        item.cardPeople.type = item.type;
        return item;
      });
      this.peopleRanking = this.peopleRelated.peopleRanking;
      this.peopleRelated.peopleCarouselCurrent.cardPeople = this.peopleRanking[this.hashIndex].cardPeople;
      this.cardPeople = this.peopleRanking[this.hashIndex].cardPeople;
      this.handleChangeFromHash(this.peopleRelated.peopleRanking[this.hashIndex], this.hashIndex);
    });
  }

  goToFeed() {
    this.$router.push('/feed');
  }

  public getParam() {
    const connectome = JSON.parse(localStorage.getItem('ds-connectome'));
    const connectomeId = connectome.connectomeId;
    return 'api/people/' + connectomeId;
  }

  handleChangeFromHash(item, index) {
    // @ts-ignore
    this.$refs.peopleRanking.changeItem(item, index);
    this.$store.commit('setNode', item.title);
    this.peopleOrCompany = item;
  }

  changeItem(item, index) {
    this.$store.commit('setNode', item.title);
    // @ts-ignore
    this.$refs.carouselPeople.getCardsInPeopleRanking(item);
    this.peopleRelated.peopleCarouselCurrent = item;
    this.peopleRelated.chosePeopleCarousel(item, index);
    this.cardPeople = this.peopleRelated.peopleCarouselCurrent.cardPeople;
    this.peopleOrCompany = item;
  }

  appendCard(item) {
    while (this.CARD_PORTION < TOTAL_CARD_PORTION) {
      if (!item.CARD_PORTION) {
        break;
      }
      this.CARD_PORTION = this.CARD_PORTION + item.CARD_PORTION;
      this.firstPage.push(item);
    }
  }

  handleActivity(item, activity, pageableCarouselPeople: CarouselPeopleModel) {
    pageableCarouselPeople.contents.forEach((val, ind) => {
      if (item.docId == val.docId) {
        if (activity.activity == ACTIVITY.like) {
          val.liked = activity.like;
        }
        if (activity.activity == ACTIVITY.bookmark) {
          val.bookmarked = activity.bookmark;
        }
        if (activity.activity == ACTIVITY.delete) {
          val.deleted = true;
          // @ts-ignore
          this.$refs.carouselPeople.hiddenCard(val);
        }
      }
    });
  }

  searchInFeed(label) {
    this.clearSelectedEntity();
    const connectomeLang = this.$store.getters['connectomeNetworkStore/lang'];
    this.setSelectedEntity({ title: label, srcLang: connectomeLang });
    this.$router.push({ path: '/feed' });
  }

  searchInMyAI(label) {
    this.clearSelectedEntity();
    const connectomeLang = this.$store.getters['connectomeNetworkStore/lang'];
    this.setSelectedEntity({ title: label, srcLang: connectomeLang });
    this.$router.push({ path: '/my-ai/learning-center' });
  }

  onSetAsFavoriteClick(label) {
    this.toggleEntityFavorite({
      title: label,
    }).then();
  }

  onHideClick(label) {
    this.isHidden = true;
    this.toggleEntityDisability({
      title: label,
    }).then();
  }

  goToConnectome(node) {
    this.setSelectedEntity({ title: node, srcLang: this.lang });
    this.$router.push('/my-ai/connectome');
  }

  learningKeyword() {
    this.$router.push('/my-ai/learning-center');
  }

  destroyed() {
    this.$root.$off('update-new-lang', this.loadPeople);
  }
}
