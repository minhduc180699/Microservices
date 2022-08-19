import { Component, Prop, Vue } from 'vue-property-decorator';
import { DATA_FAKE } from '@/shared/constants/ds-constants';

@Component
export default class DocumentListWrapComponent extends Vue {
  @Prop({ default: () => [], required: true }) docLists: Array<any>;
  @Prop({ default: () => false, required: false }) loading: boolean;
  @Prop(String) readonly parent: '' | any;
  private cardSelected = [];
  private postSelectedDocument = [];
  items = DATA_FAKE;

  created() {
    this.$parent.$on('unSelectedDocument', this.unSelectedDocument);
    this.$parent.$on('addLearning', this.addLearning);
  }

  removeFile(file) {
    this.$emit('removeFile', file);
  }

  downloadFile(file) {
    this.$emit('downloadFile', file);
  }

  selectCard(item, index) {
    const num = this.cardSelected.indexOf(index);
    if (num > -1) {
      this.cardSelected.splice(num, 1);
      this.postSelectedDocument.forEach((value, st) => {
        if (value.name === item.name) {
          this.postSelectedDocument.splice(st, 1);
        }
      });
    } else {
      this.cardSelected.push(index);
      let check = false;
      this.postSelectedDocument.forEach((value, st) => {
        if (value.name === item.name) {
          check = true;
        }
      });
      if (!check) {
        this.postSelectedDocument.push(item);
      }
    }
    this.$emit('handleSelectedDocument', this.postSelectedDocument);
  }

  unSelectedDocument() {
    this.cardSelected = [];
    this.postSelectedDocument = [];
  }

  addLearning() {
    this.docLists.forEach((item, index) => {
      if (this.cardSelected.indexOf(index) === -1) {
        this.cardSelected.push(index);
        this.postSelectedDocument.push(this.docLists[index]);
      }
    });
    this.$emit('handleSelectedDocument', this.postSelectedDocument);
  }
}
