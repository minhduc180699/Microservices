import { Component, Prop, Vue, Watch } from 'vue-property-decorator';
import axios from 'axios';
import buildPaginationQueryOpts from '@/shared/sort/sorts';

@Component
export default class FeedMemoComponent extends Vue {
  @Prop(String) readonly feedId: any | undefined;
  @Prop(Object) readonly connectomeFeed: any | undefined;
  pageable = {
    page: 0,
    size: 5,
    sort: ['createdDate,desc'],
  };

  public memos = [];
  memo = {
    content: '',
    feedId: '',
  };

  contentMemo = '';
  totalMemos = 0;
  isShow = true;

  @Watch('connectomeFeed')
  handleConnectomeFeedChange(path) {
    this.getMemosByFeed(this.pageable);
  }

  created(): void {
    this.getMemosByFeed();
  }

  getMemosByFeed(pageable?) {
    const connectome = JSON.parse(localStorage.getItem('ds-connectome'));
    const feedId = this.connectomeFeed.id;
    if (feedId) {
      let req;
      if (pageable) {
        req = {
          page: pageable.page ? pageable.page : 0,
          size: pageable.size ? pageable.size : 10,
          sort: pageable.sort ? pageable.sort : ['createdDate,desc'],
        };
      }
      axios
        .get(`api/memo/${connectome.connectomeId}/${connectome.user.id}?feedId=` + feedId + '&' + buildPaginationQueryOpts(req))
        .then(res => {
          this.totalMemos = res.headers['x-total-count'];
          this.memos = res.data;
        });
    }
  }

  saveMemo() {
    const headers = {
      Title: btoa(unescape(encodeURIComponent(this.connectomeFeed.title))),
      'Original-Url': btoa(unescape(encodeURIComponent(this.connectomeFeed.sourceId))),
    };

    const connectomeId = localStorage.getItem('ds-connectome') ? JSON.parse(localStorage.getItem('ds-connectome')).connectomeId : null;
    this.memo.content = this.contentMemo;
    this.memo.feedId = this.connectomeFeed.id;
    axios.post(`api/memo/${connectomeId}/${this.connectomeFeed.id}/save`, this.memo, { headers }).then(() => {
      this.contentMemo = '';
      this.getMemosByFeed(this.pageable);
    });
  }
}
