<template>
  <div class="panel panel-content">
    <div class="panel-header" v-if="dataReceived && dataReceived.length > 0">
      <div class="panel-title">
        {{ $t('signal-tracking.related-contents') }}<small>({{ dataReceived.length }})</small>
      </div>
    </div>
    <div class="panel-body">
      <div class="swiper-wrap" v-if="dataReceived && dataReceived.length > 0">
        <div class="swiper swiper-content-related swiper-content-related-article" v-swiper:mySwiper="swiperOptions">
          <div class="swiper-wrapper">
            <swiper-slide v-for="(item, index) in dataShow" :key="index" class="ds-media-list">
              <div class="media" v-for="(value, ind) in item" :key="'media' + ind">
                <div class="media-body">
                  <div class="text-wrap">
                    <a class="title" :href="value.link" target="_blank">{{ value.title }}</a>
                    <span class="info">{{ value.author }}</span>
                  </div>
                </div>
                <a v-if="value.img" class="media-thumb">
                  <img :src="value.img" alt="이경일" @error="value.img = '/content/images/empty-image.png'" />
                </a>
              </div>
            </swiper-slide>
          </div>
          <div class="swiper-bar">
            <div class="swiper-button-prev" :class="dataSwiper.navigationPrev"></div>
            <div class="swiper-pagination" :class="dataSwiper.paginationClass"></div>
            <div class="swiper-button-next" :class="dataSwiper.navigationNext"></div>
          </div>
        </div>
      </div>
      <div class="swiper-wrap" v-if="loading">
        <swiper class="swiper swiper-content-related swiper-content-related-article" v-if="loading">
          <div class="swiper-wrapper">
            <swiper-slide class="ds-media-list">
              <div class="media" v-for="index in 6" :key="'skeleton' + index">
                <div class="media-body">
                  <div class="text-wrap">
                    <a class="title" href="#">
                      <b-skeleton width="85%"></b-skeleton>
                    </a>
                    <span class="info"><b-skeleton width="25%"></b-skeleton></span>
                  </div>
                </div>
                <a class="media-thumb">
                  <b-skeleton-img></b-skeleton-img>
                </a>
              </div>
            </swiper-slide>
          </div>
        </swiper>
      </div>
    </div>
  </div>
</template>

<script src="./detail-related.component.ts" lang="ts"></script>

<style>
.swiper-bar-detail div {
  display: inline-block;
  justify-content: center;
  align-items: center;
}
</style>
