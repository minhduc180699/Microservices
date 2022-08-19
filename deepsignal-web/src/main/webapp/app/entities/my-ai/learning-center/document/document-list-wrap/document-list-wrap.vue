<template>
  <ul class="resource-list type-list">
    <li
      class="list-item"
      v-show="!loading"
      v-for="(file, key) of docLists"
      :key="key"
      :aria-selected="cardSelected.indexOf(key) > -1 ? 'true' : 'false'"
    >
      <div class="resource-card type-document">
        <div class="card-content">
          <div class="info">
            <div class="source-thumb">
              <!-- Sample Images -->
              <img :src="file.document === 'drive' ? 'content/images/icon-google-drive.png' : 'content/images/media-chosun.png'" alt="" />
            </div>
            <span class="source-text"
              ><em>{{ file.document === 'drive' ? 'Google Drive' : 'My PC' }}</em></span
            >
          </div>
          <div class="file-inner">
            <span class="file-icon"><img v-bind:src="file.name | fileExtension" /></span>
            <strong class="title customDiv">{{ file.name }}</strong>
            <small class="size">{{ file.size | fileSize }}</small>
          </div>
        </div>
      </div>
      <div class="btn-area">
        <button type="button" class="btn btn-check" title="선택" @click="selectCard(file, key)" v-if="parent === 'doc-add'">
          <i class="bi bi-check2"></i>
        </button>
        <b-dropdown
          class="dropdown-group"
          no-caret
          variant="link"
          toggle-class="text-decoration-none"
          size="lg"
          v-if="parent === 'doc-list'"
          :title="$t('learningCenter.more')"
        >
          <template #button-content>
            <!--                  <button class="btn btn-more" type="button" data-toggle="dropdown" aria-expanded="false" title="더보기">-->
            <i class="bi bi-three-dots-vertical"></i>
            <!--                  </button>-->
          </template>
          <!--                <div class="dropdown-menu">-->
          <b-dropdown-item class="dropdown-item" @click="downloadFile(file)">
            <i class="bi bi-download"></i> {{ $t('learningCenter.download') }}
          </b-dropdown-item>
          <b-dropdown-item class="dropdown-item" @click="removeFile(file)">
            <i class="bi bi-x-lg"></i> {{ $t('learningCenter.delete') }}
          </b-dropdown-item>
          <!--                </div>-->
        </b-dropdown>
      </div>
    </li>
    <li class="list-item" v-show="loading" v-for="item of items" :key="item.key + 'loading'">
      <div class="resource-card type-document">
        <div class="card-content">
          <div class="info">
            <div class="source-thumb">
              <!-- Sample Images -->
              <b-skeleton type="avatar"></b-skeleton>
            </div>
            <span class="source-text"></span>
          </div>
          <div class="file-inner">
            <span class="file-icon"><b-skeleton type="avatar"></b-skeleton></span>
            <strong class="title"><b-skeleton width="55%"></b-skeleton></strong>
            <small class="size"><b-skeleton width="15%"></b-skeleton></small>
          </div>
        </div>
      </div>
      <div class="btn-area">
        <div class="dropdown-group">
          <button class="btn btn-more" type="button" data-toggle="dropdown" aria-expanded="false" title="더보기">
            <i class="bi bi-three-dots-vertical"></i>
          </button>
          <div class="dropdown-menu">
            <a class="dropdown-item" href="#"><i class="bi bi-download"></i>Download</a>
            <a class="dropdown-item" href="#"><i class="bi bi-x-lg"></i>Delete</a>
          </div>
        </div>
      </div>
    </li>
  </ul>
</template>
<script lang="ts" src="./document-list-wrap.component.ts"></script>
<style scoped>
.customDiv {
  word-break: break-all;
}
</style>
