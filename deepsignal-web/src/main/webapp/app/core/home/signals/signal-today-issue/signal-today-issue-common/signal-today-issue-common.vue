<template>
  <fragment>
    <div class="ds-media-headline" v-show="!isLoadingSignal">
      <div class="media-body">
        <router-link
          :to="{ name: 'Detail', params: { connectomeId: firstClusterDocument.connectomeId, feedId: firstClusterDocument.objId } }"
        >
          <a class="title">{{ firstClusterDocument.title }}</a>
        </router-link>
        <div class="desc" :title="firstClusterDocument.content">
          {{ firstClusterDocument.content | lmTo(1000) }}
        </div>
        <!-- <a class="more-link" href="#">요약 전체 보기</a> -->
      </div>
      <a
        class="img-thumb"
        :class="validImageLinks ? '' : 'd-none'"
        v-if="
          firstClusterDocument.imageLinks &&
          firstClusterDocument.imageLinks.length > 0 &&
          firstClusterDocument.imageLinks[0] &&
          firstClusterDocument.imageLinks[0].length > 0
        "
      >
        <img :src="firstClusterDocument.imageLinks[0]" @error="validImageLinks = false" alt="" />
      </a>
    </div>
    <div class="ds-media-headline" v-show="isLoadingSignal">
      <div class="media-body">
        <b-skeleton></b-skeleton>
        <b-skeleton></b-skeleton>
        <b-skeleton></b-skeleton>
      </div>
      <a class="img-thumb">
        <b-skeleton-img></b-skeleton-img>
      </a>
    </div>
    <div class="top-bar">
      <div class="top-title">
        <strong class="title">
          <span class="num-box">{{ pageableClusterDocument.totalElements || 0 }}</span
          >{{ $t('signal-tracking.related-contents') }}
        </strong>
        <!--                    <a class="more-link" href="#">View all</a>-->
      </div>
      <div v-if="hasPageable" class="top-elements">
        <div class="elements-item page-control" v-if="pageableClusterDocument.totalPages > 0">
          <div class="page-num">
            <b class="current">{{ pageableClusterDocument.page }}</b
            >/<span class="all">{{ pageableClusterDocument.totalPages }}</span>
          </div>
          <div class="btn-group">
            <a class="btn btn-prev" aria-label="이전" @click="changePageClusterDocuments(false)"><i class="icon-arrow-left"></i></a>
            <a class="btn btn-next" aria-label="다음" @click="changePageClusterDocuments(true)"><i class="icon-arrow-right"></i></a>
          </div>
        </div>
      </div>
    </div>
    <ul class="ds-media-line-list" v-show="isLoadingSignal">
      <li class="list-item" v-for="i in size" :key="i">
        <div class="item-inner">
          <a class="title"><b-skeleton width="400px"></b-skeleton></a>
        </div>
      </li>
    </ul>
    <ul class="ds-media-line-list" v-show="!isLoadingSignal">
      <li class="list-item" v-for="(clusterDocument, index2) of pageableClusterDocument.contents" :key="index2">
        <div class="item-inner">
          <span class="info">{{ clusterDocument.writerName }}</span>
          <router-link :to="{ name: 'Detail', params: { connectomeId: clusterDocument.connectomeId, feedId: clusterDocument.objId } }">
            <div>
              <a class="title">{{ clusterDocument.title }}</a>
            </div>
          </router-link>
        </div>
      </li>
    </ul>
  </fragment>
</template>
<script lang="ts" src="./signal-today-issue-common.component.ts"></script>
