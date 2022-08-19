import { Component, Prop, Vue } from 'vue-property-decorator';

@Component
export default class MetaSearchSearchLearningComponent extends Vue {
  mounted(): void {
    const timeout = 10000;
    $('.progress-bar').animate(
      {
        width: '100%',
      },
      timeout
    );
    setTimeout(() => {
      // @ts-ignore
      //this.$router.push('/myconnectome/map/METAVERSE');
    }, timeout);
  }
}
