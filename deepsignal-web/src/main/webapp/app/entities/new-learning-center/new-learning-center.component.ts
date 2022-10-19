import { Component, Vue } from 'vue-property-decorator';
import LearningCenterAside from '@/entities/new-learning-center/aside/learning-center-aside.vue';
import ListCollection from '@/entities/new-learning-center/list-collection/list-collection.vue';
import CollectionCart from '@/entities/new-learning-center/collection-cart/collection-cart.vue';
import CollectionDiscovery from '@/entities/new-learning-center/collection-discovery/collection-discovery.vue';
import Connectome from '@/entities/new-learning-center/connectome/connectome.vue';

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
  mounted(): void {
    document.body.setAttribute('data-menu', 'connectome');
  }
}
