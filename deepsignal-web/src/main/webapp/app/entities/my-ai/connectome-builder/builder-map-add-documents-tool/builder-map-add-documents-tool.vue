<template>
  <div class="asset-panel">
    <div>
      <div>
        <span class="total" v-text="'Cart:' + cartDocumentCardItems.length"></span>
      </div>
    </div>
    <div class="list-scrollable" v-on:scroll.passive="onScroll">
      <b-card-group columns>
        <b-card
          v-for="card in cartDocumentCardItems"
          :key="card.id"
          :style="card.style"
          @mouseover="onMouseOverDocumentCard"
          v-on:click="onBtnRemovePersonalDocumentsFromCartClick(card)"
        >
          <template #header>
            <b-card-text>{{ card.author }}</b-card-text>
          </template>
          <b-card-body>
            <b-card-sub-title class="mb-2">{{ card.title }}</b-card-sub-title>
          </b-card-body>
        </b-card>
        <b-card>
          <b-card-body>
            <b-button variant="primary" @click.prevent="onAddToCurrentContext"> Add to current context</b-button>
          </b-card-body>
        </b-card>
      </b-card-group>
    </div>
    <div>
      <div>
        <span class="total" v-text="'Asset:' + documentCardItems.length"></span>
      </div>
    </div>
    <div class="list-scrollable" v-on:scroll.passive="onScroll">
      <b-card-group columns>
        <b-card
          v-for="card in documentCardItems"
          :key="card.id"
          :style="card.style"
          @mouseover="onMouseOverDocumentCard"
          v-on:click="onBtnAddPersonalDocumentsToCartClick(card)"
        >
          <template #header>
            <b-card-text>{{ card.author }}</b-card-text>
          </template>
          <b-card-body>
            <b-card-sub-title class="mb-2">{{ card.title }}</b-card-sub-title>
          </b-card-body>
        </b-card>
        <b-card>
          <b-card-body>
            <b-button variant="primary" @click.prevent="onMore"> More ... </b-button>
          </b-card-body>
        </b-card>
      </b-card-group>
    </div>
  </div>
</template>
<script lang="ts" src="./builder-map-add-documents-tool.component.ts"></script>
<style></style>
