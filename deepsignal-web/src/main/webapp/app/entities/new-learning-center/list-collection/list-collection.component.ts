import { Component, Inject, Vue, Watch } from 'vue-property-decorator';
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

import { PrincipalService } from '@/service/principal.service';
import { getDomainFromUrl, onlyInLeft } from '@/util/ds-util';
const networkStore = namespace('connectomeNetworkStore');
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
  @Inject('principalService')
  private principalService: () => PrincipalService;

  @networkStore.Getter
  public lang!: string;

  @collectionsManagerStore.Getter
  public isCurrentCollectionChanged!: number;

  displayMode = 'grid';
  currentTab = { name: 'Web Search', component: 'search' };
  tabs = [
    { name: 'Web Search', component: 'search', active: true },
    { name: 'Text', component: 'dsText', active: false },
    { name: 'URL', component: 'web', active: false },
    { name: 'Documents', component: 'document', active: false },
  ];

  //documents selector
  documentOrGroupDocumentsCardItems: Array<documentCard> = new Array<documentCard>();

  selectedItems = [];

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

  @collectionsManagerStore.Getter
  public getCollections!: any;

  @collectionsManagerStore.Action
  public loadUserCollections!: (payload: { connectomeId: string; language: string }) => Promise<any>;

  @collectionsManagerStore.Action
  public addBookmarksToCurrentCollection: (payload: { docIds: Array<string> }) => Promise<any>;

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
  private loaderDisable = false;

  //bookmark list
  bookmarkCardItems: Array<documentCard> = new Array<documentCard>();
  //collection list
  collectionCardItems: Array<documentCard> = new Array<documentCard>();
  //bookmark inside current Collection
  currentCollectiontCardItems: Array<documentCard> = new Array<documentCard>();

  mounted(): void {
    if (this.getCollections.length) {
      this.getPDApi();
    }
    // this.onCurrentCollectionDataChange(0);
  }
  @Watch('getCollections')
  onCollectionsChange() {
    this.getPDApi();
  }

  getPDApi() {
    const connectomeId = this.principalService().getConnectomeInfo().connectomeId;
    axios
      .get(`/api/connectome-feeds/getListFeeds/${connectomeId}`, {
        params: {
          page: this.page,
          size: 30,
          requestId: Date.now().toString(),
          lang: this.lang,
          type: 'PERSONAL_DOCUMENT',
        },
      })
      .then(res => {
        res.data?.body?.connectomeFeeds?.length < this.size ? (this.loaderDisable = true) : (this.page += 1);
        // if (this.totalItems != res.data.totalItems) {
        //   this.totalFeeds = res.data.totalItems;
        // }
        this.getCurrentDraftCollection()
          .then(currentCollectionResult => {
            res.data.body.connectomeFeeds.forEach(item => {
              if (!this.bookmarkCardItems) {
                this.bookmarkCardItems = new Array<documentCard>();
              }
              const card = new documentCard();
              card.id = item.docId;
              card.author = item.writer ? item.writer : getDomainFromUrl(item?.url);
              card.type = item.type;
              card.title = item.title;
              card.url = item.url;
              card.content = item.description ? '[Summary] ' + item.description : '[Content] ' + this.getShortContent(item.description);
              card.keyword = item.keyword;
              if (card.tags.indexOf(item.keyword) > -1) {
                card.tags.push(item.keyword);
              }
              card.addedAt = new Date(item.created_date);
              card.modifiedAt = new Date(item.created_date);
              card.totalDocuments = 1;
              card.favicon = item.favicon_url ? item.favicon_url : item.favicon_base64;
              card.images.push(item.og_image_url ? item.og_image_url : item.og_image_base64);

              this.bookmarkCardItems.push(card);

              this.getCollectionsFromDocIds({ docIds: [item.docId] })
                .then(res => {
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

                  // console.log(this.bookmarkCardItems, '2710book');
                  res.result.forEach(collection => {
                    const indexcard = this.collectionCardItems.find(x => x.id == collection.collectionId);
                    if (!indexcard) {
                      const card = new documentCard();
                      card.id = collection.collectionId;
                      card.author = 'me';
                      card.type = 'COLLECTION';
                      card.title = collection.collectionId;
                      card.content = collection.documentIdList.length + ' documents included';
                      card.modifiedAt = collection.modifiedDate;
                      card.isGroup = true;
                      card.totalDocuments = collection.documentIdList.length;

                      const docsDetail = [];
                      collection.documentIdList.forEach(docId => {
                        const item = this.bookmarkCardItems.find(item => item.id == docId);
                        if (item) {
                          docsDetail.push(item);
                        }
                        if (item?.images[0] && item.images[0].length) {
                          card.images.push(item.images[0]);
                        }
                        if (item?.keyword) {
                          if (card.tags.indexOf(item.keyword) > -1) {
                            card.tags.push(item.keyword);
                          }
                        }
                      });
                      card.docDetail = docsDetail;
                      this.collectionCardItems.push(card);
                    }
                  });
                  console.log('collectionCardItems2710', this.collectionCardItems);

                  if (!currentCollectionResult || currentCollectionResult.status != 'OK') {
                    return;
                  }

                  if (!currentCollectionResult.result) {
                    return;
                  }

                  if (!currentCollectionResult.result.collectionId) {
                    return;
                  }
                })
                .finally(() => {
                  this.processDocumentsSelectorList();
                });
            });

            if (!currentCollectionResult || currentCollectionResult.status != 'OK' || !currentCollectionResult.result) {
              return;
            }

            if (!currentCollectionResult.result.documentIdList || currentCollectionResult.result.documentIdList.length == 0) {
              return;
            }
          })
          .finally(() => {
            this.processDocumentsSelectorList();
          });
      })
      .catch(reason => {
        console.log('catch get mini connectome', reason);
      })
      .finally(() => {
        this.processDocumentsSelectorList();
      });
  }

  processDocumentsSelectorList() {
    this.documentOrGroupDocumentsCardItems = new Array<documentCard>();

    if (this.bookmarkCardItems && this.bookmarkCardItems.length > 0) {
      this.documentOrGroupDocumentsCardItems.push(...this.bookmarkCardItems);
    }
    if (this.collectionCardItems && this.collectionCardItems.length > 0) {
      this.documentOrGroupDocumentsCardItems.push(...this.collectionCardItems);
    }
    this.documentOrGroupDocumentsCardItems = this.documentOrGroupDocumentsCardItems.sort((a, b) => {
      if (!a.modifiedAt) {
        return 1;
      }

      if (!b.modifiedAt) {
        return -1;
      }
      return new Date(b.modifiedAt).getTime() - new Date(a.modifiedAt).getTime();
    });

    console.log('this.documentOrGroupDocumentsCardItems', this.documentOrGroupDocumentsCardItems);
  }

  @Watch('isCurrentCollectionChanged')
  onCurrentCollectionDataChange(newVal: number) {
    this.getCurrentDraftCollection().then(res => {
      console.log('getCurrentDraftCollection', res);
      if (!res || res.status === 'NOK') {
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
          card.author = item.writer;
          card.type = item.type;
          card.title = item.title;
          card.content = item.description ? '[Summary]' + item.description : '[Content]' + this.getShortContent(item.description);
          card.keyword = item.keyword;
          if (card.group.indexOf(item.keyword) > -1) {
            card.group.push(item.keyword);
          }
          card.addedAt = new Date(item.created_date);
          card.modifiedAt = new Date(item.created_date);
          card.totalDocuments = 1;
          card.favicon = item.favicon_url ? item.favicon_url : item.favicon_base64;
          card.images[0] = item.og_image_url ? item.og_image_url : item.og_image_base64;
          card.isGroup = item.isGroup ? item.isGroup : '';

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

  getShortContent(content: string) {
    if (!content) {
      return ' ';
    }

    if (content.length < 256) {
      return content;
    }

    return content.substring(0, 255) + '...';
  }

  selectAllInGroup(newItems: any) {
    newItems.forEach(newItem => {
      if (!this.selectedItems.find(item => item.id == newItem.id)) {
        this.selectedItems.push(newItem);
      }
    });
    this.$root.$emit('cart-to-conlection', this.selectedItems, 'doc');
  }

  removeAllInGroup(removeItems: any) {
    removeItems.forEach(removeItem => {
      const item = this.selectedItems.findIndex(item => item.id == removeItem.id);
      if (item > -1) {
        this.selectedItems.splice(item, 1);
      }
    });
    this.$root.$emit('cart-to-conlection', this.selectedItems, 'doc');
  }

  setSelectedItems(newData) {
    console.log('this.delec2710', newData);
    this.selectedItems = newData;
    const arrTmp = this.checkArraySelected();
    // this.totalSelected = this.allData.length - arrTmp.length;
    this.$root.$emit('cart-to-conlection', this.selectedItems, 'doc');
  }

  checkArraySelected(arg?) {
    // if (arg) return onlyInLeft(this.selectedItems, this.allData);
    // return onlyInLeft(this.allData, this.selectedItems);
  }
}
