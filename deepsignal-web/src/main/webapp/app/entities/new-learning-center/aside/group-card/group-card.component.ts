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
  // private dataDocuments = [];
  private isChecked = false;
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

  @collectionsManagerStore.Action
  public deleteCollection: (payload: { collectionId: string }) => Promise<any>;

  toggleGroupCollection(data: any) {
    this.$emit('toggleGroupCollection', this.collection);
  }

  handleGroupCard(collection: documentCard) {
    this.isChecked = !this.isChecked;
    if (this.isChecked) {
      this.selectAllInGroup(collection);
    } else {
      this.removeAllInGroup(collection);
    }
  }

  selectAllInGroup(collection: documentCard) {
    if (collection['docDetail']) {
      this.$emit('selectAllInGroup', this.collection.docDetail);
    }
  }

  removeAllInGroup(collection: documentCard) {
    if (collection?.docDetail) {
      this.$emit('removeAllInGroup', this.collection.docDetail);
    }
  }

  handleDeleteCollection() {
    this.deleteCollection({ collectionId: this.collection.id }).then();
  }
}
