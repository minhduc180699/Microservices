import { Module } from 'vuex';

export const learningCenter: Module<any, any> = {
  state: {
    searchCards: [],
    urlCards: [],
    documentCards: [],
    textCards: [],
  },
  getters: {
    getSearchCards: state => state.searchCards,
    getUrlCards: state => state.urlCards,
    getDocumentCards: state => state.documentCards,
    getTextCards: state => state.textCards,
  },
  mutations: {
    setSearchCards(state, searchCards) {
      state.searchCards = searchCards;
    },
    setUrlCards(state, urlCards) {
      state.urlCards = urlCards;
    },
    setDocumentCards(state, documentCards) {
      state.documentCards = documentCards;
    },
    setTextCards(state, textCards) {
      state.textCards = textCards;
    },
  },
};
