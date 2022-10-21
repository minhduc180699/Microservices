import { ConnectomeNetworkEdge } from '@/shared/model/connectome-network-edge.model';
import { ConnectomeNetwork } from '@/shared/model/connectome-network.model';
import { ConnectomeNetworkStatus as ConnectomeNetworkStatus } from '@/shared/model/connectome-network-status.model';
import { ConnectomeNetworkVertex } from '@/shared/model/connectome-network-vertex.model';

import axios from 'axios';
import { Module } from 'vuex';
import { Getter } from 'vuex-class';
import * as d3 from 'd3';
import { MINI_MAP_BACKGROUND_COLOR, TYPE_VERTEX } from '@/shared/constants/ds-constants';
import { ConnectomeNode } from '@/shared/model/connectome-node.model';
import { ConnectomeLink } from '@/shared/model/connectome-link.model';
import { number } from 'echarts';
import { doc } from 'prettier';
import { contentIsNullOrShorten } from '@/util/ds-feed-util';

const baseConnectomeApiUrl = '/api/connectome';
const getDataUrl = baseConnectomeApiUrl + '/pd-map/';
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

export interface ConnectomeBuilderStorable {
  documentsSelected: Map<string, Array<ConnectomeNode>>;
  documentColors: Map<string, string>;
  connectome: Array<ConnectomeNode>;
  dataUpdated: number;
  minNodeWeight: number;
  minLinkedNodes: number;
  minRelatedDocuments: number;
  noteSequence: number;
  colorSequence: number;
}

export const connectomeBuilderStore: Module<ConnectomeBuilderStorable, any> = {
  namespaced: true,
  state: {
    documentsSelected: new Map<string, Array<ConnectomeNode>>(),
    documentColors: new Map<string, string>(),
    connectome: new Array<ConnectomeNode>(),
    dataUpdated: 0,
    minNodeWeight: 0,
    minLinkedNodes: 0,
    minRelatedDocuments: 0,
    noteSequence: 0,
    colorSequence: 0,
  },
  getters: {
    getDocumentsSelected: state => state.documentsSelected,
    getConnectome: state => state.connectome,
    getNodes: state =>
      state.connectome
        .filter(
          node =>
            node.weight >= state.minNodeWeight &&
            node.linkedNodes.length >= state.minLinkedNodes &&
            node.relatedDocuments.length >= state.minRelatedDocuments
        )
        .sort((a, b) => (a.weight > b.weight ? -1 : 1)),
    getLinks: (state, getters) => {
      if (!getters.getNodes) {
        return new Array<ConnectomeLink>();
      }

      if (getters.getNodes.length == 0) {
        return new Array<ConnectomeLink>();
      }

      const links = new Array<ConnectomeLink>();
      const alreadyInserted = new Array<string>();

      getters.getNodes.forEach(node => {
        if (node.linkedNodes) {
          let nodesMatched: Array<ConnectomeNode> = [];
          const regOption = new RegExp(node.label, 'ig');
          nodesMatched = getters.getNodes.filter(vertex => vertex.label.match(regOption));

          node.linkedNodes.forEach(linkedNode => {
            const child = getters.getNodes.findIndex(x => x.id === linkedNode);
            if (child == -1) {
              return;
            }
            if (alreadyInserted.indexOf(node.id + '=>' + linkedNode) == -1) {
              alreadyInserted.push(node.id + '=>' + linkedNode);
              alreadyInserted.push(linkedNode + '=>' + node.id);
              links.push(new ConnectomeLink(node.id, linkedNode));
            } else {
              const link = links.find(x => x.label === node.id + '=>' + linkedNode || x.label === linkedNode + '=>' + node.id);
              link.weight++;
            }
          });
        }
      });

      return links;
    },
    getDocumentColors: state => state.documentColors,
    getDataUpdated: state => state.dataUpdated,
    getSequenceNote: state => state.noteSequence,
    getColorNote: state => state.colorSequence,
    getMinNodeWeight: state => state.minNodeWeight,
    getMinLinkedNodes: state => state.minLinkedNodes,
    getMinRelatedDocuments: state => state.minRelatedDocuments,
  },
  mutations: {
    addPd(
      state,
      PdData: {
        docId: string;
        connectome: Array<ConnectomeNode>;
      }
    ) {
      if (!PdData) {
        return;
      }

      if (!PdData.docId) {
        return;
      }

      if (state.documentsSelected.has(PdData.docId)) {
        return;
      }
      PdData.connectome.forEach(element => {
        const node = state.connectome.find(elem => elem.id === element.id);
        if (node) {
          node.relatedDocuments.push(PdData.docId);
          node.weight = node.weight + element.weight;
          node.linkedNodes.push(...element.linkedNodes);
        } else {
          state.connectome.push(new ConnectomeNode(element));
        }
      });
      state.documentsSelected.set(
        PdData.docId,
        PdData.connectome.map(x => new ConnectomeNode(x))
      );
      // console.log('documentsSelected', state.documentsSelected);
      // console.log('added doc', state.connectome);
      state.dataUpdated++;
    },
    removePd(
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

      if (!state.documentsSelected.has(PdData.docId)) {
        return;
      }
      const connectomeToRemove = state.documentsSelected.get(PdData.docId);

      if (!connectomeToRemove) {
        return;
      }
      // console.log('connectome to remove', connectomeToRemove);
      //const tmpConnectome = state.connectome.map(x => x);
      connectomeToRemove.forEach(element => {
        const node = state.connectome.find(elem => elem.id === element.id);
        if (node) {
          const indexDocId = node.relatedDocuments.indexOf(PdData.docId);
          if (indexDocId > -1) {
            node.relatedDocuments.splice(indexDocId, 1);
            element.linkedNodes.forEach(linkedNode => {
              const indexNodeToRemove = node.linkedNodes.indexOf(linkedNode);
              if (indexNodeToRemove > -1) {
                node.linkedNodes.splice(indexNodeToRemove, 1);
              }
            });
          }
          node.weight = node.weight - element.weight;
        }
      });

      state.connectome = state.connectome.filter(x => x.relatedDocuments.length > 0);
      // console.log('remove doc', state.connectome);
      state.documentsSelected.delete(PdData.docId);
      state.dataUpdated++;
    },
    addPdColor(
      state,
      PdData: {
        docId: string;
        color: string;
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
    removePdColor(
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

      if (!state.documentColors.has(PdData.docId)) {
        return;
      }
      state.documentColors.delete(PdData.docId);
    },
    incrementSequenceNote(state) {
      state.noteSequence++;
    },
    setMinNodeWeight(state, payload: { value: number }) {
      state.minNodeWeight = payload.value;
    },
    setMinLinkedNodes(state, payload: { value: number }) {
      state.minLinkedNodes = payload.value;
    },
    setMinRelatedDocuments(state, payload: { value: number }) {
      state.minRelatedDocuments = payload.value;
    },
    setDataUpdate(state) {
      state.dataUpdated++;
    },
    resetData(state) {},
  },
  actions: {
    addPdConnectomeData: async (
      context,
      payload: {
        connectomeId: string;
        language: string;
        ids: Array<string>;
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
      console.log('mini getPdConnectome', {
        sourceLang: language,
        ids: payload.ids,
      });

      if (!payload.ids) {
        return;
      }

      if (payload.ids.length == 0) {
        return;
      }

      let shouldContinue = true;

      payload.ids.forEach(element => {
        if (!element) {
          shouldContinue = false;
        }
      });

      if (!shouldContinue) {
        return;
      }

      if (context.getters.getDocumentsSelected.has(payload.ids[0])) {
        context.commit('removePd', { docId: payload.ids[0] });
        context.commit('removePdColor', { docId: payload.ids[0] });
        return;
      }

      const apiCallConnectomeData = new Promise<any>((resolve, reject) => {
        axios
          .post(getDataUrl + payload.connectomeId, {
            sourceLang: language,
            ids: payload.ids,
          })
          .then(res => resolve(res))
          .catch(err => reject(err));
      });

      return apiCallConnectomeData
        .then(res => {
          if (!res.data.body) {
            return;
          }
          const response = res.data.body;
          console.log('getPdConnectomeData', response);

          context.commit('addPd', { docId: response.id, connectome: response.connectome });
          context.commit('addPdColor', { docId: response.id });
          return res;
        })
        .catch(reason => {
          console.log('Reason', reason);
          console.log('Reason mini getMiniConnectomeData', {
            sourceLang: language,
            feedIds: payload.ids,
          });
        })
        .finally(() => {});
    },
    addTextConnectomeData: async (
      context,
      payload: {
        connectomeId: string;
        documentId: string;
        language: string;
        title: string;
        content: string;
      }
    ) => {
      let language = 'en';
      let docId = context.getters.getSequenceNote;
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
      // console.log('mini getPdConnectome', {
      //   sourceLang: language,
      //   title: payload.title,
      //   content: payload.content,
      // });

      if (!payload.title && !payload.content) {
        return;
      }

      if (!payload.documentId) {
        docId = 'text' + context.getters.getSequenceNote;
        context.commit('incrementSequenceNote');
      } else {
        docId = payload.documentId;
      }

      if (context.getters.getDocumentsSelected.has(docId)) {
        console.log('remove Pd' + docId);
        context.commit('removePd', { docId: docId });
        context.commit('removePdColor', { docId: docId });
        return { documentIds: [docId], connectome: [] };
      }

      const apiCallConnectomeData = new Promise<any>((resolve, reject) => {
        axios
          .post(getTextDataUrl + language, {
            connectomeId: payload.connectomeId,
            documentIds: [docId],
            title: payload.title,
            content: payload.content,
          })
          .then(res => resolve(res))
          .catch(err => reject(err));
      });

      return apiCallConnectomeData
        .then(res => {
          if (!res.data.body) {
            return;
          }
          const response = res.data.body;
          console.log('getELData', response);
          response.documentIds.forEach(element => {
            context.commit('addPd', { docId: element, connectome: response.connectome });
            context.commit('addPdColor', { docId: element });
          });

          return response;
        })
        .catch(reason => {
          console.log('Reason', reason);
          console.log('Reason  getELData', getTextDataUrl + language, {
            connectomeId: payload.connectomeId,
            documentIds: [docId],
            title: payload.title,
            content: payload.content,
          });
        })
        .finally(() => {});
    },
    removePdConnectomeData: async (
      context,
      payload: {
        id: string;
      }
    ) => {
      if (!payload.id) {
        return;
      }

      if (context.getters.getDocumentsSelected.has(payload.id)) {
        console.log('remove2 Pd' + payload.id);
        context.commit('removePd', { docId: payload.id });
        context.commit('removePdColor', { docId: payload.id });
        return;
      }
    },
    logout(context) {
      context.commit('resetData');
    },
    updateMinNodeWeight(context, value: number) {
      context.commit('setMinNodeWeight', { value: value });
      return context.getters.getMinNodeWeight;
    },
    updateMinLinkedNodes(context, value: number) {
      context.commit('setMinLinkedNodes', { value: value });
      return context.getters.getMinLinkedNodes;
    },
    updateMinRelatedDocuments(context, value: number) {
      context.commit('setMinRelatedDocuments', { value: value });
      return context.getters.getMinRelatedDocuments;
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
