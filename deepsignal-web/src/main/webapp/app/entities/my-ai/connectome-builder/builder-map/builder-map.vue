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
              <b-card-body>
                <b-input-group class="mb-2" variant="outline-primary">
                  <b-input-group-prepend is-text>
                    <b-icon icon="folder-fill"></b-icon>
                  </b-input-group-prepend>
                  <b-form-input type="text" :placeholder="collection.type" disabled></b-form-input>
                </b-input-group>
                <b-card-sub-title>{{ collection.title }}</b-card-sub-title>
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
              <b-card-body>
                <b-card-subtitle> <b-icon icon="bookmark-check-fill"></b-icon>{{ bookmark.title }} </b-card-subtitle>
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
            <label>{{ lblCurrentCollectionId }}</label>
          </div>
          <div>
            <b-button variant="primary" @click.prevent="onCommitCurrentCollection" style="margin-right: 3px">{{ labelSave }}</b-button
            ><b-button variant="secondary" @click.prevent="onResetCurrentCollection">Reset</b-button>
          </div>
          <div class="list-scrollable">
            <b-card
              border-variant="primary"
              v-for="currentBookmark in currentCollectiontCardItems"
              :key="currentBookmark.id"
              :style="currentBookmark.style"
              v-on:click="onBtnRemoveBookmarkFromCurrentCollectionClick(currentBookmark)"
            >
              <b-card-body>
                <b-card-sub-title>{{ currentBookmark.title }}</b-card-sub-title>
                <b-card-text>
                  {{ currentBookmark.content }}
                </b-card-text>
              </b-card-body>
            </b-card>
          </div>
        </b-col>
        <b-col>
          <div>
            <label>{{ lblCurrentCollectionId }}</label>
          </div>
          <div class="list-scrollable">
            <ul id="listNodes">
              <li v-for="node in getNodes" :key="node.id">
                <b-icon
                  v-for="(docId, index) in node.relatedDocuments"
                  :key="index"
                  :style="'color:' + getDocumentColors.get(docId)"
                  icon="circle-fill"
                />
                <label>|</label>
                <label>{{ localNodes.indexOf(node) + 1 }}</label>
                <label>|</label>
                <label>{{ node.label }}</label>
                <label>|</label>
                <label>{{ Math.round(node.weight * 1000) / 1000 }}</label>
              </li>
            </ul>
          </div>
        </b-col>
        <b-col>
          <div><b-button v-on:click="onSearchRequestList()" variant="primary">load</b-button></div>
          <div class="list-scrollable">
            <ul id="listNodes">
              <li v-for="request in requestList" :key="request">
                <label>{{ request }}</label>
              </li>
            </ul>
          </div>
        </b-col>
      </b-row>
    </b-container>
  </div>
</template>
<script lang="ts" src="./builder-map.component.ts"></script>
