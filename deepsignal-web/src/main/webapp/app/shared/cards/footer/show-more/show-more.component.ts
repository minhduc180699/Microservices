import { Component, Inject, Prop, Vue, Watch } from 'vue-property-decorator';
import { PLATFORM } from '@/shared/constants/feed-constants';
import { InteractionUserService } from '@/service/interaction-user.service';
import axios from 'axios';

@Component({
  computed: {
    getPost() {
      return this.$store.getters.getPost;
    },
  },
  watch: {
    getPost(value) {
      this.handleShareFromDetail();
    },
  },
})
export default class ShowMore extends Vue {
  // variables
  private xCoordinate = 500;
  private yCoordinate = 500;
  private post = { url: '', title: '', writer: '', docId: '' };
  private textShare = '';
  private isSharingLink = false;
  private selected = '';
  private linkPublic = '';
  private isShowMore = false;
  private isCopy = false;
  private isBookmark = false;
  private connectomeId;
  private textSearchEmail = '';
  private resultSearch = [];
  private emailsSending = [];
  private isSendingEmail = true;
  private isLike = false;
  private isDislike = false;
  private checkTextShare = false;

  data() {
    return {
      selected: this.selected,
      options: [
        { value: 'PUBLIC', text: 'Public' },
        { value: 'RESTRICTED', text: 'Restricted' },
      ],
    };
  }

  created(): void {
    this.connectomeId = JSON.parse(localStorage.getItem('ds-connectome'))
      ? JSON.parse(localStorage.getItem('ds-connectome')).connectomeId
      : null;
  }

  @Inject('interactionUserService')
  private interactionUserService: () => InteractionUserService;

  public handleShare() {
    this.post = this.$store.getters.getPost;
    if (this.post) {
      this.isSharingLink = false;
      this.$bvModal.show('shareModal');
    }
  }

  public shareOnFacebook() {
    // @ts-ignore
    // const share = await FB.ui(
    //   {
    //     display: 'popup',
    //     method: 'share',
    //     href: this.post.sourceId,
    //     quote: this.textShare,
    //   },
    //   response => {
    //     if (response && !response.error_message) {
    //       this.$bvModal.hide('shareModal');
    //       this.interactionUserService().handleShare(this.$store.getters.getId, PLATFORM.facebook, this.connectomeId);
    //     }
    //   }
    // );
    const url = encodeURIComponent(this.post.sourceId);
    const facebook = 'https://www.facebook.com/sharer/sharer.php?s=100&p[url]=' + url;
    window.open(facebook, '_blank');
  }

  public shareOnTwitter() {
    const url = encodeURIComponent(this.post.url);
    const tweet = 'https://twitter.com/intent/tweet?url=' + url + '&via=DeepSignal';
    window.open(tweet, '_blank');
  }

  public shareOnLinkedIn() {
    const url = encodeURIComponent(this.post.url);
    const linkedIn = 'https://www.linkedin.com/shareArticle?mini=true&url=' + url;
    window.open(linkedIn, '_blank');
  }

  public shareByLink() {
    this.isSharingLink = !this.isSharingLink;
    if (this.isSharingLink) {
      this.selected = 'PUBLIC';
      this.textShare = '';
      this.textSearchEmail = '';
      const domain = location.href.split('/');
      for (let i = 0; i < domain.length - 1; i++) {
        if (domain[i].toLowerCase().includes("detail-feed")){
          return this.linkPublic = location.href;
        }else {
          this.linkPublic += domain[i] + '/';
        }
      }
      const connectomeId = JSON.parse(localStorage.getItem('ds-connectome')).connectomeId;
      this.linkPublic = this.linkPublic + 'detail-feed/' + connectomeId + '/' + this.post.docId;
    }
  }

  public handleCopy() {
    this.isCopy = !this.isCopy;
    if (this.isCopy && this.linkPublic) {
      navigator.clipboard.writeText(this.linkPublic);
    }
  }

  handleHide() {
    this.$store.commit('setDelete', true);
  }

  public handleBookmark() {
    this.isBookmark = !this.isBookmark;
    this.$store.commit('setBookmark', this.isBookmark);
  }

  public handleLike() {
    this.isLike = !this.isLike;
    if (this.isLike) {
      this.isDislike = false;
    }
    this.$store.commit('setLike', this.isLike);
    this.$store.commit('setDislike', this.isDislike);
  }

  public handleDislike() {
    this.isDislike = !this.isDislike;
    if (this.isDislike) {
      this.isLike = false;
    }
    this.$store.commit('setDislike', this.isDislike);
    this.$store.commit('setLike', this.isLike);
  }

  public handleShareFromDetail() {
    this.handleShare();
  }

  public searchEmail() {
    if (this.textSearchEmail) {
      axios.get('/api/admin/users/searchEmail', { params: { key: this.textSearchEmail } }).then(res => {
        this.resultSearch = res.data;
      });
    }
  }

  public chooseEmail(email) {
    if (this.emailsSending.indexOf(email) == -1) {
      this.emailsSending.push(email);
    }
  }

  public removeEmail(email) {
    const index = this.emailsSending.indexOf(email);
    this.emailsSending.splice(index, 1);
  }

  public sendEmail() {
    console.log('linkPublic', this.linkPublic);
    if (this.textShare.length < 10) {
      this.checkTextShare = true;
      return;
    }
    const dayLocate = new Date().toLocaleString();
    if (this.emailsSending.length < 1) {
      return;
    }
    this.isSendingEmail = false;
    const mailParams = {
      emails: this.emailsSending,
      message: this.textShare,
      linkShare: this.linkPublic,
      dayShare: dayLocate,
    };
    const connectomeId = JSON.parse(localStorage.getItem('ds-connectome')).connectomeId;
    axios
      .post('/api/connectome-feed/saveAndSendEmail', mailParams, {
        params: {
          connectomeId: connectomeId,
          cardId: this.post.docId,
        },
      })
      .then(res => {
        this.isSendingEmail = true;
        this.$bvToast.toast('Emails have been sent!', {
          title: 'Email status:',
          variant: 'primary',
          autoHideDelay: 5000,
          solid: true,
        });
        this.$bvModal.hide('shareModal');
      })
      .catch(error => {
        this.isSendingEmail = true;
        this.$bvToast.toast('Can not send emails, please try again later', {
          title: 'Emails status:',
          variant: 'danger',
          autoHideDelay: 5000,
          solid: true,
        });
        this.$bvModal.hide('shareModal');
      });
  }
}
