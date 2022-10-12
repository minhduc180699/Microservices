import { Component, Inject, Vue, Watch } from 'vue-property-decorator';
import PublicService from '@/service/public.service';

@Component({})
export default class addUrl extends Vue {
  @Inject('publicService')
  private publicService: () => PublicService;
  private searchValue = '';
  private isShowSpinner = false;
  public previewModels = [];
  public numberOfWeb = 0;
  public selectedCards = [];
  private disableButton = true;
  private updateList = 'urlList';

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
      .then(res => {
        this.isShowSpinner = false;
        if (this.previewModels.length > 0 && res.data.url && this.previewModels.find(item => item.url == res.data.url)) {
          this.resetPreview();
          return;
        }
        const obj = {
          ...res.data,
          favicon: this.getFavicon(res.data.url),
          selected: true,
        };
        this.previewModels.push(obj);
        this.disableButton = false;
        this.numberOfWeb = this.previewModels.length;
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

  getFavicon(url) {
    const domain = new URL(url);
    domain.hostname.replace('www.', '');
    return domain.protocol + '//' + domain.hostname + '/favicon.ico';
  }

  selectCard(position) {
    this.previewModels[position].selected = !this.previewModels[position].selected;
    this.updateList += '1';
    this.checkDisableButton();
  }

  addToCollection() {
    const data = [];
    for (let i = 0; i < this.previewModels.length; i++) {
      if (this.previewModels[i].selected) {
        data.push(this.previewModels[i]);
        this.previewModels.splice(i, 1);
        i--;
      }
    }
    this.$store.commit('setUrlCards', data);
  }

  checkDisableButton() {
    this.disableButton = true;
    this.previewModels.forEach(item => {
      if (item.selected) {
        this.disableButton = false;
        return;
      }
    });
  }
}
