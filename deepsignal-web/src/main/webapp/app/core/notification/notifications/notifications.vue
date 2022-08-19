<template>
  <div class="container-inner">
    <div class="panel-page panel-notification">
      <div class="panel-header">
        <div class="panel-title">
          <!-- <i class="bi bi-arrow-left"></i> -->
          All notifications
        </div>
        <div class="panel-elements">
          <a type="button" class="btn btn-icon-circle" title="Refresh" @click="refreshNotification">
            <i class="icon-common-lg icon-refresh"></i>
          </a>
          <a type="button" class="btn btn-icon-circle" title="Notification settings">
            <i class="icon-common-lg icon-noti-setting"></i>
          </a>
          <!-- <a class="more-link">알림 설정</a> -->
        </div>
      </div>
      <div class="panel-body">
        <div class="top-bar">
          <div class="top-title">
            <div class="desc" v-if="numOfUnread > 0">
              There are <b class="text-danger">{{ numOfUnread }}</b> new notifications.
            </div>
          </div>
          <div class="top-elements">
            <button type="button" class="btn btn-default" @click="deleteReadNotification">Delete read notifications</button>
            <button type="button" class="btn btn-default" @click="deleteAll">Delete all</button>
          </div>
        </div>
        <div class="category-wrap">
          <div class="scroll-area">
            <ul class="nav nav-category">
              <li class="nav-item" v-for="(type, key) of notificationTypes" :key="key">
                <a
                  class="nav-link"
                  :class="[type === notificationTypeCurrent ? 'active' : '']"
                  data-toggle="tab"
                  @click="choseType(type)"
                  >{{ type }}</a
                >
              </li>
            </ul>
          </div>
          <div class="btn-area">
            <button type="button" class="btn btn-category-more"></button>
          </div>
        </div>
        <notification-common @numberNotification="getNumberUnread($event)" ref="notificationCommon"></notification-common>
        <div class="noti-more">
          <p>
            View all notifications received in the last 30 days.<br />Previous notifications can be viewed by clicking the button below.
          </p>
          <button type="button" class="btn btn-more-style1" @click="moreNotification">
            <em>More</em> notification<i class="icon-common icon-arrow-down"></i>
          </button>
        </div>
      </div>
    </div>
  </div>
</template>
<script lang="ts" src="./notifications.component.ts"></script>
