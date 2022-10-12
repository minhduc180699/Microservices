import { Component, Vue } from 'vue-property-decorator';
import vueCustomScrollbar from 'vue-custom-scrollbar';

@Component({
  components: {
    vueCustomScrollbar: vueCustomScrollbar,
  },
  computed: {
    getUrlCards() {
      return this.$store.getters.getUrlCards;
    },
    getDocumentCards() {
      return this.$store.getters.getDocumentCards;
    },
  },
  watch: {
    getUrlCards(value) {
      this.setDataUrlCards(value);
    },
    getDocumentCards(value) {
      this.setDataDocumentCards(value);
    },
  },
})
export default class collection extends Vue {
  private scrollSettings = {
    wheelPropagation: false,
  };
  private urlCards = [];
  private documentCards = [];
  private disableButton = true;
  private countActive = 0;
  private updateData = 'updateData';

  setDataUrlCards(data) {
    if (!data) {
      return;
    }
    if (!this.urlCards || this.urlCards.length == 0) {
      this.urlCards = data;
    } else {
      data.forEach((item, index) => {
        if (!this.urlCards.find(element => element.url == item.url)) {
          this.urlCards.push(item);
        }
      });
    }
    this.checkDisableButton();
  }

  setDataDocumentCards(data) {
    if (!data) {
      return;
    }
    if (!this.documentCards || this.documentCards.length == 0) {
      this.documentCards = data;
    } else {
      data.forEach((item, index) => {
        if (!this.documentCards.find(element => element.name == item.name)) {
          this.documentCards.push(item);
        }
      });
    }
    this.checkDisableButton();
  }

  selectUrlCard(position) {
    this.urlCards[position].selected = !this.urlCards[position].selected;
    this.checkDisableButton();
  }

  selectDocumentCard(position) {
    this.documentCards[position].selected = !this.documentCards[position].selected;
    this.checkDisableButton();
  }

  checkDisableButton() {
    this.disableButton = true;
    this.countActive = 0;
    const urlData = this.urlCards.filter(element => element.selected);
    if (urlData && urlData.length > 0) {
      this.disableButton = false;
      this.countActive += urlData.length;
    }
    const documentData = this.documentCards.filter(element => element.selected);
    if (documentData && documentData.length > 0) {
      this.disableButton = false;
      this.countActive += documentData.length;
    }
    this.updateData += '1';
  }
}
