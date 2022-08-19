import Vuex, { Module } from 'vuex';

export const nodeStore: Module<any, any> = {
  state: {
    nodeName: '',
  },
  getters: {
    nodeName: state => state.nodeName,
  },
  mutations: {
    setNode(state, nodeName) {
      state.nodeName = nodeName;
    },
  },
};
