import { Vue, Inject } from 'vue-property-decorator';
import Component from 'vue-class-component';
import AccountService from '@/account/account.service';
import ConnectomeService from '@/entities/connectome/connectome.service';
import axios from 'axios';
import { axiosRequestLoader } from '@/shared/config/loader-interceptors';
@Component
export default class qrCodeCheck extends Vue {
  @Inject('connectomeService')
  private connectomeService: () => ConnectomeService;
  @Inject('accountService')
  private accountService: () => AccountService;

  public token: any;
  public login: any;
  public connectomes = [];
  public connectomeSelected = '';
  private cnt = 0;

  mounted() {
    this.token = this.$route.query.token;
    this.login = this.$route.query.login;

    sessionStorage.setItem('ds-authenticationToken', this.token);

    this.connectomeService()
      .getConnectomeByLogin(this.login)
      .then(res => {
        console.log('conn res : ', res);
        this.connectomes = res.data;
        this.connectomeSelected = this.connectomes[0];

        this.selectConnectome(this.connectomes[0].connectomeId);
      });
  }
  public selectConnectome(connectomeId) {
    this.connectomes.forEach(item => {
      if (item.connectomeId == connectomeId) {
        this.connectomeSelected = item;
      }
    });
    localStorage.setItem('ds-connectome', JSON.stringify(this.connectomeSelected));

    this.$router.push('/feed');
    if (!localStorage.getItem('qrRefresh')) {
      localStorage.setItem('qrRefresh', 'false');
      location.reload();
    }
  }
}
