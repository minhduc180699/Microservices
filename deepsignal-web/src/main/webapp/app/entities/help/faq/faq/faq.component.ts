import Component from 'vue-class-component';
import { Vue, Inject } from 'vue-property-decorator';
import FAQCategory from '@/entities/help/faq/faq-category/faq-category.vue';

@Component({
  components: {
    'faq-category': FAQCategory,
  },
})
export default class FrequentlyAskedQuestions extends Vue {
  public totalQuestion = null;
  public tabs = [
    { name: 'Privacy', value: 'privacy' },
    { name: 'Login', value: 'login' },
    { name: 'Join And Unsubscribe', value: 'join' },
  ];
  public currentTab = this.tabs[0].value;

  mounted(): void {
    {
      const paths = this.$route.path.split('/');
      this.currentTab = paths[2] || this.tabs[0].value;
    }
  }

  updateTotalQuestion(totalQuestion: number) {
    // console.log('total question = ', totalQuestion);
    this.totalQuestion = totalQuestion;
  }
}
