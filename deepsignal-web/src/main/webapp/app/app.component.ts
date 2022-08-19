import Vue from 'vue';
import Component from 'vue-class-component';
import Ribbon from '@/core/ribbon/ribbon.vue';
import DsFooter from '@/core/ds-footer/ds-footer.vue';
import DsNavbar from '@/core/ds-navbar/ds-navbar.vue';
import DsNavleft from '@/core/ds-navleft/ds-navleft.vue';
import ShowMore from '@/shared/cards/footer/show-more/show-more.vue';

import '@/shared/config/dayjs';
import Loader from '@/core/loader/loader.vue';
import { Inject, Watch } from 'vue-property-decorator';
import TranslationService from '@/locale/translation.service';
import { PrincipalService } from '@/service/principal.service';
import DsToastLearning from '@/shared/custom/toast/ds-toast-learning.vue';
import axios from 'axios';
import { UserActivityLogModel } from '@/shared/model/user-activity-log.model';
import h337 from 'heatmap.js';
import { namespace } from 'vuex-class';
const appStoreState = namespace('appStore');

declare global {
  interface Window {
    ZoomChartsLicense: string;
    ZoomChartsLicenseKey: string;
  }
}

window.ZoomChartsLicense = 'ZCP-e3dr7u1hj: ZoomCharts SDK Internal Use license for DeepSignal, LLC. Valid for 1 chart developer and EVSL';
window.ZoomChartsLicenseKey =
  '6cc56160b6a106c7e9a30ec08ac74ab469b02fdcf4eb87c7ab' +
  '27ca7e274f5ccde74019f843ddf6b7fe1cf4d73d0fb31d7329f50e06c3f9e25d115e3fe9add0c' +
  '3823ddafd979f8318578f630cd3b99cef842577cfe9abdbfeab421573ff0fcd66a0641820baa2' +
  '3b923c505f47b8530dab330ed375acc39ae451e1e4e7db588bddd153aa68143d9acab0b41495c' +
  'e96330915c204efcdcef65642b1ddb0c1d3256879754b48f7392793595d2ebb8b804148ded8b2' +
  '0c78526496b3246dac604bb45890653849a91a602ff7f258ebe38500ad4453093b3c6d00ab326' +
  '4ae9bd33630a15af836901d7009cb52dc8be6d64533161205c804de4e136f1d659d408fe375af';

@Component({
  components: {
    ribbon: Ribbon,
    'ds-navbar': DsNavbar,
    'ds-navleft': DsNavleft,
    'ds-footer': DsFooter,
    'ds-loader': Loader,
    'show-more': ShowMore,
    'ds-toast-learning': DsToastLearning,
  },
})
export default class App extends Vue {
  public isOpenLearningCenter = true;
  public isOpenAgain = false;
  private isRegister = false;
  private paddingTopUnset = {};
  @Inject('principalService') private principalService: () => PrincipalService;
  @Inject('translationService') private translationService: () => TranslationService;
  private currentLanguage = this.$store.getters.currentLanguage;
  private languages: any = this.$store.getters.languages;
  isTraining = false;
  public isShowHeatMap = false;
  heatmapData = [];
  // isLoginPage = false;
  @appStoreState.Getter
  public isLoginPage!: boolean;
  @appStoreState.Action
  public changePage!: (payload: { isLoginPage: boolean }) => void;
  loginPage = false;

  @Watch('heatmapData')
  onHeatmapChange() {
    if (this.heatmapData.length > 4) {
      axios.post('/api/heatmap', this.heatmapData).then(
        // @ts-ignore
        (this.heatmapData.length = 0)
      );
    }
  }

  @Watch('isLoginPage')
  detectLoginPage(isLoginPage) {
    this.loginPage = !isLoginPage;
  }

  changeStatePage(navigate, e, isLoginPage) {
    this.loginPage = !this.loginPage;
    navigate(e);
    this.changePage({ isLoginPage: isLoginPage });
  }

  @Watch('$route.fullPath')
  onRouteChange(path) {
    this.isRegister = path.split('/').includes('register') || path.split('/').includes('login');
    ['register', 'login'].some(e => path.split('/').includes(e))
      ? Object.assign(this.paddingTopUnset, { 'padding-top': 'unset !important' })
      : Object.assign(this.paddingTopUnset, {});
  }

  created() {
    const current = window.location.href;
    if (current.split('/').includes('login')) {
      this.loginPage = true;
    }
    if (current.split('/').includes('register')) {
      this.loginPage = false;
    }
  }

  public changeLanguage(newLanguage: string): void {
    this.currentLanguage = newLanguage;
    this.translationService().refreshTranslation(newLanguage);
    this.$store.dispatch('connectomeNetworkStore/shiftConnectomeByLanguage', newLanguage);
  }

  mounted() {
    if (document.getElementsByClassName('heatmap-canvas').length > 0) {
      // @ts-ignore
      document.getElementsByClassName('heatmap-canvas').item(0).style.height = '100%';
    }
    document.querySelector('body').onclick = ev => {
      if (this.$route.path !== '/login') {
        // @ts-ignore
        const heatPoint = this.heatmapData.find(item => item.x === ev.pageX && item.y === ev.pageY);
        if (heatPoint) {
          heatPoint.value++;
        } else {
          this.heatmapData.push({
            // @ts-ignore
            x: ev.pageX,
            // @ts-ignore
            y: ev.pageY,
            value: 1,
          });
        }
      }
    };

    const Common = {
      init: function () {
        this.event();
      },
      event: function () {
        $('[aria-label="like"]').on('click', function () {
          if ($(this).hasClass('active')) {
            $(this).removeClass('active');
            $(this).attr('aria-pressed', 'false');
          } else {
            $(this).addClass('active');
            $(this).attr('aria-pressed', 'true');
          }
        });

        $('.tooltip-menu a').on('click', function (e) {
          if ($(this).parent().hasClass('has-treeview')) {
            e.preventDefault();
            $(this).parent().addClass('show');
          }
        });
      },
    };

    const Header = {
      init: function () {
        this.fullscreen();
      },
      fullscreen: function (e) {
        $('.fullscreen > a').on('click', function (e) {
          e.preventDefault();
          if (!$('html').hasClass('is-fullscreen')) {
            openFullScreenMode();
            $('html').addClass('is-fullscreen');
            $(this).find('i').text('fullscreen_exit');
          } else {
            closeFullScreenMode();
            $('html').removeClass('is-fullscreen');
            $(this).find('i').text('fullscreen');
          }
        });

        const docV = document.documentElement as HTMLElement & {
          mozRequestFullScreen(): Promise<void>;
          webkitRequestFullscreen(): Promise<void>;
          msRequestFullscreen(): Promise<void>;
        };

        const docEX = document as Document & {
          mozCancelFullScreen(): Promise<void>;
          webkitExitFullscreen(): Promise<void>;
          msExitFullscreen(): Promise<void>;
        };

        function openFullScreenMode() {
          if (docV.requestFullscreen) docV.requestFullscreen();
          else if (docV.webkitRequestFullscreen) docV.webkitRequestFullscreen();
          else if (docV.mozRequestFullScreen) docV.mozRequestFullScreen();
          else if (docV.msRequestFullscreen) docV.msRequestFullscreen();
        }

        function closeFullScreenMode() {
          if (document.exitFullscreen) document.exitFullscreen();
          else if (docEX.webkitExitFullscreen) docEX.webkitExitFullscreen();
          else if (docEX.mozCancelFullScreen) docEX.mozCancelFullScreen();
          else if (docEX.msExitFullscreen) docEX.msExitFullscreen();
        }
      },
    };

    $(document).ready(function () {
      Common.init();
      Header.init();
    });
    this.isTraining = true;
  }

  @Watch('$route', { immediate: true, deep: true })
  onRouterChange(newVal, oldVal) {
    if (newVal && oldVal && newVal.fullPath && oldVal.fullPath) {
      if (newVal.fullPath !== oldVal.fullPath && oldVal.fullPath !== '/') {
        this.saveUserActivitiesChangeRoute();
      }
    }
    if (this.$refs.dsToastLearning) {
      // @ts-ignore
      this.$refs.dsToastLearning.checkTrainingFeedStatus();
    }
  }

  saveUserActivitiesChangeRoute() {
    const userActivityLog = new UserActivityLogModel();
    userActivityLog.connectomeId = this.principalService().getConnectomeInfo().connectomeId;
    userActivityLog.userLanguage = this.$store.getters.currentLanguage ? this.$store.getters.currentLanguage : '';
    const token = this.principalService().getToken();
    if (!token) {
      return;
    }
    axios.post('api/user-activity-log/saveChangeUrl', userActivityLog).then();
  }

  logout() {
    // @ts-ignore
    this.$refs.logout.logout();
  }

  showMore(e) {
    if (this.$store.getters.show || this.$store.getters.show == undefined) {
      if (
        e.pageX > this.$store.getters.getCoordinate.x + window.scrollX - 230 &&
        e.pageX < this.$store.getters.getCoordinate.x + window.scrollX &&
        e.pageY > this.$store.getters.getCoordinate.y + window.scrollY - 265 &&
        e.pageY < this.$store.getters.getCoordinate.y + window.scrollY
      ) {
        //
      } else {
        this.$store.commit('setShow', false);
      }
    }
  }

  changeIsShowHeatMap() {
    this.isShowHeatMap = !this.isShowHeatMap;
    if (this.isShowHeatMap) {
      this.initHeatmap();
    } else {
      if (document.getElementsByClassName('heatmap-canvas').length > 0) {
        document.querySelector('.heatmap-canvas').remove();
      }
    }
  }

  initHeatmap() {
    const heatmapInstance = h337.create({
      container: document.querySelector('body'),
      radius: 60,
    });
    // @ts-ignore
    document.querySelector('.heatmap-canvas').style.zIndex = '100';
    axios.get('/api/heatmap').then(res => {
      const data = {
        max: res.data.maxValue,
        min: 0,
        data: res.data.heatmaps,
      };
      heatmapInstance.setData(data);
    });
  }
}
