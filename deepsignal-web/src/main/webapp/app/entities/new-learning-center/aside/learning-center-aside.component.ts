import { Vue, Component, Watch } from 'vue-property-decorator';
import vueCustomScrollbar from 'vue-custom-scrollbar';
import axios from 'axios';
import { COLLECTION_SERVER } from '@/shared/constants/ds-constants';
import listCardGroup from '@/entities/new-learning-center/aside/list-card-group/list-card-group.vue';
import singleCard from '@/entities/new-learning-center/aside/single-card/single-card.vue';
import groupCard from '@/entities/new-learning-center/aside/group-card/group-card.vue';

@Component({
  components: {
    vueCustomScrollbar: vueCustomScrollbar,
    listCard: listCardGroup,
    singleCard: singleCard,
    groupCard: groupCard,
  },
})
export default class LearningCenterAsideComponent extends Vue {
  private collections = [];

  private scrollSettings = {
    wheelPropagation: false,
  };

  private showList = false;
  private loading = false;
  private listData = [];

  data() {
    return {
      collections: this.collections,
    };
  }

  @Watch('collections')
  collectionsChange() {
    const documents = [];
    this.collections.forEach(collection => {
      if (collection.documentIdList?.length > 0) {
        collection.documentIdList.forEach(document => {
          documents.push(document);
        });
      }
    });
  }

  created(): void {
    const connectomeId = localStorage.getItem('ds-connectome')
      ? JSON.parse(localStorage.getItem('ds-connectome'))?.connectomeId
      : JSON.parse(sessionStorage.getItem('ds-connectome'))?.connectomeId;
    axios
      .get(COLLECTION_SERVER + 'api/connectome/collection/get/all', {
        headers: {
          connectomeId: connectomeId ? connectomeId : null,
        },
      })
      .then(res => {
        this.collections = res.data?.body;
        for (let i = 0; i < this.collections.length; i++) {
          if (this.collections[i].documentIdList.length == 0) {
            this.collections.splice(i, 1);
            i--;
          }
        }
        console.log('collections', this.collections);
      });
  }

  backToList() {
    this.showList = false;
  }

  showListGroupCard(data) {
    this.showList = true;
    this.listData = data;
  }
}
