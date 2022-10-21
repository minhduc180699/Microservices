import ConnectomeService from '@/entities/connectome/connectome.service';
import { PrincipalService } from '@/service/principal.service';
import { TYPE_VERTEX } from '@/shared/constants/ds-constants';
import { ConnectomeNetworkVertex } from '@/shared/model/connectome-network-vertex.model';
import { ConnectomeNetwork } from '@/shared/model/connectome-network.model';
import axios from 'axios';
import { userInfo } from 'os';
import { stringify } from 'querystring';
import { Component, Inject, Vue, Watch } from 'vue-property-decorator';
import { namespace } from 'vuex-class';

const networkStore = namespace('connectomeNetworkStore');
const mapNetworkStore = namespace('mapNetworkStore');

@Component
export default class MapSideBar extends Vue {
  @Inject('connectomeService')
  private connectomeService: () => ConnectomeService;

  @Inject('principalService')
  private principalService: () => PrincipalService;

  @networkStore.Getter
  public networkData!: ConnectomeNetwork;

  @networkStore.Getter
  public vertices!: Array<ConnectomeNetworkVertex>;

  @networkStore.Getter
  public lastUpdatedAt!: string;

  @networkStore.Getter
  public lang!: string;

  @networkStore.Getter
  public mainClusters!: Array<{ name: string; linksByVertexCount: number; vertexCount: number; maxLinksByVertex: number }>;

  @networkStore.Getter
  public verticeColorByCluster!: (mainCluster: string) => d3.RGBColor;

  @networkStore.Getter
  public vertice!: (label: string) => ConnectomeNetworkVertex;

  @networkStore.Getter
  public listDisableVerticeLabels!: Set<string>;

  @mapNetworkStore.Getter
  public entityLabelSelected!: string;

  @mapNetworkStore.Getter
  public entityDetailsSelected!: any;

  @mapNetworkStore.Getter
  public searchResult!: Array<ConnectomeNetworkVertex>;

  @mapNetworkStore.Getter
  public entityPersonalDocuments!: Array<any>;

  @mapNetworkStore.Getter
  public entityFeeds!: Array<any>;

  @networkStore.Action
  public toggleEntityDisability!: (payload: { title: string }) => Promise<any>;

  @networkStore.Action
  public toggleEntityFavorite!: (payload: { title: string }) => Promise<any>;

  @mapNetworkStore.Action
  public setSelectedEntity!: (payload: { title: string; srcLang: string }) => void;

  @mapNetworkStore.Action
  public getEntityDetails!: (payload: { title: string; srcLang: string }) => void;

  @mapNetworkStore.Action
  public clearSelectedEntity!: () => void;

  @mapNetworkStore.Action
  public clearDetails!: () => void;

  @mapNetworkStore.Action
  public setNewSearchResult!: (payload: { searchResult: Array<ConnectomeNetworkVertex> }) => void;

  @mapNetworkStore.Action
  public refreshMap!: () => void;

  @mapNetworkStore.Action
  public requestCenterAtSelection!: () => void;

  @mapNetworkStore.Action
  public getPersonalDocumentByEntityLabel!: (payload: { entityLabel: string; connectomeId: string }) => Promise<any>;

  @mapNetworkStore.Action
  public getFeedsByEntityLabel!: (payload: { entityLabel: string; connectomeId: string }) => Promise<any>;

  @mapNetworkStore.Action
  public getDocsByEntityLabel!: (payload: { connectomeId: string; language: string; label: string }) => Promise<any>;

  data(): {
    isWikipediaInfoboxExpanded: boolean;
  } {
    return {
      isWikipediaInfoboxExpanded: false,
    };
  }

  connectomeId: string = null;
  userId: string = null;
  trainingDocumentCount = 0;
  panelSub = 'panel-sub';

  @Watch('$route', { immediate: true, deep: true })
  onUrlChange(newVal: any) {
    console.log(newVal);

    if (newVal.name.localeCompare('2DNetwork') == 0) {
      document.body.setAttribute('data-menu', 'connectome');
      const vertex = this.entityLabelSelected ? this.vertice(this.entityLabelSelected) : this.entityLabelSelected;
      if (vertex && this.vertices) {
        this.onEntitySelectedChanged(this.entityLabelSelected);
      } else {
        console.log('panel should be invisible mounted');
        this.panelSub = 'panel-sub';
        if ($('#btn-sidebar-folding') && $('.connectome-home') && !$('#btn-sidebar-folding').hasClass('active')) {
          $('#btn-sidebar-folding').toggleClass('active');
          $('.connectome-home').toggleClass('active');
        }
      }
      this.initSearchResult();
    }
  }

  mounted() {
    const userId = this.principalService().getUserInfo().id;
    const connectomeId = this.principalService().getConnectomeInfo().connectomeId;
    this.setSearchResultFilterName('visible');
    document.body.setAttribute('data-menu', 'connectome');
    //window.onresize = this.resizeFullscreen;
    // this.fullscreen =
    //   this.$route.query.fullscreen === 'TRUE' || this.$route.query.fullscreen === 'true' || this.$route.query.fullscreen === '1';

    // if (!userId || !connectomeId) {
    //   $('#btn-panelSub-close').trigger('click');
    //   return;
    // }
    this.connectomeId = connectomeId;
    this.userId = userId;

    const vertex = this.entityLabelSelected ? this.vertice(this.entityLabelSelected) : this.entityLabelSelected;
    if (vertex && this.vertices) {
      console.log('update on mounted event');
      this.onEntitySelectedChanged(this.entityLabelSelected);
    } else {
      console.log('panel should be invisible mounted');
      this.panelSub = 'panel-sub';
      // if ($('.panel-sub') && $('.panel-sub').hasClass('active') === true) {
      //   console.log('panel should be invisible');
      //   $('.panel-sub').removeClass('active');
      // }
      if ($('#btn-sidebar-folding') && $('.connectome-home') && !$('#btn-sidebar-folding').hasClass('active')) {
        $('#btn-sidebar-folding').toggleClass('active');
        $('.connectome-home').toggleClass('active');
      }
    }
    this.initSearchResult();
  }

  @Watch('vertices')
  onVerticesUpdated(data: Array<ConnectomeNetworkVertex>) {
    console.log('update on VerticesUpdated');
    if (this.entityLabelSelected) {
      this.onEntitySelectedChanged(this.entityLabelSelected);
    }
  }

  @Watch('lastUpdatedAt')
  onlastUpdatedAtUpdated(lastUpdateDate: string) {
    if (!lastUpdateDate) {
      return;
    }
    if (!this.vertice(this.entityLabelSelected)) {
      this.clearSelectedEntity();
    }
    this.initSearchResult();
  }

  @Watch('entityLabelSelected')
  onEntitySelectedChanged(label: string) {
    console.log('MapSideBar saw entity label changed', label);
    this.title = label;
    const vertex = this.vertice(this.entityLabelSelected);

    if (!label || !vertex) {
      this.isDetailsPanelExist = false;
      this.iswikiInfoListExist = false;
      this.imageUrl = null;
      this.description = null;
      this.wikiInfoList = [];
      this.wikiUrl = null;
      this.snsFacebook = null;
      this.snsInstagram = null;
      this.snsTwitter = null;
      this.snsYoutube = null;
      this.snsLinkedIn = null;
      this.trainingDocumentCount = 0;
      console.log('panel should be invisible');
      //$('.panel-sub').removeClass('active');
      this.panelSub = 'panel-sub';
      return;
    }
    console.log('MapSideBar saw entity label changed', vertex);
    this.trainingDocumentCount = 0;
    this.isDetailsPanelExist = true;
    if ($('#btn-sidebar-folding') && $('.connectome-home') && $('#btn-sidebar-folding').hasClass('active')) {
      $('#btn-sidebar-folding').toggleClass('active');
      $('.connectome-home').toggleClass('active');
    }

    // if ($('.panel-sub').hasClass('active') === false) {
    //   console.log('panel should be visible');
    //   $('.panel-sub').addClass('active');
    // }
    this.panelSub = 'panel-sub active';
    // this.getPersonalDocumentByEntityLabel({ entityLabel: label, connectomeId: this.connectomeId }).then(res => {
    //   console.log('Call getPersonalDocumentByEntityLabel', res);
    //   if (res && res.data && res.data.totalItems) {
    //     this.trainingDocumentCount = res.data.totalItems;
    //   }
    // });

    // this.getFeedsByEntityLabel({ entityLabel: label, connectomeId: this.connectomeId }).then(res => {
    //   console.log('Call getPersonalDocumentByEntityLabel', res);
    // });

    this.getDocsByEntityLabel({ connectomeId: this.connectomeId, language: this.lang, label: label }).then(res => {
      console.log('Call getDocsByEntityLabel', res);

      if (!res) {
        this.trainingDocumentCount = 0;
        return;
      }

      if (!res.data) {
        this.trainingDocumentCount = 0;
        return;
      }

      if (!res.data.body) {
        this.trainingDocumentCount = 0;
        return;
      }

      this.trainingDocumentCount = this.entityFeeds.length + this.entityPersonalDocuments.length;
    });
    //console.log('data after ', this.store.getters['connectomeNetworkStore/data']);
    console.log('MapSideBar saw entity selected changed', label);

    if (!vertex || vertex.type === TYPE_VERTEX.BLACK_ENTITY) {
      this.clearDetails();
    } else {
      this.getEntityDetails({ title: label, srcLang: this.lang });
    }

    if (vertex) {
      this.isFavorite = vertex.favorite ? true : false;
      this.isDisable = vertex.disable ? true : false;
      console.log('MapSideBar saw node selected', vertex);
      this.entitiesLinked = [];
      this.entitiesLinkedFull = [];
      this.entitiesLinkedSmall = [];
      this.entitiesLinkedCount = 0;
      this.entitiesLinkedFull = vertex.entities.filter(
        vertex => vertex != this.entityLabelSelected && !this.listDisableVerticeLabels.has(vertex)
      );
      this.entitiesLinkedCount = this.entitiesLinkedFull.length;
      if (this.entitiesLinkedFull.length > 5) {
        this.entitiesLinkedSmall = this.entitiesLinkedFull.slice(0, 4);
      } else {
        this.entitiesLinkedSmall = this.entitiesLinkedFull.map(x => x);
      }
      this.labelEntitiesLinkedExpanded = this.$t('map-side-bar.details-tab.see-more').toString();
      this.iconEntitiesLinkedExpanded = 'chevron-down';
      this.entitiesLinked = this.entitiesLinkedSmall.map(x => x);
    }
  }

  @Watch('entityDetailsSelected')
  onEntityDetailsChanged(details: any) {
    console.log('MapSideBar saw entity details changed', details);
    this.imageUrl = null;
    this.description = null;
    this.wikiInfoList = [];
    this.wikiFullInfoList = [];
    this.wikiSmallInfoList = [];
    this.wikiUrl = null;
    this.snsFacebook = null;
    this.snsInstagram = null;
    this.snsTwitter = null;
    this.snsYoutube = null;
    this.snsLinkedIn = null;

    if (!details) {
      return;
    }

    const node = this.vertice(details.label);

    if (!node || node.type === TYPE_VERTEX.BLACK_ENTITY) {
      this.isImageUrlExist = false;
      this.isDescriptionExist = false;
      return;
    }

    if (details.imageUrl) {
      this.imageUrl = details.imageUrl;
      this.isImageUrlExist = true;
    } else {
      this.isImageUrlExist = false;
    }

    if (details.description) {
      this.description = details.description;
      this.isDescriptionExist = true;
    } else {
      this.isDescriptionExist = false;
    }

    if (details.wikiUrl) {
      this.wikiUrl = details.wikiUrl;
    }

    if (details.facebook) {
      this.snsFacebook = details.facebook;
    }
    if (details.instagram) {
      this.snsInstagram = details.instagram;
    }
    if (details.twitter) {
      this.snsTwitter = details.twitter;
    }
    if (details.youtube) {
      this.snsYoutube = details.youtube;
    }
    if (details.linkedin) {
      this.snsLinkedIn = details.linkedin;
    }

    if (details.wikipediaInfobox && Object.entries(details.wikipediaInfobox).length > 0) {
      //console.log('wikipediaInfobox', Object.entries(details.wikipediaInfobox));
      let i = 0;
      for (const [key, value] of Object.entries(details.wikipediaInfobox)) {
        const strValue = value.toString();
        if (
          !strValue.toLowerCase().endsWith('.jpg') &&
          !strValue.toLowerCase().endsWith('.gif') &&
          !strValue.toLowerCase().endsWith('.jpg') &&
          !strValue.toLowerCase().endsWith('.png') &&
          !strValue.toLowerCase().endsWith('.svg') &&
          !strValue.toLowerCase().endsWith('.csv') &&
          !strValue.toLowerCase().endsWith('.jpeg') &&
          !strValue.toLowerCase().endsWith('.jpg') &&
          !strValue.toLowerCase().endsWith('.jfif') &&
          !strValue.toLowerCase().endsWith('.pjpeg') &&
          !strValue.toLowerCase().endsWith('.pjpeg') &&
          !strValue.toLowerCase().endsWith('.webp') &&
          !strValue.toLowerCase().endsWith('.tif') &&
          !strValue.toLowerCase().endsWith('.ico') &&
          !strValue.toLowerCase().endsWith('.cur') &&
          !strValue.toLowerCase().endsWith('.apng') &&
          !strValue.toLowerCase().endsWith('.avif') &&
          !strValue.toLowerCase().endsWith('.jpe') &&
          !strValue.toLowerCase().endsWith('.xbm') &&
          !strValue.toLowerCase().endsWith('.tiff') &&
          !strValue.toLowerCase().endsWith('.exif') &&
          !strValue.toLowerCase().endsWith('.bmp') &&
          !key.toLowerCase().endsWith('_alt') &&
          !key.toLowerCase().endsWith('_caption')
        ) {
          this.wikiFullInfoList.push({ key: key.replace(/_/gi, ' '), value: strValue.substring(0, 300) });
          if (i < 5) {
            this.wikiSmallInfoList.push({ key: key.replace(/_/gi, ' '), value: strValue.substring(0, 300) });
            i++;
          }
        }
      }
      this.labelWikiDescriptionExpanded = this.$t('map-side-bar.details-tab.see-more').toString();
      this.iconWikiDescriptionExpanded = 'chevron-down';
      this.wikiInfoList = this.wikiSmallInfoList.map(x => x);
      this.iswikiInfoListExist = this.wikiInfoList.length > 0;
    } else {
      this.iswikiInfoListExist = false;
    }
  }

  isDetailsPanelExist = false;
  title: string = null;
  isFavorite = false;
  isDisable = false;
  isImageUrlExist = false;
  imageUrl: string = null;
  wikiUrl: string = null;
  snsFacebook: string = null;
  snsInstagram: string = null;
  snsTwitter: string = null;
  snsYoutube: string = null;
  snsLinkedIn: string = null;

  isDescriptionExist = false;
  description: string = null;

  iswikiInfoListExist = false;
  wikiInfoList: Array<{ key: string; value: any }> = [];
  wikiFullInfoList: Array<{ key: string; value: any }> = [];
  wikiSmallInfoList: Array<{ key: string; value: any }> = [];

  entitiesLinked: Array<string> = [];
  entitiesLinkedSmall: Array<string> = [];
  entitiesLinkedFull: Array<string> = [];
  entitiesLinkedCount = 0;

  private iconWikiDescriptionExpanded = 'chevron-down';
  private labelWikiDescriptionExpanded = this.$t('map-side-bar.details-tab.see-more').toString();

  private iconEntitiesLinkedExpanded = 'chevron-down';
  private labelEntitiesLinkedExpanded = this.$t('map-side-bar.details-tab.see-more').toString();

  resizeInfoBox() {
    if (this.wikiInfoList.length === this.wikiSmallInfoList.length) {
      this.labelWikiDescriptionExpanded = this.$t('map-side-bar.details-tab.see-less').toString();
      this.iconWikiDescriptionExpanded = 'chevron-up';
      this.wikiInfoList = this.wikiFullInfoList.map(x => x);
    } else {
      this.labelWikiDescriptionExpanded = this.$t('map-side-bar.details-tab.see-more').toString();
      this.iconWikiDescriptionExpanded = 'chevron-down';
      this.wikiInfoList = this.wikiSmallInfoList.map(x => x);
    }
  }

  resizeEntitiesLinked() {
    if (this.entitiesLinked.length === this.entitiesLinkedSmall.length) {
      this.labelEntitiesLinkedExpanded = this.$t('map-side-bar.details-tab.see-less').toString();
      this.iconEntitiesLinkedExpanded = 'chevron-up';
      this.entitiesLinked = this.entitiesLinkedFull.map(x => x);
    } else {
      this.labelEntitiesLinkedExpanded = this.$t('map-side-bar.details-tab.see-more').toString();
      this.iconEntitiesLinkedExpanded = 'chevron-down';
      this.entitiesLinked = this.entitiesLinkedSmall.map(x => x);
    }
  }

  moveEntitySelectedTo(node: string) {
    if (!node) {
      return;
    }
    this.setSelectedEntity({ title: node, srcLang: this.lang });
  }

  centerEntitySelectedTo(node: string) {
    if (!node) {
      return;
    }
    this.requestCenterAtSelection();
  }

  onFeedsClick() {
    this.$router.push('/feed');
  }

  onPeopleClick() {
    this.$router.push({ name: 'People', hash: '#people:' + this.title });
  }

  onLearnMoreClick() {
    this.$router.push('/my-ai/learning-center');
  }

  onBtnPersonalDocumentsRelatedClick() {
    this.$router.push('/my-ai/learning-center#learnedDocument');
  }

  onBtnSidebarFoldingClick(event) {
    event.preventDefault();
    console.log('onBtnSidebarFoldingClick', $(this));
    $('#btn-sidebar-folding').toggleClass('active');
    $('.connectome-home').toggleClass('active');
  }

  onBtnSidebarFoldingPersonalDocumentsClick(event) {
    event.preventDefault();
    $('#btn-sidebar-folding-personal-documents').toggleClass('active');
    if ($('#btn-sidebar-folding-personal-documents').hasClass('active')) {
      //$('.panel-sub').removeClass('active');
      this.panelSub = 'panel-sub';
      $('.panel-sub-personal-documents').addClass('active');
    } else {
      $('.panel-sub-personal-documents').removeClass('active');
      this.panelSub = 'panel-sub active';
      //$('.panel-sub').addClass('active');
    }
  }

  onBtnPanelSubCloseClick(event) {
    event.preventDefault();
    this.clearSelectedEntity();
    this.panelSub = 'panel-sub';
    //$('.panel-sub').removeClass('active');
  }

  isOrderByDate = true;
  rawSearchResult: Array<ConnectomeNetworkVertex> = [];
  searchResultOrderByDate: Array<ConnectomeNetworkVertex> = [];

  public onSetAsFavoriteClick() {
    if (this.title && this.connectomeId && this.userId) {
      this.toggleEntityFavorite({
        title: this.title,
      })
        .then(res => {
          if (!res) {
            return;
          }

          if (!res.data) {
            return;
          }

          const vertex = res.data;
          this.isFavorite = vertex.favorite;

          this.initSearchResult();
        })
        .catch(error => {
          this.showAddFavoriteFailedToast();
        });
    } else {
      this.showAddFavoriteFailedToast();
    }
  }

  public onHideClick() {
    if (this.title) {
      this.toggleEntityDisability({
        title: this.title,
      })
        .then(res => {
          console.log('res from map-side-bar', res);

          if (!res) {
            return;
          }

          if (!res.data) {
            return;
          }

          const response = res.data;
          const vertice = this.vertice(response.label);
          if (vertice && !vertice.disable) {
            this.setSelectedEntity({ title: vertice.label, srcLang: this.lang });
            if (this.title.localeCompare(vertice.label) == 0) {
              this.onEntitySelectedChanged(vertice.label);
            }
          } else {
            this.clearSelectedEntity();
          }
          this.refreshMap();
          this.filterTerms = null;
          this.initSearchResult();
        })
        .catch(error => {
          this.showAddFavoriteFailedToast();
        });
    } else {
      this.showAddFavoriteFailedToast();
    }
  }

  showAddFavoriteFailedToast() {
    this.$bvToast.toast("Keyword's update failed!", {
      toaster: 'b-toaster-bottom-right',
      title: 'Fail',
      variant: 'danger',
      solid: true,
      autoHideDelay: 5000,
    });
  }

  // SearchTab
  filterTerms: string = null;

  initSearchResult() {
    let entitiesMatched: Array<ConnectomeNetworkVertex> = [];
    entitiesMatched = this.vertices;
    this.setNewSearchResult({ searchResult: [] });
    this.searchResultOrderByDate = [];
    this.rawSearchResult = [];
    this.setSearchResultList(entitiesMatched);
  }

  onSearchInput(event) {
    //console.log('filterTerms', this.filterTerms);
    let entitiesMatched: Array<ConnectomeNetworkVertex> = [];
    if (event.target.value) {
      const regOption = new RegExp(event.target.value, 'ig');
      entitiesMatched = this.vertices.filter(vertex => vertex.label.match(regOption));
    } else {
      entitiesMatched = this.vertices;
    }
    this.setSearchResultList(entitiesMatched);
    //}
  }

  private setSearchResultList(entitiesMatched: Array<ConnectomeNetworkVertex>) {
    this.lastClusterRender = null;
    if (!entitiesMatched) {
      this.setNewSearchResult({ searchResult: [] });
      this.searchResultOrderByDate = [];
      this.rawSearchResult = [];
      return;
    }

    if (entitiesMatched.length > 0) {
      this.searchResultOrderByDate = entitiesMatched.map(x => x);
      this.rawSearchResult = entitiesMatched
        .map(x => x)
        .sort((a, b) => {
          const res =
            a.mainCluster.localeCompare(b.mainCluster) === 0
              ? a.entities.length < b.entities.length
                ? 1
                : -1
              : this.mainClusters.findIndex(elem => elem.name === b.mainCluster) -
                this.mainClusters.findIndex(elem => elem.name === a.mainCluster);
          return res;
        });
    } else {
      this.setNewSearchResult({ searchResult: [] });
      this.searchResultOrderByDate = [];
      this.rawSearchResult = [];
    }

    switch (this.searchResultFilterType) {
      case 'all':
        this.setNewSearchResult({
          searchResult: this.rawSearchResult.filter(item => item.type != TYPE_VERTEX.ROOT && item.type != TYPE_VERTEX.CLUSTER).map(x => x),
        });
        break;
      case 'visible':
        this.setNewSearchResult({
          searchResult: this.rawSearchResult
            .filter(item => item.type != TYPE_VERTEX.ROOT && item.type != TYPE_VERTEX.CLUSTER && !item.disable)
            .map(x => x),
        });
        break;
      case 'black-entity':
        this.setNewSearchResult({
          searchResult: this.rawSearchResult.filter(item => !item.disable && item.type === TYPE_VERTEX.BLACK_ENTITY).map(x => x),
        });
        break;
      case 'favorites':
        this.setNewSearchResult({
          searchResult: this.rawSearchResult
            .filter(item => item.type != TYPE_VERTEX.ROOT && item.type != TYPE_VERTEX.CLUSTER && item.favorite && !item.disable)
            .map(x => x),
        });
        break;
      case 'hidden':
        this.setNewSearchResult({
          searchResult: this.rawSearchResult
            .filter(item => item.type != TYPE_VERTEX.ROOT && item.type != TYPE_VERTEX.CLUSTER && item.disable)
            .map(x => x),
        });
        break;
      default:
        this.setNewSearchResult({
          searchResult: this.rawSearchResult
            .filter(item => item.type != TYPE_VERTEX.ROOT && item.type != TYPE_VERTEX.CLUSTER && !item.disable)
            .map(x => x),
        });
    }
  }

  setSearchResultFilterName(filterType: string) {
    switch (filterType) {
      case 'all':
        this.searchResultFilterLabel = this.$t('map-side-bar.entity-search-tab.result-all');
        this.setNewSearchResult({
          searchResult: this.rawSearchResult.filter(item => item.type != TYPE_VERTEX.ROOT && item.type != TYPE_VERTEX.CLUSTER).map(x => x),
        });
        return (this.searchResultFilterType = 'all');
      case 'visible':
        this.setNewSearchResult({
          searchResult: this.rawSearchResult
            .filter(item => item.type != TYPE_VERTEX.ROOT && item.type != TYPE_VERTEX.CLUSTER && !item.disable)
            .map(x => x),
        });
        this.searchResultFilterLabel = this.$t('map-side-bar.entity-search-tab.result-all-visible');
        return (this.searchResultFilterType = 'visible');
      case 'black-entity':
        this.setNewSearchResult({
          searchResult: this.rawSearchResult.filter(item => !item.disable && item.type === TYPE_VERTEX.BLACK_ENTITY).map(x => x),
        });
        this.searchResultFilterLabel = this.$t('map-side-bar.entity-search-tab.result-filter-by-black-entity');
        return (this.searchResultFilterType = 'black-entity');
      case 'favorites':
        this.setNewSearchResult({
          searchResult: this.rawSearchResult
            .filter(item => item.type != TYPE_VERTEX.ROOT && item.type != TYPE_VERTEX.CLUSTER && item.favorite && !item.disable)
            .map(x => x),
        });
        this.searchResultFilterLabel = this.$t('map-side-bar.entity-search-tab.result-filter-by-favorite');
        return (this.searchResultFilterType = 'favorites');

      case 'hidden':
        this.setNewSearchResult({
          searchResult: this.rawSearchResult
            .filter(item => item.type != TYPE_VERTEX.ROOT && item.type != TYPE_VERTEX.CLUSTER && item.disable)
            .map(x => x),
        });
        this.searchResultFilterLabel = this.$t('map-side-bar.entity-search-tab.result-filter-by-disable');
        return (this.searchResultFilterType = 'hidden');
      default:
        this.setNewSearchResult({
          searchResult: this.rawSearchResult
            .filter(item => item.type != TYPE_VERTEX.ROOT && item.type != TYPE_VERTEX.CLUSTER && !item.disable)
            .map(x => x),
        });
        this.searchResultFilterLabel = this.$t('map-side-bar.entity-search-tab.result-all-visible');
        return (this.searchResultFilterType = 'visible');
    }
  }

  public onOrderByLabel() {
    this.isOrderByDate = false;
    this.setNewSearchResult({ searchResult: this.rawSearchResult.map(x => x) });
  }

  public onOrderByDate() {
    this.isOrderByDate = true;
    this.setNewSearchResult({ searchResult: this.searchResultOrderByDate.map(x => x) });
  }

  public getEntityColorStyleByLabel(label: string) {
    if (!label) {
      return 'color:#00002f';
    }

    const vertex = this.vertice(label);
    if (!vertex) {
      return 'color:#00002f';
    }

    return 'color:' + this.verticeColorByCluster(vertex.mainCluster);
  }

  getEntityBackgroundColorStyleByLabel(label: string) {
    if (!label) {
      return 'background-color:#00002f';
    }

    const vertex = this.vertice(label);
    if (!vertex) {
      return 'background-color:#00002f';
    }

    return 'background-color:' + this.verticeColorByCluster(vertex.mainCluster);
  }

  lastClusterRender: string = null;
  getClusterBoxStyle(item: ConnectomeNetworkVertex, index: number) {
    if (!item) {
      return 'margint:0rem 1rem;color:#00002f;font-size:1rem';
    }

    const mainClusterColor = this.verticeColorByCluster(item.mainCluster);

    if (!mainClusterColor) {
      return 'margin:0rem 1rem;color:#00002f;font-size:1rem';
    }

    if (index === 0 || this.searchResult[index - 1].mainCluster.localeCompare(item.mainCluster) != 0) {
      //this.lastClusterRender = item.mainCluster;
      return 'margin:0rem 0.5rem;color:' + mainClusterColor + ';font-size:2rem';
    }
    //return 'background-color:' + mainClusterColor + ';' + 'color:' + mainClusterColor + ';margin-right: 0.5rem;';
    return 'margin:0rem 1rem;color:' + mainClusterColor + ';font-size:1rem';
  }

  searchResultFilterType = 'visible';
  searchResultFilterLabel = this.$t('map-side-bar.entity-search-tab.result-all-visible');

  getShortContent(content: string) {
    if (!content) {
      return ' ';
    }

    if (content.length < 80) {
      return content;
    }

    return content.substring(0, 79) + '...';
  }

  convertGMTToLocalTime(dateToConvert: string) {
    if (!dateToConvert) {
      return '';
    }

    const date = new Date(dateToConvert);

    return date.toLocaleString();
  }
}
