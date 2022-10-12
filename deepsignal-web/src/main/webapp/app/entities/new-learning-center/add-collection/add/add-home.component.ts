import { Component, Vue } from 'vue-property-decorator';
import addSearch from '@/entities/new-learning-center/add-collection/add/add-search/add-search.vue';
import addUrl from '@/entities/new-learning-center/add-collection/add/add-url/add-url.vue';
import addText from '@/entities/new-learning-center/add-collection/add/add-text/add-text.vue';
import addDocument from '@/entities/new-learning-center/add-collection/add/add-document/add-document.vue';

@Component({
  components: {
    addSearch: addSearch,
    addText: addText,
    addUrl: addUrl,
    addDocument: addDocument,
  },
})
export default class addHome extends Vue {
  private tabs = [
    { name: 'Web Search', component: 'addSearch', active: true, icon: 'icon-websearch' },
    { name: 'Text', component: 'addText', active: false, icon: 'icon-memo-write' },
    { name: 'URL', component: 'addUrl', active: false, icon: 'icon-link-add' },
    { name: 'Document', component: 'addDocument', active: false, icon: 'icon-document-add' },
  ];
  private currentTab = 'addSearch';
  private displayMode = 'grid';
  private fullScreenMode = false;

  changeTab(tab, e) {
    e.preventDefault();
    this.tabs.forEach(t => {
      t.active = false;
      if (tab.name == t.name) {
        this.currentTab = tab.component;
        t.active = true;
      }
    });
  }

  fullScreen() {
    this.fullScreenMode = !this.fullScreenMode;
    this.$emit('fullScreenMode', this.fullScreenMode);
  }

  closeFullScreenMode() {
    this.fullScreenMode = false;
    this.$emit('fullScreenMode', this.fullScreenMode);
  }
}
