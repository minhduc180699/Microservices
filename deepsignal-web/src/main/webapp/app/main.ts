// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.common with an alias.
import Vue from 'vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import App from './app.vue';
import Vue2Filters from 'vue2-filters';
import Multiselect from 'vue-multiselect';
import VueAwesomeSwiper from 'vue-awesome-swiper';
import { ToastPlugin } from 'bootstrap-vue';
import vueCustomScrollbar from 'vue-custom-scrollbar';
import 'vue-custom-scrollbar/dist/vueScrollbar.css';
import router from './router';
import * as config from './shared/config/config';
import * as bootstrapVueConfig from './shared/config/config-bootstrap-vue';
import * as customDirective from './shared/directives/ds-directive';
import JhiItemCountComponent from './shared/jhi-item-count.vue';
import JhiSortIndicatorComponent from './shared/sort/jhi-sort-indicator.vue';
import InfiniteLoading from 'vue-infinite-loading';
import HealthService from './admin/health/health.service';
import MetricsService from './admin/metrics/metrics.service';
import LogsService from './admin/logs/logs.service';
import ConfigurationService from '@/admin/configuration/configuration.service';
import ActivateService from './account/activate/activate.service';
import RegisterService from './account/register/register.service';
import UserManagementService from '@/admin/user-management/user-management.service';
import LoginService from './account/login.service';
import PhoneService from '@/account/phone.service';
import AccountService from './account/account.service';
import ConnectomeService from '@/entities/connectome/connectome.service';
import QnaService from '@/entities/help/question-and-answer/qna.service';
import FaqService from '@/entities/help/faq/faq.service';
import BootstrapVue from 'bootstrap-vue';
import IconsPlugin from 'bootstrap-vue';
import 'bootstrap/dist/css/bootstrap.css';
import { isJwtExpired } from 'jwt-check-expiration';

import 'echarts';
import ECharts from 'vue-echarts';
import TextHighlight from 'vue-text-highlight';

// Vuelidate
import Vuelidate from 'vuelidate';
import 'vuelidate/dist/vuelidate.min';

import '../content/scss/vendor.scss';
import TranslationService from '@/locale/translation.service';

import UserOAuth2Service from '@/entities/user/user.oauth2.service';

import FeedService from '@/core/home/feed/feed.service';
import PublicService from '@/service/public.service';
import PeopleService from '@/core/home/people/people.service';
import Fragment from 'vue-fragment';
import { RecentSearchService } from '@/service/recent-search.service';
import { PrincipalService } from '@/service/principal.service';
import { InteractionUserService } from '@/service/interaction-user.service';
import { LearnedDocumentService } from '@/service/learned-document.service';
import { CommentService } from '@/service/comment.service';
import { NotificationService } from '@/service/notification.service';
import WebsocketService from '@/service/websocket.service';
import axios from 'axios';
import { FileStorageService } from '@/service/file-storage.service';
import { LearningService } from '@/service/learning.service';
import { SignalService } from '@/service/signal.service';
import { CacheService } from '@/service/cache.service';
import { DetailFeedService } from '@/service/detail-feed.service';
import { UserSettingService } from '@/service/usersetting.service';
import VueToggles from 'vue-toggles';
/* tslint:disable */

// jhipster-needle-add-entity-service-to-main-import - JHipster will import entities services here

/* tslint:enable */
Vue.config.productionTip = false;
config.initVueApp(Vue);
config.initFortAwesome(Vue);
bootstrapVueConfig.initBootstrapVue(Vue);
customDirective.initDirective(Vue);
Vue.use(Vue2Filters);
Vue.use(ToastPlugin);
Vue.use(Vuelidate);
Vue.use(Fragment.Plugin);
Vue.use(vueCustomScrollbar);
Vue.use(BootstrapVue);
Vue.use(IconsPlugin);
Vue.component('multiselect', Multiselect);
Vue.use(VueAwesomeSwiper, {
  slidesPerView: 'auto',
  // spaceBetween: 10,
  // slidesOffsetBefore: 20,
  // slidesOffsetAfter: 20,
  pagination: {
    el: '.swiper-pagination',
    type: 'bullets',
  },
});

Vue.component('font-awesome-icon', FontAwesomeIcon);
Vue.component('jhi-item-count', JhiItemCountComponent);
Vue.component('jhi-sort-indicator', JhiSortIndicatorComponent);
Vue.component('infinite-loading', InfiniteLoading);
Vue.component('ds-echart', ECharts);
Vue.component('text-highlight', TextHighlight);
Vue.component('VueToggles', VueToggles);
const i18n = config.initI18N(Vue);
const store = config.initVueXStore(Vue);
let tokenTimer;

const websocketService = new WebsocketService(router, store);
const translationService = new TranslationService(store, i18n);
const loginService = new LoginService();
const accountService = new AccountService(store, translationService, router);

function clearStorage() {
  const storageItems = [
    'stock-codes',
    'currentLanguage',
    'learningCenter',
    'ds-connectome',
    'ds-authenticationToken',
    'card-data',
    'qrRefresh',
    'ds-selectedConnectomeNode',
    'requested-url',
  ];
  storageItems.forEach(item => {
    if (localStorage.getItem(item)) {
      localStorage.removeItem(item);
    }
    if (sessionStorage.getItem(item)) {
      sessionStorage.removeItem(item);
    }
  });
  store.dispatch('connectomeNetworkStore/logout');
  store.dispatch('mapNetworkStore/logout');
  store.commit('logout');
}

router.beforeEach((to, from, next) => {
  if (from.name === 'Feed') {
    // console.log('top = ', window.pageYOffset);
    store.commit('setScrollTop', window.pageYOffset);
  }
  const token = sessionStorage.getItem('ds-authenticationToken') || localStorage.getItem('ds-authenticationToken');
  if (token) {
    if (isJwtExpired(token)) {
      clearStorage();
      next('/login');
    } else {
      next();
    }
  }
  if (!store.getters.authenticated) {
    if (to.name !== 'Login' && to.name !== 'Register' && to.name !== 'QrCodeCheck') {
      accountService.retrieveAccount().then(res => {
        if (!res) {
          next({ name: 'Login' });
        } else {
          next();
        }
      });
    }
  }

  if (!to.matched.length) {
    next('/not-found');
  }

  if (to.meta && to.meta.authorities && to.meta.authorities.length > 0) {
    accountService.hasAnyAuthorityAndCheckAuth(to.meta.authorities).then(value => {
      if (!value) {
        sessionStorage.setItem('requested-url', to.fullPath);
        next('/forbidden');
      } else {
        next();
      }
    });
  } else if (to.matched.some(record => record.meta.hideForAuth)) {
    const token = localStorage.getItem('ds-authenticationToken') || sessionStorage.getItem('ds-authenticationToken');
    if (token) {
      if (to.name !== 'Register') {
        next({ path: sessionStorage.getItem('requested-url') || '/' });
      }
    } else {
      next();
    }
  } else {
    // no authorities, so just proceed
    next();
  }

  if (tokenTimer === undefined) {
    let isCheckResetTk = false;
    tokenTimer = setInterval(() => {
      if (localStorage.getItem('ds-authenticationToken') || sessionStorage.getItem('ds-authenticationToken')) {
        if (localStorage.getItem('ds-authenticationToken')) isCheckResetTk = true;
        axios
          .post('api/refreshToken', null, {
            params: {
              isCheckResetTk,
            },
          })
          .then(res => {
            if (localStorage.getItem('ds-authenticationToken')) {
              localStorage.setItem('ds-authenticationToken', res.data.id_token);
            } else {
              sessionStorage.setItem('ds-authenticationToken', res.data.id_token);
            }
          })
          .catch(error => {
            clearInterval(tokenTimer);
          });
      } else {
        clearInterval(tokenTimer);
      }
    }, 10 * 60 * 1000);
  }

  if (to.name == 'Detail') {
    store.commit('setDetailFeed', true);
  } else {
    store.commit('setDetailFeed', false);
  }
});

router.afterEach((to, from) => {
  if (to.name === 'Feed') {
    setTimeout(() => {
      window.scrollTo(0, store.state.accountStore.scrollTop);
    }, 0);
  }
});

/* tslint:disable */
new Vue({
  el: '#app',
  components: { App },
  template: '<App/>',
  router,
  provide: {
    loginService: () => loginService,
    activateService: () => new ActivateService(),
    registerService: () => new RegisterService(),
    userService: () => new UserManagementService(),
    healthService: () => new HealthService(),
    configurationService: () => new ConfigurationService(),
    logsService: () => new LogsService(),
    metricsService: () => new MetricsService(),

    userOAuth2Service: () => new UserOAuth2Service(),
    translationService: () => translationService,
    // jhipster-needle-add-entity-service-to-main - JHipster will import entities services here
    accountService: () => accountService,
    phoneService: () => new PhoneService(),
    connectomeService: () => new ConnectomeService(store),
    // faqService: () => new FaqService(),
    feedService: () => new FeedService(),
    qnaService: () => new QnaService(),
    publicService: () => new PublicService(),
    peopleService: () => new PeopleService(),
    recentSearchService: () => new RecentSearchService(),
    principalService: () => new PrincipalService(),
    interactionUserService: () => new InteractionUserService(),
    commentService: () => new CommentService(),
    notificationService: () => new NotificationService(),
    websocketService: () => websocketService,
    fileStorageService: () => new FileStorageService(),
    learningService: () => new LearningService(),
    signalService: () => new SignalService(),
    cacheService: () => new CacheService(),
    learnedDocumentService: () => new LearnedDocumentService(),
    detailFeedService: () => new DetailFeedService(),
    userSettingService: () => new UserSettingService(),
  },
  i18n,
  store,
});

const eventHub = new Vue();
Vue.mixin({
  data: function () {
    return {
      eventHub: eventHub,
    };
  },
});
