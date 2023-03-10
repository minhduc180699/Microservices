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
const getDocGraphUrl = baseConnectomeApiUrl + '/doc-graph/';

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
            node.relatedDocuments.length >= state.minRelatedDocuments &&
            !node.disable
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
        keyword: string;
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
          if (node.relatedDocuments && node.relatedDocuments.length > 0) {
            node.weight = node.weight / (node.relatedDocuments.length * node.relatedDocuments.length) + element.weight;
          } else {
            node.weight = element.weight;
          }
          node.relatedDocuments.push(PdData.docId);
          node.keywordList.push(...element.keywordList);
          node.weight = node.weight * (node.relatedDocuments.length * node.relatedDocuments.length);
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
      console.log('added keyword', PdData.keyword);
      console.log('added doc', PdData.connectome);
      state.dataUpdated++;
    },
    addPdList(
      state,
      PdData: {
        docId: string;
        keyword: string;
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

      state.documentsSelected.set(
        PdData.docId,
        PdData.connectome.map(x => new ConnectomeNode(x))
      );
      // console.log('documentsSelected', state.documentsSelected);
      console.log('added to pdlist keyword', PdData.keyword);
      console.log('added to pdlist  doc', PdData.connectome);
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
      console.log('connectome to remove', connectomeToRemove);
      //const tmpConnectome = state.connectome.map(x => x);
      connectomeToRemove.forEach(element => {
        const node = state.connectome.find(elem => elem.id === element.id);
        if (node) {
          const indexDocId = node.relatedDocuments.indexOf(PdData.docId);
          if (indexDocId > -1) {
            node.weight = node.weight / (node.relatedDocuments.length * node.relatedDocuments.length) - element.weight;
            node.relatedDocuments.splice(indexDocId, 1);
            node.weight = node.weight * node.relatedDocuments.length * node.relatedDocuments.length;
            element.linkedNodes.forEach(linkedNode => {
              const indexNodeToRemove = node.linkedNodes.indexOf(linkedNode);
              if (indexNodeToRemove > -1) {
                node.linkedNodes.splice(indexNodeToRemove, 1);
              }
            });
            element.keywordList.forEach(keyword => {
              const indexNodeToRemove = node.keywordList.indexOf(keyword);
              if (indexNodeToRemove > -1) {
                node.keywordList.splice(indexNodeToRemove, 1);
              }
            });
          }
        }
      });
      state.connectome = state.connectome.filter(x => x.relatedDocuments.length > 0);
      // console.log('remove doc', state.connectome);
      state.documentsSelected.delete(PdData.docId);
      state.dataUpdated++;
    },
    removePdList(
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
    setConnectome(state, payload: { connectome: Array<ConnectomeNode> }) {
      if (!payload.connectome) {
        return;
      }

      state.connectome = payload.connectome.map(x => new ConnectomeNode(x));
      state.dataUpdated++;
    },
    disableOrphans(state) {
      console.log('disableOrphans');
      state.connectome.forEach(node => {
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
          const regOption = new RegExp(node.label.toLowerCase(), 'ig');
          const targetNodes = state.connectome.filter(target => target.id === linkedNodeSet.values().next().value);
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
        } else {
          node.disable = false;
        }
      });
    },
    resetConnectomeBuilderData(state) {
      state.documentsSelected = new Map<string, Array<ConnectomeNode>>();
      state.documentColors = new Map<string, string>();
      state.connectome = new Array<ConnectomeNode>();
      state.dataUpdated = 0;
      state.minNodeWeight = 0;
      state.minLinkedNodes = 0;
      state.minRelatedDocuments = 0;
      state.noteSequence = 0;
      state.colorSequence = 0;
      console.log('connectome builder reset');
    },
  },
  actions: {
    addTextConnectomeData: async (
      context,
      payload: {
        connectomeId: string;
        documentId: string;
        language: string;
        keyword: string;
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
          case '?????????':
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
        context.commit('disableOrphans');
        return { documentIds: [docId], connectome: [] };
      }

      const apiCallConnectomeData = new Promise<any>((resolve, reject) => {
        axios
          .post(getDocGraphUrl, {
            connectomeId: payload.connectomeId,
            documentIds: [docId],
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
            response.connectome.forEach(element => {
              element.keywordList = new Array<string>();
              if (payload.keyword) element.keywordList.push(payload.keyword);
            });
            context.commit('addPd', { docId: element, keyword: payload.keyword, connectome: response.connectome });
            context.commit('addPdColor', { docId: element });
            context.commit('disableOrphans');
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
    loadDocumentFromContext: async (
      context,
      payload: {
        connectomeId: string;
        documentId: string;
        keyword: string;
      }
    ) => {
      if (!payload.documentId) {
        return;
      }

      if (context.getters.getDocumentsSelected.has(payload.documentId)) {
        return { documentIds: [payload.documentId], connectome: [] };
      }

      const apiCallConnectomeData = new Promise<any>((resolve, reject) => {
        axios
          .post(getDocGraphUrl, {
            connectomeId: payload.connectomeId,
            documentIds: [payload.documentId],
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
            response.connectome.forEach(element => {
              element.keywordList = new Array<string>();
              if (payload.keyword) {
                element.keywordList.push(payload.keyword);
                //console.log('loadDocumentFromContext element.keywordList', element.keywordList);
              }
            });
            context.commit('addPdList', { docId: element, keyword: payload.keyword, connectome: response.connectome });
            context.commit('addPdColor', { docId: element });
          });

          return response;
        })
        .catch(reason => {
          console.log('Reason', reason);
          console.log('Reason  getELData', getDocGraphUrl, {
            connectomeId: payload.connectomeId,
            documentIds: [payload.documentId],
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
