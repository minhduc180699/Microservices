import { Module } from 'vuex';

export interface AppStorable {
  isLoginPage: boolean;
}

export const appStore: Module<AppStorable, any> = {
  namespaced: true,
  state: {
    isLoginPage: true,
  },
  getters: {
    isLoginPage: state => state.isLoginPage,
  },
  mutations: {
    setPageLogin(state, isLoginPage: boolean) {
      state.isLoginPage = isLoginPage;
    },
  },
  actions: {
    changePage(context, payload: { isLoginPage: boolean }) {
      context.commit('setPageLogin', payload.isLoginPage);
    },
  },
};
