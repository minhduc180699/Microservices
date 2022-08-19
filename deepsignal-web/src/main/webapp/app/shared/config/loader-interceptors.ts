import axios, { AxiosRequestConfig } from 'axios';
import { Store } from 'vuex';
import { EXCLUDE_API_LOADER } from '@/shared/constants/ds-constants';

let numOfRequest = 0;
let appStore: Store<any>;

const onCallRequest = config => {
  const url = getUriDeepSignalApi(config);
  if (checkApiLoader(url)) {
    numOfRequest++;
    if (appStore) {
      appStore.commit('SET_LOADING', true);
    }
  }
  return config;
};

function checkApiLoader(url) {
  if (!url) {
    return;
  }
  return EXCLUDE_API_LOADER.findIndex(item => url.toLowerCase().includes(item.toLocaleLowerCase())) === -1;
}

function getUriDeepSignalApi(config: AxiosRequestConfig) {
  if (!config) {
    return;
  }
  return axios.getUri(config);
}

function hideLoader(store: Store<any>) {
  if (numOfRequest === 0) {
    // all request is done
    store.commit('SET_LOADING', false);
  }
}

const axiosRequestLoader = (store: Store<any>) => {
  appStore = store;
  const onResponseError = err => {
    const url = getUriDeepSignalApi(err.config);
    if (checkApiLoader(url)) {
      numOfRequest--;
      hideLoader(store);
    }
    return Promise.reject(err);
  };
  const onResponseSuccess = res => {
    const url = getUriDeepSignalApi(res.config);
    if (checkApiLoader(url)) {
      numOfRequest--;
      hideLoader(store);
    }
    return Promise.resolve(res);
  };
  if (axios.interceptors) {
    axios.interceptors.request.use(onCallRequest);
    axios.interceptors.response.use(onResponseSuccess, onResponseError);
  }
};

export { axiosRequestLoader };
