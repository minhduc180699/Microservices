import { Component, Vue } from 'vue-property-decorator';
import LearningCenterAside from '@/entities/new-learning-center/aside/learning-center-aside.vue';
import ListCollection from '@/entities/new-learning-center/list-collection/list-collection.vue';
import CollectionCart from '@/entities/new-learning-center/collection-cart/collection-cart.vue';
import CollectionDiscovery from '@/entities/new-learning-center/collection-discovery/collection-discovery.vue';
import Connectome from '@/entities/new-learning-center/connectome/connectome.vue';
import { CmCollection } from '@/shared/model/cm-collection.model';
import { namespace } from 'vuex-class';

const collectionsManagerStore = namespace('collectionsManagerStore');

@Component({
  components: {
    learningCenterAside: LearningCenterAside,
    listCollection: ListCollection,
    collectionCart: CollectionCart,
    collectionDiscovery: CollectionDiscovery,
    connectome: Connectome,
  },
})
export default class newLearningCenter extends Vue {
  @collectionsManagerStore.Action
  public addBookmarksToCurrentCollection: (payload: {
    docIds: Array<string>;
  }) => Promise<{ status: string; message: string; result: CmCollection }>;

  @collectionsManagerStore.Getter
  public getCurrentCollection!: CmCollection;

  private bookmarkCardItems = [];

  mounted(): void {
    document.body.setAttribute('data-menu', 'connectome');
  }

  changeBookmarkItems(data: any) {
    this.bookmarkCardItems = data;
  }
}
