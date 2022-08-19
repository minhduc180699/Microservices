<template>
  <div class="tab-pane show active" id="lc-archive">
    <div class="lc-content-top">
      <div class="title">
        <h1>{{ $t('learningCenter.title') }}</h1>
        <h2><i class="bi bi-person-check"></i>{{ $t('learningCenter.learnedDocument') }}</h2>
      </div>
    </div>
    <div class="lc-content-body">
      <div class="top-bar">
        <div class="top-title">
          <span class="total"
            >{{ $t('learningCenter.total') }}<em>{{ totalFeeds }}</em
            >{{ $t('learningCenter.items') }}</span
          >
          <span v-if="entityLabelSelected" class="keyword"
            ><i class="bi bi-search"></i>{{ entityLabelSelected
            }}<button @click="closeHashTag()" type="button" class="close" style="padding: 0 6px" aria-label="Close">
              <i class="bi bi-x"></i></button
          ></span>
        </div>
        <div class="top-elements"></div>
      </div>
      <ds-cards
        :showHidden="true"
        :cardItems="cardItems"
        :tab="'feed'"
        :page="'activity'"
        :loaderDisable="loaderDisable"
        @scrollLoader="scrollLoader()"
        v-on:handleActivity="handleActivity"
      ></ds-cards>
    </div>
  </div>
</template>

<script lang="ts" src="./learned-document.component.ts"></script>
