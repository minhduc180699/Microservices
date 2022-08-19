<template>
  <div class="body-content">
    <div class="tab-content">
      <div class="tab-pane fade show active" id="people-01">
        <div class="top-bar">
          <div class="top-title">
            <span class="total">
              {{ $t('learningCenter.total') }}
              <em>{{ pageableCarouselPeople.dfCnt }}</em>
              {{ $t('learningCenter.items') }}
            </span>
          </div>
          <div class="top-elements">
            <div class="elements-item page-control">
              <div class="page-num">
                <b class="current">{{ pageableCarouselPeople.page }}</b
                >/<span class="all">{{ pageableCarouselPeople.totalPages }}</span>
              </div>
              <div class="btn-group">
                <a class="btn btn-prev" aria-label="이전" @click="changePage(false)"><i class="icon-arrow-left"></i></a>
                <a class="btn btn-next" aria-label="다음" @click="changePage(true)"><i class="icon-arrow-right"></i></a>
              </div>
            </div>
          </div>
        </div>
        <div class="ds-card-list ds-card-list-grid-sub">
          <div class="ds-card-item" data-template="people" data-size="1x2">
            <ds-card-trend-people v-if="cardPeople" :item="cardPeople"></ds-card-trend-people>
          </div>
          <div v-show="isShow" class="ds-card-item" data-size="2x2">
            <div class="stage">
              <b-spinner label="Loading..."></b-spinner>
            </div>
          </div>
          <div
            class="ds-card-item"
            v-for="item in pageableCarouselPeople.contents"
            :class="item.cssClass"
            :key="item.id"
            :data-template="item.dataTemplate"
            :data-size="item.dataSize"
            :data-type="item.dataType"
            :style="item.deleted ? { display: 'none' } : {}"
          >
            <ds-card-default :item="item" :tab="'people'" v-on:handleActivity="handleActivity"></ds-card-default>
          </div>
          <div class="no-content ds-card-item" :data-size="dataSizeCs" v-if="goToLearningCenter">
            <i class="bi bi-search"></i>
            <strong v-text="$t('feed.no-content.no-results')">There are no results</strong>
            <p v-text="$t('feed.no-content.go-to-my-ai')">Please go to My AI and training data or try another keyword and filter.</p>
            <b-button
              variant="light"
              style="margin-top: 15px; color: #0097f6"
              @click="learningKeyword"
              v-text="$t('feed.no-content.learn-more')"
              ><b>Learn more</b></b-button
            >
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script lang="ts" src="./ds-carousel-people-feeds.component.ts"></script>
<style>
.snippet {
  position: relative;
  background: #fff;
  padding: 2rem 5%;
  border-radius: 0.25rem;
}
.stage {
  display: flex;
  justify-content: center;
  align-items: center;
  position: relative;
  padding: 6rem 0;
  margin: 0 -5%;
  overflow: hidden;
}
</style>
