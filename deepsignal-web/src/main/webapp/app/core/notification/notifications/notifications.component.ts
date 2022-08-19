import { Component, Inject, Prop, Vue } from 'vue-property-decorator';
import NotificationCommonComponent from '@/core/notification/notification-common/notification-common.vue';
import { NotificationService } from '@/service/notification.service';
import { namespace } from 'vuex-class';

const notificationViewState = namespace('notificationViewStateStore');
@Component({
  components: {
    'notification-common': NotificationCommonComponent,
  },
})
export default class NotificationsComponent extends Vue {
  @Inject('notificationService') private notificationService: () => NotificationService;
  notificationTypes = [];
  notificationTypeCurrent = null;
  numOfUnread = 0;
  notificationCommon;

  @notificationViewState.Action
  public selectTypeNotification!: (payload: { type: string }) => void;

  getAllNotificationType() {
    this.notificationService()
      .getAllNotificationType()
      .then(res => {
        if (!res || !res.data) {
          return;
        }
        this.notificationTypes = res.data;
        this.notificationTypeCurrent = this.notificationTypes[0];
        this.pagingNotification();
      });
  }

  mounted(): void {
    this.getAllNotificationType();
    this.notificationCommon = this.$refs['notificationCommon'];
  }

  choseType(type) {
    this.notificationTypeCurrent = type;
    // @ts-ignore
    //this.$refs['notificationCommon'].changeType(this.notificationTypeCurrent);
    if (this.notificationTypeCurrent) {
      this.selectTypeNotification({ type: this.notificationTypeCurrent });
    }
  }

  pagingNotification() {
    this.notificationCommon.pagingNotifications(this.notificationTypeCurrent);
  }

  refreshNotification() {
    this.notificationCommon.refreshNotifications(this.notificationTypeCurrent);
  }

  getNumberUnread(e) {
    this.numOfUnread = e;
  }

  deleteReadNotification() {
    this.notificationCommon.deleteReadNotification();
  }

  deleteAll() {
    this.notificationCommon.deleteAllNotification();
  }

  moreNotification() {
    this.notificationCommon.moreNotification();
  }
}
