import { Component, Vue } from 'vue-property-decorator';
import { namespace } from 'vuex-class';

const myAiViewState = namespace('myAIViewStateStore');
@Component
export default class mainAside extends Vue {
  private tabs = [
    // { name: 'Persona', link: '/my-ai/learning-center', icon: 'icon-persona', value: 1, disable: true },
    {
      name: this.$t('my-ai-side-bar.tooltip.connectome'),
      link: '/my-ai/connectome/2dnetwork',
      icon: 'icon-connectome',
      value: 2,
      disable: false,
    },
    {
      name: this.$t('my-ai-side-bar.tooltip.learning-center'),
      link: '/my-ai/learning-center',
      icon: 'icon-learning',
      value: 3,
      disable: false,
    },
    // { name: 'Statistics', link: '/my-ai/learning-center', icon: 'icon-stats', value: 4, disable: true },
  ];

  @myAiViewState.Getter
  public mainVerticalTabIndex!: number;

  @myAiViewState.Action
  public setMainVerticalTabIndex!: (index: number) => void;

  changeTab(index, disable) {
    if (!disable) {
      this.setMainVerticalTabIndex(index);
    }
  }
}
