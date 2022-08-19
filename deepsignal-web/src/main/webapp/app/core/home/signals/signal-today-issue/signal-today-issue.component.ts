import { Component, Inject, Vue, Watch, Prop } from 'vue-property-decorator';
import vueCustomScrollbar from 'vue-custom-scrollbar';
import { SignalService } from '@/service/signal.service';
import PeopleService from '@/core/home/people/people.service';
import SignalWordCloudChart from '@/core/home/signals/signal-today-issue/signal-word-cloud-chart/signal-word-cloud-chart.vue';
import { PrincipalService } from '@/service/principal.service';
import moment from 'moment';
import _ from 'lodash';
import SignalPeopleCompanyChart from '@/core/home/signals/signal-today-issue/signal-people-company-chart/signal-people-company-chart.vue';
import SignalRelationshipDiagramAnalysisComponent from '@/core/home/signals/signal-today-issue/signal-relationship-diagram-analysis/signal-relationship-diagram-analysis.vue';
import { SIGNAL_KEYWORD_TYPE } from '@/core/home/signals/signal-tracking-issue/signal-keyword.model';
import SignalTodayIssueCommonComponent from '@/core/home/signals/signal-today-issue/signal-today-issue-common/signal-today-issue-common.vue';

@Component({
  components: {
    vueCustomScrollbar,
    'signal-word-cloud-chart': SignalWordCloudChart,
    'signal-people-company-chart': SignalPeopleCompanyChart,
    'signal-relationship-diagram-analysis': SignalRelationshipDiagramAnalysisComponent,
    'signal-today-issue-common': SignalTodayIssueCommonComponent,
  },
})
export default class SignalTodayIssueComponent extends Vue {
  // private netChart = null;
  // private nodeDepthList: any[] = ['d1']; //d1(default::depth1), d2
  // private isdepth2On = false;
  // private infoElementVisible = false;

  @Prop(String) readonly workDay: string;

  private scrollSettings = {
    wheelPropagation: false,
    suppressScrollX: false,
    wheelSpeed: 0.5,
  };

  @Inject('peopleService')
  private peopleService: () => PeopleService;

  @Inject('signalService')
  private signalService: () => SignalService;
  @Inject('principalService') private principalService: () => PrincipalService;

  pageable = {
    page: 1,
    size: 10,
    sort: ['displayOrder,asc'],
  };
  signals = [];
  totalElements = 0;
  totalPages = 0;
  currentSignal = {};
  firstClusterDocument = {};
  clusterDocuments = [];
  clusterDocumentsCloneDeep = [];
  isLoadingSignal = true;
  private isNoData = false;

  @Watch('$route.hash')
  change(hash) {
    if (hash && this.signals.length > 0) {
      this.choseSignal(Number(hash.split('#')[1]) - 1);
    }
  }

  @Watch('workDay')
  changeWorkday() {
    this.clearData();
    this.paging();
  }

  mounted(): void {
    this.paging();
  }

  paging() {
    this.isNoData = false;
    const hashIndex = this.$route.hash.split('#')[1] || 1;
    this.signalService()
      .paging(this.principalService().getConnectomeInfo().connectomeId, this.workDay, SIGNAL_KEYWORD_TYPE.TI, this.pageable)
      .then(res => {
        if (!res || !res.data || res.data.content.length == 0) {
          this.isNoData = true;
          return;
        }
        this.signals = [...this.signals, ...res.data.content];
        this.isLoadingSignal = false;
        this.totalElements = res.data.totalElements;
        this.totalPages = res.data.totalPages;
        this.choseSignal(Number(hashIndex) - 1);
        // this.initNetChart();
      });
  }

  changeHash(hash) {
    window.location.hash = hash;
  }

  choseSignal(index) {
    if (!this.signals || this.signals.length == 0) {
      return;
    }
    this.currentSignal = this.signals[index];
    // @ts-ignore
    this.$refs.signalTodayIssueCommon.init(this.currentSignal['clusterDocuments']);
    // this.initNetChart();
  }

  viewMore() {
    this.pageable.page += 1;
    this.paging();
  }

  clearData() {
    this.signals = [];
    this.totalElements = 0;
    this.totalPages = 0;
    this.currentSignal = {};
    this.firstClusterDocument = {};
    this.clusterDocuments = [];
    this.clusterDocumentsCloneDeep = [];
    this.isLoadingSignal = true;
    window.location.hash = '';
    // @ts-ignore
    this.$refs.signalTodayIssueCommon.clearData();
  }

  learningKeyword() {
    this.$router.push('/my-ai/learning-center');
  }
}
