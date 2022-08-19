import { ConnectomeNetworkEdge } from '@/shared/model/connectome-network-edge.model';
import { ConnectomeNetwork } from '@/shared/model/connectome-network.model';
import { ConnectomeNetworkStatus as ConnectomeNetworkStatus } from '@/shared/model/connectome-network-status.model';
import { ConnectomeNetworkVertex } from '@/shared/model/connectome-network-vertex.model';

import axios from 'axios';
import { Module } from 'vuex';
import { Getter } from 'vuex-class';
import * as d3 from 'd3';
import { MAP_BACKGROUND_COLOR, TYPE_VERTEX } from '@/shared/constants/ds-constants';

const baseConnectomeApiUrl = '/api/connectome';
const getDataUrl = baseConnectomeApiUrl + '/v2/map/';
const getStatusUrl = baseConnectomeApiUrl + '/v2/status/';
const getFavoritesUrl = 'api/favorite-keyword/';
const setEntityAsFavoriteUrl = 'api/favorite-keyword/';
const updateEntityUrl = baseConnectomeApiUrl + '/entity';
const toggleDisableEntityUrl = baseConnectomeApiUrl + '/disable/entity';
const toggleFavoriteEntityUrl = baseConnectomeApiUrl + '/favorite/entity';

const keyStoreStatus = 'connectomeNetworkStatus';
const keyStoreVertices = 'connectomeNetworkVertices';
const keyStoreEdges = 'connectomeNetworkEdges';
const keyStoreMainClusters = 'connectomeMainClusters';
const keyStoreDataVertexColorByMainCluster = 'connectomeNetworkDataVertexColorByMainCluster';
const keyStoreFavorites = 'connectomeNetworkFavorites';

const noData = 'no data';
const noDate = 'no date';
const defaultLangKey = 'en';
const defaultNoColor = '#DEDEDE';
export interface ConnectomeNetworkStorable {
  data: ConnectomeNetwork;
  mainClusters: Array<{ name: string; linksByVertexCount: number; vertexCount: number; maxLinksByVertex: number }>;
  favorites: Array<{ label: string; favoriteId: string; lastModifiedDate: string }>;
  vertexColorByMainCluster: Map<string, d3.RGBColor>;
  labelHidden: any;
}

export const connectomeNetworkStore: Module<ConnectomeNetworkStorable, any> = {
  namespaced: true,
  state: {
    data: convertLocalStorageToConnectomeNetworkData(
      JSON.parse(sessionStorage.getItem(keyStoreStatus)),
      JSON.parse(sessionStorage.getItem(keyStoreVertices)),
      JSON.parse(sessionStorage.getItem(keyStoreEdges))
    ),
    mainClusters: convertLocalStorageToMainClusters(sessionStorage.getItem(keyStoreMainClusters)),
    favorites: new Array<{ label: string; favoriteId: string; lastModifiedDate: string }>(),
    vertexColorByMainCluster: convertLocalStorageToVertexColor(sessionStorage.getItem(keyStoreDataVertexColorByMainCluster)),
    labelHidden: null,
  },
  getters: {
    networkData: state => state.data,
    favorites: state => state.favorites,
    mainClusters: state => state.mainClusters,
    connectomeId: (state, getters) => {
      if (!getters.networkData) {
        return noData;
      }

      if (!getters.networkData.connectomeId) {
        return noData;
      }

      return getters.networkData.connectomeId;
    },
    lastUpdatedAt: (state, getters) => {
      if (!getters.networkData) {
        return noDate;
      }

      if (!getters.networkData.connectomeStatus) {
        return noDate;
      }

      if (!getters.networkData.connectomeStatus.lastUpdatedAt) {
        return noDate;
      }

      return getters.networkData.connectomeStatus.lastUpdatedAt;
    },
    lang: (state, getters) => {
      if (!getters.networkData) {
        return defaultLangKey;
      }

      if (!getters.networkData.connectomeStatus) {
        return defaultLangKey;
      }

      if (!getters.networkData.connectomeStatus.lang) {
        return defaultLangKey;
      }

      return getters.networkData.connectomeStatus.lang;
    },
    connectomeStatus: (state, getters) => {
      if (!getters.networkData) {
        return null;
      }

      if (!getters.networkData.connectomeStatus) {
        return null;
      }
      return getters.networkData.connectomeStatus;
    },
    vertice: (state, getters) => label => {
      if (!label) {
        return null;
      }

      if (!getters.networkData) {
        return null;
      }

      if (!getters.networkData.vertices) {
        return null;
      }

      return getters.networkData.vertices.filter(elem => elem.type != TYPE_VERTEX.ROOT).find(todo => todo.label === label);
    },
    verticeColorByCluster: (state, getters) => mainCluster => {
      if (!mainCluster) {
        return d3.rgb(defaultNoColor);
      }

      if (mainCluster === getters.connectomeId) {
        return d3.rgb(MAP_BACKGROUND_COLOR);
      }

      const value = state.vertexColorByMainCluster.get(mainCluster);
      if (!value) {
        return d3.rgb(defaultNoColor);
      }

      return value;
    },
    vertices: (state, getters) => {
      if (!getters.networkData) {
        return [];
      }

      if (!getters.networkData.vertices) {
        return [];
      }
      return getters.networkData.vertices;
    },
    disableVertices: (state, getters) => {
      if (!getters.networkData) {
        return [];
      }

      if (!getters.networkData.vertices) {
        return [];
      }
      return getters.networkData.vertices.filter(elem => elem.disable && elem.type != TYPE_VERTEX.ROOT);
    },
    listDisableVerticeLabels: (state, getters) => {
      if (!getters.disableVertices) {
        return [];
      }
      const disableLabels: Set<string> = new Set();
      getters.disableVertices.forEach(element => {
        disableLabels.add(element.label);
      });
      return disableLabels;
    },
    edges: (state, getters) => {
      if (!getters.networkData) {
        return [];
      }

      if (!getters.networkData.edges) {
        return [];
      }
      return getters.networkData.edges;
    },
    labelHidden: state => state.labelHidden,
  },
  mutations: {
    setData(state, connectomeData) {
      if (
        !state.data ||
        !state.data.connectomeStatus ||
        !state.data.connectomeStatus.lastUpdatedAt ||
        connectomeData.connectomeStatus.lastUpdatedAt != state.data.connectomeStatus.lastUpdatedAt
      ) {
        state.data = connectomeData;
        sessionStorage.removeItem(keyStoreStatus);
        sessionStorage.setItem(keyStoreStatus, JSON.stringify(state.data.connectomeStatus));
        sessionStorage.removeItem(keyStoreVertices);
        sessionStorage.setItem(keyStoreVertices, JSON.stringify(state.data.vertices));
        sessionStorage.removeItem(keyStoreEdges);
        sessionStorage.setItem(keyStoreEdges, JSON.stringify(state.data.edges));
      }
    },
    setEntityFavorite(state, entity) {
      if (entity) {
        if (state.data && state.data.vertices) {
          state.data.vertices.every(vertice => {
            if (vertice.label === entity.label) {
              vertice.favorite = entity.favorite;
              return false;
            }
            return true;
          });
          const data = state.data;
          state.data = data;
          sessionStorage.removeItem(keyStoreVertices);
          sessionStorage.setItem(keyStoreVertices, JSON.stringify(state.data.vertices));
        }
      }
    },
    setEntityDisable(state, entity) {
      if (entity) {
        if (state.data && state.data.vertices) {
          state.data.vertices.every(vertice => {
            if (vertice.label === entity.label) {
              vertice.disable = entity.disable;
              return false;
            }
            return true;
          });
          const data = state.data;
          state.data = data;
          sessionStorage.removeItem(keyStoreVertices);
          sessionStorage.setItem(keyStoreVertices, JSON.stringify(state.data.vertices));
        }
      }
    },
    putVerticeColorByMainCluster(state, clusterColor: { cluster: string; color: d3.RGBColor }) {
      if (!clusterColor) {
        return;
      }
      if (!clusterColor.cluster) {
        return;
      }

      if (!clusterColor.color) {
        return;
      }

      if (!state.vertexColorByMainCluster) {
        return;
      }

      state.vertexColorByMainCluster.set(clusterColor.cluster, clusterColor.color);
      sessionStorage.removeItem(keyStoreDataVertexColorByMainCluster);
      sessionStorage.setItem(keyStoreDataVertexColorByMainCluster, JSON.stringify(Array.from(state.vertexColorByMainCluster.entries())));
    },
    setMainClusters(state, clusters: Array<{ name: string; linksByVertexCount: number; vertexCount: number; maxLinksByVertex: number }>) {
      if (!clusters) {
        state.mainClusters = Array<{ name: string; linksByVertexCount: number; vertexCount: number; maxLinksByVertex: number }>();
        return;
      }

      state.mainClusters = clusters.map(x => x);
      sessionStorage.removeItem(keyStoreMainClusters);
      sessionStorage.setItem(keyStoreMainClusters, JSON.stringify(state.mainClusters));
    },
    resetData(state) {
      if (sessionStorage.getItem(keyStoreStatus)) {
        sessionStorage.removeItem(keyStoreStatus);
      }
      if (sessionStorage.getItem(keyStoreVertices)) {
        sessionStorage.removeItem(keyStoreVertices);
      }
      if (sessionStorage.getItem(keyStoreEdges)) {
        sessionStorage.removeItem(keyStoreEdges);
      }
      state.data = null;

      if (sessionStorage.getItem(keyStoreFavorites)) {
        sessionStorage.removeItem(keyStoreFavorites);
      }
      state.favorites = new Array<{ label: string; favoriteId: string; lastModifiedDate: string }>();

      if (sessionStorage.getItem(keyStoreDataVertexColorByMainCluster)) {
        sessionStorage.removeItem(keyStoreDataVertexColorByMainCluster);
      }

      if (sessionStorage.getItem(keyStoreMainClusters)) {
        sessionStorage.removeItem(keyStoreMainClusters);
      }
      state.vertexColorByMainCluster = new Map<string, d3.RGBColor>();
    },
    setHiddenState(state, labelHidden: any) {
      state.labelHidden = labelHidden;
    },
  },
  actions: {
    getConnectomeData: async (context, payload: { connectomeId: string; language: string }) => {
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
      console.log('try to get connectome data', payload);
      //payload.connectomeId = 'CID_daaef080-0025-4be1-9ad2-eecb7c908be9';

      const apiCallConnectomeData = new Promise<any>((resolve, reject) => {
        axios
          .get(getDataUrl + payload.connectomeId + '?filterBlackEntity=false&sourceLang=' + language)
          .then(res => resolve(res))
          .catch(err => reject(err));
      });

      return apiCallConnectomeData
        .then(res => {
          if (!res.data.body) {
            return;
          }

          const response = res.data.body;
          console.log('api get connectome reply with', response);
          if (!response.connectomeStatus) {
            return;
          }

          if (!response.connectomeStatus.lastUpdatedAt) {
            return;
          }

          if (!response.connectomeStatus.status) {
            return;
          }

          if (!response.vertices) {
            return;
          }

          if (!response.edges) {
            return;
          }

          const colorCircleBackgroundConstraints = [
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
          const nodePrimaryColor = d3.scaleOrdinal().range([...colorCircleBackgroundConstraints]);

          const newConnectomeStatus = new ConnectomeNetworkStatus(
            response.connectomeStatus.connectomeId,
            response.connectomeStatus.status,
            response.connectomeStatus.lang,
            response.connectomeStatus.whenLearningStarted,
            response.connectomeStatus.lastUpdatedAt
          );

          const newConnectomeVertices: Array<ConnectomeNetworkVertex> = [];
          const newConnectomeEdges: Array<ConnectomeNetworkEdge> = [];

          const clusters = Array<{ name: string; linksByVertexCount: number; vertexCount: number; maxLinksByVertex: number }>();
          response.vertices.forEach(vertex => {
            const newVertex = new ConnectomeNetworkVertex(vertex);
            context.commit('putVerticeColorByMainCluster', {
              cluster: newVertex.mainCluster,
              color: nodePrimaryColor(newVertex.mainCluster),
            });
            const maincluster = clusters.find(elem => elem.name === newVertex.mainCluster);
            if (maincluster) {
              maincluster.vertexCount++;
              maincluster.maxLinksByVertex =
                maincluster.maxLinksByVertex < newVertex.entities.length ? newVertex.entities.length : maincluster.maxLinksByVertex;
              maincluster.linksByVertexCount = maincluster.linksByVertexCount + newVertex.entities.length;
            } else {
              clusters.push({
                name: newVertex.mainCluster,
                linksByVertexCount: newVertex.entities.length,
                vertexCount: 1,
                maxLinksByVertex: newVertex.entities.length,
              });
            }
            newConnectomeVertices.push(newVertex);
          });

          context.commit(
            'setMainClusters',
            clusters.sort((a, b) => a.linksByVertexCount - b.linksByVertexCount)
          );
          response.edges.forEach(edge => newConnectomeEdges.push(new ConnectomeNetworkEdge(edge)));

          const newConnectomeData = new ConnectomeNetwork(
            response.connectomeId,
            newConnectomeStatus,
            [...newConnectomeVertices],
            [...newConnectomeEdges]
          );
          context.commit('setData', newConnectomeData);
        })
        .catch(reason => {
          console.log('Reason', reason);
        })
        .finally(() => {});
    },
    getConnectomeUpdate: async (context, payload: { connectomeId: string; language: string }) => {
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
      //payload.connectomeId = 'CID_daaef080-0025-4be1-9ad2-eecb7c908be9';
      console.log('try get connectome STATUS', payload);
      const apiCallDataStatus = new Promise<any>((resolve, reject) => {
        axios
          .get(getStatusUrl + payload.connectomeId + '?sourceLang=' + language)
          .then(res => resolve(res))
          .catch(err => reject(err));
      });

      return apiCallDataStatus
        .then(res => {
          if (!res.data.body) {
            return;
          }
          const response = res.data.body;
          console.log('api get connectome STATUS reply with', response);
          if (!response.lastUpdatedAt) {
            return;
          }

          if (!response.status) {
            return;
          }

          if (response.lastUpdatedAt != context.getters.lastUpdatedAt) {
            return context.dispatch('getConnectomeData', payload);
          }
        })
        .catch(reason => {
          console.log('Reason', reason);
        })
        .finally(() => {});
    },
    shiftConnectomeByLanguage: async (context, lang: string) => {
      const connectomeId = context.getters.connectomeId;
      if (!lang) {
        return null;
      }

      if (connectomeId === noData) {
        return null;
      }

      return context.dispatch('getConnectomeData', { connectomeId: connectomeId, language: lang });
    },
    toggleEntityFavorite: async (context, payload: { title: string }) => {
      if (!payload) {
        return;
      }

      if (!payload.title) {
        return;
      }

      if (!context.getters.connectomeId) {
        return;
      }

      if (payload) {
        const apiCallFavorites = new Promise<any>((resolve, reject) => {
          axios
            .put(`${toggleFavoriteEntityUrl}`, {
              vertexLabel: payload.title,
              connectomeId: context.getters.connectomeId,
              sourceLang: context.getters.lang,
            })
            .then(res => resolve(res))
            .catch(err => reject(err));
        });
        return apiCallFavorites
          .then(res => {
            if (!res) {
              return;
            }

            if (!res.data) {
              return;
            }

            const response = res.data;
            context.commit('setEntityFavorite', response);
            return res;
          })
          .catch(reason => {
            console.log('Reason', reason);
          })
          .finally(() => {});
      }
    },
    toggleEntityDisability: async (context, payload: { title: string }) => {
      if (!payload) {
        return;
      }

      if (!payload.title) {
        return;
      }

      if (!context.getters.connectomeId) {
        return;
      }

      if (payload) {
        context.commit('setHiddenState', payload.title);
        const apiCallFavorites = new Promise<any>((resolve, reject) => {
          axios
            .put(`${toggleDisableEntityUrl}`, {
              vertexLabel: payload.title,
              connectomeId: context.getters.connectomeId,
              sourceLang: context.getters.lang,
            })
            .then(res => resolve(res))
            .catch(err => reject(err));
        });
        return apiCallFavorites
          .then(res => {
            if (!res) {
              return;
            }

            if (!res.data) {
              return;
            }

            const response = res.data;

            context.commit('setEntityDisable', response);
            return res;
          })
          .catch(reason => {
            console.log('Reason', reason);
          })
          .finally(() => {});
      }
    },
    logout(context) {
      context.commit('resetData');
    },
  },
};

function convertLocalStorageToConnectomeNetworkData(connectomeStatus: any, vertices: any, edges: any) {
  if (!connectomeStatus) {
    return null;
  }

  if (!connectomeStatus.connectomeId) {
    return null;
  }

  if (!connectomeStatus.connectomeStatus) {
    return null;
  }

  if (!vertices) {
    return null;
  }

  if (!edges) {
    return null;
  }

  const res = new ConnectomeNetwork(connectomeStatus.connectomeStatus.connectomeId, connectomeStatus.connectomeStatus, vertices, edges);
  return res;
}

function convertLocalStorageToVertexColor(obj: any) {
  if (!obj) {
    return new Map<string, d3.RGBColor>();
  }

  const map = new Map<string, d3.RGBColor>(JSON.parse(obj));
  if (!map) {
    return new Map<string, d3.RGBColor>();
  }
  return map;
}

function convertLocalStorageToMainClusters(obj: any) {
  if (!obj) {
    return new Array<{ name: string; linksByVertexCount: number; vertexCount: number; maxLinksByVertex: number }>();
  }

  const map = JSON.parse(obj);
  if (!map) {
    return new Array<{ name: string; linksByVertexCount: number; vertexCount: number; maxLinksByVertex: number }>();
  }

  return map;
}
