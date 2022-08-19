import { Component, Prop, Vue, Watch } from 'vue-property-decorator';
import * as echarts from 'echarts';
import 'echarts-wordcloud';

@Component({})
export default class SignalWordCloudChart extends Vue {
  @Prop() readonly currentSignal: any | undefined;
  @Prop() readonly idChart: string | 'wordCloud';
  @Prop(String) readonly tab: any | '';

  chartData = [];
  chartScreen;

  mounted(): void {
    // if (this.idChart !== 'todayIssue') {
    this.initWordCloudChart();
    // }
  }

  convertDataForChart(rawData: []) {
    if (!rawData) {
      return;
    }
    for (const item of rawData) {
      const word = {
        name: item['wordName'],
        value: item['wordCount'],
      };
      this.chartData.push(word);
    }
  }

  // @Watch('tabSelected')
  // onChangeTab(type) {
  //   this.initWordCloudChart();
  // }

  @Watch('currentSignal')
  onCurrentSignalChange(newValue) {
    this.initWordCloudChart();
  }

  initWordCloudChart() {
    if (!this.currentSignal) {
      return;
    }
    this.chartData = [];
    this.convertDataForChart(this.currentSignal['wordClouds']);
    const chart = echarts.init(document.getElementById(this.idChart));
    this.chartScreen = echarts.init(document.getElementById('fullScreen' + this.idChart));
    const that = this;
    const option = {
      tooltip: {},
      toolbox: {
        itemSize: 16,
        showTitle: false,
        bottom: 0,
        left: 0,
        feature: {
          myTool: {
            //Custom tool myTool
            show: true,
            title: 'Full screen display',
            icon: 'image://content/images/icon-full-screen-a.png',
            onclick: function (e) {
              //Generate full screen display chart
              const element = document.getElementById('fullScreenMask' + that.idChart);
              const style = window.getComputedStyle(element);

              if (style.getPropertyValue('display') === 'block') {
                element.style.display = 'none';
                this.chartScreen = null;
                return;
              }

              element.style.display = 'block';
              option.series[0].sizeRange = [12, 60];
              this.chartScreen = echarts.init(document.getElementById('fullScreen' + that.idChart));
              if (this.chartScreen) {
                this.chartScreen.resize();
              }
              // this.chartScreen.setOption(option);
              this.chartScreen.setOption({
                toolbox: {
                  feature: {
                    myTool: {
                      title: 'Exit fullscreen',
                      icon: 'image://content/images/icon-full-screen-i.png',
                    },
                  },
                },
              });
            },
          },
        },
      },
      series: [
        {
          type: 'wordCloud',

          // The shape of the "cloud" to draw. Can be any polar equation represented as a
          // callback function, or a keyword present. Available presents are circle (default),
          // cardioid (apple or heart shape curve, the most known polar equation), diamond (
          // alias of square), triangle-forward, triangle, (alias of triangle-upright, pentagon, and star.
          shape: 'circle',

          // Keep aspect ratio of maskImage or 1:1 for shapes
          // This option is supported from echarts-wordcloud@2.1.0
          keepAspect: false,

          // A silhouette image which the white area will be excluded from drawing texts. The shape option will continue to apply as the shape of the cloud to grow.
          // maskImage: maskImage,

          // Folllowing left/top/width/height/right/bottom are used for positioning the word cloud
          // Default to be put in the center and has 75% x 80% size.
          left: 'center',
          top: 'center',
          width: '90%',
          height: '90%',
          right: null,
          bottom: null,

          // Text size range which the value in data will be mapped to.
          // Default to have minimum 12px and maximum 60px size.
          sizeRange: [8, 20],

          // Text rotation range and step in degree. Text will be rotated randomly in range [-90, 90] by rotationStep 45
          rotationRange: [-90, 90],
          rotationStep: 90,

          // size of the grid in pixels for marking the availability of the canvas
          // the larger the grid size, the bigger the gap between words.
          gridSize: 7,

          // set to true to allow word being draw partly outside of the canvas.
          // Allow word bigger than the size of the canvas to be drawn
          drawOutOfBound: false,

          // If perform layout animation.
          // NOTE disable it will lead to UI blocking when there is lots of words.
          layoutAnimation: true,

          // Global text style
          textStyle: {
            fontFamily: 'sans-serif',
            fontWeight: 'bold',
            // Color can be a callback function or a color string
            color: function () {
              // Random color
              return (
                'rgb(' + [Math.round(Math.random() * 160), Math.round(Math.random() * 160), Math.round(Math.random() * 160)].join(',') + ')'
              );
            },
          },
          emphasis: {
            focus: 'self',

            textStyle: {
              textShadowBlur: 2,
              textShadowColor: '#333',
            },
          },

          // Data is an array. Each array item must have name and value property.
          data: this.chartData,
        },
      ],
    };

    chart.setOption(option);
    this.chartScreen.setOption(option);
  }
}
