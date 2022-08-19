import { Component, Prop, Vue } from 'vue-property-decorator';
import { ACTIVITY, MESSAGES_FEEDBACK } from '@/shared/constants/feed-constants';

@Component
export default class dsCardNotUseful extends Vue {
  @Prop(Boolean) readonly isLoadingDelete: any | true;
  @Prop(Boolean) readonly isMyAi: any | false;
  private reportMessages = '';
  private isReport = false;
  private messages = MESSAGES_FEEDBACK;

  handleReport(message) {
    switch (message) {
      case MESSAGES_FEEDBACK.REPORT:
        return (this.isReport = true);
        break;
      case MESSAGES_FEEDBACK.CANCEL:
        // @ts-ignore
        return this.$parent.hiddenCard(false);
        break;
      case MESSAGES_FEEDBACK.REPORT_SEND:
        this.isReport = false;
        // @ts-ignore
        return this.$parent.hiddenCard(true, this.reportMessages);
        break;
      default:
        this.isReport = false;
        // @ts-ignore
        return this.$parent.hiddenCard(true, message);
        break;
    }
  }
}
