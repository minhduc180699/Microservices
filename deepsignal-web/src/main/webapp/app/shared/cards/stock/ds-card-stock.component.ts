import { Component, Prop, Vue } from 'vue-property-decorator';
import { CARD_TYPE } from '@/shared/constants/feed-constants';

@Component({})
export default class DsCardStock extends Vue {
  @Prop(Object) readonly item: any | undefined;
  isLoadingCompanyStock = true;
  companies = [];
  stocks = [];

  // data() {
  //   return {
  //     isLoadingCompanyStock: this.isLoadingCompanyStock,
  //   };
  // }

  mounted(): void {
    if (this.item.stockCodes && this.item.stockCodes.length > 0) {
      this.item.stockCodes.slice(0, 10).forEach(item => {
        this.stocks.push(item);
      });
      if (this.stocks.length < 6) {
        this.$root.$emit('delete-card-issue', CARD_TYPE.STOCK, true);
      }
      this.isLoadingCompanyStock = false;
    } else {
      this.$root.$emit('delete-card-issue', CARD_TYPE.STOCK);
    }

    // this.$store.watch(
    //   () => this.$store.getters.getCompanies,
    //   item => {
    //     this.companies = this.$store.getters.getCompanies;
    //   }
    // );
  }

  chooseStock(stock) {
    const index = this.item.stockCodes.indexOf(stock);
    this.$parent.$emit('chooseStockFromCard', index);
    // window.open('https://www.investing.com/search/?q=' + stock.stockCode, '_blank');
    // window.focus();
  }
}
