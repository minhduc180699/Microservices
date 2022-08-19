import { Module } from 'vuex';
import axios from 'axios';
import { UserSettingModel } from '@/shared/cards/popup-setting/popup-user-setting.model';
import { CHART_STOCK_STYLE, TEMPERATURE_UNIT, LOCAL_STORAGE_CONSTANTS } from '@/shared/constants/ds-constants';

const UserSetting = 'ds-user-setting';

export const userSettingStore: Module<any, any> = {
  namespaced: true,
  state: {
    timeSetting: '',
    userSetting: JSON.parse(sessionStorage.getItem(UserSetting)),
  },
  getters: {
    userSetting: state => state.userSetting,
    timeSetting: state => state.timeSetting,
  },
  mutations: {
    setUserSetting(state, userSetting) {
      state.userSetting = userSetting;
      sessionStorage.removeItem(UserSetting);
      sessionStorage.setItem(UserSetting, JSON.stringify(userSetting));
    },
    setTimeSetting(state, time) {
      state.timeSetting = time;
    },
  },
  actions: {
    saveUserSetting(context, payload: { setting: any; connectomeId: string }) {
      context.commit('setTimeSetting', new Date().getTime());
      context.commit('setUserSetting', payload.setting);
      const item = {
        locWeather: '',
        stock: '',
        weather: '',
      };
      item.locWeather = payload.setting.locWeather;
      item.stock = JSON.stringify(payload.setting.stock);
      item.weather = JSON.stringify(payload.setting.weather);
      return axios.post('api/usersetting/saveSetting', item, {
        params: {
          connectomeId: payload.connectomeId,
        },
      });
    },
    setUserSetting(context, payload: { connectomeId: string }) {
      if (!sessionStorage.getItem(UserSetting)) {
        const ipClient = localStorage.getItem(LOCAL_STORAGE_CONSTANTS.IP);
        axios
          .get('api/usersetting/getByUserId', {
            params: {
              connectomeId: payload.connectomeId,
            },
          })
          .then(res => {
            if (res.data) {
              context.commit('setUserSetting', convertDate(res.data));
            } else {
              context.commit('setUserSetting', new UserSettingModel(TEMPERATURE_UNIT.C, CHART_STOCK_STYLE.CHART, ipClient));
            }
          })
          .catch(error => {
            context.commit('setUserSetting', new UserSettingModel(TEMPERATURE_UNIT.C, CHART_STOCK_STYLE.CHART, ipClient));
          });
      }
    },
  },
};

function convertDate(data) {
  const transfer = {
    locWeather: '',
    stock: Object,
    weather: Object,
  };
  transfer.locWeather = data.locWeather;
  transfer.stock = JSON.parse(data.stock);
  transfer.weather = JSON.parse(data.weather);
  return transfer;
}
