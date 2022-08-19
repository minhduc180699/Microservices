// @ts-nocheck
import { Component, Vue, Prop, Inject, Watch, Emit } from 'vue-property-decorator';
import * as d3 from 'd3';
import ForceGraph from 'force-graph';
import ConnectomeService from '../../connectome.service';
import { NetworkNode } from '../network-node-model';
import { NetworkLink } from '../network-link-model';
import { SIZE_NODE_IN_MAP, TYPE_CONNECTOME_MAP_DATA, TYPE_NODE_IN_MAP, TYPE_VERTEX } from '@/shared/constants/ds-constants';
export const STEP_CONNECTOME_RENDER = {
  FIRST: 100,
  SECOND: 180,
  THIRD: 240,
};
import { RGBColor } from 'd3';

@Component({
  components: {},
})
export default class ConnectomeMapByLink extends Vue {
  @Prop(String) connectomeId: string | null;
  @Prop(Object) connectomeNetwork:
    | {
        nodes: Array<NetworkNode>;
        links: Array<NetworkLink>;
      }
    | {
        nodes: [];
        links: [];
      };

  @Inject('connectomeService')
  private connectomeService: () => ConnectomeService;

  nodes: Array<NetworkVertex> = [];
  links: Array<NetworkEdge> = [];

  highlightNode: Set<any> = new Set();
  highlightRelatedNodes: Set<any> = new Set();
  highlightLinks: Set<any> = new Set();

  private onInit = true;
  private onZoom = false;
  private graph: any = null;

  private width = 350;
  private height = 460;

  @Watch('connectomeId')
  onConnectomeIdUpdated(newId: string) {
    if (!this.connectomeId) {
      return;
    }
    const apiCall = this.connectomeService().getConnectomeNetworkMap(this.connectomeId);
    if (!apiCall) {
      return;
    }

    apiCall.then(res => {
      const data = res.data;

      this.nodes = [];
      this.links = [];
    });
  }

  @Watch('connectomeNetwork')
  onConnectomeNetworkUpdated(newConnectomeNetwork: Object) {
    if (!newConnectomeNetwork) {
      return;
    }

    this.nodes = this.connectomeNetwork.nodes;
    this.links = this.connectomeNetwork.links;

    this.renderMap(0, false);
  }

  mounted() {
    if (!this.graph) {
      this.width = Math.max($('#connectome-network').width(), 350);
      this.height = Math.max($('#connectome-network').height(), 350);
      const elem = document.getElementById('connectome-network');

      if (!elem) {
        return;
      }
      this.graph = ForceGraph()(elem).width(this.width).height(this.height);
    }
  }

  updateNetworkMap(newConnectomeNetwork: any) {
    if (!newConnectomeNetwork) {
      return;
    }
    this.nodes = newConnectomeNetwork.nodes;
    this.links = newConnectomeNetwork.links;

    console.log('try refresh graph');

    let { nodes, links } = this.graph.graphData();

    let diffNodes = this.arrayDiff(this.nodes, nodes);
    let diffLinks = this.arrayDiff(this.links, links);

    if (diffNodes.length === 0 && diffLinks.length === 0) {
      console.log('no need to refresh graph');
      return;
    }

    this.renderMap(STEP_CONNECTOME_RENDER.THIRD, true);
  }

  renderNetworkMap(newConnectomeNetwork: any) {
    if (!newConnectomeNetwork) {
      return;
    }
    this.nodes = newConnectomeNetwork.nodes;
    this.links = newConnectomeNetwork.links;

    this.renderMap(0, false);
  }

  renderNetworkMapV2(vertices: Array<NetworkVertex>, edges: Array<NetworkEdge>) {
    if (!vertices) {
      return;
    }

    if (!edges) {
      return;
    }

    console.log('try draw graph');

    this.nodes = vertices;
    this.links = edges;

    this.renderMapV2(0, false);
  }

  updateNetworkMapV2(vertices: Array<NetworkVertex>, edges: Array<NetworkEdge>) {
    if (!vertices) {
      return;
    }

    if (!edges) {
      return;
    }
    this.nodes = vertices;
    this.links = edges;

    console.log('try refresh graph');

    let { nodes, links } = this.graph.graphData();

    let diffNodes = this.arrayDiff(this.nodes, nodes);
    let diffLinks = this.arrayDiff(this.links, links);

    if (diffNodes.length === 0 && diffLinks.length === 0) {
      console.log('no need to refresh graph');
      return;
    }

    this.renderMapV2(STEP_CONNECTOME_RENDER.THIRD, true);
  }

  data() {
    return {};
  }

  mounted() {}

  private arrayDiff(a: Array<any>, b: Array<any>) {
    return [...a.filter(x => !b.includes(x)), ...b.filter(x => !a.includes(x))];
  }

  private learningRenderJob: NodeJS.Timeout = null;
  private learningCounter: number = 0;

  private renderLearningMap() {
    if (!this.learningRenderJob) {
      const k = 0;
      this.learningRenderJob = setInterval(() => {
        if (this.learningCounter < this.nodes.length) {
          const nodesToDisplay = this.nodes.slice(this.nodes.length - this.learningCounter - 1);
          if (this.graph) {
            this.graph
              .graphData({
                nodes: nodesToDisplay,
                links: this.links,
              })
              .nodeLabel(node => node.name)
              .nodeAutoColorBy('name')
              .nodeCanvasObject((node, ctx, globalScale) => {
                const label = node.name;
                const fontSize = 24 / globalScale;
                const textWidth = ctx.measureText(label).width;
                ctx.beginPath();
                ctx.fillStyle = '#ffffff';
                ctx.font = `${fontSize}px Sans-Serif`;
                ctx.textAlign = 'center';
                ctx.textBaseline = 'middle';
                ctx.fillText(label, node.x, node.y);
                ctx.closePath();
              })
              .nodeCanvasObjectMode(() => 'after')
              .nodeRelSize(25)
              .d3Force(
                'collision',
                d3.forceCollide(node => node._size * 2)
              )
              .d3Force('charge', d3.forceManyBody().strength(-150))
              .d3Force('center', d3.forceCenter())
              .nodeAutoColorBy(() => Math.floor(Math.random() * 25))
              .onEngineTick(() => {
                if (k % 20 == 1) {
                  this.graph.zoomToFit(200, 30, node => true);
                }
                k++;
              });
          }
        }
        this.learningCounter++;
      }, 500);
    }
  }

  private renderMapV2(startingTick: number, isUpdate: boolean): void {
    if (!this.graph) {
      this.width = Math.max($('#connectome-network').width(), 350);
      this.height = Math.max($('#connectome-network').height(), 350);
      const elem = document.getElementById('connectome-network');

      if (!elem) {
        return;
      }
      this.graph = ForceGraph()(elem).width(this.width).height(this.height);
    }

    // if (this.links?.length === 0) {
    //   console.log('Render Learning Map');
    //   this.renderLearningMap();
    //   return;
    // }

    // if (this.learningRenderJob) {
    //   clearInterval(this.learningRenderJob);
    //   this.learningCounter = 0;
    //   this.learningRenderJob = null;
    // }
    console.log('Render Map');
    const tick = startingTick;
    this.onInit = true;
    this.graph
      .graphData({ nodes: this.nodes, links: this.links })
      .nodeId('label')
      .nodeLabel(node => node.label)
      .nodeCanvasObject((node, ctx, globalScale) => {
        const label = node.label;
        const fontSize = node.fontSize / globalScale;
        const textWidth = ctx.measureText(label).width;
        const bckgDimensions = [textWidth, fontSize].map(n => n + fontSize * 0.2); // some padding
        if (tick > 10) {
          this.renderNodes(node, ctx, globalScale, tick);
        }
        node.__bckgDimensions = bckgDimensions; // to re-use in nodePointerAreaPaint
      })
      .nodePointerAreaPaint((node, color, ctx) => {
        ctx.fillStyle = color;
        const bckgDimensions = node.__bckgDimensions;
        bckgDimensions && ctx.fillRect(node.x - bckgDimensions[0] / 2, node.y - bckgDimensions[1] / 2, ...bckgDimensions);
      })
      .nodeCanvasObjectMode(() => 'replace')
      .onNodeClick(node => {
        this.onLeftClickOnNetworkNode(node);
      })
      .onNodeRightClick(node => {
        this.onRightClickOnNetworkNode(node);
      })
      .d3Force(
        'collision',
        d3.forceCollide(node => node._size * 2)
      )
      .d3Force('charge', d3.forceManyBody().strength(isUpdate ? -80 : -120))
      .d3Force('center', d3.forceCenter())
      .d3Force(
        'link',
        d3.forceLink().distance(link => link._length)
      )
      .linkColor(link => {
        return this.renderColorLink(link, tick);
      })
      .linkWidth(link => {
        return this.renderWidthLink(link, tick);
      })
      .linkCurvature(link => {
        return this.renderCurvatureLink(link, tick);
      })
      .linkDirectionalParticleColor(link => {
        return this.renderDirectionalParticleColor(link, tick);
      })
      .linkDirectionalParticleWidth(link => {
        return this.renderDirectionalParticleWidth(link, tick);
      })
      .linkDirectionalParticles(1)
      .onEngineTick(() => {
        if (tick % 20 == 1 && tick < STEP_CONNECTOME_RENDER.THIRD) {
          this.graph.zoomToFit(200, 30, node => true);
        }
        if (tick > STEP_CONNECTOME_RENDER.THIRD) {
          if (this.onInit) {
            if (this.$route.query.node) {
              this.focusOnNodeByParam(this.$route.query.node);
            } else {
              this.onNetworkMapInitiate();
              if (!this.onZoom) this.graph.zoomToFit(200, 10, node => true);
            }
            this.onInit = false;
          }
        }
        tick++;
      })
      .onBackgroundClick(() => {
        this.onClearSelection();
      })
      .onZoom(() => {
        this.onZoom = true;
      })
      .onEngineStop(() => {
        if (this.onInit) {
          if (this.$route.query.node) {
            this.focusOnNodeByParam(this.$route.query.node);
          } else {
            this.onNetworkMapInitiate();
            if (!this.onZoom) this.graph.zoomToFit(200, 10, node => true);
          }
          this.onInit = false;
        }
      })
      .onZoomEnd(() => {
        this.onZoom = false;
      })
      .enableZoomInteraction(false)
      .enableNodeDrag(false);
    if (isUpdate) {
      this.graph.d3ReheatSimulation();
    }
  }

  private renderMap(startingTick: number, isUpdate: boolean): void {
    if (!this.graph) {
      this.width = Math.max($('#connectome-network').width(), 350);
      this.height = Math.max($('#connectome-network').height(), 350);
      const elem = document.getElementById('connectome-network');

      if (!elem) {
        return;
      }
      this.graph = ForceGraph()(elem).width(this.width).height(this.height);
    }

    if (this.links?.length === 0) {
      console.log('Render Learning Map');
      this.renderLearningMap();
      return;
    }

    if (this.learningRenderJob) {
      clearInterval(this.learningRenderJob);
      this.learningCounter = 0;
      this.learningRenderJob = null;
    }

    const tick = startingTick;
    this.onInit = true;
    this.graph
      .graphData({ nodes: this.nodes, links: this.links })
      .nodeLabel(node => node.name)
      .nodeCanvasObject((node, ctx, globalScale) => {
        const label = node.name;
        const fontSize = node.fontSize / globalScale;
        const textWidth = ctx.measureText(label).width;
        const bckgDimensions = [textWidth, fontSize].map(n => n + fontSize * 0.2); // some padding
        if (tick > 10) {
          this.renderNodes(node, ctx, globalScale, tick);
        }
        node.__bckgDimensions = bckgDimensions; // to re-use in nodePointerAreaPaint
      })
      .nodePointerAreaPaint((node, color, ctx) => {
        ctx.fillStyle = color;
        const bckgDimensions = node.__bckgDimensions;
        bckgDimensions && ctx.fillRect(node.x - bckgDimensions[0] / 2, node.y - bckgDimensions[1] / 2, ...bckgDimensions);
      })
      .nodeCanvasObjectMode(() => 'replace')
      .onNodeClick(node => {
        this.onLeftClickOnNetworkNode(node);
      })
      .onNodeRightClick(node => {
        this.onRightClickOnNetworkNode(node);
      })
      .d3Force(
        'collision',
        d3.forceCollide(node => node._size * 10)
      )
      .d3Force('charge', d3.forceManyBody().strength(isUpdate ? -80 : -120))
      .d3Force('center', d3.forceCenter())
      .d3Force(
        'link',
        d3.forceLink().distance(link => link._length)
      )
      .linkColor(link => {
        return this.renderColorLink(link, tick);
      })
      .linkWidth(link => {
        return this.renderWidthLink(link, tick);
      })
      .linkCurvature(link => {
        return this.renderCurvatureLink(link, tick);
      })
      .linkDirectionalParticleColor(link => {
        return this.renderDirectionalParticleColor(link, tick);
      })
      .linkDirectionalParticleWidth(link => {
        return this.renderDirectionalParticleWidth(link, tick);
      })
      .linkDirectionalParticles(1)
      .onEngineTick(() => {
        if (tick % 20 == 1 && tick < STEP_CONNECTOME_RENDER.THIRD) {
          this.graph.zoomToFit(200, 30, node => true);
        }
        if (tick > STEP_CONNECTOME_RENDER.THIRD) {
          if (this.onInit) {
            if (this.$route.query.node) {
              this.focusOnNodeByParam(this.$route.query.node);
            } else {
              this.onNetworkMapInitiate();
              if (!this.onZoom) this.graph.zoomToFit(200, 10, node => true);
            }
            this.onInit = false;
          }
        }
        tick++;
      })
      .onBackgroundClick(() => {
        this.onClearSelection();
      })
      .onZoom(() => {
        this.onZoom = true;
      })
      .onEngineStop(() => {
        if (this.onInit) {
          if (this.$route.query.node) {
            this.focusOnNodeByParam(this.$route.query.node);
          } else {
            this.onNetworkMapInitiate();
            if (!this.onZoom) this.graph.zoomToFit(200, 10, node => true);
          }
          this.onInit = false;
        }
      })
      .onZoomEnd(() => {
        this.onZoom = false;
      })
      .enableZoomInteraction(false)
      .enableNodeDrag(false);
    if (isUpdate) {
      this.graph.d3ReheatSimulation();
    }
  }

  private renderNodes(node: any, ctx: any, globalScale: any, tick: number) {
    const label = node.label;
    const fontSize = node.fontSize / globalScale;
    if (!node.x || !node.y) {
      return;
    }
    if (this.highlightNode.size > 0 && tick > STEP_CONNECTOME_RENDER.SECOND) {
      this.highlightFocuseNode(node, ctx, globalScale);
      return;
    }

    if (tick <= STEP_CONNECTOME_RENDER.FIRST) {
      ctx.beginPath();
      ctx.fillStyle = node._color;
      ctx.arc(node.x, node.y, node._size, 0, 2 * Math.PI, false);
      ctx.fill();
      ctx.closePath();
    } else if (tick <= STEP_CONNECTOME_RENDER.SECOND) {
      if (node.type === TYPE_NODE_IN_MAP.CLUSTER) {
        ctx.beginPath();
        ctx.fillStyle = d3.rgb(node._color).brighter(0.6);
        ctx.arc(node.x, node.y, node._size, 0, 2 * Math.PI, false);
        ctx.fill();
        if (node.serverSize > 40) {
          ctx.beginPath();
          ctx.fillStyle = '#ffffff';
          ctx.font = `${fontSize}px Sans-Serif`;
          ctx.textAlign = 'center';
          ctx.textBaseline = 'middle';
          ctx.fillText(label, node.x, node.y);
          ctx.closePath();
        }
      } else {
        ctx.beginPath();
        ctx.fillStyle = node._color;
        ctx.arc(node.x, node.y, node._size, 0, 2 * Math.PI, false);
        ctx.fill();
        ctx.closePath();
      }
    } else if (tick <= STEP_CONNECTOME_RENDER.THIRD) {
      if (node.type === TYPE_NODE_IN_MAP.CLUSTER) {
        let radGradCluster1 = null;
        try {
          radGradCluster1 = ctx.createRadialGradient(node.x, node.y, 0, node.x, node.y, node._size * 2.0);
          radGradCluster1.addColorStop(0, d3.rgb(node._color).brighter(1));
          radGradCluster1.addColorStop(0.4, d3.rgb(node._color).brighter(0.9));
          radGradCluster1.addColorStop(1, 'rgb(228,228,228,0)');
        } catch (error) {
          console.log(error);
          radGradCluster1 = node._color;
        }

        ctx.beginPath();
        ctx.fillStyle = radGradCluster1;
        ctx.arc(node.x, node.y, node._size * 2, 0, 2 * Math.PI, false);
        ctx.fill();
        ctx.closePath();
        ctx.beginPath();
        ctx.fillStyle = d3.rgb(node._color).brighter(0.6);
        ctx.arc(node.x, node.y, node._size, 0, 2 * Math.PI, false);
        ctx.fill();
        if (node.serverSize > 40) {
          ctx.beginPath();
          ctx.fillStyle = '#ffffff';
          ctx.font = `${fontSize}px Sans-Serif`;
          ctx.textAlign = 'center';
          ctx.textBaseline = 'middle';
          ctx.fillText(label, node.x, node.y);
          ctx.closePath();
        }
      } else {
        ctx.beginPath();
        ctx.fillStyle = node._color;
        ctx.arc(node.x, node.y, node._size, 0, 2 * Math.PI, false);
        ctx.fill();
        ctx.closePath();
      }
    } else {
      if (node.type === TYPE_NODE_IN_MAP.CLUSTER) {
        ctx.beginPath();
        let radGradCluster2 = null;
        try {
          radGradCluster2 = ctx.createRadialGradient(node.x, node.y, 0, node.x, node.y, node._size * 2.0);
          radGradCluster2.addColorStop(0, d3.rgb(node._color).brighter(1));
          radGradCluster2.addColorStop(0.4, d3.rgb(node._color).brighter(0.9));
          radGradCluster2.addColorStop(1, 'rgb(228,228,228,0)');
        } catch (error) {
          console.log(error);
          radGradCluster2 = node._color;
        }
        ctx.fillStyle = radGradCluster2;
        ctx.arc(node.x, node.y, node._size * 2, 0, 2 * Math.PI, false);
        ctx.fill();
        ctx.closePath();
        ctx.beginPath();
        ctx.fillStyle = d3.rgb(node._color).brighter(0.6);
        ctx.arc(node.x, node.y, node._size, 0, 2 * Math.PI, false);
        ctx.fill();
        ctx.closePath();
        ctx.beginPath();
        ctx.fillStyle = '#ffffff';
        ctx.font = `${fontSize}px Sans-Serif`;
        ctx.textAlign = 'center';
        ctx.textBaseline = 'middle';
        if (globalScale >= 0.35) {
          ctx.fillText(label, node.x, node.y);
        } else if (globalScale >= 0.074 && node.type == TYPE_NODE_IN_MAP.CLUSTER && node.serverSize > 40) {
          ctx.fillText(label, node.x, node.y);
        }
        ctx.closePath();
      } else {
        ctx.beginPath();
        ctx.fillStyle = node._color;
        ctx.arc(node.x, node.y, node._size, 0, 2 * Math.PI, false);
        ctx.fill();
        ctx.closePath();
        ctx.beginPath();
        ctx.fillStyle = '#dfdfdf';
        ctx.font = `${fontSize}px Sans-Serif`;
        ctx.textAlign = 'center';
        ctx.textBaseline = 'middle';
        if (globalScale >= 0.5) {
          ctx.fillText(label, node.x, node.y);
        }
      }
    }
  }

  private highlightFocuseNode(node: any, ctx: any, globalScale: any) {
    const label = node.name;
    const fontSize = node.fontSize / globalScale;
    if (this.highlightNode.has(node)) {
      ctx.beginPath();
      let radgradSelected = null;
      try {
        radgradSelected = ctx.createRadialGradient(node.x, node.y, 0, node.x, node.y, node._size * 3);
        radgradSelected.addColorStop(0, d3.rgb(node._color).brighter(1));
        radgradSelected.addColorStop(0.5, d3.rgb(255, 255, 48).brighter(0.8));
        radgradSelected.addColorStop(1, 'rgb(255,255,48,0)');
      } catch (error) {
        console.log(error);
        radgradSelected = node._color;
      }
      ctx.fillStyle = radgradSelected;
      ctx.arc(node.x, node.y, node._size * 3, 0, 2 * Math.PI, false);
      ctx.fill();
      ctx.closePath();
      ctx.beginPath();
      ctx.arc(node.x, node.y, node._size * 1.5, 0, 2 * Math.PI, false);
      ctx.fillStyle = node._color;
      ctx.fill();
      ctx.closePath();
      ctx.fillStyle = '#ffffff';
    } else if (this.highlightRelatedNodes.has(node)) {
      ctx.beginPath();
      let radgradSelectedChildren = null;
      try {
        radgradSelectedChildren = ctx.createRadialGradient(node.x, node.y, 0, node.x, node.y, node._size * 1.5);
        radgradSelectedChildren.addColorStop(0, d3.rgb(node._color).brighter(1));
        radgradSelectedChildren.addColorStop(0.7, d3.rgb(148, 255, 48).brighter(0.8));
        radgradSelectedChildren.addColorStop(1, 'rgb(148,255,96,0)');
      } catch (error) {
        console.log(error);
        radgradSelectedChildren = node._color;
      }
      ctx.fillStyle = radgradSelectedChildren;
      ctx.arc(node.x, node.y, node._size * 1.5, 0, 2 * Math.PI, false);
      ctx.fill();
      ctx.closePath();
      ctx.beginPath();
      ctx.arc(node.x, node.y, node._size, 0, 2 * Math.PI, false);
      ctx.fillStyle = node._color;
      ctx.fill();
      ctx.closePath();
      ctx.fillStyle = '#ffffff';
    } else if (node._size >= SIZE_NODE_IN_MAP.CLUSTER) {
      ctx.beginPath();
      let radgradCluster = null;
      try {
        radgradCluster = ctx.createRadialGradient(node.x, node.y, 0, node.x, node.y, node._size);
        radgradCluster.addColorStop(0, d3.rgb(node._color).darker(3));
        radgradCluster.addColorStop(0.4, d3.rgb(node._color).darker(2));
        radgradCluster.addColorStop(1, 'rgb(228,228,228,0)');
      } catch (error) {
        console.log(error);
        radgradCluster = node._color;
      }

      ctx.fillStyle = radgradCluster;
      ctx.arc(node.x, node.y, node._size, 0, 2 * Math.PI, false);
      ctx.fill();
      ctx.closePath();
      ctx.beginPath();
      ctx.arc(node.x, node.y, node._size * 0.8, 0, 2 * Math.PI, false);
      ctx.fillStyle = d3.rgb(node._color).darker(2);
      ctx.fill();
      ctx.closePath();
      ctx.fillStyle = '#a9a9a9';
    } else {
      ctx.beginPath();
      ctx.arc(node.x, node.y, node._size * 0.8, 0, 2 * Math.PI, false);
      ctx.fillStyle = d3.rgb(node._color).darker(2);
      ctx.fill();
      ctx.closePath();
      ctx.beginPath();
      ctx.fillStyle = '#a9a9a9';
    }
    ctx.font = `${fontSize}px Sans-Serif`;
    ctx.textAlign = 'center';
    ctx.textBaseline = 'middle';
    if (globalScale >= 0.5) {
      ctx.fillText(label, node.x, node.y);
    } else if (globalScale >= 0.35 && node.type == TYPE_VERTEX.CLUSTER) {
      ctx.fillText(label, node.x, node.y);
    } else if (globalScale >= 0.074 && node.type == TYPE_VERTEX.CLUSTER && node._size > 40) {
      ctx.fillText(label, node.x, node.y);
    }
  }

  private renderColorLink<type>(link: any, tick: number): string | RGBColor | undefined {
    if (tick >= STEP_CONNECTOME_RENDER.SECOND) {
      if (this.highlightLinks && this.highlightLinks.size > 0) {
        return this.highlightLinks.has(link) ? d3.rgb(link._color).brighter(2) : d3.rgb(link._color).darker(2);
      } else {
        return d3.rgb(link._color).darker(2);
      }
    } else {
      return 'rgba(255,255,255,0.2)';
    }
  }

  private renderWidthLink<type>(link: any, tick: number): number | undefined {
    if (tick >= STEP_CONNECTOME_RENDER.THIRD) {
      return link._size * this.graph.zoom();
    } else {
      return 1;
    }
  }

  private renderCurvatureLink<type>(link: any, tick: number): number | undefined {
    if (tick >= STEP_CONNECTOME_RENDER.THIRD) {
      return link.curvature;
    } else {
      return 0;
    }
  }

  private renderDirectionalParticleColor<type>(link: any, tick: number): string | RGBColor | undefined {
    if (tick >= STEP_CONNECTOME_RENDER.THIRD) {
      return this.highlightLinks.has(link) ? '#ffffff' : d3.rgb(link._color).brighter(2);
    } else {
      return 'rgba(255,255,255,0.2)';
    }
  }

  private renderDirectionalParticleWidth<type>(link: any, tick: number): number | undefined {
    if (tick >= STEP_CONNECTOME_RENDER.THIRD) {
      return this.highlightLinks.has(link) ? link._size + 8 : link._size + 2;
    } else {
      return 0;
    }
  }

  // commands
  private focusOnNodeByParam(targetNode: string) {
    if (!targetNode) {
      return;
    }

    this.nodes.every(node => {
      if (node.name === targetNode) {
        this.onLeftClickOnNetworkNode(node);
        return false;
      }
      return true;
    });

    if (this.highlightNode.size == 0) {
      console.log('Node not found', targetNode);
    }
  }

  focusOnNode(targetNode: {
    origin: TYPE_CONNECTOME_MAP_DATA;
    type: TYPE_NODE_IN_MAP;
    label: string;
    id: string;
    parents: Array<MapNode>;
  }) {
    if (!targetNode) {
      return;
    }
    let targetType = targetNode.type === TYPE_NODE_IN_MAP.ENTITY ? TYPE_NODE_IN_MAP.ENTITY : TYPE_NODE_IN_MAP.CLUSTER;
    this.nodes.every(node => {
      if (node.name === targetNode.label && node.type === targetType) {
        this.highlightNode.clear();
        this.highlightLinks.clear();
        this.highlightRelatedNodes.clear();
        this.highlightNode.add(node);

        this.links.forEach(link => {
          if (link.source === node) {
            this.highlightLinks.add(link);
            this.highlightRelatedNodes.add(link.target);
          }
          if (link.target === node) {
            this.highlightLinks.add(link);
            this.highlightRelatedNodes.add(link.source);
          }
        });
        this.graph.centerAt(node.x, node.y, 2000);
        this.graph.zoom(node.type == TYPE_NODE_IN_MAP.ENTITY ? 3 : 2, 2000);
        return false;
      }
      return true;
    });
    //console.log(this.highlightNodes);
    if (this.highlightNode.size == 0) {
      console.log('Node not found', targetNode);
    }
  }

  public clearSelection() {
    this.highlightNode.clear();
    this.highlightLinks.clear();
    this.highlightRelatedNodes.clear();
  }

  public chartResize() {
    this.width = Math.max($('#connectome-network').width(), 350);
    this.height = Math.max($('#connectome-network').height(), 350);
    if (this.graph) {
      this.graph.width(this.width).height(this.height);
    }
  }

  public zoomIn() {
    if (!this.onInit && this.graph) {
      let zoom = this.graph.zoom();
      this.graph.zoom(Math.min(1000, zoom + 0.2));
    }
  }

  public zoomOut() {
    if (!this.onInit && this.graph) {
      let zoom = this.graph.zoom();
      this.graph.zoom(Math.max(0.01, zoom - 0.2));
    }
  }

  public fitScreen() {
    if (!this.onInit && this.graph) {
      this.graph.zoomToFit(200, 10, node => true);
    }
  }

  public expandNodes() {
    if (!this.onInit && this.graph) {
      this.graph
        .d3Force(
          'collision',
          d3.forceCollide(node => node._size * 2)
        )
        .d3Force('charge', d3.forceManyBody().strength(-40))
        .d3Force('center', d3.forceCenter())
        .d3ReheatSimulation();
    }
  }

  public contractNodes() {
    if (!this.onInit && this.graph) {
      this.graph
        .d3Force(
          'collision',
          d3.forceCollide(node => node._size * 2)
        )
        .d3Force('charge', d3.forceManyBody().strength(40))
        .d3Force('center', d3.forceCenter())
        .d3ReheatSimulation();
    }
  }

  @Emit('onLeftClickOnNetworkNode')
  onLeftClickOnNetworkNode(node: NetworkNode) {
    if (!this.onInit) {
      return node;
    }
  }

  @Emit('onRightClickOnNetworkNode')
  onRightClickOnNetworkNode(node: NetworkNode) {
    if (!this.onInit) {
      return node;
    }
  }

  @Emit('onGetUpdatedData')
  onGetUpdatedData() {
    return TYPE_CONNECTOME_MAP_DATA.NETWORK;
  }

  @Emit('onClearSelection')
  onClearSelection() {}

  @Emit('onNetworkMapInitiate')
  onNetworkMapInitiate() {}
}
