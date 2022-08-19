import Vuex, { Module } from 'vuex';

export const qrcodeStore: Module<any, any> = {
  state: {
    qrcode: false,
  },
  getters: {
    qrcode: state => state.qrcode,
  },
  mutations: {
    setQRcode(state, qrcode) {
      state.qrcode = qrcode;
    },
  },
};
