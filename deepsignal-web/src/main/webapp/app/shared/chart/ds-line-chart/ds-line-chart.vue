<template>
  <div style="width: 100%; height: 100%">
    <div :id="id" style="width: 100%; height: 100%"></div>
    <div
      :id="'fullScreenMask' + id"
      style="position: fixed; top: 0; left: 0; width: 100%; height: 100%; display: none; background-color: #ffffff; z-index: 1000"
    >
      <div :id="'fullScreen' + id" style="height: 100%; width: 100%"></div>
    </div>
  </div>
</template>

<script lang="ts" src="./ds-line-chart.component.ts"></script>
<style>
.issue-content .is-analysis-wrap .tab-content .preview-area {
  overflow: unset !important;
  text-align: left !important;
}
</style>
