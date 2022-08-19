import { ConnectomeNetworkEdge } from '@/shared/model/connectome-network-edge.model';
import { ConnectomeNetwork } from '@/shared/model/connectome-network.model';
import { ConnectomeNetworkStatus as ConnectomeNetworkStatus } from '@/shared/model/connectome-network-status.model';
import { ConnectomeNetworkVertex } from '@/shared/model/connectome-network-vertex.model';

import axios from 'axios';
import { Module } from 'vuex';
import { Getter } from 'vuex-class';
import * as d3 from 'd3';
import { MINI_MAP_BACKGROUND_COLOR, TYPE_VERTEX } from '@/shared/constants/ds-constants';

const baseConnectomeApiUrl = '/api/connectome';
const getDataUrl = baseConnectomeApiUrl + '/mini-map/';

const keyStoreConnectomeId = 'miniConnectomeNetworkId';
const keyStoreVertices = 'miniConnectomeNetworkVertices';
const keyStoreEdges = 'miniConnectomeNetworkEdges';
const keyStoreIds = 'miniConnectomeNetworkIds';

export interface MiniConnectomeNetworkStorable {
  data: ConnectomeNetwork;
  ids: Array<string>;
}

export const miniConnectomeNetworkStore: Module<MiniConnectomeNetworkStorable, any> = {
  namespaced: true,
  state: {
    data: convertLocalStorageToMiniConnectomeNetworkData(
      sessionStorage.getItem(keyStoreConnectomeId),
      JSON.parse(sessionStorage.getItem(keyStoreVertices)),
      JSON.parse(sessionStorage.getItem(keyStoreEdges))
    ),
    ids: convertLocalStorageToArrayString(sessionStorage.getItem(keyStoreIds)),
  },
  getters: {
    miniNetworkData: state => state.data,
    miniIds: state => state.ids,
    miniConnectomeId: (state, getters) => {
      if (!getters.miniNetworkData) {
        return 'no data';
      }

      if (!getters.miniNetworkData.connectomeId) {
        return 'no data';
      }

      return getters.miniNetworkData.connectomeId;
    },
    miniVertice: (state, getters) => label => {
      if (!label) {
        return null;
      }

      if (!getters.miniNetworkData) {
        return null;
      }

      if (!getters.miniNetworkData.vertices) {
        return null;
      }

      return getters.miniNetworkData.vertices.filter(elem => elem.type != TYPE_VERTEX.ROOT).find(todo => todo.label === label);
    },
    miniVertices: (state, getters) => {
      if (!getters.miniNetworkData) {
        return [];
      }
      if (!getters.miniNetworkData.vertices) {
        return [];
      }
      return getters.miniNetworkData.vertices;
    },
    miniDisableVertices: (state, getters) => {
      if (!getters.miniNetworkData) {
        return [];
      }

      if (!getters.miniNetworkData.vertices) {
        return [];
      }
      return getters.miniNetworkData.vertices.filter(
        elem => elem.disable && elem.type != TYPE_VERTEX.ROOT && elem.type != TYPE_VERTEX.CLUSTER
      );
    },
    miniListDisableVerticeLabels: (state, getters) => {
      if (!getters.miniDisableVertices) {
        return [];
      }
      const disableLabels: Set<string> = new Set();
      getters.miniDisableVertices.forEach(element => {
        disableLabels.add(element.label);
      });
      return disableLabels;
    },
    miniEdges: (state, getters) => {
      if (!getters.miniNetworkData) {
        return [];
      }

      if (!getters.miniNetworkData.edges) {
        return [];
      }
      return getters.miniNetworkData.edges;
    },
  },
  mutations: {
    setData(state, connectomeData) {
      console.log('insert in right state');
      state.data = connectomeData;
      sessionStorage.removeItem(keyStoreConnectomeId);
      sessionStorage.setItem(keyStoreConnectomeId, JSON.stringify(state.data.connectomeStatus));
      sessionStorage.removeItem(keyStoreVertices);
      sessionStorage.setItem(keyStoreVertices, JSON.stringify(state.data.vertices));
      sessionStorage.removeItem(keyStoreEdges);
      sessionStorage.setItem(keyStoreEdges, JSON.stringify(state.data.edges));
    },
    setIds(state, ids) {
      if (ids && ids.length > 0) {
        state.ids = ids;
        sessionStorage.removeItem(keyStoreIds);
        sessionStorage.setItem(keyStoreIds, JSON.stringify(state.ids));
      } else {
        sessionStorage.removeItem(keyStoreIds);
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
    resetData(state) {
      if (sessionStorage.getItem(keyStoreConnectomeId)) {
        sessionStorage.removeItem(keyStoreConnectomeId);
      }
      if (sessionStorage.getItem(keyStoreVertices)) {
        sessionStorage.removeItem(keyStoreVertices);
      }
      if (sessionStorage.getItem(keyStoreEdges)) {
        sessionStorage.removeItem(keyStoreEdges);
      }
      state.data = null;

      if (sessionStorage.getItem(keyStoreIds)) {
        sessionStorage.removeItem(keyStoreIds);
      }
      state.ids = [];
    },
  },
  actions: {
    getMiniConnectomeData: async (
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
      console.log('mini getMiniConnectomeData', {
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

      if (shouldContinue) {
        context.commit('setIds', payload.ids);
      } else {
        if (!context.getters.miniIds) {
          return;
        }

        if (context.getters.miniIds.length == 0) {
          return;
        }
      }

      const apiCallConnectomeData = new Promise<any>((resolve, reject) => {
        axios
          .post(getDataUrl + payload.connectomeId, {
            sourceLang: language,
            ids: context.getters.miniIds,
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
          console.log('getMiniConnectomeData', response);

          if (!response.vertices) {
            return;
          }

          if (!response.edges) {
            return;
          }

          const newConnectomeVertices: Array<ConnectomeNetworkVertex> = [];
          const newConnectomeEdges: Array<ConnectomeNetworkEdge> = [];

          const clusters = Array<{ name: string; linksByVertexCount: number; vertexCount: number; maxLinksByVertex: number }>();
          response.vertices.forEach(vertex => {
            const mainVertex = context.rootGetters['connectomeNetworkStore/vertice'](vertex.label);
            const newVertex = new ConnectomeNetworkVertex(vertex);
            if (mainVertex) {
              newVertex.mainCluster = mainVertex.mainCluster;
            } else {
              newVertex.mainCluster = null;
            }
            newConnectomeVertices.push(newVertex);
          });

          response.edges.forEach(edge => newConnectomeEdges.push(new ConnectomeNetworkEdge(edge)));

          const newConnectomeData = new ConnectomeNetwork(response.connectomeId, null, [...newConnectomeVertices], [...newConnectomeEdges]);
          context.commit('setData', newConnectomeData);

          return res;
        })
        .catch(reason => {
          console.log('Reason', reason);
          console.log('Reason mini getMiniConnectomeData', {
            sourceLang: language,
            feedIds: payload.ids,
          });
          context.commit('resetData');
        })
        .finally(() => {});
    },
    // toggleEntityFavorite: async (context, payload: { title: string }) => {
    //   if (!payload) {
    //     return;
    //   }

    //   if (!payload.title) {
    //     return;
    //   }

    //   if (!context.getters.miniConnectomeId) {
    //     return;
    //   }

    //   const lang = context.rootGetters['connectomeNetworkStore/lang'];
    //   if (!lang) {
    //     return;
    //   }
    //   if (payload) {
    //     console.log('mini toggleEntityFavorite', {
    //       vertexLabel: payload.title,
    //       connectomeId: context.getters.miniConnectomeId,
    //       sourceLang: lang,
    //     });
    //     const apiCallFavorites = new Promise<any>((resolve, reject) => {
    //       axios
    //         .put(`${toggleFavoriteEntityUrl}`, {
    //           vertexLabel: payload.title,
    //           connectomeId: context.getters.miniConnectomeId,
    //           sourceLang: lang,
    //         })
    //         .then(res => resolve(res))
    //         .catch(err => reject(err));
    //     });
    //     return apiCallFavorites
    //       .then(res => {
    //         if (!res) {
    //           return;
    //         }

    //         if (!res.data) {
    //           return;
    //         }

    //         const response = res.data;
    //         context.commit('setEntityFavorite', response);
    //         return res;
    //       })
    //       .catch(reason => {
    //         console.log('Reason', reason);
    //       })
    //       .finally(() => {});
    //   }
    // },
    // toggleEntityDisability: async (context, payload: { title: string }) => {
    //   if (!payload) {
    //     return;
    //   }

    //   if (!payload.title) {
    //     return;
    //   }

    //   if (!context.getters.miniConnectomeId) {
    //     return;
    //   }
    //   const lang = context.rootGetters['connectomeNetworkStore/lang'];
    //   if (!lang) {
    //     return;
    //   }

    //   if (payload) {
    //     console.log('mini toggleEntityDisability', {
    //       vertexLabel: payload.title,
    //       connectomeId: context.getters.miniConnectomeId,
    //       sourceLang: lang,
    //     });
    //     const apiCallFavorites = new Promise<any>((resolve, reject) => {
    //       axios
    //         .put(`${toggleDisableEntityUrl}`, {
    //           vertexLabel: payload.title,
    //           connectomeId: context.getters.miniConnectomeId,
    //           sourceLang: lang,
    //         })
    //         .then(res => resolve(res))
    //         .catch(err => reject(err));
    //     });
    //     return apiCallFavorites
    //       .then(res => {
    //         if (!res) {
    //           return;
    //         }

    //         if (!res.data) {
    //           return;
    //         }

    //         const response = res.data;

    //         context.commit('setEntityDisable', response);
    //         return res;
    //       })
    //       .catch(reason => {
    //         console.log('Reason', reason);
    //       })
    //       .finally(() => {});
    //   }
    // },
    logout(context) {
      context.commit('resetData');
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
