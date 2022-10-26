import { Component, Prop, Vue, Watch } from 'vue-property-decorator';
import vueCustomScrollbar from 'vue-custom-scrollbar';
import singleCard from '@/entities/new-learning-center/aside/single-card/single-card.vue';
import { namespace } from 'vuex-class';
import { CmCollection } from '@/shared/model/cm-collection.model';

const collectionsManagerStore = namespace('collectionsManagerStore');

@Component({
  components: {
    vueCustomScrollbar: vueCustomScrollbar,
    'single-card': singleCard,
  },
})
export default class CollectionCart extends Vue {
  @Prop(Array) readonly bookmarkCardItems: any | [];
  @Prop(Object) readonly currentCollection: any;

  @collectionsManagerStore.Getter
  public getCurrentCollection!: CmCollection;

  @collectionsManagerStore.Action
  public saveCurrentDraftCollection: () => Promise<{ status: string; message: string; result: any }>;

  private scrollSettings = {
    wheelPropagation: false,
  };

  private isCartActive = false;

  isChild = false;
  newCollection: any = [];

  @Watch('currentCollection')
  setCurrentCollection() {
    this.isChild = false;
  }

  created() {
    this.$root.$on('cart-to-conlection', this.setCollection);
  }

  destroyed() {
    this.$root.$off('cart-to-conlection', this.setCollection);
  }

  saveCollection() {
    this.saveCurrentDraftCollection().then(res => {
      if (!res) {
        return;
      }

      if (res.status === 'NOK') {
        return;
      }

      if (!res.result) {
        return;
      }
    });
  }

  getDocDetail(docId: any) {
    return this.bookmarkCardItems.find(bookmard => bookmard.id == docId);
  }

  setCollection(arrDoc?) {
    this.newCollection = this.newCollection.splice(0, 0);
    this.isChild = true;
    this.newCollection = arrDoc;
  }
}
