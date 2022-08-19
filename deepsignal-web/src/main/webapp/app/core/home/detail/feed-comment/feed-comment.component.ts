import { Component, Inject, Prop, Vue, Watch } from 'vue-property-decorator';
import { CommentService } from '@/service/comment.service';
import { InteractionUserService } from '@/service/interaction-user.service';
import DsTree from '@/shared/tree/ds-tree.vue';

@Component({
  components: {
    'ds-tree': DsTree,
  },
})
export default class FeedCommentComponent extends Vue {
  @Prop(String) readonly feedId: any | undefined;
  @Prop(Object) readonly connectomeFeed: any | undefined;
  @Inject('commentService') private commentService: () => CommentService;
  @Inject('interactionUserService') private interactionUserService: () => InteractionUserService;
  contentComment: string;
  pageable = {
    page: 0,
    size: 1,
    sort: ['createdDate,desc'],
  };
  public comments = [];
  public isLast = false;
  public isShow = true;
  public totalComments = 0;
  fromComment = null;
  // cardData = JSON.parse(localStorage.getItem('card-data'));

  @Watch('feedId')
  handleFeedIdChange(path) {
    this.getCommentsByFeed();
  }

  created(): void {
    this.getCommentsByFeed();
  }

  data() {
    return {
      contentComment: this.contentComment,
    };
  }

  saveComment() {
    if (String.prototype.isNullOrEmpty(this.contentComment)) {
      return;
    }
    const replyId = this.fromComment ? this.fromComment.id : null;
    const headers = {
      Title: btoa(unescape(encodeURIComponent(this.connectomeFeed.title))),
      'Original-Url': btoa(unescape(encodeURIComponent(this.connectomeFeed.sourceId))),
    };

    this.commentService()
      .save(this.contentComment, this.feedId, replyId, headers)
      .then(() => {
        this.contentComment = '';
        this.getCommentsByFeed();
        this.clearReply();
      });
  }

  clearReply() {
    this.fromComment = null;
  }

  getCommentsByFeed() {
    this.commentService()
      .findByFeedId(this.feedId, this.pageable)
      .then((res: any) => {
        if (!res || !res.data) {
          return;
        }
        this.comments = res.data.content;
        this.isLast = res.data.last;
        this.getTotalComment();
      });
  }

  getTotalComment() {
    this.commentService()
      .countFeedId(this.feedId)
      .then(res => {
        this.totalComments = res.data;
        this.$emit('totalComments', this.totalComments);
      });
  }

  focusToComment() {
    // @ts-ignore
    this.$refs.comment.focus();
  }

  viewMoreComment() {
    this.pageable.size += 5;
    this.getCommentsByFeed();
  }

  onReply(data) {
    this.focusToComment();
    this.fromComment = data;
  }
}
