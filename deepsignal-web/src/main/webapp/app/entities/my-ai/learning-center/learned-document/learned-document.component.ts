import { Component, Inject, Prop, Vue, Watch } from 'vue-property-decorator';
import DsCards from '@/shared/cards/ds-cards.vue';
import FeedService from '@/core/home/feed/feed.service';
import { CardModel } from '@/shared/cards/card.model';
import ShowMore from '@/shared/cards/footer/show-more/show-more.vue';
import { mixins } from 'vue-class-component';
import { ShowMoreMixin } from '@/mixins/show-more';
import axios from 'axios';
import { ACTIVITY, CARD_SIZE } from '@/shared/constants/feed-constants';
import { namespace } from 'vuex-class';
import { DOCUMENT_TYPE } from '@/shared/constants/ds-constants';

const mapNetworkStore = namespace('mapNetworkStore');
@Component({
  components: {
    'ds-cards': DsCards,
    'show-more': ShowMore,
  },
})
export default class LearnedDocument extends mixins(ShowMoreMixin) {
  private page = 0;
  private size = 20;
  private totalFeeds = 0;
  private sortDirection = 'desc';
  private loaderDisable = false;

  private listFilter = [{ field: 'liked', value: 1 }];

  private totalItems = 0;

  @Inject('feedService')
  private feedService: () => FeedService;

  @mapNetworkStore.Getter
  public entityLabelSelected!: string;

  mounted(): void {}

  public scrollLoader() {
    if (this.entityLabelSelected) {
      this.getApiFilterByEntityLabel(this.entityLabelSelected);
    } else {
      this.getApi();
    }
  }

  @Watch('entityLabelSelected')
  onEntitySelectedChanged(label: string) {
    console.log('enetity changed', label);
    if (label) {
      this.getApiFilterByEntityLabel(label);
    } else {
      this.getApi();
    }
  }

  closeHashTag() {
    this.$store.dispatch('mapNetworkStore/clearSelectedEntity');
  }

  getApiFilterByEntityLabel(entityLabel: string) {
    const connectomeId = JSON.parse(localStorage.getItem('ds-connectome')).connectomeId;
    console.log('1: documents from ' + entityLabel);

    const params = {
      page: this.page,
      size: this.size,
      uploadType: '',
      isDelete: 0,
    };

    if (entityLabel) {
      Object.assign(params, { entityLabel: entityLabel });
    }
    axios
      .post(`/api/personal-documents/getListDocuments/${connectomeId}`, [], {
        params,
      })
      .then(res => {
        // console.log('2: documents from ' + entityLabel, res);
        res.data.results.length < this.size ? (this.loaderDisable = true) : (this.page += 1);
        if (this.totalItems != res.data.totalItems) {
          this.totalFeeds = res.data.totalItems;
        }
        //try to convert data
        res.data.results.forEach(item => {
          const card = {
            writerName: item.author,
            imageLinks: [this.imgSource(item)],
            docId: item.uniqueKey + '',
            sourceId: item.url,
            recommendDate: item.publishedAt,
          };
          Object.assign(item, card);
          this.cardItems.push(new CardModel(item as any, 'feed'));
        });
      })
      .catch(err => reject(err));
  }

  getApi() {
    const connectomeId = JSON.parse(localStorage.getItem('ds-connectome')).connectomeId;
    axios
      .get(`/api/personal-documents/getListDocuments/${connectomeId}`, {
        params: {
          page: this.page,
          size: this.size,
          uploadType: '',
          isDelete: 0,
        },
      })
      .then(res => {
        res.data.results.length < this.size ? (this.loaderDisable = true) : (this.page += 1);
        if (this.totalItems != res.data.totalItems) {
          this.totalFeeds = res.data.totalItems;
        }
        //try to convert data
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

  getFeed() {
    this.feedService()
      .getAllConnectomeFeedByConnectomeId(this.connectomeId, this.page, this.size, '', this.sortDirection, '', false, '', this.listFilter)
      .then(res => {
        res.data.connectomeFeeds.length < this.size ? this.loaderDisable : (this.page += 1);
        if (this.totalItems != res.data.totalItems) {
          this.totalFeeds = res.data.totalItems;
        }
        res.data.connectomeFeeds.forEach(feed => {
          this.cardItems.push(new CardModel(feed as any, 'feed'));
        });
        // console.log(this.cardItems);
      });
  }

  processCard(res, items) {
    res.data.results.forEach(item => {
      item.dataSize = CARD_SIZE._1_1;
      item.favicon = item.faviconUrl ? item.faviconUrl : item.faviconBase64;
      item.writerName = item.author;
      item.recommendDate = item.publishedAt;
      item.imageLinks = this.imgSource(item);

      // const card = new CardModel(item as any, 'feed');
      // console.log('card=',card);
      items.push(item);
    });
  }

  imgSource(item: any) {
    return item.ogImageUrl || item.ogImageBase64 || (item.imageUrl && item.imageUrl[0]) || item.imageBase64;
  }

  handleActivity(item, activity) {
    if (activity.activity == ACTIVITY.delete) {
      this.cardItems.forEach((value, index) => {
        if (item.docId == value.docId) {
          this.cardItems.splice(index, 1);
        }
      });
    }
  }
}
function reject(err: any): any {
  throw new Error('Function not implemented.');
}
