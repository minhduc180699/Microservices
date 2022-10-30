import { documentCard } from '@/shared/model/document-card.model';
import { Component, Vue } from 'vue-property-decorator';

@Component
export default class DsText extends Vue {
  item = new documentCard();

  insertToMemory() {
    if (this.item.content && this.item.title) {
      if (localStorage.getItem('ds-connectome')) {
        const connectome = JSON.parse(localStorage.getItem('ds-connectome'));
        this.item.author = connectome.connectomeName;
      }
      this.item.keyword = '';
      this.item.type = 'USERNOTE';
      this.item.searchType = 'USERNOTE';
      this.item.addedAt = new Date();
      this.item.url = '';
      this.item.images = [];
      this.item.favicon = '';
      this.$root.$emit('cart-to-conlection', [Object.assign({}, this.item)], 'text');
    }
  }
}
