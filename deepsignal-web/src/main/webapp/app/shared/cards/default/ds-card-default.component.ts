import { Component, Vue, Prop } from 'vue-property-decorator';
import DsCardFooter from '@/shared/cards/footer/ds-card-footer.vue';
import DsCardNotUseful from '@/shared/cards/not-useful/ds-card-not-useful.vue';
import { getMaxImageBySize } from '@/util/ds-feed-util';
import { getExtensionFileBySearchType } from '@/util/ds-util';
import { ACTIVITY, CARD_SIZE } from '@/shared/constants/feed-constants';
import { FILE_TYPE } from '@/shared/constants/ds-constants';
import { randomEleInArray } from '@/util/array-util';

@Component({
  components: {
    'ds-card-footer': DsCardFooter,
    'ds-card-not-useful': DsCardNotUseful,
  },
  computed: {
    showDeleteCard() {
      return this.$store.getters.getId;
    },
  },
  watch: {
    showDeleteCard(value, oldValue) {
      if (value) {
        this.handleCloseDeleteCard(value);
      }
    },
  },
})
export default class DsCardDefault extends Vue {
  @Prop(Object) readonly item: any | undefined;
  @Prop(String) readonly tab: any | undefined;
  @Prop(String) readonly keyword: any | '';
  image;
  imageFull;
  isImage;
  private cardSide = CARD_SIZE;
  private youtubeDomain = ['www.youtube.com', 'm.youtube.com'];
  private isFile = false;
  private isImageFile = '';
  private isHidden = false;
  private isLoadingDelete = true;
  private isMyAi = false;
  private stateDelete = false;

  data() {
    return {
      image: this.image,
      imageFull: this.imageFull,
    };
  }

  public get highlightedWord(): string {
    if (this.keyword) {
      return this.keyword.split('/').map(item => item.trim());
    } else {
      return '';
    }
  }

  onErrorImage() {
    if (this.isImage) {
      this.image = randomEleInArray(this.item.subImages);
    } else {
      this.imageFull = randomEleInArray(this.item.subImages);
    }
  }

  mounted() {
    this.randomImageOrFullImage();
    if (!this.item || !this.item.search_type) {
      return;
    }
    if (this.item.search_type === "PDF" || this.item.search_type === "PPT") {
      let iconUrl;
      const extension = getExtensionFileBySearchType(this.item.search_type);
      if (FILE_TYPE.EXCEL.some(v => extension.includes(v))) {
        iconUrl = 'file-excel-fill.svg';
      } else if (FILE_TYPE.COMPRESSED.some(v => extension.toLowerCase().includes(v))) {
        iconUrl = 'file-zip-fill.svg';
      } else if (FILE_TYPE.PPT.some(v => extension.toLowerCase().includes(v))) {
        iconUrl = 'file-ppt-fill.svg';
      } else if (FILE_TYPE.PDF.some(v => extension.toLowerCase().includes(v))) {
        iconUrl = 'file-pdf-fill.svg';
      } else if (FILE_TYPE.DOC.some(v => extension.toLowerCase().includes(v))) {
        iconUrl = 'file-word-fill.svg';
      } else {
        return ['/content/images/empty-image.png'];
      }
      this.isFile = true;
      this.isImageFile = '/content/images/common/file/' + iconUrl;
    }
  }

  get connectomeId() {
    return JSON.parse(localStorage.getItem('ds-connectome')).connectomeId;
  }

  randomImageOrFullImage() {
    const imgLucky = randomEleInArray(['image', 'imageFull']);
    if (imgLucky == 'imageFull' && this.item.dataSize == CARD_SIZE._2_1) {
      this.isImage = false;
      this.imageFull = getMaxImageBySize(this.item.subImages);
    } else {
      this.isImage = true;
      this.image = getMaxImageBySize(this.item.subImages);
    }
  }

  saveToLocalStorage() {
    // if (localStorage.getItem('card-data')) {
    //   localStorage.removeItem('card-data');
    // }
    if (localStorage.getItem('stock-codes')) {
      localStorage.removeItem('stock-codes');
    }
    // const cardShorten = {
    //   title: this.item.title,
    //   sourceId: this.item.sourceId,
    //   writerName: this.item.writerName,
    //   favicon: this.item.favicon,
    //   // publishedAt: this.item.publishedAt,
    //   recommendDate: this.item.recommendDate,
    //   imageLinks: this.item.imageLinks,
    //   id: this.item.id,
    //   docId: this.item.docId,
    //   searchType: this.item.searchType,
    //   content: this.item.content,
    // };
    // localStorage.setItem('card-data', JSON.stringify(cardShorten));
    if (this.$route.fullPath.split('/').includes('feed')) {
      localStorage.setItem('stock-codes', JSON.stringify(this.item.stockCodes));
    }
  }

  getEmbedLink(url) {
    const id = url.split('?v=')[1];
    return 'https://www.youtube.com/embed/' + id;
  }

  isYoutube(url) {
    return this.youtubeDomain.some(domain => url.split('/').includes(domain));
  }

  moreAction(transfer) {
    // @ts-ignore
    // this.$refs.showMore.moreAction(transfer);
  }

  errorImage(item) {
    if (item.searchType) {
      let iconUrl;
      const extension = getExtensionFileBySearchType(item.searchType);
      if (FILE_TYPE.EXCEL.some(v => extension.includes(v))) {
        iconUrl = 'file-excel-fill.svg';
      } else if (FILE_TYPE.COMPRESSED.some(v => extension.includes(v))) {
        iconUrl = 'file-zip-fill.svg';
      } else if (FILE_TYPE.PPT.some(v => extension.includes(v))) {
        iconUrl = 'file-ppt-fill.svg';
      } else if (FILE_TYPE.PDF.some(v => extension.includes(v))) {
        iconUrl = 'file-pdf-fill.svg';
      } else if (FILE_TYPE.DOC.some(v => extension.includes(v))) {
        iconUrl = 'file-word-fill.svg';
      } else {
        return ['/content/images/empty-image.png'];
      }
      this.isFile = true;
      return ['/content/images/common/file/' + iconUrl];
    }
    return ['content/images/empty-image.png'];
  }

  handleHiddenCard(activity, checkMyAi) {
    if (activity == ACTIVITY.delete) {
      this.isLoadingDelete = false;
      this.isHidden = !this.isHidden;
    }
    this.isMyAi = checkMyAi;
  }

  handleActivity(item, activity) {
    this.isHidden = false;
    this.isLoadingDelete = false;
    this.$emit('handleActivity', item, activity);
  }

  hiddenCard(value, feedback?) {
    if (!value) {
      this.isHidden = !this.isHidden;
    } else {
      this.isLoadingDelete = true;
      // @ts-ignore
      this.$refs.handleHideCard.callApiActivity(true, ACTIVITY.delete, 0, feedback);
    }
  }

  handleCloseDeleteCard(value) {
    if (value === this.item.id) {
      this.stateDelete = true;
    } else {
      this.stateDelete = false;
      this.isHidden = false;
    }
  }

  checkRegexDate(value) {
    const regex = new RegExp('ago');
    if (regex.test(value)) return value;
    else return new Date(value);
  }
}
