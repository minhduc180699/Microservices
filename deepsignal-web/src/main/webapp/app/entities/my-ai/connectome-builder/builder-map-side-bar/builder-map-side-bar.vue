<template>
  <div class="asset-panel">
    <div style="height: 40%; padding-bottom: 10px">
      <div>
        <span class="total" v-text="'collections:' + collectionCardItems.length"></span>
      </div>
      <div class="list-scrollable">
        <ul id="listCollections">
          <li v-for="card in collectionCardItems" :key="card.id">
            <b-card :style="card.style" v-on:click="onOpenCollectionClick(card)">
              <template #header>
                <b-card-text>{{ card.Type }}</b-card-text>
              </template>
              <b-card-body>
                <b-card-sub-title class="mb-2">{{ card.title }}</b-card-sub-title>
                <b-card-text>
                  {{ card.content }}
                </b-card-text>
                <b-card-sub-title>keywords: {{ card.keyword }}</b-card-sub-title>
              </b-card-body>
            </b-card>
          </li>
        </ul>
      </div>
    </div>
    <div style="height: 60%">
      <div>
        <span class="total" v-text="'context:' + labelcontext"></span>
      </div>
      <div>
        <span class="total" v-text="'documents:' + documentCardItems.length"></span>
      </div>
      <div class="list-scrollable">
        <ul id="listDocuments">
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
        </ul>
      </div>
    </div>
  </div>
</template>
<script lang="ts" src="./builder-map-side-bar.component.ts"></script>
<style></style>
