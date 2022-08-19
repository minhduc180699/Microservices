import { Component, Inject, Vue, Watch } from 'vue-property-decorator';
import { BIcon, BIconHddFill, BIconHddNetworkFill, BIconHddStackFill, BIconServer } from 'bootstrap-vue';
import ConnectomeService from '../../connectome.service';
import { use } from 'vue/types/umd';
import { StringDecoder } from 'string_decoder';

@Component({
  components: {
    BIcon,
    BIconHddFill,
    BIconHddNetworkFill,
    BIconHddStackFill,
    BIconServer,
  },
})
export default class ConnectomeAnalyzerComponent extends Vue {
  connectomeId = '';
  topicSelected: any = null;
  topics: Array<any> = [];
  children: Array<any> = [];
  childSelected: any = null;
  entities: Array<any> = [];
  detail = '';
  entityById: Map<string, any> = new Map();

  topicById: Map<string, any> = new Map();
  @Inject('connectomeService')
  private connectomeService: () => ConnectomeService;

  onTopicClick(item: any) {
    this.detail = '';
    this.childSelected = null;
    this.entities = [];
    this.children = item.children;
    this.topicSelected = item;
  }

  onChildClick(item: any) {
    this.entities = [];
    this.childSelected = null;
    if (item.entities && item.entities.length > 0) {
      this.childSelected = item;
      item.entities.forEach(element => {
        this.entities.push(this.entityById.get(element));
      });
      return;
    }
    if (item.topic_id) {
      this.onTopicClick(item);
    }
  }

  onEntityClick(item: any) {
    this.detail = JSON.stringify(item, undefined, 2);
  }

  onConnectomeIdInput() {
    const apiCallConnectomeMap = this.connectomeService().getConnectomeMap(this.connectomeId);
    if (!apiCallConnectomeMap) {
      return;
    }
    this.topics = [];
    this.topicSelected = null;
    this.children = [];
    this.detail = '';
    this.childSelected = null;
    this.entities = [];
    apiCallConnectomeMap.then(res => {
      if (!res.data) {
        return;
      }
      console.log('ConnectomeMap:', res.data);

      this.entityById = new Map();
      if (res.data.entities) {
        res.data.entities.forEach(element => {
          this.entityById.set(element.entity_id, element);
        });
      }

      this.topicById = new Map();
      if (res.data.topics) {
        res.data.topics.forEach(element => {
          element.variant = '';
          element.icon = '';
          this.topicById.set(element.topic_id, element);
        });
      }
      this.topicSelected = null;

      //relations
      const links = res.data.edges;
      // Create nodes for each unique source and target.
      links.forEach(element => {
        const parent = (element.source = this.topicById.get(element.source));
        const child = (element.target = this.topicById.get(element.target));

        if (parent.topic_id.endsWith('-n0')) {
          child.variant = 'primary';
          child.icon = 'server';
        } else if (!child.topic_id.endsWith('-n0')) {
          child.variant = 'info';
          child.icon = 'hdd-stack-fill';
        }

        if (child.entities) {
          child.variant = 'warning';
          child.icon = 'hdd-network-fill';
        }
        if (parent.children) {
          parent.children.push(child);
        } else {
          parent.children = [child];
        }
      });

      links.forEach(element => {
        if (this.topics.indexOf(element.source) < 0) {
          this.topics.push(element.source);
        }
      });
      const dataset = links[0].source;
      console.log('dataset', dataset);
    });
  }
}
