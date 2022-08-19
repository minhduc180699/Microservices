import { Component, Inject, Prop, Vue, Watch } from 'vue-property-decorator';
import { CARD_SIZE } from '@/shared/constants/feed-constants';
import { getMaxImageBySize } from '@/util/ds-feed-util';
import DsCardFooter from '@/shared/cards/footer/ds-card-footer.vue';
import DsStockChart from '@/shared/chart/ds-stock-chart/ds-stock-chart.vue';
import FeedService from '@/core/home/feed/feed.service';
import axios from 'axios';
import 'flag-icons/css/flag-icons.min.css';
import { randomEleInArray } from '@/util/array-util';

@Component({
  components: {
    'ds-card-footer': DsCardFooter,
    'ds-stock-chart': DsStockChart,
  },
})
export default class DsChartComponent extends Vue {
  @Prop(Object) readonly item: any | undefined;

  @Inject('feedService')
  private feedService: () => FeedService;
  private isSearchStock = false;
  image;
  imageFull;
  isImage;
  isShowSpinner = false;
  private popular = [
    {
      marketCountry: 'US',
      marketName: 'NASDAQ',
      symbolCode: 'GOOG',
      symbolNameKr: '알파벳 C',
      symbolNameEn: 'Alphabet Inc Class C',
    },
    { marketCountry: 'US', marketName: 'NASDAQ', symbolCode: 'AAPL', symbolNameKr: '애플', symbolNameEn: 'Apple Inc' },
    {
      marketCountry: 'US',
      marketName: 'NASDAQ',
      symbolCode: 'FB',
      symbolNameKr: 'Meta Platforms Inc',
      symbolNameEn: 'Meta Platforms Inc',
    },
    {
      marketCountry: 'KR',
      marketName: 'KOSPI',
      symbolCode: '005930',
      symbolNameKr: '삼성전자',
      symbolNameEn: 'Samsung Electronics Co Ltd',
    },
    {
      marketCountry: 'KR',
      marketName: 'KOSDAQ',
      symbolCode: '319400',
      symbolNameKr: '삼성전자',
      symbolNameEn: 'Huyndai Movex Co Ltd',
    },
  ];
  private resultSearch = [];
  private keySearch = '';
  private title = null;
  private isFeed = true;

  data() {
    return {
      image: this.image,
      imageFull: this.imageFull,
      swiperOptions: {
        spaceBetween: 20,
        pagination: {
          el: '.swiper-stock-chart',
          type: 'bullets',
          clickable: true,
        },
        navigation: {
          nextEl: '.swiper-next-stock-chart',
          prevEl: '.swiper-prev-stock-chart',
        },
      },
    };
  }

  @Watch('item')
  onItemChange() {
    this.initData();
  }

  onSlideChange() {
    // @ts-ignore
    const index = this.mySwiperChart.activeIndex;
    this.title = this.item.stockCodes[index].stockCode;
    if (!this.item.stockCodes[index].dataChart && -1 != index) {
      const stockCode = {
        marketName: this.item.stockCodes[index].market.toUpperCase(),
        symbolCode: this.item.isCrypto
          ? this.item.stockCodes[index].symbol.toUpperCase()
          : this.item.stockCodes[index].stockCode.toUpperCase(),
        interval: this.item.isCrypto ? '5m' : '15m',
        maxRecord: this.item.isCrypto ? '30' : '10',
      };
      this.feedService()
        .getStock(stockCode)
        .then(res => {
          Object.assign(this.item.stockCodes[index], { dataChart: res.data });
          this.$forceUpdate();
        });
    }
  }

  onErrorImage() {
    if (this.isImage) {
      this.image = randomEleInArray(this.item.subImages);
    } else {
      this.imageFull = randomEleInArray(this.item.subImages);
    }
  }

  mounted() {
    if (this.$route.name != 'Feed') {
      this.isFeed = false;
    }
    this.initData();
  }

  initData() {
    this.resultSearch = this.popular;
    this.randomImageOrFullImage();
    const stockCode = {
      marketName: this.item.stockCodes[0].market.toUpperCase(),
      symbolCode: this.item.isCrypto ? this.item.stockCodes[0].symbol.toUpperCase() : this.item.stockCodes[0].stockCode.toUpperCase(),
      interval: this.item.isCrypto ? '5m' : '15m',
      maxRecord: this.item.isCrypto ? '30' : '10',
    };
    this.feedService()
      .getStock(stockCode)
      .then(res => {
        Object.assign(this.item.stockCodes[0], { dataChart: res.data });
        this.title = this.item.stockCodes[0].stockCode;
        this.$forceUpdate();
      });

    this.$parent.$on('choseStock', this.choseStock);
    this.$parent.$on('chooseStockFromCard', this.slideTo);
  }

  randomImageOrFullImage() {
    const imgLucky = randomEleInArray(['image', 'imageFull']);
    if (imgLucky == 'imageFull' && this.item.dataSize == CARD_SIZE._2_1) {
      this.isImage = false;
      this.imageFull = getMaxImageBySize(this.item.subImages);
    } else {
      this.isImage = true;
      this.image = getMaxImageBySize(this.item.subImages);
    }
  }

  saveToLocalStorage() {
    if (localStorage.getItem('stock-codes')) {
      localStorage.removeItem('stock-codes');
    }
    localStorage.setItem('stock-codes', JSON.stringify(this.item.stockCodes));
  }

  // searchStock() {
  //   this.isSearchStock = !this.isSearchStock;
  // }

  choseStock(item) {
    if (this.item.stockCodes.length > 0) {
      this.isShowSpinner = true;
      this.keySearch = '';
      this.resultSearch = this.popular;
      const stock = { market: item.marketName, stockCode: item.symbolCode };
      this.item.stockCodes.forEach((val, index) => {
        if (val.market == stock.market && val.stockCode == stock.stockCode) {
          this.item.stockCodes.splice(index, 1);
        }
      });
      this.item.stockCodes.unshift(stock);
      const stockCode = {
        marketName: this.item.stockCodes[0].market.toUpperCase(),
        symbolCode: this.item.stockCodes[0].stockCode.toUpperCase(),
        interval: '15m',
        maxRecord: '10',
      };
      this.feedService()
        .getStock(stockCode)
        .then(res => {
          Object.assign(this.item.stockCodes[0], { dataChart: res.data });
          this.isShowSpinner = false;
          this.title = this.item.stockCodes[0].stockCode;
          this.$forceUpdate();
        });
      this.isSearchStock = false;
    }
  }

  searching() {
    if (this.keySearch !== '') {
      axios
        .get('/api/connectome-feed/searchStock', {
          params: {
            search: this.keySearch,
          },
        })
        .then(res => {
          this.resultSearch = res.data;
        });
    } else {
      this.resultSearch = this.popular;
    }
  }

  changeIsSearchStock() {
    // @ts-ignore
    this.$parent.searchStock();
  }

  slideTo(index: any) {
    if (index != -1) {
      // @ts-ignore
      this.$refs.mySwiper.$swiper.slideToLoop(index);
    }
  }

  searchStock(searchStock) {
    this.isSearchStock = searchStock;
  }

  handleChangeTypeStock() {
    this.$root.$emit('change-type-stock', CARD_SIZE._1_1);
  }

  openPopUpUserSetting() {
    this.$bvModal.show('modal-member-setting');
  }

  hideStockCard() {
    this.$root.$emit('hide-stock-card');
  }
}
