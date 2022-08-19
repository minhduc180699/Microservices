import axios from 'axios';
import buildPaginationQueryOpts from '@/shared/sort/sorts';
import { buildRequestParamBase } from '@/util/ds-util';

export class CommentService {
  connectomeId = JSON.parse(localStorage.getItem('ds-connectome')).connectomeId;
  private BASE_URL = 'api/comment/'.concat(this.connectomeId);

  public save(content: string, feedId: string, parentCommentId?, headers?: any) {
    const comment = {} as any;
    comment.content = content;
    comment.feedId = feedId;
    comment.parentCommentId = parentCommentId ? parentCommentId : null;
    return axios.post(this.BASE_URL + '/save', comment, { headers });
  }

  public findByFeedId(feedId: string, pageable?) {
    const req = buildRequestParamBase(pageable);
    return axios.get(this.BASE_URL + '/findByFeed?feedId=' + feedId + '&' + buildPaginationQueryOpts(req));
  }

  public countFeedId(feedId: string) {
    return axios.get(this.BASE_URL + '/countByFeed?feedId=' + feedId);
  }
}
