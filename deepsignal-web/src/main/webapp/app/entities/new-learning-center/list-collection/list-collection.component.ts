import { Component, Vue, Watch } from 'vue-property-decorator';
import vueCustomScrollbar from 'vue-custom-scrollbar';
import axios from 'axios';
import { documentCard } from '@/shared/model/document-card.model';
import { CmCollectionsItem } from '@/shared/model/cm-collections-item.model';
import { namespace } from 'vuex-class';
import groupCard from '@/entities/new-learning-center/aside/group-card/group-card.vue';
import singleCard from '@/entities/new-learning-center/aside/single-card/single-card.vue';
import { CmCollection } from '@/shared/model/cm-collection.model';
const collectionsManagerStore = namespace('collectionsManagerStore');

@Component({
  components: {
    vueCustomScrollbar: vueCustomScrollbar,
    'group-card': groupCard,
    'single-card': singleCard,
  },
  computed: {
    currentCollection() {
      return this.$store.getters['collectionsManagerStore/getCurrentCollection'];
    },
  },
})
export default class ListCollection extends Vue {
  @collectionsManagerStore.Action
  public getCollectionsFromDocIds: (payload: {
    docIds: Array<string>;
  }) => Promise<{ status: string; message: string; result: Array<CmCollectionsItem> }>;

  @collectionsManagerStore.Action
  public getCurrentDraftCollection: () => Promise<{ status: string; message: string; result: CmCollection }>;

  @collectionsManagerStore.Action
  public loadDocumentsFromCollection: (payload: { docIds: Array<string> }) => Promise<any>;

  @collectionsManagerStore.Getter
  public getDocumentColors!: Map<string, string>;

  @collectionsManagerStore.Getter
  public isCurrentConnectomeChanged!: number;

  private scrollSettings = {
    wheelPropagation: false,
  };
  private isGroupCollectionActive = false;
  private isAddCollectionActive = false;
  private isShowAllTag = false;

  private page = 0;
  private size = 20;
  labelSave = 'Save';
  private chosenCollection = {};
  private isSelected = false;
  private currentCollection: any;

  //bookmark list
  bookmarkCardItems: Array<documentCard> = new Array<documentCard>();
  //collection list
  collectionCardItems: Array<documentCard> = new Array<documentCard>();
  //bookmark inside current Collection
  currentCollectiontCardItems: Array<documentCard> = new Array<documentCard>();

  mounted(): void {
    this.getPDApi();
    this.onCurrentCollectionDataChange(0);
  }

  getPDApi() {
    const connectomeId = JSON.parse(localStorage.getItem('ds-connectome')).connectomeId;
    axios
      .get(`/api/personal-documents/getListDocuments/${connectomeId}`, {
        params: {
          page: this.page,
          size: this.size,
          uploadType: '',
          isDelete: 0,
        },
      })
      .then(res => {
        res.data.results.forEach(item => {
          if (!this.bookmarkCardItems) {
            this.bookmarkCardItems = new Array<documentCard>();
          }
          const card = new documentCard();

          card.id = item.docId;
          card.author = item.author;
          card.type = item.type;
          card.title = item.title;
          card.content = item.contentSummary ? item.contentSummary : item.content;
          card.keyword = item.keyword;
          card.tags.push(item.keyword);
          card.addedAt = new Date(item.publishedAt);
          card.modifiedAt = new Date(item.publishedAt);
          card.favicon = item.faviconUrl || item.faviconBase64 || '';
          card.images[0] = (item.imageUrl && item.imageUrl[0]) || item.imageBase64 || item.ogImageUrl || item.ogImageBase64 || '';

          this.bookmarkCardItems.push(card);

          this.getCollectionsFromDocIds({ docIds: [item.docId] }).then(res => {
            if (!res) {
              return;
            }

            if (res.status === 'NOK') {
              return;
            }

            if (!res.result) {
              return;
            }

            if (!this.collectionCardItems) {
              this.collectionCardItems = new Array<documentCard>();
            }

            res.result.forEach(collection => {
              const card = new documentCard();
              card.id = collection.collectionId;
              card.author = 'me';
              card.type = 'COLLECTION';
              card.title = collection.collectionId;
              card.content = collection.documentIdList.length + ' documents included';
              card.modifiedAt = collection.modifiedDate;
              card.totalDocuments = collection.documentIdList.length;
              const indexcard = this.collectionCardItems.find(x => x.id == collection.collectionId);
              if (!indexcard) {
                const docsDetail = [];
                collection.documentIdList.forEach(docId => {
                  const item = this.bookmarkCardItems.find(item => item.id == docId);
                  if (item) {
                    // let l = [];
                    docsDetail.push(item);
                  }
                  if (item.images[0]) {
                    card.images.push(item.images[0]);
                  }
                  if (item.tags) {
                    card.tags = item.tags;
                  }
                });
                card['docDetail'] = docsDetail;
                console.log('card1233', card);
                this.collectionCardItems.push(card);
              }
            });
          });
        });
        this.$emit('changeBookmarkItems', this.bookmarkCardItems);
        console.log('bookmarkCardItems', this.bookmarkCardItems);
      })
      .catch(reason => {
        console.log('catch get mini connectome', reason);
      });
  }

  @Watch('isCurrentCollectionChanged')
  onCurrentCollectionDataChange(newVal: number) {
    this.getCurrentDraftCollection().then(res => {
      console.log('getCurrentDraftCollection', res);
      if (!res) {
        return;
      }

      if (res.status === 'NOK') {
        return;
      }

      if (!res.result) {
        return;
      }
      this.currentCollectiontCardItems = [];

      if (!res.result.documentIdList) {
        return;
      }
      if (!res.result.collectionId) {
        this.labelSave = 'Save';
      } else {
        this.labelSave = 'Update';
      }
      this.loadDocumentsFromCollection({ docIds: res.result.documentIdList }).then(res => {
        // console.log('loadDocumentsFromCollection', res);

        if (!res) return;
        res.forEach(item => {
          const idx = this.currentCollectiontCardItems.find(x => x.id === item.docId);
          if (idx) {
            return;
          }
          const card = new documentCard();
          card.id = item.docId;
          card.author = item.author;
          card.type = item.type;
          card.title = item.title;
          card.content = item.contentSummary;
          card.keyword = item.keyword;
          card.addedAt = new Date(item.publishedAt);
          card.modifiedAt = new Date(item.publishedAt);
          card.isAdded = true;
          card.style = 'background-color:' + this.getDocumentColors.get(card.id);
          this.currentCollectiontCardItems.push(card);
        });
      });
    });
  }

  handleGroupCollectionActive(data: any) {
    this.chosenCollection = data;
    this.isGroupCollectionActive = !this.isGroupCollectionActive;
  }

  @Watch('currentCollection')
  currentCollectionChange(item: any) {
    // debugger;
    const docId = this.currentCollection.documentIdList.find(docId => docId == item.id);
    if (docId) {
      this.isSelected = true;
    } else {
      this.isSelected = false;
    }
  }
}
