import { Component, Vue } from 'vue-property-decorator';
import { MetaSearchData } from '@/entities/connectome/meta-search/meta-search-data';
import vueCustomScrollbar from 'vue-custom-scrollbar';
import 'vue-custom-scrollbar/dist/vueScrollbar.css';

@Component({
  data(): Record<string, unknown> {
    return {
      connectomes: MetaSearchData,
    };
  },
  components: {
    vueCustomScrollbar,
  },
})
export default class SearchConnectomeComponent extends Vue {
  private scrollSettings = {
    wheelPropagation: false,
    suppressScrollX: false,
    wheelSpeed: 0.5,
  };

  public noSearchResult = false;
  public showTopicDetail = false;

  mounted(): void {
    this.$nextTick(() => {
      document.getElementsByTagName('html').item(0).classList.add('has-bar');
    });
  }

  public topicClicked(event) {
    event.preventDefault();
    document.getElementsByTagName('html').item(0).classList.add('topick-detail-layer-show');
    this.showTopicDetail = true;
  }

  public closeTopicDetail(event) {
    event.preventDefault();
    document.getElementsByTagName('html').item(0).classList.remove('topick-detail-layer-show');
    this.showTopicDetail = false;
  }
}
