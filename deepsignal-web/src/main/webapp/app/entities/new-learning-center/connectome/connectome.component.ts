import NodesMap from '@/entities/my-ai/connectome/nodes-map.vue';
import { Component, Vue } from 'vue-property-decorator';

@Component({
  components: {
    chart: NodesMap,
  },
})
export default class Connectome extends Vue {}
