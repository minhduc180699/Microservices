export class PostModel {
  id: string;
  connectomeId: string;
  docType: number;
  docId: string;
  topicSims: string;
  w2vSim: string;
  esaSim: string;
  topic: string;
  url: string;
  commentId: string;
  replyId: string;
  publishedAt: PublishedAt;
  projectIds: any;
  webSourceId: string;
  webSourceCategory: any;
  collectedAt: PublishedAt;
  serviceType: string;
  sourceUri: string;
  title: string;
  content: string;
  timestamp: number;
  serviceLanguage: string;
  stockCodes: StockCode[];
  // medias?: MediaPost[];
  // table?: TablePost[];
  // chart?: ChartPost;

  constructor(post: PostModel) {
    for (const key in post) {
      if (Object.prototype.hasOwnProperty.call(post, key)) {
        this[key] = post[key];
      }
    }
    // this.id = post.id;
    // this.connectomeId = post.connectomeId;
    // this.source_id = post.source_id;
    // this.comment_id = post.comment_id;
    // this.reply_id = post.reply_id;
    // this.crawler_id = post.crawler_id;
    // this.project_ids = post.project_ids;
    // this.web_source_id = post.web_source_id;
    // this.web_source_category = post.web_source_category;
    // this.collected_at = post.collected_at;
    // this.title = post.title;
    // this.content = post.content;
    // this.writer = post.writer;
    // this.medias = post.medias;
    // this.table = post.table;
    // this.chart = post.chart;
  }
}

class StockCode {
  market: string;
  symbol: string;
  freq: number;
}

class PublishedAt {
  seconds: number;
  nanos: number;
}

class TitlePost {
  text: string;
}

class MediaPost {
  type: string;
  url: string;
}

class TablePost {
  header: any;
  cells: CellTablePost[];
}

class CellTablePost {
  label: string;
  post_type: number;
  post_id: string;
}

class ChartPost {
  chart_type: string;
  x_axis: {
    categories: any;
  };
  y_axis: {
    title: TitlePost;
  };
  series: Series[];
}

class Series {
  label: string;
  data: any;
}
