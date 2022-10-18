<template>
  <div class="asset-panel">
    <b-container class="bv-example-row">
      <b-row>
        <b-col>
          <div class="list-scrollable">
            <b-card
              v-for="collection in collectionCardItems"
              :key="collection.id"
              v-on:click="onBtnAddCollectionToCurrentCollectionClick(collection)"
            >
              <template #header>
                <b-card-text>{{ collection.author }}</b-card-text>
              </template>
              <b-card-body>
                <b-card-sub-title class="mb-2">{{ collection.title }}</b-card-sub-title>
                <b-card-text>
                  {{ collection.content }}
                </b-card-text>
              </b-card-body>
            </b-card>
            <b-card
              v-for="bookmark in bookmarkCardItems"
              :key="bookmark.id"
              :style="bookmark.style"
              v-on:click="onBtnAddBookmarkToCurrentCollectionClick(bookmark)"
            >
              <template #header>
                <b-card-text>{{ bookmark.author }}</b-card-text>
              </template>
              <b-card-body>
                <b-card-sub-title class="mb-2">{{ bookmark.title }}</b-card-sub-title>
                <b-card-text>
                  {{ bookmark.content }}
                </b-card-text>
              </b-card-body>
            </b-card>
            <b-card>
              <b-card-body>
                <b-button variant="primary" @click.prevent="onMoreBookmark"> More ... </b-button>
              </b-card-body>
            </b-card>
          </div>
        </b-col>
        <b-col>
          <b-button variant="primary" @click.prevent="onCommitCurrentCollection"> Commit ... </b-button>
          <div class="list-scrollable">
            <b-card
              border-variant="primary"
              header="Primary"
              header-bg-variant="primary"
              v-for="currentBookmark in currentCollectiontCardItems"
              :key="currentBookmark.id"
              :style="currentBookmark.style"
              v-on:click="onBtnRemoveBookmarkFromCurrentCollectionClick(currentBookmark)"
            >
              <template #header>
                <b-card-text>{{ currentBookmark.author }}</b-card-text>
              </template>
              <b-card-body>
                <b-card-sub-title class="mb-2">{{ currentBookmark.title }}</b-card-sub-title>
                <b-card-text>
                  {{ currentBookmark.content }}
                </b-card-text>
              </b-card-body>
            </b-card>
          </div>
        </b-col>
        <b-col> </b-col>
        <b-col>
          <div class="list-scrollable">
            <!-- <b-card
              border-variant="primary"
              header="Primary"
              header-bg-variant="primary"
              v-for="discovery in discoveryCardItems"
              :key="discovery.id"
              :style="discovery.style"
            >
              <template #header>
                <b-card-text>{{ discovery.author }}</b-card-text>
              </template>
              <b-card-body>
                <b-card-sub-title class="mb-2">{{ discovery.title }}</b-card-sub-title>
                <b-card-text>
                  {{ discovery.content }}
                </b-card-text>
              </b-card-body>
            </b-card> -->
          </div>
        </b-col>
      </b-row>
    </b-container>
  </div>
</template>
<script lang="ts" src="./builder-map.component.ts"></script>
