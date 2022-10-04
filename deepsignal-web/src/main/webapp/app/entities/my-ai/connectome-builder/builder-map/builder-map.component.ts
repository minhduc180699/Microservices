// @ts-nocheck
import { Component, Vue, Watch } from 'vue-property-decorator';
import { namespace } from 'vuex-class';
import { ConnectomeNode } from '@/shared/model/connectome-node.model';
import { ConnectomeLink } from '@/shared/model/connectome-link.model';
import * as d3 from 'd3';
import * as d3ScaleChromatic from 'd3-scale-chromatic';
import ForceGraph, { ForceGraphInstance, LinkObject } from 'force-graph';
import { MAP_BACKGROUND_COLOR, TYPE_VERTEX, FONTCOLOR_NODE_IN_MAP, MINI_MAP_BACKGROUND_COLOR } from '@/shared/constants/ds-constants';
import BuilderMapSideBar from '../builder-map-side-bar/builder-map-side-bar.vue';
import BuilderMapAddDocumentsTool from '../builder-map-add-documents-tool/builder-map-add-documents-tool.vue';
import { ContextualMemoryCollection } from '@/shared/model/contextual-memory-collection.model';

export const STEP_CONNECTOME_RENDER = {
  FIRST: 10,
  SECOND: 80,
  THIRD: 120,
  FOURTH: 200,
};

const networkStore = namespace('connectomeNetworkStore');
const mapNetworkStore = namespace('mapNetworkStore');

const connectomeBuilderStore = namespace('connectomeBuilderStore');
const collectionManagerStore = namespace('collectionManagerStore');

@Component({
  components: {
    'map-side-bar': BuilderMapSideBar,
    'map-add-document-tool': BuilderMapAddDocumentsTool,
  },
})
export default class BuilderMap extends Vue {
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
  public lang!: string;

  @connectomeBuilderStore.Getter
  public getDocumentsSelected!: Map<string, Array<ConnectomeNode>>;

  @connectomeBuilderStore.Getter
  public getNodes!: Array<ConnectomeNode>;

  @connectomeBuilderStore.Getter
  public getConnectome!: Array<ConnectomeNode>;

  @connectomeBuilderStore.Getter
  public getLinks!: Array<ConnectomeLink>;

  @connectomeBuilderStore.Getter
  public getDataUpdated!: number;

  @connectomeBuilderStore.Getter
  public getDocumentColors!: Map<string, string>;

  @collectionManagerStore.Getter
  public getCurrentCollection!: ContextualMemoryCollection;

  @connectomeBuilderStore.Mutation
  public setConnectome!: (payload: { connectome: Array<ConnectomeNode> }) => void;

  @collectionManagerStore.Action
  public updateCollection!: (payload: { collection: ContextualMemoryCollection }) => Promise<any>;

  @Watch('getDocumentsSelected')
  onGetDocumentsSelectedChanged(data: Map<string, Array<ConnectomeNode>>) {
    console.log('onGetDocumentsSelectedChanged', data);
  }

  @Watch('getCurrentCollection')
  onCurrentCollectionChanged(newValue: ContextualMemoryCollection) {
    console.log('onCurrentCollectionChanged', newValue);

    if (!newValue) {
      this.setConnectome({ connectome: [] });
      return;
    }

    if (!newValue.connectomeNodeList) {
      this.setConnectome({ connectome: [] });
      return;
    }

    this.setConnectome({ connectome: newValue.connectomeNodeList });
  }

  @Watch('getDataUpdated')
  onDataChanged(data: number) {
    console.log('onDataChanged', data);
    this.updateNetworkMap();
  }

  @Watch('getNodes')
  onGetNodesChanged(data: any) {}

  calculateCanvasSize() {
    this.width = Math.max($('.connectome-area').width(), 350);
    this.height = Math.max($('.connectome-area').height(), 350);
  }

  @Watch('$route', { immediate: true, deep: true })
  onUrlChange(newVal: Route) {
    console.log(newVal);

    if (newVal.name.localeCompare('Builder') == 0) {
      document.body.setAttribute('data-menu', 'collection-builder');
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
    //console.log('mounted', this.isMounted);
    window.onresize = this.chartResize;

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
    }
    //console.log('onGetDocumentsSelectedChanged', this.GetDocumentsSelected);
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
  localNodes: Array<ConnectomeNode> = [];
  updateNetworkMap() {
    // if (!this.getDocumentsSelected || this.getDocumentsSelected.size < 1) {
    //   this.displayDummyChart(true);
    //   return;
    // } else {
    //   this.displayDummyChart(false);
    // }
    if (this.graph && this.getNodes && this.getLinks) {
      console.log('MOUNTED: links ', this.getLinks);
      console.log('MOUNTED: Total after ', this.getNodes.length);
      //console.log(nodes);
      // if (this.entityLabelSelected) {
      //   const vertex = this.vertice(this.entityLabelSelected);
      //   if (vertex) {
      //     this.clearSelection();
      //     this.createSelection(this.entityLabelSelected);
      //   }
      // }

      this.calculateCanvasSize();
      this.tick = 0;
      this.isUserStartInteract = false;
      //this.displayDummyChart(true);
      //return;
      this.localNodes = this.getNodes.map(x => x);
      this.graph
        .graphData({ nodes: this.getNodes, links: this.getLinks })
        .backgroundColor(MAP_BACKGROUND_COLOR)
        .width(this.width)
        .height(this.height)
        .nodeId('id')
        .nodeLabel(node => {
          return node.weight;
        })
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
        .linkColor(() => '#DEDEDE')
        .linkCurvature('curvature')
        .linkWidth(link => {
          return link.weight * 2;
        })
        .d3AlphaDecay(0.06)
        .autoPauseRedraw(false)
        .onZoom(event => {})
        .onZoomEnd(event => {
          if (!event) {
            return;
          }
        })
        .enableNodeDrag(true)
        .enableZoomInteraction(true)
        .onEngineTick(() => {
          if (!this.isFirstStepPassed) {
            if (this.tick > 20) {
              this.graph.zoomToFit(300, 100, node => true);
              this.isFirstStepPassed = true;
            }
          }
          if (!this.isUserStartInteract && this.isFirstStepPassed) {
            if (this.tick < STEP_CONNECTOME_RENDER.THIRD) {
              if (this.tick % 30 > 1) {
                this.graph.zoomToFit(200, 100, node => true);
              }
            }
          }
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
    if (!node.relatedDocuments) return;
    if (!node.label) return;

    const label = node.label;
    const fontSize = this.getFontSize(node);
    const textWidth = ctx.measureText(label).width;
    const bckgDimensions = [textWidth, fontSize].map(n => n + fontSize * 0.2); // some padding

    //circle
    const size = this.getNodeSize(node);
    const tmpCircleColor = '#ffffff';
    for (let i = 0; i < node.relatedDocuments.length; i++) {
      ctx.beginPath();
      const circleSize = size - i * 15;
      ctx.fillStyle = this.getDocumentColors.get(node.relatedDocuments[i]);
      ctx.arc(node.x, node.y, circleSize, 0, 2 * Math.PI, false);
      ctx.fill();
      ctx.closePath();
    }

    ctx.lineWidth = 2 / globalScale;
    ctx.beginPath();
    ctx.fillStyle = FONTCOLOR_NODE_IN_MAP.COMMON;
    ctx.strokeStyle = FONTCOLOR_NODE_IN_MAP.COMMON_STROKE;
    ctx.textAlign = 'center';
    ctx.textBaseline = 'middle';
    if (globalScale >= 0.1 || node.linkedNodes.length > 5) {
      ctx.font = `${fontSize}px Sans-Serif`;
      ctx.strokeText(label, node.x, node.y - size - 5);
      ctx.fillText(label, node.x, node.y - size - 5);
      node.__bckgDimensions = bckgDimensions;
    } else if (node.linkedNodes.length < 2) {
      if (globalScale >= 0.08) {
        fontSize = this.getFontSize(node) / (globalScale * 2);
        ctx.font = `${fontSize}px Sans-Serif`;
        ctx.strokeText(label, node.x, node.y - size - 5);
        ctx.fillText(label, node.x, node.y - size - 5);
        node.__bckgDimensions = bckgDimensions;
      } else {
        if (globalScale >= 0.05) {
          fontSize = this.getFontSize(node) / (globalScale * 3);
          ctx.font = `${fontSize}px Sans-Serif`;
          ctx.strokeText(label, node.x, node.y - size - 5);
          ctx.fillText(label, node.x, node.y - size - 5);
          node.__bckgDimensions = bckgDimensions;
        }
      }
    }
    ctx.textAlign = 'center';
    ctx.textBaseline = 'middle';
    ctx.textAlign = 'center';
    ctx.textBaseline = 'middle';
    ctx.strokeText(node.id, node.x, node.y - size / 2 - 5);
    ctx.fillText(node.id, node.x, node.y - size / 2 - 5);
    ctx.strokeText(this.localNodes.indexOf(node) + 1, node.x, node.y);
    ctx.fillText(this.localNodes.indexOf(node) + 1, node.x, node.y);
    ctx.closePath();

    bckgDimensions = [2 * size + 3, 2 * size + 3].map(n => n + 5);
    node.__bckgDimensions = bckgDimensions;
  }

  private getNodeSize(node: ConnectomeNode): number | undefined {
    if (!node) {
      return 5;
    }

    if (!node.relatedDocuments) {
      return 5;
    }

    const size = 10 + node.relatedDocuments.length * 15;

    return size;
  }

  private getFontSize(node: ConnectomeNode): number | undefined {
    if (!node) {
      return 12;
    }

    if (!node.linkedNodes) {
      return 12;
    }

    const size = 12 + node.weight;

    return size;
  }

  // private getFactorCollision(node: ConnectomeNode): number | undefined {}

  // private renderSelection(node: any, ctx: any, globalScale: any, tick: number) {
  //   if (this.selectedNodes.has(node.label)) {
  //   } else if (this.nodesLinkedToSelectedNodes.has(node.label)) {
  //   } else {
  //   }
  // }

  private chartResize() {
    this.calculateCanvasSize();
    if (this.graph) {
      this.graph.width(this.width).height(this.height);
    }
  }

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

  //#region filter
  @connectomeBuilderStore.Getter
  public getMinNodeWeight!: number;

  @connectomeBuilderStore.Getter
  public getMinLinkedNodes!: number;

  @connectomeBuilderStore.Getter
  public getMinRelatedDocuments!: number;

  @connectomeBuilderStore.Action
  public updateMinNodeWeight!: (value: number) => Promise<number>;

  @connectomeBuilderStore.Action
  public updateMinLinkedNodes!: (value: number) => Promise<number>;

  @connectomeBuilderStore.Action
  public updateMinRelatedDocuments!: (value: number) => Promise<number>;

  @connectomeBuilderStore.Mutation
  public setDataUpdate!: () => void;

  cmpMinNodeWeight = 0;
  cmpMinRelatedDocuments = 0;
  cmpMinLinkedNodes = 0;

  networkFiltersOnChange() {
    this.updateMinLinkedNodes(this.cmpMinLinkedNodes).then(res => {
      this.cmpMinLinkedNodes = res;
    });
    this.updateMinNodeWeight(this.cmpMinNodeWeight).then(res => {
      this.cmpMinNodeWeight = res;
    });
    this.updateMinRelatedDocuments(this.cmpMinRelatedDocuments).then(res => {
      this.cmpMinRelatedDocuments = res;
    });
    this.setDataUpdate();
  }
  //#endregion

  SaveContext() {
    const newContextToSave: ContextualMemoryCollection = this.getCurrentCollection;
    newContextToSave.connectomeNodeList = this.getConnectome.map(x => x);
    const docSet: Set<string> = new Set<string>();
    const keywordSet: Set<string> = new Set<string>();
    newContextToSave.connectomeNodeList.forEach(node => {
      node.relatedDocuments.forEach(docId => {
        docSet.add(docId);
      });
      node.keywordList.forEach(keyword => {
        keywordSet.add(keyword);
      });
    });

    console.log('add to context', docSet);
    newContextToSave.documentIdList = new Array<string>();
    docSet.forEach(element => {
      newContextToSave.documentIdList.push(element);
    });
    newContextToSave.keywordList = new Array<string>();
    keywordSet.forEach(element => {
      newContextToSave.keywordList.push(element);
    });
    console.log(newContextToSave);

    this.updateCollection({ collection: newContextToSave }).then(res => {
      console.log(res);
    });
  }
}
