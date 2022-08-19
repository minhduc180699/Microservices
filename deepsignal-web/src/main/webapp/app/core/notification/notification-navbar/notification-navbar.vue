<template>
  <div class="dropdown-menu" v-stop-propagation>
    <div class="util-content notice-content">
      <div class="top-bar">
        <strong class="top-title" v-text="$t('global.menu.notification')">notification</strong>
        <div class="top-elements">
          <a type="button" class="btn btn-icon" title="Refresh" @click="pagingNotification">
            <span class="icon-common icon-refresh"></span>
          </a>
          <a type="button" class="btn btn-icon" title="Notification settings">
            <span class="icon-common icon-setting"></span>
          </a>
        </div>
      </div>
      <notification-common :hasScroll="true" ref="notificationCommon" @newNotification="newNotification"></notification-common>
      <div class="btn-area">
        <router-link to="/notifications" v-slot="{ href, navigate }" custom>
          <a :href="href" @click="navigate" class="btn btn-block">See all notifications<i class="icon-common icon-arrow-right"></i></a>
        </router-link>
        <!--        <a class="btn btn-block" href="/notifications">See all notifications<i class="icon-common icon-arrow-right"></i></a>-->
      </div>
    </div>
  </div>
</template>
<script lang="ts" src="./notification-navbar.component.ts"></script>
