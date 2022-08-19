import { Component, Prop, Vue } from 'vue-property-decorator';
import { UPLOAD_FILE_SUPPORT } from '@/shared/constants/ds-constants';
import { getExtensionFileByName } from '@/util/ds-util';

@Component
export default class document extends Vue {
  @Prop(Number) readonly display;
  private uploadQueueFiles = [];
  private isSelected = [];
  private isSelectAll = false;
  private numberOfFile = 0;
  acceptUploadFile = UPLOAD_FILE_SUPPORT.join(',');

  data(): Record<string, unknown> {
    return {
      uploadQueueFiles: this.uploadQueueFiles,
    };
  }

  created() {
    this.$parent.$on('uncheckedItemDoc', this.uncheckedItemDoc);
  }

  uploadFromPC(files) {
    if (!this.uploadQueueFiles) {
      this.uploadQueueFiles = [];
    }
    for (const file of files) {
      if (this.validateFile(file.name)) {
        this.uploadQueueFiles.push(file);
      }
    }
    this.numberOfFile = this.uploadQueueFiles.length;
  }

  validateFile(name): boolean {
    if (!name) {
      return false;
    }
    const extensionFile = getExtensionFileByName(name);
    return UPLOAD_FILE_SUPPORT.indexOf('.'.concat(extensionFile)) !== -1;
  }

  selectCard(index) {
    let selected = false;
    this.isSelected.forEach((item, num) => {
      if (index == item) {
        selected = true;
      }
    });
    if (selected) {
      this.$emit('removeItemDoc', this.uploadQueueFiles[index]);
    } else {
      this.isSelected.push(index);
      this.$emit('showSelectedDoc', this.uploadQueueFiles[index]);
    }
  }

  selectAll() {
    this.isSelectAll = !this.isSelectAll;
    this.isSelected = [];
    if (this.isSelectAll) {
      this.uploadQueueFiles.forEach((item, index) => {
        this.isSelected.push(index);
        this.$emit('showSelectedDoc', this.uploadQueueFiles[index]);
      });
    } else {
      this.uploadQueueFiles.forEach((item, index) => {
        this.$emit('removeItemDoc', this.uploadQueueFiles[index]);
      });
    }
  }

  uncheckedItemDoc(item) {
    for (let i = 0; i < this.uploadQueueFiles.length; i++) {
      if (item.name == this.uploadQueueFiles[i].name) {
        const index = this.uploadQueueFiles.indexOf(item);
        const num = this.isSelected.indexOf(index);
        this.isSelected.splice(num, 1);
      }
    }
  }
}
