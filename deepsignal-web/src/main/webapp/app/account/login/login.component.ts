import axios from 'axios';
import Component from 'vue-class-component';
import { Vue, Inject } from 'vue-property-decorator';
import AccountService from '@/account/account.service';
import ConnectomeService from '@/entities/connectome/connectome.service';
import DsPhoneValidAuth from '@/account/phone-valid-auth/ds-phone-valid-auth.vue';
import vuescroll from 'vuescroll';
import TranslationService from '@/locale/translation.service';
import DsEmailValidAuth from '@/account/email-valid-auth/ds-email-valid-auth.vue';
import PublicService from '@/service/public.service';
import { namespace } from 'vuex-class';

const appStoreState = namespace('appStore');

@Component({
  components: {
    'ds-phone-valid-auth': DsPhoneValidAuth,
    'vue-scroll': vuescroll,
    'ds-email-valid-auth': DsEmailValidAuth,
  },
})
export default class LoginForm extends Vue {
  @Inject('accountService')
  private accountService: () => AccountService;

  @Inject('connectomeService')
  private connectomeService: () => ConnectomeService;

  @Inject('translationService') private translationService: () => TranslationService;

  @Inject('publicService')
  private publicService: () => PublicService;
  @appStoreState.Action
  public changePage!: (payload: { isLoginPage: boolean }) => void;

  private loginFailedAttempt = 0;
  private langKey = 'en';
  private currentLanguage = this.$store.getters.currentLanguage;
  private languages: any = this.$store.getters.languages;
  public authenticationError = null;
  public jwt = null;
  public rememberMe = false;
  public connectomes = [];
  public connectomeSelected = '';
  public isValid = false;
  public isShowConnectome = false;
  private selectedLogin = true;

  public ops = {
    vuescroll: {
      mode: 'native',
      sizeStrategy: 'percent',
      detectResize: true,
      /** Enable locking to the main axis if user moves only slightly on one of them at start */
      locking: true,
    },
    scrollPanel: {
      initialScrollY: false,
      initialScrollX: false,
      // scrollingX: true,
      scrollingY: true,
      speed: 300,
      easing: undefined,
      verticalNativeBarPos: 'right',
    },
    rail: {},
    bar: {
      showDelay: 500,
      onlyShowBarOnScroll: true,
      keepShow: true,
      background: '#c1c1c1',
      opacity: 1,
      hoverStyle: false,
      specifyBorderRadius: false,
      minSize: 0,
      size: '6px',
      disable: false,
    },
    scrollButton: {},
  };
  // Test phone number: 5571981265131
  public formModel = {
    countryCode: '',
    phoneNumber: '',
    type: 1,
    code: '',
    email: '',
    emailCode: '',
    login: '',
    timeZone: '',
  };

  public isLoginQR = true;

  private swiperOptions = {
    effect: 'fade',
    pagination: {
      el: '.swiper-pagination',
      clickable: true,
      renderBullet(index, className) {
        return '<span class="' + className + '">0' + (index + 1) + '</span>';
      },
    },
  };

  public changeTypeLogin() {
    this.isLoginQR = !this.isLoginQR;
  }

  confirm() {
    this.isValid = false;
    if (this.selectedLogin) {
      // @ts-ignore
      this.$refs.dsPhoneComponent.confirm();
    } else {
      // @ts-ignore
      this.$refs.dsEmailComponent.confirm();
    }
  }

  public doLogin(loginName, code) {
    const language = this.$i18n.locale;
    const data = { username: loginName, password: code, rememberMe: this.rememberMe, language, deviceId: '' };

    axios
      .post('api/authenticate', data)
      .then(result => {
        const bearerToken = result.headers.authorization;
        if (bearerToken && bearerToken.slice(0, 7) === 'Bearer ') {
          this.jwt = bearerToken.slice(7, bearerToken.length);
          this.isShowConnectome = false;
          if (this.jwt != null) {
            if (this.rememberMe) {
              localStorage.setItem('ds-authenticationToken', this.jwt);
              sessionStorage.removeItem('ds-authenticationToken');
            } else {
              sessionStorage.setItem('ds-authenticationToken', this.jwt);
              localStorage.removeItem('ds-authenticationToken');
            }
          }
          this.processAfterLoginSuccess(loginName);
        }
        this.authenticationError = false;

        this.publicService()
          .getIpFromClient()
          .then(ip => {
            axios.put('api/admin/users/updateLastLogin?login=' + loginName + '&ip=' + ip);
          });
      })
      .catch(() => {
        this.authenticationError = true;
        this.isValid = true;
      });
  }

  public processAfterLoginSuccess(login: string) {
    this.connectomeService()
      .getConnectomeByLogin(login)
      .then(res => {
        if (res.data.length > 0) {
          this.connectomeSelected = res.data[0];
          localStorage.setItem('ds-connectome', JSON.stringify(this.connectomeSelected));
          this.authenticationError = false;
          // this.callExternalAPI(res.data[0].connectomeId).then();
          setTimeout(() => {
            // redirect to home feed page
            this.accountService().retrieveAccount();

            // @ts-ignore
            if (this.$router.history.current.name == 'Login') {
              this.$router.push('/feed');
              location.reload();
            }
          }, 1000);
        } else {
          this.$router.push('/register');
          setTimeout(() => {
            this.$bvModal.show('modal-register-success');
            this.$root.$emit('setLogin', login);
          }, 100);
        }
        this.isValid = true;
      });
  }

  public callback(loginName, code) {
    this.doLogin(loginName, code);
  }

  public isActiveLanguage(key: string): boolean {
    return key === this.$store.getters.currentLanguage;
  }

  public changeLanguage(newLanguage: string): void {
    this.currentLanguage = newLanguage;
    this.translationService().refreshTranslation(newLanguage);
    this.$store.dispatch('connectomeNetworkStore/shiftConnectomeByLanguage', newLanguage);
  }

  public logout(): void {
    localStorage.removeItem('ds-authenticationToken');
    sessionStorage.removeItem('ds-authenticationToken');
    localStorage.removeItem('ds-connectome');
    localStorage.removeItem('openLearningCenter');
    sessionStorage.setItem('requested-url', this.$route.fullPath);
    this.$store.dispatch('connectomeNetworkStore/logout');
    this.$store.dispatch('collectionsManagerStore/logout');
    this.$store.commit('logout');
    this.$root.$emit('bv::hide::modal', 'modal-my-info');
    this.$root.$emit('bv::show::modal', 'modal-logged-out');
  }

  routeToRegister(navigate, e) {
    this.changePage({ isLoginPage: false });
    navigate(e);
  }

  mounted() {
    document.body.setAttribute('data-menu', 'login');
  }

  handleTypeLogin(type) {
    this.selectedLogin = type;
  }
}
