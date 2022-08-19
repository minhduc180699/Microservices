<template>
  <div id="app" @mousedown="showMore($event)">
    <div v-if="isShowHeatMap" class="btnCloseHeatmap" @click="changeIsShowHeatMap">
      <button class="btn btn-secondary btn-block" style="font-size: 12px; padding: 0.2rem 0.6rem">Close Heatmap</button>
    </div>
    <ds-loader></ds-loader>
    <ribbon></ribbon>
    <ds-navleft></ds-navleft>
    <div id="header-block">
      <header v-show="isRegister">
        <nav class="navbar navbar-sub fixed-top">
          <ul class="nav nav-util">
            <li class="nav-item">
              <a class="nav-link" title="이전 페이지">
                <!--                <i class="icon-common-lg icon-arrow-left"></i>-->
              </a>
            </li>
          </ul>
          <ul class="nav nav-util">
            <li class="nav-item" v-if="loginPage">
              <router-link to="/login" v-slot="{ navigate }" custom>
                <a class="nav-link" @click="changeStatePage(navigate, $event, true)">
                  <i class="icon-common-lg icon-login" :title="$t('register.signIn')"></i>
                </a>
              </router-link>
            </li>
            <li class="nav-item" v-else>
              <router-link to="/register" v-slot="{ navigate }" custom>
                <a class="nav-link" :title="$t('login.signUp')" @click="changeStatePage(navigate, $event, false)">
                  <i class="icon-common-lg icon-signup"></i>
                </a>
              </router-link>
            </li>
            <li class="nav-item">
              <a class="nav-link" :title="$t('login.changeLanguage')" data-toggle="dropdown" aria-expanded="true">
                <i class="icon-common-lg icon-language"></i>
              </a>
              <div class="dropdown-menu dropdown-menu-right">
                <div class="dropdown-item-inner">
                  <a
                    class="dropdown-item"
                    :class="currentLanguage == key ? 'active' : ''"
                    v-for="(value, key) in languages"
                    :key="`lang-${key}`"
                    @click="changeLanguage(key)"
                    >{{ value.name }}</a
                  >
                </div>
              </div>
            </li>
          </ul>
        </nav>
      </header>
      <ds-navbar @changeIsShowHeatMap="changeIsShowHeatMap" :isShowHeatMap="isShowHeatMap" ref="logout"></ds-navbar>
    </div>
    <div id="container-block">
      <show-more></show-more>
      <keep-alive include="my-ai" max="6">
        <router-view></router-view>
      </keep-alive>
      <ds-toast-learning ref="dsToastLearning"></ds-toast-learning>
    </div>
    <ds-footer></ds-footer>
    <ds-loader></ds-loader>
  </div>
</template>

<script lang="ts" src="./app.component.ts"></script>
<style src="vue-multiselect/dist/vue-multiselect.min.css"></style>
