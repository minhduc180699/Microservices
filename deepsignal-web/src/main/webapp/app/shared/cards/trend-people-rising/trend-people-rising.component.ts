import { ConnectomeNetworkVertex } from '@/shared/model/connectome-network-vertex.model';
import { Component, Prop, Vue, Watch } from 'vue-property-decorator';
import { namespace } from 'vuex-class';
const mapNetworkStore = namespace('mapNetworkStore');
const networkStore = namespace('connectomeNetworkStore');

@Component
export default class trendPeopleRising extends Vue {
  @Prop(Object) readonly item: any | undefined;

  @networkStore.Getter
  public vertices!: Array<ConnectomeNetworkVertex>;

  @networkStore.Getter
  public vertice!: (label: string) => ConnectomeNetworkVertex;

  @mapNetworkStore.Action
  public setSelectedEntity!: (payload: { title: string; srcLang: string }) => void;

  @mapNetworkStore.Action
  public clearSelectedEntity!: () => void;

  @networkStore.Getter
  public lang!: string;

  @networkStore.Action
  public toggleEntityFavorite!: (payload: { title: string }) => Promise<any>;

  @networkStore.Action
  public toggleEntityDisability!: (payload: { title: string }) => Promise<any>;

  mounted() {
    if (this.item && this.vertices && this.vertices.length > 0) {
      this.mainConnectomeNode = this.vertice(this.item.label);
    }
  }

  @Watch('item')
  detectItemChange(data) {
    if (data && this.vertices && this.vertices.length > 0) {
      this.mainConnectomeNode = this.vertice(this.item.label);
    }
  }

  @Watch('vertices')
  onVerticesUpdated(data: Array<ConnectomeNetworkVertex>) {
    if (this.item && this.item.label) {
      //this.onEntitySelectedChanged(this.entityLabelSelected);
      this.mainConnectomeNode = this.vertice(this.item.label);
    }
  }

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
    this.peopleTabToggleClass = 'btn btn-icon btn-toggle off';
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
      this.peopleTabToggleClass = 'btn btn-icon btn-toggle off';
    } else {
      this.disableToggleClass = 'btn btn-icon btn-toggle';
      this.disableToggleToolTipText = this.$t('map-side-bar.details-tab.hide');
      this.peopleTabToggleClass = 'btn btn-icon btn-toggle';
    }
  }

  searchInFeed(label) {
    this.clearSelectedEntity();
    const connectomeLang = this.$store.getters['connectomeNetworkStore/lang'];
    this.setSelectedEntity({ title: label, srcLang: connectomeLang });
    this.$router.push({ path: '/feed' });
  }

  goToPeople(people) {
    if (!people || people == '') {
      return;
    }
    this.$router.push({ name: 'People', hash: '#people:' + people });
  }

  goToConnectome(node) {
    if (!this.mainConnectomeNode) {
      return;
    }
    this.setSelectedEntity({ title: node, srcLang: this.lang });
    this.$router.push('/my-ai/connectome');
  }

  searchInMyAI(label) {
    this.clearSelectedEntity();
    const connectomeLang = this.$store.getters['connectomeNetworkStore/lang'];
    this.setSelectedEntity({ title: label, srcLang: connectomeLang });
    this.$router.push({ path: '/my-ai/learning-center' });
  }

  onSetAsFavoriteClick(label) {
    if (!this.mainConnectomeNode) {
      return;
    }

    this.toggleEntityFavorite({
      title: label,
    }).then(res => {
      if (!res) {
        return;
      }

      if (!res.data) {
        return;
      }

      this.mainConnectomeNode = res.data;
    });
  }

  onHideClick(label) {
    if (!this.mainConnectomeNode) {
      return;
    }
    // @ts-ignore
    this.item.isHidden = true;
    this.toggleEntityDisability({
      title: label,
    }).then(res => {
      if (!res) {
        return;
      }

      if (!res.data) {
        return;
      }

      this.mainConnectomeNode = res.data;
    });
  }
}
