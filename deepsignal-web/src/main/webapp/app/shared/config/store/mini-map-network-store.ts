import { ConnectomeNetworkVertex } from '@/shared/model/connectome-network-vertex.model';
import axios from 'axios';
import { Module } from 'vuex';

const baseConnectomeApiUrl = '/api/connectome';
const entityDetailsUrl = '/entity/';
const basePersonalDocumentsApiUrl = '/api/personal-documents';
const getPDList = '/getListDocuments/';
const baseFeedApiUrl = 'api/connectome-feed';
const getFeedsList = '/getListFeeds/';

const keyStoreEntityLabelSelected = 'miniMapNetworkEntityLabelSelected';
const keyStoreEntityDetailsSelected = 'miniMapNetworkEntityDetailsSelected';
const keyStoreViewSettings = 'miniMapNetworkViewSettings';
const keyStoreClusterExpanded = 'miniMapNetworkClusterExpanded';
const keyStoreSecondaryClustersExpanded = 'miniMapNetworkSecondaryClustersExpanded';
export interface miniMapNetworkStorable {
  entityLabelSelected: string;
  entityDetailsSelected: any;
  isMapNetworkInitialized: boolean;
  searchResult: Array<ConnectomeNetworkVertex>;
  refreshMapCommand: number;
  centerAtSelectionCommand: number;
  learnMore: number;
}

export const miniMapNetworkStore: Module<miniMapNetworkStorable, any> = {
  namespaced: true,
  state: {
    entityLabelSelected: sessionStorage.getItem(keyStoreEntityLabelSelected),
    entityDetailsSelected: JSON.parse(sessionStorage.getItem(keyStoreEntityDetailsSelected)),
    isMapNetworkInitialized: false,
    searchResult: new Array<ConnectomeNetworkVertex>(),
    refreshMapCommand: 0,
    centerAtSelectionCommand: 0,
    learnMore: 0,
  },
  getters: {
    entityLabelSelected: state => state.entityLabelSelected,
    entityDetailsSelected: state => state.entityDetailsSelected,
    isMapInitialized: state => state.isMapNetworkInitialized,
    searchResult: state => state.searchResult,
    refreshMapCommand: state => state.refreshMapCommand,
    centerAtSelectionCommand: state => state.centerAtSelectionCommand,
  },
  mutations: {
    setEntityLabelSelected(state, label: string) {
      state.entityLabelSelected = label;
      sessionStorage.removeItem(keyStoreEntityLabelSelected);
      sessionStorage.setItem(keyStoreEntityLabelSelected, label);
    },
    setEntityDetailsSelected(state, details: any) {
      state.entityDetailsSelected = details;
      sessionStorage.removeItem(keyStoreEntityDetailsSelected);
      sessionStorage.setItem(keyStoreEntityDetailsSelected, JSON.stringify(details));
    },
    clearEntityLabelSelected(state) {
      if (sessionStorage.getItem(keyStoreEntityLabelSelected)) {
        sessionStorage.removeItem(keyStoreEntityLabelSelected);
      }
      if (sessionStorage.getItem(keyStoreEntityDetailsSelected)) {
        sessionStorage.removeItem(keyStoreEntityDetailsSelected);
      }
      state.entityLabelSelected = null;
      state.entityDetailsSelected = null;
    },
    clearEntityDetailsSelected(state) {
      if (sessionStorage.getItem(keyStoreEntityDetailsSelected)) {
        sessionStorage.removeItem(keyStoreEntityDetailsSelected);
      }
      state.entityDetailsSelected = null;
    },
    requestRefreshMap(state) {
      state.refreshMapCommand++;
    },
    requestCenterAtSelection(state) {
      state.centerAtSelectionCommand++;
    },
    requestLearnMore(state) {
      state.learnMore++;
    },
    setMapInitializedStatus(state, isInit: boolean) {
      state.isMapNetworkInitialized = isInit;
    },
    setSearchResult(state, newSearchResult: Array<ConnectomeNetworkVertex>) {
      console.log('newSearchResult', newSearchResult);
      if (!newSearchResult) {
        state.searchResult = new Array<ConnectomeNetworkVertex>();
        return;
      }
      state.searchResult = newSearchResult.map(x => x);
    },
  },
  actions: {
    setSelectedEntity(context, payload: { title: string; srcLang: string }) {
      context.commit('setEntityLabelSelected', payload.title);
    },
    getEntityDetails: async (context, payload: { title: string; srcLang: string }) => {
      const apiCallDataStatus = new Promise<any>((resolve, reject) => {
        axios
          .get(baseConnectomeApiUrl + entityDetailsUrl + payload.srcLang + '/details?label=' + payload.title)
          .then(res => resolve(res))
          .catch(err => reject(err));
      });

      return apiCallDataStatus
        .then(res => {
          if (!res.data) {
            return;
          }

          const response = res.data;

          context.commit('setEntityDetailsSelected', response[0]);
          return res;
        })
        .catch(reason => {
          console.log('Reason', reason);
        })
        .finally(() => {});
    },
    clearSelectedEntity(context) {
      context.commit('clearEntityLabelSelected');
      context.commit('clearEntityDetailsSelected');
    },
    clearDetails(context) {
      context.commit('clearEntityDetailsSelected');
    },
    logout(context) {
      context.dispatch('clearSelectedEntity');
    },
    refreshMap(context) {
      context.commit('requestRefreshMap');
    },
    requestCenterAtSelection(context) {
      context.commit('requestCenterAtSelection');
    },
    reuestLearnMore(context) {
      context.commit('requestLearnMore');
    },
    setMapInitialized(context) {
      context.commit('setMapInitializedStatus', true);
    },
    setNewSearchResult(context, payload: { searchResult: Array<ConnectomeNetworkVertex> }) {
      if (!payload) {
        context.commit('setSearchResult', null);
      }
      if (!payload.searchResult) {
        context.commit('setSearchResult', null);
      }

      context.commit('setSearchResult', payload.searchResult);
    },
  },
};

function convertLocalStorageToArray(obj: any) {
  if (!obj) {
    return new Array<any>();
  }

  const map = JSON.parse(obj);
  if (!map) {
    return new Array<any>();
  }

  return map;
}
