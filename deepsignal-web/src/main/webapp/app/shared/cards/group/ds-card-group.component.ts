import { Component, Vue, Prop } from 'vue-property-decorator';
import DsCardFooter from '@/shared/cards/footer/ds-card-footer.vue';
import { CardModel, ImagesInfo } from '@/shared/cards/card.model';

@Component({
  components: {
    'ds-card-footer': DsCardFooter,
  },
})
export default class DsCardGroup extends Vue {
  @Prop(Object) readonly item: any | undefined;
  imagesSorted: ImagesInfo[];

  data() {
    this.sortImageBeforeRender();
    return {
      images: this.imagesSorted,
    };
  }

  dateFormat() {}

  sortImageBeforeRender() {
    if (!this.item || !this.item.subImages) {
      return;
    }
    this.imagesSorted = this.item.subImages.sort((a: ImagesInfo, b: ImagesInfo) => {
      return b.width - a.width && b.height - a.height;
    });
  }
}
