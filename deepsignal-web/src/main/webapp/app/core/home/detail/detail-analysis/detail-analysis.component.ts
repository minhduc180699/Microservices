import { Component, Prop, Vue } from 'vue-property-decorator';
import SignalRelationshipDiagramAnalysisComponent from '@/core/home/signals/signal-today-issue/signal-relationship-diagram-analysis/signal-relationship-diagram-analysis.vue';
import SignalWordCloudChart from '@/core/home/signals/signal-today-issue/signal-word-cloud-chart/signal-word-cloud-chart.vue';
import SignalPeopleCompanyChart from '@/core/home/signals/signal-today-issue/signal-people-company-chart/signal-people-company-chart.vue';
import MiniMap2dNetwork from '@/entities/my-ai/mini-connectome/mini-map-2d-network/mini-map-2d-network.vue';

@Component({
  components: {
    'signal-relationship-diagram-analysis': SignalRelationshipDiagramAnalysisComponent,
    'signal-word-cloud': SignalWordCloudChart,
    'signal-people-company-chart': SignalPeopleCompanyChart,
    'mini-connectome-map': MiniMap2dNetwork,
  },
})
export default class detailAnalysis extends Vue {
  @Prop(Object) readonly chartData: any;
  @Prop(Object) readonly signalKeyword: any;
  miniConnectomeDisplayed = true;
  mounted() {
    this.miniConnectomeDisplayed = true;
  }
  onMiniConnectomeDisplayed(isDisplayed: boolean) {
    this.miniConnectomeDisplayed = isDisplayed;
  }
}
