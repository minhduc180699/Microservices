// @ts-nocheck
import { Component, Vue, Watch } from 'vue-property-decorator';
import { namespace } from 'vuex-class';
import { ConnectomeNetwork } from '@/shared/model/connectome-network.model';
import { ConnectomeNetworkVertex } from '@/shared/model/connectome-network-vertex.model';
import { ConnectomeNetworkEdge } from '@/shared/model/connectome-network-edge.model';
import * as d3 from 'd3';
import * as d3ScaleChromatic from 'd3-scale-chromatic';
import ForceGraph, { ForceGraphInstance, LinkObject } from 'force-graph';
import { MAP_BACKGROUND_COLOR, TYPE_VERTEX, FONTCOLOR_NODE_IN_MAP, MINI_MAP_BACKGROUND_COLOR } from '@/shared/constants/ds-constants';
import MapSideBar from '../map-side-bar/map-side-bar.vue';
import { lab } from 'd3';
import { node } from 'webpack';
import PersonalDocumentsSideBar from '../personal-documents-side-bar/personal-documents-side-bar.vue';
import { faLessThanEqual } from '@fortawesome/free-solid-svg-icons';
import { cardStore } from '@/shared/config/store/card-store';

export const STEP_CONNECTOME_RENDER = {
  FIRST: 10,
  SECOND: 80,
  THIRD: 120,
  FOURTH: 200,
};

const networkStore = namespace('connectomeNetworkStore');
const mapNetworkStore = namespace('mapNetworkStore');
const myAiViewState = namespace('myAIViewStateStore');

@Component({
  components: {
    'map-side-bar': MapSideBar,
  },
})
export default class Map2dNetwork extends Vue {
  selectedNodes: Set<string> = new Set();
  nodesLinkedToSelectedNodes: Set<string> = new Set();
  linksLinkedToSelectedNodes: Set<string> = new Set();

  noData = 'no data';
  noDate = 'no date';
  defaultLangKey = 'en';

  private onInit = true;
  private onZoom = false;
  private graph: ForceGraphInstance = null;

  private tick = 0;
  private isFirstStepPassed = false;
  private isUserStartInteract = false;

  private nodePrimaryColor: d3.ScaleOrdinal<string, unknown, never> = null;
  private nodeSecondaryColor: d3.ScaleOrdinal<string, unknown, never> = null;

  private width = 350;
  private height = 460;

  private curvature = 0;

  @networkStore.Getter
  public connectomeId!: string;

  @networkStore.Getter
  public lastUpdatedAt!: string;

  @networkStore.Getter
  public networkData!: ConnectomeNetwork;

  @networkStore.Getter
  public lang!: string;

  @networkStore.Getter
  public vertice!: (label: string) => ConnectomeNetworkVertex;

  @networkStore.Getter
  public vertices!: Array<ConnectomeNetworkVertex>;

  @networkStore.Getter
  public edges!: Array<ConnectomeNetworkEdge>;

  @networkStore.Getter
  public verticeColorByCluster!: (mainCluster: string) => d3.RGBColor;

  @mapNetworkStore.Getter
  public entityLabelSelected!: string;

  @mapNetworkStore.Getter
  public centerAtSelectionCommand!: string;

  @mapNetworkStore.Getter
  public refreshMapCommand!: string;

  @mapNetworkStore.Getter
  public isMapInitialized!: boolean;

  @mapNetworkStore.Getter
  public searchResult!: Array<ConnectomeNetworkVertex>;

  @mapNetworkStore.Getter
  public clusterExpanded!: string;

  @mapNetworkStore.Getter
  public getGlobalViewSettings!: { scale: number; centerX: number; centerY: number };

  @networkStore.Action
  public getConnectomeUpdate!: (payload: { connectomeId: string; language: string }) => Promise<any>;

  @mapNetworkStore.Action
  public setSelectedEntity!: (payload: { title: string; srcLang: string }) => void;

  @mapNetworkStore.Action
  public clearSelectedEntity!: () => void;

  @mapNetworkStore.Action
  public setMapInitialized!: () => void;

  @mapNetworkStore.Action
  public saveGlobalViewParameters!: (payload: { scale: number; centerX: number; centerY: number }) => void;

  @mapNetworkStore.Action
  public expandedThisCluster!: (label: string) => void;

  @Watch('lastUpdatedAt')
  onLastUpdatedAtUpdated(lastUpdateDate: string) {
    console.log('onLastUpdatedAtUpdated', lastUpdateDate);
    if (!lastUpdateDate) {
      return;
    }
    //this.updateNetworkMap();
  }

  @Watch('networkData')
  onNetworkDataUpdated(data: ConnectomeNetwork) {
    console.log('onNetworkDataUpdated', data);
    this.updateNetworkMap();
  }

  @Watch('vertices')
  onVerticesUpdated(data: Array<ConnectomeNetworkVertex>) {
    //console.log('onVerticesUpdated', data);
    //this.updateNetworkMap();
  }

  @Watch('refreshMapCommand')
  onRefreshMapCommandChanged(count: number) {
    console.log('refreshMapCommand', count);
    //this.updateNetworkMap();
  }

  @Watch('centerAtSelectionCommand')
  onCenterAtSelectionCommandChanged(count: number) {
    console.log('centerAtSelectionCommand:', count);
    if (this.entityLabelSelected) {
      const vertex = this.vertice(this.entityLabelSelected);
      if (vertex) {
        this.zoomOnSelection(this.entityLabelSelected);
      }
    }
  }

  @Watch('entityLabelSelected')
  onEntitySelectedChanged(label: string) {
    this.clearSelection();
    if (!label) {
      return;
    }
    const vertex = this.vertice(label);
    console.log('entity selected changed', label);
    if (vertex) {
      this.createSelection(label);
      this.zoomOnSelection(label);
    }
  }

  calculateCanvasSize() {
    this.width = Math.max($('.connectome-area').width(), 350);
    this.height = Math.max($('.connectome-area').height(), 350);
  }

  @Watch('$route', { immediate: true, deep: true })
  onUrlChange(newVal: Route) {
    console.log(newVal);

    if (newVal.name.localeCompare('2DNetwork') == 0) {
      document.body.setAttribute('data-menu', 'connectome');
      //if(this.mounted){
      this.updateNetworkMap();
      //}
    }
  }

  unmounted() {
    console.log('unmounted');
  }

  isMounted = false;
  mounted() {
    console.log('mounted', this.isMounted);
    window.onresize = this.chartResize;
    console.log('MOUNTED: Total after ', this.vertices.length);
    console.log('TICK ', this.tick);

    if (!this.graph) {
      const elem = document.getElementsByClassName('connectome-area');
      if (!elem) {
        return;
      }

      if (elem.length === 0) {
        return;
      }
      this.tick = 0;
      this.graph = ForceGraph()(elem.item(0)).backgroundColor(MAP_BACKGROUND_COLOR);
      // .nodeId('label')
      // .nodeLabel(node => {
      //   return node.type === TYPE_VERTEX.CLUSTER ? node.entities?.length : node.label;
      // })
      // .nodeVisibility(node => {
      //   return this.isNodeVisible(node);
      // })
      // .nodeCanvasObject((node, ctx, globalScale) => {
      //   this.renderNodes(node, ctx, globalScale, this.tick);
      // })
      // .nodePointerAreaPaint((node, color, ctx) => {
      //   ctx.fillStyle = color;
      //   const bckgDimensions = node.__bckgDimensions;
      //   bckgDimensions && ctx.fillRect(node.x - bckgDimensions[0] / 2, node.y - bckgDimensions[1] / 2, ...bckgDimensions);
      // })
      // .nodeCanvasObjectMode(() => 'replace')
      // .linkColor(link => {
      //   return link.__indexColor;
      // })
      // .linkSource('from')
      // .linkTarget('to')
      // .linkVisibility(link => {
      //   return this.isLinkVisible(link, this.tick);
      // })
      // .autoPauseRedraw(false)
      // .onBackgroundClick(event => {
      //   this.isUserStartInteract = true;
      //   this.clearSelectedEntity();
      // })
      // .onNodeClick(node => {
      //   if (!node.label) return;
      //   if (node.TYPE === TYPE_VERTEX.ROOT || node.TYPE === TYPE_VERTEX.CLUSTER) return;
      //   this.isUserStartInteract = true;
      //   this.onNodeClick(node);
      // })
      // .onZoom(event => {
      //   //this.isUserStartInteract = true;
      // })
      // .onZoomEnd(event => {
      //   //this.isUserStartInteract = true;
      //   //console.log(event);
      //   if (!event) {
      //     return;
      //   }
      //   this.saveGlobalViewParameters({ scale: event.k, centerX: event.x, centerY: event.y });
      // })
      // .enableNodeDrag(false)
      // .enableZoomInteraction(true)
      // //.warmupTicks(50)
      // .onEngineTick(() => {
      //   if (!this.isFirstStepPassed) {
      //     if (this.tick > 20) {
      //       if (this.getGlobalViewSettings) {
      //         this.graph.centerAt(this.getGlobalViewSettings.centerX, this.getGlobalViewSettings.centerY);
      //         this.graph.zoom(this.getGlobalViewSettings.scale, 300);
      //       } else {
      //         this.graph.zoomToFit(300, 50, node => true);
      //       }
      //       this.isFirstStepPassed = true;
      //     }
      //   }
      //   if (!this.isUserStartInteract) {
      //     if (this.tick > STEP_CONNECTOME_RENDER.SECOND) {
      //       if (this.entityLabelSelected) {
      //         const vertex = this.vertice(this.entityLabelSelected);
      //         if (vertex) {
      //           this.createSelection(this.entityLabelSelected);
      //           this.zoomOnSelection(this.entityLabelSelected);
      //         }
      //       }
      //       this.isUserStartInteract = true;
      //     }
      //   }
      //   this.tick++;
      // })
      // .onEngineStop(() => {});
    }
    //if(!this.isMapInitialized){
    this.updateNetworkMap();
    this.isMounted = true;
    //}
  }

  data() {
    return {};
  }

  private arrayDiff(a: Array<any>, b: Array<any>) {
    return [...a.filter(x => !b.includes(x)), ...b.filter(x => !a.includes(x))];
  }

  updateNetworkMap() {
    if (!this.vertices || this.vertices.length < 2) {
      console.log('how is vertice', !this.vertices, this.vertices?.length);
      this.displayDummyChart(true);
      return;
    } else {
      this.displayDummyChart(false);
    }

    if (this.graph && this.vertices && this.vertices.length > 0 && this.edges) {
      const { nodes, links } = this.graph.graphData();
      //remove the node which is not exist in vertex
      const nodesToDelete = new Array<string>();
      const a = 50,
        b = 200;
      let ind = 0;
      let angle = 0;
      nodes.forEach(node => {
        const vertex = this.vertice(node.label);
        if (vertex) {
          node.type = vertex.type;
          node.mainCluster = vertex.mainCluster;
          node.entities = vertex.entities.map(x => x);
          node.disable = vertex.disable;
          node.favorite = vertex.favorite;
          if (vertex.type === TYPE_VERTEX.ROOT) {
            node.fx = 0.0;
            node.fy = 0.0;
          } else if (vertex.type === TYPE_VERTEX.CLUSTER) {
            angle = 1.2 * ind;
            node.fx = (a + b * angle) * Math.cos(angle);
            node.fy = (a + b * angle) * Math.sin(angle);
            ind++;
          } else {
            node.fx = node.x;
            node.fy = node.y;
            //node.disable = true;
          }
        } else {
          nodesToDelete.push(node.label);
        }
      });

      nodesToDelete.forEach(nodeLabel => {
        const indexNodeToDelete = nodes.findIndex(item => item.label === nodeLabel);
        if (indexNodeToDelete > -1) {
          nodes.splice(indexNodeToDelete, 1);
        }
      });

      this.vertices.forEach(vertex => {
        const indexNode = nodes.findIndex(item => item.label === vertex.label);
        if (indexNode < 0) {
          const nodeToAdd = vertex;
          if (nodeToAdd.type === TYPE_VERTEX.ROOT) {
            nodeToAdd.fx = 0.0;
            nodeToAdd.fy = 0.0;
          } else if (vertex.type === TYPE_VERTEX.CLUSTER) {
            angle = 1.2 * ind;
            nodeToAdd.fx = (a + b * angle) * Math.cos(angle);
            nodeToAdd.fy = (a + b * angle) * Math.sin(angle);
            ind++;
          } else {
            //nodeToAdd.disable=true;
          }
          nodes.push(nodeToAdd);
        }
      });

      //remove links to hide

      //remove links doesn't exist in edges
      const newEdges = this.edges.map(x => x);

      console.log('black_entity after ', this.vertices.filter(vertex => vertex.type === TYPE_VERTEX.BLACK_ENTITY).length);
      console.log('cluster after ', this.vertices.filter(vertex => vertex.type === TYPE_VERTEX.CLUSTER).length);
      console.log('entities after ', this.vertices.filter(vertex => vertex.type === TYPE_VERTEX.ENTITY).length);
      console.log('MOUNTED: Total after ', this.vertices.length);
      //console.log(nodes);
      if (this.entityLabelSelected) {
        const vertex = this.vertice(this.entityLabelSelected);
        if (vertex) {
          this.clearSelection();
          this.createSelection(this.entityLabelSelected);
        }
      }

      this.calculateCanvasSize();
      this.tick = 0;
      //this.displayDummyChart(true);
      //return;
      this.graph
        .graphData({ nodes: nodes, links: newEdges })
        .backgroundColor(MAP_BACKGROUND_COLOR)
        .width(this.width)
        .height(this.height)
        .nodeId('label')
        .nodeLabel(node => {
          return node.type === TYPE_VERTEX.CLUSTER ? node.entities?.length : node.label;
        })
        .nodeVisibility(node => {
          return this.isNodeVisible(node);
        })
        .nodeCanvasObject((node, ctx, globalScale) => {
          this.renderNodes(node, ctx, globalScale, this.tick);
        })
        .nodePointerAreaPaint((node, color, ctx) => {
          ctx.fillStyle = color;
          const bckgDimensions = node.__bckgDimensions;
          bckgDimensions && ctx.fillRect(node.x - bckgDimensions[0] / 2, node.y - bckgDimensions[1] / 2, ...bckgDimensions);
        })
        .nodeCanvasObjectMode(() => 'replace')
        .d3Force('center', null)
        //.d3Force('charge', null)
        //.d3Force('link', null)
        //.d3Force('charge', d3.forceManyBody().strength(-100))
        .d3Force(
          'link',
          d3
            .forceLink(links)
            .id(d => d.id)
            .distance(link => {
              return link.source.type === TYPE_VERTEX.CLUSTER ? this.getSizeFromType(link.target) : null;
            })
            .strength(link => {
              return link.source.type === TYPE_VERTEX.CLUSTER ? 1 : 0.04;
            })
        )
        // .d3Force(
        //   'link',
        //   null
        // )
        .d3Force(
          'collision',
          d3.forceCollide(node => {
            return this.getFactorCollisionFromType(node.type) * this.getSizeFromType(node);
            //return 300;
          })
        )
        // .d3Force(
        //   'link',
        //   d3
        //     .forceLink(links)
        //     .id(d => d.id)
        //     .distance(link=>{return link.source?.type===TYPE_VERTEX.CLUSTER?this.getSizeFromType(link.target):this.getSizeFromType(link.target)+this.getSizeFromType(link.source);})
        //     .strength(0.5)
        // )
        // .d3Force("charge", d3.forceManyBody().strength(-50))
        // .d3Force("x", d3.forceX())
        // .d3Force("y", d3.forceY())
        .linkCurvature(link => {
          return this.renderCurvatureLink(link, this.tick);
        })
        .linkColor(link => {
          return this.renderColorLink(link, this.tick);
        })
        .linkSource('from')
        .linkTarget('to')
        .linkVisibility(link => {
          return this.isLinkVisible(link, this.tick);
        })
        .onLinkClick((link, event) => {
          if (this.isSelectedLink(link) && link.target) {
            this.graph.centerAt(link.target.x, link.target.y);
          }
        })
        .d3AlphaDecay(0.06)
        //.d3VelocityDecay(0.04)
        //.cooldownTime(60000)
        .autoPauseRedraw(false)
        .onBackgroundClick(event => {
          this.isUserStartInteract = true;
          this.clearSelectedEntity();
        })
        .onNodeClick(node => {
          if (!node.label) return;
          if (node.TYPE === TYPE_VERTEX.ROOT || node.TYPE === TYPE_VERTEX.CLUSTER) return;
          this.isUserStartInteract = true;
          this.onNodeClick(node);
        })
        .onZoom(event => {})
        .onZoomEnd(event => {
          if (!event) {
            return;
          }
          this.saveGlobalViewParameters({ scale: event.k, centerX: event.x, centerY: event.y });
        })
        //.dagMode(null)
        //.dagNodeFilter(node =>{return node.type === TYPE_VERTEX.ROOT || node.type === TYPE_VERTEX.CLUSTER;})
        .enableNodeDrag(false)
        .enableZoomInteraction(true)
        //.warmupTicks(50)
        .onEngineTick(() => {
          if (!this.isFirstStepPassed) {
            if (this.tick > 20) {
              if (this.getGlobalViewSettings) {
                this.graph.centerAt(this.getGlobalViewSettings.centerX, this.getGlobalViewSettings.centerY);
                this.graph.zoom(this.getGlobalViewSettings.scale, 300);
              } else {
                this.graph.zoomToFit(300, 100, node => true);
              }
              this.isFirstStepPassed = true;
            }
          }
          if (!this.isUserStartInteract) {
            if (this.tick > STEP_CONNECTOME_RENDER.SECOND) {
              if (this.entityLabelSelected) {
                const vertex = this.vertice(this.entityLabelSelected);
                if (vertex) {
                  this.createSelection(this.entityLabelSelected);
                  this.zoomOnSelection(this.entityLabelSelected);
                }
              } else {
                this.graph.zoomToFit(200, 100, node => true);
              }
              this.isUserStartInteract = true;
            }
          }
          this.tick++;
        })
        .onEngineStop(() => {
          if (this.connectomeId != this.noData && this.$store.getters.currentLanguage) {
            this.getConnectomeUpdate({ connectomeId: this.connectomeId, language: this.$store.getters.currentLanguage });
          }
        });
    }
  }

  private isSelectedLink(link: LinkObject) {
    if (!link) {
      return false;
    }

    if (!link.source) {
      return false;
    }

    if (!link.target) {
      return false;
    }

    if (typeof link.source === 'NodeObject') {
      if (link.source.type === TYPE_VERTEX.ROOT) {
        return false;
      }
      if (link.source.type === TYPE_VERTEX.CLUSTER) {
        return false;
      }
      if (this.selectedNodes.has(link.source.label)) {
        return true;
      }
    }

    if (typeof link.target === 'NodeObject') {
      if (link.target.type === TYPE_VERTEX.ROOT) {
        return false;
      }
      if (link.target.type === TYPE_VERTEX.CLUSTER) {
        return false;
      }
      if (this.selectedNodes.has(link.target.label)) {
        return true;
      }
    }

    if (typeof link.source === 'object') {
      if (link.source.type === TYPE_VERTEX.ROOT) {
        return false;
      }
      if (link.source.type === TYPE_VERTEX.CLUSTER) {
        return false;
      }
      if (this.selectedNodes.has(link.source.label)) {
        return true;
      }
    }

    if (typeof link.target === 'object') {
      if (link.target.type === TYPE_VERTEX.ROOT) {
        return true;
      }
      if (link.target.type === TYPE_VERTEX.CLUSTER) {
        return false;
      }
      if (this.selectedNodes.has(link.target.label)) {
        return true;
      }
    }
    return false;
  }

  private isNodeVisible(node: any) {
    if (!node) {
      return false;
    }

    if (node.disable) {
      return false;
    }

    if (node.type === TYPE_VERTEX.ROOT) {
      return true;
    }

    if (node.type === TYPE_VERTEX.CLUSTER) {
      return true;
    }

    return true;
  }

  private isLinkVisible(link: LinkObject, tick: number) {
    // if (tick < STEP_CONNECTOME_RENDER.SECOND) {
    //   return false;
    // }
    if (!link) {
      return false;
    }

    if (!link.source) {
      return false;
    }

    if (!link.target) {
      return false;
    }

    //console.log(link);

    if (typeof link.source === 'string') {
      if (link.source === this.networkData.connectomeId) {
        return false;
      }
    }

    if (typeof link.source === 'NodeObject') {
      if (link.source.type === TYPE_VERTEX.ROOT) {
        return true;
      }

      if (link.source.type === TYPE_VERTEX.CLUSTER) {
        return true;
      }
    }

    if (typeof link.source === 'object') {
      if (link.source.type === TYPE_VERTEX.ROOT) {
        return false;
      }

      if (link.source.type === TYPE_VERTEX.CLUSTER) {
        return true;
      }

      if (link.source.disable) {
        return false;
      }
    }

    if (typeof link.target === 'object') {
      if (link.target.type === TYPE_VERTEX.ROOT) {
        return false;
      }

      if (link.target.type === TYPE_VERTEX.CLUSTER) {
        return true;
      }

      if (link.target.disable) {
        return false;
      }
    }

    // if (typeof link.target === 'object') {
    // if (!this.isNodeVisible(link.target)) {
    //   return false;
    // }

    // if (link.target.label.localeCompare(this.clusterExpanded) == 0) {
    //   return false;
    // }
    // }

    // if (typeof link.target === 'object' && typeof link.source === 'object') {
    //   if (link.target.mainCluster.localeCompare(link.source.mainCluster) == 0) {
    //     return true;
    //   }
    // }

    if (this.isSelectedLink(link)) {
      return true;
    }

    // if (tick > STEP_CONNECTOME_RENDER.FOURTH) {
    //   return true;
    // }

    return true;
  }

  private renderCurvatureLink<type>(link: any, tick: number): number | undefined {
    return 0.2;
  }

  private numberParticultVisible(link: LinkObject) {
    if (!link) {
      return 1;
    }

    if (!link.source) {
      return 1;
    }

    if (!link.target) {
      return 1;
    }

    if (this.isSelectedLink(link)) {
      return 2;
    }

    return 1;
  }

  private renderColorLink<type>(link: any, tick: number): string | RGBColor | undefined {
    if (link.source && tick >= STEP_CONNECTOME_RENDER.FIRST) {
      // const linkColor =
      //   link.source.type === TYPE_VERTEX.CLUSTER && link.target.type === TYPE_VERTEX.CLUSTER
      //     ? this.verticeColorByCluster(link.source.label)
      //     : this.verticeColorByCluster(link.source.mainCluster);
      const linkColor = this.verticeColorByCluster(link.source.mainCluster);
      if (this.selectedNodes && this.selectedNodes.size > 0) {
        return this.isSelectedLink(link) ? d3.rgb(linkColor).brighter(2) : d3.rgb(linkColor).darker(3);
      } else {
        return d3.rgb(linkColor).darker(2);
      }
    } else {
      return link.__indexColor;
    }
  }

  private renderDirectionalParticleColor<type>(link: LinkObject, tick: number): string | RGBColor | undefined {
    if (link.source && tick >= STEP_CONNECTOME_RENDER.FIRST) {
      const linkColor = this.verticeColorByCluster(link.source.mainCluster);
      return this.isSelectedLink(link) ? '#ffffff' : d3.rgb(linkColor).brighter(2);
    } else {
      return link.__indexColor;
    }
  }

  private renderDirectionalParticleWidth<type>(link: LinkObject, tick: number): number | undefined {
    if (tick >= STEP_CONNECTOME_RENDER.FIRST) {
      return this.isSelectedLink(link) ? 18 : 0;
    } else {
      return 0;
    }
  }

  private onNodeClick(node: any) {
    this.clearSelectedEntity();
    this.setSelectedEntity({ title: node.label, srcLang: this.lang });
  }

  private renderNodes(node: any, ctx: any, globalScale: any, tick: number) {
    if (node.type === TYPE_VERTEX.CLUSTER) {
      //console.log(node.label);
      this.renderCluster(node, ctx, globalScale, tick);
      return;
    }

    if (node.type === TYPE_VERTEX.ROOT) {
      //this.renderRootBox(node, ctx, globalScale, tick);
      return;
    }
    const label = node.label;
    const size = this.getSizeFromType(node);
    const fontSize = this.getFontsizeFromType(node.type);
    const nodeColor = d3.rgb(this.verticeColorByCluster(node.mainCluster));
    const nodeBoundaryColor = nodeColor.brighter(1.2);
    const textWidth = ctx.measureText(label).width;
    const bckgDimensions = [textWidth, fontSize].map(n => n + fontSize * 0.2); // some padding

    const indexInSearch = this.searchResult.findIndex(elem => elem.label.localeCompare(node.label) === 0);

    node.__bckgDimensions = bckgDimensions; // to re-use in nodePointerAreaPaint
    if (!node.x || !node.y) {
      return;
    }
    if (this.selectedNodes.size > 0) {
      this.renderSelection(node, ctx, globalScale, tick);
      return;
    }

    if (node.favorite) {
      this.renderFavorite(node, ctx, globalScale, tick);
      return;
    }

    if (this.tick <= STEP_CONNECTOME_RENDER.FIRST) {
      ctx.beginPath();
      ctx.fillStyle = nodeColor;
      ctx.arc(node.x, node.y, size, 0, 2 * Math.PI, false);
      ctx.fill();
      ctx.closePath();
    } else {
      ctx.beginPath();
      ctx.fillStyle = nodeBoundaryColor;
      ctx.arc(node.x, node.y, size + 3, 0, 2 * Math.PI, false);
      ctx.fill();
      ctx.closePath();
      ctx.beginPath();
      ctx.fillStyle = d3.rgb(nodeColor);
      ctx.arc(node.x, node.y, size, 0, 2 * Math.PI, false);
      ctx.fill();
      ctx.closePath();

      ctx.lineWidth = 2 / globalScale;
      ctx.beginPath();
      ctx.fillStyle = node.type != TYPE_VERTEX.BLACK_ENTITY ? FONTCOLOR_NODE_IN_MAP.COMMON : FONTCOLOR_NODE_IN_MAP.BLACK_ENTITY;
      ctx.strokeStyle =
        node.type != TYPE_VERTEX.BLACK_ENTITY ? FONTCOLOR_NODE_IN_MAP.COMMON_STROKE : FONTCOLOR_NODE_IN_MAP.BLACK_ENTITY_STROKE;
      ctx.textAlign = 'center';
      ctx.textBaseline = 'middle';
      if (globalScale >= 0.1 || node.entities.length > 5) {
        ctx.font = `${fontSize}px Sans-Serif`;
        ctx.strokeText(label, node.x, node.y - size - 5);
        ctx.fillText(label, node.x, node.y - size - 5);
        node.__bckgDimensions = bckgDimensions;
      } else if (node.entities.length < 2) {
        if (globalScale >= 0.08) {
          fontSize = this.getFontsizeFromType(node.type) / (globalScale * 2);
          ctx.font = `${fontSize}px Sans-Serif`;
          ctx.strokeText(label, node.x, node.y - size - 5);
          ctx.fillText(label, node.x, node.y - size - 5);
          node.__bckgDimensions = bckgDimensions;
        } else {
          if (globalScale >= 0.05) {
            fontSize = this.getFontsizeFromType(node.type) / (globalScale * 3);
            ctx.font = `${fontSize}px Sans-Serif`;
            ctx.strokeText(label, node.x, node.y - size - 5);
            ctx.fillText(label, node.x, node.y - size - 5);
            node.__bckgDimensions = bckgDimensions;
          }
        }
      }
      ctx.closePath();

      bckgDimensions = [2 * size + 3, 2 * size + 3].map(n => n + 5);
      node.__bckgDimensions = bckgDimensions;
    }
  }

  private renderRootBox(rootNode: any, ctx: any, globalScale: any, tick: number) {
    const size = this.getSizeFromType(rootNode.type) / (globalScale * 2);
    const fontSize = this.getFontsizeFromType(rootNode.type) / globalScale;
    const nodeColor = d3.rgb('#deffde');

    if (!rootNode.x || !rootNode.y) {
      return;
    }

    if (tick <= STEP_CONNECTOME_RENDER.FIRST) {
      ctx.beginPath();
      ctx.fillStyle = nodeColor;
      ctx.arc(rootNode.x, rootNode.y, size, 0, 2 * Math.PI, false);
      ctx.fill();
      ctx.closePath();
    } else {
      const convexBox = this.graph.getGraphBbox(node => true);
      if (convexBox) {
        const label = '[' + convexBox.x[0] + ',' + convexBox.y[0] + ']';
        const label2 = '[' + convexBox.x[1] + ',' + convexBox.y[1] + ']';
        ctx.beginPath();
        ctx.lineWidth = 4 / globalScale;
        ctx.strokeStyle = nodeColor;
        ctx.fillStyle = nodeColor;
        ctx.globalAlpha = 0.4;
        ctx.strokeRect(
          convexBox.x[0] - this.getSizeFromType(TYPE_VERTEX.ENTITY),
          convexBox.y[0] - this.getSizeFromType(TYPE_VERTEX.ENTITY),
          convexBox.x[1] - convexBox.x[0] + this.getSizeFromType(TYPE_VERTEX.ENTITY),
          convexBox.y[1] - convexBox.y[0] + this.getSizeFromType(TYPE_VERTEX.ENTITY)
        );
        // const x2 = Math.pow(clusterNode.x - convexBox.x[0], 2);
        // const y2 = Math.pow(clusterNode.y - convexBox.y[0], 2);
        // const radius = Math.sqrt(x2 + y2);
        // ctx.arc(clusterNode.x, clusterNode.y, radius, 0, 2 * Math.PI, false);
        // ctx.stroke();
        ctx.globalAlpha = 1.0;
        ctx.beginPath();
        ctx.fillStyle = rootNode.type != TYPE_VERTEX.BLACK_ENTITY ? nodeColor : FONTCOLOR_NODE_IN_MAP.BLACK_ENTITY;
        ctx.strokeStyle =
          rootNode.type != TYPE_VERTEX.BLACK_ENTITY ? FONTCOLOR_NODE_IN_MAP.COMMON_STROKE : FONTCOLOR_NODE_IN_MAP.BLACK_ENTITY_STROKE;
        ctx.textAlign = 'left';
        ctx.textBaseline = 'top';
        ctx.font = `${fontSize}px Sans-Serif`;
        ctx.strokeText(label, convexBox.x[0], convexBox.y[0]);
        ctx.fillText(label, convexBox.x[0], convexBox.y[0]);
        ctx.strokeText(label2, convexBox.x[1], convexBox.y[1]);
        ctx.fillText(label2, convexBox.x[1], convexBox.y[1]);
        ctx.closePath();
      }
    }
    const bckgDimensions = [0, 0].map(n => n + 5);
    rootNode.__bckgDimensions = bckgDimensions;
  }

  private renderCluster(clusterNode: any, ctx: any, globalScale: any, tick: number) {
    const label = clusterNode.entities?.length;
    const size = this.getSizeFromType(clusterNode.type) / (globalScale * 2);
    const fontSize = this.getFontsizeFromType(clusterNode.type) / globalScale;
    const nodeColor = d3.rgb(this.verticeColorByCluster(clusterNode.label));

    // if (!clusterNode.x || !clusterNode.y) {
    //   return;
    // }

    if (this.tick <= STEP_CONNECTOME_RENDER.FIRST) {
      ctx.beginPath();
      ctx.fillStyle = nodeColor;
      ctx.arc(clusterNode.x, clusterNode.y, size, 0, 2 * Math.PI, false);
      ctx.fill();
      ctx.closePath();
    } else {
      const convexBox = this.graph.getGraphBbox(node => node.mainCluster?.localeCompare(clusterNode.label) == 0);
      if (convexBox) {
        // ctx.beginPath();
        // ctx.fillStyle = d3.rgb(nodeColor);
        // ctx.arc(clusterNode.x, clusterNode.y, size, 0, 2 * Math.PI, false);
        // ctx.fill();
        // ctx.closePath();

        ctx.beginPath();
        ctx.lineWidth = 4 / globalScale;
        ctx.strokeStyle = nodeColor;
        ctx.fillStyle = nodeColor;
        ctx.globalAlpha = 0.4;
        // ctx.strokeRect(
        //   convexBox.x[0] - this.getSizeFromType(TYPE_VERTEX.ENTITY),
        //   convexBox.y[0] - this.getSizeFromType(TYPE_VERTEX.ENTITY),
        //   convexBox.x[1] - convexBox.x[0] + this.getSizeFromType(TYPE_VERTEX.ENTITY),
        //   convexBox.y[1] - convexBox.y[0] + this.getSizeFromType(TYPE_VERTEX.ENTITY)
        // );

        const radiusX = (convexBox.x[1] - convexBox.x[0]) / 1.5;
        const radiusY = (convexBox.y[1] - convexBox.y[0]) / 1.5;
        ctx.ellipse((convexBox.x[1] + convexBox.x[0]) / 2, (convexBox.y[1] + convexBox.y[0]) / 2, radiusX, radiusY, 0, 0, 2 * Math.PI);
        ctx.stroke();
        // const x2 = Math.pow(clusterNode.x - convexBox.x[0], 2);
        // const y2 = Math.pow(clusterNode.y - convexBox.y[0], 2);
        // const radius = Math.sqrt(x2 + y2);
        // ctx.arc(clusterNode.x, clusterNode.y, radius, 0, 2 * Math.PI, false);
        // ctx.stroke();

        ctx.globalAlpha = 1.0;
        ctx.closePath();
        // ctx.beginPath();
        // ctx.fillStyle = clusterNode.type != TYPE_VERTEX.BLACK_ENTITY ? nodeColor : FONTCOLOR_NODE_IN_MAP.BLACK_ENTITY;
        // ctx.strokeStyle =
        //   clusterNode.type != TYPE_VERTEX.BLACK_ENTITY ? FONTCOLOR_NODE_IN_MAP.COMMON_STROKE : FONTCOLOR_NODE_IN_MAP.BLACK_ENTITY_STROKE;
        // ctx.textAlign = 'left';
        // ctx.textBaseline = 'top';
        // ctx.font = `${fontSize}px Sans-Serif`;
        // ctx.strokeText(label, convexBox.x[0], convexBox.y[0]);
        // ctx.fillText(label, convexBox.x[0], convexBox.y[0]);
        // ctx.closePath();
      }
    }
    const bckgDimensions = [0, 0].map(n => n + 5);
    clusterNode.__bckgDimensions = bckgDimensions;
  }

  private getSizeFromType(node: ConnectomeNetworkVertex): number | undefined {
    const size = 1 * (node.entities?.length == 0 ? 1 : Math.min(19, node.entities?.length));
    switch (node.type) {
      case TYPE_VERTEX.CLUSTER:
        return 1;
      case TYPE_VERTEX.ENTITY:
        return 1 + size;
      case TYPE_VERTEX.BLACK_ENTITY:
        return 1 + size;
      case TYPE_VERTEX.ROOT:
        return 1;
      default:
        return 30;
    }
  }

  private getFontsizeFromType(nodeType: TYPE_VERTEX): number | undefined {
    switch (nodeType) {
      case TYPE_VERTEX.CLUSTER:
        return 26;
      case TYPE_VERTEX.ENTITY:
        return 26;
      case TYPE_VERTEX.BLACK_ENTITY:
        return 22;
      case TYPE_VERTEX.ROOT:
        return 20;
      default:
        return 18;
    }
  }

  private getFactorCollisionFromType(nodeType: TYPE_VERTEX): number | undefined {
    switch (nodeType) {
      case TYPE_VERTEX.CLUSTER:
        return 15;
      case TYPE_VERTEX.ENTITY:
        return 15;
      case TYPE_VERTEX.BLACK_ENTITY:
        return 12;
      case TYPE_VERTEX.ROOT:
        return 20;
      default:
        return 2;
    }
  }

  private renderSelection(node: any, ctx: any, globalScale: any, tick: number) {
    if (this.selectedNodes.has(node.label)) {
      this.renderSelectedNodes(node, ctx, globalScale, tick);
    } else if (this.nodesLinkedToSelectedNodes.has(node.label)) {
      this.renderSecondarySelectedNodes(node, ctx, globalScale, tick);
    } else {
      this.renderUnelectedNodes(node, ctx, globalScale, tick);
    }
  }

  private renderUnelectedNodes(node: any, ctx: any, globalScale: any, tick: number) {
    const label = node.label;
    const size = this.getSizeFromType(node) * 0.8;
    const fontSize = this.getFontsizeFromType(node.type) / (3 * globalScale);
    const nodeColor = d3.rgb(this.verticeColorByCluster(node.mainCluster)).darker(2);
    const nodeBoundaryColor = nodeColor.darker(3);
    const textWidth = ctx.measureText(label).width;
    const bckgDimensions = [textWidth, fontSize].map(n => n + fontSize * 0.2); // some padding

    node.__bckgDimensions = bckgDimensions; // to re-use in nodePointerAreaPaint
    if (!node.x || !node.y) {
      return;
    }

    if (tick <= STEP_CONNECTOME_RENDER.SECOND) {
      ctx.beginPath();
      ctx.fillStyle = nodeColor;
      ctx.arc(node.x, node.y, size, 0, 2 * Math.PI, false);
      ctx.fill();
      ctx.closePath();
      bckgDimensions = [size, size].map(n => n + 5);
      node.__bckgDimensions = bckgDimensions;
    } else {
      ctx.beginPath();
      ctx.fillStyle = nodeBoundaryColor;
      ctx.arc(node.x, node.y, size + 3, 0, 2 * Math.PI, false);
      ctx.fill();
      ctx.closePath();
      ctx.beginPath();
      ctx.fillStyle = nodeColor;
      ctx.arc(node.x, node.y, size, 0, 2 * Math.PI, false);
      ctx.fill();
      ctx.closePath();
      ctx.lineWidth = 2 / globalScale;
      ctx.beginPath();
      ctx.fillStyle =
        node.type != TYPE_VERTEX.BLACK_ENTITY
          ? d3.rgb(FONTCOLOR_NODE_IN_MAP.COMMON).darker(2)
          : d3.rgb(FONTCOLOR_NODE_IN_MAP.BLACK_ENTITY).darker(2);
      ctx.strokeStyle =
        node.type != TYPE_VERTEX.BLACK_ENTITY
          ? d3.rgb(FONTCOLOR_NODE_IN_MAP.COMMON_STROKE).darker(2)
          : d3.rgb(FONTCOLOR_NODE_IN_MAP.BLACK_ENTITY_STROKE).darker(2);
      ctx.font = `${fontSize}px Sans-Serif`;
      ctx.textAlign = 'center';
      ctx.textBaseline = 'middle';
      if (globalScale >= 0.4) {
        ctx.strokeText(label, node.x, node.y - size - 5);
        ctx.fillText(label, node.x, node.y - size - 5);
      } else if (node.entities.length > 5) {
        ctx.strokeText(label, node.x, node.y - size - 5);
        ctx.fillText(label, node.x, node.y - size - 5);
      }
      bckgDimensions = [2 * size + 3, 2 * size + 3].map(n => n + 5);
      node.__bckgDimensions = bckgDimensions;
      ctx.closePath();
    }
  }

  private renderSecondarySelectedNodes(node: any, ctx: any, globalScale: any, tick: number) {
    const label = node.label;
    const size = this.getSizeFromType(node);
    const fontSize = this.getFontsizeFromType(node.type) / globalScale;
    const nodeColor = d3.rgb(this.verticeColorByCluster(node.mainCluster)).brighter(1.2);
    const nodeBoundaryColor = nodeColor.brighter(1.5);
    const textWidth = ctx.measureText(label).width;
    const bckgDimensions = [textWidth, fontSize].map(n => n + fontSize * 0.2); // some padding

    node.__bckgDimensions = bckgDimensions; // to re-use in nodePointerAreaPaint
    if (!node.x || !node.y) {
      return;
    }

    if (tick <= STEP_CONNECTOME_RENDER.SECOND) {
      ctx.beginPath();
      ctx.fillStyle = nodeColor;
      ctx.arc(node.x, node.y, size, 0, 2 * Math.PI, false);
      ctx.fill();
      ctx.closePath();
      bckgDimensions = [size, size].map(n => n + 5);
      node.__bckgDimensions = bckgDimensions;
    } else {
      ctx.beginPath();
      ctx.fillStyle = nodeBoundaryColor;
      ctx.arc(node.x, node.y, size + 3, 0, 2 * Math.PI, false);
      ctx.fill();
      ctx.closePath();
      ctx.beginPath();
      ctx.fillStyle = nodeColor;
      ctx.arc(node.x, node.y, size, 0, 2 * Math.PI, false);
      ctx.fill();
      ctx.closePath();
      ctx.lineWidth = 2 / globalScale;
      ctx.beginPath();
      ctx.fillStyle = node.type != TYPE_VERTEX.BLACK_ENTITY ? FONTCOLOR_NODE_IN_MAP.COMMON : FONTCOLOR_NODE_IN_MAP.BLACK_ENTITY;
      ctx.strokeStyle =
        node.type != TYPE_VERTEX.BLACK_ENTITY ? FONTCOLOR_NODE_IN_MAP.COMMON_STROKE : FONTCOLOR_NODE_IN_MAP.BLACK_ENTITY_STROKE;
      ctx.font = `${fontSize}px Sans-Serif`;
      ctx.textAlign = 'center';
      ctx.textBaseline = 'middle';
      ctx.strokeText(label, node.x, node.y - size - 5);
      ctx.fillText(label, node.x, node.y - size - 5);

      bckgDimensions = [2 * size + 3, 2 * size + 3].map(n => n + 5);
      node.__bckgDimensions = bckgDimensions;
      ctx.closePath();
    }
  }

  private renderSelectedNodes(node: any, ctx: any, globalScale: any, tick: number) {
    const label = node.label;
    const size = this.getSizeFromType(node) * 1.2;
    const fontSize = (this.getFontsizeFromType(node.type) / globalScale) * 0.8;
    const nodeColor = d3.rgb(this.verticeColorByCluster(node.mainCluster)).brighter(2.5);
    const nodeBoundaryColor = nodeColor.brighter(3);
    const textWidth = ctx.measureText(label).width;
    const bckgDimensions = [textWidth, fontSize].map(n => n + fontSize * 0.2); // some padding

    node.__bckgDimensions = bckgDimensions; // to re-use in nodePointerAreaPaint
    if (!node.x || !node.y) {
      return;
    }

    if (tick <= STEP_CONNECTOME_RENDER.SECOND) {
      ctx.beginPath();
      ctx.fillStyle = nodeColor;
      ctx.arc(node.x, node.y, size, 0, 2 * Math.PI, false);
      ctx.fill();
      ctx.closePath();
      bckgDimensions = [size, size].map(n => n + 5);
      node.__bckgDimensions = bckgDimensions;
    } else {
      ctx.beginPath();
      ctx.strokeStyle = nodeBoundaryColor;
      ctx.strokeRect(
        node.x - Math.round(size * 1.4),
        node.y - Math.round(size * 1.4),
        Math.round(size * 1.4 * 2),
        Math.round(size * 1.4 * 2)
      );
      ctx.stroke();
      ctx.closePath();
      if (node.favorite) {
        const radgradFavoriteCrown = null;
        try {
          radgradFavoriteCrown = ctx.createRadialGradient(node.x, node.y, 0, node.x, node.y, Math.round(size + 10));
          radgradFavoriteCrown.addColorStop(0, nodeColor);
          radgradFavoriteCrown.addColorStop(0.8, d3.rgb(255, 255, 0).brighter(2));
          radgradFavoriteCrown.addColorStop(1, 'rgb(255,255,0,0)');
        } catch (error) {
          console.log(error);
          radgradFavoriteCrown = nodeColor;
        }
        ctx.beginPath();
        ctx.fillStyle = radgradFavoriteCrown;
        ctx.arc(node.x, node.y, Math.round(size + 10), 0, 2 * Math.PI, false);
        ctx.fill();
        ctx.closePath();
      }
      ctx.beginPath();
      ctx.fillStyle = nodeBoundaryColor;
      ctx.arc(node.x, node.y, Math.round(size * 1.1), 0, 2 * Math.PI, false);
      ctx.fill();
      ctx.closePath();

      ctx.lineWidth = 2 / globalScale;
      ctx.beginPath();
      const radgradSelected = null;
      try {
        radgradSelected = nodeColor;
      } catch (error) {
        console.log(error);
        radgradSelected = nodeColor;
      }
      ctx.fillStyle = radgradSelected;
      ctx.arc(node.x, node.y, size, 0, 2 * Math.PI, false);
      ctx.fill();
      ctx.closePath();

      ctx.beginPath();
      ctx.fillStyle = node.type != TYPE_VERTEX.BLACK_ENTITY ? FONTCOLOR_NODE_IN_MAP.COMMON : FONTCOLOR_NODE_IN_MAP.BLACK_ENTITY;
      ctx.strokeStyle =
        node.type != TYPE_VERTEX.BLACK_ENTITY ? FONTCOLOR_NODE_IN_MAP.COMMON_STROKE : FONTCOLOR_NODE_IN_MAP.BLACK_ENTITY_STROKE;
      ctx.font = `${fontSize}px Sans-Serif`;
      ctx.textAlign = 'center';
      ctx.textBaseline = 'middle';
      ctx.strokeText(label, node.x, node.y - size - 5);
      ctx.fillText(label, node.x, node.y - size - 5);
      bckgDimensions = [Math.round(2 * size * 1.4), Math.round(2 * size * 1.4)].map(n => n + 5);
      node.__bckgDimensions = bckgDimensions;
      ctx.closePath();
    }
  }

  private renderFavorite(node: any, ctx: any, globalScale: any, tick: number) {
    const label = node.label;
    const size = this.getSizeFromType(node);
    const fontSize = this.getFontsizeFromType(node.type);
    const nodeColor = d3.rgb(this.verticeColorByCluster(node.mainCluster));
    const nodeBoundaryColor = nodeColor.brighter(1.2);
    const textWidth = ctx.measureText(label).width;
    const bckgDimensions = [textWidth, fontSize].map(n => n + fontSize * 0.2); // some padding

    node.__bckgDimensions = bckgDimensions; // to re-use in nodePointerAreaPaint
    if (!node.x || !node.y) {
      return;
    }

    if (tick <= STEP_CONNECTOME_RENDER.SECOND) {
      ctx.beginPath();
      ctx.fillStyle = nodeColor;
      ctx.arc(node.x, node.y, size, 0, 2 * Math.PI, false);
      ctx.fill();
      ctx.closePath();
      bckgDimensions = [size, size].map(n => n + 5);
      node.__bckgDimensions = bckgDimensions;
    } else {
      const radgradFavoriteCrown = null;
      try {
        radgradFavoriteCrown = ctx.createRadialGradient(node.x, node.y, 0, node.x, node.y, Math.round(size + 10));
        radgradFavoriteCrown.addColorStop(0, nodeColor);
        radgradFavoriteCrown.addColorStop(0.8, d3.rgb(255, 255, 0).brighter(2));
        radgradFavoriteCrown.addColorStop(1, 'rgb(255,255,0,0)');
      } catch (error) {
        console.log(error);
        radgradFavoriteCrown = node._color;
      }
      ctx.beginPath();
      ctx.fillStyle = radgradFavoriteCrown;
      ctx.arc(node.x, node.y, Math.round(size + 10), 0, 2 * Math.PI, false);
      ctx.fill();
      ctx.closePath();
      ctx.beginPath();
      ctx.fillStyle = nodeBoundaryColor;
      ctx.arc(node.x, node.y, size + 3, 0, 2 * Math.PI, false);
      ctx.fill();
      ctx.closePath();
      ctx.beginPath();
      ctx.fillStyle = d3.rgb(nodeColor);
      ctx.arc(node.x, node.y, size, 0, 2 * Math.PI, false);
      ctx.fill();
      ctx.closePath();
      ctx.lineWidth = 2;
      ctx.beginPath();
      ctx.fillStyle = node.type != TYPE_VERTEX.BLACK_ENTITY ? FONTCOLOR_NODE_IN_MAP.COMMON : FONTCOLOR_NODE_IN_MAP.BLACK_ENTITY;
      ctx.strokeStyle =
        node.type != TYPE_VERTEX.BLACK_ENTITY ? FONTCOLOR_NODE_IN_MAP.COMMON_STROKE : FONTCOLOR_NODE_IN_MAP.BLACK_ENTITY_STROKE;
      ctx.font = `${fontSize}px Sans-Serif`;
      ctx.textAlign = 'center';
      ctx.textBaseline = 'middle';
      if (globalScale >= 0.05) {
        ctx.strokeText(label, node.x, node.y - size - 5);
        ctx.fillText(label, node.x, node.y - size - 5);
        node.__bckgDimensions = bckgDimensions;
      }
      bckgDimensions = [Math.round(2 * size * 1.4), Math.round(2 * size * 1.4)].map(n => n + 5);
      node.__bckgDimensions = bckgDimensions;
      ctx.closePath();
    }
  }

  private chartResize() {
    //console.log('window resize');
    this.calculateCanvasSize();
    if (this.graph) {
      //console.log('graph resize');
      this.graph.width(this.width).height(this.height);
    }
  }

  //map interaction
  private zoomOnSelection(label: string) {
    if (!label) {
      return;
    }
    const node = this.vertice(label);
    if (node) {
      const nodeCount = this.selectedNodes.size + this.nodesLinkedToSelectedNodes.size;
      let padding = 100;
      console.log('nodeCount1', nodeCount, padding);
      if ((node.x && node.y) || (node.fx && node.fy)) {
        //   if (nodeCount < 3) {
        //     console.log('node', node);
        //     this.graph.centerAt(node.fx ? node.fx : node.x, node.fy ? node.fy : node.y, 200);
        //     this.graph.zoom(0.5, 2000);
        //     return;
        //   } else if (nodeCount < 6) {
        //     this.graph.centerAt(node.fx ? node.fx : node.x, node.fy ? node.fy : node.y, 200);
        //     this.graph.zoom(0.25, 2000);
        //     return;
        //   }
        // }
        if (nodeCount < 10) {
          padding = 150;
        } else {
          padding = 50;
        }
        this.graph.zoomToFit(0, padding, node => this.selectedNodes.has(node.label) || this.nodesLinkedToSelectedNodes.has(node.label));
      }
    }
  }

  private clearSelection() {
    this.selectedNodes.clear();
    this.nodesLinkedToSelectedNodes.clear();
    this.linksLinkedToSelectedNodes.clear();
  }

  private createSelection(label: string) {
    const node = this.vertice(label);
    this.selectedNodes.clear();
    this.nodesLinkedToSelectedNodes.clear();
    if (node) {
      this.selectedNodes.add(label);
      node.entities.forEach(element => {
        this.nodesLinkedToSelectedNodes.add(element);
      });
    }
  }

  public fitScreen() {
    if (this.graph) {
      this.graph.zoomToFit(500, 150, node => true);
    }
  }

  public zoomIn() {
    if (this.graph) {
      const zoom = this.graph.zoom();
      this.graph.zoom(Math.min(1000, zoom + 0.05));
    }
  }

  public zoomOut() {
    if (this.graph) {
      const zoom = this.graph.zoom();
      this.graph.zoom(Math.max(0.005, zoom - 0.05));
    }
  }

  fullscreen = false;

  private resizeFullscreen() {
    //console.log('resize event detected!');
    if (this.fullscreen) {
      //this.containerCSS = $('#container-block').css
      document.body.setAttribute('data-menu', 'connectome-fullscreen');

      //resize child?
    } else {
      document.body.setAttribute('data-menu', 'connectome');
      //$('.panel-sidebar').css({ top: this.sideBarTop > 100 ? this.sideBarTop : 150 });
    }
    this.chartResize();
  }

  fullScreen() {
    this.fullscreen = !this.fullscreen;
    this.resizeFullscreen();
  }

  dummytimer: NodeJS.Timeout = null;
  //training
  displayDummyChart(run: boolean) {
    const elem = document.getElementsByClassName('connectome-area');
    if (!elem) {
      return;
    }

    if (elem.length === 0) {
      return;
    }

    if (run) {
      const initData = {
        nodes: [{ id: 0, label: 'Training', group: 'en' }],
        links: [],
      };

      this.calculateCanvasSize();
      this.graph = ForceGraph()(elem[0])
        .graphData(initData)
        .backgroundColor(MAP_BACKGROUND_COLOR)
        .width(this.width)
        .height(this.height)
        .linkWidth(1)
        .linkColor(link => MINI_MAP_BACKGROUND_COLOR);

      this.dummytimer = setInterval(() => {
        const words = [
          { language: 'en', word: 'Training' },
          { language: 'en', word: 'Coaching' },
          { language: 'en', word: 'Discipline' },
          { language: 'en', word: 'Drill' },
          { language: 'en', word: 'Education' },
          { language: 'en', word: 'Exercice' },
          { language: 'en', word: 'Guidance' },
          { language: 'en', word: 'Instruction' },
          { language: 'en', word: 'Practice' },
          { language: 'en', word: 'Schooling' },
          { language: 'en', word: 'Teaching' },
          { language: 'en', word: 'Workout' },
          { language: 'en', word: 'Background' },
          { language: 'en', word: 'Basics' },
          { language: 'en', word: 'Buildup' },
          { language: 'en', word: 'Cultivation' },
          { language: 'en', word: 'Domestication' },
          { language: 'en', word: 'Foundation' },
          { language: 'en', word: 'Grounding' },
          { language: 'en', word: 'Groundwork' },
          { language: 'en', word: 'Indoctrination' },
          { language: 'en', word: 'Preliminaries' },
          { language: 'en', word: 'Principles' },
          { language: 'en', word: 'Readying' },
          { language: 'en', word: 'Seasoning' },
          { language: 'en', word: 'Sharpening' },
          { language: 'en', word: 'Tuition' },
          { language: 'en', word: 'Tune-up' },
          { language: 'en', word: 'Tutelage' },
          { language: 'en', word: 'Upbringing' },
          { language: 'en', word: 'Warmup' },
          { language: 'en', word: 'Chalk Talk' },
          { language: 'fr', word: 'Apprendre' },
          { language: 'fr', word: 'Inculquer' },
          { language: 'fr', word: 'Initier' },
          { language: 'fr', word: 'Instruire' },
          { language: 'fr', word: 'Professer' },
          { language: 'fr', word: 'Démontrer' },
          { language: 'fr', word: "Donner l'exemple" },
          { language: 'fr', word: 'Indiquer' },
          { language: 'fr', word: 'Montrer' },
          { language: 'fr', word: 'Montrer la voie' },
          { language: 'fr', word: 'Montrer le chemin' },
          { language: 'fr', word: 'Expliquer' },
          { language: 'fr', word: 'Cultiver' },
          { language: 'fr', word: 'Évangéliser' },
          { language: 'fr', word: 'Catéchiser' },
          { language: 'fr', word: 'Civiliser' },
          { language: 'fr', word: 'Communiquer' },
          { language: 'fr', word: 'Convertir' },
          { language: 'fr', word: 'Développer' },
          { language: 'fr', word: 'Informer' },
          { language: 'fr', word: 'Initier' },
          { language: 'fr', word: 'Prêcher' },
          { language: 'fr', word: 'Propager' },
          { language: 'fr', word: 'Rapporter' },
          { language: 'fr', word: 'Révéler' },
          { language: 'fr', word: 'Renseigner' },
          { language: 'fr', word: 'Signifier' },
          { language: 'fr', word: 'Soutenir' },
          { language: 'fr', word: 'Styler' },
          { language: 'fr', word: 'Suggérer' },
        ];
        const colors = new Map([
          ['en', '#00CD6C'],
          ['fr', '#FF1F5B'],
          ['kr', '#009ADE'],
          ['vi', '#AF58BA'],
        ]);
        const { nodes, links } = this.graph.graphData();
        const id = nodes.length;
        const values = words[Math.floor(Math.random() * words.length)];
        const label = values.word;
        const group = values.language;
        if (!this.vertices || this.vertices.length < 2) {
          this.graph
            .graphData({
              nodes: [...nodes, { id, label: label, group: group }],
              links: [...links, { source: id, target: Math.round(Math.random() * (id - 1)) }],
            })
            .nodeCanvasObject((node, ctx, globalScale) => {
              const label: string = node.label;
              const size: number = 10 + 2 * label.length;
              const fontSize = 18 / globalScale;
              const nodeColor = d3.rgb(colors.get(node.group));

              ctx.beginPath();
              ctx.strokeStyle = FONTCOLOR_NODE_IN_MAP.COMMON_STROKE;
              ctx.lineWidth = 2 / globalScale;
              ctx.fillStyle = nodeColor;
              ctx.font = `${fontSize}px Sans-Serif`;
              ctx.textAlign = 'center';
              ctx.textBaseline = 'middle';
              ctx.strokeText(label, node.x, node.y);
              ctx.fillText(label, node.x, node.y);
              ctx.closePath();
            })
            .nodeCanvasObjectMode(() => 'replace');
        } else {
          console.log('clear dummytimer', this.dummytimer);
          clearInterval(this.dummytimer);
        }
      }, 1000);
    } else {
      if (this.dummytimer) {
        console.log('clear dummytimer');
        clearInterval(this.dummytimer);
      }
      this.dummytimer = null;
      const initData = {
        nodes: [],
        links: [],
      };

      this.graph?.graphData(initData);
    }
  }

  destroyed() {
    if (this.dummytimer) {
      clearInterval(this.dummytimer);
    }
    this.dummytimer = null;

    const initData = {
      nodes: [],
      links: [],
    };

    console.log('destroyed');
    this.graph?.graphData(initData);
  }
}
