import { email } from 'vuelidate/lib/validators';
import { Component, Inject, Prop, PropSync, Vue } from 'vue-property-decorator';
import RegisterService from '@/account/register/register.service';
import { getCunrentTimeZoneOffset } from '@/shared/constants/ds-constants';

@Component({})
export default class DsEmailValidAuth extends Vue {
  @Prop(Object) readonly formModel: any | undefined;
  @Prop(Number) loginFailedAttempt: any | undefined;
  @PropSync('isValid', Boolean) isValidSync;
  @Inject('registerService') private registerService: () => RegisterService;
  @Prop(Boolean) readonly selectedLogin: boolean | false;
  @Prop(String) page;

  private email = '';
  public countDown;
  public minLeft: any = '05';
  public secLeft: any = '00';
  public checkEmailIsExisted = false;
  public validateEmail = false;
  private maxlength = false;
  private confirmEmailCode = '';
  private checkEmailIsNotExisted = false;
  private isCodeSent = false;
  private disabledConfirm = true;
  private disableSend = false;
  private confirmCode = false;
  private isSended = false;

  public checkEmail(email) {
    const checkEmail = new RegExp('^[a-zA-Z0-9_.$]+[\\@][a-zA-Z0-9.-]+[\\.][a-zA-Z]+$');
    if (checkEmail.test(email)) {
      this.validateEmail = false;
    } else {
      this.validateEmail = true;
    }
    if (!this.email) {
      this.checkEmailIsExisted = false;
      this.checkEmailIsNotExisted = false;
      this.validateEmail = false;
    }
  }

  sendCodeEnter() {
    if (!this.validateEmail && !this.disableSend && this.email) {
      this.sendEmail();
    }
  }

  //send email
  public sendEmail() {
    if (this.mailOld && this.mailOld == this.email) {
      if (this.minLeft === '00' && this.secLeft === '00') this.isSended = false;
    } else {
      this.resetTime();
      clearInterval(this.timeDown);
    }
    this.checkEmailCorrect = false;
    this.checkEmailIsNotExisted = false;
    this.checkEmailIsExisted = false;
    this.validateEmail = false;
    if (!this.validateEmail && !this.isSended) {
      this.registerService()
        .checkUsernamExisted(this.email)
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
              this.checkEmailIsExisted = true;
            } else {
              this.sendEmailSuccess();
            }
          } else {
            this.formModel.login = this.email;
            if (isLogin) {
              this.checkEmailIsNotExisted = true;
            } else {
              this.sendEmailSuccess();
            }
          }
        });
    }
  }
  mailOld;
  sendEmailSuccess() {
    this.disableSend = true;
    this.formModel.email = this.email;
    this.registerService()
      .sendEmailService(this.formModel)
      .then(res => {
        this.disableSend = false;
        this.isValidSync = true;
        this.checkEmailIsExisted = false;
        this.isCodeSent = true;
        this.disabledConfirm = false;
        this.$nextTick(() => {
          // @ts-ignore
          this.$refs['emailCode'].focus();
        });
        clearTimeout(this.countDown);
        this.countDownTimer(299);
        this.isSended = true;
        this.mailOld = this.email;
      })
      .catch(error => {
        this.checkEmailIsExisted = true;
        const err = error.response.data;
        // this.$bvToast.toast(err.message, {
        //   toaster: 'b-toaster-bottom-right',
        //   title: err.errorKey,
        //   variant: 'danger',
        //   solid: true,
        //   autoHideDelay: 5000,
        // });
      });
  }

  checkEmailCorrect = false;
  public confirm() {
    this.checkEmailCorrect = false;
    if (this.email != this.formModel.email) {
      this.resetTime();
      this.disableSend = false;
      this.checkEmailCorrect = true;
      clearInterval(this.timeDown);
      return;
    }

    this.confirmCode = false;
    if (this.formModel.emailCode.length >= 50) {
      this.maxlength = true;
    } else {
      this.maxlength = false;
    }

    if (!this.maxlength) {
      this.formModel.emailCode = this.confirmEmailCode;
      this.formModel.timeZone = getCunrentTimeZoneOffset();
      this.registerService()
        .verifyEmailCode(this.formModel)
        .then(res => {
          // @ts-ignore
          this.$parent.callback(this.formModel.login, this.confirmEmailCode);
          clearTimeout(this.countDown);
          // this.buttonEmail = false;
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
  }

  timeDown;
  public countDownTimer(countDownTime: number) {
    if (countDownTime >= 0) {
      this.timeDown = this.countDown = setTimeout(() => {
        this.minLeft = '0' + Math.floor(countDownTime / 60);
        this.secLeft = Math.floor(countDownTime % 60);
        this.secLeft = this.secLeft < 10 ? '0' + this.secLeft : this.secLeft;

        countDownTime--;
        this.countDownTimer(countDownTime);
      }, 1000);
      if (countDownTime == 0) this.isCodeSent = false;
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

  resetTime() {
    this.isSended = false;
    this.minLeft = '05';
    this.secLeft = '00';
    this.isCodeSent = false;
  }
}
