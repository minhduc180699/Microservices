import Vue from 'vue';
import { Component, Inject } from 'vue-property-decorator';
import { email, helpers, maxLength, minLength, required, sameAs } from 'vuelidate/lib/validators';
import LoginService from '@/account/login.service';
import RegisterService from '@/account/register/register.service';
import DsPhoneValidAuth from '@/account/phone-valid-auth/ds-phone-valid-auth.vue';
import { EMAIL_ALREADY_USED_TYPE, LOGIN_ALREADY_USED_TYPE } from '@/constants';
import AccountService from '@/account/account.service';
import axios from 'axios';
import ConnectomeService from '@/entities/connectome/connectome.service';
import any = jasmine.any;
import anything = jasmine.anything;
import DsEmailValidAuth from '@/account/email-valid-auth/ds-email-valid-auth.vue';
import TranslationService from '@/locale/translation.service';
import { Authority } from '@/shared/security/authority';
import TermsOfUse from '@/account/register/terms-of-use/terms-of-use.vue';

const validations: any = {
  formModel: {
    phoneNumber: {
      required,
      minLength: minLength(4),
      maxLength: maxLength(254),
    },
    email: {
      required,
      minLength: minLength(5),
      maxLength: maxLength(254),
      email,
    },
  },
  purposeSelected: {
    required,
    checked(val) {
      return val;
    },
  },
  aiName: {
    required,
    minLength: minLength(2),
    maxLength: maxLength(254),
  },
};

@Component({
  validations,
  components: {
    'ds-phone-valid-auth': DsPhoneValidAuth,
    'ds-email-valid-auth': DsEmailValidAuth,
    'terms-of-use': TermsOfUse,
  },
})
export default class Register extends Vue {
  @Inject('registerService') private registerService: () => RegisterService;
  @Inject('loginService') private loginService: () => LoginService;
  @Inject('accountService') private accountService: () => AccountService;
  @Inject('connectomeService') private connectomeService: () => ConnectomeService;
  @Inject('translationService') private translationService: () => TranslationService;
  public error = '';
  public errorEmailExists = '';
  public errorUserExists = '';
  public success = false;
  public step = 1;
  private isNext = 1;
  public policies = [];
  public isSelectAll = true;
  public purposes;
  public formModel = {
    countryCode: '',
    phoneNumber: '',
    email: '',
    code: '',
    termOfService: 1,
    receiveNews: 0,
    emailCode: '',
    purposeSet: [],
    nameConnectome: 'Warren',
    login: '',
    lastName: '',
    deleted: 0,
    langKey: Object.keys(this.$store.getters.languages)[0],
    authorities: [Authority.USER],
  };
  public purposeSelected = [];
  public aiName = '';
  public isConfirmSuccess = false;
  public isSendEmail = false;
  public validateEmail = false;
  public maxlength = false;
  public authenticationError = null;
  public connectomes = [];
  public connectomeSelected = '';
  public linkModal = 'Purpose';
  public validateModal = false;
  public minLeft = '05';
  public minleftt = 5;
  public secLeft = '00';
  public secLeftt = 59;
  public countDown;
  public disableButton = false;
  public buttonEmail = true;
  public buttonPurposes = false;
  public checkEmailIsExisted = false;
  public isAiAnimation = true;
  public urlImage = '';
  public file: any;
  private selectedLogin = true;
  public isValid = false;
  private checkName = false;
  private loginDeny = false;

  created() {
    this.getAllPurposes();
    this.$root.$on('setLogin', this.setLogin);
  }

  destroyed() {
    this.$root.$off('setLogin', this.setLogin);
  }

  data() {
    return {
      purposes: this.purposes,
    };
  }

  public setLogin(login) {
    this.formModel.login = login;
  }

  //Receive newsletters and messages
  public receive() {
    if (!this.formModel.lastName) {
      this.checkName = true;
    } else {
      this.checkName = false;
      if (this.selectedLogin) {
        // @ts-ignore
        this.$refs.confirmPhone.confirm();
      } else {
        // @ts-ignore
        this.$refs.confirmEmail.confirm();
      }
    }
  }

  public handleReceiving() {
    this.registerService()
      .userReceive(this.formModel)
      .then(res => {
        this.nextStep();
      })
      .catch(error => {
        const err = error.response.data;
        this.success = null;
        this.$bvToast.toast(err.message, {
          toaster: 'b-toaster-bottom-right',
          title: err.errorKey,
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
      });
  }

  public nextStep(): void {
    if (this.policies.length == 3) {
      this.formModel.receiveNews = 1;
    } else {
      this.formModel.receiveNews = 0;
    }
    this.step++;
    if (this.step >= this.isNext) {
      this.isNext++;
    }
  }

  public goStep(number): void {
    this.step = number;
  }

  public toggleAll(): void {
    if (this.policies.length == 3) {
      this.policies = [];
    } else {
      this.policies = ['option1', 'option2', 'option3'];
    }
  }

  public toggleSelect(): void {
    if (this.policies.length == 3) {
      this.isSelectAll = true;
    } else {
      this.isSelectAll = false;
    }
  }

  skipStep(item) {
    if (item <= this.isNext) {
      if (item != 1 && !(this.policies.includes('option1') && this.policies.includes('option2'))) return;
      this.step = item;
    }
  }

  public callback(login) {
    this.isConfirmSuccess = true;
  }

  public handleOk(): void {
    this.$router.push('/loginAccount');
  }

  //validate Email
  public checkEmail(email) {
    const checkEmail = new RegExp('^[a-zA-Z0-9_.$]+[\\@][a-zA-Z0-9.-]+[\\.][a-zA-Z]+$');
    if (checkEmail.test(email)) {
      this.isSendEmail = true;
      return true;
    } else {
      return false;
    }
  }

  //skip to learning center from modal in register after login
  skipToLearningCenter() {
    if (this.formModel.nameConnectome == '' || this.formModel.nameConnectome == null) {
      this.validateModal = true;
    } else {
      this.validateModal = false;
      if (!this.validateModal) {
        this.registerService()
          .getConnectome(this.formModel)
          .then(res => {
            if (this.file) {
              const data = new FormData();
              data.append('file', this.file);
              data.append('connectomeId', res.data);
              this.registerService()
                .uploadImageProfile(data)
                .then(res => {
                  //
                })
                .catch(error => {});
            }

            this.connectomeService()
              .getConnectomeByLogin(this.formModel.login)
              .then(res => {
                this.connectomes = res.data;
                this.connectomeSelected = this.connectomes[0];
                localStorage.setItem('ds-connectome', JSON.stringify(this.connectomeSelected));
                this.$bvModal.hide('modal-register-success');
                this.$router.push('/feed');
                // this.$emit('openLearningCenter');
              })
              .catch(error => {});
          })
          .catch(error => {
            const err = error.response.data;
            this.$bvToast.toast(err.message, {
              toaster: 'b-toaster-bottom-right',
              title: err.errorKey,
              variant: 'danger',
              solid: true,
              autoHideDelay: 5000,
            });
          });
      }
    }
  }

  getAllPurposes() {
    axios.get('/api/purpose/getAll').then(res => {
      this.purposes = res.data;
    });
  }

  // solve purposes checkbox
  handleCheckbox() {
    this.buttonPurposes = false;
    this.loginDeny = false;
    if (this.formModel.purposeSet.length == 0) {
      this.buttonPurposes = true;
    }

    if (!this.buttonPurposes) {
      this.registerService()
        .getPurposes(this.formModel)
        .then(res => {
          const data = {
            username: this.formModel.login,
            password: this.formModel.code || this.formModel.emailCode,
            rememberMe: true,
          };
          axios
            .post('api/authenticate', data)
            .then(result => {
              const bearerToken = result.headers.authorization;
              if (bearerToken && bearerToken.slice(0, 7) === 'Bearer ') {
                const jwt = bearerToken.slice(7, bearerToken.length);
                // sessionStorage.setItem('ds-authenticationToken', jwt);
                localStorage.setItem('ds-authenticationToken', jwt);
              }
              this.authenticationError = false;
              this.$bvModal.show('modal-register-success');
            })
            .catch(error => {
              this.authenticationError = true;
            });
        })
        .catch(error => {
          this.loginDeny = true;
          // const err = error.response.data;
          // this.$bvToast.toast(err.message, {
          //   toaster: 'b-toaster-bottom-right',
          //   title: err.errorKey,
          //   variant: 'danger',
          //   solid: true,
          //   autoHideDelay: 5000,
          // });
        });
    }

    // this.$bvModal.show("modal-register-success");
  }

  //change image profile
  changeImageProfile(e) {
    e.preventDefault();
    this.file = e.target.files[0] || e.dataTransfer.files[0];
    const reader = new FileReader();
    reader.readAsDataURL(this.file);
    reader.addEventListener('load', e => {
      const path = e.target.result.toString();
      $('#image-profile').attr('src', path);
    });
    this.isAiAnimation = false;
  }

  function() {
    /* Mouse tracking */
    const currentMousePos = { x: -1, y: -1 };
    const frameDimensions = { width: 0, height: 0 };
    const dirVector = { x: 0, y: 0 };
    const lookAmount = 30;
    document.addEventListener('mousemove', event => {
      currentMousePos.x = event.pageX;
      currentMousePos.y = event.pageY;

      frameDimensions.width = $(document).width();
      frameDimensions.height = $(document).height();

      dirVector.x = currentMousePos.x / frameDimensions.width;
      dirVector.y = currentMousePos.y / frameDimensions.height;

      $('.eyes .left, .eyes .right').css(
        'transform',
        'translate(' + (dirVector.x * lookAmount - 20) + 'px,' + (dirVector.y * lookAmount - 10) + 'px)'
      );
    });
  }

  mounted() {
    document.body.setAttribute('data-menu', 'login');
    this.function();
    this.policies = ['option1', 'option2', 'option3'];
  }

  selectPurposes(id) {
    if (this.formModel.purposeSet.length < 1) {
      this.formModel.purposeSet.push(id);
    } else {
      const index = this.formModel.purposeSet.indexOf(id);
      if (index > -1) {
        this.formModel.purposeSet.splice(index, 1);
      } else {
        this.formModel.purposeSet.push(id);
      }
    }
  }

  handleTypeLogin(type) {
    this.selectedLogin = type;
  }
}
