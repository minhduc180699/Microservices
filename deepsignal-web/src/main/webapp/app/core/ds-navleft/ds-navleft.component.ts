import { Component, Vue } from 'vue-property-decorator';
import { BSidebar } from 'bootstrap-vue';

@Component({
  components: {
    'b-sidebar': BSidebar,
  },
})
export default class DsNavleft extends Vue {
  private isActive = false;

  public get authenticated(): boolean {
    return this.$store.getters.authenticated;
  }
  public get connectome(): any {
    return JSON.parse(localStorage.getItem('ds-connectome'));
  }

  logout() {
    // @ts-ignore
    this.$parent.logout();
  }
}
