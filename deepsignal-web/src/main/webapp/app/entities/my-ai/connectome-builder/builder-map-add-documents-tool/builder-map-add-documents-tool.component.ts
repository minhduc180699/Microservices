import FeedService from '@/core/home/feed/feed.service';
import ConnectomeService from '@/entities/connectome/connectome.service';
import { ShowMoreMixin } from '@/mixins/show-more';
import { PrincipalService } from '@/service/principal.service';
import { CardModel } from '@/shared/cards/card.model';
import DsCards from '@/shared/cards/ds-cards.vue';
import ShowMore from '@/shared/cards/footer/show-more/show-more.vue';
import { DOCUMENT_TYPE, TYPE_VERTEX } from '@/shared/constants/ds-constants';
import { ACTIVITY, CARD_SIZE } from '@/shared/constants/feed-constants';
//import { ConnectomeNetworkVertex } from '@/shared/model/connectome-network-vertex.model';
import { ConnectomeNetwork } from '@/shared/model/connectome-network.model';
import { documentCard } from '@/shared/model/document-card.model';
import { documentMetadataPlus } from '@/shared/model/document-metadata-plus.model';
import axios from 'axios';
import moment from 'moment';
import { userInfo } from 'os';
import { stringify } from 'querystring';
import { start } from 'repl';
import { mixins } from 'vue-class-component';
import { Component, Inject, Vue, Watch } from 'vue-property-decorator';
import { namespace } from 'vuex-class';
import LearnedDocument from '../../learning-center/learned-document/learned-document.component';

const networkStore = namespace('connectomeNetworkStore');
const connectomeBuilderStore = namespace('connectomeBuilderStore');

@Component({
  components: {
    'ds-cards': DsCards,
    'show-more': ShowMore,
  },
})
export default class BuilderMapAddDocumentsTool extends mixins(ShowMoreMixin) {
  @Inject('principalService')
  private principalService: () => PrincipalService;

  @networkStore.Getter
  public lang!: string;

  @connectomeBuilderStore.Mutation
  public setDataUpdate!: () => void;

  @connectomeBuilderStore.Action
  public addPdConnectomeData!: (payload: { connectomeId: string; language: string; ids: Array<string> }) => Promise<any>;

  @connectomeBuilderStore.Action
  public addTextConnectomeData!: (payload: {
    connectomeId: string;
    documentId: string;
    language: string;
    title: string;
    content: string;
  }) => Promise<any>;

  @connectomeBuilderStore.Action
  public removePdConnectomeData!: (payload: { id: string }) => Promise<any>;

  @connectomeBuilderStore.Action
  public updateMinNodeWeight!: (value: number) => Promise<number>;

  @connectomeBuilderStore.Action
  public updateMinLinkedNodes!: (value: number) => Promise<number>;

  @connectomeBuilderStore.Action
  public updateMinRelatedDocuments!: (value: number) => Promise<number>;

  connectomeId: string = null;
  userId: string = null;
  trainingDocumentCount = 0;
  documentCardItems: Array<documentCard> = new Array<documentCard>();
  documentMetadataPlusItems: Array<documentMetadataPlus> = new Array<documentMetadataPlus>();
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
    this.connectomeId = connectomeId;
    this.userId = userId;
    this.getPDApi();
  }

  noteTitle: string = null;
  noteContent: string = null;

  onBtnAddPersonalDocumentsToConnectomeClick(card: CardModel) {
    this.addTextConnectomeData({
      connectomeId: this.connectomeId,
      documentId: card.id,
      language: this.lang,
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
          card.style = 'background-color:lime';
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

  public scrollLoader() {
    this.getPDApi();
  }

  public onScroll({ target: { scrollTop, clientHeight, scrollHeight } }) {
    //console.log(event);

    if (scrollTop + clientHeight >= scrollHeight) {
      this.getPDApi();
    }
  }

  public onMore(event) {
    //console.log(event);
    this.getPDApi();
  }

  onBtnSidebarFoldingClick(event) {
    event.preventDefault();
    console.log('onBtnSidebarFoldingClick', $(this));
    $('#btn-sidebar-folding').toggleClass('active');
    $('.connectome-home').toggleClass('active');
  }

  onBtnPanelSubCloseClick(event) {
    event.preventDefault();
    this.panelSub = 'panel-sub';
  }

  onMouseOverDocumentCard(event) {
    console.log('test mouse hver');
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

  private page = 0;
  private size = 50;
  private totalFeeds = 0;
  private sortDirection = 'desc';
  private loaderDisable = false;

  private listFilter = [{ field: 'liked', value: 1 }];

  private totalItems = 0;

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
        res.data.results.length < this.size ? (this.loaderDisable = true) : (this.page += 1);
        if (this.totalItems != res.data.totalItems) {
          this.totalFeeds = res.data.totalItems;
        }
        res.data.results.forEach(item => {
          const card = new documentCard();

          card.id = item.docId;
          card.author = item.author;
          card.type = item.type;
          card.title = item.title;
          card.content = this.getShortContent(item.content);
          //card.keyword = item.keyword;
          card.group.push(item.keyword);
          card.addedAt = new Date(item.publishedAt);
          card.modifiedAt = new Date(item.publishedAt);

          const metadata = this.documentMetadataPlusItems.find(
            x => x.author === item.author && x.name === item.title && x.url === item.url
          );
          if (metadata) {
            card.content = metadata.description;
            card.keyword = metadata.keyword;
          }

          this.documentCardItems.push(card);
        });
        console.log(res);
      });
  }

  handleActivity(item, activity) {}

  onSearchInput(event) {
    //console.log('filterTerms', this.filterTerms);
    let entitiesMatched: Array<documentCard> = [];
    if (event.target.value) {
      const regOption = new RegExp(event.target.value, 'ig');
      entitiesMatched = this.documentCardItems
        .filter(card => card.title.match(regOption) || card.content.match(regOption) || card.author.match(regOption))
        .map(x => x);
    } else {
      entitiesMatched = this.documentCardItems.map(x => x);
    }
    //this.setSearchResultList(entitiesMatched);
    //}
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