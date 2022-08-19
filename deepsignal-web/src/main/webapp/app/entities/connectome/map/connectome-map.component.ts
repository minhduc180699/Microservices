import { Vue, Watch } from 'vue-property-decorator';
import * as d3 from 'd3';
import * as d3ScaleChromatic from 'd3-scale-chromatic';
import { NetworkNode } from './network-node-model';
import { TYPE_CONNECTOME_MAP_DATA } from '@/shared/constants/ds-constants';
import { MapNode } from './map-node-model';
import { ConnectomeNode } from './connectome-node-model';

// @Component({
//   components: {
//     'map-by-link': ConnectomeMapByLink,
//     'map-3d-by-link': ConnectomeMap3dByLink,
//     'ds-search-dropdown': DsSearchDropdownComponent,
//     ConnectomeMapSideBySide,
//   },
// })
export default class ConnectomeMapComponent extends Vue {
  connectomeId = '';

  // @Inject('connectomeService')
  // private connectomeService: () => ConnectomeService;

  // topics: Array<any> = [];
  // entities: Array<any> = [];
  // entityById: Map<string, any> = null;

  // networkNodesNewValues: Array<NetworkNode> = [];
  // networkLinksNewValues: Array<NetworkLink> = [];

  // networkNodes3dNewValues: Array<NetworkNode> = [];
  // networkLinks3dNewValues: Array<NetworkLink> = [];

  // mapNodesNewValues: Array<MapNode> = [];
  // mapLinksNewValues: Array<MapLink> = [];

  // networkNodes: Array<NetworkNode> = [];
  // networkLinks: Array<NetworkLink> = [];

  // networkNodes3d: Array<NetworkNode> = [];
  // networkLinks3d: Array<NetworkLink> = [];

  // mapNodes: Array<MapNode> = [];
  // mapLinks: Array<MapLink> = [];

  // connectomeNetwork: {
  //   connectomeId: string;
  //   nodes: Array<NetworkNode>;
  //   links: Array<NetworkLink>;
  // } = {
  //   connectomeId: null,
  //   nodes: [],
  //   links: [],
  // };

  // connectomeNetwork3d: {
  //   connectomeId: string;
  //   nodes: Array<NetworkNode>;
  //   links: Array<NetworkLink>;
  // } = {
  //   connectomeId: null,
  //   nodes: [],
  //   links: [],
  // };

  // connectomeMap: {
  //   connectomeId: string;
  //   dataset: Object;
  //   color: any;
  //   colorLabel: any;
  // } = {
  //   connectomeId: null,
  //   dataset: null,
  //   color: null,
  //   colorLabel: null,
  // };

  //listNodesByIdAndLabel: Array<{ id: NetworkNode; name: string }> = [];

  fullscreen = false;
  private width = 350;
  private height = 460;

  //build the data
  mounted() {
    document.body.setAttribute('data-menu', 'connectome');
    //this.connectomeId = this.$route.params.connectomeId;
    const User = JSON.parse(localStorage.getItem('ds-connectome'));
    if (!User?.connectomeId) {
      return;
    } else {
      this.connectomeId = User.connectomeId;
    }

    console.log('mounted map-connectome', User);

    window.onresize = this.resizeFullscreen;
    this.fullscreen =
      this.$route.query.fullscreen === 'TRUE' || this.$route.query.fullscreen === 'true' || this.$route.query.fullscreen === '1';

    //this.connectomeService().startConnectomeMapDataJob(this.connectomeId, 'connectome-map', this.onConnectomeDataChanged);
  }

  private sideBarTop: number = 150;

  private resizeFullscreen() {
    //console.log('resize event detected!');
    if (this.fullscreen) {
      //this.containerCSS = $('#container-block').css
      document.body.setAttribute('data-menu', 'connectome-fullscreen');
      this.sideBarTop = $('.panel-sidebar').position().top;
      $('.panel-sidebar').css({ top: 0 });
      this.width = window.innerWidth;
      this.height = Math.max($('#app').height(), window.innerHeight);
      //resize child?
    } else {
      document.body.setAttribute('data-menu', 'connectome');
      $('.panel-sidebar').css({ top: this.sideBarTop > 100 ? this.sideBarTop : 150 });
    }
    this.$children.forEach(child => {
      if (child.hasOwnProperty('chartResize')) {
        // @ts-ignore
        child.chartResize();
      }
    });
  }

  setFullScreen() {
    this.fullscreen = !this.fullscreen;
    this.resizeFullscreen();
  }

  // private getCurrentMapNodeById<Type>(topic: any): MapNode | undefined {
  //   let nodeResult = null;
  //   this.mapNodes.every(node => {
  //     if (node.id === topic.topic_id) {
  //       nodeResult = node;
  //       return false;
  //     }
  //     return true;
  //   });

  //   if (!nodeResult) {
  //     //console.log('current map node not found', topic.topic_id);
  //     nodeResult = new MapNode(topic);
  //   }
  //   return nodeResult;
  // }

  // private getMapNodeNewValueById<Type>(id): MapNode | undefined {
  //   let nodeResult = null;
  //   this.mapNodesNewValues.every(node => {
  //     if (node.id === id) {
  //       nodeResult = node;
  //       return false;
  //     }
  //     return true;
  //   });

  //   if (!nodeResult) {
  //     //console.log('new map node not found', id);
  //   }
  //   return nodeResult;
  // }

  // private getMapLink<Type>(link: any, targetNode: MapNode): MapLink | undefined {
  //   let mapLink: MapLink = null;
  //   // this.mapLinks.every(mlink => {
  //   //   if (mlink.source === link.source && mlink.target === link.target) {
  //   //     mapLink = mlink;
  //   //     return false;
  //   //   }
  //   //   return true;
  //   // });

  //   // if (!mapLink) {
  //   //   //console.log('current map link not found', link.source, link.target);
  //   //   mapLink = new MapLink(link, targetNode);
  //   // }

  //   return mapLink;
  // }

  // private getNetworkNode<type>(element: any, color: any, colorLabel: any, is3D: boolean): NetworkNode | undefined {
  //   let netNode: NetworkNode = null;
  //   // if (is3D) {
  //   //   this.networkNodes3d.every(nNode => {
  //   //     if (nNode.id === element.id) {
  //   //       netNode = nNode;
  //   //       netNode.update(element, color, colorLabel);
  //   //       return false;
  //   //     }
  //   //     return true;
  //   //   });
  //   // } else {
  //   //   this.networkNodes.every(nNode => {
  //   //     if (nNode.id === element.id) {
  //   //       netNode = nNode;
  //   //       netNode.update(element, color, colorLabel);
  //   //       return false;
  //   //     }
  //   //     return true;
  //   //   });
  //   // }
  //   // if (!netNode) {
  //   //   //console.log('current network node not found', element.id);
  //   //   netNode = new NetworkNode(element, color, colorLabel);
  //   // }
  //   return netNode;
  // }

  // private getNetworkLink<type>(element: any, sourceNode: NetworkNode, targetNode: NetworkNode, is3D: boolean): NetworkLink | undefined {
  //   // let netLink: NetworkLink = null;
  //   // if (is3D) {
  //   //   this.networkLinks3d.every(nLink => {
  //   //     if (nLink.id === element.from + '=>' + element.to) {
  //   //       netLink = nLink;
  //   //       netLink.update(sourceNode, targetNode);
  //   //       return false;
  //   //     }
  //   //     return true;
  //   //   });
  //   // } else {
  //   //   this.networkLinks.every(nLink => {
  //   //     if (nLink.id === element.from + '=>' + element.to) {
  //   //       netLink = nLink;
  //   //       netLink.update(sourceNode, targetNode);
  //   //       return false;
  //   //     }
  //   //     return true;
  //   //   });
  //   // }

  //   // if (!netLink) {
  //   //   //console.log('current network link node not found', element);
  //   //   netLink = new NetworkLink(element, sourceNode, targetNode);
  //   // }

  //   // return netLink;
  // }

  private defineLevelForEachMapNode(currentNode: MapNode, level: number) {
    // currentNode.level = level;
    // if (currentNode.parent && currentNode.parent.level > 0) {
    //   currentNode._color = currentNode.parent._color;
    //   currentNode._textColor = currentNode.parent._textColor;
    //   currentNode.path = [...currentNode.parent.path];
    // }
    // currentNode.path.push(currentNode);
    // if (currentNode.children) {
    //   currentNode.children.forEach(node => {
    //     this.defineLevelForEachMapNode(node, level + 1);
    //   });
    // }
  }

  private updateMapData(newMapData: any, newNetworkData: any) {
    if (!newMapData) return;
    if (!newNetworkData) return;

    const colorCircleBackgroundConstraints = [];
    const colorCircleTextBorderConstraints = [];
    for (let i = 0; i <= 1; i += 0.04) {
      colorCircleBackgroundConstraints.push(d3ScaleChromatic.interpolateRainbow(i));
      colorCircleTextBorderConstraints.push(d3ScaleChromatic.interpolateRainbow(i));
    }
    const color = d3.scaleOrdinal().range([...colorCircleBackgroundConstraints]);
    const colorLabel = d3.scaleOrdinal().range([...colorCircleTextBorderConstraints]);

    // this.mapNodesNewValues = [];
    // this.mapLinksNewValues = [];

    // this.topics = newMapData.topics;
    // this.entities = newMapData.entities;

    // this.entityById = new Map();
    // if (this.entities) {
    //   this.entities.forEach(element => {
    //     this.entityById.set(element.entity_id, element);
    //   });
    // }

    // const links = newMapData.edges;
    // this.topics.forEach(topic => {
    //   let node = this.getCurrentMapNodeById(topic);
    //   node.children = [];
    //   node.path = [];
    //   if (node.Entities) {
    //     node.Entities = null;
    //   }
    //   if (topic.entities && topic.entities.length === 1) {
    //     node.Entities = [];
    //     topic.entities.forEach(entity => {
    //       node.Entities.push(this.entityById.get(entity));
    //     });
    //   }
    //   this.mapNodesNewValues.push(node);
    // });

    // // Create nodes for each unique source and target.
    // links.forEach(link => {
    //   const nodeCurrent = this.getMapNodeNewValueById(link.source);
    //   const nodeChild = this.getMapNodeNewValueById(link.target);
    //   nodeChild.parent = nodeCurrent;
    //   nodeCurrent.type = nodeCurrent.type === TYPE_NODE_IN_MAP.CLUSTER ? TYPE_NODE_IN_MAP.TOPIC : nodeCurrent.type;
    //   nodeCurrent._size = nodeCurrent.type != TYPE_NODE_IN_MAP.ENTITY ? SIZE_NODE_IN_MAP.TOPIC : nodeCurrent._size;
    //   nodeCurrent.fontSize = nodeCurrent.type != TYPE_NODE_IN_MAP.ENTITY ? FONTSIZE_NODE_IN_MAP.TOPIC : nodeCurrent.fontSize;
    //   if (nodeCurrent.children) {
    //     nodeCurrent.children.push(nodeChild);
    //   } else {
    //     nodeCurrent.children = [nodeChild];
    //   }
    //   this.mapLinksNewValues.push(this.getMapLink(link, nodeChild));
    // });

    // if (this.mapNodesNewValues && this.mapNodesNewValues.length > 0) {
    //   this.mapNodesNewValues[0].type = TYPE_NODE_IN_MAP.ROOT;
    //   this.mapNodesNewValues[0]._size = SIZE_NODE_IN_MAP.ROOT;
    //   this.mapNodesNewValues[0].fontSize = FONTSIZE_NODE_IN_MAP.ROOT;
    //   // @ts-ignore
    //   this.mapNodesNewValues[0]._color = color(this.mapNodesNewValues[0].name);
    //   this.mapNodesNewValues[0].level = 0;
    //   if (this.mapNodesNewValues[0].children && this.mapNodesNewValues[0].children.length > 0) {
    //     this.mapNodesNewValues[0].children.forEach(maintopic => {
    //       maintopic.type = TYPE_NODE_IN_MAP.MAIN_TOPIC;
    //       maintopic._size = SIZE_NODE_IN_MAP.MAIN_TOPIC;
    //       maintopic.fontSize = FONTSIZE_NODE_IN_MAP.MAIN_TOPIC;
    //       // @ts-ignore
    //       maintopic._color = color(maintopic.name);
    //     });
    //   }
    //   this.defineLevelForEachMapNode(this.mapNodesNewValues[0], 0);
    // }

    // this.networkNodesNewValues = [];
    // this.networkNodes3dNewValues = [];
    // this.networkLinksNewValues = [];
    // this.networkLinks3dNewValues = [];
    // this.listNodesByIdAndLabel = [];

    // //check nodes exist
    // const nodesIds: Map<string, NetworkNode> = new Map<string, NetworkNode>();
    // const nodesIds3d: Map<string, NetworkNode> = new Map<string, NetworkNode>();
    // if (newNetworkData.elements) {
    //   newNetworkData.elements.forEach(element => {
    //     const node = this.getNetworkNode(element, color, colorLabel, false);
    //     const node3d = this.getNetworkNode(element, color, colorLabel, true);

    //     this.networkNodesNewValues.push(node);
    //     this.networkNodes3dNewValues.push(node3d);

    //     this.listNodesByIdAndLabel.push({
    //       id: node,
    //       name: node.name,
    //     });
    //     nodesIds.set(element.id, node);
    //     nodesIds3d.set(element.id, node3d);
    //   });
    // }

    // if (newNetworkData.edges) {
    //   newNetworkData.edges.forEach(edge => {
    //     if (!nodesIds.has(edge.to)) {
    //       return;
    //     }

    //     if (!nodesIds.has(edge.from)) {
    //       return;
    //     }
    //     const sourceNode = nodesIds.get(edge.from);
    //     const targetNode = nodesIds.get(edge.to);

    //     const sourceNode3d = nodesIds3d.get(edge.from);
    //     const targetNode3d = nodesIds3d.get(edge.to);

    //     const link = this.getNetworkLink(edge, sourceNode, targetNode, false);
    //     const link3d = this.getNetworkLink(edge, sourceNode3d, targetNode3d, true);
    //     this.networkLinksNewValues.push(link);
    //     this.networkLinks3dNewValues.push(link3d);
    //   });
    // }

    // this.mapNodes = this.mapNodesNewValues.map(x => x);
    // this.mapLinks = this.mapLinksNewValues.map(x => x);
    // this.networkNodes = this.networkNodesNewValues.map(x => x);
    // this.networkNodes3d = this.networkNodes3dNewValues.map(x => x);
    // this.networkLinks = this.networkLinksNewValues.map(x => x);
    // this.networkLinks3d = this.networkLinks3dNewValues.map(x => x);

    // this.connectomeNetwork = {
    //   connectomeId: this.connectomeId,
    //   nodes: this.networkNodes,
    //   links: this.networkLinks,
    // };
    // //console.log('connectomeNetwork', this.connectomeNetwork);

    // this.connectomeNetwork3d = {
    //   connectomeId: this.connectomeId,
    //   nodes: this.networkNodes3d,
    //   links: this.networkLinks3d,
    // };
    // //console.log('connectomeNetwork3d', this.connectomeNetwork3d);

    // // Extract the root node.
    // // this.connectomeMap = {
    // //   connectomeId: this.connectomeId,
    // //   dataset: dataset,
    // //   color: color,
    // //   colorLabel: colorLabel,
    // // };
    // //console.log('connectomeNetworkMap', this.connectomeMap);
    // //console.log('connectomeTree', this.connectomeTree);
  }
  /////////////////////////////////////////////////////////////////////////////////////////////
  //
  //INTERACTIONS WITH CHILDREN
  //
  /////////////////////////////////////////////////////////////////////////////////////////////

  getConnectomeData(dataType: TYPE_CONNECTOME_MAP_DATA) {
    // let apiCallConnectomeData: '' | Promise<AxiosResponse<any>> = '';
    // switch (dataType) {
    //   case TYPE_CONNECTOME_MAP_DATA.MAP:
    //     apiCallConnectomeData = this.connectomeService().getConnectomeUpdatedMap(this.connectomeId);
    //     break;
    //   case TYPE_CONNECTOME_MAP_DATA.NETWORK:
    //     apiCallConnectomeData = this.connectomeService().getConnectomeUpdatedNetworkMap(this.connectomeId);
    //     break;
    //   default:
    //     return;
    // }
    // if (!apiCallConnectomeData) {
    //   return;
    // }
    // apiCallConnectomeData.then(res => {
    //   if (!res.data) {
    //     return;
    //   }
    //   console.log(res.data);
    // });
  }

  zoomIn() {
    // this.$children.forEach(child => {
    //   if (child.hasOwnProperty('zoomIn')) {
    //     // @ts-ignore
    //     child.zoomIn();
    //   }
    // });
  }

  zoomOut() {
    // this.$children.forEach(child => {
    //   if (child.hasOwnProperty('zoomIn')) {
    //     // @ts-ignore
    //     child.zoomOut();
    //   }
    // });
  }

  fitScreen() {
    // this.$children.forEach(child => {
    //   if (child.hasOwnProperty('fitScreen')) {
    //     // @ts-ignore
    //     child.fitScreen();
    //   }
    // });
  }

  expandNodes() {
    this.$children.forEach(child => {
      if (child.hasOwnProperty('expandNodes')) {
        // @ts-ignore
        child.expandNodes();
      }
    });
  }

  contractNodes() {
    // this.$children.forEach(child => {
    //   if (child.hasOwnProperty('contractNodes')) {
    //     // @ts-ignore
    //     child.contractNodes();
    //   }
    // });
  }

  onTopicDetailOpen(event: any) {
    // if (!this.$data.focusNode) {
    //   this.onTopicSearchOpen(event);
    //   return;
    // }
    // event?.preventDefault();
    // $('.connectome-detail').addClass('active');
  }

  onTopicDetailClose(event: any) {
    // event?.preventDefault();
    // $('.connectome-detail').removeClass('active');
  }

  onTopicSearchOpen(event: any) {
    // event?.preventDefault();
    // $('.connectome-search').addClass('active');
  }

  onTopicSearchClose(event: any) {
    // event?.preventDefault();
    // $('.connectome-search').removeClass('active');
  }

  //left click on charts
  leftClickOnMapNode(node: MapNode) {
    // if (!node) {
    //   return;
    // }
    // //console.log('leftClickOnMapNode', node);
    // this.focusOnNode(node, TYPE_CONNECTOME_MAP_DATA.MAP);
  }

  leftClickOnNetworkNode(node: NetworkNode) {
    // if (!node) {
    //   return;
    // }
    // console.log('leftClickOnNetworkNode', node);
    // this.focusOnNode(node, TYPE_CONNECTOME_MAP_DATA.NETWORK);
  }

  //Right click on charts
  rightClickOnMapNode(node: MapNode) {
    // if (!node) {
    //   return;
    // }
  }

  rightClickOnNetworkNode(node: NetworkNode) {
    // if (!node) {
    //   return;
    // }
  }

  //focus on the node
  focusFromNetwork(node: NetworkNode) {
    // this.focusOnNode(node, TYPE_CONNECTOME_MAP_DATA.NETWORK);
  }

  focusFromMap(node: MapNode) {
    // this.focusOnNode(node, TYPE_CONNECTOME_MAP_DATA.MAP);
  }

  focusOnNode(node: any, typeData: TYPE_CONNECTOME_MAP_DATA) {
    // const targetNode: ConnectomeNode =
    //   typeData === TYPE_CONNECTOME_MAP_DATA.NETWORK ? this.convertNetworkNodeToTargetNode(node) : this.convertMapNodeToTargetNode(node);
    // console.log('node id clicked on connectome Network', targetNode);
    // if (targetNode) {
    //   //console.log(this);
    //   this.$data.focusNode = targetNode;
    //   console.log('stored focused node', localStorage.getItem('ds-selectedConnectomeNode'));
    // }
  }

  // private convertNetworkNodeToTargetNode<Type>(node: NetworkNode): ConnectomeNode | undefined {
  //   // if (!node) return null;

  //   // const targetNode = new ConnectomeNode();
  //   // targetNode.origin = TYPE_CONNECTOME_MAP_DATA.NETWORK;
  //   // targetNode.type = node.type;
  //   // targetNode.label = node.name;
  //   // targetNode.id = node.id;
  //   // targetNode.parents = [];

  //   // return targetNode;
  // }

  // private convertMapNodeToTargetNode<Type>(node: MapNode): ConnectomeNode | undefined {
  //   // if (!node) return null;

  //   // const targetNode = new ConnectomeNode();
  //   // targetNode.origin = TYPE_CONNECTOME_MAP_DATA.MAP;
  //   // targetNode.type = node.type;
  //   // targetNode.label = node.name;
  //   // targetNode.id = node.id;
  //   // targetNode.parents = [node.parent];

  //   // return targetNode;
  // }

  //find a node by the search bar
  validateSelection(value: { id: any; name: string }) {
    // if (value && value.hasOwnProperty('id')) {
    //   this.focusOnNode(value.id, TYPE_CONNECTOME_MAP_DATA.NETWORK);
    // }
  }

  clearSelection() {
    // this.$data.focusNode = null;
    // console.log('stored focused node', localStorage.getItem('ds-selectedConnectomeNode'));
  }

  networkMapInitiate() {
    // if (!this.$data.isNetworkMapInitiate) {
    //   this.$data.isNetworkMapInitiate = true;
    //   console.log('stored focused node', localStorage.getItem('ds-selectedConnectomeNode'));
    //   const favoriteKeyword = localStorage.getItem('ds-favorite');
    //   if (favoriteKeyword) {
    //     for (let node of this.listNodesByIdAndLabel) {
    //       if (node['name'] === favoriteKeyword) {
    //         const newFocusNode: ConnectomeNode = this.convertNetworkNodeToTargetNode(node.id);
    //         localStorage.setItem('ds-selectedConnectomeNode', JSON.stringify(newFocusNode));
    //         localStorage.removeItem('ds-favorite');
    //         break;
    //       }
    //     }
    //   }
    //   const savedNode = this.recoverFocusNodeObject(JSON.parse(localStorage.getItem('ds-selectedConnectomeNode')));
    //   if (!savedNode || !savedNode.id || !savedNode.type || !savedNode.label) {
    //     this.$data.focusNode = null;
    //   } else {
    //     this.$data.focusNode = savedNode;
    //   }
    // }
  }

  //topic bar

  topicBarMapNodeSelected: MapNode = null;

  topicBarNetworkTopicFocused: NetworkNode = null;
  topicBarNetworkEntityFocused: NetworkNode = null;

  topicBarTitle: string = null;
  topicBarImageUrl: string = null;

  topicBarDescriptionShouldBeDisplayed: boolean = false;
  topicBarDescription: string = null;
  topicBarWikiInfoList: Array<{ key: string; value: any }> = [];

  topicBarNodePaths: Array<Array<MapNode>> = [];
  topicBarTopicsList: Array<NetworkNode> = [];
  topicBarEntitiesList: Array<NetworkNode> = [];

  topicBarDocumentsList: Array<any> = [];
  topicBarDocumentsPresent: boolean = false;

  // private isKnowledgeBusy = false;
  // private getNodeDetails(targetNode: ConnectomeNode) {
  //   this.isKnowledgeBusy = false;
  //   //find all the way to this node by Map
  //   this.topicBarTitle = null;
  //   this.topicBarImageUrl = null;

  //   this.topicBarDescriptionShouldBeDisplayed = false;
  //   this.topicBarDescription = null;
  //   this.topicBarWikiInfoList = [];

  //   this.topicBarNodePaths = [];
  //   this.topicBarTopicsList = [];
  //   this.topicBarEntitiesList = [];
  //   this.topicBarNetworkTopicFocused = null;
  //   this.topicBarNetworkEntityFocused = null;

  //   this.topicBarDocumentsList = [];
  //   this.topicBarDocumentsPresent = false;

  //   if (!targetNode) {
  //     this.isKnowledgeBusy = false;
  //     return;
  //   }

  //   this.isKnowledgeBusy = true;
  //   let networkNode: NetworkNode = null;
  //   let networkType = targetNode.type === TYPE_NODE_IN_MAP.ENTITY ? TYPE_NODE_IN_MAP.ENTITY : TYPE_NODE_IN_MAP.CLUSTER;
  //   this.networkNodes.every(nNode => {
  //     if (nNode.name === targetNode.label && nNode.type === networkType) {
  //       networkNode = nNode;
  //       return false;
  //     }
  //     return true;
  //   });

  //   this.topicBarTitle = targetNode.label;

  //   if (targetNode.type === TYPE_NODE_IN_MAP.ENTITY) {
  //     this.topicBarNetworkEntityFocused = networkNode;
  //   } else {
  //     this.topicBarNetworkTopicFocused = networkNode;
  //   }

  //   let mapNodes: Array<MapNode> = [];
  //   this.mapNodes.every(mNode => {
  //     if (targetNode.origin === TYPE_CONNECTOME_MAP_DATA.NETWORK) {
  //       if (mNode.name === targetNode.label) {
  //         if (targetNode.type === TYPE_NODE_IN_MAP.ENTITY && mNode.type === TYPE_NODE_IN_MAP.ENTITY) {
  //           mapNodes.push(mNode);
  //           return false;
  //         } else if (targetNode.type !== TYPE_NODE_IN_MAP.ENTITY) {
  //           mapNodes.push(mNode);
  //         }
  //       }
  //     } else {
  //       if (
  //         mNode.name === targetNode.label &&
  //         mNode.type === targetNode.type &&
  //         ((targetNode.parents.length === 0 && !mNode.parent) || mNode.parent === targetNode.parents[0])
  //       ) {
  //         mapNodes.push(mNode);
  //         return false;
  //       }
  //     }
  //     return true;
  //   });
  //   console.log('networkNodefocus', networkNode);
  //   console.log('mapNodesfocus', mapNodes);

  //   mapNodes.forEach(node => {
  //     if (node.type === TYPE_NODE_IN_MAP.ENTITY) {
  //       node.Entities?.forEach(entity => {
  //         entity.documents.forEach(doc => {
  //           let newDoc = true;
  //           this.topicBarDocumentsList.every(d => {
  //             if (d.url === doc.url) {
  //               newDoc = false;
  //               return false;
  //             }
  //             return true;
  //           });
  //           if (newDoc) {
  //             this.topicBarDocumentsList.push(doc);
  //           }
  //         });
  //       });
  //       this.topicBarDocumentsPresent = this.topicBarDocumentsList.length > 0;
  //     }
  //     this.topicBarNodePaths.push(node.path);
  //   });

  //   this.networkLinks.forEach(link => {
  //     if (link.from === networkNode.id) {
  //       //@ts-ignore
  //       if (link.target.type === TYPE_NODE_IN_MAP.ENTITY) {
  //         if (networkNode.type !== TYPE_NODE_IN_MAP.ENTITY) {
  //           //@ts-ignore
  //           this.topicBarEntitiesList.push(link.target);
  //         }
  //       } else {
  //         //@ts-ignore
  //         this.topicBarTopicsList.push(link.target);
  //       }
  //     }

  //     if (link.to === networkNode.id) {
  //       //@ts-ignore
  //       if (link.source.type === TYPE_NODE_IN_MAP.ENTITY) {
  //         if (networkNode.type !== TYPE_NODE_IN_MAP.ENTITY) {
  //           //@ts-ignore
  //           this.topicBarEntitiesList.push(link.source);
  //         }
  //       } else {
  //         //@ts-ignore
  //         this.topicBarTopicsList.push(link.source);
  //       }
  //     }
  //   });

  //   if (networkNode.type === TYPE_NODE_IN_MAP.ENTITY && this.topicBarTopicsList.length > 0) {
  //     this.networkLinks.forEach(link => {
  //       //@ts-ignore
  //       if (link.target.type === TYPE_NODE_IN_MAP.ENTITY && link.from === this.topicBarTopicsList[0].id && link.to !== networkNode.id) {
  //         //@ts-ignore
  //         this.topicBarEntitiesList.push(link.target);
  //       }

  //       //@ts-ignore
  //       if (link.target.type === TYPE_NODE_IN_MAP.ENTITY && link.to === this.topicBarTopicsList[0].id && link.from !== networkNode.id) {
  //         //@ts-ignore
  //         this.topicBarEntitiesList.push(link.source);
  //       }
  //     });
  //   }

  //   const language = this.$i18n.locale;
  //   const result = this.connectomeService().getNodeDetails(targetNode.label, 'en');
  //   if (!result) {
  //     this.isKnowledgeBusy = false;
  //     return;
  //   }
  //   result.then(res => {
  //     this.isKnowledgeBusy = false;
  //     let url: String = res.config.url;
  //     if (this.$data.focusNode && url.includes('title=' + this.$data.focusNode.label + '&')) {
  //       // if (res.data.famousImage) {
  //       //   if (res.data.famousImage.cdn) {
  //       //     this.topicBarImageUrl = res.data.famousImage.cdn;
  //       //   }
  //       // }

  //       console.log(res.data);

  //       if (res.data.details.descriptions) {
  //         for (const [key, value] of Object.entries(res.data.details.descriptions)) {
  //           if (!this.topicBarDescription || this.topicBarDescription.length < value.toString().length) {
  //             console.log('description', this.topicBarDescription);
  //             this.topicBarDescription = value.toString();
  //           }
  //         }
  //       }

  //       if (!this.topicBarImageUrl && res.data.details.images) {
  //         for (const [key, value] of Object.entries(res.data.details.images)) {
  //           this.topicBarImageUrl = value.toString();
  //         }
  //       }
  //       const description: Array<any> = res.data.details.sources;
  //       this.topicBarWikiInfoList = [];
  //       description?.forEach(desc => {
  //         if (desc.type === TYPE_NODE_DESC.WIKI && desc.language === language) {
  //           //this.topicBarWikiLabel = desc.label;

  //           for (const [key, value] of Object.entries(desc.informations)) {
  //             let strValue = value.toString();

  //             if (!this.topicBarImageUrl && key === 'image') {
  //               this.topicBarImageUrl = strValue;
  //             } else {
  //               if (
  //                 strValue.toLowerCase().endsWith('.jpg') ||
  //                 strValue.toLowerCase().endsWith('.png') ||
  //                 strValue.toLowerCase().endsWith('.svg') ||
  //                 strValue.toLowerCase().endsWith('.csv') ||
  //                 strValue.toLowerCase().endsWith('.jpeg') ||
  //                 strValue.toLowerCase().endsWith('.tiff') ||
  //                 strValue.toLowerCase().endsWith('.bmp')
  //               ) {
  //               } else {
  //                 this.topicBarWikiInfoList.push({ key: key.replace('_', ' '), value: strValue.substring(0, 200) });
  //               }
  //             }
  //           }
  //         }
  //       });
  //     }
  //     //find documents
  //   });
  // }

  onFeedsClick() {
    this.$router.push('/feed');
  }

  data(): {
    isWikiDescriptionExpanded: boolean;
    focusNode: ConnectomeNode;
    isNetworkMapInitiate: boolean;
  } {
    return {
      isWikiDescriptionExpanded: false,
      focusNode: null,
      isNetworkMapInitiate: false,
    };
  }

  // private iconWikiDescriptionExpanded: string = 'chevron-down';
  // private labelWikiDescriptionExpanded: string = this.$t('map-side-bar.see-more').toString();

  @Watch('isWikiDescriptionExpanded')
  onWikiDescriptionExtendedToggle() {
    // //this.isWikiDescriptionExpanded=!this.isWikiDescriptionExpanded;
    // if (this.$data.isWikiDescriptionExpanded) {
    //   $('.entity-info-wrap dl').css({ height: '100%' });
    //   this.iconWikiDescriptionExpanded = 'chevron-up';
    //   this.labelWikiDescriptionExpanded = this.$t('map-side-bar.see-less').toString();
    // } else {
    //   $('.entity-info-wrap dl').css({ height: 280 });
    //   this.iconWikiDescriptionExpanded = 'chevron-down';
    //   this.labelWikiDescriptionExpanded = this.$t('map-side-bar.see-more').toString();
    // }
  }

  @Watch('focusNode')
  onFocusNodeChanged(newValue) {
    // if (!newValue || newValue === null) {
    //   this.$store.commit('setselectedConnectomeNode', null);
    //   localStorage.removeItem('ds-selectedConnectomeNode');
    //   this.getNodeDetails(null);
    //   this.onTopicDetailClose(null);
    //   this.$children.forEach(child => {
    //     if (child.hasOwnProperty('clearSelection')) {
    //       // @ts-ignore
    //       child.clearSelection();
    //     }
    //   });
    // } else {
    //   console.log('focusNode?', newValue);
    //   this.$store.commit('setselectedConnectomeNode', newValue);
    //   localStorage.setItem('ds-selectedConnectomeNode', JSON.stringify(newValue));
    //   this.getNodeDetails(newValue);
    //   this.$children.forEach(child => {
    //     if (child.hasOwnProperty('focusOnNode')) {
    //       // @ts-ignore
    //       child.focusOnNode(newValue);
    //     }
    //   });
    // }
  }

  // public addFavorites(keyword) {
  //   const connectome = JSON.parse(localStorage.getItem('ds-connectome'));
  //   if (connectome) {
  //     const userId = connectome.user.id;
  //     axios
  //       .post(`api/favorite-keyword/${connectome.connectomeId}/save`, {
  //         content: keyword,
  //         userId: userId,
  //         connectomeId: connectome.connectomeId,
  //       })
  //       .then(res => {
  //         this.$bvToast.toast('Add to favorites successful!', {
  //           toaster: 'b-toaster-bottom-right',
  //           title: 'Success',
  //           variant: 'success',
  //           solid: true,
  //           autoHideDelay: 5000,
  //         });
  //       })
  //       .catch(error => {
  //         this.showAddFavoriteFailedToast();
  //       });
  //   } else {
  //     this.showAddFavoriteFailedToast();
  //   }
  // }

  // showAddFavoriteFailedToast() {
  //   this.$bvToast.toast('Add to favorites failed!', {
  //     toaster: 'b-toaster-bottom-right',
  //     title: 'Fail',
  //     variant: 'danger',
  //     solid: true,
  //     autoHideDelay: 5000,
  //   });
  // }
}
