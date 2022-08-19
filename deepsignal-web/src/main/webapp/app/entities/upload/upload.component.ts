import axios from 'axios';
import { Component, Vue } from 'vue-property-decorator';
import { maxLength, required } from 'vuelidate/lib/validators';
const API_PATH = 'api/file-storage';

const validations: any = {
  uploadFile: {
    file: {
      required,
    },
  },
  uploadURL: {
    name: {
      required,
      maxLength: maxLength(254),
    },
    url: {
      required,
      maxLength: maxLength(255),
    },
  },
};

@Component({
  validations,
})
export default class UploadComponent extends Vue {
  public active_tab = 0;
  public uploadURL: any = {
    name: undefined,
    url: undefined,
  };

  public uploadFile: any = {
    file: undefined,
  };
  public isSuccess = false;
  public isSaving = false;

  public changeTab(tabIndexValue): void {
    this.active_tab = tabIndexValue;
  }

  public handleFileUpload(): void {
    // @ts-ignore
    this.uploadFile.file = this.$refs.file.files[0];
  }

  public submitFile(): void {
    this.isSaving = true;
    const connectome = JSON.parse(localStorage.getItem('ds-connectome'));
    const userId = connectome.user.id;
    const connectomeId = connectome.connectomeId;
    if (!connectome || !connectomeId || !userId) {
      return;
    }

    const param = userId + '/' + connectomeId;
    const formData = new FormData();

    formData.append('file', this.uploadFile.file);

    /*
      Make the request to the POST /upload URL
    */
    axios
      .post(API_PATH.concat('/upload/').concat(param), formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      })
      .then(() => {
        this.isSaving = false;
        console.log('SUCCESS!!');
        this.$bvToast.toast('Upload successful!', {
          toaster: 'b-toaster-bottom-right',
          title: 'Success',
          variant: 'success',
          solid: true,
          autoHideDelay: 5000,
        });
      })
      .catch(() => {
        console.log('FAILURE!!');
        this.isSaving = false;
        this.$bvToast.toast('Upload fail!', {
          toaster: 'b-toaster-bottom-right',
          title: 'Fail',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
      });
  }

  public submitURL(): void {
    this.isSaving = true;
    const connectome = JSON.parse(localStorage.getItem('ds-connectome'));
    const userId = connectome.user.id;
    const connectomeId = connectome.connectomeId;
    if (!connectome || !connectomeId || !userId) {
      return;
    }

    const param = userId + '/' + connectomeId;
    const formData = new FormData();

    formData.append('name', this.uploadURL.name);
    formData.append('url', this.uploadURL.url);

    /*
      Make the request to the POST /upload URL
    */
    axios
      .post(API_PATH.concat('/uploadURL/').concat(param), formData, {
        headers: {
          'Content-Type': 'application/json',
        },
      })
      .then(() => {
        console.log('SUCCESS!!');
        this.isSaving = false;
        this.$bvToast.toast('Upload successful!', {
          toaster: 'b-toaster-bottom-right',
          title: 'Success',
          variant: 'success',
          solid: true,
          autoHideDelay: 5000,
        });
      })
      .catch(() => {
        console.log('FAILURE!!');
        this.isSaving = false;
        this.$bvToast.toast('Upload fail!', {
          toaster: 'b-toaster-bottom-right',
          title: 'Fail',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
      });
  }
}
