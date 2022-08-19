import { Component, Inject, Prop, Vue, Watch } from 'vue-property-decorator';
import { DetailFeedService } from '@/service/detail-feed.service';
import { PrincipalService } from '@/service/principal.service';
import { SwiperDetailRelated } from '@/core/home/detail/detail-related/detail-related.model';
import checkMobile, { ACTIVITY } from '@/shared/constants/feed-constants';

@Component
export default class DetailRelatedComponent extends Vue {
  @Prop(String) readonly feedDocId: string | '';
  @Prop(Object) readonly dataSwiper: any;
  @Inject('detailFeedService') private detailFeedService: () => DetailFeedService;
  @Inject('principalService') private principalService: () => PrincipalService;
  connectomeId;
  swipersDetailRelated: SwiperDetailRelated[] = [];
  totalElements = 0;
  totalPages = 0;
  currentPage = 1;
  loading = false;
  private dataReceived = [];
  private dataShow = [];
  private activePage = 0;
  private activeNav = false;
  private activePrev = false;
  private activeNext = true;

  created() {
    this.connectomeId = this.principalService().getConnectomeInfo().connectomeId;
    this.getRelatedDocs();
  }

  data() {
    return {
      // see more options in: https://swiperjs.com/get-started#swiper-css-styles-size
      swiperOptions: {
        slidesPerView: 1,
        spaceBetween: 24,
        pagination: {
          el: '.' + this.dataSwiper.paginationClass,
          type: 'fraction',
          clickable: true,
        },
        navigation: {
          nextEl: '.' + this.dataSwiper.navigationNext,
          prevEl: '.' + this.dataSwiper.navigationPrev,
        },
        breakpoints: {
          270: {
            slidesPerView: 1,
          },
          1300: {
            slidesPerView: 1,
          },
          768: {
            slidesPerView: 2,
            slidesPerGroup: 2,
          },
        },
      },
    };
  }

  onSlideChange() {
    // @ts-ignore
    // this.activePage = this.$refs.detailRelatedSwiper.$swiper.activeIndex;
  }

  getRelatedDocs() {
    this.loading = true;
    this.detailFeedService()
      .getRelatedDocs(this.connectomeId, this.feedDocId)
      .then(res => {
        this.dataReceived = res.data.data;
        if (!this.dataReceived || this.dataReceived.length < 1) {
          this.$emit('hideDetailSide', false);
        } else {
          // @ts-ignore
          this.$emit('hideDetailSide', true);
        }
        this.dataShow = this.convertData(this.dataReceived);
      })
      .catch(error => {
        // @ts-ignore
        this.$emit('hideDetailSide', false);
      })
      .finally(() => (this.loading = false));
  }

  convertData(data?) {
    let num = 1;
    let value = [];
    const dataShow = [];
    data.forEach((item, index) => {
      if (index == num * this.dataSwiper.numData) {
        dataShow.push(value);
        value = [];
        num++;
      }
      if (index < num * this.dataSwiper.numData) {
        value.push(item);
      }
      if (index == data.length - 1) {
        dataShow.push(value);
      }
    });
    return dataShow;
  }
}
