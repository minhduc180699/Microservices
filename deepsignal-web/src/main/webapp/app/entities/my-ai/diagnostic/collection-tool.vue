<template>
  <div class="asset-panel">
    <b-container fluid>
      <b-row>
        <b-col>
          <div><b-badge variant="primary">Bookmarks&Group</b-badge></div>
          <div class="list-scrollable">
            <b-card v-for="document in documentOrGroupDocumentsCardItems" :key="document.id" :style="document.style">
              <template v-if="document.isGroup">
                <b-card-body>
                  <b-card-text><b-icon icon="folder-fill"></b-icon> {{ document.title }}</b-card-text>
                  <b-card-text>
                    {{ document.content }}
                  </b-card-text>
                  <b-card-sub-title>modified: {{ document.modifiedAt }}</b-card-sub-title>
                  <b-button v-on:click="onBtnEditCollectionToCurrentCollectionClick(document)" variant="primary"
                    ><b-icon icon="pencil-square"></b-icon
                  ></b-button>
                  <b-button v-on:click="onBtnAddCollectionToCurrentCollectionClick(document)" variant="primary"
                    ><b-icon icon="arrow-right"></b-icon
                  ></b-button> </b-card-body
              ></template>
              <template v-else>
                <b-card-body>
                  <b-card-text><b-icon icon="bookmark-check-fill"></b-icon>{{ document.title }} </b-card-text
                  ><b-button v-on:click="onBtnAddBookmarkToCurrentCollectionClick(document)" variant="primary"
                    ><b-icon icon="arrow-right"></b-icon
                  ></b-button>
                  <b-card-text>
                    {{ document.content }}
                  </b-card-text>
                  <b-card-text>modified: {{ document.modifiedAt }}</b-card-text>
                </b-card-body>
              </template>
            </b-card>
            <b-card>
              <b-card-body>
                <b-button variant="primary" @click.prevent="onMoreBookmark"> More ... </b-button>
              </b-card-body>
            </b-card>
          </div>
        </b-col>

        <b-col>
          <div><b-badge variant="success">Current Collection</b-badge></div>
          <div>
            <label>{{ lblCurrentCollectionId }}</label>
          </div>
          <div>
            <b-button variant="primary" @click.prevent="onCommitCurrentCollection" style="margin-right: 3px">{{ labelSave }}</b-button
            ><b-button variant="secondary" @click.prevent="onResetCurrentCollection">Reset</b-button>
          </div>
          <div class="list-scrollable">
            <b-card v-for="currentBookmark in currentCollectiontCardItems" :key="currentBookmark.id" :style="currentBookmark.style">
              <b-card-body>
                <b-card-sub-title
                  ><b-button v-on:click="onBtnRemoveBookmarkFromCurrentCollectionClick(currentBookmark)" variant="primary"
                    ><b-icon icon="arrow-left"></b-icon></b-button
                  >{{ currentBookmark.title }}</b-card-sub-title
                >
                <b-card-text>
                  {{ currentBookmark.content }}
                </b-card-text>
              </b-card-body>
            </b-card>
          </div>
        </b-col>
        <b-col>
          <div><b-badge variant="info">Current Collection's connectome</b-badge></div>
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
          <div><b-badge variant="light">Current Collection's requests</b-badge></div>
          <div><b-button v-on:click="onSearchRequestList()" variant="primary">load</b-button></div>
          <div>
            <label>{{ requestListCollectionId }}</label>
          </div>
          <div class="list-scrollable">
            <pre>{{ requests }}</pre>
          </div>
        </b-col>
      </b-row>
    </b-container>
  </div>
</template>
<script lang="ts" src="./collection-tool.component.ts"></script>
