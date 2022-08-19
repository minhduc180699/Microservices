<template>
  <div style="height: 95%; width: 100%">
    <div :id="idChart" style="height: 100%; width: 100%" :style="tab == 'detail' ? { minHeight: '450px' } : {}"></div>
    <div
      :id="'fullScreenMask' + idChart"
      style="position: fixed; top: 0; left: 0; width: 100%; height: 100%; display: none; background-color: #ffffff; z-index: 1000"
    >
      <div :id="'fullScreen' + idChart" style="height: 100%; width: 100%"></div>
    </div>
  </div>
  <!--    {{currentSignal['clusterName']}}-->
</template>
<style>
#fullScreenMask {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  display: none;
  background-color: #ffffff;
  z-index: 1000;
}
#fullScreen {
  width: 100%;
  height: 100%;
}
</style>
<script lang="ts" src="./signal-word-cloud-chart.component.ts"></script>
