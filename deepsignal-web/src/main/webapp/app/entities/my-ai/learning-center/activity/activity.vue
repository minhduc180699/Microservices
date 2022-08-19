<template>
  <div class="tab-pane show active" id="lc-archive">
    <div class="lc-content-top">
      <div class="title">
        <h1>{{ $t('learningCenter.title') }}</h1>
        <h2><i class="bi bi-person-check"></i>{{ $t('learningCenter.myActivity') }}</h2>
      </div>
      <ul class="nav">
        <li class="nav-item" v-for="(activity, index) in activities" :key="index" @click="changeLink(activity)">
          <a :class="['nav-link', { active: activity.active }]" style="pointer-events: none"
            ><i :class="activity.icon + ' pr-1'"></i>{{ $t('learningCenter.' + activity.name) }}</a
          >
        </li>
      </ul>
    </div>
    <div class="lc-content-body">
      <div class="top-bar">
        <div class="top-title">
          <strong><i :class="currentActivity.icon"></i>&nbsp;{{ $t('learningCenter.' + currentActivity.name) }}</strong>
          <span class="total"
            >{{ $t('learningCenter.total') }}<em>{{ totalItems }}</em
            >{{ $t('learningCenter.items') }}</span
          >
        </div>
        <div class="top-elements"></div>
      </div>
      <ds-cards
        :showHidden="showHidden"
        :cardItems="cardItems"
        :tab="'feed'"
        :page="'activity'"
        :loaderDisable="loaderDisable"
        @scrollLoader="scrollLoader"
        v-on:handleActivity="handleActivity"
      ></ds-cards>
    </div>
  </div>
</template>

<script lang="ts" src="./activity.component.ts"></script>
