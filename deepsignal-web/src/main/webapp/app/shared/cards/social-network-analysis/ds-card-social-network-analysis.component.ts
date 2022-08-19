import { Component, Vue, Prop, Inject, Watch } from 'vue-property-decorator';
import DsCardFooter from '@/shared/cards/footer/ds-card-footer.vue';
import DsNetworkChart from '@/shared/chart/ds-network-chart/ds-network-chart.vue';
import PeopleService from '@/core/home/people/people.service';
import * as zc from '@najinuki/zoomcharts';

@Component({
  components: {
    'ds-card-footer': DsCardFooter,
    'ds-network-chart': DsNetworkChart,
  },
  computed: {
    nodeName() {
      return this.$store.getters.nodeName;
    },
  },
  watch: {
    nodeName(newValue, oldValue) {
      this.title = newValue;
      this.getNetworkChartData(newValue);
    },
  },
})
export default class DsCardSocialNetworkAnalysis extends Vue {
  @Inject('peopleService')
  private peopleService: () => PeopleService;

  private netChart = null;
  private pieChart;
  private nodeTypeList: any[] = ['wiki']; //wiki(default), twitter, executive
  private nodeDepthList: any[] = ['d1']; //d1(default::depth1), d2
  private infoElementVisible = false;
  private lastPieChartNode;
  private lastSettings;

  // sort :: twitter, facebook, instagram, wiki, linkedin, youtube, tiktok, investing
  private imagePath = '../../../content/images/';
  private sliceColors = ['#1DA1F2', '#3b5998', '#bc2a8d', '#f0f8ff', '#0e76a8', '#c4302b', '#010101', '#010101'];
  private sliceIcons = [
    'social_icon_twitter.png',
    'social_icon_facebook.png',
    'social_icon_instagram.png',
    'social_icon_wiki_black.png',
    'social_icon_linkedin.png',
    'social_icon_youtube.png',
    'social_icon_tiktok.png',
    'social_icon_investing.png',
  ];

  private companyDefaultImg = this.imagePath + 'icon-organization_back.png';
  private peopleDefaultImg = this.imagePath + 'icon-person_back.png';

  private isTwitterOn = false;
  private isExecutiveOn = false;
  private isdepth2On = false;
  isLoading = false;
  private title = '';
  private fromNode = [];

  getNetworkChartData(title: string) {
    const removeTwitterIndex = this.nodeTypeList.indexOf('twitter');
    if (removeTwitterIndex != -1) {
      this.nodeTypeList.splice(removeTwitterIndex, 1);
      this.isTwitterOn = false;
    }

    const removeExecutiveIndex = this.nodeTypeList.indexOf('executive');
    if (removeExecutiveIndex != -1) {
      this.nodeTypeList.splice(removeExecutiveIndex, 1);
      this.isExecutiveOn = false;
    }

    const removeDepthIndex = this.nodeTypeList.indexOf('d2');
    if (removeDepthIndex != -1) {
      this.nodeDepthList.splice(removeDepthIndex, 1);
      this.isdepth2On = false;
    }

    this.title = title;
    this.isLoading = true;
    const result = this.peopleService().getNetworkChart(title, this.$i18n.locale);
    if (!result) {
      return;
    }
    result.then(res => {
      if (!res.data) {
        return;
      }
      const data = res.data;
      data.links.forEach((item, index) => {
        if (item.from == this.title) {
          this.fromNode.push(item.to);
        }

        item.style.fillColor = '#888888';
      });
      this.fromNode.push(this.title);
      this.makeChart(data);
    });
  }

  makeChart(chartData) {
    if (this.netChart) {
      this.netChart.remove();
    }

    // @ts-ignore
    const width = this.$refs.cardChart.clientWidth;
    // @ts-ignore
    const height = this.$refs.cardChart.clientHeight;
    this.netChart = new zc.NetChart({
      container: document.getElementById('social-network-chart'),
      area: { width: width, height: height },
      // @ts-ignore
      data: { preloaded: chartData },
      numberOfParallelRequests: 5, //Max number of parallel data requests to issue. More requests will result in faster loading, but might put heavy load on server. => default:3
      navigation: {
        initialNodes: [this.title],
      },
      toolbar: {
        items: [
          {
            // @ts-ignore
            item: 'showLabels',
            enabled: true,
          },
          {
            item: 'zoomControl',
            enabled: true,
          },
          {
            item: 'fit',
            enabled: true,
          },
          {
            item: 'rearrange',
            enabled: true,
          },
          {
            item: 'freeze',
            enabled: true,
          },
          {
            item: 'back',
            enabled: true,
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
            if (node.data.type == 'people') {
              node.image = this.peopleDefaultImg;
              node.data.style.image = this.peopleDefaultImg;
            } else if (node.data.type == 'company') {
              node.image = this.companyDefaultImg;
              console.log(this.companyDefaultImg);
              node.data.style.image = this.companyDefaultImg;
            }
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
          aspectRatio: 15,
          textStyle: {
            font: '20px Arial',
          },
        },
        nodeStyleFunction: node => {
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
        onSelectionChange: this.updatePieChart,
        onPositionChange: this.movePieChart,
        onHoverChange: (event, args) => {
          //this.netChart.reloadData();
          let content = '';
          if (args.hoverItem) {
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
          document.getElementById('divInfo').innerHTML = content;
          document.getElementById('inforBox').style.display = this.infoElementVisible ? 'block' : 'none';
        },
        onClick: this.graphClick,
        onRightClick: event => {
          this.rightClick(event);
          event.preventDefault();
        },
      },
      filters: {
        nodeFilter: (nodeData: any, linkData: any) => {
          if (nodeData.style.radius == 15) nodeData.style.radius = 30;
          if (this.nodeTypeList.indexOf(nodeData.resource) == -1 || linkData.length == 0 || nodeData.count <= 2) {
            return false;
          } else if (this.nodeDepthList.indexOf(nodeData.depth) == -1) {
            if (
              nodeData.depth == 'd2' &&
              nodeData.resource == 'executive' &&
              this.netChart
                .nodes()
                .filter(n => n.data.depth == 'd1' && n.data.stockId && n.data.type == 'company')
                .map(n => n.data.stockId)
                .includes(nodeData.stockId)
            ) {
              return true;
            } else {
              return false;
            }
          } else {
            return true;
          }
        },
        linkFilter: (linkData: any, fromData: any, toData: any) => {
          if (this.nodeTypeList.indexOf(linkData.type) == -1) {
            return false;
          } else {
            return true;
          }
        },
      },
    });

    this.pieChart = new zc.PieChart({
      parentChart: this.netChart,
      // @ts-ignore
      data: { preloaded: { subvalues: [] } },
      pie: {
        style: { sliceColors: this.sliceColors },
      },
      slice: {
        styleFunction: (slice: any, data: any) => {
          slice.url = data.link;
          slice.icon = this.imagePath + this.sliceIcons[data.idx];
          slice.fillColor = this.sliceColors[data.idx];
        },
      },
      labels: { enabled: false },
      info: {
        contentsFunction: data => {
          return data.name;
        },
      },
    });

    this.isLoading = false;
  }

  rightClick(event) {
    this.title = event.clickNode.label;
    this.getNetworkChartData(this.title);
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

  disposeDemo() {
    document.getElementById('divInfo').parentNode.removeChild(document.getElementById('divInfo'));
  }

  hidePieChart() {
    this.pieChart.updateSettings({ area: { left: -100, top: -100, width: 0, height: 0 } });
    this.lastSettings = null;
    this.lastPieChartNode = null;
  }

  updatePieChart() {
    const selectedNodes = this.netChart.selection();
    if (selectedNodes.length === 0) {
      this.hidePieChart();
      return;
    }

    const currentNode = selectedNodes[0];
    if (currentNode.data.categories == null || currentNode.data.categories.length === 0) {
      this.hidePieChart();
      return;
    }

    if (currentNode === this.lastPieChartNode) {
      return;
    }
    this.lastPieChartNode = currentNode;
    const pieChartSettings = this.getPieChartDimensions(currentNode);
    // @ts-ignore
    pieChartSettings.data = { preloaded: { subvalues: currentNode.data.categories } };
    // @ts-ignore
    pieChartSettings.navigation = {};

    this.pieChart.updateSettings(pieChartSettings);
  }

  getPieChartDimensions(node) {
    const dimensions = this.netChart.getNodeDimensions(node);
    const output = {
      area: {
        left: dimensions.x - dimensions.radius * 5,
        top: dimensions.y - dimensions.radius * 5,
        width: dimensions.radius * 10,
        height: dimensions.radius * 10,
      },
      pie: {
        radius: dimensions.radius + 40,
        innerRadius: dimensions.radius,
      },
    };
    return output;
  }

  movePieChart() {
    const selectedNodes = this.netChart.selection();
    if (selectedNodes.length === 0) {
      return;
    }

    const currentNode = selectedNodes[0];
    if (currentNode.data.categories == null || currentNode.data.categories.length === 0) {
      return;
    }

    const settings = this.getPieChartDimensions(currentNode);
    const currentArea = settings.area;
    if (
      this.lastSettings &&
      this.lastSettings.area.left === currentArea.left &&
      this.lastSettings.area.top === currentArea.top &&
      this.lastSettings.pie.radius === settings.pie.radius
    ) {
      return;
    }

    this.lastSettings = settings;
    this.pieChart.updateSettings(settings);
  }

  updateChart(value) {
    if (value === 'twitter') {
      if (this.isTwitterOn) {
        const removeindex = this.nodeTypeList.indexOf(value);
        if (removeindex !== -1) {
          this.nodeTypeList.splice(removeindex, 1);
          this.netChart.clearFocus();
          this.netChart.updateFilters();
          this.isTwitterOn = false;
        }
      } else {
        this.nodeTypeList.push(value);
        this.netChart.clearFocus();
        this.netChart.updateFilters();
        this.isTwitterOn = true;
      }
    } else {
      if (this.isExecutiveOn) {
        const removeindex = this.nodeTypeList.indexOf(value);
        if (removeindex !== -1) {
          this.nodeTypeList.splice(removeindex, 1);
          this.netChart.clearFocus();
          this.netChart.updateFilters();
          this.isExecutiveOn = false;
        }
      } else {
        this.nodeTypeList.push(value);
        this.netChart.clearFocus();
        this.netChart.updateFilters();
        this.isExecutiveOn = true;
      }
    }
  }

  updateDepth(value) {
    if (value == 'd2') {
      if (this.isdepth2On == true) {
        const removeindex = this.nodeDepthList.indexOf(value);
        if (removeindex != -1) {
          this.nodeDepthList.splice(removeindex, 1);
          this.netChart.clearFocus();
          this.netChart.updateFilters();
          this.isdepth2On = false;

          this.netChart.fullscreen(false);
          this.netChart.resetLayout();
        }
      } else {
        this.nodeDepthList.push(value);
        this.netChart.clearFocus();
        this.netChart.updateFilters();
        this.isdepth2On = true;

        this.netChart.fullscreen(true);
        this.netChart.resetLayout();
      }
    }
  }
}
