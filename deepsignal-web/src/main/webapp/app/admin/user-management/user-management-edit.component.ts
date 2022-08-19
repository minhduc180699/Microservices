import { email, maxLength, minLength, required } from 'vuelidate/lib/validators';
import { Component, Inject, Vue } from 'vue-property-decorator';
import UserManagementService from './user-management.service';
import { IUser, User } from '@/shared/model/user.model';

const loginValidator = (value: string) => {
  if (!value) {
    return true;
  }
  return /^[a-zA-Z0-9!$&*+=?^_`{|}~.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$|^[_.@A-Za-z0-9-]+$/.test(value);
};

const validations: any = {
  userAccount: {
    login: {
      // required,
      maxLength: maxLength(254),
      pattern: loginValidator,
    },
    firstName: {
      maxLength: maxLength(50),
    },
    lastName: {
      maxLength: maxLength(50),
    },
    email: {
      required,
      email,
      minLength: minLength(5),
      maxLength: maxLength(50),
    },
  },
};

@Component({
  validations,
})
export default class DsUserManagementEdit extends Vue {
  @Inject('userService') private userManagementService: () => UserManagementService;
  public userAccount: IUser;
  public isSaving = false;
  public authorities: any[] = [];
  public languages: any = this.$store.getters.languages;
  private isActive = true;
  private isCheckEmail = false;

  beforeRouteEnter(to, from, next) {
    next(vm => {
      vm.initAuthorities();
      if (to.params.login) {
        vm.init(to.params.login);
      }
    });
  }

  public constructor() {
    super();
    this.userAccount = new User();
    this.userAccount.authorities = [];
  }

  public initAuthorities() {
    this.userManagementService()
      .retrieveAuthorities()
      .then(_res => {
        this.authorities = _res.data;
      });
  }

  public init(login): void {
    this.userManagementService()
      .getUserInfo(login)
      .then(res => {
        this.userAccount = res.data;
        console.log('user-account', this.userAccount);
        if (this.userAccount.activated == 0) {
          this.isActive = false;
        } else {
          this.isActive = true;
        }
      });
  }

  public previousState(): void {
    (<any>this).$router.go(-1);
  }

  public save(): void {
    this.isCheckEmail = false;
    this.isSaving = true;
    if (this.isActive) {
      this.userAccount.activated = 1;
    } else {
      this.userAccount.activated = 0;
    }
    if (this.userAccount.id) {
      this.userManagementService()
        .update(this.userAccount)
        .then(res => {
          this.returnToList();
          this.$root.$bvToast.toast(this.getMessageFromHeader(res).toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Info',
            variant: 'info',
            solid: true,
            autoHideDelay: 5000,
          });
        });
    } else {
      this.userAccount.login = this.userAccount.email;
      this.userManagementService()
        .create(this.userAccount)
        .then(res => {
          this.returnToList();
          this.$root.$bvToast.toast(this.getMessageFromHeader(res).toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Success',
            variant: 'success',
            solid: true,
            autoHideDelay: 5000,
          });
        })
        .catch(error => {
          this.isCheckEmail = true;
        });
    }
  }

  private returnToList(): void {
    this.isSaving = false;
    this.isCheckEmail = false;
    (<any>this).$router.go(-1);
  }

  private getMessageFromHeader(res: any): any {
    return this.$t(res.headers['x-deepsignalapp-alert'], {
      param: decodeURIComponent(res.headers['x-deepsignalapp-params'].replace(/\+/g, ' ')),
    });
  }
}
