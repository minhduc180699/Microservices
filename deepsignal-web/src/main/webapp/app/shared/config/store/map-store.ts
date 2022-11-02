import { ConnectomeNetwork } from '@/shared/model/connectome-network.model';

import { ConnectomeLink } from '@/shared/model/connectome-link.model';
import { ConnectomeNode } from '@/shared/model/connectome-node.model';
import { MapNode } from '@/shared/model/map-node.model';
import axios from 'axios';
import { Module } from 'vuex';
import { MapLink } from '@/shared/model/map-link.model';

const baseConnectomeApiUrl = '/api/connectome';
const getDataUrl = baseConnectomeApiUrl + '/pd-map/';
const getTextDataUrl = baseConnectomeApiUrl + '/text-map/';
const getDocGraphUrl = baseConnectomeApiUrl + '/doc-graph/';

const keyStorePdList = 'ConnectomeBuilderPdIds';
const keyStoreStorageList = 'ConnectomeBuilderStorageList';
const keyStoreNodesPrefix = 'ConnectomeBuilderNodes_';

const defaultLangKey = 'en';
const defaultNoColor = '#DEDEDE';

export interface MapStorable {
  nodesSelected: Array<MapNode>;
  nodesLinkedSelected: Set<string>;
  nodes: Array<MapNode>;
  links: Array<MapLink>;
  nodesSelectedChanged: number;
  nodesChanged: number;
  linksChanged: number;
  mapChanged: number;
}

export const mapStore: Module<MapStorable, any> = {
  namespaced: true,
  state: {
    nodesSelected: new Array<MapNode>(),
    nodesLinkedSelected: new Set<string>(),
    nodes: new Array<MapNode>(),
    links: new Array<MapLink>(),
    nodesSelectedChanged: 0,
    nodesChanged: 0,
    linksChanged: 0,
    mapChanged: 0,
  },
  getters: {
    getNodesSelected: state => state.nodesSelected,
    getNodesLinkedSelected: state => state.nodesLinkedSelected,
    getNodes: state => state.nodes,
    getLinks: state => state.links,
    nodesSelectedHaveChanged: state => state.nodesSelectedChanged,
    nodesHaveChanged: state => state.nodesChanged,
    linksHaveChanged: state => state.linksChanged,
    mapHaveChanged: state => state.mapChanged,
  },
  mutations: {
    setNodes(state, payload: { newNodes: Array<MapNode> }) {
      if (!payload) {
        return;
      }

      if (!payload.newNodes || payload.newNodes.length < 1) {
        state.nodes = new Array<MapNode>();
      }

      state.nodes = payload.newNodes.map(x => x);
      state.nodesChanged++;
      state.mapChanged++;
    },
    setLinks(state, payload: { newLinks: Array<MapLink> }) {
      if (!payload) {
        return;
      }

      if (!payload.newLinks || payload.newLinks.length < 1) {
        state.links = new Array<MapLink>();
        return;
      }

      state.links = payload.newLinks.map(x => x);
      state.linksChanged++;
      state.mapChanged++;
    },
    setNodesSelected(state, payload: { newNodesSelected: Array<MapNode> }) {
      if (!payload) {
        return;
      }

      if (!payload.newNodesSelected || payload.newNodesSelected.length < 1) {
        state.nodesSelected = new Array<MapNode>();
        state.nodesLinkedSelected = new Set<string>();
        return;
      }

      state.nodesSelected = payload.newNodesSelected.map(x => x);
      state.nodesLinkedSelected = new Set<string>();
      payload.newNodesSelected.forEach(newNode => {
        if (newNode.nodesLinked?.length > 0) {
          newNode.nodesLinked.forEach(nodeLinked => {
            state.nodesLinkedSelected.add(nodeLinked);
          });
        }
      });

      state.nodesSelectedChanged++;
    },
    resetMapData(state) {
      state.nodesSelected = new Array<MapNode>();
      state.nodesLinkedSelected = new Set<string>();
      state.nodes = new Array<MapNode>();
      state.links = new Array<MapLink>();
      state.nodesSelectedChanged = 0;
      state.nodesChanged = 0;
      state.linksChanged = 0;
      state.mapChanged = 0;
      console.log('map reset');
    },
  },
  actions: {},
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
