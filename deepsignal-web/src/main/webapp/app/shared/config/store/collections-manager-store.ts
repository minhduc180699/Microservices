import { ConnectomeNetwork } from '@/shared/model/connectome-network.model';

import { ConnectomeLink } from '@/shared/model/connectome-link.model';
import { ConnectomeNode } from '@/shared/model/connectome-node.model';
import axios from 'axios';
import { Module } from 'vuex';
import { ContextualMemoryCollection } from '@/shared/model/contextual-memory-collection.model';
import { CmCollectionsItem } from '@/shared/model/cm-collections-item.model';
import { CmCollection } from '@/shared/model/cm-collection.model';

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

export interface CollectionsManagerStorable {
  indexSessionStorage: Map<string, string>;
  collections: Array<CmCollectionsItem>;
  currentCollection: CmCollection;
  collectionsChanged: number;
  currentCollectionChanged: number;
}

export const collectionsManagerStore: Module<CollectionsManagerStorable, any> = {
  namespaced: true,
  state: {
    indexSessionStorage: new Map<string, string>(),
    collections: new Array<CmCollectionsItem>(),
    currentCollection: new CmCollection(null),
    collectionsChanged: 0,
    currentCollectionChanged: 0,
  },
  getters: {
    getIndexSessionStorage: state => state.indexSessionStorage,
    getCollections: state => state.collections,
    getCurrentCollection: state => state.currentCollection,
    getCollectionsFromDocument: (state, docId: string) => {
      if (!state.collections) {
        return [];
      }

      if (!docId) {
        return [];
      }

      if (!state.collections.filter(collection => collection.documentIdList.includes(docId))) {
        return [];
      }

      const collections = state.collections.filter(collection => collection.documentIdList.includes(docId)).map(x => x);

      if (!collections) {
        return [];
      }

      return collections;
    },
    isCollectionsChanged: state => state.collectionsChanged,
    isCurrentCollectionChanged: state => state.currentCollectionChanged,
  },
  mutations: {
    setCollections(state, payload: { collections: Array<CmCollectionsItem> }) {
      if (!payload.collections) {
        return;
      }
      state.collections = payload.collections.map(x => x);
      state.collectionsChanged++;
    },
    addToCollections(state, payload: { collections: Array<CmCollectionsItem> }) {
      if (!payload.collections) {
        return;
      }
      payload.collections.forEach(collectionToAdd => {
        state.collections.push(collectionToAdd);
      });
      state.collectionsChanged++;
    },
    removeFromCollections(state, payload: { collections: Array<CmCollectionsItem> }) {
      if (!payload.collections) {
        return;
      }
      payload.collections.forEach(collectionToRemove => {
        const indexCollecctionToRemove = state.collections.findIndex(
          collection => collection.collectionId === collectionToRemove.collectionId
        );
        state.collections.splice(indexCollecctionToRemove, 1);
      });
      state.collectionsChanged++;
    },
    updateCollectionDocumentListInCollections(state, payload: { collectionUpdated: CmCollection }) {
      if (!payload.collectionUpdated) {
        return;
      }

      const collectionToUpdate = state.collections.find(x => x.collectionId === payload.collectionUpdated.collectionId);

      if (!collectionToUpdate) {
        return;
      }

      collectionToUpdate.documentIdList = payload.collectionUpdated.documentIdList.map(x => x);
      collectionToUpdate.modified = payload.collectionUpdated.modified;
      state.collectionsChanged++;
    },
    setCurrentCollection(state, payload: { collection: CmCollection }) {
      if (!payload.collection) {
        state.currentCollection = new CmCollection(null);
        state.currentCollectionChanged++;
        return;
      }

      if (!payload.collection.collectionId) {
        state.currentCollection = new CmCollection(null);
        state.currentCollectionChanged++;
        return;
      }

      state.currentCollection = new CmCollection(payload.collection);
      state.currentCollectionChanged++;
    },
    AddDocumentsToCurrentCollection(state, payload: { docToAdd: Array<string> }) {
      console.log('AddDocumentsToCurrentCollection', payload);
      if (!payload.docToAdd) {
        return;
      }
      if (payload.docToAdd.length == 0) {
        return;
      }

      payload.docToAdd.forEach(docId => {
        state.currentCollection.documentIdList.push(docId);
        state.currentCollection.modified = new Date();
      });

      state.currentCollectionChanged++;
    },
    RemoveFromCurrentCollection(state, payload: { docToRemove: Array<string> }) {
      console.log('RemoveFromCurrentDocumentSet', payload);
      if (!payload.docToRemove) {
        return;
      }
      if (payload.docToRemove.length == 0) {
        return;
      }

      payload.docToRemove.forEach(docId => {
        const indexToRemove = state.currentCollection.documentIdList.findIndex(x => x === docId);
        if (indexToRemove > -1) {
          state.currentCollection.documentIdList.splice(indexToRemove, 1);
          state.currentCollection.modified = new Date();
        }
      });
      state.currentCollectionChanged++;
    },
    ProcessCurrentDocumentList(state) {},
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
