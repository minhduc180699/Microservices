import { Component, Vue, Watch } from 'vue-property-decorator';
import { namespace } from 'vuex-class';
import { ContextualMemoryCollection } from '@/shared/model/contextual-memory-collection.model';
import { CmCollectionsItem } from '@/shared/model/cm-collections-item.model';
import { CmCollection } from '@/shared/model/cm-collection.model';
import { documentCard } from '@/shared/model/document-card.model';
import axios from 'axios';
import { ShowMoreMixin } from '@/mixins/show-more';
import { mixins } from 'vue-class-component';
import { ConnectomeNode } from '@/shared/model/connectome-node.model';

const collectionsManagerStore = namespace('collectionsManagerStore');

@Component({
  components: {},
})
export default class BuilderMap extends Vue {
  @collectionsManagerStore.Getter
  public isCollectionsChanged!: number;

  @collectionsManagerStore.Getter
  public isCurrentCollectionChanged!: number;

  @collectionsManagerStore.Action
  public getAllCollections!: () => Promise<{ status: string; message: string; result: Array<CmCollectionsItem> }>;

  @collectionsManagerStore.Action
  public getCollectionsFromDocIds: (payload: {
    docIds: Array<string>;
  }) => Promise<{ status: string; message: string; result: Array<CmCollectionsItem> }>;

  @collectionsManagerStore.Action
  public getCurrentDraftCollection: () => Promise<{ status: string; message: string; result: CmCollection }>;

  @Watch('$route', { immediate: true, deep: true })
  onUrlChange(newVal: any) {
    console.log(newVal);

    if (newVal.name.localeCompare('Builder') == 0) {
      document.body.setAttribute('data-menu', 'collection-builder');
    }
  }

  mounted() {
    this.getPDApi();
    this.onCurrentCollectionDataChange(0);
  }

  //Observer Collections changed
  @Watch('isCollectionsChanged')
  onCollectionsDataChange(newVal: number) {
    console.log('onCollectionsDataChange', newVal);
    this.getAllCollections().then(res => {
      console.log('getAllCollections', res);
    });

    const listDocIds = new Array<string>();
    this.bookmarkCardItems.forEach(card => {
      listDocIds.push(card.id);
    });
    this.collectionCardItems = new Array<documentCard>();
    this.getCollectionsFromDocIds({ docIds: listDocIds }).then(res => {
      console.log('getCollectionsFromDocIds', res);
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

        const indexcard = this.collectionCardItems.find(x => x.id == collection.collectionId);
        if (!indexcard) {
          this.collectionCardItems.push(card);
        }
      });

      if (this.getCurrentCollection && this.getCurrentCollection.collectionId) {
        this.collectionCardItems.forEach(collection => {
          if (collection.id === this.getCurrentCollection.collectionId) {
            collection.style = 'border-color:red; border-width:thick';
          } else {
            collection.style = '';
          }
        });
      }
    });
  }

  @Watch('isCurrentCollectionChanged')
  onCurrentCollectionDataChange(newVal: number) {
    console.log('onCurrentCollectionDataChange', newVal);
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
        this.lblCurrentCollectionId = 'New Collection';
      } else {
        this.labelSave = 'Update';
        this.lblCurrentCollectionId = res.result.collectionId;
      }

      this.loadDocumentsFromCollection({ docIds: res.result.documentIdList }).then(res => {
        console.log('loadDocumentsFromCollection', res);
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
          card.content = item.content;
          card.keyword = item.keyword;
          card.addedAt = new Date(item.publishedAt);
          card.modifiedAt = new Date(item.publishedAt);
          console.log(' inside getDocumentsSelected');
          card.isAdded = true;
          card.style = 'border-color:' + this.getDocumentColors.get(card.id) + '; border-width:thick';
          this.currentCollectiontCardItems.push(card);
        });
      });

      if (this.bookmarkCardItems && this.bookmarkCardItems.length > 0) {
        this.bookmarkCardItems.forEach(bookmark => {
          if (res.result.documentIdList.includes(bookmark.id)) {
            bookmark.style = 'border-color:lime; border-width:thick';
          } else {
            bookmark.style = '';
          }
        });
      }

      if (this.collectionCardItems && this.collectionCardItems.length > 0) {
        this.collectionCardItems.forEach(collection => {
          if (res.result.collectionId && collection.id === res.result.collectionId) {
            collection.style = 'border-color:red; border-width:thick';
          } else {
            collection.style = '';
          }
        });
      }
    });
  }

  //collection list
  collectionCardItems: Array<documentCard> = new Array<documentCard>();

  @collectionsManagerStore.Action
  public getCollectionDetails: (payload: { collectionId: string }) => Promise<{ status: string; message: string; result: CmCollection }>;

  @collectionsManagerStore.Action
  public editCollection: (payload: { collectionId: string }) => Promise<{ status: string; message: string; result: CmCollection }>;

  onBtnAddCollectionToCurrentCollectionClick(card: documentCard) {
    console.log('onBtnAddCollectionToCurrentCollectionClick', card);
    if (!card) return;

    if (!card.id) return;
    this.getCollectionDetails({ collectionId: card.id }).then(res => {
      console.log('getCollectionDetails', res);
      if (!res) {
        return;
      }

      if (res.status === 'NOK') {
        return;
      }

      if (!res.result) {
        return;
      }

      if (!res.result.documentIdList) {
        return;
      }

      if (res.result.documentIdList.length == 0) {
        return;
      }

      this.addBookmarksToCurrentCollection({ docIds: res.result.documentIdList }).then(res => {
        console.log('addBookmarksToCurrentCollection', res);
        if (!res) {
          return;
        }

        if (res.status === 'NOK') {
          return;
        }

        if (!res.result) {
          return;
        }
      });
    });
  }

  onBtnEditCollectionToCurrentCollectionClick(card: documentCard) {
    console.log('onBtnAddCollectionToCurrentCollectionClick', card);
    if (!card) return;

    if (!card.id) return;
    this.editCollection({ collectionId: card.id }).then(res => {
      console.log('editCollection', res);
      if (!res) {
        return;
      }

      if (res.status === 'NOK') {
        return;
      }

      if (!res.result) {
        return;
      }

      if (!res.result.documentIdList) {
        return;
      }

      if (res.result.documentIdList.length == 0) {
        return;
      }
    });
  }

  //bookmark list
  bookmarkCardItems: Array<documentCard> = new Array<documentCard>();
  private page = 0;
  private size = 20;
  private totalFeeds = 0;
  private sortDirection = 'desc';
  private loaderDisable = false;
  private listFilter = [{ field: 'liked', value: 1 }];
  private totalItems = 0;

  @collectionsManagerStore.Action
  public addBookmarksToCurrentCollection: (payload: {
    docIds: Array<string>;
  }) => Promise<{ status: string; message: string; result: CmCollection }>;

  public getPDApi() {
    console.log('getPDApi');
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
        res.data.results.length < this.size ? (this.loaderDisable = true) : (this.page += 1);
        if (this.totalItems != res.data.totalItems) {
          this.totalFeeds = res.data.totalItems;
        }
        res.data.results.forEach(item => {
          if (!this.bookmarkCardItems) {
            this.bookmarkCardItems = new Array<documentCard>();
          }
          const card = new documentCard();

          card.id = item.docId;
          card.author = item.author;
          card.type = item.type;
          card.title = item.title;
          card.content = item.contentSummary ? '[Summary]' + item.contentSummary : '[Content]' + this.getShortContent(item.content);
          card.keyword = item.keyword;
          card.group.push(item.keyword);
          card.addedAt = new Date(item.publishedAt);
          card.modifiedAt = new Date(item.publishedAt);

          this.bookmarkCardItems.push(card);

          this.getCollectionsFromDocIds({ docIds: [item.docId] }).then(res => {
            console.log('getCollectionsFromDocIds', res);
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

              const indexcard = this.collectionCardItems.find(x => x.id == collection.collectionId);
              if (!indexcard) {
                this.collectionCardItems.push(card);
              }
            });
          });
        });
        console.log(res);
      })
      .catch(reason => {
        console.log('catch get mini connectome', reason);
      });
  }
  public onMoreBookmark(event) {
    console.log(event);
    this.getPDApi();
  }

  onBtnAddBookmarkToCurrentCollectionClick(card: documentCard) {
    console.log('onBtnAddBookmarkToCurrentCollectionClick', card);

    if (!card) return;

    if (!card.id) return;

    this.addBookmarksToCurrentCollection({ docIds: [card.id] }).then(res => {
      console.log('addBookmarksToCurrentCollection', res);
      if (!res) {
        return;
      }

      if (res.status === 'NOK') {
        return;
      }

      if (!res.result) {
        return;
      }
    });
  }

  //bookmark inside current Collection
  currentCollectiontCardItems: Array<documentCard> = new Array<documentCard>();

  labelSave = 'Save';
  lblCurrentCollectionId = '';
  @collectionsManagerStore.Action
  public removeBookmarksFromCurrentCollection: (payload: {
    docIds: Array<string>;
  }) => Promise<{ status: string; message: string; result: CmCollection }>;

  @collectionsManagerStore.Action
  public loadDocumentsFromCollection: (payload: { docIds: Array<string> }) => Promise<any>;

  @collectionsManagerStore.Action
  public saveCurrentDraftCollection: () => Promise<{ status: string; message: string; result: any }>;

  @collectionsManagerStore.Action
  public closeCurrentDraftCollection: () => Promise<{ status: string; message: string; result: any }>;

  onBtnRemoveBookmarkFromCurrentCollectionClick(card: documentCard) {
    console.log('onBtnRemoveBookmarkFromCurrentCollectionClick', card);

    if (!card) return;

    if (!card.id) return;

    this.removeBookmarksFromCurrentCollection({ docIds: [card.id] }).then(res => {
      console.log('removeBookmarksFromCurrentCollection', res);
      if (!res) {
        return;
      }

      if (res.status === 'NOK') {
        return;
      }

      if (!res.result) {
        return;
      }
    });
  }

  public onResetCurrentCollection(event) {
    this.closeCurrentDraftCollection().then(res => {
      console.log('closeCurrentDraftCollection', res);
      if (!res) {
        return;
      }

      if (res.status === 'NOK') {
        return;
      }

      if (!res.result) {
        return;
      }
    });
  }

  public onCommitCurrentCollection(event) {
    this.saveCurrentDraftCollection().then(res => {
      console.log('saveCurrentDraftCollection', res);
      if (!res) {
        return;
      }

      if (res.status === 'NOK') {
        return;
      }

      if (!res.result) {
        return;
      }
    });
  }

  //current connectome
  @collectionsManagerStore.Getter
  public getNodes!: Array<ConnectomeNode>;

  @collectionsManagerStore.Getter
  public isCurrentConnectomeChanged!: number;

  @collectionsManagerStore.Getter
  public getDocumentColors!: Map<string, string>;

  localNodes: Array<ConnectomeNode> = [];

  @Watch('isCurrentConnectomeChanged')
  onCurrentConnectomeChanged(newval: number) {
    console.log('connectomeUpdated');
    this.localNodes = this.getNodes.map(x => x);
  }

  //requestlist
  @collectionsManagerStore.Getter
  public getCurrentCollection!: CmCollection;

  @collectionsManagerStore.Action
  public loadCollectionRequest!: (payload: { collectionId: string }) => Promise<any>;

  onSearchRequestList() {
    if (!this.getCurrentCollection || !this.getCurrentCollection.collectionId) {
      console.log('this.getCurrentCollection not defined', this.getCurrentCollection);
      return;
    }
    this.requestList = new Array<string>();
    this.loadCollectionRequest({ collectionId: this.getCurrentCollection.collectionId }).then(res => {
      this.requestList = new Array<string>();
      if (!res) return;
      res.forEach(query => {
        this.requestList.push('  [' + query.keywordPattern + ']  ');
      });
    });
  }
  requestList = new Array<string>();
  discoveryCardItems: Array<documentCard> = new Array<documentCard>();

  convertGMTToLocalTime(dateToConvert: string) {
    if (!dateToConvert) {
      return '';
    }

    const date = new Date(dateToConvert);

    return date.toLocaleString();
  }

  getShortContent(content: string) {
    if (!content) {
      return ' ';
    }

    if (content.length < 256) {
      return content;
    }

    return content.substring(0, 255) + '...';
  }
}
