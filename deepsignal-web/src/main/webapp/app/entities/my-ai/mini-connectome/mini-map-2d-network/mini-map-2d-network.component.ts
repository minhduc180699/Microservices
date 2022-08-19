// @ts-nocheck
import { MINI_MAP_BACKGROUND_COLOR, TYPE_VERTEX, FONTCOLOR_NODE_IN_MAP } from '@/shared/constants/ds-constants';
import { ConnectomeNetworkEdge } from '@/shared/model/connectome-network-edge.model';
import { ConnectomeNetworkStatus } from '@/shared/model/connectome-network-status.model';
import { ConnectomeNetworkVertex } from '@/shared/model/connectome-network-vertex.model';
import { ConnectomeNetwork } from '@/shared/model/connectome-network.model';
import MiniMapBottomBar from '@/entities/my-ai/mini-connectome/mini-map-bottom-bar/mini-map-bottom-bar.vue';
import * as d3 from 'd3';
import ForceGraph, { ForceGraphInstance, LinkObject } from 'force-graph';
import { Component, Vue, Watch, Prop, Emit } from 'vue-property-decorator';
import { namespace } from 'vuex-class';

export const STEP_CONNECTOME_RENDER = {
  FIRST: 10,
  SECOND: 80,
  THIRD: 120,
  FOURTH: 200,
};

const networkStore = namespace('connectomeNetworkStore');
const miniNetworkStore = namespace('miniConnectomeNetworkStore');
const miniMapNetworkStore = namespace('miniMapNetworkStore');

@Component({
  components: {
    'mini-map-bottom-bar': MiniMapBottomBar,
  },
})
export default class MiniMap2dNetwork extends Vue {
  selectedNodes: Set<string> = new Set();
  nodesLinkedToSelectedNodes: Set<string> = new Set();
  linksLinkedToSelectedNodes: Set<string> = new Set();

  private onInit = true;
  private onZoom = false;
  private graph: ForceGraphInstance = null;

  private tick = 0;
  private isFirstStepPassed = false;
  private isUserStartInteract = false;

  private width = 350;
  private height = 460;

  @Prop({
    default: () => {
      return [];
    },
  })
  readonly propIds: Array<string>;

  @networkStore.Getter
  public connectomeId!: string;

  @networkStore.Getter
  public lang!: string;

  @networkStore.Getter
  public connectomeStatus!: ConnectomeNetworkStatus;

  @networkStore.Getter
  public lastUpdatedAt!: string;

  @networkStore.Getter
  public verticeColorByCluster!: (mainCluster: string) => d3.RGBColor;

  @miniNetworkStore.Getter
  public miniNetworkData!: ConnectomeNetwork;

  @miniNetworkStore.Getter
  public miniVertice!: (label: string) => ConnectomeNetworkVertex;

  @miniNetworkStore.Getter
  public miniVertices!: Array<ConnectomeNetworkVertex>;

  @miniNetworkStore.Getter
  public miniEdges!: Array<ConnectomeNetworkEdge>;

  @miniMapNetworkStore.Getter
  public entityLabelSelected!: string;

  @miniMapNetworkStore.Getter
  public centerAtSelectionCommand!: string;

  @miniMapNetworkStore.Getter
  public refreshMapCommand!: string;

  @miniMapNetworkStore.Getter
  public isMapInitialized!: boolean;

  @miniMapNetworkStore.Getter
  public searchResult!: Array<ConnectomeNetworkVertex>;

  @miniNetworkStore.Action
  public getMiniConnectomeData!: (payload: {
    connectomeId: string;
    language: string;
    personalDocumentIds: Array<string>;
    feedIds: Array<string>;
  }) => Promise<any>;

  @miniMapNetworkStore.Action
  public setSelectedEntity!: (payload: { title: string; srcLang: string }) => void;

  @miniMapNetworkStore.Action
  public clearSelectedEntity!: () => void;

  @miniMapNetworkStore.Action
  public setMapInitialized!: () => void;

  @Watch('lastUpdatedAt')
  onLastUpdatedAtUpdated(lastUpdateDate: string) {
    // console.log('onLastUpdatedAtUpdated', lastUpdateDate);
    // if (!lastUpdateDate) {
    //   return;
    // }
    this.loadData();
  }

  @Watch('propIds')
  onPropIdsChanged(value) {
    console.log('onPropIdsChanged', value);
    // if (!lastUpdateDate) {
    //   return;
    // }
    this.loadData();
  }

  @Watch('miniNetworkData')
  onNetworkDataUpdated(data: ConnectomeNetwork) {
    console.log('onNetworkDataUpdated', data);
    this.renderMap();
  }

  @Watch('miniVertices')
  onVerticesUpdated(data: Array<ConnectomeNetworkVertex>) {
    //console.log('onVerticesUpdated', data);
  }

  @Watch('refreshMapCommand')
  onRefreshMapCommandChanged(count: number) {
    console.log('refreshMapCommand', count);
    //this.renderMap();
  }

  @Watch('centerAtSelectionCommand')
  onCenterAtSelectionCommandChanged(count: number) {
    console.log('centerAtSelectionCommand:', count);
    if (this.entityLabelSelected) {
      this.zoomOnSelection(this.entityLabelSelected);
    }
  }

  @Watch('entityLabelSelected')
  onEntitySelectedChanged(label: string) {
    this.clearSelection();
    if (!label) {
      return;
    }
    console.log('entity selected changed', label);
    this.createSelection(label);
    this.zoomOnSelection(label);
  }

  calculateCanvasSize() {
    this.width = Math.max($('.connectome-area').width(), 350);
    this.height = Math.max($('.connectome-area').height(), 350);
    console.log(this.width, this.height);
  }

  @Watch('$route', { immediate: true, deep: true })
  onUrlChange(newVal) {
    console.log('MiniMap2dNetwork', newVal);

    if (newVal.name.localeCompare('Mini2DNetwork') == 0) {
      document.body.setAttribute('data-menu', 'connectome');
    }

    if (newVal.name.localeCompare('Detail') == 0) {
      //this.loadData();
    }
  }

  isMounted = false;
  mounted() {
    console.log('mounted');
    window.onresize = this.chartResize;
    console.log('MOUNTED: Total after ', this.miniVertices.length);
    console.log('TICK ', this.tick);

    //this.loadData();
    this.isMounted = true;
  }

  data() {
    return {};
  }

  private arrayDiff(a: Array<any>, b: Array<any>) {
    return [...a.filter(x => !b.includes(x)), ...b.filter(x => !a.includes(x))];
  }

  loadData() {
    if (!this.connectomeStatus) {
      return;
    }

    if (!this.connectomeId) {
      return;
    }

    if (!this.lang) {
      return;
    }
    const id = this.$route.query.Id;

    if (this.propIds || id) {
      console.log('propIds', this.propIds);
      const ids = id ? [id] : this.propIds ? this.propIds : [];

      console.log('request mini connectome', this.connectomeId, this.lang, ids);
      this.getMiniConnectomeData({
        connectomeId: this.connectomeId,
        language: this.lang,
        ids: ids,
      })
        .then(res => {
          if (!res) {
            console.log('res null', res);
            this.onMiniConnectomeDisplayed(false);
            return;
          }
          //this.renderMap();
        })
        .catch(reason => {
          console.log('catch get mini connectome', reason);
          this.onMiniConnectomeDisplayed(false);
        });
    }
  }

  renderMap() {
    if (this.miniVertices && this.miniVertices.length > 0 && this.miniEdges) {
      const newNodes = new Array<any>();
      newNodes = this.miniVertices.map(x => x);
      const newEdges = this.miniEdges.map(y => y);

      if (this.miniVertices.filter(vertex => vertex.type === TYPE_VERTEX.ENTITY || vertex.type === TYPE_VERTEX.BLACK_ENTITY).length == 0) {
        console.log('Nothing to render');
        this.onMiniConnectomeDisplayed(false);
        return;
      }

      console.log('black_entity after ', this.miniVertices.filter(vertex => vertex.type === TYPE_VERTEX.BLACK_ENTITY).length);
      console.log('cluster after ', this.miniVertices.filter(vertex => vertex.type === TYPE_VERTEX.CLUSTER).length);
      console.log('entities after ', this.miniVertices.filter(vertex => vertex.type === TYPE_VERTEX.ENTITY).length);
      console.log('MOUNTED: Total after ', this.miniVertices.length);

      this.tick = 0;
      this.clearSelection();
      this.calculateCanvasSize();
      const elem = document.getElementsByClassName('connectome-area');
      if (!elem) {
        return;
      }

      if (elem.length === 0) {
        return;
      }
      this.graph = ForceGraph()(elem.item(0))
        .graphData({ nodes: newNodes, links: newEdges })
        .width(this.width)
        .height(this.height)
        .backgroundColor(MINI_MAP_BACKGROUND_COLOR)
        .nodeId('label')
        .nodeLabel(node => node.label)
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
        .d3Force(
          'collision',
          d3.forceCollide(node => {
            return this.getFactorCollisionFromType(node.type) * this.getSizeFromType(node);
          })
        )
        .linkCurvature(link => {
          return this.renderCurvatureLink(link, this.tick);
        })
        .linkColor(link => {
          return this.renderColorLink(link, this.tick);
        })
        .linkSource('from')
        .linkTarget('to')
        .linkVisibility(link => {
          return this.isLinkVisible(link);
        })
        .linkWidth(link => this.renderLinkWidth(link, this.tick))
        .linkLineDash(link => this.renderLinkDash(link, this.tick))
        .linkDirectionalParticles(link => this.numberParticultVisible(link, this.tick))
        .linkDirectionalParticleWidth(link => this.renderDirectionalParticleWidth(link, this.tick))
        .linkDirectionalParticleColor(link => this.renderDirectionalParticleColor(link, this.tick))
        .autoPauseRedraw(false)
        .onBackgroundClick(event => {
          this.isUserStartInteract = true;
          this.clearSelectedEntity();
        })
        .onNodeClick((node, event) => {
          if (!node.label) return;
          if (node.TYPE === TYPE_VERTEX.ROOT || node.TYPE === TYPE_VERTEX.CLUSTER) return;
          console.log(event);
          this.isUserStartInteract = true;
          this.clearSelectedEntity();
          this.setSelectedEntity({ title: node.label, srcLang: this.lang });
        })
        .onZoom(event => {})
        .onZoomEnd(event => {})
        .enableNodeDrag(false)
        .enableZoomInteraction(true)
        .warmupTicks(50)
        .onEngineTick(() => {
          if (!this.isFirstStepPassed) {
            if (this.tick > STEP_CONNECTOME_RENDER.FIRST) {
              this.graph.zoomToFit(300, 100, node => true);
              this.isFirstStepPassed = true;
            }
          }
          if (!this.isUserStartInteract) {
            if (this.tick > STEP_CONNECTOME_RENDER.SECOND) {
              if (this.entityLabelSelected) {
                this.createSelection(this.entityLabelSelected);
                this.zoomOnSelection(this.entityLabelSelected);
              } else {
                this.graph.zoomToFit(200, 100, node => true);
              }
              this.isUserStartInteract = true;
            }
          }
          this.tick++;
        })
        .onEngineStop(() => {});
      this.onMiniConnectomeDisplayed(true);
    } else {
      console.log('Nothing to render');
      this.onMiniConnectomeDisplayed(false);
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

  private isLinkVisible(link: LinkObject) {
    if (!link) {
      return false;
    }

    if (!link.source) {
      return false;
    }

    if (!link.target) {
      return false;
    }

    if (typeof link.source === 'string') {
      if (link.source === this.miniNetworkData.connectomeId) {
        return true;
      }
    }

    if (typeof link.source === 'NodeObject') {
      if (link.source.type === TYPE_VERTEX.ROOT) {
        return false;
      }

      if (link.source.type === TYPE_VERTEX.CLUSTER) {
        return false;
      }
    }

    if (typeof link.source === 'object') {
      if (link.source.type === TYPE_VERTEX.ROOT) {
        return false;
      }

      if (link.source.type === TYPE_VERTEX.CLUSTER) {
        return false;
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
        return false;
      }

      if (link.target.disable) {
        return false;
      }
    }

    if (this.isSelectedLink(link)) {
      return true;
    }

    return true;
  }

  private renderCurvatureLink<type>(link: any, tick: number): number | undefined {
    if (tick >= STEP_CONNECTOME_RENDER.THIRD) {
      return 0.3;
    } else {
      return 0;
    }
  }

  private isNodeVisible(node: any) {
    if (!node) {
      return false;
    }

    if (node.disable) {
      return false;
    }

    if (node.type === TYPE_VERTEX.ROOT) {
      return false;
    }

    if (node.type === TYPE_VERTEX.CLUSTER) {
      return false;
    }

    return true;
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
      const linkColor =
        link.source.type === TYPE_VERTEX.CLUSTER || link.source.type === TYPE_VERTEX.ROOT
          ? MINI_MAP_BACKGROUND_COLOR
          : this.verticeColorByCluster(link.source.mainCluster);
      if (this.selectedNodes && this.selectedNodes.size > 0) {
        return this.isSelectedLink(link) ? d3.rgb(linkColor).darker(1) : d3.rgb(linkColor).brighter(0.3);
      } else {
        return d3.rgb(linkColor);
      }
    } else {
      return link.__indexColor;
    }
  }

  private renderDirectionalParticleColor<type>(link: LinkObject, tick: number): string | RGBColor | undefined {
    if (link.source && tick >= STEP_CONNECTOME_RENDER.FIRST) {
      const linkColor =
        link.source.type === TYPE_VERTEX.CLUSTER || link.source.type === TYPE_VERTEX.ROOT
          ? MINI_MAP_BACKGROUND_COLOR
          : this.verticeColorByCluster(link.source.mainCluster);
      return this.isSelectedLink(link) ? '#000000' : d3.rgb(linkColor).brighter(1);
    } else {
      return link.__indexColor;
    }
  }

  private renderDirectionalParticleWidth<type>(link: LinkObject, tick: number): number | undefined {
    if (tick >= STEP_CONNECTOME_RENDER.FIRST) {
      return this.isSelectedLink(link) ? 8 : 0;
    } else {
      return 0;
    }
  }

  private renderLinkWidth<type>(link: LinkObject, tick: number): number | undefined {
    if (tick >= STEP_CONNECTOME_RENDER.FIRST) {
      return this.isSelectedLink(link) ? 4 : 1;
    } else {
      return 1;
    }
  }

  private renderLinkDash<type>(link: LinkObject, tick: number): number | undefined {
    if (tick >= STEP_CONNECTOME_RENDER.FIRST) {
      return this.isSelectedLink(link) ? [15, 5] : null;
    } else {
      return null;
    }
  }

  private renderNodes(node: any, ctx: any, globalScale: any, tick: number) {
    if (node.type === TYPE_VERTEX.CLUSTER) {
      return;
    }

    if (node.type === TYPE_VERTEX.ROOT) {
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

      ctx.lineWidth = 1;
      ctx.beginPath();
      ctx.fillStyle = node.type != TYPE_VERTEX.BLACK_ENTITY ? FONTCOLOR_NODE_IN_MAP.COMMON : FONTCOLOR_NODE_IN_MAP.BLACK_ENTITY;
      ctx.strokeStyle =
        node.type != TYPE_VERTEX.BLACK_ENTITY ? FONTCOLOR_NODE_IN_MAP.COMMON_STROKE : FONTCOLOR_NODE_IN_MAP.BLACK_ENTITY_STROKE;
      ctx.textAlign = 'center';
      ctx.textBaseline = 'middle';
      // if (globalScale >= 0.1 || node.entities.length > 5) {
      ctx.font = `${fontSize}px Sans-Serif`;
      ctx.strokeText(label, node.x, node.y - size - 5);
      ctx.fillText(label, node.x, node.y - size - 5);
      node.__bckgDimensions = bckgDimensions;
      // } else if (node.entities.length < 2) {
      //   if (globalScale >= 0.08) {
      //     fontSize = this.getFontsizeFromType(node.type) / (globalScale * 2);
      //     ctx.font = `${fontSize}px Sans-Serif`;
      //     ctx.strokeText(label, node.x, node.y - size - 5);
      //     ctx.fillText(label, node.x, node.y - size - 5);
      //     node.__bckgDimensions = bckgDimensions;
      //   } else {
      //     if (globalScale >= 0.05) {
      //       fontSize = this.getFontsizeFromType(node.type) / (globalScale * 3);
      //       ctx.font = `${fontSize}px Sans-Serif`;
      //       ctx.strokeText(label, node.x, node.y - size - 5);
      //       ctx.fillText(label, node.x, node.y - size - 5);
      //       node.__bckgDimensions = bckgDimensions;
      //     }
      //   }
      // }
      ctx.closePath();

      bckgDimensions = [2 * size + 3, 2 * size + 3].map(n => n + 5);
      node.__bckgDimensions = bckgDimensions;
    }
  }

  private getSizeFromType(node: ConnectomeNetworkVertex): number | undefined {
    const size = 2 * (node.entities?.length == 0 ? 1 : Math.min(20, node.entities?.length));
    switch (node.type) {
      case TYPE_VERTEX.CLUSTER:
        return 2;
      case TYPE_VERTEX.ENTITY:
        return 2 + size;
      case TYPE_VERTEX.BLACK_ENTITY:
        return 2 + size;
      case TYPE_VERTEX.ROOT:
        return 2;
      default:
        return 30;
    }
  }

  private getFontsizeFromType(nodeType: TYPE_VERTEX): number | undefined {
    switch (nodeType) {
      case TYPE_VERTEX.CLUSTER:
        return 26;
      case TYPE_VERTEX.ENTITY:
        return 20;
      case TYPE_VERTEX.BLACK_ENTITY:
        return 18;
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
        return 5;
      case TYPE_VERTEX.BLACK_ENTITY:
        return 5;
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
    const nodeColor = d3.rgb(this.verticeColorByCluster(node.mainCluster));
    const nodeBoundaryColor = nodeColor;
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
      ctx.lineWidth = 2 / (3 * globalScale);
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
    const nodeColor = d3.rgb(this.verticeColorByCluster(node.mainCluster));
    const nodeBoundaryColor = nodeColor.darker(1.2);
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
          radgradFavoriteCrown = ctx.createRadialGradient(node.x, node.y, 0, node.x, node.y, Math.round(size * 1.4));
          radgradFavoriteCrown.addColorStop(0, nodeColor);
          radgradFavoriteCrown.addColorStop(0.8, d3.rgb(255, 255, 0).brighter(2));
          radgradFavoriteCrown.addColorStop(1, 'rgb(255,255,0,0)');
        } catch (error) {
          console.log(error);
          radgradFavoriteCrown = nodeColor;
        }
        ctx.beginPath();
        ctx.fillStyle = radgradFavoriteCrown;
        ctx.arc(node.x, node.y, Math.round(size * 1.4), 0, 2 * Math.PI, false);
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
      ctx.lineWidth = 1;
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
    console.log('window resize');
    this.calculateCanvasSize();
    if (this.graph) {
      console.log('graph resize');
      this.graph.width(this.width).height(this.height);
    }
  }

  //map interaction
  private zoomOnSelection(label: string) {
    if (!label) {
      return;
    }
    const node = this.miniVertice(label);
    if (node) {
      const nodeCount = this.selectedNodes.size + this.nodesLinkedToSelectedNodes.size;
      let padding = 10;
      console.log('nodeCount1', nodeCount, padding);
      // if ((node.x && node.y) || (node.fx && node.fy)) {
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
      if (nodeCount < 6) {
        padding = 150;
      } else {
        padding = 50;
      }
      this.graph.zoomToFit(0, padding, node => this.selectedNodes.has(node.label) || this.nodesLinkedToSelectedNodes.has(node.label));
    }
  }

  private clearSelection() {
    this.selectedNodes.clear();
    this.nodesLinkedToSelectedNodes.clear();
    this.linksLinkedToSelectedNodes.clear();
  }

  private createSelection(label: string) {
    const node = this.miniVertice(label);
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
      this.graph.zoomToFit(500, 10, node => true);
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

  @Emit('onMiniConnectomeDisplayed')
  onMiniConnectomeDisplayed(isDisplay: boolean) {
    console.log('onMiniConnectomeDisplayed', isDisplay);

    return isDisplay;
  }
}
