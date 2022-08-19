import Vuex, { Module } from 'vuex';

export const loaderStore: Module<any, any> = {
  state: {
    isLoading: false,
  },
  mutations: {
    SET_LOADING(state, isLoading) {
      state.isLoading = isLoading;
    },
  },
};
