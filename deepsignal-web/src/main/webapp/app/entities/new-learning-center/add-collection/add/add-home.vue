<template>
  <div class="col-7">
    <div class="go-panel panel-LS-add">
      <div class="panel-content tab-content" style="flex: 0 0 100% !important; max-width: 100%">
        <div class="lc-page">
          <div class="lc-nav">
            <ul class="nav">
              <li class="nav-item" v-for="(tab, index) in tabs" :key="index" @click="changeTab(tab, $event)">
                <a :class="['nav-link', { active: tab.active }]" data-toggle="tab" aria-selected="true"
                  ><i class="go-icon-common" :class="tab.icon"></i>{{ tab.name }}</a
                >
              </li>
            </ul>
          </div>
        </div>
        <keep-alive>
          <component :is="currentTab" :displayMode.sync="displayMode"> </component>
        </keep-alive>
      </div>
    </div>
  </div>
</template>

<script src="./add-home.component.ts" lang="ts"></script>

<style>
.panel-LS-content {
  padding: 0 0 62px !important;
}
</style>
