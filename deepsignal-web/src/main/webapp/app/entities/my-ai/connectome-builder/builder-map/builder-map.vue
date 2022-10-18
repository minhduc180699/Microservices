<template>
  <div class="asset-panel">
    <b-container class="bv-example-row">
      <b-row>
        <b-col>
          <b-badge variant="primary">Collections</b-badge>
        </b-col>
        <b-col>
          <b-badge variant="primary">Bookmarks</b-badge>
        </b-col>
        <b-col>
          <b-badge variant="success">Current Collection</b-badge>
        </b-col>
        <b-col>
          <b-badge variant="info">Current Collection's Connectome</b-badge>
        </b-col>
        <b-col>
          <b-badge variant="light">Current Collection's requests</b-badge>
        </b-col>
      </b-row>
      <b-row>
        <b-col>
          <div class="list-scrollable">
            <b-card v-for="collection in collectionCardItems" :key="collection.id">
              <template #header>
                <b-input-group class="mb-2" variant="outline-primary">
                  <b-input-group-prepend is-text>
                    <b-icon icon="folder-fill"></b-icon>
                  </b-input-group-prepend>
                  <b-form-input type="text" :placeholder="collection.type" disabled></b-form-input>
                </b-input-group>
              </template>
              <b-card-body>
                <b-card-sub-title class="mb-2">{{ collection.title }}</b-card-sub-title>
                <b-card-text>
                  {{ collection.content }}
                </b-card-text>
                <b-card-sub-title>modified: {{ collection.modifiedAt }}</b-card-sub-title>
                <b-button v-on:click="onBtnAddCollectionToCurrentCollectionClick(collection)" variant="primary">Add</b-button>
                <b-button v-on:click="onBtnEditCollectionToCurrentCollectionClick(collection)" variant="primary">Edit</b-button>
              </b-card-body>
            </b-card>
          </div>
        </b-col>
        <b-col>
          <div class="list-scrollable">
            <b-card
              v-for="bookmark in bookmarkCardItems"
              :key="bookmark.id"
              :style="bookmark.style"
              v-on:click="onBtnAddBookmarkToCurrentCollectionClick(bookmark)"
            >
              <template #header>
                <b-input-group class="mb-2" variant="outline-primary">
                  <b-input-group-prepend is-text>
                    <b-icon icon="bookmark-check-fill"></b-icon>
                  </b-input-group-prepend>
                  <b-form-input type="text" :placeholder="bookmark.author" disabled></b-form-input>
                </b-input-group>
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
          <div>
            <b-button variant="primary" @click.prevent="onCommitCurrentCollection" style="margin-right: 3px">{{ labelSave }}</b-button
            ><b-button variant="secondary" @click.prevent="onResetCurrentCollection">Reset</b-button>
          </div>
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
            <b-card
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
            </b-card>
          </div>
        </b-col>
      </b-row>
    </b-container>
  </div>
</template>
<script lang="ts" src="./builder-map.component.ts"></script>
