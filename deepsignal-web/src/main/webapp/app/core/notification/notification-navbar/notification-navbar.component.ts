import { Component, Inject, Prop, PropSync, Vue, Watch } from 'vue-property-decorator';
import NotificationCommonComponent from '@/core/notification/notification-common/notification-common.vue';
import { namespace } from 'vuex-class';
const notificationViewState = namespace('notificationViewStateStore');

@Component({
  components: {
    'notification-common': NotificationCommonComponent,
  },
})
export default class NotificationNavbarComponent extends Vue {
  @Prop(Boolean) showingNotification: boolean | false;
  @notificationViewState.Getter
  public countPressedNotification!: number;
  notificationCommon;

  mounted(): void {
    this.notificationCommon = this.$refs['notificationCommon'];
  }

  pagingNotification() {
    this.notificationCommon.pagingNotifications();
  }

  @Watch('countPressedNotification')
  openInitNotification(countPressNotification) {
    this.notificationCommon.openInitNotification();
  }

  newNotification() {
    this.$emit('newNotification', true);
  }
}
