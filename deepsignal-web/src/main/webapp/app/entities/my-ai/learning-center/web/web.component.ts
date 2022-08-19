import { Component, Inject, Prop, PropSync, Vue, Watch } from 'vue-property-decorator';
import PublicService from '@/service/public.service';
import axios from 'axios';
import pagination from '@/entities/connectome/learning-management/pagination/pagination.vue';
import { FILE_TYPE_UPLOAD } from '@/shared/constants/ds-constants';

@Component({
  components: {
    pagination: pagination,
  },
  computed: {
    checkLearning() {
      return this.$store.getters.getLearning;
    },
  },
  watch: {
    checkLearning(value) {
      if (value != this.isLearning) {
        this.isLearning = value;
      }
    },
  },
})
export default class web extends Vue {
  @Prop(String) readonly currentTab: string | any;
  @PropSync('displayMode', String) displayModeSync;
  @Inject('publicService')
  private publicService: () => PublicService;
  private showSearch = false;
  searchValue = '';
  public previewModels = [];
  public numberOfWeb = 0;
  private cardSelected = [];
  private cardSelectedToDelete = [];
  private postSelectedWeb = [];
  private isLoading = true;
  private allDataWeb = [];
  private page = 1;
  private size = 20;
  private totalPage = 0;
  private totalPosts = 0;
  private isShowSpinner = false;
  private isSelectAll = false;
  private isSelectAllLearningComplete = false;
  private isLearning = false;

  created() {
    this.$parent.$on('unSelectedWeb', this.unSelectedWeb);
    this.$root.$on('deleteFile', this.getApi);
    // this.getApi();
  }

  @Watch('allDataWeb')
  onAllDataSearchChange(newVal, oldVal) {
    return (this.totalPosts = this.allDataWeb.length);
  }

  unSelectedWeb() {
    // this.previewModels = [];
    this.cardSelected = [];
    this.postSelectedWeb = [];
    if (this.cardSelected.length == 0) {
      this.isSelectAll = false;
    }
  }

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
        };
        this.previewModels.push(obj);
        this.numberOfWeb = this.previewModels.length;
        this.resetPreview();
        this.selectCard(obj, this.previewModels.length - 1);
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

  clearPreviewModels() {
    this.previewModels = [];
  }

  selectAllLearningComplete() {
    this.isSelectAllLearningComplete = !this.isSelectAllLearningComplete;
    if (this.isSelectAllLearningComplete) {
      this.allDataWeb.forEach((item, num) => {
        if (this.cardSelectedToDelete.indexOf(num) === -1) {
          this.cardSelectedToDelete.push(num);
        }
      });
    } else {
      this.cardSelectedToDelete = [];
    }
  }

  selectCard(item, index) {
    const num = this.cardSelected.indexOf(index);
    if (num > -1) {
      this.cardSelected.splice(num, 1);
      this.postSelectedWeb.forEach((value, st) => {
        if (value.url === item.url) {
          this.postSelectedWeb.splice(st, 1);
        }
      });
    } else {
      this.cardSelected.push(index);
      let check = false;
      this.postSelectedWeb.forEach((value, st) => {
        if (value.url === item.url) {
          check = true;
        }
      });
      if (!check) {
        this.postSelectedWeb.push(item);
      }
    }
    if (this.cardSelected.length == this.previewModels.length) {
      this.isSelectAll = true;
    }
    if (this.cardSelected.length == 0) {
      this.isSelectAll = false;
    }
    this.$emit('handleSelectedWeb', this.postSelectedWeb);
  }

  addLearning() {
    this.previewModels.forEach((item, num) => {
      if (this.cardSelected.indexOf(num) === -1) {
        this.cardSelected.push(num);
        this.postSelectedWeb.push(this.previewModels[num]);
      }
    });
    this.$emit('handleSelectedWeb', this.postSelectedWeb);
  }

  startLearning() {
    // @ts-ignore
    this.$parent.learning();
  }

  methodSkipScroll() {
    return;
  }

  pagination(data) {
    this.page = data;
    this.getApi();
  }

  getApi() {
    this.isLoading = true;
    const connectomeId = JSON.parse(localStorage.getItem('ds-connectome')).connectomeId;
    axios
      .get(`/api/personal-documents/getListDocuments/${connectomeId}`, {
        params: {
          page: this.page - 1,
          size: this.size,
          uploadType: FILE_TYPE_UPLOAD.WEB,
          isDelete: 0,
        },
      })
      .then(res => {
        const data = res.data;
        this.allDataWeb = data.results;
        this.totalPosts = data.totalItems;
        this.totalPage = data.totalPages;
        // if (this.totalPosts % 20 > 0) {
        //   this.totalPage = (this.totalPosts - (this.totalPosts % 20)) / 20 + 1;
        // } else {
        //   this.totalPage = this.totalPosts / 20;
        // }
        this.isLoading = false;
      })
      .catch(error => {
        this.isLoading = false;
      });
  }

  getDateTimeUTC() {
    const date = new Date();
    return (
      date.getUTCFullYear() +
      '-' +
      (date.getUTCMonth().valueOf() + 1) +
      '-' +
      date.getUTCDate() +
      ' ' +
      date.getUTCHours() +
      ':' +
      date.getUTCMinutes()
    );
  }

  public changeDisplayMode(mode) {
    if (this.displayModeSync === mode) {
      return;
    }
    this.displayModeSync = mode;
  }

  deleteFile(item: any) {
    this.$root.$emit('deleteFile');
    if (item.id) {
      axios
        .delete('api/file-storage/deleteData/' + item.id)
        .then(res => {
          this.getApi();
        })
        .catch(error => {});
    }
  }

  deleteAll() {
    const cardId = [];
    this.cardSelectedToDelete.forEach(index => {
      cardId.push(this.allDataWeb[index].id);
    });
    const connectome = JSON.parse(localStorage.getItem('ds-connectome'));
    if (connectome) {
      axios
        .post('api/personal-documents/deleteDocuments/' + connectome.connectomeId, cardId)
        .then(res => {
          this.cardSelectedToDelete = [];
          this.isSelectAllLearningComplete = false;
          // this.getApi();
          for (let i = 0; i < this.allDataWeb.length; i++) {
            if (cardId.includes(this.allDataWeb[i].id)) {
              this.allDataWeb.splice(i, 1);
              i--;
            }
          }
        })
        .catch(error => {});
    }
  }

  imgSource(item: any) {
    return item.ogImageUrl || item.ogImageBase64 || (item.imageUrl && item.imageUrl[0]) || item.imageBase64 || '';
  }

  selectCardToDelete(item, index) {
    const num = this.cardSelectedToDelete.indexOf(index);
    if (num > -1) {
      this.cardSelectedToDelete.splice(num, 1);
    } else {
      this.cardSelectedToDelete.push(index);
    }
  }

  selectAll() {
    this.isSelectAll = !this.isSelectAll;
    if (this.isSelectAll) {
      this.previewModels.forEach((item, num) => {
        if (this.cardSelected.indexOf(num) === -1) {
          this.cardSelected.push(num);
          let check = false;
          this.postSelectedWeb.forEach(value => {
            if (value.url === item.url) {
              check = true;
            }
          });
          if (!check) {
            this.postSelectedWeb.push(this.previewModels[num]);
          }
        }
      });
    } else {
      this.cardSelected = [];
      for (let i = 0; i < this.previewModels.length; i++) {
        for (let j = 0; j < this.postSelectedWeb.length; j++) {
          if (
            this.previewModels[i].url === this.postSelectedWeb[j].url &&
            this.previewModels[i].keyword === this.postSelectedWeb[j].keyword
          ) {
            this.postSelectedWeb.splice(j, 1);
            j--;
          }
        }
      }
    }
    this.$emit('handleSelectedWeb', this.postSelectedWeb);
  }
}
