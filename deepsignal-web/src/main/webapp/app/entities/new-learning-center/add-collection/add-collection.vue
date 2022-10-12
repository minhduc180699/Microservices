<template>
  <div class="container" id="lc-container" style="padding: 20px 0">
    <div class="row">
      <add-home v-on:fullScreenMode="handleFullScreen"></add-home>
      <collection :fullScreenMode="fullScreenMode"></collection>
    </div>
  </div>
</template>

<script src="./add-collection.component.ts" lang="ts"></script>
