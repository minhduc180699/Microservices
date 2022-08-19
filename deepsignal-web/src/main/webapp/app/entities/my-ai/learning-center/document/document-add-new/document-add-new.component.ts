import { Inject, Vue, Watch } from 'vue-property-decorator';
import Component from 'vue-class-component';
import { UPLOAD_FILE_SUPPORT, GOOGLE_CONFIG } from '@/shared/constants/ds-constants';
import { getExtensionFileByName } from '@/util/ds-util';
import _ from 'lodash';
import { FileStorageService } from '@/service/file-storage.service';
import DocumentListWrapComponent from '@/entities/my-ai/learning-center/document/document-list-wrap/document-list-wrap.vue';

let tokenClient;
let access_token = null;

@Component({
  components: {
    'doc-wrap-list': DocumentListWrapComponent,
  },
  delimiters: ['${', '}'], // Avoid Twig conflicts
  computed: {
    checkLearning() {
      return this.$store.getters.getLearning;
    },
  },
  watch: {
    checkLearning(value) {
      if (value != this.isLearning) {
        this.isLearning = value;
      }
    },
  },
})
export default class DocumentAddNewComponent extends Vue {
  public itemsSelected = [];
  public uploadQueueFiles = [];
  acceptUploadFile = UPLOAD_FILE_SUPPORT.join(', ');
  public isShowNotice = false;
  public addition = false;
  private parent = 'doc-add';
  private lstFileToLarge = [];
  private isLearning = false;
  @Inject('fileStorageService') private fileStorageService: () => FileStorageService;

  beforeCreate() {
    function gapiInit() {
      // @ts-ignore
      gapi.client.init({
        apiKey: GOOGLE_CONFIG.API_KEY,
        discoveryDocs: [GOOGLE_CONFIG.DISCOVERY_DOCS],
      });
    }
    // @ts-ignore
    gapi.load('client', gapiInit);
    // @ts-ignore
    gapi.load('picker', () => {}); // load google picker
    // @ts-ignore
    tokenClient = google.accounts.oauth2.initTokenClient({
      client_id: GOOGLE_CONFIG.CLIENT_ID,
      scope: GOOGLE_CONFIG.SCOPES,
      callback: '', // defined later
    });
  }

  created() {
    this.$parent.$on('unSelectedDocument', this.unSelectedDocument);
  }

  beforeDestroy(): void {
    this.googleSignout();
  }

  unSelectedDocument() {
    this.$emit('unSelectedDocument');
  }

  showAddition() {
    this.addition = !this.addition;
  }

  openChooseFile() {
    const fileUpload = document.getElementById('fileUpload') as HTMLInputElement;
    fileUpload.click();
  }

  uploadFromPC(files) {
    if (!this.uploadQueueFiles) {
      this.uploadQueueFiles = [];
    }
    this.lstFileToLarge = [];
    const array = this.uploadQueueFiles.filter(file => file.document && file.document === 'local');
    let i = array.length > 0 ? Math.max(...array.map(file => file.id)) : -1;
    for (const file of files) {
      if (this.isValidFileSize(file)) {
        if (this.validateFile(file.name)) {
          if (!this.uploadQueueFiles.filter(item => item.document && item.document === 'local').some(item => item.name === file.name)) {
            // check duplicate file name
            i++;
            Object.assign(file, { id: i, checked: false, document: 'local' });
            this.uploadQueueFiles.push(file);
          }
        }
      } else {
        this.lstFileToLarge.push(file.name);
      }
    }
    // this.uploadQueueFiles.map(item => {
    //   item.id = i;
    //   item.checked = false;
    //   i++;
    // });
    if (this.lstFileToLarge.length > 0) {
      this.noticeFileSizeToLarge();
    }
    // this.$emit('queueFiles', this.uploadQueueFiles);
  }

  isValidFileSize(file) {
    const size = file.size || file.sizeBytes;
    return size / 1024 / 1024 < 50; // restrict file less than 50MB
  }

  noticeFileSizeToLarge() {
    this.$bvModal
      .msgBoxOk(`Upload files must be less than 50MB: ${this.lstFileToLarge.toString()}`, {
        title: 'Invalid File Size',
        size: 'sm',
        buttonSize: 'sm',
        okVariant: 'primary',
        headerClass: 'p-2 border-bottom-0',
        footerClass: 'p-2 mt-0 border-top-0',
        centered: true,
        headerTextVariant: 'danger',
      })
      .then(value => {})
      .catch(err => {});
  }

  validateFile(name): boolean {
    if (!name) {
      return false;
    }
    const extensionFile = getExtensionFileByName(name);
    return UPLOAD_FILE_SUPPORT.indexOf('.'.concat(extensionFile)) !== -1;
  }

  dragleave(event) {
    event.currentTarget.classList.remove('bg-secondary');
  }

  drop(event) {
    this.isShowNotice = false;
    const File = event.dataTransfer.files;
    for (let i = 0; i < File.length; i++) {
      let isFile = false;
      const extending = File[i].name.split('.');
      UPLOAD_FILE_SUPPORT.forEach(item => {
        if ('.' + extending[extending.length - 1] == item) {
          isFile = true;
        }
      });
      if (!isFile) {
        this.isShowNotice = true;
      }
    }
    this.uploadFromPC(event.dataTransfer.files);
    this.dragleave(event);
  }

  dragover(event) {
    if (!event.currentTarget.classList.contains('bg-secondary')) {
      event.currentTarget.classList.add('bg-secondary');
    }
  }

  public submitFile(): void {
    const connectome = JSON.parse(localStorage.getItem('ds-connectome'));
    const userId = connectome.user.id;
    const connectomeId = connectome.connectomeId;
    if (!connectome || !connectomeId || !userId) {
      return;
    }

    const param = userId + '/' + connectomeId;
    const formData = new FormData();
    for (const file of this.itemsSelected) {
      formData.append('files', file);
    }

    this.fileStorageService()
      .uploadDoc(param, formData)
      .then(res => {
        const numOfSuccess = res.data.numOfSuccess;
        const numOfFail = res.data.numOfFail;
        const message = `File success: ${numOfSuccess}. File fail: ${numOfFail}`;
        this.$bvToast.toast(message, {
          toaster: 'b-toaster-bottom-right',
          title: 'Success',
          variant: 'info',
          solid: true,
          autoHideDelay: 5000,
        });
      })
      .catch(() => {
        this.$bvToast.toast('Upload fail!', {
          toaster: 'b-toaster-bottom-right',
          title: 'Fail',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
      });
  }

  deleteAllDoc() {
    this.uploadQueueFiles = [];
  }

  removeFile(e) {
    if (!e) {
      return;
    }
    this.uploadQueueFiles = this.uploadQueueFiles.filter(item => item.id !== e.id);
  }

  handleSelectedDocument(data) {
    this.$emit('handleSelectedDocument', data);
  }

  addLearning() {
    this.$emit('addLearning');
  }

  startLearning() {
    // @ts-ignore
    this.$parent.startLearning();
  }

  public googleSignin() {
    tokenClient.callback = async resp => {
      if (resp.error !== undefined) {
        throw resp;
      }
      access_token = resp.access_token;
      // await this.getFilesFromDrive();
      await this.createPicker(); // create google picker
    };

    if (access_token === null) {
      // Prompt the user to select a Google Account and ask for consent to share their data
      // when establishing a new session.
      tokenClient.requestAccessToken({ prompt: 'consent' });
    } else {
      // Skip display of account chooser and consent dialog for an existing session.
      tokenClient.requestAccessToken({ prompt: '' });
    }
  }

  public googleSignout() {
    access_token = null;
    // @ts-ignore
    google.accounts.oauth2.revoke(access_token);
  }

  // public getFilesFromDrive() {
  //   const params = {
  //     pageSize: 30,
  //     pageToken: this.pageToken,
  //     fields: 'files(id, name, mimeType, size), nextPageToken',
  //     q: '"me" in owners and mimeType != "application/vnd.google-apps.folder"',
  //   };
  //   // @ts-ignore
  //   gapi.client.drive.files.list(params).then(response => {
  //     const files = response.result.files;
  //     console.log('response file = ', response);
  //     this.pageToken = files.length < 20 ? null : response.result.nextPageToken;
  //     // if (!files || files.length == 0) {
  //     //   // document.getElementById('content').innerText = 'No files found.';
  //     //   console.log('no file found');
  //     //   return;
  //     // }
  //   }).catch(err => {
  //     // document.getElementById('content').innerText = err.message;
  //     console.log('error get file = ', err);
  //     return;
  //   });
  // }

  public createPicker() {
    // @ts-ignore
    const view = new google.picker.DocsView(google.picker.ViewId.DOCS);
    view.setMimeTypes(GOOGLE_CONFIG.MIME_TYPES_SUPPORT); // limit file format
    // view.setOwnedByMe(true); // only owner file (not contain share with me)
    // @ts-ignore
    const picker = new google.picker.PickerBuilder()
      // @ts-ignore
      .enableFeature(google.picker.Feature.NAV_HIDDEN)
      // @ts-ignore
      .enableFeature(google.picker.Feature.MULTISELECT_ENABLED)
      .setDeveloperKey(GOOGLE_CONFIG.API_KEY)
      .setAppId(GOOGLE_CONFIG.APP_ID)
      .setOAuthToken(access_token)
      .addView(view)
      // // @ts-ignore
      // .addView(new google.picker.DocsUploadView())
      .setCallback(this.pickerCallback)
      .build();
    picker.setVisible(true);
  }

  public pickerCallback(data) {
    // @ts-ignore
    if (data.action === google.picker.Action.PICKED) {
      this.lstFileToLarge = [];
      data.docs.forEach(item => {
        if (this.isValidFileSize(item)) {
          if (this.uploadQueueFiles.map(file => file.id).includes(item.id)) {
            return;
          }
          // @ts-ignore
          gapi.client.drive.files
            .get({
              fileId: item.id,
              alt: 'media',
            })
            .then(res => {
              const file = new File([new Blob([res.body])], item.name, {
                type: item.mimeType,
                lastModified: item.lastEditedUtc,
              });
              Object.assign(file, { id: item.id, checked: false, document: 'drive' });
              this.uploadQueueFiles.push(file);
            });
        } else {
          this.lstFileToLarge.push(item.name);
        }
      });
      if (this.lstFileToLarge.length > 0) {
        this.noticeFileSizeToLarge();
      }
    }
  }
}
