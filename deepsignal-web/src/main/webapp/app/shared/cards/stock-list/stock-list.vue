<template>
  <div class="ds-card ds-card-info">
    <div class="ds-card-header">
      <div class="ds-card-title">Stocks</div>
      <div class="ds-card-elements">
        <b-dropdown size="lg" variant="link" toggle-class="text-decoration-none" no-caret right v-if="!isSearchStock">
          <template #button-content>
            <button type="button" class="btn btn-icon"><i class="icon-common icon-more"></i></button>
          </template>
          <b-dropdown-item @click.prevent="searchStock(true)">
            <i class="icon-common-sm icon-add"></i>{{ $t('feed.weather.search-add') }}</b-dropdown-item
          >
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
    <div class="ds-card-body" v-if="!isShowSpinner">
      <table class="table-finance" v-if="!isSearchStock">
        <colgroup>
          <col style="width: auto" />
          <col style="width: 60px" />
          <col style="width: 70px" />
        </colgroup>
        <tbody>
          <tr :class="item.up ? 'up' : 'down'" v-for="(item, index) in dataShow" :key="item.title + index">
            <th scope="row">{{ item.title }}</th>
            <td>{{ item.index }}</td>
            <td>
              <div class="rank-changes" :class="item.up ? 'rank-up' : 'rank-down'">
                <span class="change-icon"></span>
                <span class="change-num">{{ item.difference }}</span>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
      <div v-else>
        <b-form-input placeholder="Search stocks ..." v-model="keySearch" @input="searching" style="padding-top: 8px"></b-form-input>
        <div style="margin-bottom: 5px" id="result-search">
          <div class="layer-body">
            <ul class="search-keyword-list history">
              <li
                v-for="(item, index) in resultSearch"
                :key="index"
                @click="choseStock(item)"
                id="stock-detail"
                style="cursor: context-menu"
              >
                <span :class="'fi fi-' + item.marketCountry.toLowerCase()" style="width: 10%"></span>
                <p style="width: 27.5%" class="keyword">{{ item.symbolCode }}</p>
                <p style="width: 62.5%" class="keyword">{{ item.symbolNameEn }}</p>
                <!--                <p style="width: 22.5%" class="keyword">{{ item.marketName }}</p>-->
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

<script lang="ts" src="./stock-list.component.ts"></script>
