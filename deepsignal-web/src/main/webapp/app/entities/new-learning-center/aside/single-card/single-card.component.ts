import { Component, Inject, Prop, Vue } from 'vue-property-decorator';
import { asideService } from '@/entities/new-learning-center/aside/aside-service/aside.service';

@Component
export default class singleCard extends Vue {
  @Prop(Object) readonly document: any;
  @Prop(Array) readonly documentId: [] | any;
  @Inject('asideService')
  private asideService: () => asideService;
  private data = {};
  private loading = true;

  created() {
    if (this.documentId) {
      this.asideService()
        .getByDocId(this.documentId)
        .then(res => {
          this.loading = false;
          this.data = res.data.connectomePersonalDocuments[0];
        })
        .catch(error => {
          this.loading = false;
        });
    } else if (this.document) {
      this.loading = false;
      this.data = this.document;
    }
  }
}
