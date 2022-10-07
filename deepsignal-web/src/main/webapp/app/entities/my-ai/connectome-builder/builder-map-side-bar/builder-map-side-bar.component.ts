import FeedService from '@/core/home/feed/feed.service';
import { ShowMoreMixin } from '@/mixins/show-more';
import { PrincipalService } from '@/service/principal.service';
import { CardModel } from '@/shared/cards/card.model';
import DsCards from '@/shared/cards/ds-cards.vue';
import ShowMore from '@/shared/cards/footer/show-more/show-more.vue';
import { ConnectomeNode } from '@/shared/model/connectome-node.model';
//import { ConnectomeNetworkVertex } from '@/shared/model/connectome-network-vertex.model';
import { ContextualMemoryCollection } from '@/shared/model/contextual-memory-collection.model';
import { documentCard } from '@/shared/model/document-card.model';
import { mixins } from 'vue-class-component';
import { Component, Inject, Watch } from 'vue-property-decorator';
import { namespace } from 'vuex-class';

const networkStore = namespace('connectomeNetworkStore');
const connectomeBuilderStore = namespace('connectomeBuilderStore');
const collectionManagerStore = namespace('collectionManagerStore');

@Component({
  components: {
    'ds-cards': DsCards,
    'show-more': ShowMore,
  },
})
export default class BuilderMapSideBar extends mixins(ShowMoreMixin) {
  @Inject('feedService')
  private feedService: () => FeedService;
  @Inject('principalService')
  private principalService: () => PrincipalService;

  @networkStore.Getter
  public lang!: string;

  @collectionManagerStore.Getter
  public getCollections!: Array<ContextualMemoryCollection>;

  @collectionManagerStore.Getter
  public getCurrentCollection!: ContextualMemoryCollection;

  @collectionManagerStore.Getter
  public isUpdated!: number;

  @collectionManagerStore.Getter
  public isCurrentDocumentSetUpdated!: number;

  @collectionManagerStore.Getter
  public getCurrentDocumentSet!: Set<string>;

  @collectionManagerStore.Mutation
  public setCurrentDocumentSet!: (payload: { docSet: Array<string> }) => void;

  @collectionManagerStore.Mutation
  public AddToCurrentDocumentSet!: (payload: { docToAdd: Array<string> }) => void;

  @collectionManagerStore.Action
  public loadUserCollections!: (payload: { connectomeId: string; language: string }) => Promise<any>;

  @collectionManagerStore.Action
  public loadCurrentCollection!: (payload: { collectionId: string }) => Promise<any>;

  @collectionManagerStore.Action
  public loadDocumentsFromCollection!: (payload: { docIds: Array<string> }) => Promise<any>;

  collectionCardItems: Array<documentCard> = new Array<documentCard>();

  @Watch('getCollections')
  onCollectionsChange(newVal: Array<ContextualMemoryCollection>) {
    console.log('onCollectionsChange', newVal);
    this.collectionCardItems = [];
    newVal.forEach(item => {
      const card = new documentCard();
      card.id = item.collectionId;
      card.author = 'collection';
      card.type = 'COLLECTION';
      card.title = item.collectionId;
      card.content = item.documentIdList.length + ' documents included';
      card.keyword = item.keywordList.join(',');
      card.addedAt = null;
      card.modifiedAt = null;
      if (
        this.getCurrentCollection &&
        this.getCurrentCollection.collectionId &&
        this.getCurrentCollection.collectionId === item.collectionId
      ) {
        card.isAdded = true;
        card.style = 'background-color:Lime';
      } else {
        card.isAdded = false;
        card.style = '';
      }
      this.collectionCardItems.push(card);
    });
  }

  @Watch('getCurrentCollection')
  onCurrentCollectionChanged(newVal: ContextualMemoryCollection) {
    console.log('onCurrentCollectionChange', newVal);
    this.labelcontext = '';

    this.setCurrentDocumentSet({ docSet: [] });

    if (!newVal) {
      this.resetConnectomeBuilderData();
      return;
    }

    if (newVal.documentIdList) {
      this.loadDocumentsFromCollection({ docIds: newVal.documentIdList }).then(res => {
        console.log('loadDocumentsFromCollection', res);
        res.forEach(item => {
          this.loadDocumentFromContext({ connectomeId: this.connectomeId, documentId: item.docId, keyword: item.keyword }).then(res => {
            if (!res) {
              return;
            }

            if (!res.documentIds) {
              return;
            }
            this.AddToCurrentDocumentSet({ docToAdd: res.documentIds });
          });
        });
      });
    }

    if (!newVal.connectomeNodeList || newVal.connectomeNodeList.length == 0) {
      this.resetConnectomeBuilderData();
    } else {
      this.setConnectome({ connectome: newVal.connectomeNodeList });
      this.disableOrphans();
    }

    if (newVal.collectionId) {
      this.labelcontext = newVal.collectionId;
      const card = this.collectionCardItems.forEach(card => {
        if (card.id === newVal.collectionId) {
          card.isAdded = true;
          card.style = 'background-color:Lime';
        } else {
          card.isAdded = false;
          card.style = '';
        }
      });
    }
  }

  labelcontext = '';

  @Watch('isCurrentDocumentSetUpdated')
  onCurrentDocumentSetChanged(newVal: number) {
    console.log('getCurrentDocumentSet', newVal);
    if (!this.getCurrentDocumentSet) {
      return;
    }

    this.documentCardItems = [];

    if (this.getCurrentDocumentSet.size == 0) {
      return;
    }

    this.loadDocumentsFromCollection({ docIds: Array.from(this.getCurrentDocumentSet) }).then(res => {
      console.log('onCurrentDocumentSetChanged res', res);
      this.documentCardItems = [];
      res.forEach(item => {
        const idx = this.documentCardItems.find(x => x.id === item.docId);
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
        if (this.getDocumentsSelected.has(item.docId)) {
          console.log(' inside getDocumentsSelected');
          card.isAdded = true;
          card.style = 'background-color:' + this.getDocumentColors.get(card.id);
        } else {
          card.isAdded = false;
          card.style = '';
        }
        this.documentCardItems.push(card);
      });
    });
  }

  @Watch('isUpdated')
  onDataChange(newVal: number) {
    // console.log('onDataChange', newVal);
    // console.log('getCollections', this.getCollections);
  }

  @connectomeBuilderStore.Getter
  public getNodes!: Array<ConnectomeNode>;

  @connectomeBuilderStore.Getter
  public getConnectome!: Array<ConnectomeNode>;

  @connectomeBuilderStore.Getter
  public getSequenceNote!: number;

  @connectomeBuilderStore.Getter
  public getMinNodeWeight!: number;

  @connectomeBuilderStore.Getter
  public getMinLinkedNodes!: number;

  @connectomeBuilderStore.Getter
  public getMinRelatedDocuments!: number;

  @connectomeBuilderStore.Getter
  public getDocumentsSelected!: Map<string, Array<ConnectomeNode>>;

  @connectomeBuilderStore.Getter
  public getDocumentColors!: Map<string, string>;

  @connectomeBuilderStore.Mutation
  public setConnectome!: (payload: { connectome: Array<ConnectomeNode> }) => void;

  @connectomeBuilderStore.Mutation
  public disableOrphans!: () => void;

  @connectomeBuilderStore.Mutation
  public incrementSequenceNote!: () => void;

  @connectomeBuilderStore.Mutation
  public resetConnectomeBuilderData!: () => void;

  @connectomeBuilderStore.Mutation
  public setDataUpdate!: () => void;

  @connectomeBuilderStore.Action
  public addPdConnectomeData!: (payload: { connectomeId: string; language: string; ids: Array<string> }) => Promise<any>;

  @connectomeBuilderStore.Action
  public addTextConnectomeData!: (payload: {
    connectomeId: string;
    documentId: string;
    language: string;
    keyword: string;
    title: string;
    content: string;
  }) => Promise<any>;

  @connectomeBuilderStore.Action
  public loadDocumentFromContext!: (payload: { connectomeId: string; documentId: string; keyword: string }) => Promise<any>;

  @connectomeBuilderStore.Action
  public removePdConnectomeData!: (payload: { id: string }) => Promise<any>;

  @connectomeBuilderStore.Action
  public updateMinNodeWeight!: (value: number) => Promise<number>;

  @connectomeBuilderStore.Action
  public updateMinLinkedNodes!: (value: number) => Promise<number>;

  @connectomeBuilderStore.Action
  public updateMinRelatedDocuments!: (value: number) => Promise<number>;

  localNodes: Array<ConnectomeNode> = [];
  @Watch('getConnectome')
  onGetNodesChanged(data: Array<ConnectomeNode>) {
    console.log('connectomeUpdated');
    this.localNodes = this.getNodes.map(x => x);
  }

  connectomeId: string = null;
  userId: string = null;
  trainingDocumentCount = 0;
  documentCardItems: Array<documentCard> = new Array<documentCard>();
  panelSub = 'panel-sub active';
  filterTerms = null;

  @Watch('$route', { immediate: true, deep: true })
  onUrlChange(newVal: any) {
    console.log(newVal);

    if (newVal.name.localeCompare('Builder') == 0) {
      document.body.setAttribute('data-menu', 'collection-builder');
    }
  }

  mounted() {
    const userId = this.principalService().getUserInfo().id;
    const connectomeId = this.principalService().getConnectomeInfo().connectomeId;
    //const connectomeId ='CID_b0b8a423-69fa-4d07-b880-866a0e34487e';
    this.connectomeId = connectomeId;
    this.userId = userId;

    this.loadUserCollections({ connectomeId: this.connectomeId, language: this.lang }).then(res => {
      if (!res) {
        return;
      }
    });
  }

  noteTitle: string = null;
  noteContent: string = null;
  onOpenCollectionClick(card: documentCard) {
    if (!card.id) return;

    this.resetConnectomeBuilderData();
    //card.id = 'ctxmm_fcd19151-ae56-4565-86d8-97def8c64ba7';
    this.loadCurrentCollection({ collectionId: card.id })
      .then(res => {
        if (!res) {
          return;
        }
      })
      .catch(reason => {
        console.log('catch get mini connectome', reason);
      });
  }

  onBtnAddPersonalDocumentsToConnectomeClick(card: documentCard) {
    this.addTextConnectomeData({
      connectomeId: this.connectomeId,
      documentId: card.id,
      language: this.lang,
      keyword: card.keyword,
      title: card.title,
      content: card.content,
    })
      .then(res => {
        if (!res) {
          return;
        }

        if (!res.documentIds) {
          return;
        }

        if (res.documentIds.length == 0) {
          return;
        }
        const card = this.documentCardItems.find(x => x.id == res.documentIds[0]);
        if (res.connectome && res.connectome.length > 0) {
          card.isAdded = true;
          card.style = 'background-color:' + this.getDocumentColors.get(card.id);
        } else {
          card.isAdded = false;
          card.style = '';
        }
        console.log(this.documentCardItems);
      })
      .catch(reason => {
        console.log('catch get mini connectome', reason);
      });
    // this.addPdConnectomeData({
    //   connectomeId: this.connectomeId,
    //   language: this.lang,
    //   ids: ids,
    // })
    //   .then(res => {
    //     if (!res) {
    //       console.log('res null', res);
    //       return;
    //     }
    //     //this.renderMap();
    //   })
    //   .catch(reason => {
    //     console.log('catch get mini connectome', reason);
    //   });
  }

  onMouseOverDocumentCard(event) {
    //console.log('test mouse hver');
  }

  isOrderByDate = true;

  showAddFavoriteFailedToast() {
    this.$bvToast.toast("Keyword's update failed!", {
      toaster: 'b-toaster-bottom-right',
      title: 'Fail',
      variant: 'danger',
      solid: true,
      autoHideDelay: 5000,
    });
  }

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
