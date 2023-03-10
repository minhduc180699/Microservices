import { Component, Prop, Vue, Watch } from 'vue-property-decorator';
import vueCustomScrollbar from 'vue-custom-scrollbar';
import axios from 'axios';
import singleCard from '@/entities/new-learning-center/aside/single-card/single-card.vue';
import { documentCard } from '@/shared/model/document-card.model';
import { getDomainFromUrl, onlyInLeft, timeDifference, toDataURL } from '@/util/ds-util';

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
  allData = [];
  tabTypeActive = 'WEB';
  loaderDisable = true;
  internalSearch = [];
  metaSearch = [];
  externalSearch = [];
  isShowMore = false;
  isMoreResults = false;
  lengthData = 0;
  isAddCondition = true;
  tabs = [
    { name: 'All', searchType: 'WEB', value: 1 },
    { name: 'News', searchType: 'NEWS', value: 2 },
    { name: 'Videos', searchType: 'VIDEO', value: 3 },
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
    }
    if (this.dataSearch.length > 0 && this.getParam() == null) {
      this.loaderDisable = true;
    } else {
      const param = new (class {
        [x: string]: any;
      })();
      param.searchType = this.tabTypeActive;
      if (this.tabActive === 1) param.channel = 'google,github,medium';
      await axios
        .get('api/learning/search' + this.getParam() + '/' + this.timeShowSpinner + '/' + this.queries, {
          params: param,
        })
        .then(async res => {
          this.isShowSpinner = false;
          console.log('res', res);
          if (res.data.internalSearch) {
            this.internalSearch = res.data.internalSearch.body;
            // @ts-ignore
            this.dataSearch = this.internalSearch.documents;
          }
          if (res.data.metaSearch) {
            if (this.tabActive === 1) {
              if (res.data.metaSearch.body.data && res.data.metaSearch.body.data.length > 0) {
                const size = res.data.metaSearch.body.data.length;
                const arrChannel = [
                  { key: 'google', value: 'search_result' },
                  { key: 'medium', value: 'stories' },
                  { key: 'github', value: 'repositories' },
                ];
                for (let i = 0; i < size; i++) {
                  const value = arrChannel.find(item => item.key == res.data.metaSearch.body.data[i].channel)?.value;
                  this.metaSearch = this.metaSearch.concat(res.data.metaSearch.body.data[i][value]);
                }
                console.log('this.metaSearch', this.metaSearch);
              }

              if (this.metaSearch.length <= 0) {
                this.isMoreResults = false;
                return;
              }
            } else {
              this.externalSearch = res.data.metaSearch.body;
              // @ts-ignore
              this.metaSearch = this.externalSearch.data;
              // @ts-ignore
              if (this.externalSearch.result.includes('NO_MORE_DATA')) {
                this.isMoreResults = false;
                return;
              }
            }
            this.isMoreResults = true;
          }
          if (this.allData.length > 0) {
            this.allData = this.allData
              .concat(await this.badArrToGoodArr(this.dataSearch, this.tabActive === 4 ? true : false))
              .concat(await this.badArrToGoodArr(this.metaSearch, this.tabActive === 4 ? true : false));
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
            this.allData = await this.badArrToGoodArr(this.allData, this.tabActive === 4 ? true : false);
          }

          this.isShowMore = false;
        })
        .catch(error => {
          console.log(error);
          this.isShowMore = false;
          const err = error;
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
          if (this.allData[i].docId == this.selectedItems[j].docId && this.allData[i].searchType == this.selectedItems[j].searchType) {
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
        if (this.allData[i].docId == item.docId && this.allData[i].searchType == item.searchType) {
          arrItem.push(this.allData[i]);
          break;
        }
        i++;
      }
    }

    arrItem.length === this.allData.length ? $('#btnSelect').addClass('active') : $('#btnSelect').removeClass('active');
    this.totalSelected = arrItem.length;

    console.log(this.selectedItems);
  }

  checkArraySelected(arg?) {
    if (arg) return onlyInLeft(this.selectedItems, this.allData, ['docId', 'searchType']);
    return onlyInLeft(this.allData, this.selectedItems, ['docId', 'searchType']);
  }

  setSelectedItems(newData) {
    this.selectedItems = newData;
    const arrTmp = this.checkArraySelected();
    this.totalSelected = this.allData.length - arrTmp.length;
    if (this.totalSelected > 0 && arrTmp.length === 0) $('#btnSelect').addClass('active');
    else $('#btnSelect').removeClass('active');
  }

  insertToMemory() {
    console.log(this.selectedItems);
    this.$root.$emit('cart-to-conlection', this.selectedItems, 'search');
  }

  deleteAll() {
    this.selectedItems = this.selectedItems.splice(0, 0);
    $('#btnSelect').removeClass('active');
  }

  showMore() {
    this.isShowMore = true;
    this.searching();
  }

  async badArrToGoodArr(arr, typeDownload?) {
    const results: documentCard[] = await Promise.all(
      arr.map(async item => {
        await this.test(item);
        const objectTmp = new documentCard();
        if (objectTmp.author) objectTmp.author = item.author;
        else objectTmp.author = getDomainFromUrl(item.link);
        objectTmp.title = item.title;
        objectTmp.content = item.description;
        objectTmp.keyword = item.keyword;
        objectTmp.type = typeDownload ? 'DOWNLOAD' : 'URL';
        objectTmp.searchType = item.searchType;
        objectTmp.addedAt = item.org_date ? item.org_date : item.datetime ? timeDifference(new Date(), new Date(item.datetime)) : '';
        objectTmp.url = item.link;
        objectTmp.images = [item.imgBase64 ? item.imgBase64 : item.img ? item.img : ''];
        objectTmp.favicon = item.favicon;
        objectTmp.docId = item.docId;
        objectTmp.noConvertTime = true;
        objectTmp.tags = item.tags;
        return objectTmp;
      })
    );
    return results;
  }

  async test(item) {
    if (item.favicon && item.favicon.includes('/favicon.ico')) item.favicon = await toDataURL(item.favicon);
    else item.favicon = null;
  }
}
