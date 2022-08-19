<template>
  <div>
    <div class="connectome-control">
      <mini-map-bottom-bar></mini-map-bottom-bar>
      <div class="control-screen">
        <div class="btn-group-vertical">
          <button type="button" class="btn btn-dark" v-on:click="fitScreen">
            <b-icon icon="aspect-ratio"></b-icon>
            <span><span v-text="$t('connectome-map.fit-to-screen')" /></span>
          </button>
        </div>
        <div class="btn-group-vertical">
          <button type="button" class="btn btn-dark" v-on:click="zoomIn">
            <b-icon icon="plus" size="lg"></b-icon>
            <span><span v-text="$t('connectome-map.zoom-in')" /></span>
          </button>
          <button type="button" class="btn btn-dark" v-on:click="zoomOut">
            <b-icon icon="dash" size="lg"></b-icon>
            <span><span v-text="$t('connectome-map.zoom-out')" /></span>
          </button>
        </div>
      </div>
    </div>
    <div class="connectome-area"></div>
  </div>
</template>
<script lang="ts" src="./mini-map-2d-network.component.ts"></script>
<style scoped>
.connectome-area {
  background-color: white;
}
</style>
