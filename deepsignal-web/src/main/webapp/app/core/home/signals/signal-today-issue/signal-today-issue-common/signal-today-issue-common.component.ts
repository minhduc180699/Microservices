import { Component, Prop, Vue } from 'vue-property-decorator';
import { ClusterDocument } from '@/core/home/signals/signal-today-issue/signal-today-issue.model';
import { PageableModel } from '@/shared/model/pageable.model';

@Component({})
export default class SignalTodayIssueCommonComponent extends Vue {
  @Prop(Number) sizeCluster: number | 8;
  @Prop() hasPageable: any;
  clusterDocuments: ClusterDocument[] | undefined;
  isLoadingSignal = true;
  firstClusterDocument = new ClusterDocument();
  pageableClusterDocument = new PageableModel();
  validImageLinks = true;

  data() {
    return {
      size: this.sizeCluster ? this.sizeCluster : 8,
    };
  }

  mounted(): void {
    setTimeout(() => (this.isLoadingSignal = false), 10000);
  }

  init(clusters: ClusterDocument[]) {
    this.isLoadingSignal = false;
    if (!clusters) {
      return;
    }
    clusters = clusters.filter(ele => ele.title != null && ele.title != '' && ele.title != undefined);
    this.clusterDocuments = clusters;
    const [first, ...rest] = clusters;
    this.firstClusterDocument = first;
    this.pageableClusterDocument = new PageableModel(rest, 1, this.sizeCluster);
    this.validImageLinks = true;
  }

  changePageClusterDocuments(isNext) {
    if (isNext) {
      this.pageableClusterDocument.nextPage();
    } else {
      this.pageableClusterDocument.previousPage();
    }
  }

  clearData() {
    this.isLoadingSignal = true;
    this.firstClusterDocument = new ClusterDocument();
    this.pageableClusterDocument = new PageableModel();
  }
}
