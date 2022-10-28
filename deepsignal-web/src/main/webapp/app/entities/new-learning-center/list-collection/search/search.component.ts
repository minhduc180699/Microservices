import { Component, Prop, Vue, Watch } from 'vue-property-decorator';
import vueCustomScrollbar from 'vue-custom-scrollbar';
import axios from 'axios';
import singleCard from '@/entities/new-learning-center/aside/single-card/single-card.vue';
import { documentCard } from '@/shared/model/document-card.model';

@Component({
  components: {
    vueCustomScrollbar: vueCustomScrollbar,
    'single-card': singleCard,
  },
})
export default class Search extends Vue {
  @Prop(Boolean) readonly fullScreenMode: false | any;
  scrollSettings = {
    wheelPropagation: false,
  };
  queries = '';
  timeShowSpinner = 0;
  textSearch = '';
  isShowSpinner = false;
  dataSearch = [];
  cardSelected = [];
  allData = [];
  total = 0;
  isSelectAll = false;
  tabTypeActive = '';
  loaderDisable = true;
  internalSearch = [];
  metaSearch = [];
  externalSearch = [];
  isShowMore = false;
  isMoreResults = false;
  lengthData = 0;
  isAddCondition = true;
  tabs = [
    { name: 'All', searchType: '', value: 1 },
    { name: 'News', searchType: 'searchNews', value: 2 },
    { name: 'Videos', searchType: 'searchVideo', value: 3 },
    { name: 'Documents', searchType: 'searchFileType', value: 4 },
  ];
  tabActive = 1;

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
          this.allData = this.allData.map(item => {
            const objectTmp = new documentCard();
            objectTmp.author = item.author;
            objectTmp.title = item.title;
            objectTmp.content = item.description;
            objectTmp.keyword = item.keyword;
            objectTmp.type = item.searchType;
            objectTmp.addedAt = item.org_date;
            objectTmp.url = item.link;
            objectTmp.images = [item.img];
            objectTmp.favicon = item.favicon;
            return objectTmp;
          });
          console.log(this.allData);

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

  showTag(e) {
    e.preventDefault();
    $('.tag-box').toggleClass('show');
  }

  totalSelected = 0;
  selectedItems = [];
  checked = false;

  selectAll() {
    const arrTmp2 = this.checkArraySelected();
    if (arrTmp2.length <= this.allData.length && arrTmp2.length != 0) {
      this.selectedItems = [...this.selectedItems, ...arrTmp2];
    } else if (arrTmp2.length == 0) {
      for (let i = 0; i < this.allData.length; i++) {
        let j = 0;
        while (j < this.selectedItems.length) {
          if (this.allData[i].url == this.selectedItems[j].url) {
            this.selectedItems.splice(j, 1);
            break;
          }
          j++;
        }
      }
    }
  }
  @Watch('selectedItems')
  watchSelectedItems() {
    const arrTmp = this.checkArraySelected();
    this.totalSelected = this.allData.length - arrTmp.length;
    // this.$emit('handleSelectedSearch', this.selectedItems);
  }

  @Watch('allData')
  dataChange() {
    const arrItem = [];
    for (const item of this.selectedItems) {
      let i = 0;
      while (i < this.allData.length) {
        if (this.allData[i].url == item.url) {
          arrItem.push(this.allData[i]);
          break;
        }
        i++;
      }
    }

    arrItem.length === this.allData.length ? $('#btnSelect').addClass('active') : $('#btnSelect').removeClass('active');
    this.totalSelected = arrItem.length;
  }

  checkArraySelected(arg?) {
    function onlyInLeft(leftValue, rightValue) {
      const res = [];
      for (let i = 0; i < leftValue.length; i++) {
        let j = 0;
        let isSame = false;
        while (j < rightValue.length) {
          if (rightValue[j].url == leftValue[i].url) {
            isSame = true;
            break;
          }
          j++;
        }
        if (!isSame) res.push(leftValue[i]);
      }
      return res;
    }
    if (arg) return onlyInLeft(this.selectedItems, this.allData);
    return onlyInLeft(this.allData, this.selectedItems);
  }

  setSelectedItems(newData) {
    this.selectedItems = newData;
    const arrTmp = this.checkArraySelected();
    this.totalSelected = this.allData.length - arrTmp.length;
    // this.$emit('handleSelectedSearch', this.selectedItems);
    if (this.totalSelected > 0 && arrTmp.length === 0) $('#btnSelect').addClass('active');
    else $('#btnSelect').removeClass('active');
  }

  insertToMemory() {
    this.$root.$emit('cart-to-conlection', this.selectedItems, 'search');
  }
}
