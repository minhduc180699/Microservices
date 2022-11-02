import {Component, Inject, Prop, Vue, Watch} from 'vue-property-decorator';
import axios from 'axios';
import { ACTIVITY, DOCUMENT } from '@/shared/constants/feed-constants';
import { interactionLike } from '@/shared/cards/footer/ds-card-footer.component';
import DsCardFooter from '@/shared/cards/footer/ds-card-footer.vue';
import FeedService from "@/core/home/feed/feed.service";

@Component({
  components: {
    'ds-card-footer': DsCardFooter,
  },
})
export default class activityDetail extends Vue {
  @Prop(Object) readonly item: any;
  @Inject('feedService')
  private feedService: () => FeedService;
  private isShowMore = false;
  private isLiked = false;
  private isDisliked = false;
  private isBookmarked = false;

  @Watch('item')
  onItemChange(value) {
    if (this.item.liked == 1) {
      this.isLiked = true;
    }
    if (this.item.liked == 2) {
      this.isDisliked = true;
    }
    if (this.item.isBookmarked) {
      this.isBookmarked = true;
    }
  }

  handleDislike() {
    this.isDisliked = !this.isDisliked;
    let interaction = 0;
    if (this.isDisliked) {
      this.isLiked = false;
      interaction = interactionLike.dislike;
    } else {
      interaction = interactionLike.noAction;
    }
    // const value = {
    //   id: this.item.id,
    //   state: this.isDisliked,
    // };
    // this.callApiActivity(false, ACTIVITY.like, interaction, this.item.collectionType)
    //   .then(res => {
    //     this.$root.$emit('handle-dislike-detail', value);
    //   })
    //   .catch(error => {
    //     this.$root.$emit('handle-dislike-detail', value);
    //   });
    this.handleActivityLike(interaction)
  }

  handleLike() {
    this.isLiked = !this.isLiked;
    let interaction = 0;
    if (this.isLiked) {
      this.isDisliked = false;
      interaction = interactionLike.like;
    } else {
      interaction = interactionLike.noAction;
    }
    // const value = {
    //   id: this.item.id,
    //   state: this.isLiked,
    // };
    // this.callApiActivity(false, ACTIVITY.like, interaction, this.item.collectionType)
    //   .then(res => {
    //     this.$root.$emit('handle-like-detail', value);
    //   })
    //   .catch(error => {
    //     this.$root.$emit('handle-like-detail', value);
    //   });
    this.handleActivityLike(interaction)

  }

  handleBookmark() {
    this.isBookmarked = !this.isBookmarked;
    // const value = {
    //   id: this.item.id,
    //   state: this.isBookmarked,
    // };
    // this.callApiActivity(this.isBookmarked, ACTIVITY.bookmark, interactionLike.noAction, this.item.collectionType)
    //   .then(res => {
    //     this.$root.$emit('handle-bookmark-detail', value);
    //   })
    //   .catch(error => {
    //     this.$root.$emit('handle-bookmark-detail', value);
    //   });
    this.handleActivityBookmark(this.isBookmarked, ACTIVITY.bookmark)

  }

  handleShare() {
    const post = {url: this.item.url, title: this.item.title, writer: this.item.write, docId: this.item.docId};
    this.$store.commit('setPost', post);
  }

  showMore() {
    this.isShowMore = !this.isShowMore;
  }

  handleHide() {
    const value = {
      id: this.item._id,
      state: this.isBookmarked,
    };
    console.log("value: ",value)
    // let page = DOCUMENT.feed;
    // if (this.item.idHash) {
    //   page = DOCUMENT.personal;
    // }
    // this.callApiActivity(true, ACTIVITY.delete, interactionLike.noAction, page)
    //   .then(res => {
    //     this.$router.push('/feed');
    //     this.$root.$emit('handle-hidden-detail', value);
    //   })
    //   .catch(error => {
    //     this.$router.push('/feed');
    //     this.$root.$emit('handle-hidden-detail', value);
    //   });
    this.handleHideFeed(value)
  }

  callApiActivity(value, type, interaction, page, feedback?) {
    return axios.get('/api/connectome-feed/activity/' + this.item.connectomeId, {
      params: {
        state: value,
        activity: type,
        page: page,
        docId: this.item.id,
        likeState: interaction,
        feedback: feedback,
      },
      headers: {
        Title: btoa(unescape(encodeURIComponent(this.item.title))),
        'Original-Url': btoa(unescape(encodeURIComponent(this.item.sourceId))),
      },
    });
  }

  handleHideFeed(value) {
    this.feedService().handleHideFeed(
      null,
      this.item.connectomeId,
      this.item.docId,
      0,
      null,
      true
    ).then(res => {
      this.$router.push('/feed');
      this.$root.$emit('handle-hidden-detail', value);
    })
      .catch(res => {
        this.$router.push('/feed');
        this.$root.$emit('handle-hidden-detail', value);
      });
  }

  handleActivityLike(interaction) {
    // const activity = {like: like, activity: type};
    this.feedService().handleActivityLike(
      null,
      this.item.connectomeId,
      this.item.docId,
      interaction,
      null,
      null
    )
  }

  handleActivityBookmark(value, type) {
    const activity = {bookmark: value, activity: type};
    this.feedService().handleActivityBookmark(
      null,
      this.item.connectomeId,
      this.item.docId,
      0,
      value,
      null
    )
  }



}
