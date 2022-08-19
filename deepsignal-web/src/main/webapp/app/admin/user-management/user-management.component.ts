import { Component, Inject, Vue } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import UserManagementService from './user-management.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class DsUserManagementComponent extends Vue {
  @Inject('userService') private userManagementService: () => UserManagementService;
  public error = '';
  public success = '';
  public users: any[] = [];
  public itemsPerPage = 20;
  public queryCount: number = null;
  public page = 1;
  public previousPage = 1;
  public propOrder = 'id';
  public reverse = false;
  public totalItems = 0;
  public isLoading = false;
  public removeId: number = null;
  private keyword = '';
  private results = false;
  private keySearch = '';
  private isSearching = false;

  public mounted(): void {
    this.loadAll();
  }

  public setActive(user, isActivated): void {
    if (isActivated) {
      user.activated = 1;
    } else {
      user.activated = 0;
    }
    this.userManagementService()
      .update(user)
      .then(() => {
        this.error = null;
        this.success = 'OK';
        if (isActivated) {
          this.userManagementService().sendEmailNoticeActivation(user.id).then().catch();
        }
        this.loadAll();
      })
      .catch(() => {
        this.success = null;
        this.error = 'ERROR';
        user.activated = false;
      });
  }

  public loadAll(): void {
    this.isSearching = false;
    this.isLoading = true;

    this.userManagementService()
      .retrieve({
        page: this.page - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .then(res => {
        this.isLoading = false;
        this.users = res.data;
        this.totalItems = Number(res.headers['x-total-count']);
        this.queryCount = this.totalItems;
      })
      .catch(() => {
        this.isLoading = false;
      });
  }

  public handleSyncList(): void {
    if (this.isSearching) {
      this.searchUser(this.previousPage);
      return;
    }
    this.loadAll();
  }

  public sort(): any {
    const result = [this.propOrder + ',' + (this.reverse ? 'desc' : 'asc')];
    if (this.propOrder !== 'id') {
      result.push('id');
    }
    return result;
  }

  public loadPage(page: number): void {
    if (page !== this.previousPage) {
      this.previousPage = page;
      if (this.isSearching) {
        this.searchUser(page);
      } else {
        this.transition();
      }
    }
  }

  public transition(): void {
    this.loadAll();
  }

  public changeOrder(propOrder: string): void {
    this.propOrder = propOrder;
    this.reverse = !this.reverse;
    this.transition();
  }

  public deleteUser(): void {
    this.userManagementService()
      .remove(this.removeId)
      .then(res => {
        const message = this.$t(res.headers['x-deepsignalapp-alert'], {
          param: decodeURIComponent(res.headers['x-deepsignalapp-params'].replace(/\+/g, ' ')),
        });
        this.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
        this.removeId = null;
        this.loadAll();
        this.closeDialog();
      });
  }

  public prepareRemove(instance): void {
    this.removeId = instance.login;
    if (<any>this.$refs.removeUser) {
      (<any>this.$refs.removeUser).show();
    }
  }

  deleteUserByAdmin(id) {
    if (!id || id == '') {
      return;
    }
    this.userManagementService()
      .deleteUserByAdmin(id)
      .then(res => {
        this.$bvToast.toast('Delete successfully', {
          toaster: 'b-toaster-top-right',
          title: 'Info',
          variant: 'success',
          solid: true,
          autoHideDelay: 5000,
        });
        this.loadAll();
      })
      .catch(error => {
        this.$bvToast.toast('Delete failed', {
          toaster: 'b-toaster-top-right',
          title: 'Info',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
        this.loadAll();
      });
  }

  public closeDialog(): void {
    if (<any>this.$refs.removeUser) {
      (<any>this.$refs.removeUser).hide();
    }
  }

  public get username(): string {
    return this.$store.getters.account ? this.$store.getters.account.login : '';
  }

  public searchUser(page?) {
    this.users = [];
    if (this.keyword === '' || !this.keyword) {
      this.loadAll();
      return;
    }
    this.isSearching = true;
    if (this.keyword != this.keySearch) {
      this.page = 1;
      this.keySearch = this.keyword;
    } else {
      if (page && page > 0) {
        this.page = page;
      }
    }
    this.results = true;
    this.userManagementService()
      .searchUser(this.keySearch, this.page)
      .then(res => {
        this.users = res.data.userManagement;
        this.queryCount = res.data.count;
        this.results = false;
        this.totalItems = this.queryCount;
      })
      .catch(error => {
        this.results = false;
      });
  }
}
