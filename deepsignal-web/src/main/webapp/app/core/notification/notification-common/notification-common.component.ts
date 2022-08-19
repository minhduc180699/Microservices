import { Component, Inject, Prop, Vue, Watch } from 'vue-property-decorator';
import { NotificationService } from '@/service/notification.service';
import { Subscription } from 'rxjs';
import WebsocketService from '@/service/websocket.service';
import { DATA_FAKE, IS_SHOW_NOTIFICATION } from '@/shared/constants/ds-constants';
import { PrincipalService } from '@/service/principal.service';
import vueCustomScrollbar from 'vue-custom-scrollbar';
import { splitTagHtml } from '@/util/ds-util';
import { namespace } from 'vuex-class';
import { CacheService } from '@/service/cache.service';

const notificationViewState = namespace('notificationViewStateStore');

export enum NotificationCategory {
  basic = 'basic',
  interpolation = 'interpolation',
}

@Component({
  components: {
    vueCustomScrollbar,
  },
})
export default class NotificationCommonComponent extends Vue {
  private scrollSettings = {
    wheelPropagation: false,
    suppressScrollX: false,
    wheelSpeed: 0.5,
  };
  loading = false;
  @Inject('notificationService')
  private notificationService: () => NotificationService;
  @Inject('websocketService') private websocketService: () => WebsocketService;
  @Inject('principalService') private principalService: () => PrincipalService;
  @Inject('cacheService') private cacheService: () => CacheService;
  @Prop(Boolean) readonly hasScroll: boolean | false;
  currentType;
  pageable = {
    page: 1,
    size: 10,
    sort: ['createdDate,desc'],
  };
  notifications = [];
  totalElements = 0;
  public isReadAll = false;
  totalNotificationUnread = 0;
  private subscription?: Subscription;
  loaderDisable = true;

  @notificationViewState.Getter
  public typeSelected!: string;

  pagingNotifications(type?) {
    this.currentType = type;
    this.$getComment().then((res: any) => {
      this.notifications = res.data.content;
      this.countAllUnread();
    });
  }

  refreshNotifications(type) {
    this.pageable.page = 1;
    this.pagingNotifications(type);
  }

  async $getComment() {
    this.loading = true;
    return new Promise((resolve, reject) => {
      // if hasScroll is true, that mean list is notification of navbar -> ignore currentType
      if (this.hasScroll) {
        this.currentType = '';
      }
      this.notificationService()
        .paging(this.currentType, this.pageable)
        .then(res => {
          this.loading = false;
          this.totalElements = res.data.totalElements;
          resolve(res);
        })
        .catch(err => {
          this.loading = false;
          reject(err);
        });
    });
  }

  @Watch('typeSelected')
  onChangeSelectedType(type) {
    this.pageable.page = 1;
    this.pagingNotifications(type);
  }

  checkCacheNotification() {
    if (!this.principalService().getUserInfo()) {
      return;
    }
    this.cacheService()
      .checkNotification(this.principalService().getUserInfo().id)
      .then(res => {
        if (res.data) {
          this.newNotification();
        }
      });
  }

  moreNotification() {
    if (this.totalElements > this.pageable.page * this.pageable.size) {
      this.pageable.page += 1;
      this.$getComment().then((res: any) => {
        this.notifications = [...this.notifications, ...res.data.content];
      });
    }
  }

  scrollHandle(e) {
    const target = e.target;
    if (target) {
      if (target.scrollTop + target.clientHeight >= target.scrollHeight) {
        this.moreNotification();
      }
    }
  }

  openInitNotification() {
    if (!this.notifications || !this.notifications.length) {
      this.pagingNotifications(this.currentType);
    }
  }

  public init(): void {
    this.subscription = this.websocketService().subscribeNotification(activity => {
      const userId = this.principalService().getUserInfo().id;
      if (activity.data == userId) {
        this.pagingNotifications(this.currentType);
        this.newNotification();
      }
    });
    this.checkCacheNotification();
  }

  newNotification() {
    localStorage.setItem(IS_SHOW_NOTIFICATION, 'true');
    this.$emit('newNotification', true);
  }

  mounted(): void {
    this.init();
  }

  public destroyed(): void {
    if (this.subscription) {
      try {
        this.subscription.unsubscribe();
        this.subscription = undefined;
      } catch (error) {
        console.log(error);
      }
    }
  }

  private countAllUnread() {
    this.notificationService()
      .countAllUnread()
      .then(res => {
        if (!res) {
          return;
        }
        this.setTotalNotificationUnread(res.data);
      });
  }

  setTotalNotificationUnread(total = 0) {
    this.$emit('numberNotification', total);
    this.isReadAll = total === 0;
    this.totalNotificationUnread = total;
  }

  markAllChecked() {
    // this.notifications.map(item => item.isChecked = 1);
    this.notificationService()
      .markAllChecked()
      .then(() => {
        this.pagingNotifications(this.currentType);
      });
  }

  markReadNotification(item) {
    // if (item.isMarkedRead === 1 && item.isChecked === 1) {
    //   return;
    // }
    item.isMarkedRead = 1;
    item.isChecked = 1;
    this.totalNotificationUnread -= 1;
    this.setTotalNotificationUnread(this.totalNotificationUnread);
    this.notificationService().markAsRead(item.id).then();
    this.$router.push(item.url).catch(error => {
      if (error.name !== 'NavigationDuplicated' && !error.message.includes('Avoided redundant navigation to current location')) {
        console.log(error);
      }
    });
  }

  deleteReadNotification() {
    this.notifications = this.notifications.filter(item => !item.isMarkedRead);
    this.notificationService()
      .deleteReadNotification()
      .then(res => {
        this.pagingNotifications(this.currentType);
      });
  }

  deleteAllNotification() {
    this.notifications = [];
    this.setTotalNotificationUnread();
    this.notificationService().deleteAllNotification().then();
  }

  deleteNotification(item) {
    this.notifications = this.notifications.filter(data => data.id !== item.id);
    if (this.totalNotificationUnread && this.totalNotificationUnread > 0 && item.isMarkedRead == 0) {
      this.totalNotificationUnread -= 1;
      this.setTotalNotificationUnread(this.totalNotificationUnread);
    }
    this.notificationService()
      .deleteNotificationById(item.id)
      .then(res => {
        if (this.notifications.length < 5 && this.totalElements > this.pageable.size) {
          this.pagingNotifications(this.currentType);
        }
      });
  }

  contentNotification(item) {
    if (item.category == NotificationCategory.basic) {
      return this.$t(item.contentI18n);
    }
    if (item.category == NotificationCategory.interpolation) {
      const datas = this.processContentNotification(item.content);
      return this.$t(item.contentI18n, datas);
    }
  }

  titleNotification(item) {
    if (item.category == NotificationCategory.basic) {
      return this.$t(item.titleI18n);
    }
    if (item.category == NotificationCategory.interpolation) {
      const datas = this.processContentNotification(item.title);
      return this.$t(item.titleI18n, datas);
    }
  }

  processContentNotification(content) {
    const datas = splitTagHtml(content);
    if (datas) {
      return datas.filter((item: string) => item.includes('<span'));
    }
    return [];
  }
}
