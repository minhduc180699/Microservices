import { Component, Inject, Prop, Vue } from 'vue-property-decorator';
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

  // created() {
  //   console.log(this.currentCollectionDocIds,1111111);
  // }

  toggleGroupCollection(data: any) {
    this.$emit('toggleGroupCollection', this.collection);
  }

  handleClickGroupCard(collection: documentCard) {
    if (collection['docDetail']) {
      collection['docDetail'].forEach(item => {
        console.log(item, 111100);
        const doc = this.currentCollectionDocIds.find(docId => docId == item.id);
        if (doc) {
          this.removeFromCurrentCollection(item);
        } else {
          this.addToCurrentCollection(item);
        }
      });
    }
  }

  addToCurrentCollection(item) {
    this.addBookmarksToCurrentCollection({ docIds: [item.id] }).then(res => {
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

  removeFromCurrentCollection(item) {
    this.removeBookmarksFromCurrentCollection({ docIds: [item.id] }).then(res => {
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
}
