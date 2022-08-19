import { Component, Inject, Prop, Vue, Watch } from 'vue-property-decorator';
import DsCardDefault from '@/shared/cards/default/ds-card-default.vue';
import DsCardStock from '@/shared/cards/stock/ds-card-stock.vue';
import DsCardWeather from '@/shared/cards/weather/ds-card-weather.vue';
import DsCardVideo from '@/shared/cards/video/ds-card-video.vue';
import DsCardSurvey from '@/shared/cards/survey/ds-card-survey.vue';
import DsCardMap from '@/shared/cards/map/ds-card-map.vue';
import DsCardGroup from '@/shared/cards/group/ds-card-group.vue';
import DsCardRisingPeople from '@/shared/cards/rising-people/ds-card-rising-people.vue';
import DsCardRisingCompany from '@/shared/cards/rising-company/ds-card-rising-company.vue';
import DsCardTrendPeople from '@/shared/cards/trend-people/ds-card-trend-people.vue';
import DsCardRanking from '@/shared/cards/ranking/ds-card-ranking.vue';
import DsCardPeople from '@/shared/cards/people/ds-card-people.vue';
import DsCardSocialNetworkAnalysis from '@/shared/cards/social-network-analysis/ds-card-social-network-analysis.vue';
import DsChartComponent from '@/shared/cards/chart/ds-chart.vue';
import DsCardTopTenParent from '@/shared/cards/topten/ds-card-topten-parent/ds-card-topten-parent.vue';
import DsCardSocial from '@/shared/cards/social/ds-card-social.vue';
import DsCardCompany from '@/shared/cards/company/ds-card-company.vue';
import DsCardPeopleRanking from '@/shared/cards/people/people-ranking/ds-card-people-ranking.vue';
import DsCardSignal from '@/shared/cards/signal/ds-card-signal.vue';
import ScrollLoader from 'vue-scroll-loader/dist/scroll-loader.umd.min';
import { InteractionUserService } from '@/service/interaction-user.service';
import DsCardSignalIssue from '@/shared/cards/signal-issue/signal-issue.vue';
import { CARD_TYPE, CARD_SIZE } from '@/shared/constants/feed-constants';
import DsCardPeopleSocial from '@/shared/cards/people/people-social/ds-card-people-social.vue';
import StockList from '@/shared/cards/stock-list/stock-list.vue';
import { UserSettingService } from '@/service/usersetting.service';
import { ResponseCode } from '@/shared/constants/ds-constants';
import { UserSetting } from '@/shared/model/usersetting.model';

Vue.use(ScrollLoader);

@Component({
  components: {
    'ds-card-default': DsCardDefault,
    'ds-card-stock': DsCardStock,
    'ds-card-weather': DsCardWeather,
    'ds-card-topten-parent': DsCardTopTenParent,
    'ds-card-video': DsCardVideo,
    'ds-card-survey': DsCardSurvey,
    'ds-card-map': DsCardMap,
    'ds-card-group': DsCardGroup,
    'ds-card-rising-people': DsCardRisingPeople,
    'ds-card-rising-company': DsCardRisingCompany,
    'ds-card-trend-people': DsCardTrendPeople,
    'ds-card-ranking': DsCardRanking,
    'ds-card-people': DsCardPeople,
    'ds-card-social-network-analysis': DsCardSocialNetworkAnalysis,
    'ds-chart': DsChartComponent,
    'ds-card-social': DsCardSocial,
    'ds-card-company': DsCardCompany,
    'ds-card-people-ranking': DsCardPeopleRanking,
    'ds-card-signal': DsCardSignal,
    'ds-card-signal-issue': DsCardSignalIssue,
    'ds-card-people-social': DsCardPeopleSocial,
    'ds-stock-list': StockList,
  },
})
export default class DsCards extends Vue {
  @Prop(Array) readonly cardItems: any | undefined;
  @Prop(String) readonly tab: any | undefined;
  @Prop(Boolean) readonly loaderDisable: any | undefined;
  @Prop(Boolean) readonly showHidden: any | false;
  @Prop(String) readonly keyword: any | '';
  private isSearchStock = false;
  @Inject('interactionUserService') private interactionUserService: () => InteractionUserService;
  @Inject('userSettingService') private userSettingService: () => UserSettingService;
  CARD_TYPE = CARD_TYPE;
  CARD_SIZE = CARD_SIZE;

  isShowWeatherCard = true;

  scrollLoader() {
    this.$emit('scrollLoader');
    this.$parent.$on('searchStock', this.searchStock);
  }

  searchStock() {
    this.isSearchStock = !this.isSearchStock;
  }

  showMore(coordinate) {
    this.$emit('showMore', coordinate);
  }

  handleActivity(item, activity) {
    this.$emit('handleActivity', item, activity);
  }

  // currentSetting: UserSetting = null;

  // created() {
  //   if (localStorage.getItem('ds-connectome')) {
  //     const connectome = JSON.parse(localStorage.getItem('ds-connectome'));
  //     this.userSettingService()
  //       .getSettingByUserId(connectome?.user?.id)
  //       .then(res => {
  //         if (res && res.data) {
  //           this.currentSetting = res.data;
  //         }
  //       });
  //   }
  //   this.$root.$on('update-new-setting', this.setNewSetting);
  // }

  // hideWeatherCard() {
  //   this.isShowWeatherCard = false;
  // }
  // showWeatherCard() {
  //   this.isShowWeatherCard = true;
  // }
  //
  // setNewSetting(item) {
  //   this.currentSetting = item;
  // }
  //
  // destroyed() {
  //   this.$root.$off('update-new-setting', this.setNewSetting);
  // }
}
