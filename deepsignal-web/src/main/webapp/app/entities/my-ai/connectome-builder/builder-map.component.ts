import { PrincipalService } from '@/service/principal.service';
import { Component, Inject, Vue } from 'vue-property-decorator';
import { namespace } from 'vuex-class';

const networkStore = namespace('connectomeNetworkStore');
@Component({
  components: {},
})
export default class BuilderMapComponent extends Vue {
  connectomeId: string = null;
  userId: string = null;

  @Inject('principalService')
  private principalService: () => PrincipalService;

  fullscreen = false;

  mounted() {
    const userId = this.principalService().getUserInfo().id;
    const connectomeId = this.principalService().getConnectomeInfo().connectomeId;
    if (!userId || !connectomeId) {
      return;
    }
    this.connectomeId = connectomeId;
    this.userId = userId;
  }
}
