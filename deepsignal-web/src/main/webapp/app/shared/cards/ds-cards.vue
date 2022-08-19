<template>
  <div>
    <div class="ds-card-list ds-card-list-grid" id="ds-card">
      <div
        class="ds-card-item"
        v-for="item in cardItems"
        :class="item.cssClass"
        :key="item.id ? item.id : item.docId"
        :data-template="item.dataTemplate"
        :data-size="item.dataSize"
        :data-type="item.dataType"
        :style="item.deleted && showHidden != true ? { display: 'none' } : {}"
      >
        <ds-chart v-if="item.dataTemplate === CARD_TYPE.CHART && item.dataSize === CARD_SIZE._2_1" :item="item"></ds-chart>

        <ds-stock-list v-else-if="item.dataTemplate === CARD_TYPE.CHART && item.dataSize === CARD_SIZE._1_1" :item="item"></ds-stock-list>

        <ds-card-weather v-else-if="item.dataTemplate === CARD_TYPE.WEATHER"></ds-card-weather>

        <ds-card-stock v-else-if="item.dataTemplate === CARD_TYPE.STOCK" :item="item"></ds-card-stock>

        <ds-card-signal v-else-if="item.dataTemplate === CARD_TYPE.SIGNAL"></ds-card-signal>

        <ds-card-topten-parent v-else-if="item.dataTemplate === CARD_TYPE.TOP_10" :item="item"></ds-card-topten-parent>

        <ds-card-video v-else-if="item.dataTemplate === CARD_TYPE.YOU_TUBE" :item="item"></ds-card-video>

        <ds-card-survey v-else-if="item.dataTemplate === CARD_TYPE.SURVEY" :item="item"></ds-card-survey>

        <ds-card-map v-else-if="item.dataTemplate === CARD_TYPE.MAP" :item="item"></ds-card-map>

        <ds-card-signal-issue
          v-else-if="item.dataTemplate === CARD_TYPE.CONTENTS_GROUP"
          :item="item"
          :clusterDocuments="item.clusterDocuments"
        ></ds-card-signal-issue>

        <!--        <ds-card-rising-people-->
        <!--          v-else-if="item.dataTemplate === CARD_TYPE.PEOPLE || item.dataTemplate === CARD_TYPE.COMPANY"-->
        <!--          :item="item"-->
        <!--        ></ds-card-rising-people>-->

        <ds-card-social-network-analysis v-else-if="item.dataTemplate === CARD_TYPE.SOCIAL_NETWORK_ANALYSIS" :item="item">
        </ds-card-social-network-analysis>

        <ds-card-social v-else-if="item.dataTemplate === CARD_TYPE.SOCIAL" :item="item"></ds-card-social>

        <ds-card-trend-people v-else-if="item.dataTemplate === CARD_TYPE.PEOPLE" :item="item"></ds-card-trend-people>

        <ds-card-default
          v-else
          :item="item"
          :tab="tab"
          :keyword="keyword"
          v-on:showMore="showMore"
          v-on:handleActivity="handleActivity"
        ></ds-card-default>

        <!--        <button-->
        <!--          type="button"-->
        <!--          aria-label="유용하지 않아요"-->
        <!--          class="btn-close"-->
        <!--          v-if="item.dataTemplate === 'chart' && !item.isCrypto"-->
        <!--          @click="searchStock()"-->
        <!--          style="top: 15px; right: 15px; position: absolute"-->
        <!--        >-->
        <!--          <i class="icon-common-lg icon-fillter" v-if="!isSearchStock" style="width: 20px; height: 20px"></i>-->
        <!--          <i class="bi bi-x-circle" v-else></i>-->
        <!--        </button>-->
        <!--        <button-->
        <!--          type="button"-->
        <!--          aria-label="유용하지 않아요"-->
        <!--          class="btn-close"-->
        <!--          v-else-if="!item.isCrypto && item.dataTemplate !== 'wheather'"-->
        <!--          @click="showNotUseful(item)"-->
        <!--        >-->
        <!--          <i class="icon-x"></i>-->
        <!--        </button>-->
      </div>
    </div>
    <scroll-loader
      v-if="tab === 'feed' || 'people' || 'myai'"
      :loader-method="scrollLoader"
      :loader-disable="loaderDisable"
      :loader-distance="600"
    ></scroll-loader>
  </div>
</template>

<script lang="ts" src="./ds-cards.component.ts"></script>
<style scoped>
.not-useful {
  border-radius: 6px;
}
.not-useful p {
  padding-top: 0.375rem;
}
.ds-card-item .btn-area {
  position: absolute;
  top: 8px;
  right: 8px;
}

.ds-card-item .btn-area .btn-check {
  width: 25px;
  height: 25px;
  font-size: 0;
  padding: 0.25rem;
  border-radius: 50%;
  border-color: #eceef1;
  background: #f9f9f9;
}

.ds-card-item .btn-area .btn-check i {
  color: #bac2cd;
  font-size: 1rem;
  line-height: 1;
}

.btn-check-selected {
  border-color: #516eff !important;
  background-color: #516eff !important;
}

.icon-check-selected {
  color: #fff !important;
}
</style>
