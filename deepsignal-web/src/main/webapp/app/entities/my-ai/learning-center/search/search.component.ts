import { Component, Inject, Prop, PropSync, Vue, Watch } from 'vue-property-decorator';
import axios from 'axios';
import FeedService from '@/core/home/feed/feed.service';
import pagination from '@/entities/connectome/learning-management/pagination/pagination.vue';
import { FILE_TYPE_UPLOAD } from '@/shared/constants/ds-constants';
import { namespace } from 'vuex-class';
import { ConnectomeNetworkVertex } from '@/shared/model/connectome-network-vertex.model';

const mapNetworkStore = namespace('mapNetworkStore');
const networkStore = namespace('connectomeNetworkStore');
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
export default class search extends Vue {
  @Prop(String) readonly currentTab: string | any;
  @PropSync('displayMode', String) displayModeSync;
  @Inject('feedService') private feedService: () => FeedService;
  private showSearch = false;
  private queries = '';
  private timeShowSpinner = 0;
  private isShowSpinner = false;
  private textSearch = '';
  private isSelected = [];
  private dataSearch = [];
  private allData = [];
  private total = 0;
  private loaderDisable = true;
  private internalSearch = [];
  private metaSearch = [];
  private externalSearch = [];
  private lengthData = 0;
  private isAddCondition = true;
  private cardSelected = [];
  private cardSelectedToDelete = [];
  private postSelectedSearch = [];
  private isLoading = true;
  private allDataSearch = [];
  private page = 1;
  private size = 20;
  private totalPage = 0;
  private totalPosts = 0;
  private isShowMore = false;
  private isMoreResults = false;
  private tabs = [
    { name: 'All', searchType: '', value: 1 },
    { name: 'News', searchType: 'searchNews', value: 2 },
    { name: 'Videos', searchType: 'searchVideo', value: 3 },
    { name: 'Documents', searchType: 'searchFileType', value: 4 },
  ];
  private tabActive = 1;
  private tabTypeActive = '';
  private isSelectAll = false;
  private isSelectAllLearningComplete = false;
  private isLearning = false;

  @networkStore.Getter
  public vertice!: (label: string) => ConnectomeNetworkVertex;

  @mapNetworkStore.Getter
  public entityLabelSelected!: string;

  async created() {
    this.$parent.$on('unSelectedSearch', this.unSelectedSearch);
    this.$root.$on('deleteFile', this.getApi);
    this.$root.$on('learning-keyword', this.learningKeywordFeed);
    this.$parent.$on('unSelectedAllSearch', this.unSelectedAllSearch);
  }

  mounted() {
    if (this.entityLabelSelected) {
      this.onEntityLabelSelectedChange(this.entityLabelSelected);
    }
  }

  @Watch('entityLabelSelected')
  onEntityLabelSelectedChange(entityLabel) {
    if (!this.queries) {
      this.queries = entityLabel;
      if (this.queries) {
        this.onEnterSearch();
      }
    } else {
      const vertex = this.vertice(this.queries);
      if (vertex) {
        this.queries = entityLabel;
        if (this.queries) {
          this.onEnterSearch();
        }
      }
    }
  }

  @Watch('allDataSearch')
  onAllDataSearchChange(newVal, oldVal) {
    return (this.totalPosts = this.allDataSearch.length);
  }

  unSelectedSearch(key) {
    if (key === this.textSearch.toLowerCase()) {
      this.cardSelected = [];
      this.isSelectAll = false;
    }
    for (let i = 0; i < this.postSelectedSearch.length; i++) {
      if (this.postSelectedSearch[i].keyword === key) {
        this.postSelectedSearch.splice(i, 1);
        i--;
      }
    }
  }

  unSelectedAllSearch() {
    this.cardSelected = [];
    this.postSelectedSearch = [];
    this.isSelectAll = false;
  }

  async searching() {
    if (!this.queries && !this.textSearch) {
      return;
    }
    this.timeShowSpinner++;
    if (this.timeShowSpinner <= 1) {
      this.isShowSpinner = true;
      if (this.queries != '') {
        this.textSearch = this.queries;
      } else {
        this.queries = this.textSearch;
      }
    }
    if (this.timeShowSpinner > 1 && this.queries == '') {
      this.queries = this.textSearch;
    }
    if (this.queries != this.textSearch) {
      this.isSelected = [];
      this.timeShowSpinner = 1;
      this.isShowSpinner = true;
      this.dataSearch = [];
      this.textSearch = this.queries;
      this.allData = [];
      this.cardSelected = [];
      this.isSelectAll = false;
    }
    if (this.dataSearch.length > 0 && this.total < 10 && this.getParam() == null) {
      this.loaderDisable = true;
    } else {
      await axios
        .get('api/learning/search' + this.getParam() + '/' + this.timeShowSpinner + '/' + this.queries, {
          params: {
            searchType: this.tabTypeActive,
          },
        })
        .then(res => {
          this.isShowSpinner = false;
          if (res.data.internalSearch) {
            this.internalSearch = res.data.internalSearch.body;
            // @ts-ignore
            this.dataSearch = this.internalSearch.documents;
          }
          if (res.data.metaSearch) {
            this.externalSearch = res.data.metaSearch.body;
            // @ts-ignore
            this.metaSearch = this.externalSearch.data;
            // @ts-ignore
            if (this.externalSearch.total < 1) {
              this.loaderDisable = true;
              this.isMoreResults = false;
              this.isShowMore = false;
              return;
            } else {
              this.isMoreResults = true;
            }
            // @ts-ignore
            if (this.externalSearch.result.includes('NO_MORE_DATA')) {
              this.isMoreResults = false;
              return;
            }
          }
          if (this.allData.length > 0) {
            this.allData = this.allData.concat(this.dataSearch, this.metaSearch);
          } else {
            this.dataSearch.forEach((item, index) => {
              this.allData.push(item);
              this.metaSearch.forEach((val, ind) => {
                if (index == ind) {
                  this.allData.push(val);
                }
              });
            });
            if (this.dataSearch.length < this.metaSearch.length) {
              for (let i = this.dataSearch.length; i < this.metaSearch.length; i++) {
                this.allData.push(this.metaSearch[i]);
              }
            }
          }
          this.lengthData = this.allData.length - this.metaSearch.length;
          this.isShowMore = false;
        })
        .catch(error => {
          this.isShowMore = false;
          const err = error.response.data;
          this.$bvToast.toast(err.message, {
            toaster: 'b-toaster-bottom-right',
            title: err.errorKey,
            variant: 'danger',
            solid: true,
            autoHideDelay: 5000,
          });
        });
    }
    // after search done
    if (this.isAddCondition) {
      // this.addCondition();
    }
  }

  public getParam() {
    const connectome = JSON.parse(localStorage.getItem('ds-connectome'));
    const connectomeId = connectome.connectomeId;
    const language = localStorage.getItem('currentLanguage');
    if (!language || !connectomeId) {
      return null;
    }
    const param = '/' + connectomeId + '/' + language;
    return param;
  }

  public getParamLearning() {
    const connectome = JSON.parse(localStorage.getItem('ds-connectome'));
    const userId = connectome.user.id;
    const connectomeId = connectome.connectomeId;
    if (!connectome || !connectomeId || !userId) {
      return null;
    }

    const param = userId + '/' + connectomeId;
    return param;
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

  selectCard(item, index) {
    const num = this.cardSelected.indexOf(index);
    if (num > -1) {
      this.cardSelected.splice(num, 1);
      this.postSelectedSearch.forEach((value, st) => {
        if (value.__unique__key === item.__unique__key) {
          this.postSelectedSearch.splice(st, 1);
        }
      });
    } else {
      this.cardSelected.push(index);
      let check = false;
      this.postSelectedSearch.forEach((value, st) => {
        if (value.__unique__key === item.__unique__key) {
          check = true;
        }
      });
      if (!check) {
        this.postSelectedSearch.push(item);
      }
    }
    if (this.cardSelected.length == this.allData.length) {
      this.isSelectAll = true;
    }
    if (this.cardSelected.length == 0) {
      this.isSelectAll = false;
    }
    this.$emit('handleSelectedSearch', this.postSelectedSearch);
  }

  selectCardToDelete(item, index) {
    const num = this.cardSelectedToDelete.indexOf(index);
    if (num > -1) {
      this.cardSelectedToDelete.splice(num, 1);
    } else {
      this.cardSelectedToDelete.push(index);
    }
  }

  scrollLoader() {
    this.isAddCondition = false;
    this.searching();
    if (this.total < 10) {
      this.loaderDisable = true;
    }
  }

  showMore() {
    this.isShowMore = true;
    this.searching();
  }

  onEnterSearch() {
    if (this.timeShowSpinner >= 1 && this.queries == this.textSearch) {
      return;
    }
    this.searching();
  }

  methodSkipScroll() {
    return;
  }

  selectAll() {
    this.isSelectAll = !this.isSelectAll;
    if (this.isSelectAll) {
      this.allData.forEach((item, num) => {
        if (this.cardSelected.indexOf(num) === -1) {
          this.cardSelected.push(num);
          let check = false;
          this.postSelectedSearch.forEach(value => {
            if (value.__unique__key === item.__unique__key) {
              check = true;
            }
          });
          if (!check) {
            this.postSelectedSearch.push(this.allData[num]);
          }
        }
      });
    } else {
      this.cardSelected = [];
      for (let i = 0; i < this.allData.length; i++) {
        for (let j = 0; j < this.postSelectedSearch.length; j++) {
          if (
            this.allData[i].__unique__key === this.postSelectedSearch[j].__unique__key &&
            this.allData[i].keyword === this.postSelectedSearch[j].keyword
          ) {
            this.postSelectedSearch.splice(j, 1);
            j--;
          }
        }
      }
    }
    this.$emit('handleSelectedSearch', this.postSelectedSearch);
  }

  selectAllLearningComplete() {
    this.isSelectAllLearningComplete = !this.isSelectAllLearningComplete;
    if (this.isSelectAllLearningComplete) {
      this.allDataSearch.forEach((item, num) => {
        if (this.cardSelectedToDelete.indexOf(num) === -1) {
          this.cardSelectedToDelete.push(num);
        }
      });
    } else {
      this.cardSelectedToDelete = [];
    }
  }

  startLearning() {
    // @ts-ignore
    this.$parent.learning();
  }

  getApi() {
    this.isLoading = true;
    const connectomeId = JSON.parse(localStorage.getItem('ds-connectome')).connectomeId;
    axios
      .get(`/api/personal-documents/getListDocuments/${connectomeId}`, {
        params: {
          page: this.page - 1,
          size: this.size,
          uploadType: FILE_TYPE_UPLOAD.URL,
          isDelete: 0,
        },
      })
      .then(res => {
        const data = res.data;
        this.allDataSearch = data.results;
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

  pagination(data) {
    this.page = data;
    this.getApi();
  }

  public changeDisplayMode(mode) {
    if (this.displayModeSync === mode) {
      return;
    }
    this.displayModeSync = mode;
  }

  deleteFile(item: any) {
    if (item.id) {
      axios
        .delete('api/file-storage/deleteData/' + item.id)
        .then(res => {
          this.$root.$emit('deleteFile');
          this.getApi();
        })
        .catch(error => {});
    }
  }

  deleteAll() {
    const cardId = [];
    this.cardSelectedToDelete.forEach(index => {
      cardId.push(this.allDataSearch[index].id);
    });
    const connectome = JSON.parse(localStorage.getItem('ds-connectome'));
    if (connectome) {
      axios
        .post('api/personal-documents/deleteDocuments/' + connectome.connectomeId, cardId)
        .then(res => {
          // this.$root.$emit('deleteFile');
          this.cardSelectedToDelete = [];
          this.isSelectAllLearningComplete = false;
          // this.getApi();
          for (let i = 0; i < this.allDataSearch.length; i++) {
            if (cardId.includes(this.allDataSearch[i].id)) {
              this.allDataSearch.splice(i, 1);
              i--;
            }
          }
        })
        .catch(error => {});
    }
  }

  activeTab(value, searchType) {
    this.tabActive = value;
    this.allData = [];
    this.timeShowSpinner = 0;
    this.tabTypeActive = searchType;
    this.cardSelected = [];
    this.isSelectAll = false;
    this.searching();
  }

  imgSource(item: any) {
    return item.ogImageUrl || item.ogImageBase64 || (item.imageUrl && item.imageUrl[0]) || item.imageBase64;
  }

  learningKeywordFeed(keyword) {
    this.queries = keyword;
    this.searching();
  }

  destroyed() {
    this.$root.$off('learning-keyword', this.learningKeywordFeed);
  }
}
