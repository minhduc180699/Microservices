// @ts-nocheck
import { Component, Vue, Prop, Inject, Emit, Watch } from 'vue-property-decorator';
import ForceGraph3D from '3d-force-graph';
import ConnectomeService from '../../connectome.service';
import * as THREE from 'three';
import * as d3 from 'd3';
import SpriteText from 'three-spritetext';
import { NetworkNode } from '../network-node-model';
import { NetworkLink } from '../network-link-model';
import MapPopupComponent from '@/entities/connectome/map/map-popup/map-popup.vue';
import { STEP_CONNECTOME_RENDER, TYPE_NODE_IN_MAP, TYPE_CONNECTOME_MAP_DATA } from '@/shared/constants/ds-constants';
import { ConnectomeNodeOld } from '../connectome-node-model-old';
// import { UnrealBloomPass } from '//cdn.skypack.dev/three/examples/jsm/postprocessing/UnrealBloomPass.js';

@Component({
  components: {
    'map-popup': MapPopupComponent,
  },
})
export default class ConnectomeMap3dByLink extends Vue {
  @Prop(String) connectomeId: string | undefined;
  @Prop(Object) connectomeNetworkThreed:
    | {
        connectomeId: string;
        nodes: Array<NetworkNode>;
        links: Array<NetworkLink>;
      }
    | undefined;

  @Inject('connectomeService')
  private connectomeService: () => ConnectomeService;

  nodes: Array<NetworkNode> = [];
  links: Array<NetworkLink> = [];

  highlightNode: Set<any> = new Set();
  highlightRelatedNodes: Set<any> = new Set();
  highlightLinks: Set<any> = new Set();

  private onInit = true;
  private onZoom = false;
  private graph: any = null;

  private onInitFocusNode = true;
  private onParticuleInit = true;

  private width = 350;
  private height = 460;
  private rotationY = 0.0;

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

  @Watch('connectomeNetworkThreed')
  onConnectomeNetworkUpdated(newConnectomeNetwork: Object) {
    console.log('connectomeNetworkThreed connectomeId=', this.connectomeNetworkThreed);
    if (!newConnectomeNetwork) {
      return;
    }

    this.nodes = this.connectomeNetworkThreed.nodes;
    this.links = this.connectomeNetworkThreed.links;

    this.renderMap(0, false);
  }

  renderNetworkMap3D(newConnectomeNetwork: any) {
    console.log('renderNetworkMap3D input=', newConnectomeNetwork);

    if (!newConnectomeNetwork) {
      return;
    }

    this.nodes = newConnectomeNetwork.nodes;
    this.links = newConnectomeNetwork.links;

    this.renderMap(0, false);
  }

  updateNetworkMap3D(newConnectomeNetwork: any) {
    console.log('onConnectomeIdUpdated input=', newConnectomeNetwork);

    if (!newConnectomeNetwork) {
      return;
    }
    this.nodes = newConnectomeNetwork.nodes;
    this.links = newConnectomeNetwork.links;

    console.log('try refresh graph 3D');

    let { nodes, links } = this.graph.graphData();

    let diffNodes = this.arrayDiff(this.nodes, nodes);
    let diffLinks = this.arrayDiff(this.links, links);

    if (diffNodes.length === 0 && diffLinks.length === 0) {
      console.log('no need to refresh graph 3D');
      return;
    }

    this.renderMap(STEP_CONNECTOME_RENDER.THIRD, true);
  }

  data() {
    return {};
  }

  mounted() {
    if (!this.graph) {
      this.width = Math.max($('#connectome-area').width(), 350);
      this.height = Math.max($('#connectome-area').height(), 350);
      const elem = document.getElementById('connectome-network-3d');

      if (!elem) {
        return;
      }
      this.graph = ForceGraph3D({ antialias: false, alpha: false })(elem).width(this.width).height(this.height);
    }
  }

  private arrayDiff(a: Array<any>, b: Array<any>) {
    return [...a.filter(x => !b.includes(x)), ...b.filter(x => !a.includes(x))];
  }

  updateMap(): void {
    this.graph
      .graphData({ nodes: this.nodes, links: this.links })
      .linkCurvature(null)
      .linkDirectionalParticleColor('rgba(255, 255, 255, 0.1)')
      .linkDirectionalParticleWidth(0)
      .linkDirectionalParticles(0)
      .onEngineStop(() => {
        this.graph
          .linkCurvature('curvature')
          .linkOpacity(1)
          .linkDirectionalParticleColor(link => (!this.onInit ? link._color : 'rgba(255, 255, 255, 0.1)'))
          .linkDirectionalParticleWidth(link => (!this.onInit ? link._size + 4 : 0))
          .linkDirectionalParticles(() => (!this.onInit ? 1 : 0));
      });
  }

  renderMap(startingTick: number, isUpdate: boolean): void {
    const elem = document.getElementById('connectome-network-3d');

    if (!this.graph) {
      this.width = Math.max($('#connectome-area').width(), 350);
      this.height = Math.max($('#connectome-area').height(), 350);

      if (!elem) {
        return;
      }
      this.graph = ForceGraph3D({ antialias: false, alpha: false })(elem).width(this.width).height(this.height);
    }

    if (!elem) {
      return;
    }

    const tick = startingTick;
    this.onInit = true;
    this.graph
      .graphData({ nodes: this.nodes, links: this.links })
      .showNavInfo(false)
      .nodeThreeObject(node => {
        if (!this.onInit) {
          const sprite = new SpriteText(node.name);
          sprite.material.depthWrite = false; // make sprite background transparent
          sprite.color = '#ffffff';
          sprite.textHeight = node.fontSize;
          sprite.translateZ(node._size + 10);
          return sprite;
        }
        return null;
      })
      .nodeOpacity(1.0)
      .nodeThreeObjectExtend(true)
      .onNodeClick(node => {
        this.onLeftClickOnNetworkNode(node);
      })
      .nodeColor(node => (!this.onInit ? node._color : 'rgba(255, 255, 255, 0.2)'))
      .nodeVal(0)
      .onNodeRightClick(node => {
        //console.log("test right click");
        this.onRightClickOnNetworkNode(node);
      })
      .linkColor(link => (!this.onInit ? link._color : 'rgba(255, 255, 255, 0.2)'))
      .linkWidth(link => (!this.onInit ? link._size : 1))
      .onEngineTick(() => {
        if (tick % 40 == 1 && tick < 122) {
          this.graph.zoomToFit(2000, 0, node => true);
        }
        if (tick > 200) {
          if (this.onInitFocusNode) {
            if (this.$route.query.node) {
              this.focusOnNodeByParam(this.$route.query.node);
            }
            this.onInitFocusNode = false;
          }
          if (this.onInit) {
            this.onInit = false;
            this.activeChart();
          }
        }
        tick++;
      })
      .onBackgroundClick(() => {
        //this.ClearSelection();
      })
      .onEngineStop(() => {
        if (this.onInitFocusNode) {
          if (this.$route.query.node) {
            this.focusOnNodeByParam(this.$route.query.node);
          }
          this.onInitFocusNode = false;
        }
        if (this.onInit) {
          this.onInit = false;
          this.activeChart();
        }
        if (this.onParticuleInit) {
          this.onParticuleInit = false;
          this.graph
            .linkCurvature('curvature')
            .linkOpacity(1)
            .linkDirectionalParticleColor(link => (!this.onInit ? link._color : 'rgba(255, 255, 255, 0.1)'))
            .linkDirectionalParticleWidth(link => (!this.onInit ? link._size + 4 : 0))
            .linkDirectionalParticles(() => (!this.onInit ? 1 : 0));
        }
      })
      .enableNodeDrag(false);

    const linkForce = this.graph.d3Force('link').distance(link => link._length);
    const chargeForce = this.graph.d3Force('charge').strength(-100);
    const centerForce = this.graph.d3Force('center', d3.forceCenter());
    const collisionForce = this.graph.d3Force(
      'collision',
      d3.forceCollide(node => node._size * 2)
    );
  }

  activeChart() {
    // trigger update of highlighted objects in scene
    this.graph
      .nodeThreeObject(this.graph.nodeThreeObject())
      .nodeColor(this.graph.nodeColor())
      .nodeVal(this.graph.nodeVal())
      .linkColor(this.graph.linkColor())
      .linkWidth(this.graph.linkWidth())
      .linkOpacity(0.4)
      .nodeColor(this.graph.nodeColor());
  }

  //Commands
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

  focusOnNode(targetNode: ConnectomeNodeOld) {
    if (!targetNode) {
      return;
    }

    this.nodes.every(node => {
      if (node.name === targetNode.label && node.type === targetNode.type) {
        // Aim at node from outside it
        const distance = node._size + 80;
        const distRatio = 1 + distance / Math.hypot(node.x, node.y, node.z);

        this.graph.cameraPosition(
          { x: node.x * distRatio, y: node.y * distRatio, z: node.z * distRatio }, // new position
          node, // lookAt ({ x, y, z })
          3000 // ms transition duration
        );
        return false;
      }
      return true;
    });
  }

  public chartResize() {
    this.width = Math.max($('#connectome-area').width(), 350);
    this.height = Math.max($('#connectome-area').height(), 350);
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
