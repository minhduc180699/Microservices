import { Component, Prop, Vue, Watch } from 'vue-property-decorator';
import DsCardFooter from '@/shared/cards/footer/ds-card-footer.vue';
import vueCustomScrollbar from 'vue-custom-scrollbar';
import { PeopleRankingModel } from '@/shared/cards/people/people-ranking/people-ranking.model';

@Component({
  components: {
    'ds-card-footer': DsCardFooter,
    vueCustomScrollbar,
  },
})
export default class DsCardPeopleRanking extends Vue {
  @Prop(Array) readonly dataTransfer: any | [];
  private dataSearch: PeopleRankingModel[] = [];
  private cardActive = 0;
  private textSearch = '';
  private tabActive = 'All';
  private isMobile = /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent);
  private scrollSettings = {
    wheelPropagation: false,
  };

  created(): void {
    Object.assign(
      this.scrollSettings,
      this.isMobile ? { suppressScrollX: false, suppressScrollY: true } : { suppressScrollX: true, suppressScrollY: false }
    );
  }

  @Watch('dataTransfer')
  detectDataChange(data: PeopleRankingModel[]) {
    this.dataSearch = data;
  }

  searchInput() {
    this.cardActive = 100;
    this.dataSearch = [];
    this.dataSearch = this.dataTransfer.filter(item => {
      if (this.tabActive.toLowerCase() === 'all') {
        return item.title.toLowerCase().includes(this.textSearch.toLowerCase());
      } else {
        return item.title.toLowerCase().includes(this.textSearch.toLowerCase()) && item.type.toLowerCase() === this.tabActive.toLowerCase();
      }
    });
  }

  changeItem(item, index) {
    this.cardActive = index;
    this.dataTransfer.forEach((value, num) => {
      if (item.title == value.title) {
        this.$emit('changeItem', item, index);
      }
    });
  }

  filterPart(filter: string) {
    this.tabActive = filter;
    if (filter == 'All') {
      this.dataSearch = this.dataTransfer;
    } else {
      this.dataSearch = this.dataTransfer.filter(item => {
        return item.type == filter.toUpperCase();
      });
    }
  }

  clearSearch() {
    this.textSearch = '';
    this.searchInput();
  }
}
