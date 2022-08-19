import axios from 'axios';
import { Module } from 'vuex';

export interface myAIViewStateStorable {
  mainVerticalTabIndex: number;
}
export const myAIViewStateStore: Module<myAIViewStateStorable, any> = {
  namespaced: true,
  state: {
    mainVerticalTabIndex: Number.parseInt(
      sessionStorage.getItem('myAIViewStateStore_mainVerticalTabIndex')
        ? sessionStorage.getItem('myAIViewStateStore_mainVerticalTabIndex')
        : '3'
    ),
  },
  getters: {
    mainVerticalTabIndex: state => state.mainVerticalTabIndex,
  },
  mutations: {
    setMainVerticalTabIndex(state, index: number) {
      state.mainVerticalTabIndex = index;
      sessionStorage.removeItem('myAIViewStateStore_mainVerticalTabIndex');
      sessionStorage.setItem('myAIViewStateStore_mainVerticalTabIndex', index.toString());
    },
    clearData(state) {
      sessionStorage.removeItem('myAIViewStateStore_mainVerticalTabIndex');
    },
  },
  actions: {
    setMainVerticalTabIndex(context, index: number) {
      context.commit('setMainVerticalTabIndex', index);
    },
    logout(context) {
      context.dispatch('clearData');
    },
  },
};
