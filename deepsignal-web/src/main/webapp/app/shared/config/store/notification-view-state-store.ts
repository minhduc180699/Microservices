import axios from 'axios';
import { Module } from 'vuex';

export interface notificationViewStateStorable {
  type: string;
  countPressNotification: number;
}

export const notificationViewStateStore: Module<notificationViewStateStorable, any> = {
  namespaced: true,
  state: {
    type: sessionStorage.getItem('mapNetworkEntityLabelSelected'),
    countPressNotification: 0,
  },
  getters: {
    typeSelected: state => state.type,
    countPressedNotification: state => state.countPressNotification,
  },
  mutations: {
    setTypeSelected(state, type: string) {
      state.type = type;
    },
    setCountPressNotification(state, countPressNotification: number) {
      state.countPressNotification = countPressNotification;
    },
  },
  actions: {
    selectTypeNotification(context, payload: { type: string }) {
      context.commit('setTypeSelected', payload.type);
    },
    pressNotification(context, payload: { countPressNotification: number }) {
      context.commit('setCountPressNotification', payload.countPressNotification);
    },
  },
};
