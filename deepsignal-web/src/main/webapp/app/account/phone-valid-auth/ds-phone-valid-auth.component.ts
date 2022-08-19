import { Component, Inject, Prop, PropSync, Vue, Watch } from 'vue-property-decorator';
import { PATTERN } from '@/constants';
import PhoneService from '@/account/phone.service';
import AccountService from '@/account/account.service';
import { maxLength, minLength, required } from 'vuelidate/lib/validators';
import RegisterService from '@/account/register/register.service';
import PublicService from '@/service/public.service';
import { LOCAL_STORAGE_CONSTANTS } from '@/shared/constants/ds-constants';
import { getCunrentTimeZoneOffset } from '@/shared/constants/ds-constants';

const validations = {
  formModel: {
    phoneNumber: {
      required,
      minLength: minLength(1),
      maxLength: maxLength(50),
    },
    code: {
      required,
      minLength: minLength(1),
      maxLength: maxLength(50),
    },
  },
};

@Component({
  validations,
})
export default class DsPhoneValidAuth extends Vue {
  @Inject('registerService') private registerService: () => RegisterService;
  @Inject('phoneService') private phoneService: () => PhoneService;
  @Inject('accountService') private accountService: () => AccountService;
  @Inject('publicService') private publicService: () => PublicService;
  // eslint-disable-next-line @typescript-eslint/ban-types
  @Prop(Object) readonly formModel: any | undefined;
  @Prop(Boolean) isShowConfirmBtn: boolean | false;
  @Prop(Number) loginFailedAttempt: any | undefined;
  @PropSync('isValid', Boolean) isValidSync;
  @Prop(Boolean) readonly selectedLogin: boolean | true;
  @Prop(String) page;

  SEND_CODE_TIMEOUT = 180;
  public countDown;
  public minLeft: any = '03';
  public secLeft: any = '00';
  public countries = [];
  public patternPhoneNumber = PATTERN.PHONE;
  public isSendingCode = false;
  public sending = false;
  public readonlyInput = true;
  public confirmed = false;
  public countrySelect = {
    phoneNumber: '',
    code: '',
  };
  private confirmCode = false;
  public checkPhoneNumberIsExisted = false;
  public checkPhoneNumberIsNotExisted = false;
  private checkRegex = true;
  private isSended = false;

  public getCountryCode(code?) {
    this.publicService()
      .getCountryCode()
      .then(res => {
        this.countries = res.data;
        if (code) {
          const indexOfCountry = this.countries.findIndex(item => item.code === code);
          this.countrySelect = this.countries[indexOfCountry];
        } else {
          this.countrySelect = this.countries[121];
        }
      });
  }

  public confirm() {
    this.confirmCode = false;
    this.formModel.timeZone = getCunrentTimeZoneOffset();
    this.phoneService()
      .confirm(this.formModel)
      .then(res => {
        this.confirmed = true;
        // @ts-ignore
        this.$parent.callback(this.formModel.login, this.formModel.code);
        this.readonlyInput = false;
        clearTimeout(this.countDown);
        this.$emit('update:loginFailedAttempt', 0);
      })
      .then(() => {
        const href = window.location.href;
        const page = href.split('/');
        if (page[page.length - 1] === 'register') {
          // @ts-ignore
          this.$parent.handleReceiving();
        }
      })
      .catch(error => {
        const err = error.response.data;
        this.$emit('update:loginFailedAttempt', err.loginFailedCount);
        this.confirmCode = true;
        this.isValidSync = true;
      });
  }
  phoneOld;
  handleSendCode() {
    this.checkPhoneNumberIsNotExisted = false;
    this.checkPhoneNumberIsExisted = false;
    this.phoneService()
      .send(this.formModel)
      .then(res => {
        this.isValidSync = true;
        this.formModel.code = res.data.code;
        this.readonlyInput = false;
        // this.sending = false;
        this.$nextTick(() => {
          // @ts-ignore
          this.$refs['txtCode'].focus();
        });
        clearTimeout(this.countDown);
        this.countDownTimer(this.SEND_CODE_TIMEOUT - 1);
        this.isSended = true;
        this.phoneOld = this.formModel.phoneNumber;
      })
      .catch(error => {
        const err = error.response.data;
        this.readonlyInput = true;
        this.$bvToast.toast(err.message, {
          toaster: 'b-toaster-bottom-right',
          title: err.code,
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
      });
  }

  public sendCodeEnter() {
    if (this.checkRegex && this.formModel.phoneNumber) {
      this.sendCode();
    }
  }

  public sendCode() {
    if (this.phoneOld && this.phoneOld == this.formModel.phoneNumber) {
      if (this.minLeft === '00' && this.secLeft === '00') this.isSended = false;
    } else {
      this.minLeft = '03';
      this.secLeft = '00';
      this.isSended = false;
      this.readonlyInput = true;
      clearInterval(this.countDown);
    }

    this.formModel.countryCode = this.countrySelect.code;
    this.isSendingCode = false;
    if (!this.formModel.countryCode || !this.formModel.phoneNumber || this.isSended) {
      this.isSendingCode = true;
      return;
    }
    this.sending = true;
    this.registerService()
      .checkUsernamExisted(this.formModel.phoneNumber)
      .then(res => {
        let isRegister = false;
        let isLogin = false;
        const href = window.location.href;
        const page = href.split('/');
        if (page[page.length - 1] === 'register') {
          isRegister = true;
        }
        if (page[page.length - 1] === 'login') {
          isLogin = true;
        }
        if (res.data) {
          this.formModel.login = res.data;
          if (isRegister) {
            this.checkPhoneNumberIsExisted = true;
          } else {
            this.handleSendCode();
          }
        } else {
          this.formModel.login = this.formModel.phoneNumber;
          if (isLogin) {
            this.checkPhoneNumberIsNotExisted = true;
          } else {
            this.handleSendCode();
          }
        }
      })
      .catch(error => {
        this.sending = false;
        const err = error.response.data;
        this.readonlyInput = true;
        this.$bvToast.toast(err.message, {
          toaster: 'b-toaster-bottom-right',
          title: err.code,
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
      });
  }
  public countDownTimer(countDownTime: number) {
    if (countDownTime >= 0) {
      this.countDown = setTimeout(() => {
        this.minLeft = '0' + Math.floor(countDownTime / 60);
        this.secLeft = Math.floor(countDownTime % 60);
        this.secLeft = this.secLeft < 10 ? '0' + this.secLeft : this.secLeft;
        if (countDownTime == 0) {
          this.sending = false;
        }
        countDownTime--;
        this.countDownTimer(countDownTime);
      }, 1000);
    }
  }

  public nextOrLogin() {
    if (this.page === 'login') {
      // @ts-ignore
      this.$parent.confirm();
    } else {
      // @ts-ignore
      this.$parent.receive();
    }
  }

  private getMessageFromHeader(res: any): any {
    return this.$t(res.headers['x-deepsignalapp-alert'], {
      param: decodeURIComponent(res.headers['x-deepsignalapp-params'].replace(/\+/g, ' ')),
    });
  }

  beforeMount() {
    this.getIpOfClient();
  }

  checkRegexPhoneNumber() {
    const pattern = new RegExp('\\b\\d{6,15}\\b');
    this.checkRegex = pattern.test(this.formModel.phoneNumber);
  }

  getIpOfClient() {
    this.publicService()
      .getIpFromClient()
      .then(res => {
        this.setIpToCache(res);
        this.getLocationByIp(res);
      });
  }

  setIpToCache(ip) {
    if (!ip) {
      return;
    }
    localStorage.setItem(LOCAL_STORAGE_CONSTANTS.IP, ip);
  }

  getLocationByIp(ip) {
    if (!ip) {
      return;
    }
    this.publicService()
      .getLocationByIp(ip)
      .then(res => {
        this.getCountryCode(res.data.countryCode);
      });
  }
}
