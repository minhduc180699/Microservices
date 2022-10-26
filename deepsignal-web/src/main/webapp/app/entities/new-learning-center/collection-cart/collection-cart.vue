<template>
  <div class="lc-sidebar lc-collection-cart" :class="isCartActive ? 'active' : null">
    <div class="panel-collection panel-collection-cart">
      <div class="panel-header">
        <div class="elements-left">
          <div class="panel-title">
            <i class="go-icon-common icon-storage"></i>
            <strong class="title">Collection</strong>
          </div>
        </div>
        <div class="elements-right">
          <button
            type="button"
            class="btn btn-close"
            id="btn-collection-cart-close"
            data-toggle="dropdown"
            aria-expanded="false"
            @click.prevent="isCartActive = !isCartActive"
          >
            <i class="icon-common icon-close"></i>
          </button>
        </div>
      </div>
      <div class="panel-body">
        <!--
        // Shows only a list of the types in the storage
        <div class="storage-filter">
          <div class="filter-main">
            <ul class="filter-list">
              <li class="filter-item">
                <a class="filter-link active" href="#">All</a>
              </li>
              <li class="filter-item">
                <a class="filter-link" href="#">Previous</a>
              </li>
              <li class="filter-item">
                <a class="filter-link" href="#">Web Search</a>
              </li>
              <li class="filter-item">
                <a class="filter-link" href="#">Text</a>
              </li>
              <li class="filter-item">
                <a class="filter-link" href="#">URL</a>
              </li>
              <li class="filter-item">
                <a class="filter-link" href="#">Documents</a>
              </li>
            </ul>
          </div>
          // filter-sub :: Use only for 'web search'
          <div class="filter-sub">
            <ul class="filter-list">
              <li class="filter-item">
                <a class="filter-link active" href="#"><i class="icon-common icon-search"></i>Keyword</a>
              </li>
              <li class="filter-item">
                <a class="filter-link" href="#"><i class="icon-common icon-search"></i>키워드</a>
              </li>
            </ul>
          </div>
        </div> -->
        <div class="lc-list-top">
          <div class="elements-left">
            <div class="check-group">
              <button type="button" class="btn btn-check" id="list-check-all" data-toggle="button" aria-pressed="false"></button>
              <label for="list-check-all">전체 선택</label>
            </div>
            <div class="list-info">(<strong>0</strong>/20)</div>
          </div>
          <div class="elements-right">
            <button type="button" class="btn btn-default btn-sm"><i class="icon-common icon-close"></i>선택 삭제</button>
          </div>
        </div>
        <div class="lc-list-wrap">
          <div class="list-inner overflow-y-scroll">
            <vue-custom-scrollbar class="overflow-yx-scroll csScrollPositon h-100" :settings="scrollSettings">
              <ul class="lc-card-list">
                <li
                  class="lc-card-item"
                  v-for="item in currentCollection.documentIdList"
                  :aria-selected="currentCollection.documentIdList.indexOf(item.id) > -1 ? 'true' : 'false'"
                  :key="item.id"
                >
                  <single-card :document="getDocDetail(item)"></single-card>
                </li>
              </ul>
            </vue-custom-scrollbar>
          </div>
        </div>
      </div>
      <div class="panel-footer">
        <button type="button" class="btn btn-primary btn-block" @click="saveCollection">Save Collection</button>
      </div>
    </div>
    <button
      type="button"
      class="btn btn-toggle btn-sidebar-toggle btn-sidebar-collection-open"
      :class="isCartActive ? '' : 'active'"
      id="btn-sidebar-collection-open"
      @click.prevent="isCartActive = !isCartActive"
    >
      {{ isCartActive ? '접기' : '펼치기' }}
    </button>
  </div>
</template>

<script lang="ts" src="./collection-cart.component.ts"></script>
