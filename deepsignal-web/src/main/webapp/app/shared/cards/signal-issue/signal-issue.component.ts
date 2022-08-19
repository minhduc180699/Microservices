import { Component, Vue, Prop, Watch } from 'vue-property-decorator';
import SignalTodayIssueCommonComponent from '@/core/home/signals/signal-today-issue/signal-today-issue-common/signal-today-issue-common.vue';
import moment from 'moment';

@Component({
  components: {
    'signal-today-issue-common': SignalTodayIssueCommonComponent,
  },
})
export default class DsCardSignalIssue extends Vue {
  @Prop(Object) readonly item: any | undefined;
  @Prop(Array) readonly clusterDocuments: any | undefined;
  workDay = moment().format('yyyy-MM-DD');

  @Watch('clusterDocuments')
  onItemChange(newValue) {
    if (newValue.length > 0) {
      // @ts-ignore
      this.$refs.cardSignalTodayIssueCommon.init(newValue);
    }
  }

  mounted(): void {
    if (this.item.clusterDocuments && this.item.clusterDocuments.length > 0) {
      // mutable object cardSignalIssue in feed
      // @ts-ignore
      this.$refs.cardSignalTodayIssueCommon.init(this.item.clusterDocuments);
    }
  }

  toSignal() {
    this.$router.push({ name: 'Signals' });
  }
}
