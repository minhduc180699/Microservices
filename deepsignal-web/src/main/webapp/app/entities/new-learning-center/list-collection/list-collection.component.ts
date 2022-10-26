import { Component, Vue, Watch } from 'vue-property-decorator';
import vueCustomScrollbar from 'vue-custom-scrollbar';
import axios from 'axios';
import { documentCard } from '@/shared/model/document-card.model';
import { CmCollectionsItem } from '@/shared/model/cm-collections-item.model';
import { namespace } from 'vuex-class';
import groupCard from '@/entities/new-learning-center/aside/group-card/group-card.vue';
import singleCard from '@/entities/new-learning-center/aside/single-card/single-card.vue';
import { CmCollection } from '@/shared/model/cm-collection.model';

import search from '@/entities/new-learning-center/list-collection/search/search.vue';
import web from '@/entities/new-learning-center/list-collection/web/web.vue';
import dsText from '@/entities/new-learning-center/list-collection/text/dsText.vue';
import document from '@/entities/new-learning-center/list-collection/document/document.vue';

const collectionsManagerStore = namespace('collectionsManagerStore');

@Component({
  components: {
    vueCustomScrollbar: vueCustomScrollbar,
    'group-card': groupCard,
    'single-card': singleCard,
    search: search,
    web: web,
    document: document,
    dsText: dsText,
  },
  computed: {
    currentCollection() {
      return this.$store.getters['collectionsManagerStore/getCurrentCollection'];
    },
  },
})
export default class ListCollection extends Vue {
  displayMode = 'grid';
  currentTab = { name: 'Web Search', component: 'search' };
  tabs = [
    { name: 'Web Search', component: 'search', active: true },
    { name: 'Text', component: 'dsText', active: false },
    { name: 'URL', component: 'web', active: false },
    { name: 'Documents', component: 'document', active: false },
  ];

  changeTab(tab, e) {
    e.preventDefault();
    this.tabs.forEach(t => {
      t.active = false;
      if (tab.name == t.name) {
        this.currentTab.component = tab.component;
        this.currentTab.name = tab.name;
        t.active = true;
      }
    });
  }

  handleSelectedWeb(data) {}
  selectedSearch = [];

  handleSelectedSearch(data) {
    this.selectedSearch = data;
    console.log('this.selectedSearch', this.selectedSearch);
  }
  handleSelectedDocument(data) {}

  insertToMemory() {
    this.$root.$emit('cart-to-conlection', this.selectedSearch);
  }

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

  scrollSettings = {
    wheelPropagation: false,
  };
  isGroupCollectionActive = false;
  isAddCollectionActive = false;
  isShowAllTag = false;

  page = 0;
  size = 20;
  labelSave = 'Save';
  chosenCollection: any = {};
  isSelected = false;
  currentCollection: any;

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
