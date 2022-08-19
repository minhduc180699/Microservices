import { Component, Vue, Prop, Watch, Inject } from 'vue-property-decorator';

import { PeopleAndCompanyModel } from '@/shared/cards/people/people-ranking/people-ranking.model';
import { namespace } from 'vuex-class';
import { ConnectomeNetworkVertex } from '@/shared/model/connectome-network-vertex.model';
import PeopleService from '@/core/home/people/people.service';
import { PrincipalService } from '@/service/principal.service';
const networkStore = namespace('connectomeNetworkStore');
const mapNetworkStore = namespace('mapNetworkStore');
@Component({
  components: {},
})
export default class DsCardTrendPeople extends Vue {
  @Prop(Object) readonly item: any | undefined;
  peopleTrend = new PeopleAndCompanyModel();

  @networkStore.Getter
  public vertices!: Array<ConnectomeNetworkVertex>;

  @networkStore.Getter
  public vertice!: (label: string) => ConnectomeNetworkVertex;

  @mapNetworkStore.Action
  public setSelectedEntity!: (payload: { title: string; srcLang: string }) => void;

  @mapNetworkStore.Action
  public clearSelectedEntity!: () => void;

  @networkStore.Action
  public toggleEntityFavorite!: (payload: { title: string }) => Promise<any>;

  @networkStore.Action
  public toggleEntityDisability!: (payload: { title: string }) => Promise<any>;

  @networkStore.Getter
  public lang!: string;
  @Inject('peopleService')
  private peopleService: () => PeopleService;
  @Inject('principalService') private principalService: () => PrincipalService;

  data() {
    return {
      // see more options in: https://swiperjs.com/get-started#swiper-css-styles-size
      swiperOptions: {
        slidesPerView: 'auto',
        spaceBetween: 8,
        slidesOffsetBefore: 20,
        slidesOffsetAfter: 20,
      },
    };
  }
  private connectomeId;

  created(): void {
    this.connectomeId = JSON.parse(localStorage.getItem('ds-connectome')).connectomeId;
    if (this.item) {
      this.peopleTrend = new PeopleAndCompanyModel(this.item);
    }

    if (this.vertices && this.vertices.length > 0) {
      this.mainConnectomeNode = this.vertice(this.peopleTrend.label);
    }
    // this.connectomeId = this.principalService().getConnectomeInfo().connectomeId;
  }

  @Watch('item')
  detectItemChange(data) {
    this.peopleTrend = new PeopleAndCompanyModel(data);
    if (this.vertices && this.vertices.length > 0) {
      this.mainConnectomeNode = this.vertice(this.peopleTrend.label);
    }
  }

  @Watch('vertices')
  onVerticesUpdated(data: Array<ConnectomeNetworkVertex>) {
    if (this.peopleTrend) {
      //this.onEntitySelectedChanged(this.entityLabelSelected);
      this.mainConnectomeNode = this.vertice(this.peopleTrend.label);
    }
  }

  @Watch('$route', { immediate: true, deep: true })
  onUrlChange(newVal) {
    this.isLinkToPeopleHidden = false;
    if (newVal.name.localeCompare('People') == 0) {
      this.isLinkToPeopleHidden = true;
    }
  }

  isLinkToPeopleHidden = false;
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
    this.peopleTabToggleClass = 'btn btn-icon btn-toggle';
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
  searchInFeed(label) {
    this.clearSelectedEntity();
    const connectomeLang = this.$store.getters['connectomeNetworkStore/lang'];
    this.setSelectedEntity({ title: label, srcLang: connectomeLang });
    this.$router.push({ path: '/feed' });
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

  onHideClick(people) {
    if (!this.mainConnectomeNode) {
      return;
    }
    this.hiddenPeople(people, !this.mainConnectomeNode.disable);
    const label = people.label;
    // @ts-ignore
    this.peopleTrend.isHidden = true;
    this.toggleEntityDisability({
      title: label,
    }).then(res => {
      if (!res) {
        return;
      }

      if (!res.data) {
        return;
      }

      if (res.data.label.localeCompare(this.peopleTrend.label) == 0) {
        this.mainConnectomeNode = res.data;
      }
    });
  }

  goToConnectome(label) {
    if (!this.mainConnectomeNode) {
      return;
    }

    this.setSelectedEntity({ title: label, srcLang: this.lang });
    this.$router.push('/my-ai/connectome');
  }

  goToPeople(people) {
    if (!people || people == '') {
      return;
    }
    this.$router.push({ name: 'People', hash: '#people:' + people });
  }

  hiddenPeople(people, deleted) {
    const title = people.label;
    const type = people.type;
    const lang = localStorage.getItem('currentLanguage').toLowerCase();

    this.peopleService().hiddenPeople(this.connectomeId, title, type, lang, deleted).then();
  }
}
