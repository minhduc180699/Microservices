import { Component, Prop, Vue } from 'vue-property-decorator';
import DsCardPeopleRising from '@/shared/cards/trend-people-rising/trend-people-rising.vue';

@Component({
  components: {
    'ds-card-people-rising': DsCardPeopleRising,
  },
})
export default class RelatedPeopleCompany extends Vue {
  @Prop(Array) readonly dataRelated: any | [];
  @Prop(Object) readonly dataSwiper: any;
  private activePage = 0;
  private activePrev = false;
  private activeNext = true;

  data() {
    return {
      // see more options in: https://swiperjs.com/get-started#swiper-css-styles-size
      swiperOptions: {
        slidesPerView: 1,
        spaceBetween: 32,
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
          1300: {
            slidesPerView: 1,
          },
          992: {
            slidesPerView: 3,
            slidesPerGroup: 3,
          },
          768: {
            slidesPerView: 2,
            slidesPerGroup: 2,
          },
          100: {
            slidesPerView: 1,
          },
        },
      },
    };
  }

  // onSlideChange() {
  //   // @ts-ignore
  //   this.activePage = this.$refs.detailPeopleSwiper.$swiper.activeIndex;
  //   if (this.activePage == 0) {
  //     this.activePrev = false;
  //   }
  //   if (this.activePage == this.dataRelated.length - 1) {
  //     this.activeNext = false;
  //   }
  //   if (this.activePage > 0 && this.activePage < this.dataRelated.length - 1) {
  //     this.activeNext = true;
  //     this.activePrev = true;
  //   }
  // }
}
