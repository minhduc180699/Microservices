import Vue from 'vue';
import Component from 'vue-class-component';
import { Prop } from 'vue-property-decorator';
import vuescroll from 'vuescroll';

@Component({
  components: {
    'vue-scroll': vuescroll,
  },
})
export default class MetaSearchDetailComponent extends Vue {
  @Prop() metaSearchItem;

  public ops = {
    vuescroll: {
      mode: 'native',
      sizeStrategy: 'percent',
      detectResize: true,
      /** Enable locking to the main axis if user moves only slightly on one of them at start */
      locking: true,
    },
    scrollPanel: {
      initialScrollY: false,
      initialScrollX: false,
      // scrollingX: true,
      scrollingY: true,
      speed: 300,
      easing: undefined,
      verticalNativeBarPos: 'right',
    },
    // maxHeight: 300,
    // minHeight: 540,
    rail: {},
    bar: {
      showDelay: 500,
      onlyShowBarOnScroll: true,
      keepShow: true,
      background: '#c1c1c1',
      opacity: 1,
      hoverStyle: false,
      specifyBorderRadius: false,
      minSize: 0,
      size: '6px',
      disable: false,
    },
    scrollButton: {},
  };
}
