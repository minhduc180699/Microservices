import { Component, Vue } from 'vue-property-decorator';
import MetaSearchSelectedComponent from '@/entities/connectome/meta-search/meta-search-selected/meta-search-selected.vue';
import { MetaSearchData } from '@/entities/connectome/meta-search/meta-search-data';
import MetaSearchDetailComponent from '@/entities/connectome/meta-search/meta-search-detail/meta-search-detail.vue';

@Component({
  components: {
    'meta-search-selected': MetaSearchSelectedComponent,
    'meta-search-detail': MetaSearchDetailComponent,
  },
})
export default class MetaSearchComponent extends Vue {
  public itemsSelected = [];
  public connectomes = MetaSearchData;
  public metaSearchItem = '';

  public keyword: string | (string | null)[] = '';

  onSelectItem(item) {
    item.checked = !item.checked;
    MetaSearchData.map(item => item.checked);
    this.itemsSelected = this.filterItems(MetaSearchData);
  }

  remove(e) {
    e.checked = false;
    this.itemsSelected = this.filterItems(this.itemsSelected);
  }

  filterItems(items: Array<any>) {
    if (!items || items.length == 0) {
      return;
    }
    return items.filter(item => item.checked);
  }

  updateDetail(searchItem: any) {
    this.metaSearchItem = searchItem;
  }

  mounted() {
    this.keyword = this.$route.query.keyword;
  }
}
