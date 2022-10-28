import { Component, Prop, Vue, Watch } from 'vue-property-decorator';
import { GOOGLE_CONFIG, UPLOAD_FILE_SUPPORT } from '@/shared/constants/ds-constants';
import { getExtensionFileByName } from '@/util/ds-util';
import vueCustomScrollbar from 'vue-custom-scrollbar';
import singleCard from '@/entities/new-learning-center/aside/single-card/single-card.vue';
import { documentCard } from '@/shared/model/document-card.model';

let tokenClient;
let access_token = null;

@Component({
  components: {
    vueCustomScrollbar: vueCustomScrollbar,
    'single-card': singleCard,
  },
})
export default class Document extends Vue {
  @Prop(Boolean) readonly fullScreenMode: false | any;
  scrollSettings = {
    wheelPropagation: false,
  };
  acceptUploadFile = UPLOAD_FILE_SUPPORT.join(', ');
  public uploadQueueFiles = [];
  lstFileToLarge = [];
  selectedCards = [];
  disableButton = true;
  updateList = 'updateList';
  countActive = 0;

  totalSelected = 0;
  selectedItems = [];
  checked = false;

  @Watch('uploadQueueFiles')
  onListDataChange(value) {
    this.checkDisableButton();
  }

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

  public openChooseFile() {
    const fileUpload = document.getElementById('fileUpload') as HTMLInputElement;
    fileUpload.click();
  }

  public uploadFromPC(files) {
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
            console.log(file);
            const obj = new documentCard();
            obj.author = 'My PC';
            obj.title = file.name;
            obj.content = file.size;
            obj.type = 'URL';
            obj.searchType = 'WEB';
            obj.addedAt = file.lastModified;
            obj.url = '';
            obj.images = [];
            obj.favicon = '';
            this.uploadQueueFiles.push(Object.assign(obj, { id: i, checked: false, document: 'local', selected: true, name: file.name }));
          }
        }
      } else {
        this.lstFileToLarge.push(file.name);
      }
    }
    if (this.lstFileToLarge.length > 0) {
      this.noticeFileSizeToLarge();
    }
  }

  public isValidFileSize(file) {
    const size = file.size || file.sizeBytes;
    return size / 1024 / 1024 < 50; // restrict file less than 50MB
  }

  public validateFile(name): boolean {
    if (!name) {
      return false;
    }
    const extensionFile = getExtensionFileByName(name);
    return UPLOAD_FILE_SUPPORT.indexOf('.'.concat(extensionFile)) !== -1;
  }

  public noticeFileSizeToLarge() {
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
              Object.assign(file, { id: item.id, checked: false, document: 'drive', selected: true });
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

  beforeDestroy(): void {
    this.googleSignout();
  }

  public googleSignout() {
    access_token = null;
    // @ts-ignore
    google.accounts.oauth2.revoke(access_token);
  }

  selectAll() {
    if (this.selectedItems.length === this.uploadQueueFiles.length) this.selectedItems = [];
    else this.selectedItems = this.uploadQueueFiles;
  }

  setSelectedItems(newData) {
    console.log('a');
    this.selectedItems = newData;
    this.totalSelected = this.selectedItems.length;
    this.selectedItems.length === this.uploadQueueFiles.length ? $('#btnSelect').addClass('active') : $('#btnSelect').removeClass('active');
  }

  checkDisableButton() {
    this.disableButton = true;
    this.countActive = 0;
    const check = this.uploadQueueFiles.filter(element => element.selected);
    if (check && check.length > 0) {
      this.disableButton = false;
      this.countActive = check.length;
    }
  }

  deleteAll() {
    this.selectedItems = this.selectedItems.splice(0, 0);
    $('#btnSelect').removeClass('active');
  }
}
