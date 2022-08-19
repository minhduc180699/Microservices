import Vuex, { Module } from 'vuex';

export interface CardStateStorable {
  companiesOrPeoples: [];
  signalIssues: [];
  companies: [];
  peoples: [];
}

// Management data store of card
export const cardStore: Module<CardStateStorable, any> = {
  namespaced: true,
  state: {
    companiesOrPeoples: [],
    signalIssues: null,
    companies: [],
    peoples: [],
  },
  getters: {
    getCompaniesOrPeoples: state => state.companiesOrPeoples,
    signalIssues: state => state.signalIssues,
    getCompanies: state => state.companies,
    getPeoples: state => state.peoples,
  },
  mutations: {
    setCompaniesOrPeoples(state, companiesOrPeoples) {
      state.companiesOrPeoples = companiesOrPeoples;
    },
    setSignalIssues(state, signalIssues: []) {
      state.signalIssues = signalIssues;
    },
    setCompanies(state, companies) {
      state.companies = companies;
    },
    setPeoples(state, peoples) {
      state.peoples = peoples;
    },
  },
  actions: {
    changeSignalIssues(context, payload: { signalIssues: [] }) {
      context.commit('setSignalIssues', payload.signalIssues);
    },
    changeCompaniesOrPeoples(context, payload: { companiesOrPeoples: [] }) {
      context.commit('setCompaniesOrPeoples', payload.companiesOrPeoples);
    },
    changeCompanies(context, payload: { companies: [] }) {
      context.commit('setCompanies', payload.companies);
    },
    changePeoples(context, payload: { peoples: [] }) {
      context.commit('setPeoples', payload.peoples);
    },
  },
};
