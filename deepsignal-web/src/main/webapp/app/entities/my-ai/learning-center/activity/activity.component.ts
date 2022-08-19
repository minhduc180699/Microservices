import { Component, Inject, Prop, Vue, Watch } from 'vue-property-decorator';
import DsCards from '@/shared/cards/ds-cards.vue';
import FeedService from '@/core/home/feed/feed.service';
import { CardModel } from '@/shared/cards/card.model';
import ShowMore from '@/shared/cards/footer/show-more/show-more.vue';
import { mixins } from 'vue-class-component';
import { ShowMoreMixin } from '@/mixins/show-more';
import axios from 'axios';
import { DOCUMENT_TYPE } from '@/shared/constants/ds-constants';
import { ACTIVITY, DOCUMENT } from '@/shared/constants/feed-constants';

@Component({
  components: {
    'ds-cards': DsCards,
    'show-more': ShowMore,
  },
})
export default class Activity extends mixins(ShowMoreMixin) {
  private activities = [
    // {name: '기록', active: true, icon: 'bi bi-clock-history'},
    { name: 'liked', active: true, icon: 'bi bi-heart', interaction: { field: 'liked', value: 1 } },
    { name: 'disliked', active: false, icon: 'bi bi-hand-thumbs-down', interaction: { field: 'liked', value: 2 } },
    { name: 'bookmarked', active: false, icon: 'bi bi-bookmark', interaction: { field: 'bookmarked', value: true } },
    { name: 'memo', active: false, icon: 'bi bi-list-ul', interaction: { field: 'memo', value: 1 } },
    { name: 'hidden', active: false, icon: 'bi bi-trash', interaction: { field: 'deleted', value: true } },
  ];

  private page = 0;
  private size = 10;
  private sortDirection = 'desc';
  private keyword = '';
  private loaderDisable = false;
  private listFilter = [this.activities[0].interaction];

  private totalItems = 0;
  private totalFeeds = 0;
  private totalDeleteDocuments = 0;

  private currentActivity = { name: this.activities[0].name, icon: this.activities[0].icon };
  private isDisable = false;
  private showHidden = true;

  @Inject('feedService')
  private feedService: () => FeedService;

  created(): void {
    this.$root.$on('search-feed', this.searchFeed);
  }

  mounted(): void {}

  // @Watch('totalFeeds')
  // @Watch('totalDeleteDocuments')
  // onTotalItemsChange(newVal, oldVal) {
  //   this.totalItems = this.totalDeleteDocuments + this.totalFeeds;
  // }

  destroyed(): void {
    this.$root.$off('search-feed', this.searchFeed);
  }

  changeLink(activity) {
    if (this.isDisable) {
      return;
    } else {
      // if (activity.name == 'hidden') {
      //   this.showHidden = true;
      // } else {
      //   this.showHidden = true;
      // }
      this.doFilter(activity);
    }
  }

  public doFilter(activity) {
    this.isDisable = true;
    if (activity.name === this.currentActivity.name) {
      return;
    }
    this.activities.forEach(activity => {
      if (activity.active) {
        activity.active = false;
      }
    });
    activity.active = true;
    Object.assign(this.currentActivity, { name: activity.name, icon: activity.icon });
    this.totalItems = 0;
    if (this.keyword) {
      this.keyword = '';
      this.$root.$emit('clear-keyword');
    }
    // filter one condition
    this.listFilter = [];
    this.listFilter.push(activity.interaction);
    this.enableScrollLoader();
  }

  public enableScrollLoader() {
    this.cardItems = [];
    this.loaderDisable = false;
    this.page = 0;
    this.getActivity();
  }

  public scrollLoader() {
    if (!this.isDisable) {
      // this.getFeedByConnectomeId();
      this.getActivity();
    }
  }

  public getFeedByConnectomeId() {
    this.feedService()
      .getAllConnectomeFeedByConnectomeId(
        this.connectomeId,
        this.page,
        this.size,
        '',
        this.sortDirection,
        '',
        false,
        this.keyword,
        this.listFilter
      )
      .then(res => {
        res.data.connectomeFeeds.length < this.size ? (this.loaderDisable = true) : (this.page += 1);
        if (this.totalItems != res.data.totalItems) {
          this.totalItems = res.data.totalItems;
        }
        res.data.connectomeFeeds.forEach(feed => {
          this.cardItems.push(new CardModel(feed as any, 'feed'));
        });
        this.isDisable = false;
      });
  }

  public getActivity() {
    this.isDisable = true;
    this.feedService()
      .getActivity(this.connectomeId, this.page, this.size, '', this.sortDirection, this.listFilter)
      .then(res => {
        if (this.totalItems != res.data.totalItems) {
          this.totalItems = res.data.totalItems;
        }
        this.totalItems === this.cardItems.length + res.data.activityList.length ? (this.loaderDisable = true) : (this.page += 1);
        res.data.activityList.forEach(item => {
          if (item.collectionType === DOCUMENT.personal) {
            const card = {
              writerName: item.author,
              imageLinks: [this.imgSource(item)],
              docId: item.uniqueKey + '',
              sourceId: item.url,
              recommendDate: item.publishedAt,
              documentType: DOCUMENT_TYPE.FEED,
            };
            Object.assign(item, card);
          }
          this.cardItems.push(new CardModel(item as any, 'feed'));
        });
        this.isDisable = false;
      })
      .catch(erorr => {
        this.isDisable = false;
      });
    console.log('items in activity: ', this.cardItems);
  }

  public searchFeed(keyword) {
    if (keyword) {
      this.totalItems = 0;
      this.keyword = keyword;
    }
    this.enableScrollLoader();
  }

  public getDeletedPersonalDocuments() {
    const connectome = JSON.parse(localStorage.getItem('ds-connectome'));
    if (connectome) {
      axios
        .get('api/personal-documents/getListDeletedDocuments/'.concat(connectome.connectomeId), {
          params: {
            size: this.size,
            page: this.page,
            orderBy: 'publishedAt',
            sortDirection: this.sortDirection,
          },
        })
        .then(res => {
          res.data.results.length < this.size ? this.loaderDisable : (this.page += 1);
          if (this.totalItems != res.data.totalItems) {
            this.totalDeleteDocuments = res.data.totalItems;
          }
          res.data.results.forEach(item => {
            const card = {
              writerName: item.author,
              imageLinks: [this.imgSource(item)],
              // docId: item.uniqueKey + '',
              sourceId: item.url,
              recommendDate: item.publishedAt,
              documentType: DOCUMENT_TYPE.PERSONAL_DOCUMENT,
            };
            Object.assign(item, card);
            this.cardItems.push(new CardModel(item as any, 'feed'));
          });
        });
    }
  }

  imgSource(item: any) {
    return item.ogImageUrl || item.ogImageBase64 || (item.imageUrl && item.imageUrl[0]) || item.imageBase64 || '';
  }

  handleActivity(item, activity) {
    if (activity.activity == ACTIVITY.delete) {
      this.cardItems.forEach((value, index) => {
        if (item.id === value.id) {
          value.deleted = true;
        }
      });
    }
  }
}
