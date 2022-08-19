<template>
  <div>
    <div class="container p-t-60" v-if="keyword != undefined && keyword != ''">
      <div class="title-area">
        <div class="row align-items-center justify-content-between">
          <div class="col-auto">
            <div class="tit">
              <strong>'{{ keyword }}'</strong>
              <span>Web Search Result</span>
              <a
                href="javascript:;"
                data-toggle="tooltip"
                data-placement="bottom"
                data-html="true"
                title="These are web search results that require further learning. <br>Please select the content you need for learning"
                ><i class="icon-info"></i
              ></a>
            </div>
          </div>
          <div class="col-auto">
            <div class="folding-toggler">
              <a href="javascript:;"><span>Put aside</span><i class="icon-folding"></i></a>
            </div>
          </div>
        </div>
      </div>
      <div class="search-item-info">
        <div class="row row-0 align-items-center justify-content-end">
          <div class="col-auto">
            <div class="count">
              Search Results (<i>{{ connectomes.length }}</i
              >)
            </div>
          </div>
        </div>
      </div>
      <div class="item-list only-1x1" id="grid-item">
        <div class="item" data-template="news" data-size="1x1" data-type="01" v-for="(item, index) in connectomes" :key="index">
          <div class="card" :class="item.image ? 'has-img' : ''">
            <div class="img-full" v-if="item.image">
              <!-- 풀이미지일 경우 -->
              <img :src="item.image" alt="" />
            </div>
            <div class="card-header">
              <div class="media">
                <div class="logo mxw-24 mxh-24"><img v-bind:src="item.favicon" /></div>
                <div class="name">{{ item.name }}</div>
              </div>
            </div>
            <div class="card-body">
              <div class="title max-line-4">{{ item.title }}</div>
              <div class="desc max-line-2">
                {{ item.desc }}
              </div>
              <div class="date">{{ item.date }}</div>
            </div>
            <div class="card-dim">
              <button
                type="button"
                aria-label="선택"
                class="btn-select"
                :class="item.checked ? 'item-checked' : ''"
                @click="onSelectItem(item)"
              >
                <i class="icon-check-dark"></i>
              </button>
              <button
                v-b-modal.meta-search-detail
                @click="updateDetail(item)"
                type="button"
                aria-label="보기"
                class="btn-detail"
                data-toggle="modal"
                data-target="#modalContentsDetail"
              >
                <i class="icon-search-dark"></i>
              </button>
            </div>
          </div>
        </div>
        <meta-search-detail :metaSearchItem="metaSearchItem"></meta-search-detail>
      </div>
    </div>

    <meta-search-selected :itemsSelected="itemsSelected" @removeItem="remove" :hasLearning="true"></meta-search-selected>
  </div>
</template>

<script lang="ts" src="./meta-search.component.ts"></script>
<style>
.item-checked {
  background: #516eff !important;
}
.has-img .card-header,
.has-img .card-body {
  background: rgba(0, 0, 0, 0.3) !important;
}
</style>
