import { Component, Inject, Prop, Vue, Watch } from 'vue-property-decorator';
import { asideService } from '@/entities/new-learning-center/aside/aside-service/aside.service';
import listCardGroup from '@/entities/new-learning-center/aside/list-card-group/list-card-group.vue';
import { CmCollection } from '@/shared/model/cm-collection.model';
import { namespace } from 'vuex-class';
import { documentCard } from '@/shared/model/document-card.model';

const collectionsManagerStore = namespace('collectionsManagerStore');

@Component({
  components: {
    listCardGroup: listCardGroup,
  },
  computed: {
    currentCollectionDocIds() {
      return this.$store.getters['collectionsManagerStore/getCurrentCollection']?.documentIdList;
    },
  },
})
export default class groupCard extends Vue {
  @Prop(Object) readonly collection: any;
  // @Prop(Array) readonly selectedItems: any | [];
  private dataDocuments = [];
  private images = [];
  private loading = true;

  private currentCollectionDocIds: any[];

  @collectionsManagerStore.Action
  public addBookmarksToCurrentCollection: (payload: {
    docIds: Array<string>;
  }) => Promise<{ status: string; message: string; result: CmCollection }>;

  @collectionsManagerStore.Action
  public removeBookmarksFromCurrentCollection: (payload: {
    docIds: Array<string>;
  }) => Promise<{ status: string; message: string; result: CmCollection }>;

  toggleGroupCollection(data: any) {
    this.$emit('toggleGroupCollection', this.collection);
  }

  selectGroupCard(collection: documentCard) {
    if (collection['docDetail']) {
      this.$emit('selectAllInGroup', this.collection.docDetail);
    }
  }
}
