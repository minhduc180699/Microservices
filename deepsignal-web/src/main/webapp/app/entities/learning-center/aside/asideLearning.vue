<template>
  <aside id="lc-aside">
    <div class="learning-type">
      <div class="lc-aside-nav list-group" id="list-tab">
        <a
          v-for="(item, index) in data"
          :key="index"
          :class="getClass(index)"
          :id="item.id"
          data-toggle="list"
          :aria-controls="item.ariaControls"
          @click="changeTab(index)"
        >
          <i :class="item.icon"></i>
          <h2>{{ item.name }}</h2>
        </a>
      </div>
    </div>
  </aside>
</template>

<script lang="ts" src="./asideLearning.component.ts"></script>
