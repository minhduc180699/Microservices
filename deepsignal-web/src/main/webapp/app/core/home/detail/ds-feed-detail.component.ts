import { Component, Inject, Prop, Vue } from 'vue-property-decorator';
import FeedService from '@/core/home/feed/feed.service';
import DsStockChart from '@/shared/chart/ds-stock-chart/ds-stock-chart.vue';
import axios from 'axios';
import DetailModuleTitle from '@/core/home/detail/detail-module-title/detail-module-title.vue';
import { InteractionUserService } from '@/service/interaction-user.service';
import FeedCommentComponent from '@/core/home/detail/feed-comment/feed-comment.vue';
import FeedMemoComponent from '@/core/home/detail/feed-memo/feed-memo.vue';
import DetailAnalysis from '@/core/home/detail/detail-analysis/detail-analysis.vue';
import DetailRelatedComponent from '@/core/home/detail/detail-related/detail-related.vue';
import { LearnedDocumentService } from '@/service/learned-document.service';
import { DOCUMENT_TYPE } from '@/shared/constants/ds-constants';
import SignalRelationshipDiagramAnalysisComponent from '@/core/home/signals/signal-today-issue/signal-relationship-diagram-analysis/signal-relationship-diagram-analysis.vue';
import SignalWordCloudChart from '@/core/home/signals/signal-today-issue/signal-word-cloud-chart/signal-word-cloud-chart.vue';
import SignalPeopleCompanyChart from '@/core/home/signals/signal-today-issue/signal-people-company-chart/signal-people-company-chart.vue';
import ActivityDetail from '@/core/home/detail/activity-detail/activity-detail.vue';
import DsChart from '@/shared/cards/chart/ds-chart.vue';
import MiniMap2dNetwork from '@/entities/my-ai/mini-connectome/mini-map-2d-network/mini-map-2d-network.vue';
import PeopleService from '@/core/home/people/people.service';
import RelatedPeopleCompany from '@/core/home/detail/detail-related-people-company/detail-related-people-company.vue';
import { CARD_SIZE, CARD_TYPE } from '@/shared/constants/feed-constants';

@Component({
  components: {
    'ds-stock-chart': DsStockChart,
    'detail-module-title': DetailModuleTitle,
    'feed-comment': FeedCommentComponent,
    'feed-memo': FeedMemoComponent,
    'detail-analysis': DetailAnalysis,
    'signal-relationship-diagram-analysis': SignalRelationshipDiagramAnalysisComponent,
    'signal-word-cloud': SignalWordCloudChart,
    'signal-people-company-chart': SignalPeopleCompanyChart,
    'detail-related': DetailRelatedComponent,
    'detail-activity': ActivityDetail,
    'ds-chart': DsChart,
    'mini-connectome-map': MiniMap2dNetwork,
    'related-people-company': RelatedPeopleCompany,
  },
})
export default class DsFeedDetail extends Vue {
  connectomeFeed = {} as any;
  isShowSpinner = false;
  stockCodes = [];
  youtubeDomain = ['www.youtube.com', 'm.youtube.com'];
  statistic: any = {};
  permission = true;
  loading = false;
  interval: any;
  isShowHtmlContent = false;
  related = [];
  relatedPeople = [];
  relatedCompany = [];
  relatedPeopleCompany = [];
  private swiperDataDesktop = {
    paginationClass: 'pagination-related-d',
    navigationNext: 'swiper-related-next-d',
    navigationPrev: 'swiper-related-prev-d',
    ref: 'swiperRelatedD',
    numData: 6,
  };
  private swiperDataMobile = {
    paginationClass: 'pagination-related-m',
    navigationNext: 'swiper-related-next-m',
    navigationPrev: 'swiper-related-prev-m',
    ref: 'swiperRelatedM',
    numData: 3,
  };
  private peopleCompanyDesktop = {
    paginationClass: 'swiper-pagination-people-d',
    navigationNext: 'swiper-people-next-d',
    navigationPrev: 'swiper-people-prev-d',
    ref: 'detailPeopleSwiper-d',
    numData: 6,
  };
  private peopleCompanyMobile = {
    paginationClass: 'swiper-pagination-people-m',
    navigationNext: 'swiper-people-next-m',
    navigationPrev: 'swiper-people-prev-m',
    ref: 'detailPeopleSwiper-m',
    numData: 6,
  };
  private isFixedDetailSide = 0;
  private isShowDetailSideRelated = true;
  private isShowDetailSidePeople = true;

  data() {
    return {
      swiperOptions: {
        spaceBetween: 50,
        pagination: {
          el: '.swiper-pagination',
          clickable: true,
        },
      },
    };
  }

  @Prop() readonly feedId: any | undefined;
  @Prop() readonly item: any | undefined;
  @Prop() readonly source: any | undefined;

  @Inject('feedService') private feedService: () => FeedService;
  @Inject('interactionUserService') private interactionUserService: () => InteractionUserService;
  @Inject('learnedDocumentService') private learnedDocumentService: () => LearnedDocumentService;
  @Inject('peopleService') private peopleService: () => PeopleService;

  created(): void {
    if (this.item) this.connectomeFeed = this.item;
  }

  mounted(): void {
    this.loadDataFromUrl();
    this.interval = setInterval(() => {
      const iframeRef = this.$refs.docsEmbed;
      if (iframeRef != undefined) {
        try {
          // google docs page is blank (204), hence we need to reload the iframe.
          // @ts-ignore
          if (iframeRef.contentDocument.URL == 'about:blank') {
            // @ts-ignore
            iframeRef.setAttribute('src', iframeRef.getAttribute('src'));
          } else {
            this.clearCheckingInterval();
          }
        } catch (e) {
          // google docs page is being loaded, but will throw CORS error.
          // it mean that the page won't be blank and we can remove the checking interval.
          this.onIframeLoaded();
        }
      }
    }, 4000); // 4000ms is reasonable time to load 2MB document
    window.addEventListener('scroll', () => {
      this.fixSideDetail();
    });
  }

  public totalComments(data) {
    this.statistic.totalComment = data;
  }

  public loadDataFromUrl() {
    this.loading = true;
    const connectomeId = this.$route.params.connectomeId;
    // this.cardData = this.item ? this.item : JSON.parse(localStorage.getItem('card-data'));
    if (!this.connectomeFeed) {
      const connnectomeUserId = JSON.parse(localStorage.getItem('ds-connectome')).connectomeId;
      if (connectomeId !== connnectomeUserId) {
        axios
          .get('/api/connectome-feed/checkPermissionShare', {
            params: {
              connectomeId: connectomeId,
              connectomeUserId: connnectomeUserId,
              cardId: this.feedId,
            },
          })
          .then(res => {
            if (res.data) {
              this.permission = true;
              this.getFeedById(connectomeId, this.feedId);
            } else {
              this.permission = false;
            }
          });
      } else {
        this.getFeedById(connectomeId, this.feedId);
      }
    } else {
      this.getFeedById(connectomeId, this.feedId);
    }
  }

  public getFeedById(connectomeId: string, id: string) {
    if (this.item) {
      this.feedService()
        .getFeedById(connectomeId, id)
        .then(res => {
          this.connectomeFeed = res.data;
          this.loading = false;
          this.convertPersonalToFeed();
          this.getTitles(this.connectomeFeed.relatedPeople, 'PEOPLE');
          this.getTitles(this.connectomeFeed.relatedCompany, 'COMPANY');
          this.getPeopleAndCompany();
        })
        .catch(err => {
          this.loading = false;
          this.isShowDetailSidePeople = false;
        });
    } else {
      this.feedService()
        .getFeedById(connectomeId, id)
        .then(res => {
          this.connectomeFeed = res.data;
          this.convertPersonalToFeed();
          this.loading = false;
          this.getTitles(this.connectomeFeed.relatedPeople, 'PEOPLE');
          this.getTitles(this.connectomeFeed.relatedCompany, 'COMPANY');
          this.getPeopleAndCompany();
        })
        .catch(err => {
          this.loading = false;
          this.isShowDetailSidePeople = false;
        });
    }
  }

  public onSlideChange() {
    // @ts-ignore
    const index = this.$refs.mySwiper.$swiper.activeIndex;
    if (!this.stockCodes[index].dataChart && -1 != index) {
      this.isShowSpinner = true;
      const stockCode = {
        marketName: this.stockCodes[index].market.toUpperCase(),
        symbolCode: this.stockCodes[index].stockCode.toUpperCase(),
        interval: '15m',
        maxRecord: '10',
      };
      this.feedService()
        .getStock(stockCode)
        .then(res => {
          Object.assign(this.stockCodes[index], { dataChart: res.data });
          this.isShowSpinner = false;
          this.$forceUpdate();
        });
    }
  }

  beforeRouteLeave(to, from, next) {
    $('.item-list-top').css('margin-top', '60px');
    next();
  }

  getEmbedLink(url) {
    const id = url.split('?v=')[1];
    return 'https://www.youtube.com/embed/' + id;
  }

  isYoutube(url) {
    return this.youtubeDomain.some(domain => url.split('/').includes(domain));
  }

  onSaveCommentSuccess() {
    this.statisticComment();
  }

  statisticComment() {
    this.interactionUserService()
      .statistic(this.connectomeFeed.id)
      .then(res => {
        this.statistic = res.data;
      });
  }

  backFeed() {
    this.$router.go(-1);
  }

  clearCheckingInterval() {
    clearInterval(this.interval);
  }

  onIframeLoaded() {
    this.clearCheckingInterval();
  }

  convertPersonalToFeed() {
    if (
      this.connectomeFeed.ogImageUrl ||
      this.connectomeFeed.ogImageBase64 ||
      (this.connectomeFeed.imageUrl && this.connectomeFeed.imageUrl[0]) ||
      this.connectomeFeed.imageBase64
    ) {
      this.connectomeFeed.writerName = this.connectomeFeed.author;
      this.connectomeFeed.imageLinks = [
        this.connectomeFeed.ogImageUrl ||
          this.connectomeFeed.ogImageBase64 ||
          (this.connectomeFeed.imageUrl && this.connectomeFeed.imageUrl[0]) ||
          this.connectomeFeed.imageBase64,
      ];
      this.connectomeFeed.sourceId = this.connectomeFeed.url;
      this.connectomeFeed.recommendDate = this.connectomeFeed.publishedAt;
    }
  }

  getPersonalDocumentById(connectomeId: string, id: string) {
    this.learnedDocumentService()
      .getPersonalDocumentById(connectomeId, id)
      .then(res => {
        this.connectomeFeed = res.data;
        this.connectomeFeed.writerName = this.connectomeFeed.author;
        this.connectomeFeed.imageLinks = [
          this.connectomeFeed.ogImageUrl ||
            this.connectomeFeed.ogImageBase64 ||
            (this.connectomeFeed.imageUrl && this.connectomeFeed.imageUrl[0]) ||
            this.connectomeFeed.imageBase64,
        ];
        this.connectomeFeed.sourceId = this.connectomeFeed.url;
        this.connectomeFeed.recommendDate = this.connectomeFeed.publishedAt;
        this.loading = false;
      })
      .catch(err => {
        // console.log('err get personal document by id: ', err);
        this.loading = false;
      });
  }

  getTitles(data, type) {
    data.famous.forEach((item, index) => {
      const value = item.toLowerCase();
      if (type == 'PEOPLE') {
        this.relatedPeople.push(value);
      } else {
        this.relatedCompany.push(value);
      }
      this.related.push(item);
    });
    // data.anonymous.forEach((item, index) => {
    //   const value = item.toLowerCase();
    //   if (type == 'PEOPLE') {
    //     this.relatedPeople.push(value);
    //   } else {
    //     this.relatedCompany.push(value);
    //   }
    //   this.related.push(item);
    // });
  }

  getPeopleAndCompany() {
    const peopleAndCompany = {
      sourceLang: this.$store.getters.currentLanguage.toUpperCase(),
      titles: this.related,
      getOnlyImages: false,
    };
    const result = this.peopleService().getPeopleAndCompany(peopleAndCompany);
    result
      .then(res => {
        if (res.data && res.data.length > 0) {
          this.isShowDetailSidePeople = true;
          res.data.forEach((item, index) => {
            if (this.relatedPeople.indexOf(item.label.toLowerCase()) > -1) {
              item.type = 'PEOPLE';
            } else {
              item.type = 'COMPANY';
            }
            this.relatedPeopleCompany.push(item);
          });
        } else {
          this.isShowDetailSidePeople = false;
        }
      })
      .catch(error => {
        this.isShowDetailSidePeople = false;
      });
  }

  viewPost(link) {
    const connectome = localStorage.getItem('ds-connectome')
      ? JSON.parse(localStorage.getItem('ds-connectome'))
      : JSON.parse(sessionStorage.getItem('ds-connectome'));
    const userId = connectome?.user?.id;

    const externalUrl = {
      userId: userId ? userId : '',
      connectomeId: this.$route.params?.connectomeId ? this.$route.params?.connectomeId : '',
      url: this.$route.path,
      originalUrl: this.connectomeFeed.sourceId ? this.connectomeFeed.sourceId : '',
      title: this.connectomeFeed.title ? this.connectomeFeed.title : '',
    };

    axios.post('/api/url-tracking', externalUrl).then();
    window.open(link, '_blank');
  }

  fixSideDetail() {
    const body = document.body,
      html = document.documentElement;

    const height = Math.max(body.scrollHeight, body.offsetHeight, html.clientHeight, html.scrollHeight, html.offsetHeight);

    if (window.scrollY > height - screen.height + 90) {
      this.isFixedDetailSide = 2;
    } else if (window.scrollY > 50) {
      this.isFixedDetailSide = 1;
    } else {
      this.isFixedDetailSide = 0;
    }
  }

  hideDetailSide(state) {
    this.isShowDetailSideRelated = state;
  }
}
