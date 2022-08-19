import Component from 'vue-class-component';
import { Prop, Vue } from 'vue-property-decorator';

@Component({
  name: 'ds-stock-chart',
})
export default class DsStockChart extends Vue {
  @Prop(Object) readonly item: any | undefined;
  @Prop(Object) readonly stockCode: any | undefined;
  @Prop(Boolean) hasTitle: boolean | false;
  @Prop(Boolean) hasLegend: boolean | false;
  @Prop(Boolean) hasTooltip: boolean | false;
  @Prop(Boolean) dataZoom: boolean | false;

  private option = null;

  mounted(): void {
    this.getStock();
  }

  public getStock() {
    const titleCrypto = this.item && this.item.isCrypto ? this.item.isCrypto : '';
    const title =
      (titleCrypto ? `${this.stockCode.market} ${this.stockCode.symbol}` : `${this.stockCode.market}-${this.stockCode.stockCode}`) +
      ' data every 15 minutes';
    const upColor = '#ec0000';
    const upBorderColor = '#8A0000';
    const downColor = '#00da3c';
    const downBorderColor = '#008F28';
    const data = this.generateData(this.stockCode.dataChart);
    let grid = [
      {
        bottom: 200,
      },
      {
        height: 80,
        bottom: 80,
      },
    ];

    const gridSmall = [
      {
        height: 85,
        bottom: 125,
        left: '10%',
        right: '4%',
      },
      {
        height: 40,
        bottom: 60,
      },
    ];

    grid = this.item ? (this.item.dataSize != '2x1' ? grid : gridSmall) : grid;

    this.option = {
      // title: {
      //   text: title,
      //   textStyle: {
      //     fontSize: 14,
      //   },
      // },
      dataset: {
        source: data,
      },
      tooltip: {
        trigger: 'axis',
        axisPointer: {
          type: 'line',
        },
      },
      grid: grid,
      xAxis: [
        {
          type: 'category',
          scale: true,
          boundaryGap: false,
          // inverse: true,
          axisLine: { onZero: false },
          splitLine: { show: false },
          min: 'dataMin',
          max: 'dataMax',
        },
        {
          type: 'category',
          gridIndex: 1,
          scale: true,
          boundaryGap: false,
          axisLine: { onZero: false },
          axisTick: { show: false },
          splitLine: { show: false },
          axisLabel: { show: false },
          min: 'dataMin',
          max: 'dataMax',
        },
      ],
      yAxis: [
        {
          scale: true,
          splitArea: {
            show: true,
          },
        },
        {
          scale: true,
          gridIndex: 1,
          splitNumber: 2,
          axisLabel: { show: false },
          axisLine: { show: false },
          axisTick: { show: false },
          splitLine: { show: false },
        },
      ],
      axisPointer: {
        link: [
          {
            xAxisIndex: 'all',
          },
        ],
        label: {
          backgroundColor: '#777',
        },
      },
      dataZoom: [
        {
          type: 'inside',
          xAxisIndex: [0, 1],
          start: 10,
          end: 100,
        },
        {
          show: this.dataZoom,
          xAxisIndex: [0, 1],
          type: 'slider',
          bottom: 10,
          start: 10,
          end: 100,
        },
      ],
      visualMap: {
        show: false,
        seriesIndex: 1,
        dimension: 6,
        pieces: [
          {
            value: 1,
            color: upColor,
          },
          {
            value: -1,
            color: downColor,
          },
        ],
      },
      series: [
        {
          type: 'line',
          itemStyle: {
            color: upColor,
            color0: downColor,
            borderColor: upBorderColor,
            borderColor0: downBorderColor,
          },
          encode: {
            x: 0,
            y: [1, 4, 3, 2],
          },
          data: data,
        },
        {
          name: 'Volumn',
          type: 'bar',
          xAxisIndex: 1,
          yAxisIndex: 1,
          barWidth: 20,
          itemStyle: {
            color: '#7fbe9e',
          },
          large: true,
          encode: {
            x: 0,
            y: 5,
          },
          data: data,
        },
      ],
    };
  }

  // getDataFromSeries() {
  //   if (!this.item.series) {
  //     return;
  //   }
  //   this.item.data = [];
  //   for (const seri of this.item.series) {
  //     this.item.data.push(seri.name);
  //   }
  // }

  public generateData(stock) {
    const data = [];
    for (let i = 0; i < stock.length; i++) {
      const item = stock[i];
      data[i] = [
        dateStringToDate(item.baseYyyymmddhhmmss),
        +item.open,
        +item.close,
        +item.low,
        +item.high,
        +item.volume,
        getSign(data, i, item.open, item.close, 4), // sign
      ];
    }
    const dataReversedForDateAscending = data.reverse();
    return dataReversedForDateAscending;

    function getSign(data, dataIndex, openVal, closeVal, closeDimIdx) {
      let sign;
      if (openVal > closeVal) {
        sign = -1;
      } else if (openVal < closeVal) {
        sign = 1;
      } else {
        sign =
          dataIndex > 0
            ? // If close === open, compare with close of last record
              data[dataIndex - 1][closeDimIdx] <= closeVal
              ? 1
              : -1
            : // No record of previous, set to be positive
              1;
      }
      return sign;
    }
    function dateStringToDate(dateString) {
      try {
        const year = dateString.substring(0, 4);
        const month = dateString.substring(4, 6);
        const day = dateString.substring(6, 8);
        const hour = dateString.substring(8, 10);
        const minute = dateString.substring(10, 12);
        const second = dateString.substring(12, 14);
        // const date = new Date(year, month - 1, day, hour, minute, second);
        const date = year + '-' + month + '-' + day + '\n' + hour + ':' + minute + ':' + second;
        // const offset = date.getTimezoneOffset()
        // date = new Date(date.getTime() - (offset  60  1000));
        return date;
      } catch (error) {
        return null;
      }
    }
  }
}
