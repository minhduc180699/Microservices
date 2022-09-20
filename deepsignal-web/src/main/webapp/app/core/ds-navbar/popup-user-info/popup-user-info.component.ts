import { Vue, Component, Inject, Prop, PropSync } from 'vue-property-decorator';
import AccountService from '@/account/account.service';
import axios from 'axios';
import PhoneService from '@/account/phone.service';
import { maxLength, minLength, required } from 'vuelidate/lib/validators';
import { IUserModel, UserModel } from '@/shared/model/users.model';
import TranslationService from '@/locale/translation.service';
import PublicService from '@/service/public.service';

const validations = {
  formModel: {
    phoneNumber: {
      // required,
      minLength: minLength(1),
      maxLength: maxLength(50),
    },
    code: {
      required,
      minLength: minLength(1),
      maxLength: maxLength(50),
    },
  },
  firstName: {
    maxLength: maxLength(50),
  },
  lastName: {
    maxLength: maxLength(50),
  },
};

@Component({
  validations,
  computed: {
    getCurrentLanguage() {
      return this.$store.getters.currentLanguage;
    },
  },
  watch: {
    getCurrentLanguage(value) {
      if (value != this.langKey) {
        this.langKey = value;
        this.updateUser(value);
      }
      this.language = this.$store.getters.languages[value].name;
    },
  },
})
export default class PopupUserInfoComponent extends Vue {
  @Inject('translationService') private translationService: () => TranslationService;
  @Inject('publicService') private publicService: () => PublicService;

  private languages: any = this.$store.getters.languages;
  private language;
  private phoneNumber = '';
  private langKey = '';
  private countryCode;
  private email = '';
  public imageUrl = null;
  private countries = [];
  private countrySelect = null;
  private isSendingCode = false;
  private sending = false;
  private readonlyInput = true;
  private formModel = {
    phoneNumber: '',
    code: '',
  };
  private SEND_CODE_TIMEOUT = 180;
  private countDown;
  private minLeft: any = '03';
  private secLeft: any = '00';
  private oldPhoneNumber;
  private id;
  private connectomeId;
  private user;
  private fileAvatar;
  private firstName = '';
  private lastName = '';

  @Prop() isDeleteUser;
  @PropSync('avatar') avatarSync;

  @Inject('accountService') private accountService: () => AccountService;
  @Inject('phoneService') private phoneService: () => PhoneService;

  created() {
    this.langKey = this.$store.getters.currentLanguage;
    this.language = this.$store.getters.languages[this.langKey].name;
  }

  onOpenPopUpUserInfo() {
    if (localStorage.getItem('ds-connectome')) {
      const connectome = JSON.parse(localStorage.getItem('ds-connectome'));
      this.id = connectome.user.id;
      this.connectomeId = connectome.connectomeId;
    }
    this.getCountryCode();
  }

  public getUserById() {
    axios.get('api/getUserById?id=' + this.id).then(res => {
      this.user = res.data;
      this.fillPopupUserInfo(res.data);
      if (this.user.phoneCountry && this.user.phoneNumber) {
        this.countrySelect = this.countries.find(item => item.code === this.user.phoneCountry);
      }
    });
  }

  public getCurrentUser() {
    axios.get('api/account').then(res => {
      this.id = res.data.id;
      this.getUserById();
    });
  }

  // public getCurrentLangName(): string {
  //   return this.$store.getters.languages[this.langKey].name;
  // }

  public isActiveLanguage(key: string): boolean {
    return key === this.$store.getters.currentLanguage;
  }

  public changeLanguage(newLanguage: string): void {
    this.langKey = newLanguage;
    this.$store.commit('currentLanguage', newLanguage);
    this.language = this.$store.getters.languages[newLanguage].name;
    this.translationService().refreshTranslation(newLanguage);
    this.$store.dispatch('connectomeNetworkStore/shiftConnectomeByLanguage', newLanguage);
    this.$root.$emit('update-new-lang', newLanguage);
  }

  public getCountryCode() {
    this.publicService()
      .getCountryCode()
      .then(res => {
        this.countries = res.data;
        this.countrySelect = this.countries[121];
        this.id ? this.getUserById() : this.getCurrentUser();
        // console.log('country = ', this.countries);
      });
  }

  public fillPopupUserInfo(account) {
    this.firstName = account.firstName;
    this.lastName = account.lastName;
    this.formModel.phoneNumber = account.phoneNumber;
    this.email = account.email;
    this.avatarSync = account.imageUrl;
    this.imageUrl = account.imageUrl;
    this.oldPhoneNumber = account.phoneNumber;
  }

  public openPopupUpload() {
    const fileUpload = document.getElementById('fileUpload') as HTMLInputElement;
    fileUpload.click();
  }

  public blobToBase64(blob) {
    return new Promise((resolve, _) => {
      const reader = new FileReader();
      reader.onloadend = () => resolve(reader.result);
      reader.readAsDataURL(blob);
    });
  }

  public uploadAvatar(e) {
    this.blobToBase64(e.target.files[0]).then(res => {
      this.imageUrl = res;
      // console.log('base64 image = ', this.imageUrl);
    });
    this.fileAvatar = e.target.files[0];
  }

  public sendCode() {
    // @ts-ignore
    this.formModel.countryCode = this.countrySelect.code;
    this.isSendingCode = false;
    // @ts-ignore
    if (!this.formModel.countryCode || !this.formModel.phoneNumber) {
      this.isSendingCode = true;
      return;
    }
    this.sending = true;
    // this.phoneService()
    // // @ts-ignore
    //   .checkUserExisted(this.formModel.phoneNumber)
    //   .then(res => {
    this.handleSendCode();
    // })
    // .catch(error => {
    //   const err = error.response.data;
    //   this.readonlyInput = true;
    //   this.$bvToast.toast(err.message, {
    //     toaster: 'b-toaster-bottom-right',
    //     title: err.code,
    //     variant: 'danger',
    //     solid: true,
    //     autoHideDelay: 5000,
    //   });
    // });
  }

  public handleSendCode() {
    this.phoneService()
      .send(this.formModel)
      .then(res => {
        // @ts-ignore
        this.formModel.code = res.data.code;
        this.readonlyInput = false;
        this.sending = false;
        this.$nextTick(() => {
          // @ts-ignore
          this.$refs['txtCode'].focus();
        });
        clearTimeout(this.countDown);
        this.countDownTimer(this.SEND_CODE_TIMEOUT - 1);
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

  public countDownTimer(countDownTime: number) {
    if (countDownTime >= 0) {
      this.countDown = setTimeout(() => {
        this.minLeft = '0' + Math.floor(countDownTime / 60);
        this.secLeft = Math.floor(countDownTime % 60);
        this.secLeft = this.secLeft < 10 ? '0' + this.secLeft : this.secLeft;

        countDownTime--;
        this.countDownTimer(countDownTime);
      }, 1000);
    }
  }

  public changePhoneNumber() {
    // @ts-ignore
    this.formModel.login = JSON.parse(localStorage.getItem('ds-connectome')).user.login;
    this.phoneService()
      .confirm(this.formModel)
      .then(res => {
        this.getCurrentUser();
        this.formModel.code = '';
        this.readonlyInput = false;
        clearTimeout(this.countDown);
        this.$bvToast.toast('Edit Phone Success', {
          toaster: 'b-toaster-bottom-right',
          title: res.status,
          variant: 'success',
          solid: true,
          autoHideDelay: 5000,
        });
      })
      .catch(error => {
        const err = error.response.data;
        this.$bvToast.toast(err.message, {
          toaster: 'b-toaster-bottom-right',
          title: err.code,
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
      });
  }

  public createUser(): IUserModel {
    return {
      ...new UserModel(),
      id: this.user.id,
      login: this.user.login,
      password: this.user.password,
      firstName: this.firstName,
      lastName: this.lastName,
      email: this.email,
      phoneCountry: this.countrySelect.code,
      phoneNumber: this.formModel.phoneNumber,
      imageUrl: '',
      activated: this.user.activated,
      langKey: this.langKey,
      activationKey: this.user.activationKey,
      resetKey: this.user.resetKey,
      createdBy: this.user.createdBy,
      // createdDate: this.user.createdDate,
      // resetDate: this.user.resetDate,
      lastModifiedBy: this.user.lastModifiedBy,
      // lastModifiedDate: this.user.lastModifiedDate,
      termOfService: this.user.term_of_service,
      receiveNews: this.user.receive_news,
    };
  }

  public updateUser(language?) {
    const formData = new FormData();
    formData.append('file', this.fileAvatar);
    const user = this.createUser();
    formData.append('user', JSON.stringify(user));
    formData.append('connectomeId', this.connectomeId);
    const config = {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    };
    axios
      .put<IUserModel>('/api/admin/users', formData)
      .then(res => {
        this.user = res.data;
        if (!language) {
          this.$bvModal.hide('modal-user-info');
          this.$bvToast.toast('Edit User Success', {
            toaster: 'b-toaster-bottom-right',
            title: 'Success',
            variant: 'success',
            solid: true,
            autoHideDelay: 5000,
          });
        }
      })
      .catch(err => {
        this.$bvToast.toast(err.response?.data?.title, {
          toaster: 'b-toaster-bottom-right',
          title: 'Error',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
      });
  }

  public closePopup() {
    this.fillPopupUserInfo(this.user);
  }

  public deleteUser() {
    axios
      .put(`/api/admin/users/${this.user.login}`)
      .then(res => {
        this.$bvToast.toast('Delete User Success', {
          toaster: 'b-toaster-bottom-right',
          title: res.status.toString(),
          variant: 'success',
          solid: true,
          autoHideDelay: 5000,
        });
        this.$root.$emit('bv::hide::modal', 'modal-user-info');
        this.$emit('update:isDeleteUser', true); // update prop isDeleteUser in parent by .sync
        this.$emit('delete');
      })
      .catch(error => {
        this.$bvToast.toast(error.message, {
          toaster: 'b-toaster-bottom-right',
          title: error.response.status,
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
      });
  }
}
