// @ts-nocheck
import { FONTCOLOR_NODE_IN_MAP, MAP_BACKGROUND_COLOR } from '@/shared/constants/ds-constants';
import { ConnectomeNode } from '@/shared/model/connectome-node.model';
import { MapLink } from '@/shared/model/map-link.model';
import { MapNode } from '@/shared/model/map-node.model';
import * as d3 from 'd3';
import ForceGraph, { ForceGraphInstance } from 'force-graph';
import { Component, Vue, Watch } from 'vue-property-decorator';
import { namespace } from 'vuex-class';

export const STEP_CONNECTOME_RENDER = {
  FIRST: 10,
  SECOND: 80,
  THIRD: 120,
  FOURTH: 200,
};

const mapStore = namespace('mapStore');

@Component({
  components: {},
})
export default class NodesMap extends Vue {
  noData = 'no data';
  noDate = 'no date';
  defaultLangKey = 'en';

  private onInit = true;
  private onZoom = false;
  private graph: ForceGraphInstance = null;

  private tick = 0;
  private isFirstStepPassed = false;
  private isUserStartInteract = false;

  private width = 350;
  private height = 460;

  @mapStore.Getter
  public getNodes!: Array<MapNode>;

  @mapStore.Getter
  public getLinks!: Array<MapLink>;

  @mapStore.Getter
  public nodesHaveChanged!: number;

  @mapStore.Getter
  public linksHaveChanged!: number;

  @mapStore.Getter
  public mapHaveChanged!: number;

  @mapStore.Getter
  public nodesSelectedHaveChanged!: number;

  @mapStore.Mutation
  public setNodesSelected!: (payload: { newNodesSelected: Array<MapNode> }) => void;

  @Watch('mapHaveChanged')
  onMapChanged(val: number) {
    console.log('onMapChanged', val);

    this.updateNetworkMap();
  }

  @Watch('nodesSelectedHaveChanged')
  onNodeSelectedChanged(val: number) {
    console.log('onNodeSelectedChanged', val);
  }

  calculateCanvasSize() {
    this.width = Math.max($('.nodes-map-area').width(), 350);
    this.height = Math.max($('.nodes-map-area').height(), 350);
  }

  @Watch('$route', { immediate: true, deep: true })
  onUrlChange(newVal: Route) {
    console.log('nodes-map-compount', newVal);

    // if (newVal.name.localeCompare('Builder') == 0) {
    //   document.body.setAttribute('data-menu', 'collection-builder');
    //   //if(this.mounted){
    //   this.updateNetworkMap();
    //   //}
    // }
  }

  unmounted() {
    console.log('unmounted');
  }

  isMounted = false;
  mounted() {
    //console.log('mounted', this.isMounted);
    window.onresize = this.chartResize;

    //console.log('onGetDocumentsSelectedChanged', this.GetDocumentsSelected);
    //if(!this.isMapInitialized){
    //this.updateNetworkMap();
    this.isMounted = true;
    //}
  }

  data() {
    return {};
  }

  private arrayDiff(a: Array<any>, b: Array<any>) {
    return [...a.filter(x => !b.includes(x)), ...b.filter(x => !a.includes(x))];
  }

  //#region canvas generation
  nodesRendered: Array<MapNode> = new Array<MapNode>();
  linksRendered: Arry<MapLink> = new Array<MapLink>();

  selectedNodes: Set<string> = new Set();
  nodesLinkedToSelectedNodes: Set<string> = new Set();
  linksLinkedToSelectedNodes: Set<string> = new Set();

  updateNetworkMap() {
    if (!this.graph) {
      const elem = document.getElementsByClassName('map-area');
      if (!elem) {
        return;
      }

      if (elem.length === 0) {
        return;
      }
      this.tick = 0;
      this.graph = ForceGraph()(elem.item(0)).backgroundColor(MAP_BACKGROUND_COLOR);
    }

    if (this.graph && this.nodesRendered && this.linksRendered) {
      // console.log('MOUNTED: links ', this.getLinks);
      //console.log('MOUNTED: Total after ', this.getNodes);
      //console.log('MOUNTED: connectome ', this.getConnectome);
      //console.log(nodes);

      this.calculateCanvasSize();
      this.tick = 0;
      this.isUserStartInteract = false;

      // if (this.getNodes.length > 0) {
      //   this.maxWeight = this.getNodes[0].weight;
      //   this.minWeight = this.getNodes[this.getNodes.length - 1].weight;
      //   this.diffWeight = this.maxWeight - this.minWeight;
      // }

      this.graph
        .graphData({ nodes: this.getNodes, links: this.getLinks })
        .backgroundColor(MAP_BACKGROUND_COLOR)
        .width(this.width)
        .height(this.height)
        .nodeId('id')
        .nodeVisibility(node => {
          return true;
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
            const size = this.getNodeSize(node) + 60;
            return size;
          })
        )
        .linkSource('from')
        .linkTarget('to')
        .linkColor(link => link.color)
        .linkCurvature('curvature')
        .linkWidth(link => {
          return 2 + link.weight * 2;
        })
        .d3AlphaDecay(0.06)
        .autoPauseRedraw(false)
        .onZoom(event => {})
        .onZoomEnd(event => {})
        .enableNodeDrag(true)
        .enableZoomInteraction(true)
        .onEngineTick(() => {
          this.tick++;
        })
        .onEngineStop(() => {});
    }
  }

  // private renderColorLink<type>(link: any, tick: number): string | RGBColor | undefined {}

  // private renderDirectionalParticleColor<type>(link: LinkObject, tick: number): string | RGBColor | undefined {}

  // private renderDirectionalParticleWidth<type>(link: LinkObject, tick: number): number | undefined {}

  // private onNodeClick(node: any) {}

  private renderNodes(node: any, ctx: any, globalScale: any, tick: number) {
    if (!node) return;
    if (!node.label) return;

    const label = node.label;
    const size = node.size;
    const color = node.color;

    let fontSize = this.getFontSize(node);
    const textWidth = ctx.measureText(label).width;
    const bckgDimensions = [textWidth, fontSize].map(n => n + fontSize * 0.2); // some padding

    ctx.beginPath();
    ctx.fillStyle = color;
    ctx.arc(node.x, node.y, size, 0, 2 * Math.PI, false);
    ctx.fill();
    ctx.closePath();

    ctx.lineWidth = 2 / globalScale;
    ctx.beginPath();
    ctx.fillStyle = FONTCOLOR_NODE_IN_MAP.COMMON;
    ctx.strokeStyle = FONTCOLOR_NODE_IN_MAP.COMMON_STROKE;
    ctx.textAlign = 'center';
    ctx.textBaseline = 'middle';
    fontSize = this.getFontSize(node);
    ctx.font = `${fontSize}px sans-serif`;
    ctx.strokeText(label, node.x, node.y - size - 5);
    ctx.fillText(label, node.x, node.y - size - 5);
    node.__bckgDimensions = bckgDimensions;
    ctx.closePath();
    bckgDimensions = [2 * size + 3, 2 * size + 3].map(n => n + 5);
    node.__bckgDimensions = bckgDimensions;
  }

  private getFontSize(node: ConnectomeNode): number | undefined {
    return 22;
  }

  private chartResize() {
    this.calculateCanvasSize();
    if (this.graph) {
      this.graph.width(this.width).height(this.height);
    }
  }
  //#endregion

  //map interaction

  private clearSelection() {
    this.selectedNodes.clear();
    this.nodesLinkedToSelectedNodes.clear();
    this.linksLinkedToSelectedNodes.clear();
  }

  private createSelection(label: string) {
    if (label) {
      this.selectedNodes.add(label);
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
    console.log('resize event detected!');
    // if (this.fullscreen) {
    //   //this.containerCSS = $('#container-block').css
    //   document.body.setAttribute('data-menu', 'connectome-fullscreen');

    //   //resize child?
    // } else {
    //   document.body.setAttribute('data-menu', 'connectome');
    //   //$('.panel-sidebar').css({ top: this.sideBarTop > 100 ? this.sideBarTop : 150 });
    // }
    this.chartResize();
  }

  fullScreen() {
    this.fullscreen = !this.fullscreen;
    this.resizeFullscreen();
  }

  dummytimer: NodeJS.Timeout = null;
  //training

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
