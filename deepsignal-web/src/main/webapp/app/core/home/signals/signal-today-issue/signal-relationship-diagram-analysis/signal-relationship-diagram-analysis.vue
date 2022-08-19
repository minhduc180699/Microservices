<template>
  <div style="height: 100%" :style="tab == 'detail' ? { minHeight: '450px' } : {}" :id="idChart" :ref="'ref' + idChart"></div>
</template>
<script lang="ts" src="./signal-relationship-diagram-analysis.component.ts"></script>
