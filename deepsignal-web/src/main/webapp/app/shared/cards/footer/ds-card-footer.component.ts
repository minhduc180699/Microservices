import { Component, Inject, Prop, Vue, Watch } from 'vue-property-decorator';
import axios from 'axios';
import { InteractionUserService } from '@/service/interaction-user.service';
import { computed } from '@vue/composition-api';
import { ACTIVITY, PLATFORM } from '@/shared/constants/feed-constants';

export const interactionLike = { noAction: 0, like: 1, dislike: 2 };

@Component({
  // computed: {
  //   show() {
  //     return this.$store.getters.show;
  //   },
  //   footerLike() {
  //     return this.$store.getters.getLike;
  //   },
  //   footerBookmark() {
  //     return this.$store.getters.getBookmark;
  //   },
  //   footerDislike() {
  //     return this.$store.getters.getDislike;
  //   },
  //   footerHidden() {
  //     return this.$store.getters.getDelete;
  //   },
  // },
  // watch: {
  //   show(value, oldValue) {
  //     this.isShow = value;
  //   },
  //   footerLike(value, oldValue) {
  //     this.handleLikeFromMore(value);
  //   },
  //   footerBookmark(value) {
  //     this.handleBookmarkFromMore(value);
  //   },
  //   footerDislike(value) {
  //     this.handleDislikeFromMore(value);
  //   },
  //   footerHidden(value) {
  //     this.handleHidden(value);
  //   },
  // },
})
export default class DsCardFooter extends Vue {
  @Prop(Object) readonly item: any | undefined;
  @Inject('interactionUserService') private interactionUserService: () => InteractionUserService;
  isLike = false;
  isDislike = false;
  private isShow = false;
  private isBookmark = false;
  private page = 'feed';

  mounted() {
    if (this.item.liked == 0) {
      this.isLike = false;
      this.isDislike = false;
    } else if (this.item.liked == 1) {
      this.isLike = true;
      this.isDislike = false;
    } else if (this.item.liked == 2) {
      this.isLike = false;
      this.isDislike = true;
    }
    if (this.item.bookmarked) {
      this.isBookmark = true;
    }
  }

  created() {
    this.$root.$on('handle-like-detail', this.handleLikeFromDetail);
    this.$root.$on('handle-dislike-detail', this.handleDislikeFromDetail);
    this.$root.$on('handle-bookmark-detail', this.handleBookmarkFromDetail);
    this.$root.$on('handle-hidden-detail', this.handleHiddenFromDetail);
  }

  destroyed() {
    this.$root.$off('handle-like-detail', this.handleLikeFromDetail);
    this.$root.$off('handle-dislike-detail', this.handleDislikeFromDetail);
    this.$root.$off('handle-bookmark-detail', this.handleBookmarkFromDetail);
    this.$root.$on('handle-hidden-detail', this.handleHiddenFromDetail);
  }

  handleLike() {
    this.isLike = !this.isLike;
    let interaction = 0;
    if (this.isLike) {
      this.isDislike = false;
      interaction = interactionLike.like;
    } else {
      interaction = interactionLike.noAction;
    }
    if (!this.isLike && this.isDislike) {
      return;
    }
    this.callApiActivity(true, ACTIVITY.like, interaction);
  }

  handleDislike() {
    this.isDislike = !this.isDislike;
    let interaction = 0;
    if (this.isDislike) {
      this.isLike = false;
      interaction = interactionLike.dislike;
    } else {
      interaction = interactionLike.noAction;
    }
    if (!this.isDislike && this.isLike) {
      return;
    }
    this.callApiActivity(true, ACTIVITY.like, interaction);
  }

  // handleMore() {
  //   this.isShow = !this.isShow;
  //   // @ts-ignore
  //   const x = this.$refs.footerMore.getBoundingClientRect().left;
  //   // @ts-ignore
  //   const y = this.$refs.footerMore.getBoundingClientRect().top;
  //   // const transfer = { showState: this.isShow, xCoordinate: x, yCoordinate: y };
  //   this.$store.commit('setShow', this.isShow);
  //   this.$store.commit('setCoordinate', { x, y });
  //   this.$store.commit('setLike', this.isLike);
  //   this.$store.commit('setDislike', this.isDislike);
  //   this.$store.commit('setId', this.item.id);
  //   this.$store.commit('setBookmark', this.isBookmark);
  //   const post = { sourceId: this.item.sourceId, title: this.item.title, writer: this.item.writerName };
  //   this.$store.commit('setPost', post);
  //   this.$store.commit('setDelete', false);
  // }

  // handleLikeFromMore(isLike) {
  //   if (this.$store.getters.getId == this.item.id) {
  //     if (this.isLike != isLike) {
  //       this.handleLike();
  //     }
  //   }
  // }

  // handleDislikeFromMore(isDislike) {
  //   if (this.$store.getters.getId == this.item.id) {
  //     if (this.isDislike != isDislike) {
  //       this.handleDislike();
  //     }
  //   }
  // }

  handleBookmark() {
    this.isBookmark = !this.isBookmark;
    this.callApiActivity(this.isBookmark, ACTIVITY.bookmark, interactionLike.noAction);
  }

  // handleBookmarkFromMore(isBookmark) {
  //   if (this.$store.getters.getId == this.item.id) {
  //     if (this.isBookmark != isBookmark) {
  //       this.handleBookmark();
  //     }
  //   }
  // }

  // handleHidden(isHidden) {
  //   if (this.$store.getters.getId == this.item.id) {
  //     if (isHidden) {
  //       this.$emit('handleHiddenCard', ACTIVITY.delete);
  //       this.$store.commit('setShow', false);
  //     }
  //   }
  // }

  handleShare() {
    const post = { sourceId: this.item.sourceId, title: this.item.title, writer: this.item.writerName, id: this.item.id };
    this.$store.commit('setPost', post);
  }

  handleHidden() {
    this.$store.commit('setId', this.item.id);
    let checkMyAi = false;
    if (location.href.includes('my-ai') || location.href.includes('people')) {
      checkMyAi = true;
    }
    this.$emit('handleHiddenCard', ACTIVITY.delete, checkMyAi);
  }

  handleLikeFromDetail(value) {
    if (value.id == this.item.id) {
      this.isLike = value.state;
      if (this.isLike) {
        this.isDislike = false;
      }
    }
  }

  handleDislikeFromDetail(value) {
    if (value.id == this.item.id) {
      this.isDislike = value.state;
      if (this.isDislike) {
        this.isLike = false;
      }
    }
  }

  handleBookmarkFromDetail(value) {
    if (value.id == this.item.id) {
      this.isBookmark = value.state;
    }
  }

  handleHiddenFromDetail(value) {
    if (value.id == this.item.id) {
      const activity = { like: 0, bookmark: false, activity: ACTIVITY.delete };
      this.$emit('handleActivity', this.item, activity);
    }
  }

  callApiActivity(value, type, interaction, feedback?) {
    let like = 0;

    if (this.isLike) {
      like = 1;
    }
    if (this.isDislike) {
      like = 2;
    }
    const activity = { like: like, bookmark: value, activity: type };
    axios
      .get('/api/connectome-feed/activity/' + this.item.connectomeId, {
        params: {
          state: value,
          activity: type,
          page: this.item.collectionType,
          docId: this.item.id,
          likeState: interaction,
          feedback: feedback,
        },
        headers: {
          Title: btoa(unescape(encodeURIComponent(this.item.title))),
          'Original-Url': btoa(unescape(encodeURIComponent(this.item.sourceId))),
        },
      })
      .then(res => {
        this.$emit('handleActivity', this.item, activity);
      })
      .catch(res => {
        this.$emit('handleActivity', this.item, activity);
      });
  }
}
