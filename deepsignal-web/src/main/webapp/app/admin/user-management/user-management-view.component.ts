import Vue from 'vue';
import { Component, Inject } from 'vue-property-decorator';
import UserManagementService from './user-management.service';

@Component
export default class DsUserManagementView extends Vue {
  @Inject('userService') private userManagementService: () => UserManagementService;
  public user: any = null;

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.login) {
        vm.init(to.params.login);
      }
    });
  }
  public init(login): void {
    this.userManagementService()
      .get(login)
      .then(res => {
        this.user = res.data;
      });
  }
}
