<template>
  <div class="list-wrap overflow-y-scroll">
    <vue-custom-scrollbar class="customScrollNotification" :settings="scrollSettings" v-if="hasScroll" @ps-scroll-y="scrollHandle($event)">
      <ul class="noti-list">
        <li
          class="noti-item"
          v-for="(item, index) of notifications"
          :key="index"
          :class="{ new: !item.isChecked, visited: item.isMarkedRead }"
        >
          <div class="noti-thumb" @click="markReadNotification(item)">
            <div v-html="item.iconContent"></div>
            <!--          <span class="blind">feed</span>-->
          </div>
          <div class="noti-info" @click="markReadNotification(item)">
            <div class="top-area mr-4">
              <strong class="title" v-if="!item.titleI18n">{{ item.title }}</strong>
              <strong class="title" v-if="item.titleI18n">{{ titleNotification(item) }}</strong>
              <span class="datetime">{{ item.createdDate | dateAgo }}</span>
            </div>
            <p class="desc" v-if="!item.contentI18n" v-html="item.content"></p>
            <p class="desc" v-if="item.contentI18n" v-html="contentNotification(item)"></p>
            <div class="btn-area" v-html="item.btnContent"></div>
          </div>
          <button type="button" class="btn btn-icon btn-del" title="삭제" @click="deleteNotification(item)">
            <i class="icon-common icon-close"></i>
          </button>
        </li>
      </ul>
      <ul class="noti-list" v-if="loading">
        <li class="noti-item new" v-for="i of 5" :key="i">
          <div class="noti-thumb">
            <div><b-skeleton type="avatar"></b-skeleton></div>
          </div>
          <div class="noti-info">
            <div class="top-area">
              <strong class="title mnw-40"><b-skeleton></b-skeleton></strong>
            </div>
            <p class="desc w-100"><b-skeleton animation="wave"></b-skeleton></p>
          </div>
        </li>
      </ul>
    </vue-custom-scrollbar>
    <ul class="noti-list" v-if="!hasScroll">
      <li
        class="noti-item"
        v-for="(item, index) of notifications"
        :key="index"
        :class="{ new: !item.isChecked, visited: item.isMarkedRead }"
      >
        <div class="noti-thumb" @click="markReadNotification(item)">
          <div v-html="item.iconContent"></div>
          <!--          <span class="blind">feed</span>-->
        </div>
        <div class="noti-info" @click="markReadNotification(item)">
          <div class="top-area">
            <strong class="title" v-if="!item.titleI18n">{{ item.title }}</strong>
            <strong class="title" v-if="item.titleI18n">{{ titleNotification(item) }}</strong>
            <span class="datetime">{{ item.createdDate | dateAgo }}</span>
          </div>
          <p class="desc" v-if="!item.contentI18n" v-html="item.content"></p>
          <p class="desc" v-if="item.contentI18n" v-html="contentNotification(item)"></p>
          <div class="btn-area" v-html="item.btnContent"></div>
        </div>
        <button type="button" class="btn btn-icon btn-del" title="삭제" @click="deleteNotification(item)">
          <i class="icon-common icon-close"></i>
        </button>
      </li>
    </ul>
    <ul class="noti-list" v-if="loading && !hasScroll">
      <li class="noti-item new" v-for="i of 10" :key="i">
        <div class="noti-thumb">
          <div><b-skeleton type="avatar"></b-skeleton></div>
        </div>
        <div class="noti-info">
          <div class="top-area">
            <strong class="title mnw-40"><b-skeleton></b-skeleton></strong>
          </div>
          <p class="desc w-100"><b-skeleton animation="wave"></b-skeleton></p>
        </div>
      </li>
    </ul>
  </div>
</template>
<script src="./notification-common.component.ts" lang="ts"></script>
<style scoped>
.customScrollNotification {
  max-height: 400px;
}
</style>
