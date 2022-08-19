import { Component, Inject, Prop, Vue } from 'vue-property-decorator';
import MetaSearchSearchLearningComponent from '@/entities/connectome/meta-search/meta-search-learning/meta-search-learning.vue';
import axios from 'axios';
import { error } from 'jquery';
import FeedService from '@/core/home/feed/feed.service';
import { LearningService } from '@/service/learning.service';
import { PrincipalService } from '@/service/principal.service';

const API_PATH = 'api/file-storage';

@Component({
  components: {
    'meta-search-learning': MetaSearchSearchLearningComponent,
  },
})
export default class MetaSearchSelectedComponent extends Vue {
  @Prop(Array) readonly itemsSelected: any | [];
  @Prop(Array) readonly itemsSelectedSearch: any | [];
  @Prop(Array) readonly itemsSelectedWeb: any | [];
  @Prop(Array) readonly itemSelectedMetaSearch: any | [];
  @Prop({ default: false }) hasLearning;
  @Inject('feedService') private feedService: () => FeedService;
  @Inject('learningService') private learningService: () => LearningService;
  @Inject('principalService') private principalService: () => PrincipalService;
  isShow = true;
  dataSearch = [];
  isloading = true;
  countUploadFile;
  isQRCodeShow = false;

  removeItem(index) {
    this.$emit('removeItem', index);
  }

  learning() {
    this.isloading = false;
    this.upload();
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
        this.isloading = true;

        this.showQRCode();

        //this.$router.push({ path: '/feed', query: { isLearning: 'true' } });
        //this.$root.$emit('bv::hide::modal', 'learning-center');
      })
      .catch(error => {
        console.log('error', error);
        this.isloading = true;
      });
  }

  upload() {
    this.submitLearning();
  }

  handleNext() {
    this.$emit('handleNext');
  }

  uploading() {
    // this.$emit('learning');
  }

  public submitLearning() {
    const formData = new FormData();
    // saveDoc
    let docs = '';
    for (let i = 0; i < this.itemsSelectedSearch.length; i++) {
      if (i + 1 == this.itemsSelectedSearch.length) {
        docs = docs.concat(this.itemsSelectedSearch[i].id);
      } else {
        docs = docs.concat(this.itemsSelectedSearch[i].id + ',');
      }
    }
    for (const file of this.itemsSelected) {
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
            date: item.date ? item.date : new Date().toISOString(),
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
            date: item.org_date ? item.org_date : new Date().toISOString(),
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
        const message = `Successfully: ${res.data.numOfSuccess}. Fail: ${res.data.numOfFail}`;
        for (let i = 0; i < this.itemsSelected.length; i++) {
          this.removeItem(this.itemsSelected[i]);
        }
        while (this.itemsSelectedSearch.length > 0) {
          this.removeItemSearch(this.itemsSelectedSearch[0]);
        }
        while (this.itemsSelectedWeb.length > 0) {
          this.removeItemWeb(this.itemsSelectedWeb[0]);
        }
        while (this.itemSelectedMetaSearch.length > 0) {
          this.removeItemSearch(this.itemSelectedMetaSearch[0]);
        }
        this.$emit('showToastSuccess', message);
        this.training();
      })
      .catch(error => {
        this.$emit('showToastFailure', 'Upload failed!');
      });
  }

  public getParam() {
    const connectome = JSON.parse(localStorage.getItem('ds-connectome'));
    const userId = connectome.user.id;
    const connectomeId = connectome.connectomeId;
    if (!connectome || !connectomeId || !userId) {
      return null;
    }

    const param = userId + '/' + connectomeId;
    return param;
  }

  //
  public submitFile(): void {
    if (this.itemsSelected.length < 1 || this.getParam() == null) {
      return;
    }
    const formData = new FormData();
    for (const file of this.itemsSelected) {
      formData.append('files', file);
    }
    /*
      Make the request to the POST /upload URL
    */
    axios
      .post(API_PATH.concat('/upload/').concat(this.getParam()), formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      })
      .then(res => {
        const numOfSuccess = res.data.numOfSuccess;
        const numOfFail = res.data.numOfFail;
        const message = `File upload successfully:  ${res.data.numOfSuccess}.
             File is duplicated: ${res.data.numOfFail}`;
        for (let i = 0; i < this.itemsSelected.length; i++) {
          this.removeItem(this.itemsSelected[i]);
        }
        this.$emit('showToastSuccess', message);
      })
      .catch(error => {
        console.log('error', error);
        this.$emit('showToastFailure', 'Upload files failed!');
      });
  }

  //
  public submitDoc() {
    // function convertTZ(date, tzString) {
    //   return new Date((typeof date === "string" ? new Date(date) : date).toLocaleString("en-US", {timeZone: tzString}));
    // }
    if (this.itemsSelectedSearch.length < 1 || this.getParam() == null) {
      //
    } else {
      let docs = '';
      for (let i = 0; i < this.itemsSelectedSearch.length; i++) {
        // const date = new Date(this.itemsSelectedSearch[i].fields.published_at[0]);
        // const dateSave = date.toISOString().split('T')[0] + ' ' + date.toISOString().split('T')[1].substring(0, 2) + ':' + date.getMinutes();
        if (i + 1 == this.itemsSelectedSearch.length) {
          docs = docs.concat(this.itemsSelectedSearch[i].id);
        } else {
          docs = docs.concat(this.itemsSelectedSearch[i].id + ',');
        }
      }
      const data = new FormData();
      data.append('docs', docs);
      axios
        .post(API_PATH + '/uploadDOC/' + this.getParam(), data)
        .then(res => {
          while (this.itemsSelectedSearch.length > 0) {
            this.removeItemSearch(this.itemsSelectedSearch[0]);
          }
          this.$emit('showToastSuccess', 'Upload Docs successfully!');
        })
        .catch(error => {
          console.log('error', error);
          this.$emit('showToastFailure', 'Upload Docs fail!');
        });
    }
  }

  //
  public submitUrl() {
    if ((this.itemsSelectedWeb.length < 1 && this.itemSelectedMetaSearch.length < 1) || this.getParam() == null) {
      //
    } else {
      const urlList = [];
      if (this.itemsSelectedWeb.length > 0) {
        this.itemsSelectedWeb.forEach(item => {
          if (item.title || item.desc || item.url) {
            const urlDTO = {
              name: item.title || item.desc,
              url: item.url,
              author: item.author ? item.author : '',
              date: item.date ? item.date : new Date().toISOString(),
              searchType: item.searchType ? item.searchType : '',
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
              date: item.org_date ? item.org_date : new Date().toISOString(),
              searchType: item.searchType ? item.searchType : '',
            };
            urlList.push(urlDTO);
          }
        });
      }
      axios
        .post(API_PATH + '/uploadURL/' + this.getParam(), urlList)
        .then(res => {
          while (this.itemsSelectedWeb.length > 0) {
            this.removeItemWeb(this.itemsSelectedWeb[0]);
          }
          while (this.itemSelectedMetaSearch.length > 0) {
            this.removeItemSearch(this.itemSelectedMetaSearch[0]);
          }
          const message = `Url upload successfully:  ${res.data.numOfSuccess}.
             Url is duplicated: ${res.data.numOfFail}`;
          this.$emit('showToastSuccess', message);
        })
        .catch(error => {
          console.log('error upload url', error);
          this.$emit('showToastFailure', 'Upload Url fail!');
        });
    }
  }

  //
  removeItemSearch(item) {
    this.$emit('removeItemSearch', item);
  }

  removeItemWeb(item) {
    this.$emit('removeItemWeb', item);
  }

  showQRCode() {
    console.log('show QRCode!!!!!!!!!!!!!');
    this.$store.commit('setQRcode', true);
  }
}
