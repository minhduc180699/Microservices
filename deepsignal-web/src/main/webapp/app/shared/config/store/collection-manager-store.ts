import { ConnectomeNetwork } from '@/shared/model/connectome-network.model';

import { ConnectomeLink } from '@/shared/model/connectome-link.model';
import { ConnectomeNode } from '@/shared/model/connectome-node.model';
import axios from 'axios';
import { Module } from 'vuex';
import { ContextualMemoryCollection } from '@/shared/model/contextual-memory-collection.model';

const baseConnectomeApiUrl = '/api/connectome';
const getCollectionsURL = baseConnectomeApiUrl + '/collection/get/all';
const getCollectionURL = baseConnectomeApiUrl + '/collection/get';
const postCreateCollectionURL = baseConnectomeApiUrl + '/collection/create';
const putCollectionURL = baseConnectomeApiUrl + '/collection/update';

const basePersonalDocumentsApiUrl = '/api/personal-documents';
const getPersonalDocumentsByDocIdsUrl = basePersonalDocumentsApiUrl + '/getByDocIds';

const getTextDataUrl = baseConnectomeApiUrl + '/text-map/';

const keyStorePdList = 'ConnectomeBuilderPdIds';
const keyStoreStorageList = 'ConnectomeBuilderStorageList';
const keyStoreNodesPrefix = 'ConnectomeBuilderNodes_';

const circleColors = [
  '#8600b0',
  '#6bc600',
  '#002aba',
  '#ffe022',
  '#0081fa',
  '#00db83',
  '#a40075',
  '#008d2c',
  '#ff4e92',
  '#cbffa8',
  '#0a003e',
  '#ffa54c',
  '#004089',
  '#8b7400',
  '#31a7ff',
  '#975b00',
  '#01b5df',
  '#ff775f',
  '#95f6ff',
  '#5e000d',
  '#e8ffff',
  '#440035',
  '#008d78',
  '#ff8cd3',
  '#1e3f00',
  '#ffbffa',
  '#051b00',
  '#a9baff',
  '#001127',
  '#008285',
];

const defaultLangKey = 'en';
const defaultNoColor = '#DEDEDE';

export interface CollectionManagerStorable {
  indexSessionStorage: Map<string, string>;
  collections: Array<ContextualMemoryCollection>;
  currentCollection: ContextualMemoryCollection;
  currentDocumentSet: Array<string>;
  dataUpdated: number;
}

export const collectionManagerStore: Module<CollectionManagerStorable, any> = {
  namespaced: true,
  state: {
    indexSessionStorage: new Map<string, string>(),
    collections: new Array<ContextualMemoryCollection>(),
    currentCollection: new ContextualMemoryCollection(null),
    currentDocumentSet: new Array<string>(),
    dataUpdated: 0,
  },
  getters: {
    getIndexSessionStorage: state => state.indexSessionStorage,
    getCollections: state => state.collections,
    getCurrentCollection: state => state.currentCollection,
    getCurrentDocumentSet: state => state.currentDocumentSet,
    isUpdated: state => state.dataUpdated,
  },
  mutations: {
    setCollections(state, payload: { collections: Array<ContextualMemoryCollection> }) {
      if (!payload.collections) {
        return;
      }
      state.collections = payload.collections.map(x => x);
      state.dataUpdated++;
    },
    setCurrentCollection(state, payload: { collection: ContextualMemoryCollection }) {
      if (!payload.collection) {
        return;
      }

      state.currentCollection = new ContextualMemoryCollection(payload.collection);
      state.dataUpdated++;
    },
    setCurrentDocumentSet(state, payload: { docSet: Array<string> }) {
      if (!payload.docSet) {
        state.currentDocumentSet = new Array<string>();
        state.dataUpdated++;
        return;
      }
      if (payload.docSet.length == 0) {
        state.currentDocumentSet = new Array<string>();
        state.dataUpdated++;
        return;
      }
      state.currentDocumentSet = payload.docSet.map(x => x);
      state.dataUpdated++;
    },
    resetData(state) {},
  },
  actions: {
    loadUserCollections: async (
      context,
      payload: {
        connectomeId: string;
        language: string;
      }
    ) => {
      let language = 'en';
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
      if (!payload.connectomeId) {
        return;
      }

      axios.defaults.headers.common['connectomeId'] = payload.connectomeId;
      axios.defaults.headers.common['lang'] = language;
      const apiCallConnectomeData = new Promise<any>((resolve, reject) => {
        axios
          .get(getCollectionsURL)
          .then(res => resolve(res))
          .catch(err => reject(err));
      });

      return apiCallConnectomeData
        .then(res => {
          if (!res.data.body) {
            return;
          }
          const response = res.data.body;
          console.log('loadUserCollections', response);

          const responseCollections = response.map(x => new ContextualMemoryCollection(x));
          context.commit('setCollections', { collections: responseCollections });

          return response;
        })
        .catch(reason => {
          console.log('Reason', reason);
        })
        .finally(() => {});
    },
    loadCurrentCollection: async (
      context,
      payload: {
        collectionId: string;
      }
    ) => {
      if (!payload.collectionId) {
        return;
      }

      const apiCallConnectomeData = new Promise<any>((resolve, reject) => {
        axios
          .get(getCollectionURL, { params: { collectionId: payload.collectionId } })
          .then(res => resolve(res))
          .catch(err => reject(err));
      });

      return apiCallConnectomeData
        .then(res => {
          if (!res.data.body) {
            return;
          }

          if (res.data.body.length == 0) {
            return;
          }
          const response = res.data.body[0];
          context.commit('setCurrentCollection', { collection: response });

          return response;
        })
        .catch(reason => {
          console.log('Reason', reason);
        })
        .finally(() => {});
    },
    loadDocumentsFromCollection: async (
      context,
      payload: {
        docIds: Array<string>;
      }
    ) => {
      if (!payload.docIds) {
        return;
      }

      if (payload.docIds.length == 0) {
        return;
      }

      const apiCallConnectomeData = new Promise<any>((resolve, reject) => {
        axios
          .post(getPersonalDocumentsByDocIdsUrl, payload.docIds)
          .then(res => resolve(res))
          .catch(err => reject(err));
      });

      return apiCallConnectomeData
        .then(res => {
          console.log('loadDocumentsFromCollection', res);
          if (!res) {
            return;
          }

          if (!res.data) {
            return;
          }
          const response = res.data.connectomePersonalDocuments;
          console.log('loadDocumentsFromCollection', response);

          return response;
        })
        .catch(reason => {
          console.log('Reason', reason);
        })
        .finally(() => {});
    },
    createCollection: async (
      context,
      payload: {
        connectomeId: string;
        language: string;
      }
    ) => {
      let language = 'en';
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
      const newCollection = new ContextualMemoryCollection(null);
      newCollection.connectomeId = payload.connectomeId;
      newCollection.lang = language;
      const apiCallConnectomeData = new Promise<any>((resolve, reject) => {
        axios
          .post(postCreateCollectionURL, newCollection)
          .then(res => resolve(res))
          .catch(err => reject(err));
      });

      return apiCallConnectomeData
        .then(res => {
          if (!res.data.body) {
            return;
          }
          const response = res.data.body;
          console.log('create new collection', response);
          context.commit('setCurrentCollection', { collection: response });
          context.commit('setCurrentDocumentSet', { docSet: [] });
          return res;
        })
        .catch(reason => {
          console.log('Reason', reason);
          console.log('Reason mini getMiniConnectomeData', payload);
        })
        .finally(() => {});
    },
    AddToCurrentDocumentSet(
      context,
      payload: {
        docToAdd: Array<string>;
      }
    ) {
      if (!payload.docToAdd) {
        return;
      }
      if (payload.docToAdd.length == 0) {
        return;
      }
      const newDocSet = context.getters.getCurrentDocumentSet.map(x => x);
      payload.docToAdd.forEach(item => {
        const indexDoc = context.getters.getCurrentDocumentSet.indexOf(item);
        if (indexDoc == -1) {
          newDocSet.push(item);
        }
      });
      console.log('AddToCurrentDocumentSet', newDocSet);
      context.commit('setCurrentDocumentSet', { docSet: newDocSet });
    },
    updateCollection: async (
      context,
      payload: {
        collection: ContextualMemoryCollection;
      }
    ) => {
      if (!payload.collection) return;

      const apiCallConnectomeData = new Promise<any>((resolve, reject) => {
        axios
          .put(putCollectionURL, payload.collection)
          .then(res => resolve(res))
          .catch(err => reject(err));
      });

      return apiCallConnectomeData
        .then(res => {
          if (!res) {
            return;
          }

          console.log('create new collection', res);
          context.commit('setCurrentCollection', { collection: payload.collection });
          context.dispatch('loadUserCollections', {
            connectomeId: payload.collection.connectomeId,
            language: payload.collection.lang,
          });
          return res;
        })
        .catch(reason => {
          console.log('Reason', reason);
          console.log('Reason mini getMiniConnectomeData', payload);
        })
        .finally(() => {});
    },
  },
};

function convertLocalStorageToMiniConnectomeNetworkData(connectomeId: string, vertices: any, edges: any) {
  if (!connectomeId) {
    return null;
  }

  if (!vertices) {
    return null;
  }

  if (!edges) {
    return null;
  }

  const res = new ConnectomeNetwork(connectomeId, null, vertices, edges);
  return res;
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

function convertLocalStorageToMapStringArrayString(obj: any) {
  if (!obj) {
    return new Map<string, Array<string>>();
  }

  const map = new Map<string, Array<string>>(JSON.parse(obj));
  if (!map) {
    return new Map<string, Array<string>>();
  }
  return map;
}

function convertLocalStorageToArrayNodes(obj: any) {
  if (!obj) {
    return new Array<ConnectomeNode>();
  }

  const list = new Array<ConnectomeNode>(JSON.parse(obj));
  if (!list) {
    return new Array<ConnectomeNode>();
  }
  return list;
}
