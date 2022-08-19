import { Component, Prop, Vue, Watch } from 'vue-property-decorator';
import headerLearning from '@/entities/learning-center/header/headerLearning.vue';
import search from '@/entities/learning-center/search/search.vue';
import web from '@/entities/learning-center/web/web.vue';
import selected from '@/entities/learning-center/selected/selected.vue';
import document from '@/entities/learning-center/document/document.vue';

@Component({
  components: {
    headerLearning: headerLearning,
    searchLearning: search,
    webLearning: web,
    selected: selected,
    document: document,
  },
})
export default class layout extends Vue {
  @Prop(String) keywords: string | '';
  private display = 0;
  private itemSelectedDocument = [];
  private itemSelectedMetaSearch = [];
  private itemsSelectedInternalSearch = [];
  private itemsSelectedWeb = [];

  changeTab(item) {
    this.display = item;
  }

  // search
  showSelectedSearch(data) {
    if (data.metasearch) {
      let val = true;
      for (let i = 0; i < this.itemSelectedMetaSearch.length; i++) {
        if (this.itemSelectedMetaSearch[i].link === data.link) {
          val = false;
        }
      }
      if (val) {
        this.itemSelectedMetaSearch.push(data);
      }
    } else {
      let val = true;
      for (let i = 0; i < this.itemsSelectedInternalSearch.length; i++) {
        if (this.itemsSelectedInternalSearch[i].id === data.id) {
          val = false;
        }
      }
      if (val) {
        this.itemsSelectedInternalSearch.push(data);
      }
    }
  }

  removeItemSearch(item) {
    if (item.metasearch) {
      for (let i = 0; i < this.itemSelectedMetaSearch.length; i++) {
        if (this.itemSelectedMetaSearch[i].link === item.link) {
          this.itemSelectedMetaSearch.splice(i, 1);
        }
      }
    } else {
      for (let i = 0; i < this.itemsSelectedInternalSearch.length; i++) {
        if (this.itemsSelectedInternalSearch[i].id === item.id) {
          this.itemsSelectedInternalSearch.splice(i, 1);
        }
      }
    }
    this.$emit('uncheckedCard', item);
  }

  // web
  showSelectedWeb(item) {
    let val = true;
    for (let i = 0; i < this.itemsSelectedWeb.length; i++) {
      if (this.itemsSelectedWeb[i].url === item.url) {
        val = false;
      }
    }
    if (val) {
      this.itemsSelectedWeb.push(item);
    }
  }

  removeItemWeb(item) {
    for (let i = 0; i < this.itemsSelectedWeb.length; i++) {
      if (this.itemsSelectedWeb[i].url === item.url) {
        this.itemsSelectedWeb.splice(i, 1);
      }
    }
    this.$emit('uncheckedItemWeb', item);
  }
  // document
  showSelectedDoc(item) {
    this.itemSelectedDocument.push(item);
  }

  removeItemDoc(item) {
    for (let i = 0; i < this.itemSelectedDocument.length; i++) {
      if (this.itemSelectedDocument[i].name === item.name) {
        this.itemSelectedDocument.splice(i, 1);
      }
    }
    this.$emit('uncheckedItemDoc', item);
  }

  handleNext() {
    // @ts-ignore
    this.$refs.handleNext.handleNext();
  }

  showToastSuccess(message) {
    this.$bvToast.toast(message, {
      title: 'Upload status',
      variant: 'primary',
      autoHideDelay: 5000,
      solid: true,
    });
  }

  showToastFailure(message) {
    this.$bvToast.toast(message, {
      title: 'Upload status:',
      variant: 'danger',
      autoHideDelay: 5000,
      solid: true,
    });
  }

  closeModal() {
    this.$emit('closeModal');
  }
}
