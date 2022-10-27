import { Component, Inject, Prop, Vue, Watch } from 'vue-property-decorator';
import { asideService } from '@/entities/new-learning-center/aside/aside-service/aside.service';
import { CmCollection } from '@/shared/model/cm-collection.model';
import { namespace } from 'vuex-class';

const collectionsManagerStore = namespace('collectionsManagerStore');

@Component({
  computed: {
    currentCollectionDocIds() {
      return this.$store.getters['collectionsManagerStore/getCurrentCollection']?.documentIdList;
    },
  },
})
export default class singleCard extends Vue {
  @Prop(Object) readonly document: any;
  @Prop() readonly selectedItems: any;
  @Prop() readonly isHideCheck: any;

  @collectionsManagerStore.Action
  public addBookmarksToCurrentCollection: (payload: {
    docIds: Array<string>;
  }) => Promise<{ status: string; message: string; result: CmCollection }>;

  @collectionsManagerStore.Action
  public removeBookmarksFromCurrentCollection: (payload: {
    docIds: Array<string>;
  }) => Promise<{ status: string; message: string; result: CmCollection }>;

  private loading = true;
  private currentCollectionDocIds: any[];

  handleClickSingleCard() {
    const doc = this.currentCollectionDocIds.find(docId => docId == this.document.id);
    if (doc) {
      this.removeFromCurrentCollection(this.document);
    } else {
      this.addToCurrentCollection(this.document);
    }
  }

  addToCurrentCollection(item: any) {
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

  removeFromCurrentCollection(item: any) {
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

  showTag(e) {
    e.preventDefault();
    $('.tag-box').toggleClass('show');
  }

  dataTmp = [];
  dataTmpItemsChage() {
    this.$emit('setSelectedItems', this.dataTmp);
  }

  @Watch('selectedItems')
  selectedItemsChage() {
    this.dataTmp = this.selectedItems;
  }

  created() {
    this.dataTmp = this.selectedItems;
  }

  checkRegexDate(value) {
    const regex = new RegExp('ago');
    if (regex.test(value)) return value;
    else return new Date(value);
  }
}
