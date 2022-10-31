import { Component, Inject, Vue, Watch } from 'vue-property-decorator';
import PublicService from '@/service/public.service';
import singleCard from '@/entities/new-learning-center/aside/single-card/single-card.vue';
import vueCustomScrollbar from 'vue-custom-scrollbar';
import { documentCard } from '@/shared/model/document-card.model';
import { getDomainFromUrl, onlyInLeft, timeDifference, toDataURL } from '@/util/ds-util';

@Component({
  components: {
    vueCustomScrollbar: vueCustomScrollbar,
    'single-card': singleCard,
  },
})
export default class web extends Vue {
  @Inject('publicService')
  publicService: () => PublicService;
  scrollSettings = {
    wheelPropagation: false,
  };
  searchValue = '';
  isShowSpinner = false;
  previewModels = [];

  preview() {
    if (!this.searchValue || this.searchValue == '') {
      return;
    }
    this.isShowSpinner = true;
    if (!this.previewModels) {
      this.previewModels = [];
    }
    this.publicService()
      .previewInfoByUrl(this.searchValue, JSON.parse(localStorage.getItem('ds-connectome')).connectomeId)
      .then(async res => {
        this.isShowSpinner = false;
        if (this.previewModels.length > 0 && res.data.url && this.previewModels.find(item => item.url == res.data.url)) {
          this.resetPreview();
          return;
        }

        const obj = new documentCard();
        if (res.data.author) obj.author = res.data.author;
        else obj.author = getDomainFromUrl(res.data.url);
        obj.title = res.data.title;
        obj.content = res.data.desc;
        obj.keyword = res.data.keyword;
        obj.type = 'URL';
        obj.searchType = 'WEB';
        if (obj.addedAt) obj.addedAt = timeDifference(new Date(), new Date(res.data.publicTime));
        obj.url = res.data.url;
        obj.images = [res.data.image ? res.data.image : res.data.imageAlt ? res.data.imageAlt : null];
        if (res.data.favicon) obj.favicon = await toDataURL(res.data.favicon);
        obj.noConvertTime = true;

        this.previewModels.push(obj);
        this.resetPreview();
      })
      .catch(() => {
        this.resetPreview();
        this.isShowSpinner = false;
      });
  }

  resetPreview() {
    this.searchValue = null;
  }

  totalSelected = 0;
  selectedItems = [];
  checked = false;

  selectAll() {
    if (this.selectedItems.length === this.previewModels.length) this.selectedItems = [];
    else this.selectedItems = this.previewModels;
  }
  @Watch('selectedItems')
  watchSelectedItems() {
    this.totalSelected = this.selectedItems.length;
  }

  @Watch('previewModels')
  dataChange() {
    const arrRight = this.checkArraySelected(true);
    console.log(arrRight);
    if (arrRight && arrRight.length > 0) {
      for (const item of arrRight) {
        let i = 0;
        while (i < this.selectedItems.length) {
          if (this.selectedItems[i].link == item.link) {
            this.selectedItems.splice(i, 1);
            break;
          }
          i++;
        }
      }
    }

    this.selectedItems.length === this.previewModels.length ? $('#btnSelect').addClass('active') : $('#btnSelect').removeClass('active');
    this.totalSelected = this.selectedItems.length;
    console.log('this.selectedItems', this.selectedItems);
    console.log('this.previewModels', this.previewModels);
  }

  checkArraySelected(arg?) {
    if (arg) return onlyInLeft(this.selectedItems, this.previewModels, ['url']);
    return onlyInLeft(this.previewModels, this.selectedItems, ['url']);
  }

  setSelectedItems(newData) {
    console.log('a');
    this.selectedItems = newData;
    this.totalSelected = this.selectedItems.length;
    this.selectedItems.length === this.previewModels.length ? $('#btnSelect').addClass('active') : $('#btnSelect').removeClass('active');
  }

  insertToMemory() {
    this.$root.$emit('cart-to-conlection', this.selectedItems, 'web');
  }

  deleteAll() {
    this.selectedItems = this.selectedItems.splice(0, 0);
    $('#btnSelect').removeClass('active');
  }
}
