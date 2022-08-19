<template>
  <ds-card-top-ten-title v-if="item && item.isCardTopTenTitle" :item="item"></ds-card-top-ten-title>
  <ds-card-top-ten-content v-else-if="item && item.isCardTopTenContent" :item="item"></ds-card-top-ten-content>
  <ds-card-top-ten-default v-else :item="item"></ds-card-top-ten-default>
</template>

<script lang="ts" src="./ds-card-topten-parent.component.ts"></script>
