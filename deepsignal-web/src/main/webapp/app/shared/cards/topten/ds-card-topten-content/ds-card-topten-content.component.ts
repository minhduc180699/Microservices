import { Component, Vue, Prop, Inject, Watch } from 'vue-property-decorator';
import DsCardFooter from '@/shared/cards/footer/ds-card-footer.vue';
import PeopleService from '@/core/home/people/people.service';
import { TopTenModel } from '@/shared/cards/topten/topten.model';
import { CARD_TYPE } from '@/shared/constants/feed-constants';
import { namespace } from 'vuex-class';
const cardState = namespace('cardStore');
const networkStore = namespace('connectomeNetworkStore');

@Component({
  components: {
    'ds-card-footer': DsCardFooter,
  },
})
export default class DsCardTopTenContent extends Vue {
  @Prop(Object) readonly item: any | undefined;
  @Inject('peopleService')
  private peopleService: () => PeopleService;
  topTenInfos: TopTenModel[] = [];
  private isMoveToPeople = true;
  @cardState.Action
  public changeCompaniesOrPeoples!: (payload: { companiesOrPeoples: any }) => void;
  @networkStore.Getter
  public labelHidden;

  mounted(): void {
    if (this.item && this.item.peoples) {
      const titles = this.item.peoples.map(item => item.title);
      this.getCompanyOfPeople(titles);
    }
  }

  @Watch('labelHidden')
  onLabelHiddenChangeOnTopTen(value) {
    if (this.item && this.item.peoples) {
      this.topTenInfos = [];
      const content = this.item.peoples.filter(item => item.title.toLowerCase() != value.toLowerCase());
      const titles = content.map(item => item.title);
      this.getCompanyOfPeople(titles);
    }
  }

  getCompanyOfPeople(titles: []) {
    if (!titles || titles.length == 0) {
      return;
    }
    const req = {
      getOnlyImages: false,
      titles: titles,
      sourceLang: this.$store.getters.currentLanguage.toUpperCase(),
    };
    this.peopleService()
      .getPeopleAndCompany(req)
      .then(res => {
        if (!res || !res.data) {
          this.$root.$emit('delete-card-issue', CARD_TYPE.TOP_10);
          this.$root.$emit('delete-card-issue', CARD_TYPE.STOCK);
          return;
        }
        if (res.data.length <= 2) {
          this.$root.$emit('delete-card-issue', CARD_TYPE.TOP_10, true);
        }
        this.processData(res.data);
        this.item.companies = res.data; // append companies to item to process company card
        // @ts-ignore
        this.changeCompaniesOrPeoples({ companiesOrPeoples: res.data });
        // this.$store.commit('setCompanies', res.data);
      });
  }

  processData(datas) {
    if (!datas) {
      return;
    }
    for (const data of datas.slice(0, 5)) {
      // Show only the first 5 items
      const topTen = new TopTenModel();
      topTen.imageUrl =
        data.imageUrl || (data.wikipediaInfobox && data.wikipediaInfobox.image) || (data.googleInfobox && data.googleInfobox.logo);
      topTen.label = data.label || data.googleInfobox.title;
      topTen.website = data.googleInfobox ? data.googleInfobox.website || data.wikiUrl : data.wikiUrl;
      topTen.info = data.googleInfobox
        ? data.googleInfobox.founder || data.wikipediaInfobox.founder || data.googleInfobox.founders || data.googleInfobox.subtitle
        : data.wikipediaInfobox.founder;
      this.topTenInfos.push(topTen);
    }
  }

  goToPeoplePage() {
    this.isMoveToPeople = false;
  }

  handleMoveToPeople(index, label) {
    if (!this.isMoveToPeople) {
      return;
    }
    this.$router.push({ name: 'People', hash: '#people:' + label });
  }

  goToPeopleWebsite(link) {
    this.isMoveToPeople = false;
    window.open(link, '_blank');
    setTimeout(() => {
      this.isMoveToPeople = true;
    }, 500);
  }
}
