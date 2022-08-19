<template>
  <aside id="ma-aside">
    <!-- <div class="ma-aside-head">
      <button class="btn" data-toggle="button" aria-pressed="false">
        <span class="icon-submenu icon-folded"></span>
        <span class="text">Persona</span>
      </button>
    </div> -->
    <div class="ma-aside-body">
      <ul class="nav ma-aside-nav">
        <li class="nav-item" v-for="(item, index) in tabs" :key="index">
          <router-link :to="item.disable ? '' : item.link">
            <a
              :class="item.value === mainVerticalTabIndex ? 'nav-link active' : 'nav-link'"
              @click="changeTab(item.value, item.disable)"
              v-b-tooltip.hover.topright="item.name"
            >
              <span class="icon-submenu" :class="item.icon"></span>
            </a>
          </router-link>
        </li>
      </ul>
    </div>
    <!-- <div class="ma-aside-foot">
      <a class="btn new" href="#">
        <span class="icon-common-lg icon-noti"></span>
        <span class="text">Notification</span>
      </a>
      <a class="btn" href="#">
        <span class="icon-common-lg icon-help"></span>
        <span class="text">Help</span>
      </a>
    </div> -->
  </aside>
</template>

<script lang="ts" src="./main-aside.component.ts"></script>
