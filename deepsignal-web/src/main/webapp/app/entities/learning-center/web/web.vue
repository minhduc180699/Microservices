<template>
  <div :class="display == 1 ? 'tab-pane fade active show' : 'tab-pane fade'" id="resource-web" aria-labelledby="resource-web-tab">
    <div class="content-top">
      <div class="container">
        <strong class="title"
          ><i class="bi bi-window"></i>Web<small v-text="$t('web.description')">URL을 등록하여 커넴톰을 학습 시켜주세요.</small></strong
        >
        <div class="url-group">
          <input type="url" class="form-control" v-model="searchValue" placeholder="http://" v-on:keydown.enter.prevent="preview" />
          <button class="btn-add" type="button" @click="preview">
            <i class="bi bi-plus-circle-fill"></i>
          </button>
          <!-- <textarea class="form-control" rows="3"></textarea> -->
          <!-- <button class="btn btn-outline-secondary" type="button" id="inputGroupFileAddon04"><i class="bi bi-plus-square-dotted"></i></button> -->
        </div>
      </div>
    </div>
    <div class="container">
      <div class="resource-wrap">
        <div class="resource-top">
          <strong class="title"
            >List to add<small>{{ '(' + numberOfWeb + ')' }}</small></strong
          >
          <div class="top-elements">
            <button
              type="button"
              class="btn btn-outline-lightgray btn-sm btn-toggle btn-allcheck"
              :class="isSelectAll ? 'active' : ''"
              data-toggle="button"
              aria-pressed="true"
              @click="selectAll"
            >
              <i class="bi bi-check2 mr-1" v-text="$t('web.all')"></i><span class="on" v-text="$t('web.select')">select all</span
              ><span class="off" v-text="$t('web.remove')">remove all</span>
            </button>
          </div>
        </div>
        <ul class="resource-list list-row" v-loading="loading">
          <li
            class="list-item"
            :aria-selected="isSelected.indexOf(index) > -1 ? 'true' : 'false'"
            v-for="(previewModel, index) of previewModels"
            :key="index"
          >
            <div class="resource-card type-web" v-if="previewModel.image">
              <div class="card-thumb">
                <img :src="previewModel.image" alt="" />
              </div>
              <div class="card-content">
                <div class="info">
                  <div class="source-thumb" v-if="previewModel.favicon">
                    <img :src="previewModel.favicon" alt="" />
                  </div>
                  <span class="source-url"
                    ><a :href="previewModel.url" target="_blank">{{ previewModel.url }}</a></span
                  >
                </div>
                <a class="title" :href="previewModel.url" target="_blank">{{ previewModel.title }}</a>
                <p class="desc">{{ previewModel.desc }}</p>
              </div>
            </div>
            <div class="resource-card type-web" v-else>
              <div class="card-content">
                <div class="info">
                  <div class="source-thumb" v-if="previewModel.favicon">
                    <!--                    <i v-bind:style="{ 'background-image': 'url(' + previewModel.favicon + ')' }"></i>-->
                    <img :src="previewModel.favicon" alt="" />
                  </div>
                  <span class="source-url"
                    ><a :href="previewModel.url" target="_blank">{{ previewModel.url }}</a></span
                  >
                </div>
                <a class="title" :href="previewModel.url" target="_blank" v-if="previewModel.title">{{ previewModel.title }}</a>
                <p class="desc" v-if="previewModel.desc">{{ previewModel.desc }}</p>
                <p class="desc" v-if="!previewModel.desc">&#160;</p>
              </div>
            </div>
            <button type="button" class="btn btn-check" @click="selectCard(index)"><i class="icon-check-gray"></i></button>
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./web.component.ts"></script>

<style>
.url-group input {
  padding-left: 10px !important;
}
</style>
