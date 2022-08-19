import Component from 'vue-class-component';
import { Emit, Inject, Prop, Vue } from 'vue-property-decorator';
import { FaqModel, IFaqModel } from '@/shared/model/faq.model';
import FaqService from '@/entities/help/faq/faq.service';
import { PageableModel } from '@/shared/model/pageable.model';
import store from '@/store';

@Component({
  name: 'faq-privacy',
})
export default class FAQPrivacy extends Vue {
  public faqModels: FaqModel[] = [];
  // public pageSize = 2;

  get faqs() {
    return store.state.faqs;
  }

  get typeSearch() {
    return store.state.typeSearch;
  }

  get pageSize() {
    return store.state.pageSize;
  }

  mounted(): void {
    this.findAllFaq(this.pageSize);
  }

  public findAllFaq(pageSize: number) {
    store.commit('setTypeSearch', 'all');

    this.faqModels = [];
    const pageable = new PageableModel();
    pageable.size = pageSize;
    FaqService.findAllFaq(pageable).then(res => {
      // console.log('res all = ', res);
      // this.pageSize = res.data['totalElements'];
      this.$emit('total-question', res.data['totalElements']);
      res.data['content'].forEach(item => {
        const faq = new FaqModel(item, false);
        this.faqModels.push(faq);
      });
      store.commit('setFaqs', this.faqModels);
    });
  }

  public loadMore() {
    store.commit('setPageSize', 2);
    const pageable = new PageableModel();
    pageable.size = this.pageSize;
    if (this.typeSearch === 'keyWord') {
      store.dispatch('searchKeyWord', { pageable: pageable, keyWord: store.state.keySearch, category: store.state.category });
    } else if (this.typeSearch === 'category') {
      store.dispatch('searchCategory', { pageable: pageable, category: store.state.category });
    } else {
      this.findAllFaq(this.pageSize);
    }
  }
}
