import { Component, Inject, Vue, Watch } from 'vue-property-decorator';
import DsCards from '@/shared/cards/ds-cards.vue';
import PeopleService from '../people/people.service';
import FeedService from '../feed/feed.service';
import DsStockChart from '@/shared/chart/ds-stock-chart/ds-stock-chart.vue';
import DetailRelatedComponent from '@/core/home/detail/detail-related/detail-related.vue';
import ActivityDetail from '@/core/home/detail/activity-detail/activity-detail.vue';
import DetailAnalysis from '@/core/home/detail/detail-analysis/detail-analysis.vue';

@Component({
  components: {
    'ds-stock-chart': DsStockChart,
    DsCards,
    'detail-related': DetailRelatedComponent,
    'detail-activity': ActivityDetail,
    'detail-analysis': DetailAnalysis,
  },
})
export default class DsPeopleDetail extends Vue {
  @Inject('peopleService')
  private peopleService: () => PeopleService;
  @Inject('feedService')
  private feedService: () => FeedService;

  // name = null;
  stockCode = null;
  isShowSpinner = false;
  image = null;
  wikiList = [];
  docList = [];
  page = 1;
  totalPage = -1;
  size = 6;
  cardItems = [];
  loaderDisable = true;
  stockCodes = [];
  risingPeopleList = [];
  people = {} as any;
  social = [];

  data() {
    return {
      cardItems: this.cardItems,
      swiperOptions: {
        spaceBetween: 50,
        pagination: {
          el: '.swiper-pagination',
          clickable: true,
        },
      },
      people: this.people,
    };
  }

  created(): void {
    this.stockCode = this.$route.query.stockCode;
  }

  @Watch('$route.query.name')
  onNameChange(value) {
    this.stockCode = this.$route.query.stockCode;
    this.stockCodes = [];
    this.wikiList = [];
    this.image = '';
    this.risingPeopleList = [];
    this.cardItems = [];
    this.getWikiInfo();
  }

  mounted() {
    if (!this.cardItems) {
      this.cardItems = [];
    }
    this.getWikiInfo();
  }

  scrollLoader() {
    if (this.page < this.totalPage) {
      this.page += 1;
      this.showCardList();
    } else if (this.totalPage != -1) {
      this.loaderDisable = true;
    }
  }

  getWikiInfo() {
    const title = this.$route.query.name;
    const peopleAndCompany = {
      sourceLang: this.$store.getters.currentLanguage.toUpperCase(),
      titles: [title],
      getOnlyImages: false,
    };
    const result = this.peopleService().getPeopleAndCompany(peopleAndCompany);
    if (!result) {
      return;
    }
    result.then(res => {
      this.risingPeopleList = res.data;
      this.risingPeopleList.map((item, index) => (item.type = 'people'));
      for (const item of res.data) {
        if (item.label === this.$route.query.name && item.wikipediaInfobox) {
          this.people = item;
          for (const [key, value] of Object.entries(item.wikipediaInfobox)) {
            if (key === 'image') {
              this.image = value;
            }

            const wiki = {
              name: key,
              value: value,
            };
            this.wikiList.push(wiki);
          }
        }
      }
      this.showCardList();
    });
  }

  showCardList() {
    let content = '';
    this.risingPeopleList.forEach(data => {
      if (!data.googleInfobox.description && !data.wikipediaInfobox.title) {
        content = '';
        Object.keys(data.googleInfobox).forEach(key => {
          content += key + ': ' + data.googleInfobox[key] + '\n';
        });
      }
      const item = {
        dataTemplate: data.type,
        dataSize: '1x2',
        dataType: '01',
        name: data.label,
        title: data.title,
        date: '01',
        image: data.imageUrl,
        content: data.googleInfobox.description || data.wikipediaInfobox.title || content,
        source: data.googleInfobox.description_url || data.wikiUrl || '',
        stockCode: (data.stock && data.stock.stockCode) || '',
      };
      this.cardItems.push(item);
    });
  }

  getChartData() {
    this.peopleService()
      .getMarketName(this.stockCode)
      .then(res => {
        const marketName = res.data.marketName;

        const stockCode = {
          marketName: marketName.toUpperCase(),
          symbolCode: this.stockCode.toUpperCase(),
          interval: '15m',
          maxRecord: '10',
        };
        this.feedService()
          .getStock(stockCode)
          .then(res => {
            if (this.stockCodes.length == 0) {
              this.stockCodes[0] = Object.assign({}, { dataChart: res.data });
              this.stockCodes[0].market = marketName.toUpperCase();
              this.stockCodes[0].stockCode = this.stockCode.toString().toUpperCase();
            } else {
              Object.assign(this.stockCodes[0], { dataChart: res.data });
            }
            // this.isShowSpinner = false;
            this.$forceUpdate();
          });
      });
  }

  public onSlideChange() {
    // @ts-ignore
    const index = this.$refs.mySwiper.$swiper.activeIndex;
    if (!this.stockCodes[index].dataChart && -1 != index) {
      this.isShowSpinner = true;
      const stockCode = {
        marketName: this.stockCodes[index].market.toUpperCase(),
        symbolCode: this.stockCodes[index].symbol.toUpperCase(),
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

  goBack() {
    this.$router.go(-1);
  }
}
function then(arg0: (res: any) => void) {
  throw new Error('Function not implemented.');
}
