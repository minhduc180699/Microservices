import Vuex from 'vuex';
import Vue from 'vue';
import { CategoryModel } from '@/shared/model/category.model';
import FaqService from '@/entities/help/faq/faq.service';
import { Inject } from 'vue-property-decorator';
import { FaqModel } from '@/shared/model/faq.model';

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    faqs: [],
    keySearch: null,
    totalFaqSearch: null,
    typeSearch: null,
    category: '',
    pageSize: 2,
  },

  mutations: {
    setFaqs(state, faqModel) {
      state.faqs = faqModel;
    },

    setKeySearch(state, key) {
      state.keySearch = key;
    },

    setTotalFaqSearch(state, total) {
      state.totalFaqSearch = total;
    },

    setTypeSearch(state, type) {
      state.typeSearch = type;
    },

    setCategory(state, cate) {
      state.category = cate;
    },

    setPageSize(state, size) {
      state.pageSize += size;
    },

    emptyFaqs(state) {
      state.faqs = [];
    },

    initialPageSize(state) {
      state.pageSize = 2;
    },
  },

  actions: {
    searchCategory(context, { pageable, category }) {
      const faqs = [];
      // console.log('page size: ', pageable.size);
      FaqService.findByCategory(pageable, category).then(res => {
        // console.log('find by category: ', res);
        res.data['content'].forEach(item => {
          const faq = new FaqModel(item, false);
          faqs.push(faq);
        });
        context.commit('setFaqs', faqs);
      });
    },

    searchKeyWord(context, { pageable, keyWord, category }) {
      const faqs = [];
      FaqService.findByKeyWord(pageable, keyWord, category).then(res => {
        // console.log('search key word result: ', res);
        context.commit('setTotalFaqSearch', res.data['totalElements']);
        res.data['content'].forEach(item => {
          const faq = new FaqModel(item, false);
          faqs.push(faq);
        });
        context.commit('setFaqs', faqs);
      });
    },
  },
});
