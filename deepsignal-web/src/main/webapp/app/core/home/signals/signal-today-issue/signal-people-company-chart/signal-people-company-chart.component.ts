import { Vue, Component, Prop, Watch } from 'vue-property-decorator';
import * as zc from '@najinuki/zoomcharts';

@Component({})
export default class SignalPeopleCompanyChart extends Vue {
  private netChart = null;
  private nodeDepthList: any[] = ['d1']; //d1(default::depth1), d2
  private isdepth2On = false;
  private infoElementVisible = false;
  chartData = {
    nodes: [],
    links: [],
  };

  DOC_PERSON_ORG_NODE_ID = [];

  @Prop() readonly currentSignal: any | undefined;
  @Prop() readonly idChart: string | 'peopleChart';
  @Prop(String) readonly tab: any | '';

  @Watch('currentSignal')
  onCurrentSignalChange(newValue) {
    this.initNetChart();
  }

  mounted(): void {
    this.initNetChart();
  }

  convertData() {
    if (this.currentSignal['neuronNetworkChart']['nodes']) {
      for (const item of this.currentSignal['neuronNetworkChart']['nodes']) {
        if (item.nerType === 'PERSON' || item.nerType === 'DOC' || item.nerType === 'ORG') {
          this.chartData.nodes.push(item);
          this.DOC_PERSON_ORG_NODE_ID.push(item['id']);
        }
      }
      // this.chartData.nodes = [
      //   // {"id": "n1", "loaded": true},
      //   // {"id": "n2", "loaded": true},
      //   // {"id": "n3", "loaded": true}
      // ];
      // this.chartData.links = [
      //   {"id": "l1", "from": "n1", "to": "n2",},
      //   {"id": "l2", "from": "n2", "to": "n3",},
      //   {"id": "l3", "from": "n3", "to": "n1",}
      // ];

      for (const link of this.currentSignal['neuronNetworkChart']['links']) {
        if (this.DOC_PERSON_ORG_NODE_ID.includes(link.from) && this.DOC_PERSON_ORG_NODE_ID.includes(link.to)) {
          this.chartData.links.push(link);
        }
      }
      this.makeChart(this.chartData);
    }
  }

  initNetChart() {
    this.chartData.nodes = [];
    this.chartData.links = [];
    this.DOC_PERSON_ORG_NODE_ID = [];
    if (this.currentSignal && this.currentSignal['neuronNetworkChart']) {
      this.convertData();
    }
  }

  makeChart(chartData) {
    if (this.netChart) {
      this.netChart.remove();
    }

    // @ts-ignore
    const width = this.$refs['ref' + this.idChart].clientWidth;
    // @ts-ignore
    const height = this.$refs['ref' + this.idChart].clientHeight;
    this.netChart = new zc.NetChart({
      container: document.getElementById(this.idChart),
      area: { width: width, height: height },
      // @ts-ignore
      data: { preloaded: chartData },
      numberOfParallelRequests: 5, //Max number of parallel data requests to issue. More requests will result in faster loading, but might put heavy load on server. => default:3
      navigation: {
        initialNodes: [chartData.nodes[0].name],
      },
      toolbar: {
        items: [
          {
            // @ts-ignore
            item: 'showLabels',
            enabled: false,
          },
          {
            item: 'fullscreen',
            enabled: true,
            onClick: this.clickFullscreen,
          },
        ],
      },
      style: {
        link: {
          // toDecoration: 'open arrow',
          radius: 1,
        },
        node: {
          imageCropping: true,
          onImageLoadError: node => {
            node.image = 'content/images/ETC.png';
            node.data.style.image = 'content/images/ETC.png';
          },
        },
        linkStyleFunction: link => {
          if (link.from.focused || link.to.focused || link.from.hovered || link.to.hovered) {
            link.radius = 5;
          } else {
            link.radius = 1;
          }
        },
        nodeDetailMinSize: 10,
        nodeDetailMinZoom: 0.1,
        nodeLabelScaleBase: 70,
        nodeLabel: {
          margin: 10,
          padding: 20,
          aspectRatio: 0,
          textStyle: {
            font: '20px Arial',
          },
        },
        nodeStyleFunction: node => {
          node.label = node.data['name'];
          if (node.data['nerType'] && node.data['nerType'].length > 0 && node.data['nerType'] !== 'O') {
            node.image = 'content/images/' + node.data['nerType'] + '.png';
          } else {
            node.image = 'content/images/ETC.png';
          }
          const links = node.links;
          if (node.focused || node.hovered) {
            node.opacity = 1;
            node.lineColor = '#78909c';
            node.lineWidth = 5;
            node.radius = node.radius * 1.5;

            for (const i in links) {
              links[i].radius = 10;
            }
          } else {
            node.opacity = 0.8;
          }
        },
        selection: {
          fillColor: 'transparent',
        },
      },
      events: {
        onHoverChange: (event, args) => {
          //this.netChart.reloadData();
          let content = '';
          if (args.hoverItem) {
            // console.log('hoverItem', args.hoverItem);
            // @ts-ignore
            content = args.hoverItem.label;
          } else if (args.hoverNode) {
            content = '<strong>&nbsp;name : </strong>' + args.hoverNode.label + '&nbsp;';
            // @ts-ignore
            if (args.hoverNode.data.executive != undefined && args.hoverNode.data.type == 'people') {
              // @ts-ignore
              content += '</br><strong>&nbsp;title : </strong>' + args.hoverNode.data.executive + '&nbsp;';
            }
          } else if (args.hoverLink) {
            content = args.hoverLink.label;
          }

          this.infoElementVisible = !!content;
        },
        onClick: this.graphClick,
        // onRightClick: event => {
        //   this.rightClick(event);
        //   event.preventDefault();
        // },
      },
      filters: {},
    });
  }

  clickFullscreen() {
    if (!this.netChart.fullscreen()) {
      const removeindex = this.nodeDepthList.indexOf('d2');
      if (removeindex != -1) {
        this.nodeDepthList.splice(removeindex, 1);
        this.netChart.clearFocus();
        this.netChart.updateFilters();
        this.isdepth2On = false;
        this.netChart.fullscreen(false);
        this.netChart.resetLayout();
      }
    }
  }

  graphClick(e) {
    if (e.clickNode) {
      const node = e.clickNode;
      this.netChart.clearFocus();
      this.netChart.addFocusNode(node.id);

      const scrollToNodes = () => {
        const arr = [];
        arr.push(node.id);
        this.netChart.selection(arr);
        this.netChart.scrollIntoView(arr);
      };
      scrollToNodes();
    } else {
      this.netChart.clearFocus();
    }
  }
}
