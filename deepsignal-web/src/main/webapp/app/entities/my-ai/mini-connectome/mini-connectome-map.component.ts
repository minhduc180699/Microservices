import { PrincipalService } from '@/service/principal.service';
import { Component, Inject, Vue, Watch } from 'vue-property-decorator';
import { Route } from 'vue-router';
import { namespace } from 'vuex-class';
import MiniMapBottomBar from './mini-map-bottom-bar/mini-map-bottom-bar.vue';

@Component({
  components: {
    'mini-map-bottom-bar': MiniMapBottomBar,
  },
})
export default class MiniConnectomeMapComponent extends Vue {
  // @Watch('$route', { immediate: true, deep: true })
  // onUrlChange(newVal: Route) {
  //   console.log('MiniConnectomeMapComponent',newVal);
  // }

  mounted() {}
}
