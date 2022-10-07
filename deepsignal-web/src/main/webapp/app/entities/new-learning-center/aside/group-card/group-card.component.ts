import { Component, Inject, Prop, Vue } from 'vue-property-decorator';
import { asideService } from '@/entities/new-learning-center/aside/aside-service/aside.service';
import listCardGroup from '@/entities/new-learning-center/aside/list-card-group/list-card-group.vue';

@Component({
  components: {
    listCardGroup: listCardGroup,
  },
})
export default class groupCard extends Vue {
  @Prop(Object) readonly item: any;
  @Inject('asideService')
  private asideService: () => asideService;
  private dataDocuments = [];
  private images = [];
  private loading = true;

  created() {
    this.asideService()
      .getByDocId(this.item.documentIdList)
      .then(res => {
        this.dataDocuments = res.data.connectomePersonalDocuments;
        this.dataDocuments.forEach((item, index) => {
          if (item.imageUrl && item.imageUrl.length > 0) {
            this.images.push(item.imageUrl[0]);
          } else if (item.imageBase64 && item.imageBase64.length > 0) {
            this.images.push(item.imageBase64[0]);
          } else if (item.ogImageUrl && item.ogImageUrl != '') {
            this.images.push(item.ogImageUrl);
          } else if (item.ogImageBase64 && item.ogImageBase64 != '') {
            this.images.push(item.ogImageBase64);
          }
        });
        let i = 0;
        while (this.images.length < 4) {
          this.images.push(this.images[i]);
          i++;
        }
        this.loading = false;
      })
      .catch(error => {
        this.loading = false;
      });
  }

  showListCard() {
    this.$emit('showListGroupCard', this.dataDocuments);
  }
}
