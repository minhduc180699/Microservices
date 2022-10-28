import { Component, Prop, Vue, Watch } from 'vue-property-decorator';
import vueCustomScrollbar from 'vue-custom-scrollbar';
import singleCard from '@/entities/new-learning-center/aside/single-card/single-card.vue';
import { namespace } from 'vuex-class';
import { CmCollection } from '@/shared/model/cm-collection.model';
import { ReqestModel } from '@/shared/model/request-savecollection.model';
import axios from 'axios';

const collectionsManagerStore = namespace('collectionsManagerStore');

@Component({
  components: {
    vueCustomScrollbar: vueCustomScrollbar,
    'single-card': singleCard,
  },
})
export default class CollectionCart extends Vue {
  @Prop(Array) readonly bookmarkCardItems: any | [];
  @Prop(Object) readonly currentCollection: any;

  @collectionsManagerStore.Getter
  public getCurrentCollection!: CmCollection;

  @collectionsManagerStore.Action
  public saveCurrentDraftCollection: () => Promise<{ status: string; message: string; result: any }>;
  @collectionsManagerStore.Action
  public addBookmarksToCurrentCollection: (payload: {
    docIds: Array<string>;
  }) => Promise<{ status: string; message: string; result: CmCollection }>;

  private scrollSettings = {
    wheelPropagation: false,
  };

  private isCartActive = false;

  isChild = false;
  newCollection: any = [];

  @Watch('currentCollection')
  setCurrentCollection() {
    // this.isChild = false;
  }

  created() {
    this.$root.$on('cart-to-conlection', this.setCollection);
  }

  destroyed() {
    this.$root.$off('cart-to-conlection', this.setCollection);
  }

  saveCollection() {
    console.log('this.newCollection', this.newCollection);
    const arrReq = [];
    let connectomeId;
    if (localStorage.getItem('ds-connectome')) {
      connectomeId = JSON.parse(localStorage.getItem('ds-connectome')).connectomeId;
    }
    if (connectomeId && this.newCollection && this.newCollection.length > 0) {
      this.newCollection.forEach(element => {
        const objectTmp = new ReqestModel();
        objectTmp.id = Date.now();
        objectTmp.name = element.title;
        objectTmp.path = element.url;
        objectTmp.description = element.content;
        objectTmp.type = element.type;
        objectTmp.connectomeId = connectomeId;
        objectTmp.author = element.author;
        objectTmp.searchType = element.searchType;
        objectTmp.favicon = element.favicon;
        objectTmp.lang = this.$store.getters.currentLanguage;
        objectTmp.keyword = element.keyword;
        objectTmp.originDate = element.type === 'URL' ? '' : element.addedAt;
        arrReq.push(objectTmp);
      });

      axios.post('/api/personal-documents/urlconvert', arrReq).then(res => {
        console.log('res', res);
        if (res.status === 200) {
          this.addBookmarksToCurrentCollection({ docIds: res.data.data }).then(res => {
            console.log('addBookmarksToCurrentCollection', res);
            if (!res || res.status === 'NOK' || !res.result) {
              return;
            }

            this.saveCurrentDraftCollection().then(res => {
              if (!res || res.status === 'NOK' || !res.result) {
                return;
              }
              console.log('res', res);
              this.$router.push('/new-learning-center');
            });
          });
        }
      });
    }
  }

  getDocDetail(docId: any) {
    return this.bookmarkCardItems.find(bookmard => bookmard.id == docId);
  }

  arrCollection = [
    { type: 'search', arr: [] },
    { type: 'text', arr: [] },
    { type: 'web', arr: [] },
    { type: 'doc', arr: [] },
  ];

  setCollection(arrDoc, type) {
    this.isChild = true;
    for (let i = 0; i < this.arrCollection.length; i++) {
      if (this.arrCollection[i].type === type) {
        if (type === 'text') this.arrCollection[i].arr = this.arrCollection[i].arr.concat(arrDoc);
        else this.arrCollection[i].arr = arrDoc;
        break;
      }
    }

    this.newCollection = this.newCollection.splice(0, 0);
    this.arrCollection.forEach(item => {
      this.newCollection = this.newCollection.concat(item.arr);
    });
  }
}
