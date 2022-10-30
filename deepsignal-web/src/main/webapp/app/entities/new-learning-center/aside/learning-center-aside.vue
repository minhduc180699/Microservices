<template>
  <div class="lc-sidebar">
    <div class="lc-panel">
      <!-- panel-main -->
      <div class="lc-panel-main">
        <div class="lc-main-inner">
          <div class="panel-body">
            <vue-custom-scrollbar class="overflow-yx-scroll csScrollPositon h-100" :settings="scrollSettings">
              <ul class="lc-card-list lc-context-list" v-for="item in collections" :key="item.collectionId">
                <group-card v-if="item.documentIdList.length > 1" :item="item" @showListGroupCard="showListGroupCard"></group-card>
                <!--                <single-card :documentId="item.documentIdList" v-if="item.documentIdList.length == 1"></single-card>-->
              </ul>
            </vue-custom-scrollbar>
            <div class="list-add">
              <button type="button" class="btn btn-add">+ Add Learning Source</button>
            </div>
          </div>
        </div>
        <div class="panel-footer">
          <div class="list-count">
            <span class="all">전체 <em>20</em>개</span>
            <span class="select">선택 <em>10</em>개</span>
          </div>
        </div>
      </div>
      <list-card :showList="showList" @backToList="backToList" :listData="listData"></list-card>
    </div>
    <div v-show="loading" style="position: absolute; top: 50%; left: 50%">
      <b-spinner variant="secondary"></b-spinner>
    </div>
  </div>
</template>

<script lang="ts" src="./learning-center-aside.component.ts"></script>
