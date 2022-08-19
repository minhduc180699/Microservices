import ConnectomeService from '@/entities/connectome/connectome.service';
import { PrincipalService } from '@/service/principal.service';
import { TYPE_VERTEX } from '@/shared/constants/ds-constants';
import { ConnectomeNetworkVertex } from '@/shared/model/connectome-network-vertex.model';
import { ConnectomeNetwork } from '@/shared/model/connectome-network.model';
import axios from 'axios';
import { watch } from 'fs';
import { userInfo } from 'os';
import { stringify } from 'querystring';
import { Component, Inject, Vue, Watch } from 'vue-property-decorator';
import { namespace } from 'vuex-class';

const networkStore = namespace('connectomeNetworkStore');
const mapNetworkStore = namespace('mapNetworkStore');
const miniNetworkStore = namespace('miniConnectomeNetworkStore');
const miniMapNetworkStore = namespace('miniMapNetworkStore');

@Component
export default class MiniMapBottomBar extends Vue {
  @Inject('connectomeService')
  private connectomeService: () => ConnectomeService;

  @Inject('principalService')
  private principalService: () => PrincipalService;

  @networkStore.Getter
  public lastUpdatedAt!: string;

  @networkStore.Getter
  public lang!: string;

  @networkStore.Getter
  public verticeColorByCluster!: (mainCluster: string) => d3.RGBColor;

  @networkStore.Getter
  public vertice!: (label: string) => ConnectomeNetworkVertex;

  @miniNetworkStore.Getter
  public miniNetworkData!: ConnectomeNetwork;

  @miniNetworkStore.Getter
  public miniVertices!: Array<ConnectomeNetworkVertex>;

  @miniNetworkStore.Getter
  public miniVertice!: (label: string) => ConnectomeNetworkVertex;

  @miniNetworkStore.Getter
  public miniListDisableVerticeLabels!: Set<string>;

  @miniMapNetworkStore.Getter
  public entityLabelSelected!: string;

  @miniMapNetworkStore.Getter
  public entityDetailsSelected!: any;

  @mapNetworkStore.Action
  public externalSetSelectedEntityOnMainConnectome!: (payload: { title: string; srcLang: string }) => void;

  @networkStore.Action
  public toggleEntityDisability!: (payload: { title: string }) => Promise<any>;

  @networkStore.Action
  public toggleEntityFavorite!: (payload: { title: string }) => Promise<any>;

  @miniNetworkStore.Mutation
  public setEntityFavorite!: (vertex: ConnectomeNetworkVertex) => void;

  @miniNetworkStore.Mutation
  public setEntityDisable!: (vertex: ConnectomeNetworkVertex) => void;

  @miniMapNetworkStore.Action
  public setSelectedEntity!: (payload: { title: string; srcLang: string }) => void;

  @miniMapNetworkStore.Action
  public getEntityDetails!: (payload: { title: string; srcLang: string }) => void;

  @miniMapNetworkStore.Action
  public clearSelectedEntity!: () => void;

  @miniMapNetworkStore.Action
  public clearDetails!: () => void;

  @miniMapNetworkStore.Action
  public setNewSearchResult!: (payload: { searchResult: Array<ConnectomeNetworkVertex> }) => void;

  @miniMapNetworkStore.Action
  public refreshMap!: () => void;

  @miniMapNetworkStore.Action
  public requestCenterAtSelection!: () => void;

  data(): {
    isWikipediaInfoboxExpanded: boolean;
  } {
    return {
      isWikipediaInfoboxExpanded: false,
    };
  }

  connectomeId: string = null;
  userId: string = null;

  mounted() {
    const userId = this.principalService().getUserInfo().id;
    const connectomeId = this.principalService().getConnectomeInfo().connectomeId;
    this.connectomeId = connectomeId;
    this.userId = userId;
    this.clearSelectedEntity();
  }

  @Watch('lastUpdatedAt')
  onlastUpdatedAtUpdated(lastUpdateDate: string) {}

  @Watch('miniVertices')
  onVerticesUpdated(data: Array<ConnectomeNetworkVertex>) {
    //console.log('onVerticesUpdated', data);
    if (this.entityLabelSelected) {
      console.log('update on vertices updated event');
      this.onEntitySelectedChanged(this.entityLabelSelected);
    }
  }

  @Watch('entityLabelSelected')
  onEntitySelectedChanged(label: string) {
    this.title = label;
    if (!label) {
      this.isDetailsPanelExist = false;
      this.mainConnectomeNode = null;
      this.snsFacebook = null;
      this.snsInstagram = null;
      this.snsTwitter = null;
      this.snsYoutube = null;
      this.snsLinkedIn = null;
      return;
    }

    //this.getEntityDetails({ title: label, srcLang: this.lang });
    this.isDetailsPanelExist = true;
    //console.log('data after ', this.store.getters['connectomeNetworkStore/data']);
    console.log('Mini MapSideBar saw entity selected changed', label);

    this.mainConnectomeNode = this.vertice(label);

    if (!this.mainConnectomeNode || this.mainConnectomeNode.type === TYPE_VERTEX.BLACK_ENTITY) {
      this.clearDetails();
    } else {
      this.getEntityDetails({ title: label, srcLang: this.lang });
    }

    if (this.mainConnectomeNode) {
      this.isFavorite = this.mainConnectomeNode.favorite ? true : false;
      this.isDisable = this.mainConnectomeNode.disable ? true : false;
    }
  }

  @Watch('entityDetailsSelected')
  onEntityDetailsChanged(details: any) {
    console.log('Mini MapSideBar saw entity details changed', details);
    if (!details) {
      this.peopleTabToggleClass = 'btn btn-icon btn-toggle off';
      this.snsFacebook = null;
      this.snsInstagram = null;
      this.snsTwitter = null;
      this.snsYoutube = null;
      this.snsLinkedIn = null;
      return;
    }
    this.hasSNS = false;
    if (details.facebook) {
      this.snsFacebook = details.facebook;
      this.hasSNS = true;
    } else {
      this.snsFacebook = null;
    }
    if (details.instagram) {
      this.snsInstagram = details.instagram;
      this.hasSNS = true;
    } else {
      this.snsInstagram = null;
    }
    if (details.twitter) {
      this.snsTwitter = details.twitter;
      this.hasSNS = true;
    } else {
      this.snsTwitter = null;
    }
    if (details.youtube) {
      this.snsYoutube = details.youtube;
      this.hasSNS = true;
    } else {
      this.snsYoutube = null;
    }
    if (details.linkedin) {
      this.snsLinkedIn = details.linkedin;
      this.hasSNS = true;
    } else {
      this.snsLinkedIn = null;
    }

    if (this.mainConnectomeNode && this.hasSNS) {
      this.peopleTabToggleClass = 'btn btn-icon btn-toggle';
    }
  }

  isDetailsPanelExist = false;
  title: string = null;
  isFavorite = false;
  isDisable = false;
  snsFacebook: string = null;
  snsInstagram: string = null;
  snsTwitter: string = null;
  snsYoutube: string = null;
  snsLinkedIn: string = null;
  hasSNS = false;

  mainConnectomeNode: ConnectomeNetworkVertex = null;
  connectomeToggleClass = 'btn btn-icon btn-toggle off';
  connectomeToggleToolTipText = this.$t('map-side-bar.details-tab.go-to-connectome');
  peopleTabToggleClass = 'btn btn-icon btn-toggle off';
  peopleTabToggleToolTipText = this.$t('map-side-bar.details-tab.go-to-people');
  favoriteToggleClass = 'btn btn-icon btn-toggle off';
  favoriteToggleToolTipText = this.$t('map-side-bar.details-tab.set-as-favorite');
  disableToggleClass = 'btn btn-icon btn-toggle off';
  disableToggleToolTipText = this.$t('map-side-bar.details-tab.hide');

  @Watch('mainConnectomeNode')
  onMainConnectomeNodeChanged(value: ConnectomeNetworkVertex) {
    this.favoriteToggleClass = 'btn btn-icon btn-toggle off';
    this.disableToggleClass = 'btn btn-icon btn-toggle off';
    this.connectomeToggleClass = 'btn btn-icon btn-toggle off';

    if (!value) {
      return;
    }

    this.connectomeToggleClass = 'btn btn-icon btn-toggle';
    if (value.favorite) {
      this.favoriteToggleClass = 'btn btn-icon btn-toggle active';
      this.favoriteToggleToolTipText = this.$t('map-side-bar.details-tab.unset-as-favorite');
    } else {
      this.favoriteToggleClass = 'btn btn-icon btn-toggle';
      this.favoriteToggleToolTipText = this.$t('map-side-bar.details-tab.set-as-favorite');
    }

    if (value.disable) {
      this.disableToggleClass = 'btn btn-icon btn-toggle active';
      this.disableToggleToolTipText = this.$t('map-side-bar.details-tab.display');
    } else {
      this.disableToggleClass = 'btn btn-icon btn-toggle';
      this.disableToggleToolTipText = this.$t('map-side-bar.details-tab.hide');
    }
  }
  centerEntitySelectedTo(node: string) {
    if (!node) {
      return;
    }
    this.requestCenterAtSelection();
  }

  onFeedsClick() {
    this.externalSetSelectedEntityOnMainConnectome({ title: this.title, srcLang: this.lang });
    this.$router.push('/feed');
  }

  onConnectomeClick() {
    if (!this.mainConnectomeNode) {
      return;
    }

    this.externalSetSelectedEntityOnMainConnectome({ title: this.title, srcLang: this.lang });
    this.$router.push('/my-ai/connectome');
  }

  onPeopleClick() {
    if (!this.mainConnectomeNode) {
      return;
    }

    if (!this.hasSNS) {
      return;
    }
    this.$router.push({ name: 'People', hash: '#people:' + this.title });
  }

  onLearnMoreClick() {
    this.externalSetSelectedEntityOnMainConnectome({ title: this.title, srcLang: this.lang });
    this.$router.push('/my-ai/learning-center');
  }

  onBtnPersonalDocumentsRelatedClick() {
    this.$router.push('/my-ai/learning-center#learnedDocument');
  }

  public onSetAsFavoriteClick() {
    if (!this.mainConnectomeNode) {
      return;
    }

    if (this.title) {
      this.toggleEntityFavorite({
        title: this.title,
      })
        .then(res => {
          if (!res) {
            this.mainConnectomeNode = null;
            return;
          }

          if (!res.data) {
            this.mainConnectomeNode = null;
            return;
          }

          const vertex = res.data;
          this.mainConnectomeNode = vertex;
          this.setEntityFavorite(vertex);
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

          const vertex = res.data;
          if (vertex && !vertex.disable) {
            this.setSelectedEntity({ title: vertex.label, srcLang: this.lang });
            if (this.title.localeCompare(vertex.label) == 0) {
              this.onEntitySelectedChanged(vertex.label);
            }
          } else {
            this.clearSelectedEntity();
          }

          this.setEntityDisable(vertex);
          //this.$store.dispatch('connectomeNetworkStore/getConnectomeData', { connectomeId: this.connectomeId, language: this.lang });
          //this.refreshMap();
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
