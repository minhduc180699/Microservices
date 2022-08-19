<template>
  <div class="chart">
    <ds-echart :option="option"></ds-echart>
  </div>
</template>

<script lang="ts" src="./ds-stock-chart.component.ts"></script>
<style>
.chart {
  /*height: 100%;*/
  height: 450px;
  width: 100%;
}
.chart .echarts {
  min-height: 218px;
}
[data-template='chart'] .swiper-pagination {
  position: absolute !important;
  bottom: 0px !important;
}
[data-template='chart'] .btn-close {
  position: absolute;
  right: 25px;
}
</style>
