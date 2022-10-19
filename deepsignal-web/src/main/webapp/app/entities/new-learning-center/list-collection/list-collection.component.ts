import { Component, Vue } from 'vue-property-decorator';
import vueCustomScrollbar from 'vue-custom-scrollbar';

@Component({
  components: {
    vueCustomScrollbar: vueCustomScrollbar,
  },
})
export default class ListCollection extends Vue {
  private scrollSettings = {
    wheelPropagation: false,
  };
  private isGroupCollectionActive = false;
  private isAddCollectionActive = false;
  private isShowAllTag = false;
}
