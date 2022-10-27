import { ConnectomeNetwork } from '@/shared/model/connectome-network.model';

import { ConnectomeNode } from '@/shared/model/connectome-node.model';
import axios from 'axios';
import { Module } from 'vuex';
import { ContextualMemoryCollection } from '@/shared/model/contextual-memory-collection.model';
import { CmCollectionsItem } from '@/shared/model/cm-collections-item.model';
import { CmCollection } from '@/shared/model/cm-collection.model';

const baseCollectionsApiUrl = '/api/collections';
const getCollectionsURL = baseCollectionsApiUrl + '/get/all';
const getCollectionURL = baseCollectionsApiUrl + '/get';
const getCollectionRequestListURL = baseCollectionsApiUrl + '/getRequestList';
const postCreateCollectionURL = baseCollectionsApiUrl + '/create';
const postUpdateCollectionURL = baseCollectionsApiUrl + '/update';
const getDocGraphUrl = baseCollectionsApiUrl + '/document-map';

const basePersonalDocumentsApiUrl = '/api/personal-documents';
const getPersonalDocumentsByDocIdsUrl = basePersonalDocumentsApiUrl + '/getByDocIds';

const getTextDataUrl = baseCollectionsApiUrl + '/text-map/';

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
const defaultNoColor = '#dedede';

export interface CollectionsManagerStorable {
  indexSessionStorage: Map<string, string>;
  connectomeId: string;
  lang: string;
  collections: Array<CmCollectionsItem>;
  currentCollection: CmCollection;
  documentColors: Map<string, string>;
  currentConnectome: Array<ConnectomeNode>;
  nodesListByDocument: Map<string, Array<ConnectomeNode>>;
  minNodeWeight: number;
  minLinkedNodes: number;
  minRelatedDocuments: number;
  collectionsChanged: number;
  currentCollectionChanged: number;
  currentConnectomeChanged: number;
  colorSequence: number;
}

export const collectionsManagerStore: Module<CollectionsManagerStorable, any> = {
  namespaced: true,
  state: {
    indexSessionStorage: new Map<string, string>(),
    connectomeId: null,
    lang: null,
    collections: new Array<CmCollectionsItem>(),
    currentCollection: new CmCollection(null),
    documentColors: new Map<string, string>(),
    currentConnectome: new Array<ConnectomeNode>(),
    nodesListByDocument: new Map<string, Array<ConnectomeNode>>(),
    minNodeWeight: 0,
    minLinkedNodes: 0,
    minRelatedDocuments: 0,
    collectionsChanged: 0,
    currentCollectionChanged: 0,
    currentConnectomeChanged: 0,
    colorSequence: 0,
  },
  getters: {
    getIndexSessionStorage: state => state.indexSessionStorage,
    getConnectomeId: state => state.connectomeId,
    getLang: State => State.lang,
    getCollections: state => state.collections,
    getCurrentCollection: state => state.currentCollection,
    getCurrentConnectome: state => state.currentConnectome,
    getNodesListByDocument: state => state.nodesListByDocument,
    getDocumentColors: state => state.documentColors,
    getNodes: state =>
      state.currentConnectome
        .filter(
          node =>
            node.weight >= state.minNodeWeight &&
            node.linkedNodes.length >= state.minLinkedNodes &&
            node.relatedDocuments.length >= state.minRelatedDocuments &&
            !node.disable
        )
        .sort((a, b) => (a.weight > b.weight ? -1 : 1)),
    getCollectionsFromDocument: state => (docId: string) => {
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
    getCollectionsFromDocuments: (state, getters) => (docIds: Array<string>) => {
      if (!state.collections) {
        return [];
      }

      if (!docIds) {
        return [];
      }

      if (docIds.length == 0) {
        return [];
      }
      //console.log(docIds);
      const result = new Array<CmCollectionsItem>();
      docIds.forEach(docId => {
        const listCollections = getters.getCollectionsFromDocument(docId);
        listCollections.forEach(collection => {
          if (!result.includes(collection)) result.push(collection);
        });
      });

      return result;
    },
    isCollectionsChanged: state => state.collectionsChanged,
    isCurrentCollectionChanged: state => state.currentCollectionChanged,
    isCurrentConnectomeChanged: state => state.currentConnectomeChanged,
  },
  mutations: {
    setConnectomeId(state, connectomeId: string) {
      if (!connectomeId) {
        return;
      }

      state.connectomeId = connectomeId;
    },
    setLang(state, lang: string) {
      let language = 'en';
      if (lang) {
        switch (lang.toLowerCase()) {
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
      state.lang = language;
    },
    setCollections(state, payload: { collections: Array<CmCollectionsItem> }) {
      if (!payload.collections) {
        return;
      }
      state.collections = payload.collections.map(x => x);
      state.collectionsChanged++;
    },
    addPd(
      state,
      payload: {
        docId: string;
        connectome: Array<ConnectomeNode>;
      }
    ) {
      if (!payload) {
        return;
      }

      if (!payload.docId) {
        return;
      }

      payload.connectome.forEach(element => {
        const node = state.currentConnectome.find(elem => elem.id === element.id);
        if (node) {
          if (node.relatedDocuments && node.relatedDocuments.length > 0) {
            node.weight = node.weight / (node.relatedDocuments.length * node.relatedDocuments.length) + element.weight;
          } else {
            node.weight = element.weight;
          }
          node.relatedDocuments.push(payload.docId);
          node.keywordList.push(...element.keywordList);
          node.weight = node.weight * (node.relatedDocuments.length * node.relatedDocuments.length);
          node.linkedNodes.push(...element.linkedNodes);
        } else {
          state.currentConnectome.push(new ConnectomeNode(element));
        }
      });

      state.nodesListByDocument.set(
        payload.docId,
        payload.connectome.map(x => new ConnectomeNode(x))
      );
      console.log('added doc', payload.connectome);
      state.currentConnectomeChanged++;
    },
    disableOrphans(state) {
      console.log('disableOrphans');
      state.currentConnectome.forEach(node => {
        if (!node.linkedNodes) {
          return;
        }
        const linkedNodeSet = new Set<string>();
        node.linkedNodes.forEach(docId => {
          linkedNodeSet.add(docId);
        });

        if (linkedNodeSet.size < 1) {
          console.log('node is disabled:' + node.label);
          node.disable = true;
        } else if (linkedNodeSet.size == 1) {
          console.log('candidate to disable:' + node.label + ':' + linkedNodeSet.values().next().value);
          try {
            const regOption = new RegExp(node.label.toLowerCase(), 'ig');
            const targetNodes = state.currentConnectome.filter(target => target.id === linkedNodeSet.values().next().value);
            console.log('target node:', targetNodes);
            if (targetNodes && targetNodes.length == 1) {
              if (targetNodes[0].label.toLowerCase().match(regOption)) {
                console.log('node is disabled1:' + node.label);
                node.disable = true;
              } else if (targetNodes[0].label.toLowerCase().includes(node.label.toLowerCase())) {
                console.log('node is disabled2:' + node.label);
                node.disable = true;
              } else {
                node.disable = false;
              }
            } else {
              node.disable = false;
            }
          } catch (e) {
            console.log('error' + e);
          } finally {
            node.disable = false;
          }
        } else {
          node.disable = false;
        }
      });
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
        const indexCollectionToRemove = state.collections.findIndex(
          collection => collection.collectionId === collectionToRemove.collectionId
        );
        state.collections.splice(indexCollectionToRemove, 1);
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
      collectionToUpdate.modifiedDate = payload.collectionUpdated.modifiedDate;
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

      if (!payload.collection.nodeList) {
        return;
      }
      state.currentConnectome = payload.collection.nodeList.map(x => x);
      state.currentConnectomeChanged++;
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
        if (state.currentCollection.documentIdList.indexOf(docId) < 0) {
          state.currentCollection.documentIdList.push(docId);
          state.currentCollection.modifiedDate = new Date();
        }
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
          state.currentCollection.modifiedDate = new Date();
        }
      });
      state.currentCollectionChanged++;
    },
    addPdColor(
      state,
      PdData: {
        docId: string;
      }
    ) {
      if (!PdData) {
        return;
      }

      if (!PdData.docId) {
        return;
      }

      if (state.documentColors.has(PdData.docId)) {
        return;
      }

      const colorIndex = state.colorSequence % circleColors.length;
      state.colorSequence++;
      const newColor = circleColors[colorIndex];
      state.documentColors.set(PdData.docId, newColor);
    },
    AddNodesList(state, payload: { documentId: string; nodesList: Array<ConnectomeNode> }) {
      if (!payload.documentId) {
        return;
      }

      if (!payload.nodesList) {
        return;
      }

      if (!state.nodesListByDocument) {
        state.nodesListByDocument = new Map<string, Array<ConnectomeNode>>();
      }
      state.nodesListByDocument.set(payload.documentId, payload.nodesList);
    },
    resetCurrentConnectome(state) {
      state.currentConnectome = new Array<ConnectomeNode>();
    },
    resetConnectomeBuilderData(state) {
      state.nodesListByDocument = new Map<string, Array<ConnectomeNode>>();
      state.documentColors = new Map<string, string>();
      state.currentConnectome = new Array<ConnectomeNode>();
      state.minNodeWeight = 0;
      state.minLinkedNodes = 0;
      state.minRelatedDocuments = 0;
      state.colorSequence = 0;
      console.log('connectome builder reset');
    },
    ProcessCurrentDocumentList(state) {},
    resetData(state) {},
  },
  actions: {
    loadUserContext: async (
      context,
      payload: {
        connectomeId: string;
        language: string;
      }
    ) => {
      if (!payload) {
        return;
      }

      context.commit('setConnectomeId', payload.connectomeId);
      context.commit('setLang', payload.language);

      return context
        .dispatch('loadUserCollections')
        .then(res => {
          if (!res) {
            return { status: 'NOK', message: 'error load User Context', result: null };
          }
          return { status: 'OK', message: 'user context loaded', result: res };
        })
        .catch(reason => {
          console.log('Reason', reason);
          return { status: 'NOK', message: 'error load User Context', result: null };
        });
    },
    loadUserCollections: async context => {
      if (!context.getters.getConnectomeId || !context.getters.getLang) {
        context.commit('setCollections', { collections: new Array<CmCollectionsItem>() });
        return;
      }

      axios.defaults.headers.common['connectomeId'] = context.getters.getConnectomeId;
      axios.defaults.headers.common['lang'] = context.getters.getLang;

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

          const responseCollections = response.map(x => new CmCollectionsItem(x));
          context.commit('setCollections', { collections: responseCollections });

          return response;
        })
        .catch(reason => {
          console.log('Reason', reason);
        });
    },
    loadCollection: async (context, payload: { collectionId: string }) => {
      if (!context.getters.getConnectomeId || !context.getters.getLang) {
        return;
      }

      axios.defaults.headers.common['connectomeId'] = context.getters.getConnectomeId;
      axios.defaults.headers.common['lang'] = context.getters.getLang;
      axios.defaults.headers.common['collectionId'] = payload.collectionId;

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

          const response = res.data.body;
          context.commit('setCurrentCollection', { collection: response });

          return response;
        })
        .catch(reason => {
          console.log('Reason', reason);
        });
    },
    loadCollectionRequest: async (context, payload: { collectionId: string }) => {
      if (!context.getters.getConnectomeId || !context.getters.getLang) {
        return;
      }

      axios.defaults.headers.common['connectomeId'] = context.getters.getConnectomeId;
      axios.defaults.headers.common['lang'] = context.getters.getLang;
      axios.defaults.headers.common['collectionId'] = payload.collectionId;

      if (!payload.collectionId) {
        return;
      }

      const apiCallConnectomeData = new Promise<any>((resolve, reject) => {
        axios
          .get(getCollectionRequestListURL, { params: { collectionId: payload.collectionId } })
          .then(res => resolve(res))
          .catch(err => reject(err));
      });

      return apiCallConnectomeData
        .then(res => {
          if (!res.data.body) {
            return;
          }

          const response = res.data.body;
          //context.commit('setCurrentCollection', { collection: response });
          console.log('collection requests', res);
          return response;
        })
        .catch(reason => {
          console.log('Reason', reason);
        });
    },
    GenerateCurrentConnectome: async context => {
      if (!context.getters.getConnectomeId || !context.getters.getLang) {
        return;
      }
      context.commit('resetCurrentConnectome');

      if (!context.getters.getNodesListByDocument) {
        console.log('nodesListByDocument undefined');
        return;
      }
      if (!context.getters.getCurrentCollection) {
        console.log('currentCollection undefined');
        return;
      }

      if (!context.getters.getCurrentCollection.documentIdList) {
        console.log('currentCollection.documentIdList undefined');
        return;
      }
      context.getters.getCurrentCollection.documentIdList.forEach(docId => {
        if (!docId) {
          return;
        }

        context.commit('addPdColor', { docId: docId });
      });

      // context.getters.getCurrentCollection.documentIdList.forEach(docId => {
      //   if (!docId) {
      //     return;
      //   }

      //   const docConnectome = context.getters.getNodesListByDocument.get(docId);
      //   if (docConnectome) {
      //     context.commit('addPd', { docId: docId, connectome: docConnectome });
      //     return;
      //   }

      //   axios.defaults.headers.common['connectomeId'] = context.getters.getConnectomeId;
      //   axios.defaults.headers.common['lang'] = context.getters.getLang;

      //   const apiCallConnectomeData = new Promise<any>((resolve, reject) => {
      //     axios
      //       .post(getDocGraphUrl, {
      //         documentId: docId,
      //       })
      //       .then(res => resolve(res))
      //       .catch(err => reject(err));
      //   });

      //   apiCallConnectomeData
      //     .then(res => {
      //       if (!res.data.body) {
      //         return;
      //       }

      //       const response = res.data.body;
      //       console.log('getELData', response);
      //       context.commit('addPd', { docId: docId, connectome: response.nodeList });
      //       return response;
      //     })
      //     .catch(reason => {
      //       console.log('Reason', reason);
      //       console.log('Reason  getELData', getDocGraphUrl, {
      //         documentIds: [docId],
      //       });
      //     })
      //     .finally(() => {});
      // });

      // context.commit('disableOrphans');
    },
    createCollection: async (
      context,
      payload: {
        docIds: Array<string>;
      }
    ) => {
      if (!context.getters.getConnectomeId || !context.getters.getLang) {
        return;
      }

      axios.defaults.headers.common['connectomeId'] = context.getters.getConnectomeId;
      axios.defaults.headers.common['lang'] = context.getters.getLang;

      const newCollection = new ContextualMemoryCollection(null);
      newCollection.connectomeId = context.getters.getConnectomeId;
      newCollection.lang = context.getters.getLang;
      newCollection.documentIdList = payload.docIds.map(x => x);

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

          // context.commit('setCurrentCollection', { collection: response });
          // context.commit('setCurrentDocumentSet', { docSet: [] });
          context
            .dispatch('loadUserCollections')
            .then(res => {
              if (!res) {
                return { status: 'NOK', message: 'error load User Context', result: null };
              }
              return { status: 'OK', message: 'user context loaded', result: new CmCollection(response) };
            })
            .catch(reason => {
              console.log('Reason', reason);
              return { status: 'NOK', message: 'error load User Context', result: null };
            })
            .finally(() => {});
          return response;
        })
        .catch(reason => {
          console.log('Reason', reason);
          console.log('Reason mini getMiniConnectomeData', payload);
        });
    },
    updateCollection: async (
      context,
      payload: {
        collection: CmCollection;
      }
    ) => {
      if (!context.getters.getConnectomeId || !context.getters.getLang) {
        return;
      }

      axios.defaults.headers.common['connectomeId'] = context.getters.getConnectomeId;
      axios.defaults.headers.common['lang'] = context.getters.getLang;

      if (!payload.collection) return;
      payload.collection.modifiedDate = null;
      const apiCallConnectomeData = new Promise<any>((resolve, reject) => {
        axios
          .post(postUpdateCollectionURL, payload.collection)
          .then(res => resolve(res))
          .catch(err => reject(err));
      });

      return apiCallConnectomeData
        .then(res => {
          if (!res) {
            return;
          }
          if (!res.data.body) {
            return;
          }
          const response = res.data.body;
          console.log('update collection', response);
          //context.commit('setCurrentCollection', { collection: payload.collection });
          context
            .dispatch('loadUserCollections')
            .then(res => {
              if (!res) {
                return { status: 'NOK', message: 'error load User Context', result: null };
              }
              return { status: 'OK', message: 'user context loaded', result: res };
            })
            .catch(reason => {
              console.log('Reason', reason);
              return { status: 'NOK', message: 'error load User Context', result: null };
            })
            .finally(() => {});
          return response;
        })
        .catch(reason => {
          console.log('Reason', reason);
          console.log('Reason mini getMiniConnectomeData', payload);
        });
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
    logout(context) {},

    //#region Interface with SLT
    getAllCollections: async context => {
      //return list of collection contains this docIds
      return { status: 'OK', message: 'user context loaded', result: context.getters.getCollections };
    },
    //return list of collection contains this docIds
    getCollectionsFromDocIds: async (
      context,
      payload: {
        docIds: Array<string>;
      }
    ) => {
      if (!payload.docIds) {
        return { status: 'NOK', message: 'docIds list is undefined', result: [] };
      }

      if (payload.docIds.length == 0) {
        return { status: 'OK', message: 'docIds list is empty', result: [] };
      }

      return {
        status: 'OK',
        message: 'list collection containing the docIds',
        result: context.getters.getCollectionsFromDocuments(payload.docIds),
      };
    },
    getCollectionDetails: async (
      context,
      payload: {
        collectionId: string;
      }
    ) => {
      if (!payload) {
        return { status: 'NOK', message: 'paramters undefined', result: null };
      }

      if (!payload.collectionId) {
        return { status: 'NOK', message: 'collectionId is undefined', result: null };
      }

      const collectionItemIndex = context.getters.getCollections.findIndex(collection => collection.collectionId == payload.collectionId);
      console.log('getCollectionDetails:collectionItemIndex', collectionItemIndex);
      if (collectionItemIndex < 0) {
        return { status: 'NOK', message: 'collectionId not include in collections', result: null };
      }

      const collectionItem = context.getters.getCollections[collectionItemIndex];
      console.log('getCollectionDetails:collectionItem', collectionItem);

      if (!collectionItem) {
        return { status: 'NOK', message: 'collection is undefined', result: null };
      }

      const newCurrentCollection = new CmCollection(collectionItem);

      return { status: 'OK', message: 'collection to be edited', result: newCurrentCollection };
    },
    //return the collection and set it as current collection
    editCollection: async (
      context,
      payload: {
        collectionId: string;
      }
    ) => {
      if (!payload) {
        return { status: 'NOK', message: 'paramters undefined', result: null };
      }

      if (!payload.collectionId) {
        return { status: 'NOK', message: 'collectionId is undefined', result: null };
      }

      const collectionItemIndex = context.getters.getCollections.findIndex(collection => collection.collectionId == payload.collectionId);
      console.log('editCollection:collectionItemIndex', collectionItemIndex);
      if (collectionItemIndex < 0) {
        return { status: 'NOK', message: 'collectionId not include in collections', result: null };
      }

      const collectionItem = context.getters.getCollections[collectionItemIndex];
      console.log('editCollection:collectionItem', collectionItem);

      if (!collectionItem) {
        return { status: 'NOK', message: 'collection is undefined', result: null };
      }

      const newCurrentCollection = new CmCollection(collectionItem);
      newCurrentCollection.connectomeId = context.getters.getConnectomeId;
      newCurrentCollection.lang = context.getters.getLang;

      context.commit('setCurrentCollection', { collection: newCurrentCollection });
      context.dispatch('GenerateCurrentConnectome');
      return { status: 'OK', message: 'collection to be edited', result: newCurrentCollection };
    },
    getCurrentDraftCollection: async context => {
      if (!context.getters.getCurrentCollection) {
        return { status: 'NOK', message: 'no current Colleciton defined', result: null };
      }

      return { status: 'OK', message: 'user context loaded', result: context.getters.getCurrentCollection };
      //return the current collection
    },
    //add bookmarks to the current draft collection.  if a collection is selected to Add, you should send the documentlist from this collection
    addBookmarksToCurrentCollection: async (
      context,
      payload: {
        docIds: Array<string>;
      }
    ) => {
      if (!context.getters.getCurrentCollection) {
        return { status: 'NOK', message: 'no current Colleciton defined', result: null };
      }

      context.commit('AddDocumentsToCurrentCollection', { docToAdd: payload.docIds });
      context.dispatch('GenerateCurrentConnectome');
      return { status: 'OK', message: 'documents added', result: context.getters.getCurrentCollection };
    },
    //from the current draft collection container, remove the bookmark
    removeBookmarksFromCurrentCollection: async (
      context,
      payload: {
        docIds: Array<string>;
      }
    ) => {
      if (!context.getters.getCurrentCollection) {
        return { status: 'NOK', message: 'no current Colleciton defined', result: null };
      }

      context.commit('RemoveFromCurrentCollection', { docToRemove: payload.docIds });
      context.dispatch('GenerateCurrentConnectome');
      return { status: 'OK', message: 'documents removed', result: context.getters.getCurrentCollection };
    },
    saveCurrentDraftCollection: async context => {
      if (!context.getters.getCurrentCollection) {
        return { status: 'NOK', message: 'no current Colleciton defined', result: null };
      }
      if (!context.getters.getCurrentCollection.collectionId) {
        return context
          .dispatch('createCollection', {
            docIds: context.getters.getCurrentCollection.documentIdList,
          })
          .then(res => {
            if (!res) {
              return { status: 'NOK', message: 'cannot create the collection', result: null };
            }
            console.log('createCollection', res);
            context.commit('setCurrentCollection', { collection: new CmCollection(res) });
            return { status: 'OK', message: 'draft saved', result: new CmCollection(res) };
          });
      } else {
        return context
          .dispatch('updateCollection', {
            collection: context.getters.getCurrentCollection,
          })
          .then(res => {
            if (!res) {
              return { status: 'NOK', message: 'cannot create the collection', result: null };
            }
            context.commit('setCurrentCollection', { collection: new CmCollection(res) });
            return { status: 'OK', message: 'collection updated', result: res };
          });
      }
    },
    closeCurrentDraftCollection: async context => {
      if (!context.getters.getCurrentCollection) {
        return { status: 'NOK', message: 'no current Colleciton defined', result: null };
      }

      context.commit('setCurrentCollection', { collection: new CmCollection(null) });
      return { status: 'OK', message: 'draft saved', result: null };
    },
    deleteCollection: async (
      context,
      payload: {
        collectionId: string;
      }
    ) => {},
    //#endregion
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
