import { Inject, Vue } from 'vue-property-decorator';
import Component from 'vue-class-component';
import QrcodeVue from 'qrcode.vue';
import UserManagementService from '@/admin/user-management/user-management.service';

@Component({
  components: {
    QrcodeVue: QrcodeVue,
  },
})
export default class qrCode extends Vue {
  @Inject('userService') private userManagementService: () => UserManagementService;

  data() {
    const token = localStorage.getItem('ds-authenticationToken') || sessionStorage.getItem('ds-authenticationToken');
    const login = this.$store.getters.account.login;

    return {
      value: 'http://dev.deepsignal.ai/qrCodeCheck?token=' + token + '&login=' + login,
      size: 150,
    };
  }
}
