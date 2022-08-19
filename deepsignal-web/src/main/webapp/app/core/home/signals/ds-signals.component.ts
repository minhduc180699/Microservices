import { Component, Inject, Vue } from 'vue-property-decorator';
import DsCards from '@/shared/cards/ds-cards.vue';
import SignalTodayIssueComponent from '@/core/home/signals/signal-today-issue/signal-today-issue.vue';
import PageTopComponent from '@/core/home/page-top.vue';
import { SignalService } from '@/service/signal.service';
import { PrincipalService } from '@/service/principal.service';
import { SIGNAL_KEYWORD_TYPE, SignalKeywordModel } from '@/core/home/signals/signal-tracking-issue/signal-keyword.model';
import moment from 'moment';
import RegisterSignalTrackingIssueComponent from '@/core/home/signals/signal-tracking-issue/register-signal-tracking-issue.vue';
import SignalRelationshipDiagramAnalysisComponent from '@/core/home/signals/signal-today-issue/signal-relationship-diagram-analysis/signal-relationship-diagram-analysis.vue';
import SignalWordCloudChart from '@/core/home/signals/signal-today-issue/signal-word-cloud-chart/signal-word-cloud-chart.vue';
import SignalPeopleCompanyChart from '@/core/home/signals/signal-today-issue/signal-people-company-chart/signal-people-company-chart.vue';
import WebsocketService from '@/service/websocket.service';
import LineChart from '@/shared/chart/ds-line-chart/ds-line-chart.vue';
import { Subscription } from 'rxjs';

@Component({
  name: 'ds-signal',
  components: {
    'signal-today-issue': SignalTodayIssueComponent,
    DsCards,
    'page-top': PageTopComponent,
    'register-signal-tracking-issue': RegisterSignalTrackingIssueComponent,
    'signal-relationship-diagram-analysis': SignalRelationshipDiagramAnalysisComponent,
    'signal-word-cloud': SignalWordCloudChart,
    'signal-people-company-chart': SignalPeopleCompanyChart,
    'line-chart': LineChart,
  },
})
export default class DsSignals extends Vue {
  @Inject('signalService') private signalService: () => SignalService;
  @Inject('principalService') private principalService: () => PrincipalService;
  @Inject('websocketService') private websocketService: () => WebsocketService;
  connectomeId;
  signalKeywords: SignalKeywordModel[] = [];
  workDay = moment(new Date(new Date(moment().format('yyyy.MM.DD hh:mm:ss a')).getTime() + new Date().getTimezoneOffset() * 60000)).format(
    'yyyyMMDD'
  );
  pageable = {
    page: 1,
    size: 10,
    sort: ['signalId,asc'],
  };
  private subscription?: Subscription;
  loading = false;
  private timeSeries = {
    connectome_id: '',
    compkeywords: '',
    search_source: '',
    country: '',
    from: '',
    until: '',
    request_id: '',
  };

  created(): void {
    this.connectomeId = this.principalService().getConnectomeInfo().connectomeId;
    this.getAllSignalKeywords();
    this.initWebsocket();
  }

  getAllSignalKeywords() {
    this.signalService()
      .paging(this.connectomeId, this.workDay, SIGNAL_KEYWORD_TYPE.IT, this.pageable)
      .then(res => {
        if (!res || !res.data || res.data.content.length == 0) {
          return;
        }
        this.signalKeywords = [];
        for (const data of res.data.content) {
          const signalKeywordModel = new SignalKeywordModel();
          this.timeSeries.compkeywords = data.keywords;
          if (this.timeSeries.compkeywords && this.timeSeries.compkeywords != '') {
            this.signalService()
              .getTimeSeries(this.timeSeries)
              .then(res => {
                data.timeSeries = res.data;
                signalKeywordModel.init(data);
                this.signalKeywords.push(signalKeywordModel);
              });
          }
        }
      });
  }

  public initWebsocket(): void {
    this.subscription = this.websocketService().subscribeSignalTracking(activity => {
      if (activity) {
        if (!activity.connectomeId) {
          return;
        }
        if (activity.connectomeId === this.connectomeId) {
          this.getAllSignalKeywords();
        }
      }
    });
  }

  onEdit(item: SignalKeywordModel) {
    item.isEdit = true;
    this.changeSignalKeywordsDOM(item);
  }

  onDelete(item: SignalKeywordModel) {
    this.signalService()
      .deleteById(this.connectomeId, item.signalId, item.id)
      .then(() => this.getAllSignalKeywords());
  }

  // function to re-render DOM by using array function: splice(). See more: https://vuejs.org/guide/essentials/list.html#array-change-detection
  changeSignalKeywordsDOM(item: SignalKeywordModel) {
    if (!item || !item.id) {
      return;
    }
    const indexOfItem = this.signalKeywords.findIndex(signal => signal.id === item.id);
    this.signalKeywords.splice(indexOfItem, 1, item);
  }

  addToKeywords(item: SignalKeywordModel) {
    if (!item.messageKeyword) {
      return;
    }
    item.keywordsToShow.push(item.messageKeyword);
    item.messageKeyword = '';
  }

  removeKeywords(item: SignalKeywordModel, keyword: string) {
    item.keywordsToShow = item.keywordsToShow.filter(item => item != keyword);
  }

  addToMainKeyword(e: any, item: SignalKeywordModel) {
    if (!item.messageMainKeyword || SignalKeywordModel.isDuplicateKeyword(item)) {
      return;
    }
    if (e.type === 'submit') {
      item.mainKeyword = item.messageMainKeyword;
      item.messageMainKeyword = '';
    }
  }

  onApply(item: SignalKeywordModel) {
    const signalKeywordModel = SignalKeywordModel.prepareSignalModelToSave(item);
    if (!signalKeywordModel) return;
    this.loading = true;
    this.signalService()
      .saveSignalKeywords(this.connectomeId, signalKeywordModel)
      .then(res => {
        this.getAllSignalKeywords();
      })
      .finally(() => (this.loading = false));
  }

  onCancel(item: SignalKeywordModel) {
    item.keywordsToShow = item.getKeywordsShowFromKeywords(item.getSignalKeywordCloneDeep().keywords);
    item.mainKeyword = item.getSignalKeywordCloneDeep().mainKeyword;
    item.isEdit = false;
    this.changeSignalKeywordsDOM(item);
  }

  activeTab(signalKeyword: SignalKeywordModel, name: string) {
    signalKeyword.tabNameActive = name + signalKeyword.id;
  }

  confirmDelete(item) {
    this.$bvModal
      .msgBoxConfirm('Please confirm that you want to delete everything.', {
        title: 'Please Confirm',
        size: 'sm',
        buttonSize: 'sm',
        okVariant: 'danger',
        okTitle: 'YES',
        cancelTitle: 'NO',
        footerClass: 'p-2',
        hideHeaderClose: false,
        centered: true,
      })
      .then(value => {
        if (value) this.onDelete(item);
      })
      .catch(err => {
        // An error occurred
      });
  }

  destroyed() {
    if (this.subscription) {
      try {
        this.subscription.unsubscribe();
        this.subscription = undefined;
      } catch (error) {
        console.log(error);
      }
    }
  }

  learningKeyword() {
    this.$router.push('/my-ai/learning-center');
  }

  selectedDate(date) {
    this.signalKeywords = [];
    this.workDay = date;
    this.getAllSignalKeywords();
  }
}
