import { Vue, Component } from 'vue-property-decorator';
import vueCustomScrollbar from 'vue-custom-scrollbar';

@Component({
  components: {
    vueCustomScrollbar: vueCustomScrollbar,
  },
})
export default class LearningCenterAsideComponent extends Vue {
  private scrollSettings = {
    wheelPropagation: false,
  };
}
