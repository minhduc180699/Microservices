import { Component, Inject, Vue } from 'vue-property-decorator';
import AccountService from '@/account/account.service';

@Component({
  watch: {
    $route(value) {
      this.handleActiveLayer(value);
    },
  },
})
export default class DsFooter extends Vue {
  @Inject('accountService') private accountService: () => AccountService;
  private isShow = false;
  private isMobile = false;
  private isUserManagement = false;
  private checkInMyAi = false;
  private hasAnyAuthorityValue = false;

  mounted() {
    this.scrolling();
    window.addEventListener('mousewheel', this.scrolling);
    window.addEventListener('touchmove', this.scrolling);
    window.addEventListener('scroll', () => {
      this.showHideButtonScroll();
    });
  }

  public scrolling(): void {
    const html = document.getElementsByTagName('html')[0];
    let gnbTop = $('#header').height();
    gnbTop = Number(gnbTop);

    if (window.scrollY > 50) {
      html.classList.add('is-scrolled');
    } else {
      html.classList.remove('is-scrolled');
    }

    //scrollbar bottom check
    if (window.scrollY + window.innerHeight > document.body.scrollHeight - 50) {
      html.classList.add('is-bottom');
    } else {
      html.classList.remove('is-bottom');
    }
  }

  scrollToTop() {
    document.documentElement.scrollTop = 0;
  }

  openSiteSaltlux() {
    const language = this.$store.getters.currentLanguage;
    const link = 'http://www.saltlux.com/index.do?lang=' + language;
    window.open(link, '_target');
  }

  showHideButtonScroll() {
    if (window.scrollY > 250) {
      this.isShow = true;
    } else {
      this.isShow = false;
    }
  }

  showMobileLayer() {
    if (this.isUserManagement) {
      this.isUserManagement = false;
    }
    this.isMobile = !this.isMobile;
  }

  showUserManagement() {
    if (this.isMobile) {
      this.isMobile = false;
    }
    this.isUserManagement = !this.isUserManagement;
  }

  changeTab(router) {
    if (router) {
      this.$router.push(router);
    }
    this.isMobile = false;
    this.isUserManagement = false;
  }

  handleActiveLayer(value) {
    if (value.path.includes('my-ai')) {
      this.checkInMyAi = true;
    } else {
      this.checkInMyAi = false;
    }
  }

  public hasAnyAuthority(authorities: any): boolean {
    this.accountService()
      .hasAnyAuthorityAndCheckAuth(authorities)
      .then(value => {
        this.hasAnyAuthorityValue = value;
      });
    return this.hasAnyAuthorityValue;
  }
}
