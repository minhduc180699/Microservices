import Vue from 'vue';
import Component from 'vue-class-component';
import { Inject } from 'vue-property-decorator';
import { SignalService } from '@/service/signal.service';
import { PrincipalService } from '@/service/principal.service';
import moment from 'moment';
import { SIGNAL_KEYWORD_TYPE } from '@/core/home/signals/signal-tracking-issue/signal-keyword.model';

import { namespace } from 'vuex-class';
const signalIssuesState = namespace('cardStore');
import { CARD_TYPE } from '@/shared/constants/feed-constants';

@Component
export default class DsCardSignal extends Vue {
  pageable = {
    page: 1,
    size: 10,
    sort: ['displayOrder,asc'],
  };
  signals = [];
  totalPages = 0;
  workDay = moment(new Date(new Date(moment().format('yyyy.MM.DD hh:mm:ss a')).getTime() + new Date().getTimezoneOffset() * 60000)).format(
    'yyyyMMDD'
  );
  isLoadingSignal = true;

  @Inject('signalService')
  private signalService: () => SignalService;
  @Inject('principalService') private principalService: () => PrincipalService;
  @signalIssuesState.Action
  public changeSignalIssues!: (payload: { signalIssues: [] }) => void;

  created(): void {
    this.paging();
  }

  mounted(): void {}

  public paging() {
    this.signalService()
      .paging(this.principalService().getConnectomeInfo().connectomeId, this.workDay, SIGNAL_KEYWORD_TYPE.TI, this.pageable)
      .then(res => {
        if (!res || !res.data) {
          return;
        }
        this.changeSignalIssues({ signalIssues: res.data.content });
        if (res.data.content.length < 1) {
          this.$root.$emit('delete-card-issue', CARD_TYPE.SIGNAL);
          this.$root.$emit('delete-card-issue', CARD_TYPE.CONTENTS_GROUP);
        }
        if (res.data.content.length <= 4) {
          this.$root.$emit('delete-card-issue', CARD_TYPE.SIGNAL, true);
        }
        this.signals.push(...res.data.content);
        this.isLoadingSignal = false;
      });
  }

  public toSignal(hash) {
    this.$router.push({ name: 'Signals', hash: hash });
  }
}
