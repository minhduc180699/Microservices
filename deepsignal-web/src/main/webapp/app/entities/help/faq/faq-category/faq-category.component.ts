import Component from 'vue-class-component';
import { Vue } from 'vue-property-decorator';
import FaqService from '@/entities/help/faq/faq.service';
import { CategoryModel } from '@/shared/model/category.model';
import { PageableModel } from '@/shared/model/pageable.model';
import store from '@/store';

@Component({
  name: 'faq-category',
})
export default class FAQCategory extends Vue {
  public categories: CategoryModel[] = [];
  public faqModel = '';
  public searched = false;
  // public faqModels: FaqModel[] = [];
  public currentCategory = '';
  // public pageSize = 2;

  get totalFaqSearch() {
    return store.state.totalFaqSearch;
  }

  get pageSize() {
    return store.state.pageSize;
  }

  // @Inject('faqService') private faqService: () => FaqService;

  mounted(): void {
    this.getAllCategoryFaq();
    // console.log('faq model: ', this.faqModels);
  }

  public getAllCategoryFaq() {
    FaqService.getAllCategoryFaq(1).then(res => {
      // console.log('category: ', res);
      res.data.forEach(cate => {
        const category = new CategoryModel(cate, false);
        this.categories.push(category);
      });
    });
  }

  public searchKeyWord() {
    // if (isNullOrEmpty(this.faqModel)) {
    //   return;
    // }
    store.commit('initialPageSize');
    store.commit('setTypeSearch', 'keyWord');
    store.commit('setKeySearch', this.faqModel);
    // this.faqModels = [];
    const pageable = new PageableModel();
    pageable.size = this.pageSize;
    // console.log('key word: ', this.faqModel);
    // FaqService.findByKeyWord(pageable, this.faqModel, this.currentCategory).then(res => {
    //   console.log('search key word result: ', res);
    //   this.totalFaqSearch = res.data['totalElements'];
    //   res.data['content'].forEach(item => {
    //     let faq = new FaqModel(item, false);
    //     this.faqModels.push(faq);
    //   });
    //   store.commit('setFaqs', this.faqModels);
    // });
    store.dispatch('searchKeyWord', { pageable: pageable, keyWord: this.faqModel, category: this.currentCategory });

    this.searched = true;
  }

  public searchCategory(cate: CategoryModel) {
    // console.log('faq model in cate: ', this.faqModels);
    // this.faqModels = [];
    this.categories.forEach(cateModel => (cateModel.isActive = false));
    cate.isActive = true;

    store.commit('initialPageSize');
    store.commit('setTypeSearch', 'category');
    store.commit('setCategory', cate.category.code);

    this.currentCategory = cate.category.code;
    const pageable = new PageableModel();
    pageable.size = this.pageSize;
    // console.log('key word: ', this.faqModel);
    // FaqService.findByCategory(pageable, cate.category.code).then(res => {
    //   // console.log('find by category: ', res);
    //   res.data['content'].forEach(item => {
    //     let faq = new FaqModel(item, false);
    //     this.faqModels.push(faq);
    //   });
    //   store.commit('setFaqs', this.faqModels);
    // });
    store.dispatch('searchCategory', { pageable: pageable, category: this.currentCategory });
  }
}
