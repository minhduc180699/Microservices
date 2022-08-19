<template>
  <div class="ds-card">
    <div class="ds-card-body">
      <router-link to="detail" custom>
        <a>
          <div class="img">
            <img v-if="item.image" src="content/images/@362x290.png" alt="" />
            <div v-swiper class="swiper-container" id="card-img-multi" v-else-if="item.swipers">
              <div class="swiper-wrapper">
                <div class="swiper-slide" v-for="(banner, index) in item.swipers" :key="index">
                  <img :src="banner" alt="" />
                </div>
              </div>
              <div class="swiper-pagination"></div>
            </div>
          </div>
          <div class="source">
            <div class="source-img"><i class="source-twitter"></i></div>
            <div class="source-text">권미진<small>@pigmijinz</small></div>
          </div>
          <div class="desc max-line-4">
            목요일 거래를 시작합니다. Robinhood는 올해 가장 기대되는 기업 공개(IPO) 중 하나로 목요일 시장에 출시될 예정입니다.
          </div>
          <div class="date">2021.06.29 오후 4:10</div>
        </a>
      </router-link>
    </div>
    <ds-card-footer :item="item" />
  </div>
</template>

<script src="./ds-card-social.component.ts" lang="ts"></script>
