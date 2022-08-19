import { Prop, Vue } from 'vue-property-decorator';
import Component from 'vue-class-component';
import axios from 'axios';
import ScrollLoader from 'vue-scroll-loader/dist/scroll-loader.umd.min';
import QrCode from '@/qrCode/qrCode.vue';

Vue.use(ScrollLoader);

@Component({
  components: {
    qrCode: QrCode,
  },
  computed: {
    qrCode() {
      return this.$store.getters.qrcode;
    },
  },
  watch: {
    qrCode(newValue, oldValue) {
      if (newValue == true) {
        this.$bvModal.show('qrModal');
      }
    },
  },
})
export default class search extends Vue {
  @Prop(Number) readonly display;
  @Prop(String) keywords: string | '';
  isIconSearch = true;
  selected = false;
  dataSearch = [];
  internalSearch = [];
  metaSearch = [];
  externalSearch = [];
  allData = [];
  dataItemSearch = [];
  total = 0;
  queries = '';
  textSearch = '';
  lengthData = 0;
  textHistory = '';
  isShow = false;
  isShowSpinner = false;
  timeShowSpinner = 0;
  loaderDisable = true;
  isAddCondition = true;
  isScrollDown = false;
  homeInterface = true;
  isSelected = [];

  mounted() {
    this.queries = this.keywords;
    if (this.queries != '') {
      this.searching();
    }
  }

  handleClickIconInSearch(e) {
    e.preventDefault();
    if ($('.favorite-list').css('display') == 'none') {
      $('.favorite-list').css('display', 'inline');
    }
  }

  created() {
    this.$parent.$on('uncheckedCard', this.uncheckedCard);
  }

  // select card
  selectCard(index) {
    if (this.allData[index].metasearch) {
      if (!this.allData[index].org_date) {
        this.allData[index].org_date = this.getDateTimeUTC();
      }
    }
    let selected = false;
    this.isSelected.forEach((item, num) => {
      if (index == item) {
        selected = true;
      }
    });
    if (selected) {
      this.$emit('removeItemSearch', this.allData[index]);
    } else {
      this.isSelected.push(index);
      this.$emit('showSelectedSearch', this.allData[index]);
    }
  }

  uncheckedCard(item) {
    if (item.metasearch) {
      for (let i = 0; i < this.allData.length; i++) {
        if (item.link == this.allData[i].link) {
          const index = this.allData.indexOf(item);
          const num = this.isSelected.indexOf(index);
          this.isSelected.splice(num, 1);
        }
      }
    } else {
      for (let i = 0; i < this.allData.length; i++) {
        if (item.id == this.allData[i].id) {
          const index = this.allData.indexOf(item);
          const num = this.isSelected.indexOf(index);
          this.isSelected.splice(num, 1);
        }
      }
    }
  }

  async searching() {
    this.timeShowSpinner++;
    if (this.timeShowSpinner <= 1) {
      this.isShowSpinner = true;
      this.textSearch = this.queries;
    }
    if (this.queries != this.textSearch) {
      this.isSelected = [];
      this.timeShowSpinner = 1;
      this.isShowSpinner = true;
      this.dataSearch = [];
      this.textSearch = this.queries;
      this.allData = [];
    }
    if (this.dataSearch.length > 0 && this.total < 10 && this.getParam() == null) {
      this.loaderDisable = true;
    } else {
      await axios
        .get('api/learning/search' + this.getParam() + '/' + this.timeShowSpinner + '/' + this.queries)
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
            // @ts-ignore
            // this.dataTransfer.scrollId = this.internalSearch.scroll_id;
            // @ts-ignore
            this.total = this.internalSearch.total;
          }
          this.lengthData = this.allData.length - this.metaSearch.length;
          // disable loader in metaSearch
          this.loaderDisable = this.metaSearch.length < 10;
        })
        .catch(error => {
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

  addCondition() {
    const connectome = JSON.parse(localStorage.getItem('ds-connectome'));
    const webSourceCondition = {
      condition: this.queries,
      category: 'AI_STARTUP, FACEBOOK, PROFILE',
      webSourceName: connectome.connectomeName + '-' + this.queries,
      connectomeId: connectome.connectomeId,
      connectomeName: connectome.connectomeName,
    };
    axios
      .post('api/websource/add-condition', webSourceCondition)
      // .then(res => {
      //   console.log('res add condition = ', res.data);
      // })
      .catch(err => {
        console.log('add condition error!');
      });
  }

  getTime(time) {
    const date = time.slice(0, 10);
    const hour = time.slice(11, 16);
    return date + ' ' + hour;
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

  scrollLoader() {
    // $('html').addClass('is-scrolled');
    this.isAddCondition = false;
    this.searching();
    if (this.total < 10) {
      this.loaderDisable = true;
    }
  }

  getEmbedLink(url) {
    const id = url.split('?v=')[1];
    return 'https://www.youtube.com/embed/' + id;
  }

  methodSkipScroll() {
    return;
  }

  close() {
    this.$bvModal.hide('qrModal');
    this.$store.commit('setQRcode', false);
  }

  goFeed() {
    this.$router.push({ path: '/feed', query: { isLearning: 'true' } });
    this.$root.$emit('bv::hide::modal', 'learning-center');
  }

  displayCard() {
    this.homeInterface = !this.homeInterface;
  }
}
