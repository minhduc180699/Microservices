<template>
  <div class="asset-panel">
    <div>
      <div>
        <span class="total" v-text="$t('map-side-bar.entity-search-tab.result-count', [documentCardItems.length])"></span>
      </div>
    </div>
    <div class="list-scrollable">
      <ul>
        <li v-for="card in documentCardItems" :key="card.id">
          <b-card :style="card.style" v-on:click="onBtnAddPersonalDocumentsToConnectomeClick(card)">
            <template #header>
              <b-card-text>{{ card.author }}</b-card-text>
            </template>
            <b-card-body>
              <b-card-sub-title class="mb-2">{{ card.title }}</b-card-sub-title>
              <b-card-text>
                {{ card.content }}
              </b-card-text>
              <b-card-sub-title>keywords: {{ card.keyword }}</b-card-sub-title>
              <b-card-text class="small text-muted">{{ convertGMTToLocalTime(card.addedAt) }}</b-card-text>
            </b-card-body>
          </b-card>
        </li>
        <li>
          <b-card>
            <b-card-body>
              <b-button variant="primary" @click.prevent="onMore"> More ... </b-button>
            </b-card-body>
          </b-card>
        </li>
      </ul>
    </div>
  </div>
</template>
<script lang="ts" src="./builder-map-side-bar.component.ts"></script>
<style></style>
