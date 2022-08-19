import { Component, Vue } from 'vue-property-decorator';

@Component
export default class asideLearning extends Vue {
  private activeClass = 0;
  private data = [
    { name: 'Search', icon: 'bi bi-search', id: 'resource-search-tab', ariaControls: 'search' },
    { name: 'Web', icon: 'bi bi-window', id: 'resource-web-tab', ariaControls: 'web' },
    { name: 'Document', icon: 'bi bi-folder2', id: 'resource-document-tab', ariaControls: 'document' },
    { name: 'Social', icon: 'bi bi-file-person', id: 'resource-social-tab', ariaControls: 'social' },
  ];

  changeTab(index) {
    this.activeClass = index;
    this.$emit('changeTab', index);
  }

  getClass(index) {
    if (index != 3) {
      if (this.activeClass == index) {
        return 'list-group-item list-group-item-action active';
      } else {
        return 'list-group-item list-group-item-action';
      }
    } else {
      return 'list-group-item list-group-item-action disabled';
    }
  }

  handleNext() {
    if (this.activeClass >= 2) {
      this.activeClass = 0;
    } else {
      this.activeClass++;
    }
    this.$emit('changeTab', this.activeClass);
  }
}
