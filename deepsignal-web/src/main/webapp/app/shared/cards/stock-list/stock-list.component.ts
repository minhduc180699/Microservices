import { Component, Inject, Prop, Vue, Watch } from 'vue-property-decorator';
import axios from 'axios';
import FeedService from '@/core/home/feed/feed.service';
import { CARD_SIZE } from '@/shared/constants/feed-constants';

@Component
export default class stockList extends Vue {
  @Prop(Object) readonly item: any | undefined;
  @Inject('feedService')
  private feedService: () => FeedService;
  private isSearchStock = false;
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
  private isShowSpinner = false;
  private keySearch = '';
  private resultSearch = [];
  private dataShow = [];

  @Watch('item')
  onItemChange(value) {
    this.initData();
  }

  searchStock(searchStock) {
    this.isSearchStock = searchStock;
    console.log('search-stock', this.isSearchStock);
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
          this.isSearchStock = false;
          this.isShowSpinner = false;
          if (this.dataShow.length > 4) {
            this.dataShow.splice(4, 1);
          }
          this.dataShow.unshift(this.convertData(res.data, stockCode));
        })
        .catch(error => {
          this.isSearchStock = false;
          this.isShowSpinner = false;
        });
    }
  }

  mounted() {
    this.initData();
  }

  initData() {
    this.dataShow = [];
    this.resultSearch = this.popular;
    if (this.item.stockCodes.length > 0) {
      this.item.stockCodes.forEach((item, index) => {
        const stockCode = {
          marketName: this.item.stockCodes[index].market.toUpperCase(),
          symbolCode: this.item.stockCodes[index].stockCode.toUpperCase(),
          interval: '15m',
          maxRecord: '10',
        };
        this.feedService()
          .getStock(stockCode)
          .then(res => {
            this.dataShow.push(this.convertData(res.data, stockCode));
          });
      });
    }
  }

  takeDecimalNumber(num, n) {
    const base = 10 ** n;
    return Math.round(num * base) / base;
  }

  convertData(data, stockCode) {
    const index = {
      title: String,
      up: true,
      index: 0,
      difference: 0,
    };
    index.title = stockCode.symbolCode;
    index.index = this.takeDecimalNumber(data[data.length - 1].close, 3);
    const difference = this.takeDecimalNumber(Number(data[data.length - 1].close - data[0].open), 3);
    if (difference > 0) {
      index.up = true;
    } else {
      index.up = false;
    }
    index.difference = Math.abs(difference);
    return index;
  }

  handleChangeTypeStock() {
    this.$root.$emit('change-type-stock', CARD_SIZE._2_1);
  }

  openPopUpUserSetting() {
    this.$bvModal.show('modal-member-setting');
  }

  hideStockCard() {
    this.$root.$emit('hide-stock-card');
  }
}
