import { Component, Prop, Vue } from 'vue-property-decorator';
import * as echarts from 'echarts';

@Component
export default class stackedLineChart extends Vue {
  @Prop(String) readonly id: any | string;
  @Prop(Object) readonly data: any | string;
  private chartScreen;

  mounted() {
    this.initStackedLineChart();
  }

  initStackedLineChart() {
    const chart = echarts.init(document.getElementById(this.id));
    const that = this;
    const option = {
      title: {
        text: '',
        textStyle: {
          fontSize: 14,
        },
      },
      tooltip: {
        trigger: 'axis',
      },
      legend: {
        data: this.data.legend,
        left: 'center',
        textStyle: {
          fontSize: 9,
          marginLeft: 100,
        },
      },
      grid: {
        left: '3%',
        right: '6%',
        bottom: '3%',
        containLabel: true,
      },
      toolbox: {
        feature: {
          saveAsImage: {
            show: false,
          },
          myTool: {
            //Custom tool myTool
            show: true,
            title: 'Full screen display',
            icon: 'image://content/images/icon-full-screen-a.png',
            onclick: function (e) {
              //Generate full screen display chart
              const element = document.getElementById('fullScreenMask' + that.id);
              const style = window.getComputedStyle(element);

              if (style.getPropertyValue('display') === 'block') {
                element.style.display = 'none';
                that.chartScreen = null;
                return;
              }

              element.style.display = 'block';
              option.series[0].sizeRange = [12, 60];
              that.chartScreen = echarts.init(document.getElementById('fullScreen' + that.id));
              that.chartScreen.setOption(option);
              that.chartScreen.setOption({
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
      xAxis: {
        type: 'category',
        boundaryGap: false,
        data: this.data.x,
      },
      yAxis: {
        type: 'value',
      },
      series: this.data.y,
    };

    chart.setOption(option);
  }
}
