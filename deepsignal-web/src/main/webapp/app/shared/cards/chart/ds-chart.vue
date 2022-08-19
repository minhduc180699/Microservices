<template>
  <div class="ds-card ds-card-info" style="position: relative">
    <div class="ds-card-header">
      <div class="ds-card-title">Stocks</div>
      <div class="ds-card-elements">
        <b-dropdown size="lg" variant="link" toggle-class="text-decoration-none" no-caret right v-if="!isSearchStock && isFeed">
          <template #button-content>
            <button type="button" class="btn btn-icon"><i class="icon-common icon-more"></i></button>
          </template>
          <b-dropdown-item @click.prevent="searchStock(true)">
            <i class="icon-common-sm icon-add"></i>{{ $t('feed.weather.search-add') }}
          </b-dropdown-item>
          <b-dropdown-item @click="hideStockCard">
            <i class="icon-common-sm icon-visibility"></i>{{ $t('feed.weather.hide-stock') }}</b-dropdown-item
          >
          <b-dropdown-item @click="openPopUpUserSetting()">
            <i class="icon-common-sm icon-setting"></i>{{ $t('feed.weather.detail-setting') }}</b-dropdown-item
          >
          <!--          <b-dropdown-item> <i class="icon-common-sm icon-problem"></i>{{ $t('feed.weather.report-problem') }}</b-dropdown-item>-->
        </b-dropdown>
        <button type="button" class="btn" style="font-size: 14px" v-if="isSearchStock" @click="searchStock(false)">
          <i class="bi bi-x-circle"></i>
        </button>
      </div>
    </div>
    <div class="ds-card-body" style="padding-top: 0" v-if="!isShowSpinner">
      <div class="multi-content" v-if="!isSearchStock">
        <div
          class="swiper mySwiper"
          v-swiper:mySwiperChart="swiperOptions"
          @slideChange="onSlideChange"
          ref="mySwiperChart"
          style="margin-top: -6px"
        >
          <div class="swiper-wrapper">
            <div class="swiper-slide" v-for="(stockCode, index) in item.stockCodes" :key="index">
              <div class="top-area">
                <strong class="title" v-if="title">{{ title }}</strong>
                <strong class="title" v-if="!title">&nbsp;</strong>
                <div class="rank-changes rank-up" v-if="false">
                  <span class="change-icon"></span>
                  <span class="change-num">240,300</span>
                </div>
              </div>
              <ds-stock-chart
                :item="item"
                :stockCode="stockCode"
                v-if="item.stockCodes[index].dataChart"
                :dataZoom="false"
              ></ds-stock-chart>
            </div>
          </div>
          <div class="swiper-button-next swiper-next-stock-chart"></div>
          <div class="swiper-button-prev swiper-prev-stock-chart"></div>
          <div class="swiper-pagination swiper-stock-chart" v-if="isFeed"></div>
        </div>
      </div>
      <div v-else>
        <b-form-input
          placeholder="Search stocks ..."
          v-model="keySearch"
          @input="searching"
          @keyup.esc="changeIsSearchStock"
          style="padding-top: 8px"
        ></b-form-input>
        <div style="padding-top: 10px" id="result-search">
          <div class="layer-body">
            <ul class="search-keyword-list history">
              <li
                v-for="(item, index) in resultSearch"
                :key="index"
                @click="choseStock(item)"
                id="stock-detail"
                style="cursor: context-menu"
              >
                <span :class="'fi fi-' + item.marketCountry.toLowerCase()" style="width: 7.5%"></span>
                <p style="width: 20%" class="keyword">{{ item.symbolCode }}</p>
                <p style="width: 50%" class="keyword">{{ item.symbolNameEn }}</p>
                <p style="width: 22.5%" class="keyword">{{ item.marketName }}</p>
              </li>
            </ul>
            <div class="no-content">
              <strong></strong>
            </div>
          </div>
        </div>
      </div>
    </div>
    <b-spinner v-if="isShowSpinner" style="position: absolute; top: 50%; left: 50%" small label="Small Spinner"></b-spinner>
  </div>
</template>
<script lang="ts" src="./ds-chart.component.ts"></script>
<style>
#search-input button {
  position: absolute;
  top: 5px;
  right: 10px;
}
#result-search div {
  max-height: 145px;
  overflow-y: scroll;
}
.fi {
  background-position: 0 !important;
}
.search-keyword-list li {
  padding: 5px 5px !important;
}

.dropdown-item {
  font-weight: 400;
}
</style>
