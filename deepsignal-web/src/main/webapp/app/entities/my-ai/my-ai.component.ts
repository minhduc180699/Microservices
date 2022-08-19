import { Component, Inject, Vue } from 'vue-property-decorator';
import aside from '@/entities/my-ai/aside/main-aside.vue';
import { namespace } from 'vuex-class';
import { PrincipalService } from '@/service/principal.service';

@Component({
  name: 'my-ai',
  components: {
    asideMyAi: aside,
  },
})
export default class MyAI extends Vue {
  connectomeId: string = null;
  userId: string = null;

  @Inject('principalService')
  private principalService: () => PrincipalService;

  mounted() {
    // document.body.setAttribute('data-menu', 'connectome');

    const userId = this.principalService().getUserInfo().id;
    const connectomeId = this.principalService().getConnectomeInfo().connectomeId;
    if (!userId || !connectomeId) {
      return;
    }
    this.connectomeId = connectomeId;
    this.userId = userId;
  }
}
