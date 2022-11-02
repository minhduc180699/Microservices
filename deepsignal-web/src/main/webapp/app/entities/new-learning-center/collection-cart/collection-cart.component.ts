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
    let trainedDocumentIds = [];
    const arrReq = [];
    let connectomeId;
    if (localStorage.getItem('ds-connectome')) {
      connectomeId = JSON.parse(localStorage.getItem('ds-connectome')).connectomeId;
    }
    if (connectomeId && this.newCollection && this.newCollection.length > 0) {
      console.log('this.newCollection2', this.newCollection);
      this.newCollection.forEach(element => {
        if (element?.type) {
          const objectTmp = new ReqestModel();
          objectTmp.id = Date.now();
          objectTmp.title = element.title;
          objectTmp.path = element.url;
          objectTmp.content = element.content;
          objectTmp.type = element.type;
          objectTmp.connectomeId = connectomeId;
          objectTmp.author = element.author;
          objectTmp.searchType = element.searchType;
          objectTmp.favicon = element.favicon;
          objectTmp.lang = this.$store.getters.currentLanguage;
          objectTmp.keyword = element.keyword;
          objectTmp.originDate = element.addedAt;
          objectTmp.docId = element.docId;
          objectTmp.img = element?.images?.[0] ? element.images[0] : '';
          arrReq.push(objectTmp);
        }
      });

      if (arrReq.length > 0) {
        axios.post('/api/personal-documents/urlconvert', arrReq).then(res => {
          console.log('res', res);
          if (res.data?.data?.[0]) {
            trainedDocumentIds = this.newCollection.filter(item => !item.type).map(x => x.id);
            this.addBookmarksToCurrentCollection({ docIds: [...res.data.data, ...trainedDocumentIds] }).then(res => {
              console.log('addBookmarksToCurrentCollection', res);
              if (!res || res.status === 'NOK' || !res.result) {
                return;
              }

              this.doSaveCurrentDraftCollection();
            });
          }
        });
      } else {
        const docIds = this.newCollection.map(item => item.id);
        this.addBookmarksToCurrentCollection({ docIds: docIds }).then(res => {
          console.log('addBookmarksToCurrentCollection', res);
          if (!res || res.status === 'NOK' || !res.result) {
            return;
          }

          this.doSaveCurrentDraftCollection();
        });
      }
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

  setCollection(arrDoc, type, remove?) {
    this.isChild = true;
    for (let i = 0; i < this.arrCollection.length; i++) {
      if (this.arrCollection[i].type === type) {
        if (type === 'text' && !remove) this.arrCollection[i].arr = this.arrCollection[i].arr.concat([...arrDoc]);
        else this.arrCollection[i].arr = [...arrDoc];
        break;
      }
    }

    this.newCollection = this.newCollection.splice(0, 0);
    this.arrCollection.forEach(item => {
      this.newCollection = this.newCollection.concat(item.arr);
    });

    if (this.newCollection.length > 0) {
      this.isCartActive = true;
    }
  }

  removeCardReq(arr, item) {
    let type;
    switch (item.searchType) {
      case 'WEB': {
        type = 'web';
        break;
      }
      case 'USERNOTE': {
        type = 'text';
        break;
      }
      default: {
        type = 'search';
        break;
      }
    }
    const arrTmp = this.arrCollection.find(item => item.type === type).arr;

    const index = arrTmp.indexOf(item);
    if (index !== -1) arrTmp.splice(index, 1);
    this.setCollection(arrTmp, type, true);
  }

  doSaveCurrentDraftCollection() {
    this.saveCurrentDraftCollection().then(res => {
      console.log('res', res);
      if (!res || res.status === 'NOK' || !res.result) {
        return;
      }
      // @ts-ignore
      this.$router.go();
    });
  }
}
