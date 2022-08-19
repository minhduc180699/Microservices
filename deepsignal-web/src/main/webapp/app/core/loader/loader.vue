<template>
  <b-overlay :show="loading" rounded="sm" no-wrap class="position-fixed">
    <template #overlay>
      <div class="collecting">
        <div class="loading">
          <div class="ring"></div>
        </div>
        <!--        <p>커넥톰을 <br />수집중입니다.</p>-->
      </div>
    </template>
  </b-overlay>
</template>
<script lang="ts" src="./loader.component.ts"></script>
