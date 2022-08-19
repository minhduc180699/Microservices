import { Component, Inject, Vue, Watch } from 'vue-property-decorator';
import Activity from '@/entities/my-ai/learning-center/activity/activity.vue';
import Document from '@/entities/my-ai/learning-center/document/document.vue';
import Search from '@/entities/my-ai/learning-center/search/search.vue';
import Web from '@/entities/my-ai/learning-center/web/web.vue';
import LearnedDocument from '@/entities/my-ai/learning-center/learned-document/learned-document.vue';
import FeedService from '@/core/home/feed/feed.service';
import { LearningService } from '@/service/learning.service';
import { PrincipalService } from '@/service/principal.service';
import { namespace } from 'vuex-class';
import { FILE_TYPE_UPLOAD, SESSION_STORAGE_CONSTANTS } from '@/shared/constants/ds-constants';
import vueCustomScrollbar from 'vue-custom-scrollbar';

const myAiViewState = namespace('myAIViewStateStore');
@Component({
  components: {
    activity: Activity,
    document: Document,
    search: Search,
    web: Web,
    vueCustomScrollbar,
    learnedDocument: LearnedDocument,
  },
})
export default class learningCenter extends Vue {
  @Inject('feedService') private feedService: () => FeedService;
  @Inject('learningService') private learningService: () => LearningService;
  @Inject('principalService') private principalService: () => PrincipalService;
  private currentTab = 'search';
  private tabs = [
    { name: 'Search', component: 'search', active: true, icon: 'bi bi-search' },
    { name: 'Website', component: 'web', active: false, icon: 'bi bi-window' },
    { name: 'Document', component: 'document', active: false, icon: 'bi bi-file-earmark-text' },
    { name: 'Trained Document', component: 'learnedDocument', active: false, icon: 'bi bi-file-earmark-check' },
    { name: 'My Activity', component: 'activity', active: false, icon: 'bi bi-person-check' },
  ];
  private isShowSelected = false;
  private selectedSearch = [];
  private selectedSearchShow = [];
  private isLearning = true;
  private selectedDocument = [];
  private selectedWeb = [];
  private displayMode = 'grid';
  private scrollSettings = {
    wheelPropagation: false,
    suppressScrollX: true,
    suppressScrollY: false,
  };
  loaderDisable = false;

  created(): void {
    const routeHash = this.$route.hash;
    this.currentTab = routeHash ? routeHash.substring(1) : this.tabs[0].component;
    this.tabs.map(item => (item.active = item.component === this.currentTab));
  }

  @myAiViewState.Action
  public setMainVerticalTabIndex!: (index: number) => void;

  public changeTab(tabNavigate, e, isLearnedTab?) {
    e.preventDefault();
    this.tabs.forEach(tab => {
      tab.active = false;
      if (isLearnedTab && tab.component === 'learnedDocument') {
        tab.active = true;
        this.currentTab = tab.component;
        return;
      } else {
        if (tabNavigate == tab) {
          tab.active = true;
          this.currentTab = tab.component;
        }
      }
    });

    location.hash = this.currentTab;
  }

  showSelected() {
    this.isShowSelected = !this.isShowSelected;
  }

  mounted() {
    document.body.setAttribute('data-menu', 'learning-center');
    this.setMainVerticalTabIndex(3);
  }

  handleSelectedSearch(data) {
    this.selectedSearch = data;
    const list = [...data];
    for (let i = 0; i < list.length; i++) {
      for (let j = i + 1; j < list.length; j++) {
        if (list[i].keyword === list[j].keyword) {
          list.splice(j, 1);
          j--;
        }
      }
    }
    this.selectedSearchShow = [];
    list.forEach(item => {
      const show = { key: item.keyword, arr: [] };
      this.selectedSearch.forEach(value => {
        if (item.keyword === value.keyword) {
          show.arr.push(value);
        }
      });
      this.selectedSearchShow.push(show);
    });
  }

  removeSelectedSearch(item, index) {
    for (let i = 0; i < this.selectedSearch.length; i++) {
      if (this.selectedSearch[i].keyword === item.key) {
        this.selectedSearch.splice(i, 1);
        i--;
      }
    }
    this.selectedSearchShow.splice(index, 1);
    this.$emit('unSelectedSearch', item.key);
  }

  handleSelectedWeb(data) {
    this.selectedWeb = data;
  }

  removeSelectedWeb() {
    this.$emit('unSelectedWeb');
    this.selectedWeb = [];
  }

  handleSelectedDocument(data) {
    this.selectedDocument = data;
  }

  removeSelectedDocument() {
    this.$emit('unSelectedDocument', this.selectedDocument);
    this.selectedDocument = [];
  }

  learning() {
    if (this.selectedSearch.length < 1 && this.selectedDocument.length < 1 && this.selectedWeb.length < 1) {
      return;
    }
    const urlList = [];
    this.changeLearningStatus();
    this.isLearning = false;
    if (this.selectedSearch.length > 0) {
      this.selectedSearch.forEach(item => {
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
            type: FILE_TYPE_UPLOAD.URL,
          };
          urlList.push(urlDTO);
        }
      });
    }
    if (this.selectedWeb.length > 0) {
      this.selectedWeb.forEach(item => {
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
            type: FILE_TYPE_UPLOAD.WEB,
          };
          urlList.push(urlDTO);
        }
      });
    }
    const formData = new FormData();
    for (const file of this.selectedDocument) {
      formData.append(file.document === 'drive' ? 'driveFiles' : 'localFiles', file);
    }
    formData.append('urls', JSON.stringify({ urlUploadDTOS: urlList }));
    const language = this.$i18n.locale;
    const userId = this.principalService().getUserInfo().id;
    const connectomeId = this.principalService().getConnectomeInfo().connectomeId;
    this.learningService()
      .learning(userId, connectomeId, '', language, formData)
      .then(res => {
        this.selectedSearch = [];
        // this.cardSelected = [];
        this.isLearning = true;
        this.$store.commit('setLearning', false);
        this.goFeed();
      })
      .catch(error => {
        this.isLearning = true;
      });
  }

  changeLearningStatus() {
    this.$store.commit('setLearning', true);
    sessionStorage.setItem(SESSION_STORAGE_CONSTANTS.FEED_STATUS_TRAINING, 'true');
  }

  DeleteAllSelected() {
    this.removeSelectedDocument();
    this.removeSelectedWeb();
    this.$emit('unSelectedAllSearch');
    this.selectedSearchShow = [];
    this.selectedSearch = [];
  }

  goFeed() {
    this.$router.push('/my-ai/connectome/2dnetwork');
  }
}
