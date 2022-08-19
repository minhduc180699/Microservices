import { Component, Prop, Vue } from 'vue-property-decorator';
import { PAGE } from '@/shared/constants/ds-constants';

@Component
export default class dsSidebar extends Vue {
  @Prop(Boolean) readonly sidebarActive: any | false;
  private page = { name: '', link: '' };

  logout() {
    this.$emit('openModalLogout');
  }

  closeSidebar() {
    this.$emit('closeSidebar');
  }

  changePage(page) {
    switch (page) {
      case 1:
        this.page = Object.assign(PAGE.FEED);
        break;
      case 2:
        this.page = Object.assign(PAGE.PEOPLE);
        break;
      case 3:
        this.page = Object.assign(PAGE.SIGNALS);
        break;
      case 5:
        this.page = Object.assign(PAGE.CONNECTOME);
        break;
      case 6:
        this.page = Object.assign(PAGE.MYAI);
        break;
      default:
        this.page = Object.assign(PAGE.FEED);
        break;
    }
    this.$emit('changePage', this.page);
  }
}
