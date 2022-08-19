import { Component, Prop, Vue, Watch } from 'vue-property-decorator';
import vueCustomScrollbar from 'vue-custom-scrollbar';
import { SocialPeople } from '@/shared/cards/people/people-ranking/people-ranking.model';

@Component({
  components: {
    vueCustomScrollbar,
  },
})
export default class DsCardPeopleSocial extends Vue {
  @Prop(Object) readonly peopleOrCompany: any;
  currentServiceType = 0;
  serviceTypes: any[] = [];
  socialPeoplesLoad: SocialPeople[] = [];
  isShow = true;

  scrollSettings = {
    wheelPropagation: false,
    suppressScrollX: false,
    wheelSpeed: 0.5,
  };

  @Watch('peopleOrCompany')
  detectPeopleCarouselChange() {
    this.getServiceTypes();
    this.choseServiceType(this.serviceTypes.length > 0 ? this.serviceTypes[0].serviceType : null);
  }

  getServiceTypes() {
    const seen = new Set();
    this.serviceTypes = this.peopleOrCompany.social
      ?.map(item => ({ serviceType: item.serviceType, favicon: item.favicon }))
      .filter(el => {
        const duplicate = seen.has(el.serviceType);
        seen.add(el.serviceType);
        return !duplicate;
      });
  }

  choseServiceType(serviceType) {
    if (this.peopleOrCompany.social?.length == 0) {
      this.socialPeoplesLoad = [];
      this.isShow = false;
      return;
    }
    this.isShow = true;
    if (serviceType)
      this.socialPeoplesLoad = this.peopleOrCompany.social
        ?.filter(item => {
          return item.serviceType === serviceType;
        })
        .slice(0, 20);
  }

  scrollHandle(e) {
    const target = e.target;
    if (target) {
      if (target.scrollTop + target.clientHeight >= target.scrollHeight) {
        // console.log('scroll');
      }
    }
  }
}
