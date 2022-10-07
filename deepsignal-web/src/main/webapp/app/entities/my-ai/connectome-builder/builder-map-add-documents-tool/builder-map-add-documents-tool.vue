<template>
  <div class="asset-panel">
    <b-container class="bv-example-row">
      <b-row>
        <b-col cols="8">
          <div>
            <div>
              <b-input-group size="sm" class="mb-2">
                <b-input-group-prepend is-text @click.prevent="searchOnPersonalDocument">
                  <b-icon icon="search"></b-icon>
                </b-input-group-prepend>
                <b-form-input
                  v-model="inputSearchTerms"
                  v-on:input="onSearchInput($event)"
                  type="search"
                  placeholder="Search terms"
                ></b-form-input>
              </b-input-group>
            </div>
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
                v-on:click="onBtnAddPersonalDocumentsToCartClick(card)"
              >
                <template #header>
                  <b-card-text>{{ card.author }}</b-card-text>
                </template>
                <b-card-body>
                  <b-card-sub-title class="mb-2">{{ card.title }}</b-card-sub-title>
                  <b-card-text>
                    {{ card.content }}
                  </b-card-text>
                  <b-card-sub-title>keywords: {{ card.keyword }}</b-card-sub-title>
                </b-card-body>
              </b-card>
              <b-card>
                <b-card-body>
                  <b-button variant="primary" @click.prevent="onMore"> More ... </b-button>
                </b-card-body>
              </b-card>
            </b-card-group>
          </div>
        </b-col>
        <b-col>
          <div>
            <div>
              <span class="total" v-text="'Cart:' + cartDocumentCardItems.length"></span>
            </div>
          </div>
          <div class="list-scrollable" v-on:scroll.passive="onScroll">
            <b-card
              border-variant="primary"
              header="Primary"
              header-bg-variant="primary"
              v-for="cartCard in cartDocumentCardItems"
              :key="cartCard.id"
              :style="cartCard.style"
              v-on:click="onBtnRemovePersonalDocumentsFromCartClick(cartCard)"
            >
              <template #header>
                <b-card-text>{{ cartCard.author }}</b-card-text>
              </template>
              <b-card-body>
                <b-card-sub-title class="mb-2">{{ cartCard.title }}</b-card-sub-title>
                <b-card-text>
                  {{ cartCard.content }}
                </b-card-text>
              </b-card-body>
            </b-card>
            <b-card>
              <b-card-body>
                <b-button variant="primary" @click.prevent="onAddToCurrentContext"> Add to current context</b-button>
              </b-card-body>
            </b-card>
          </div>
        </b-col>
      </b-row>
    </b-container>
  </div>
</template>
<script lang="ts" src="./builder-map-add-documents-tool.component.ts"></script>
<style></style>
