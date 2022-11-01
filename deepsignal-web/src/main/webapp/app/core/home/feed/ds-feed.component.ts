import { Component, Inject, Watch } from 'vue-property-decorator';
import DsCards from '@/shared/cards/ds-cards.vue';
import {
  ACTIVITY,
  CARD_SIZE,
  CARD_TYPE,
  FEED_FILTER,
  FEED_FILTER_CATEGORY,
  FEED_FILTER_PERIOD,
  SORT_BY,
  SORT_DIRECTIONS,
  SYMBOL_CRYPTO,
} from '@/shared/constants/feed-constants';
import FeedService from '@/core/home/feed/feed.service';
import { CARD_PEOPLE_TYPE, CardModel } from '@/shared/cards/card.model';
import axios from 'axios';
import ShowMore from '@/shared/cards/footer/show-more/show-more.vue';
import { mixins } from 'vue-class-component';
import { ShowMoreMixin } from '@/mixins/show-more';
import PeopleService from '@/core/home/people/people.service';
import PageTopComponent from '@/core/home/page-top.vue';
import DsToastNewFeed from '@/shared/custom/toast/ds-toast-new-feed.vue';
import DatePicker from 'vue2-datepicker';
import 'vue2-datepicker/index.css';
import _ from 'lodash';
import { namespace } from 'vuex-class';
import { SignalTodayIssueModel } from '@/core/home/signals/signal-today-issue/signal-today-issue.model';
import { UserSettingService } from '@/service/usersetting.service';
import { CHART_STOCK_STYLE } from '@/shared/constants/ds-constants';
import moment from 'moment';

const cardState = namespace('cardStore');
const networkStore = namespace('connectomeNetworkStore');
const userSettingStore = namespace('userSettingStore');

@Component({
  name: 'ds-feed', // use for keep-alive include
  components: {
    DsCards,
    'show-more': ShowMore,
    'page-top': PageTopComponent,
    'ds-toast-new-feed': DsToastNewFeed,
    'date-picker': DatePicker,
  },
  computed: {
    getSetting() {
      return this.$store.getters['userSettingStore/timeSetting'];
    },
  },
  watch: {
    getSetting(value) {
      this.onSettingChange();
    },
  },
})
export default class DsFeed extends mixins(ShowMoreMixin) {
  @Inject('feedService')
  private feedService: () => FeedService;
  @Inject('peopleService')
  private peopleService: () => PeopleService;
  @Inject('userSettingService')
  private userSettingService: () => UserSettingService;

  private totalPagesDefault = 0;
  private totalPagesSearch = 0;
  loaderDisable = false;
  public isDisabled = false;
  private localStore: [] | any;
  private label: '' | [];
  private parent: [] | any;
  private pageDefault = 0;
  private size = 20;
  private totalData = 0;
  private totalDataSearch = 0;
  private pageSearch = 0;
  private fromDate = '';
  private untilDate = '';
  private formatDate = 'MM/DD/YYYY';
  private isDatePicker = false;
  private hashTag = '';
  private expected = false;
  private dataMetaSearch: [] | any;
  private isWeather = true;
  private keyword = '';
  private isNoResult = false;
  isTrainingDone = false;
  private periods = FEED_FILTER_PERIOD;
  private categories = FEED_FILTER_CATEGORY;
  private datePicker = {
    from: new Date().toISOString().split('T')[0],
    to: new Date().toISOString().split('T')[0],
  };

  dataTransfer = {
    scrollId: '',
    query: '',
    language: 'en',
  };

  private isContinuted = -1;
  private dataAutoRefresh = true;
  // private showFilter = false;
  private sortBys = SORT_BY;
  private sortDirections = SORT_DIRECTIONS;
  private listFilter = [];
  private sortDirection = 'desc';
  private isFiltering = false;
  private isAll = true;
  private search_type = '';
  private lang = '';
  stockCodes = [];
  peoples = []; // array data of card top ten for people
  private searchFilter = false;
  companies = [];
  loadDoneCompany = false;
  cardsPreAppend = []; // this card is array includes all card always show when card init
  @cardState.Getter
  public signalIssues: SignalTodayIssueModel[];
  @cardState.Getter
  public getCompanies: any;
  @cardState.Getter
  public getCompaniesOrPeoples: any;
  clusterDocuments = [];
  searchQuery = '';
  private goToLearningCenter = false;

  @networkStore.Getter
  public labelHidden;

  @userSettingStore.Getter
  public userSetting;

  @userSettingStore.Action
  public setUserSetting!: (payload: { connectomeId: string }) => void;

  @userSettingStore.Action
  public saveUserSetting!: (payload: { setting: any; connectomeId: string }) => void;

  created(): void {
    this.localStore = this.$store.getters['mapNetworkStore/entityLabelSelected'];
    if (this.localStore) {
      this.searchFeed(this.localStore);
    }
    this.$root.$on('search-feed', this.searchFeed);
    this.$root.$on('choose-favorite', this.searchFeed);
    this.$root.$on('delete-card-issue', this.hiddenCardDefault);
    this.$root.$on('update-new-lang', this.updateNewLang);
    this.$root.$on('hide-weather-card', this.hideWeatherCard);
    this.$root.$on('hide-stock-card', this.hideStockCard);
  }
  data() {
    return {
      parent: this.parent,
      label: this.label,
    };
  }

  @Watch('$route')
  detectRouterChange() {
    this.localStore = this.$store.getters['mapNetworkStore/entityLabelSelected'];
    if (this.localStore) {
      this.searchFeed(this.localStore);
    }
  }

  @Watch('labelHidden')
  onLabelHiddenChange(value) {
    this.cardItems.forEach((item, index) => {
      if (item.dataTemplate === 'people' && item.label.toLowerCase() === value.toLowerCase()) {
        this.cardItems.splice(index, 1);
      }
    });
    if (this.companies.length > 0) {
      this.peoples.forEach((item, index) => {
        if (item.title.toLowerCase() === value.toLowerCase()) {
          this.peoples.splice(index, 1);
        }
      });
      this.companies.forEach((item, index) => {
        if (item.label.toLowerCase() === value.toLowerCase()) {
          this.companies.splice(index, 1);
        }
      });
    }
  }

  destroyed() {
    this.$root.$off('search-feed', this.searchFeed);
    this.$root.$off('choose-favorite', this.searchFeed);
    this.$root.$off('delete-card-issue', this.hiddenCardDefault);
    this.$root.$off('update-new-lang', this.updateNewLang);
    this.$root.$off('hide-weather-card', this.hideWeatherCard);
    this.$root.$off('hide-stock-card', this.hideStockCard);
  }

  updateNewLang() {
    this.isNoResult = false;
    this.goToLearningCenter = false;
    this.searchFeed();
  }

  searchFeed(favoriteKeyword?) {
    if (favoriteKeyword) {
      this.keyword = favoriteKeyword.toString();
      this.searchFilter = true;
    }
    this.keyword = String.prototype.trimAllSpace(this.keyword);
    if (this.keyword) {
      this.loaderDisable = false;
      this.pageSearch = 0;
      this.totalPagesSearch = 0;
      // isSearching = true -> cardItems change to cardItemSearch -> cardItemSearch = [] -> active scrollLoader()
      this.isSearching = true;
      this.cardItemSearch = [];
      this.label = '';
      this.isNoResult = false;
    } else {
      this.closeSearch();
    }
  }

  closeSearch() {
    this.loaderDisable = false;
    this.isSearching = false;
    if (this.keyword.localeCompare(this.$store.getters['mapNetworkStore/entityLabelSelected']) == 0) {
      this.$store.dispatch('mapNetworkStore/clearSelectedEntity');
    }
    // this.$root.$emit('clear-keyword');
    this.keyword = '';
    this.cardItems = [];
    this.pageDefault = 0;
    this.totalPagesDefault = 0;
    this.openSearchFilter();
  }

  openSearchFilter() {
    this.searchFilter = !this.searchFilter;
  }

  searchFeedByConnectomeId(isAppend?) {
    if (isAppend && this.pageSearch < this.totalPagesSearch) {
      this.pageSearch += 1;
    }
    if (this.isSearching) {
      this.goToLearningCenter = false;
    }
    this.feedService()
      .getListFeed(
        this.connectomeId,
        null,
        this.keyword,
        this.fromDate,
        this.untilDate,
        this.pageSearch,
        this.size,
        this.search_type,
        null,
        "Feed",
        null
      )
      .then(res => {
        this.totalDataSearch += res.data.body.data.length;
        this.isNoResult = res.data.body.totalItems <= 0;
        // this.loaderDisable = res.data.body.data.length < this.size;
        this.loaderDisable = res.data.body.totalItems == this.totalDataSearch;
        if (this.loaderDisable) {
          this.goToLearningCenter = true;
        }
        this.setTimeoutFeed();
        this.processResponseConnectomeFeed(res);
      })
      .catch(() => this.setTimeoutFeed());
    // this.feedService()
    //   .getAllConnectomeFeedByConnectomeId(
    //     this.connectomeId,
    //     this.pageSearch,
    //     this.size,
    //     '',
    //     this.sortDirection,
    //     this.hashTag,
    //     this.expected,
    //     this.keyword,
    //     // this.isFiltering ? this.listFilter : []
    //     this.listFilter
    //   )
    //   .then(res => {
    //     this.isNoResult = res.data.totalItems <= 0;
    //     this.loaderDisable = res.data.connectomeFeeds.length < this.size;
    //     this.setTimeoutFeed();
    //     this.processResponseConnectomeFeed(res);
    //   })
    //   .catch(err => {
    //     this.setTimeoutFeed();
    //   });
  }

  // @Watch('signalIssues')
  // detectSignalIssuesChange() {
  //   if (this.signalIssues) {
  //   }
  // }
  @Watch('getCompaniesOrPeoples')
  onChangeCompaniesOrPeoples() {
    if (this.companies.length == 0) {
      this.companies = CardModel.getCompanyCards(this.cardItems);
    }
  }

  @Watch('signalIssues')
  onSignalIssuesChange() {
    if (this.signalIssues) {
      this.clusterDocuments = this.signalIssues.map(item => item.clusterDocuments);
      this.cardsPreAppend
        .filter(item => item.dataTemplate === CARD_TYPE.CONTENTS_GROUP)
        .map(i => (i.clusterDocuments = this.clusterDocuments.shift()));
    }
  }

  scrollLoader(connectomeNode?: any) {
    //this.label = this.$store.getters['mapNetworkStore/entityLabelSelected'];
    // if (this.signalIssues) {
    //   this.clusterDocuments = this.signalIssues.map(item => item.clusterDocuments);
    // }
    this.$findPeopleByConnectomeId(this.connectomeId)
      .then(res => {
        const connectomeId = JSON.parse(localStorage.getItem('ds-connectome')).connectomeId;
        this.setUserSetting({ connectomeId: connectomeId });
      })
      .finally(() => {
        if (this.isSearching) {
          this.totalPagesSearch > 0 ? this.searchFeedByConnectomeId(true) : this.searchFeedByConnectomeId();
        } else {
          this.totalPagesDefault > 0 ? this.getAllFeed(this.hashTag, this.expected, true) : this.getAllFeed(this.hashTag, this.expected);
        }
      });
  }

  refreshFeed() {
    this.cardItems = [];
    this.pageDefault = 0;
    this.isSearching = false;
    this.keyword = '';
    this.getAllFeed(this.hashTag, this.expected);
    this.updateFeed(this.connectomeId);
  }

  feedUpdated(e) {
    this.feedService()
      .getFeedByRecommendDate(e.connectomeId, e.recommendDate)
      .then(res => {
        if (!res || !res.data) {
          return;
        }
        // mutable object before process, this object depend on processCard() method
        const data = {
          data: {
            connectomeFeeds: res.data,
          },
        };
        const cardsPreAppend = _.cloneDeep(this.cardsPreAppend);
        const arr = this.processCard(data, [], 'feed');
        this.cardItems = CardModel.autoAppendCardNew(this.cardItems, arr, cardsPreAppend);
        if (this.needToLoadNextPage) {
          this.needToLoadNextPage = false;
          this.scrollLoader();
        }
      });
  }

  updateFeed(connectomeId) {
    if (!connectomeId) {
      return;
    }
    this.feedService()
      .updateFeed(connectomeId)
      .then(res => {});
  }

  setTimeoutFeed() {
    setTimeout(() => {
      this.isDisabled = false;
    }, 60000);
  }

  getAllFeed(item, excepted, isAppend?) {
    if (isAppend && this.pageDefault < this.totalPagesDefault) {
      this.pageDefault += 1;
    }

    // this.cardItems = this.cardFakes;
    if (!this.cardItems) {
      this.cardItems = [];
    }
    this.getConnectomeFeedByLogin(item, excepted);
  }

  getConnectomeFeedByLogin(item, expected) {
    // this.getAllConnectomeFeedByConnectomeId(item, expected, this.connectomeId);
    this.getAllFeeds(this.connectomeId);
  }

  getAllConnectomeFeed() {
    this.feedService()
      .getAllConnectomeFeed(this.pageDefault, this.size)
      .then(res => {
        this.processResponseConnectomeFeed(res);
      })
      .catch(err => {
        console.error('Error when get all feed, err = ', err);
        // this.cardItems = this.cardFakes;
      });
  }

  getAllFeeds(connectomeId) {
    if (!connectomeId) {
      return;
    }
    this.isDisabled = true;
    this.isTrainingDone = false;
    this.feedService()
      .getListFeed(
        connectomeId,
        null,
        this.keyword,
        this.fromDate,
        this.untilDate,
        this.pageDefault,
        this.size,
        this.search_type,
        null,
        "Feed",
        null
      )
      .then(res => {
        this.totalData += res.data.body.data.length;
        this.isNoResult = res.data.body.totalItems <= 0;
        // this.loaderDisable = res.data.body.data.length < this.size;

        this.loaderDisable = res.data.body.totalItems == this.totalData;
        if (this.loaderDisable) {
          this.goToLearningCenter = true;
        }
        this.setTimeoutFeed();
        this.processResponseConnectomeFeed(res);
      })
      .catch(() => this.setTimeoutFeed());
  }

  // getAllConnectomeFeedByConnectomeId(item, expected, connectomeId) {
  //   if (!connectomeId) {
  //     return;
  //   }
  //   this.isDisabled = true;
  //   this.isTrainingDone = false;
  //   this.feedService()
  //     .getAllConnectomeFeedByConnectomeId(
  //       connectomeId,
  //       this.pageDefault,
  //       this.size,
  //       '',
  //       this.sortDirection,
  //       item,
  //       expected,
  //       null,
  //       // this.isFiltering ? this.listFilter : []
  //       this.listFilter
  //     )
  //     .then(res => {
  //       this.isNoResult = res.data.totalItems <= 0;
  //       this.loaderDisable = res.data.connectomeFeeds.length < this.size;
  //       if (this.loaderDisable) {
  //         this.goToLearningCenter = true;
  //       }
  //       this.setTimeoutFeed();
  //       // this.processResponseConnectomeFeed(res);
  //     })
  //     .catch(() => this.setTimeoutFeed());
  // }

  processResponseConnectomeFeed(res) {
    if (!res) {
      return;
    }
    this.isSearching ? (this.totalPagesSearch = res.data.body.totalPages) : (this.totalPagesDefault = res.data.body.totalPages);
    this.processCard(res, this.isSearching ? this.cardItemSearch : this.cardItems, 'feed');
    if (this.needToLoadNextPage) {
      this.needToLoadNextPage = false;
      this.scrollLoader();
    }
  }

  $findPeopleByConnectomeId(connectomeId) {
    this.stockCodes = [];
    return new Promise<any>((resolve, reject) => {
      this.peopleService()
        .findByConnectomeId(connectomeId)
        .then(res => {
          if (!res || !res.data) {
            return;
          }
          let firstStock = {} as any;
          const result = res.data.results;
          const companies = result.company.map(item => ({ ...item, type: 'COMPANY' }));
          const peoples = result.people.map(item => ({ ...item, type: 'PEOPLE' }));
          this.peoples = [...peoples, ...companies].sort((a, b) => (a.dfCnt > b.dfCnt ? -1 : 1));
          for (let i = 0; i < result.stock.length; i++) {
            const stock = result.stock[i];
            if (i === 0) {
              firstStock = stock;
            }
            this.stockCodes.push(stock);
          }
          resolve(this.stockCodes);
        })
        .catch(err => reject(err));
    });
  }

  //sourceFeedIds:Set<string> = new Set();
  needToLoadNextPage = false;

  processCard(res, currentCard, component) {
    this.cardsPreAppend = [];
    this.needToLoadNextPage = false;
    if (component === 'feed') {
      // let countCardAdded = 0;
      for (const cardFake of res.data.body.data) {
        // const isPresent = cardFake.sourceId
        //   ? currentCard.find(elem => elem.sourceId && elem.sourceId.localeCompare(cardFake.sourceId) == 0)
        //     ? true
        //     : false
        //   : false;
        // if (!isPresent) {
        const cardItem = new CardModel(cardFake as any, component);
        currentCard.push(cardItem);
        // countCardAdded++;
        // }
      }
      // if (countCardAdded <= Math.floor(this.size * 0.75) && res.data.body.currentPage < res.data.body.totalPages){
      //   this.needToLoadNextPage = true;
      // }
    } else {
      res.data.body.data.forEach((item, index) => {
        const image = [];
        image.push(item.og_image_base64 ? item.og_image_base64 : item.og_image_url);
        Object.assign(
          item,
          ...[
            { description: item.description },
            { docId_content: item.docId_content },
            { imageLinks: item.og_image_base64 ? item.og_image_base64 : item.og_image_url },
            { writer: item.writer },
            { url: item.url },
            { created_date: item.created_date },
          ]
        );
        const card = new CardModel(item as any, component);
        currentCard.push(card);
      });
    }
    if (component === 'feed' ? res.data.body.currentPage == 0 : res.currentPage == 0) {
      // if ((this.isSearching || this.listFilter.length > 0) && this.isNoResult) {
      if (this.isSearching || this.keyword || this.isNoResult || (this.fromDate && this.untilDate) || this.search_type) {
        return;
      }
      const stocks = this.stockCodes;

      const stockCodesCrypto = [];
      SYMBOL_CRYPTO.forEach(symbol => {
        const stockCodeCrypto = {
          market: 'Crypto',
          symbol: symbol,
        };
        stockCodesCrypto.push(stockCodeCrypto);
      });
      const cardCrypto = {
        isCrypto: true,
        stockCodes: stockCodesCrypto,
        dataSize: CARD_SIZE._2_1,
        dataTemplate: CARD_TYPE.CHART,
        notUseful: false,
        notUseful2: false,
      };
      const cardTopTen = {
        ...CardModel.buildTopTenCard(CARD_PEOPLE_TYPE.IS_CONTENT),
        peoples: this.peoples,
      };
      const cardWeather = {
        dataSize: CARD_SIZE._1_1,
        dataTemplate: CARD_TYPE.WEATHER,
      };

      const cardSignal = {
        dataSize: CARD_SIZE._1_2,
        dataTemplate: CARD_TYPE.SIGNAL,
      };
      this.cardsPreAppend = [];
      if (stocks.length > 0) {
        //edit card_size stock here
        const cardStock = {
          dataSize: this.userSetting.stock.cardStyle === CHART_STOCK_STYLE.CHART ? CARD_SIZE._2_1 : CARD_SIZE._1_1,
          dataTemplate: CARD_TYPE.CHART,
          stockCodes: stocks,
        };
        if (this.userSetting.stock.showStockCard) {
          this.cardsPreAppend.push(cardStock);
        }
      }
      if (this.userSetting.weather.showWeatherCard) {
        this.cardsPreAppend.push(cardWeather);
      }

      const cardPeopleStock = {
        dataSize: CARD_SIZE._1_2,
        dataTemplate: CARD_TYPE.STOCK,
        stockCodes: stocks,
      };
      if (cardTopTen.peoples && cardTopTen.peoples.length > 0) {
        // this.cardsPreAppend.push(cardPeopleStock); -- Remove because of duplicate
        this.cardsPreAppend.push(cardTopTen); // relevant data between cardPeopleStock and cardTopTen
      }
      this.cardsPreAppend.push(cardSignal);

      const cardTodayTopic = {
        dataSize: CARD_SIZE._2_2,
        dataTemplate: CARD_TYPE.CONTENTS_GROUP,
        clusterDocuments: [],
      };
      this.cardsPreAppend.push(cardTodayTopic);

      currentCard.unshift(...this.cardsPreAppend);
    }
    if (this.companies.length > 0) {
      const cardCompanyFirst = this.companies.shift();
      const peopleOrCompany = this.peoples.find(item => item.title === cardCompanyFirst.label);
      if (peopleOrCompany) {
        cardCompanyFirst.type = peopleOrCompany.type;
      }
      if (cardCompanyFirst) {
        currentCard.push(cardCompanyFirst);
      }
    }
    if (this.clusterDocuments.length > 0) {
      const cardTodayTopic = {
        dataSize: CARD_SIZE._2_2,
        dataTemplate: CARD_TYPE.CONTENTS_GROUP,
        clusterDocuments: this.clusterDocuments.shift(),
      };
      currentCard.push(cardTodayTopic);
    }
    return currentCard;
  }

  getHashTag() {
    this.isWeather = true;
    this.isContinuted++;
    this.dataTransfer.language = localStorage.getItem('currentLanguage');
    // @ts-ignore
    this.dataTransfer.query = this.label.replace(/[-_.()]/gi, ',');
    axios
      .post(
        'api/learning/search-cache/' + this.connectomeId + '/' + this.dataTransfer.language + '/' + this.isContinuted,
        this.dataTransfer
      )
      .then(res => {
        // this.setTimeoutFeed();
        this.dataMetaSearch = res.data.metaSearch.body.data;
        if (this.dataMetaSearch.length < 10) {
          this.loaderDisable = true;
        }
        if (this.isWeather) {
          this.processCard(res.data.metaSearch.body, this.cardItems, 'metaSearch');
        }
      });
    // .catch(() => this.setTimeoutFeed());
  }

  closeHashTag() {
    this.isSearching = false;
    this.isWeather = false;
    this.$store.dispatch('mapNetworkStore/clearSelectedEntity');
    //localStorage.removeItem('ds-selectedConnectomeNode');
    localStorage.removeItem('ds-favorite');
    this.label = '';
    this.cardItems = [];
    this.loaderDisable = false;
    this.pageDefault = 0;
    this.totalPagesDefault = 0;
  }

  toggleDataAutoRefresh() {
    this.dataAutoRefresh = !this.dataAutoRefresh;
  }

  sortFeedBy(item) {
    if (this.listFilter.length > 0 && JSON.stringify(this.listFilter[0]) === JSON.stringify(item.interaction)) {
      return;
    }
    // Current filter one condition
    this.listFilter = [];
    this.isAll = false;
    this.isFiltering = true;
    this.sort(this.sortBys, item);
  }

  sortFeedDirection(direction) {
    if (this.sortDirection === direction.value) {
      return;
    }
    this.sort(this.sortDirections, direction);
  }

  sort(sortArr, itemOfArr) {
    sortArr.forEach(item => (item.selected = false));
    itemOfArr.selected = true;
    itemOfArr.interaction ? this.listFilter.push(itemOfArr.interaction) : (this.sortDirection = itemOfArr.value);
    this.enableScrollLoader();
  }

  enableScrollLoader() {
    this.loaderDisable = false;
    if (this.isSearching) {
      this.pageSearch = 0;
      this.totalPagesSearch = 0;
      this.cardItemSearch = [];
    } else {
      this.pageDefault = 0;
      this.totalPagesDefault = 0;
      this.cardItems = [];
    }
  }

  sortOptionAll() {
    if (this.isAll) {
      return;
    }
    this.isAll = true;
    // this.closeSearch();
    this.isFiltering = false;
    this.listFilter = [];
    this.sortBys.forEach(item => (item.selected = false));
    this.enableScrollLoader();
  }

  handleActivity(item, activity) {
    if (activity.activity == ACTIVITY.delete) {
      // this.cardItems.forEach((value, index) => {
      //   if (item.docId === value.docId) {
      //     value.isDeleted = true;
      //   }
      // });
      this.cardItems = this.cardItems.filter(card => card.docId !== item.docId)
    }
  }

  public filterFeed(type, condition) {
    if (!condition.selected) {
      this.totalData = 0;
      this.totalDataSearch = 0;
      this.goToLearningCenter = false;
    } else {
      return;
    }
    this.isNoResult = false;
    if (type === 'period') {
      if (document.querySelector("[data-target='#period-directInput']").classList.contains('active')) {
        document.querySelector("[data-target='#period-directInput']").classList.remove('active');
      }
      if (condition.text === 'all') {
        this.fromDate = null;
        this.untilDate = null;
      } else {
        this.fromDate = moment(condition.filter.value?.from).format(this.formatDate);
        this.untilDate = moment(condition.filter.value?.to).format(this.formatDate);
      }

      this.periods.map(item => (item.selected = false));
      // this.listFilter = this.listFilter.filter(item => item.field !== condition.filter.field); // remove old period
      // if (condition.text !== 'all') {
      //   this.listFilter.push(condition.filter); // push new period selected
      // }
    } else {
      this.categories.map(item => (item.selected = false));
      // this.listFilter = this.listFilter.filter(item => item.field !== condition.filter.field);
      if (condition.text !== 'all') {
        this.search_type = condition.filter.value;
        // this.listFilter.push(condition.filter);
      } else {
        this.search_type = null;
      }
    }
    condition.selected = true;
    this.enableScrollLoader();
  }

  public choosePeriod() {
    this.isDatePicker = false;
    this.totalData = 0;
    this.totalDataSearch = 0;
    this.goToLearningCenter = false;
    this.isNoResult = false;
    this.periods.map(item => (item.selected = false));
    document.querySelector("[data-target='#period-directInput']").classList.add('active');
    // const timeISO = new Date().toISOString().substring(new Date().toISOString().indexOf('T'));
    const filter = {
      field: FEED_FILTER.recommendDate,
      value: { from: this.datePicker.from, to: this.datePicker.to },
    };
    if (filter.value.from > filter.value.to) {
      this.isDatePicker = true;
      return;
    }
    this.fromDate = moment(filter.value.from).format(this.formatDate);
    this.untilDate = moment(filter.value.to).format(this.formatDate);
    // this.listFilter = this.listFilter.filter(item => item.field !== FEED_FILTER.recommendDate);
    // this.listFilter.push(filter);
    this.enableScrollLoader();
  }

  hiddenCardDefault(card, template?) {
    if (card) {
      this.cardItems.forEach((value, index) => {
        if (value.dataTemplate === card) {
          if (template) {
            value.dataSize = CARD_SIZE._1_1;
          } else {
            this.cardItems.splice(index, 1);
          }
        }
      });
    }
  }

  learningKeyword() {
    if (((!this.keyword || this.keyword == '') && !this.cardItems.length) || this.goToLearningCenter) {
      this.$router.push('/my-ai/learning-center');
    }
    if (this.isNoResult && this.keyword && this.keyword != '') {
      this.$router.push('/my-ai/learning-center');
      setTimeout(() => {
        this.$root.$emit('learning-keyword', this.keyword);
      }, 1000);
    }
  }

  onSettingChange() {
    if (this.cardItems.length > 0 && this.userSetting) {
      if (this.userSetting.stock.showStockCard != this.checkCardStock()) {
        this.showStockCard(this.userSetting.stock.showStockCard);
      }
      if (this.userSetting.weather.showWeatherCard != this.checkCardWeather()) {
        this.showWeatherCard(this.userSetting.weather.showWeatherCard);
      }
      if (this.userSetting.stock.showStockCard) {
        this.handleChangeTypeStock(this.userSetting.stock.cardStyle);
      }
    }
  }

  checkCardWeather() {
    let weather = [];
    weather = this.cardItems.filter((item, index) => {
      return index < 6 && item.dataTemplate === CARD_TYPE.WEATHER;
    });
    if (weather.length > 0) {
      return true;
    } else {
      return false;
    }
  }

  checkCardStock() {
    let stock = [];
    stock = this.cardItems.filter((item, index) => {
      return index < 6 && item.dataTemplate === CARD_TYPE.CHART;
    });
    if (stock.length > 0) {
      return true;
    } else {
      return false;
    }
  }

  handleChangeTypeStock(cardStyle) {
    if (this.stockCodes.length < 1) {
      return;
    }
    const type = this.checkTypeStock(cardStyle);
    this.cardItems.forEach((item, index) => {
      if (item.dataTemplate === CARD_TYPE.CHART) {
        if (item.cardSize === type) {
          return;
        }
        this.cardItems.splice(index, 1);
        const cardStock = {
          dataSize: type,
          dataTemplate: CARD_TYPE.CHART,
          stockCodes: this.stockCodes,
        };
        this.cardItems.unshift(cardStock);
      }
    });
  }

  checkTypeStock(cardStyle) {
    if (cardStyle === CHART_STOCK_STYLE.CHART) {
      return CARD_SIZE._2_1;
    } else {
      return CARD_SIZE._1_1;
    }
  }

  showStockCard(isShow) {
    if (isShow && this.userSetting) {
      if (!this.checkCardStock()) {
        if (this.stockCodes.length < 1) {
          return;
        }
        const type = this.checkTypeStock(this.userSetting.stock.cardStyle);
        const cardStock = {
          dataSize: type,
          dataTemplate: CARD_TYPE.CHART,
          stockCodes: this.stockCodes,
        };
        this.cardItems.unshift(cardStock);
      }
    } else if (!isShow && this.userSetting) {
      if (this.checkCardStock()) {
        this.cardItems.forEach((item, index) => {
          if (item.dataTemplate === CARD_TYPE.CHART) {
            this.cardItems.splice(index, 1);
          }
        });
      }
    }
  }

  showWeatherCard(isShow) {
    if (isShow && this.userSetting) {
      const cardWeather = {
        dataSize: CARD_SIZE._1_1,
        dataTemplate: CARD_TYPE.WEATHER,
      };
      if (this.checkCardStock() && !this.checkCardWeather()) {
        this.cardItems.splice(1, 0, cardWeather);
      } else if (!this.checkCardStock() && !this.checkCardWeather()) {
        this.cardItems.unshift(cardWeather);
      }
    } else if (!isShow && this.userSetting) {
      if (!this.checkCardWeather()) {
        return;
      }
      this.cardItems.forEach((item, index) => {
        if (item.dataTemplate === CARD_TYPE.WEATHER) {
          this.cardItems.splice(index, 1);
        }
      });
    }
  }

  hideWeatherCard() {
    const dataTransfer = { ...this.userSetting };
    dataTransfer.weather.showWeatherCard = false;
    const connectomeId = JSON.parse(localStorage.getItem('ds-connectome')).connectomeId;
    this.saveUserSetting({ setting: dataTransfer, connectomeId: connectomeId });
  }

  hideStockCard() {
    const dataTransfer = { ...this.userSetting };
    dataTransfer.stock.showStockCard = false;
    const connectomeId = JSON.parse(localStorage.getItem('ds-connectome')).connectomeId;
    this.saveUserSetting({ setting: dataTransfer, connectomeId: connectomeId });
  }
}
