<template>
  <div>
    <div class="connectome-control">
      <div class="control-screen">
        <div class="control-screen">
          <div class="btn-group-vertical">
            <!-- <button type="button" class="btn btn-dark btn-toggle" data-toggle="button" aria-pressed="false" v-on:click="fullScreen">
              <b-icon icon="fullscreen"></b-icon>
              <span
                ><em><span v-text="$t('connectome-map.leave')" /></em>
                <span v-text="$t('connectome-map.fullscreen')" /></span>
            </button> -->
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
      <map-side-bar></map-side-bar>
    </div>
    <div class="connectome-area"></div>
  </div>
</template>
<script lang="ts" src="./map-3d-network.component.ts"></script>
<style scoped>
#connectome-network {
  background-color: black;
}
</style>
