import { Component, Inject, Prop, Vue, Watch } from 'vue-property-decorator';
import selectedLearning from '@/entities/learning-center/selected-learning/selected-learning.vue';
import FeedService from '@/core/home/feed/feed.service';
import { LearningService } from '@/service/learning.service';
import { PrincipalService } from '@/service/principal.service';

@Component({
  components: {
    slectedLearning: selectedLearning,
  },
})
export default class selected extends Vue {
  @Prop(Array) readonly itemSelectedMetaSearch: any | [];
  @Prop(Array) readonly itemsSelectedWeb: any | [];
  @Prop(Array) readonly itemsSelectedInternalSearch: any | [];
  @Prop(Array) readonly itemSelectedDocument: any | [];
  @Prop({ default: false }) hasLearning;
  @Inject('feedService') private feedService: () => FeedService;
  @Inject('learningService') private learningService: () => LearningService;
  @Inject('principalService') private principalService: () => PrincipalService;
  private isShow = false;
  private isLearning = true;
  private data = [];

  @Watch('itemSelectedMetaSearch')
  @Watch('itemsSelectedWeb')
  @Watch('itemsSelectedInternalSearch')
  @Watch('itemSelectedDocument')
  onItemChange(value, oldValue) {
    this.data = [];
    this.data = this.data
      .concat(this.itemsSelectedInternalSearch)
      .concat(this.itemSelectedMetaSearch)
      .concat(this.itemsSelectedWeb)
      .concat(this.itemSelectedDocument);
  }

  removeItem(item) {
    if (item.metasearch) {
      this.$emit('removeItemSearch', item);
    }
    if (item.url) {
      this.$emit('removeItemWeb', item);
    }
    if (item.name) {
      this.$emit('removeItemDoc', item);
    }
  }

  handleNext() {
    this.$emit('handleNext');
  }

  learning() {
    this.isLearning = false;
    this.submitLearning();
  }

  public submitLearning() {
    const formData = new FormData();
    // saveDoc
    let docs = '';
    for (let i = 0; i < this.itemsSelectedInternalSearch.length; i++) {
      if (i + 1 == this.itemsSelectedInternalSearch.length) {
        docs = docs.concat(this.itemsSelectedInternalSearch[i].id);
      } else {
        docs = docs.concat(this.itemsSelectedInternalSearch[i].id + ',');
      }
    }
    for (const file of this.itemSelectedDocument) {
      formData.append('files', file);
    }
    const urlList = [];
    if (this.itemsSelectedWeb.length > 0) {
      this.itemsSelectedWeb.forEach(item => {
        if (item.title || item.desc || item.url) {
          const urlDTO = {
            name: item.title || item.desc,
            url: item.url,
            author: item.author ? item.author : '',
            originDate: item.date ? item.date : new Date().toISOString(),
            searchType: item.searchType ? item.searchType : '',
            favicon: item.favicon,
            img: item.image,
            description: item.desc,
          };
          urlList.push(urlDTO);
        }
      });
    }
    if (this.itemSelectedMetaSearch.length > 0) {
      this.itemSelectedMetaSearch.forEach(item => {
        if (item.title || item.author) {
          const urlDTO = {
            name: item.title || item.author,
            url: item.link,
            author: item.author,
            originDate: item.org_date ? item.org_date : new Date().toISOString(),
            searchType: item.searchType ? item.searchType : '',
            favicon: item.favicon,
            img: item.img,
            description: item.description,
            keyword: item.keyword,
          };
          urlList.push(urlDTO);
        }
      });
    }
    formData.append('urls', JSON.stringify({ urlUploadDTOS: urlList }));
    const language = this.$i18n.locale;
    const userId = this.principalService().getUserInfo().id;
    const connectomeId = this.principalService().getConnectomeInfo().connectomeId;
    this.learningService()
      .learning(userId, connectomeId, docs, language, formData)
      .then(res => {
        const message = `Items upload successfully: ${res.data.numOfSuccess}. Items upload failed: ${res.data.numOfFail}`;
        while (this.itemSelectedDocument.length > 0) {
          this.removeItem(this.itemSelectedDocument[0]);
        }
        while (this.itemsSelectedInternalSearch.length > 0) {
          this.removeItem(this.itemsSelectedInternalSearch[0]);
        }
        while (this.itemsSelectedWeb.length > 0) {
          this.removeItem(this.itemsSelectedWeb[0]);
        }
        while (this.itemSelectedMetaSearch.length > 0) {
          this.removeItem(this.itemSelectedMetaSearch[0]);
        }
        this.$emit('showToastSuccess', message);
        this.training();
      })
      .catch(error => {
        this.$emit('showToastFailure', 'Upload failed! Please try again.');
        this.isLearning = true;
      });
  }

  training() {
    const connectome = JSON.parse(localStorage.getItem('ds-connectome'));
    const connectomeId = connectome.connectomeId;
    const language = localStorage.getItem('currentLanguage');
    const data = new FormData();
    data.append('sourceLang', language);
    this.feedService()
      .trainingConnectome(connectomeId, data)
      .then(res => {
        this.isLearning = true;
        // this.showQRCode();
        this.goFeed();
      })
      .catch(error => {
        console.log('error', error);
        this.isLearning = true;
      });
  }

  showQRCode() {
    this.$store.commit('setQRcode', true);
  }

  goFeed() {
    this.$router.push({ path: '/feed', query: { isLearning: 'true' } });
    this.$root.$emit('bv::hide::modal', 'learning-center');
  }
}
