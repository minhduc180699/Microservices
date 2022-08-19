import { Component, Inject, Prop, Vue, Watch } from 'vue-property-decorator';
import { VERSION } from '@/constants';
import LoginService from '@/account/login.service';
import AccountService from '@/account/account.service';
import TranslationService from '@/locale/translation.service';
import vueCustomScrollbar from 'vue-custom-scrollbar';
import 'vue-custom-scrollbar/dist/vueScrollbar.css';
import axios from 'axios';
import PopupUserInfoComponent from '@/core/ds-navbar/popup-user-info/popup-user-info.vue';
import SearchMain from '@/core/search-main/search-main.vue';
import { IS_SHOW_NOTIFICATION, SESSION_STORAGE_CONSTANTS } from '@/shared/constants/ds-constants';
import NotificationNavbarComponent from '@/core/notification/notification-navbar/notification-navbar.vue';
import WebsocketService from '@/service/websocket.service';
import { Subscription } from 'rxjs';
import { namespace } from 'vuex-class';
import DsSidebar from '@/core/ds-navbar/ds-sidebar/ds-sidebar.vue';
import { PAGE } from '@/shared/constants/ds-constants';
import { PrincipalService } from '@/service/principal.service';
import checkMobile from '@/shared/constants/feed-constants';
import PopupUserSetting from '@/shared/cards/popup-setting/popup-user-setting.vue';
const networkStore = namespace('connectomeNetworkStore');
const notificationViewState = namespace('notificationViewStateStore');

@Component({
  computed: {
    detailFeed() {
      return this.$store.getters.getDetailFeed;
    },
  },

  watch: {
    $route() {
      this.isShowDaily = false;
      this.isShowMore = false;
      this.handleChangePage();
    },
    detailFeed(value) {
      this.isDetailFeed = value;
    },
  },
  components: {
    Sidebar: DsSidebar,
    vueCustomScrollbar,
    'popup-user-info': PopupUserInfoComponent,
    searchMain: SearchMain,
    notification: NotificationNavbarComponent,
    'popup-user-setting': PopupUserSetting,
  },
})
export default class DsNavbar extends Vue {
  props: {
    state: boolean;
  };
  @Inject('loginService')
  private loginService: () => LoginService;
  @Inject('translationService') private translationService: () => TranslationService;
  @Inject('accountService') private accountService: () => AccountService;
  @Inject('websocketService') private websocketService: () => WebsocketService;
  @Inject('principalService') private principalService: () => PrincipalService;
  @Prop(Boolean) readonly isShowHeatMap: boolean | false;

  public version = VERSION ? 'v' + VERSION : '';
  private currentLanguage = this.$store.getters.currentLanguage;
  private languages: any = this.$store.getters.languages;
  private langKey = 'en';
  private hasAnyAuthorityValue = false;
  private isShowDaily = false;
  private isShowMore = false;
  private isShowSearch = true;
  private searchAutoComplete = false;
  private avatar = '';
  private isDeleteUser = false;
  public searchKeyword = '';
  private router = this.$router;
  isShowNotification = false;
  private isEditing = false;
  private connectomeName = '';
  private connectome = null;
  private showUserMenu = false;
  private countPressNotification = 0;
  private page = 'Feed';
  private isDetailFeed = false;
  private isFavoriteShow = false;

  @networkStore.Action
  public getConnectomeUpdate!: (payload: { connectomeId: string; language: string }) => Promise<any>;

  @notificationViewState.Action
  public pressNotification!: (payload: { countPressNotification: number }) => void;

  private subscription?: Subscription;

  created() {
    this.langKey = this.currentLanguage;
    this.translationService().refreshTranslation(this.currentLanguage);
    //this.$store.dispatch('connectomeNetworkStore/shiftConnectomeByLanguage',newLanguage);
  }

  mounted() {
    if (localStorage.getItem('ds-connectome')) {
      this.connectome = JSON.parse(localStorage.getItem('ds-connectome'));
      axios.get('api/getUserById?id=' + this.connectome?.user?.id).then(res => {
        if (res.data) this.avatar = res.data.imageUrl;
      });
    } else {
      this.logout();
    }
    this.handleChangePage();
  }

  @Watch('connectome')
  setConnectomeToLocalStorage(value) {
    localStorage.setItem('ds-connectome', JSON.stringify(value));
    if (this.connectome && this.connectome.connectomeId && this.connectome.user && this.connectome.user.id) {
      this.getConnectomeUpdate({ connectomeId: this.connectome.connectomeId, language: this.currentLanguage }).then(() => {});
      this.initWebsocket();
    }
  }

  public subIsActive(input) {
    const paths = Array.isArray(input) ? input : [input];
    return paths.some(path => {
      return this.$route.path.indexOf(path) === 0; // current path starts with this path string
    });
  }

  public changeLanguage(newLanguage: string): void {
    this.langKey = newLanguage;
    this.translationService().refreshTranslation(newLanguage);
    this.$store.dispatch('connectomeNetworkStore/shiftConnectomeByLanguage', newLanguage);
  }

  public isActiveLanguage(key: string): boolean {
    return key === this.$store.getters.currentLanguage;
  }

  public getCurrentLangName(): string {
    return this.$store.getters.languages[this.langKey].name;
  }

  public logout(): void {
    this.handlerClose()
      .then()
      .finally(() => {
        localStorage.removeItem('ds-authenticationToken');
        sessionStorage.removeItem('ds-authenticationToken');
        localStorage.removeItem('ds-connectome');
        localStorage.removeItem('card-data');
        localStorage.removeItem('stock-codes');
        sessionStorage.removeItem(SESSION_STORAGE_CONSTANTS.FEED_STATUS_TRAINING);
        //localStorage.removeItem('ds-selectedConnectomeNode');
        sessionStorage.setItem('requested-url', this.$route.fullPath);
        this.$store.dispatch('connectomeNetworkStore/logout');
        this.$store.dispatch('mapNetworkStore/logout');
        this.$store.commit('logout');
        this.$root.$emit('bv::hide::modal', 'modal-my-info');
        this.$root.$emit('bv::hide::modal', 'modal-alert-logout');
        this.$router.push('/login');
      });
  }

  async handlerClose() {
    if (this.principalService().getUserInfo()) {
      const login = this.principalService().getUserInfo().login;
      await axios.get('api/clearTimer/' + login).then();
    }
  }

  public login(): void {
    if (sessionStorage.getItem('requested-url') == '') {
      const url = this.$route.fullPath != '/register' && this.$route.fullPath != '/login' ? this.$route.fullPath : '/';
      sessionStorage.setItem('requested-url', url);
    }
    this.$router.push('/login', () => {});
  }

  public get authenticated(): boolean {
    return this.$store.getters.authenticated;
  }

  public hasAnyAuthority(authorities: any): boolean {
    this.accountService()
      .hasAnyAuthorityAndCheckAuth(authorities)
      .then(value => {
        this.hasAnyAuthorityValue = value;
      });
    return this.hasAnyAuthorityValue;
  }

  public openPopupMyInfo(): void {
    this.$root.$emit('bv::show::modal', 'modal-my-info');
  }

  public closePopupMyInfo(): void {
    this.$root.$emit('bv::hide::modal', 'modal-my-info');
  }

  public openPopupUserProfile(): void {
    this.closePopupMyInfo();
    this.$root.$emit('bv::show::modal', 'modal-user-profile');
  }

  public closePopupUserProfile(): void {
    this.$root.$emit('bv::hide::modal', 'modal-user-profile');
  }

  public openPopupAlarm(): void {
    this.$root.$emit('bv::show::modal', 'modal-alarm');
  }

  public get openAPIEnabled(): boolean {
    return this.$store.getters.activeProfiles.indexOf('api-docs') > -1;
  }

  public get inProduction(): boolean {
    return this.$store.getters.activeProfiles.indexOf('prod') > -1;
  }

  public get username(): string {
    return this.$store.getters.account ? this.$store.getters.account.login : '';
  }

  public search(e) {
    this.searchKeyword = e;
  }

  public focusOut(e) {
    this.isShowSearch = e;
  }

  public openLearningCenter() {
    // @ts-ignore
    this.$refs.searchMain.openLearningCenter();
  }

  public checkShowNotification() {
    this.isShowNotification = localStorage.getItem(IS_SHOW_NOTIFICATION) == 'true';
  }

  public closeShowNotification() {
    this.countPressNotification += 1;
    this.pressNotification({ countPressNotification: this.countPressNotification });
    this.isShowNotification = false;
    localStorage.setItem(IS_SHOW_NOTIFICATION, 'false');
  }

  public handleEditConnectomeName() {
    this.isEditing = !this.isEditing;
    if (this.isEditing) {
      this.connectomeName = this.connectome.connectomeName;
      this.$nextTick().then(() => {
        // @ts-ignore
        this.$refs['editConnectomeBtn'].focus();
      });
    }
  }

  public editConnectomeName() {
    axios
      .put(`api/connectome/${this.connectome.connectomeId}/?connectomeName=` + this.connectomeName)
      .then(res => {
        this.connectome = res.data;
        this.isEditing = !this.isEditing;
        this.$bvToast.toast('Edit connectome name success', {
          toaster: 'b-toaster-bottom-right',
          title: res.status.toString(),
          variant: 'success',
          solid: true,
          autoHideDelay: 5000,
        });
      })
      .catch(err => {
        this.$bvToast.toast(err.message, {
          toaster: 'b-toaster-bottom-right',
          title: err.response.status,
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
      });
  }

  public openUserMenu() {
    this.showUserMenu = !this.showUserMenu;
    this.isEditing = false;
    if (this.showUserMenu) {
      this.$nextTick().then(() => {
        if (checkMobile()) {
          // @ts-ignore
          this.$refs.userMenuMobile.focus(); // set active element
        } else {
          // @ts-ignore
          this.$refs.userMenu.focus();
        }
      });
    }
  }

  public closeUserMenu() {
    if (document.activeElement.tagName === 'BODY' && document.activeElement.className !== 'modal-open') {
      this.showUserMenu = false;
    }
  }

  public initWebsocket(): void {
    if (this.connectome && this.connectome.connectomeId && this.connectome.user && this.connectome.user.id) {
      console.log('Init UpdateConnectonne websocket', this.connectome.connectomeId, this.connectome.user.id);
      this.subscription = this.websocketService().subscribeUpdateConnectome(activity => {
        console.log('UpdateConnectonne Event from websocket' + activity);
        if (activity) {
          this.getConnectomeUpdate({ connectomeId: this.connectome.connectomeId, language: this.currentLanguage }).then(() => {});
        }
      });
    }
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

  openSidebar() {
    $('html').addClass('sitemap-layer-show');
  }

  openModalLogout() {
    this.$bvModal.show('modal-logged-out');
  }

  closeSidebar() {
    $('html').removeClass('sitemap-layer-show');
  }

  changePage(page) {
    this.page = page.name;
    this.$router.push(page.link);
    this.closeSidebar();
  }

  handleChangePage() {
    const href = location.href;
    if (href.split('/').includes('feed')) {
      this.page = Object.assign(PAGE.FEED).name;
    } else if (href.split('/').includes('people')) {
      this.page = Object.assign(PAGE.PEOPLE).name;
    } else if (href.split('/').includes('signals')) {
      this.page = Object.assign(PAGE.SIGNALS).name;
    } else if (href.split('/').includes('connectome')) {
      this.page = Object.assign(PAGE.CONNECTOME).name;
    } else if (href.split('/').includes('my-ai')) {
      this.page = Object.assign(PAGE.MYAI).name;
    }
  }

  openFavorite(isOpen?) {
    if (!isOpen) {
      this.isFavoriteShow = false;
      return;
    }
    this.isFavoriteShow = !this.isFavoriteShow;
  }

  openPopUpUserInfo() {
    this.$bvModal.show('modal-user-info');
    // @ts-ignore
    this.$refs.userInfo.onOpenPopUpUserInfo();
  }

  openPopUpUserSetting() {
    this.$bvModal.show('modal-member-setting');
  }

  changeTab(router) {
    if (router) {
      this.$router.push(router);
    }
  }

  changeIsShowHeatMap() {
    this.$emit('changeIsShowHeatMap', this.isShowHeatMap);
  }
}
