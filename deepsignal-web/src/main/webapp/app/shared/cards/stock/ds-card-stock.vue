<template>
  <div class="ds-card">
    <div class="ds-card-header">
      <div class="ds-card-title">Stock</div>
    </div>
    <div class="ds-card-body">
      <div class="tab-content">
        <div class="tab-pane fade show active" id="pills-3" role="tabpanel">
          <ol class="ds-rank-list" v-if="isLoadingCompanyStock">
            <li v-for="i in 10" :key="i">
              <b-skeleton width="260px"></b-skeleton>
            </li>
          </ol>
          <ol class="ds-rank-list">
            <li v-for="(stock, index) of stocks" :key="index">
              <span class="rank-num">{{ index + 1 }}</span>
              <strong>
                <a
                  class="text"
                  @click="chooseStock(stock)"
                  :href="'https://www.investing.com/search/?q=' + stock.stockCode"
                  target="_blank"
                >
                  {{ stock.market }} - {{ stock.stockCode }}
                </a>
              </strong>
            </li>
          </ol>
        </div>
      </div>
    </div>
  </div>
</template>
<script lang="ts" src="./ds-card-stock.component.ts"></script>

<style scoped lang="scss">
.ds-rank-list li {
  height: 40px;
}
</style>
