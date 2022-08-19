<template>
  <div class="panel panel-content">
    <div class="panel-header">
      <div class="panel-title">
        {{ $t('signal-tracking.people-company') }}<small>({{ dataRelated.length }})</small>
      </div>
    </div>
    <div class="panel-body" style="margin-top: 15px">
      <div class="swiper-wrap">
        <div v-swiper:mySwiperPeople="swiperOptions" class="swiper swiper-content-related swiper-content-related-people">
          <div class="swiper-wrapper">
            <div class="swiper-slide" v-for="(item, index) in dataRelated" :key="'pc' + index">
              <ds-card-people-rising :item="item"></ds-card-people-rising>
            </div>
          </div>
          <div class="swiper-bar">
            <div class="swiper-button-prev" :class="dataSwiper.navigationPrev"></div>
            <div :class="dataSwiper.paginationClass" style="text-align: center"></div>
            <div class="swiper-button-next" :class="dataSwiper.navigationNext"></div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./detail-related-people-company.component.ts"></script>
