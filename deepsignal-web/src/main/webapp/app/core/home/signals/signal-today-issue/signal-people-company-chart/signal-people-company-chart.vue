<template>
  <div style="height: 100%" :id="idChart" :ref="'ref' + idChart" :style="tab == 'detail' ? { minHeight: '450px' } : {}"></div>
</template>

<script lang="ts" src="./signal-people-company-chart.component.ts"></script>

<style scoped></style>
