import { ConnectomeNetworkVertex } from '@/shared/model/connectome-network-vertex.model';
import axios from 'axios';
import { Module } from 'vuex';

const baseConnectomeApiUrl = '/api/connectome';
const entityDetailsUrl = '/entity/';
const basePersonalDocumentsApiUrl = '/api/personal-documents';
const getPDList = '/getListDocuments/';
const baseFeedApiUrl = 'api/connectome-feed';
const getFeedsList = '/getListFeeds/';

const keyStoreEntityLabelSelected = 'mapNetworkEntityLabelSelected';
const keyStoreEntityDetailsSelected = 'mapNetworkEntityDetailsSelected';
const keyStoreEntityPersonalDocuments = 'mapNetworkEntityPersonalDocumentsSelected';
const keyStoreEntityFeeds = 'mapNetworkEntityFeedsSelected';
const keyStoreViewSettings = 'mapNetworkViewSettings';
const keyStoreClusterExpanded = 'mapNetworkClusterExpanded';
const keyStoreSecondaryClustersExpanded = 'mapNetworkSecondaryClustersExpanded';

export interface mapNetworkStorable {
  entityLabelSelected: string;
  entityDetailsSelected: any;
  entityPersonalDocuments: Array<any>;
  entityFeeds: Array<any>;
  isMapNetworkInitialized: boolean;
  searchResult: Array<ConnectomeNetworkVertex>;
  refreshMapCommand: number;
  centerAtSelectionCommand: number;
  learnMore: number;
  viewParameters: { scale: number; centerX: number; centerY: number };
  clusterExpanded: string;
  secondaryClustersExpanded: Array<string>;
}

export const mapNetworkStore: Module<mapNetworkStorable, any> = {
  namespaced: true,
  state: {
    entityLabelSelected: sessionStorage.getItem(keyStoreEntityLabelSelected),
    entityDetailsSelected: JSON.parse(sessionStorage.getItem(keyStoreEntityDetailsSelected)),
    entityPersonalDocuments: convertLocalStorageToArray(sessionStorage.getItem(keyStoreEntityPersonalDocuments)),
    entityFeeds: convertLocalStorageToArray(sessionStorage.getItem(keyStoreEntityFeeds)),
    clusterExpanded: sessionStorage.getItem(keyStoreClusterExpanded),
    isMapNetworkInitialized: false,
    searchResult: new Array<ConnectomeNetworkVertex>(),
    refreshMapCommand: 0,
    centerAtSelectionCommand: 0,
    learnMore: 0,
    viewParameters: JSON.parse(sessionStorage.getItem(keyStoreViewSettings)),
    secondaryClustersExpanded: convertLocalStorageToArrayString(sessionStorage.getItem(keyStoreSecondaryClustersExpanded)),
  },
  getters: {
    entityLabelSelected: state => state.entityLabelSelected,
    entityDetailsSelected: state => state.entityDetailsSelected,
    entityPersonalDocuments: state => state.entityPersonalDocuments,
    entityFeeds: state => state.entityFeeds,
    clusterExpanded: state => state.clusterExpanded,
    secondaryClustersExpanded: state => state.clusterExpanded,
    isMapInitialized: state => state.isMapNetworkInitialized,
    searchResult: state => state.searchResult,
    refreshMapCommand: state => state.refreshMapCommand,
    centerAtSelectionCommand: state => state.centerAtSelectionCommand,
    getGlobalViewSettings: state => state.viewParameters,
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
    setEntityPersonalDocumentsSelected(state, personalDocuments: any) {
      state.entityPersonalDocuments = personalDocuments;
      sessionStorage.removeItem(keyStoreEntityPersonalDocuments);
      sessionStorage.setItem(keyStoreEntityPersonalDocuments, JSON.stringify(personalDocuments));
    },
    setEntityFeedsSelected(state, feeds: any) {
      state.entityFeeds = feeds;
      sessionStorage.removeItem(keyStoreEntityFeeds);
      sessionStorage.setItem(keyStoreEntityFeeds, JSON.stringify(feeds));
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
      //console.log('newSearchResult', newSearchResult);
      if (!newSearchResult) {
        state.searchResult = new Array<ConnectomeNetworkVertex>();
        return;
      }
      state.searchResult = newSearchResult.map(x => x);
    },
    setViewParameters(state, settings: { scale: number; centerX: number; centerY: number }) {
      if (!settings) {
        state.viewParameters = null;
        return;
      }

      state.viewParameters = settings;
      sessionStorage.removeItem(keyStoreViewSettings);
      sessionStorage.setItem(keyStoreViewSettings, JSON.stringify(settings));
    },
    setClusterExpanded(state, label: string) {
      state.clusterExpanded = label;
      sessionStorage.removeItem(keyStoreClusterExpanded);
      sessionStorage.setItem(keyStoreClusterExpanded, label);
    },
    setSecondaryClustersExpanded(state, labels: Array<string>) {
      state.secondaryClustersExpanded = labels;
      sessionStorage.removeItem(keyStoreSecondaryClustersExpanded);
      sessionStorage.setItem(keyStoreSecondaryClustersExpanded, JSON.stringify(labels));
    },
    AddSecondaryClusterExpanded(state, label: string) {
      state.secondaryClustersExpanded.push(label);
      sessionStorage.removeItem(keyStoreSecondaryClustersExpanded);
      sessionStorage.setItem(keyStoreSecondaryClustersExpanded, JSON.stringify(state.secondaryClustersExpanded));
    },
  },
  actions: {
    setSelectedEntity(context, payload: { title: string; srcLang: string }) {
      context.commit('setEntityLabelSelected', payload.title);
    },
    externalSetSelectedEntityOnMainConnectome(context, payload: { title: string; srcLang: string }) {
      context.dispatch('setSelectedEntity', payload);
    },
    getEntityDetails: async (context, payload: { title: string; srcLang: string }) => {
      const apiCallDataStatus = new Promise<any>((resolve, reject) => {
        axios
          .get(baseConnectomeApiUrl + entityDetailsUrl + payload.srcLang + '/details?label=' + encodeURIComponent(payload.title))
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
    getPersonalDocumentByEntityLabel: async (context, payload: { entityLabel: string; connectomeId: string }) => {
      if (!payload.entityLabel && !payload.connectomeId) {
        return;
      }

      const params = {
        page: 0,
        size: 5,
        uploadType: '',
        isDelete: 0,
        entityLabel: payload.entityLabel,
      };

      const apiCallGetPersonalDocuments = new Promise<any>((resolve, reject) => {
        axios
          .post(basePersonalDocumentsApiUrl + getPDList + payload.connectomeId, [], {
            params,
          })
          .then(res => resolve(res))
          .catch(err => reject(err));
      });

      return apiCallGetPersonalDocuments
        .then(res => {
          //console.log(res);
          if (!res.data) {
            return res;
          }

          // if (!res.data.results) {
          //   context.commit('setEntityPersonalDocumentsSelected', []);
          //   return res;
          // }

          // if (!res.data.totalItems) {
          //   context.commit('setEntityPersonalDocumentsSelected', []);
          //   return res;
          // }

          // if (res.data.totalItems == 0) {
          //   context.commit('setEntityPersonalDocumentsSelected', []);
          //   return res;
          // }
          // context.commit('setEntityPersonalDocumentsSelected', res.data.results);
          return res;
        })
        .catch(reason => {
          console.log('Reason', reason);
        })
        .finally(() => {});
    },
    getFeedsByEntityLabel: async (context, payload: { entityLabel: string; connectomeId: string }) => {
      if (!payload) {
        return;
      }

      if (!payload.entityLabel && !payload.connectomeId) {
        return;
      }

      const params = {
        page: 0,
        size: 5,
        orderBy: '',
        sortDirection: 'desc',
        topic: '',
        excepted: false,
        keyword: payload.entityLabel,
      };
      const apiCallGetFeeds = new Promise<any>((resolve, reject) => {
        axios
          .post(baseFeedApiUrl + getFeedsList + payload.connectomeId, [], { params: params })
          .then(res => resolve(res))
          .catch(err => reject(err));
      });

      return apiCallGetFeeds
        .then(res => {
          //console.log(res);
          if (!res.data) {
            return res;
          }

          // if (!res.data.results) {
          //   context.commit('setEntityFeedsSelected', []);
          //   return res;
          // }

          // if (!res.data.totalItems) {
          //   context.commit('setEntityFeedsSelected', []);
          //   return res;
          // }

          // if (res.data.totalItems == 0) {
          //   context.commit('setEntityFeedsSelected', []);
          //   return res;
          // }
          // context.commit('setEntityFeedsSelected', res.data.results);
          return res;
        })
        .catch(reason => {
          console.log('Reason', reason);
        })
        .finally(() => {});
    },
    getDocsByEntityLabel: async (context, payload: { connectomeId: string; language: string; label: string }) => {
      let language = 'en';

      if (!payload.label) {
        return;
      }

      if (payload.language) {
        switch (payload.language.toLowerCase()) {
          case 'en':
          case 'english':
          case 'uk':
          case 'us':
            language = 'en';
            break;
          case 'kr':
          case 'ko':
          case 'korea':
          case 'korean':
          case 'south korea':
          case '한국어':
            language = 'ko';
            break;
          default:
            language = 'en';
        }
      }

      const apiCallConnectomeData = new Promise<any>((resolve, reject) => {
        axios
          .get(
            baseConnectomeApiUrl +
              entityDetailsUrl +
              payload.connectomeId +
              '/docs?label=' +
              encodeURIComponent(payload.label) +
              '&sourceLang=' +
              language
          )
          .then(res => resolve(res))
          .catch(err => reject(err));
      });

      return apiCallConnectomeData
        .then(res => {
          console.log('getDocsByEntityLabel', res);
          if (!res) {
            context.commit('setEntityFeedsSelected', []);
            context.commit('setEntityPersonalDocumentsSelected', []);
            return res;
          }

          if (!res.data) {
            context.commit('setEntityFeedsSelected', []);
            context.commit('setEntityPersonalDocumentsSelected', []);
            return res;
          }

          if (!res.data.body) {
            context.commit('setEntityFeedsSelected', []);
            context.commit('setEntityPersonalDocumentsSelected', []);
            return res;
          }

          const response = res.data.body;

          if (response.feeds) {
            context.commit('setEntityFeedsSelected', response.feeds);
          }

          if (response.personalDocuments) {
            context.commit('setEntityPersonalDocumentsSelected', response.personalDocuments);
          }

          return res;
          // if (!res.data.results) {
          //   context.commit('setEntityPersonalDocumentsSelected', []);
          //   return res;
          // }

          // if (!res.data.totalItems) {
          //   context.commit('setEntityPersonalDocumentsSelected', []);
          //   return res;
          // }

          // if (res.data.totalItems == 0) {
          //   context.commit('setEntityPersonalDocumentsSelected', []);
          //   return res;
          // }
          // context.commit('setEntityPersonalDocumentsSelected', res.data.results);
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
    externalClearSelectedEntityOnMainConnectome(context) {
      context.dispatch('clearSelectedEntity');
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
    saveGlobalViewParameters(context, payload: { scale: number; centerX: number; centerY: number }) {
      if (!payload) {
        return;
      }
      if (!payload.scale) {
        return;
      }
      if (!payload.centerX) {
        return;
      }
      if (!payload.centerY) {
        return;
      }

      context.commit('setViewParameters', payload);
    },
    expandedThisCluster(context, label: string) {
      if (!label) {
        return;
      }

      context.commit('setClusterExpanded', label);
    },
    // expandedThisCluster(context, label: string) {
    //   if (!label) {
    //     return;
    //   }

    //   context.commit('setClusterExpanded', label);
    // },
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

function convertLocalStorageToArrayString(obj: any) {
  if (!obj) {
    return new Array<string>();
  }

  const map = JSON.parse(obj);
  const list = new Array<string>();
  for (let i = 0; i < map.length; i++) {
    list.push(map[i]);
  }
  if (!map) {
    return new Array<string>();
  }

  return list;
}
