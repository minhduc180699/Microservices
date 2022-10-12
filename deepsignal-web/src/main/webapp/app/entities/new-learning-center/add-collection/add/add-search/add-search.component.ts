import { Component, Prop, Vue } from 'vue-property-decorator';
import vueCustomScrollbar from 'vue-custom-scrollbar';
import axios from 'axios';

@Component({
  components: {
    vueCustomScrollbar: vueCustomScrollbar,
  },
})
export default class addSearch extends Vue {
  @Prop(Boolean) readonly fullScreenMode: false | any;
  private scrollSettings = {
    wheelPropagation: false,
  };
  private queries = '';
  private timeShowSpinner = 0;
  private textSearch = '';
  private isShowSpinner = false;
  private isSelected = [];
  private dataSearch = [];
  private cardSelected = [];
  private allData = [];
  private total = 0;
  private isSelectAll = false;
  private tabTypeActive = '';
  private loaderDisable = true;
  private internalSearch = [];
  private metaSearch = [];
  private externalSearch = [];
  private isShowMore = false;
  private isMoreResults = false;
  private lengthData = 0;
  private isAddCondition = true;
  private tabs = [
    { name: 'All', searchType: '', value: 1 },
    { name: 'News', searchType: 'searchNews', value: 2 },
    { name: 'Videos', searchType: 'searchVideo', value: 3 },
    { name: 'Documents', searchType: 'searchFileType', value: 4 },
  ];
  private tabActive = 1;

  onEnterSearch() {
    if (this.timeShowSpinner >= 1 && this.queries == this.textSearch) {
      return;
    }
    this.searching();
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

  activeTab(value, searchType) {
    this.tabActive = value;
    this.allData = [];
    this.timeShowSpinner = 0;
    this.tabTypeActive = searchType;
    this.cardSelected = [];
    this.isSelectAll = false;
    this.searching();
  }
}
