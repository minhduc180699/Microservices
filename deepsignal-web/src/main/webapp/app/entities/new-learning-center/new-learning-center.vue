<template>
  <div id="lc-container">
    <learning-center-aside></learning-center-aside>
    <div class="lc-connectome">
      <div class="connectome-container">
        <div class="control-right">
          <div class="btn-group-vertical">
            <button type="button" class="btn btn-dark">
              <i class="bi bi-fullscreen"></i>
              <span>fullscreen</span>
            </button>
            <button type="button" class="btn btn-dark">
              <i class="bi bi-aspect-ratio"></i>
              <span>fit to screen</span>
            </button>
          </div>
          <div class="btn-group-vertical">
            <button type="button" class="btn btn-dark">
              <i class="bi bi-plus-lg"></i>
              <span>zoom in</span>
            </button>
            <button type="button" class="btn btn-dark">
              <i class="bi bi-dash-lg"></i>
              <span>zoom out</span>
            </button>
          </div>
        </div>
        <div class="control-bottom">
          <button type="button" class="btn btn-primary">Context Group 만들기</button>
        </div>
        <div class="connectome-area">
          <!-- sample images-->
          <img src="content/images/sample/@connectome.png" />
        </div>
      </div>
    </div>
  </div>
</template>
<script lang="ts" src="./new-learning-center.component.ts"></script>
