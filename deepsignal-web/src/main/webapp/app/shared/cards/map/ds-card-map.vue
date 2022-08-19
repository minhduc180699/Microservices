<template>
  <!-- [Map] -->
  <div class="ds-card">
    <div class="ds-card-header">
      <div class="ds-card-title">{{ item.title }}</div>
    </div>
    <div class="ds-card-body">
      <div class="map">
        <img :src="item.image" alt="" />
      </div>
    </div>
  </div>
</template>
<script lang="ts" src="./ds-card-map.component.ts"></script>
