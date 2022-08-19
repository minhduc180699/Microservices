import { Component, Prop, Vue } from 'vue-property-decorator';
import DocumentAddNewComponent from '@/entities/my-ai/learning-center/document/document-add-new/document-add-new.vue';
import DocumentCompleteListComponent from '@/entities/my-ai/learning-center/document/document-complete-list/document-complete-list.vue';

@Component({
  components: {
    'doc-add-new': DocumentAddNewComponent,
    'doc-complete-list': DocumentCompleteListComponent,
  },
})
export default class document extends Vue {
  @Prop(String) readonly currentTab: string | any;
  public showSearch = false;

  created() {
    this.$parent.$on('unSelectedDocument', this.unSelectedDocument);
  }

  showSearchLayer() {
    this.showSearch = !this.showSearch;
  }

  handleSelectedDocument(data) {
    this.$emit('handleSelectedDocument', data);
  }

  unSelectedDocument() {
    this.$emit('unSelectedDocument');
  }

  startLearning() {
    // @ts-ignore
    this.$parent.learning();
  }
}
