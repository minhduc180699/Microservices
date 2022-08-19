import { Component, Inject, Vue } from 'vue-property-decorator';
import DocumentListWrapComponent from '@/entities/my-ai/learning-center/document/document-list-wrap/document-list-wrap.vue';
import { FileStorageService } from '@/service/file-storage.service';
import { PrincipalService } from '@/service/principal.service';

@Component({
  components: {
    'doc-wrap-list': DocumentListWrapComponent,
  },
})
export default class DocumentCompleteListComponent extends Vue {
  @Inject('fileStorageService') private fileStorageService: () => FileStorageService;
  @Inject('principalService') private principalService: () => PrincipalService;
  pageable = {
    page: 1,
    size: 10,
    sort: ['createdDate,desc'],
  };
  docLists = [];
  loading = false;
  private parent = 'doc-list';

  mounted(): void {
    this.getAllDoc();
  }

  getAllDoc() {
    const userId = this.principalService().getUserInfo().id || '';
    if (!userId) {
      return;
    }
    this.loading = true;
    this.fileStorageService()
      .getAllDoc(userId, this.pageable)
      .then(res => {
        this.docLists = res.data.content;
        this.loading = false;
      })
      .catch(() => (this.loading = false));
  }

  removeFile(e) {
    if (!e) {
      return;
    }
    this.fileStorageService()
      .deleteDocById(e.id)
      .then(() => this.getAllDoc());
  }

  downloadFile(e) {
    if (!e) {
      return;
    }
    this.fileStorageService()
      .downloadDocById(e.id)
      .then(res => {
        const url = window.URL.createObjectURL(new Blob([res.data]));
        const link = document.createElement('a');
        link.href = url;
        link.setAttribute('download', e.name);
        document.body.appendChild(link);
        link.click();
      })
      .catch(err => {
        this.$bvToast.toast('Download fail! File not found', {
          toaster: 'b-toaster-bottom-right',
          title: 'Fail',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
      });
  }
}
