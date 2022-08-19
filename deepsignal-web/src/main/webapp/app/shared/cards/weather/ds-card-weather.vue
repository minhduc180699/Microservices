<template>
  <div data-template="weather" style="height: -webkit-fill-available" v-if="item && item.current">
    <div class="ds-card ds-card-info">
      <div class="ds-card-header">
        <div class="ds-card-title">Weather</div>
        <div class="ds-card-elements">
          <b-dropdown size="lg" variant="link" toggle-class="text-decoration-none" no-caret right>
            <template #button-content>
              <button type="button" class="btn btn-icon"><i class="icon-common icon-more"></i></button>
            </template>
            <b-dropdown-item @click.prevent="hideWeatherCard()">
              <i class="icon-common-sm icon-visibility"></i>{{ $t('feed.weather.hide-weather') }}
            </b-dropdown-item>
            <b-dropdown-item @click.prevent="openPopUpUserSetting()">
              <i class="icon-common-sm icon-setting"></i>{{ $t('feed.weather.detail-setting') }}</b-dropdown-item
            >
            <!--          <b-dropdown-item> <i class="icon-common-sm icon-problem"></i>{{ $t('feed.weather.report-problem') }}</b-dropdown-item>-->
          </b-dropdown>
        </div>
      </div>
      <div class="ds-card-body">
        <div class="weather-area">
          <div class="main-wrap">
            <div class="we-temperature" style="max-width: 170px">
              <strong>{{ item.current.temp }}</strong
              ><span>°</span>
            </div>
            <p class="we-summary" style="text-overflow: ellipsis; white-space: nowrap; overflow: hidden">
              {{ item.current.weather[0].description }}
            </p>
            <div class="we-summary-img">
              <img style="width: 80px" :src="item.current.weather[0].icon | iconWeather" alt="Weather Image" />
            </div>
          </div>
          <div class="we-location">
            <dl>
              <dt><i class="icon-common-sm icon-location"></i></dt>
              <dd>{{ item.city + ', ' + item.country }}</dd>
            </dl>
          </div>
          <div class="we-details">
            <div class="wrap">
              <dl>
                <dt>{{ $t('feed.weather.highest') }}</dt>
                <dd>{{ today.temp.max }}°</dd>
              </dl>
              <dl>
                <dt>{{ $t('feed.weather.lowest') }}</dt>
                <dd>{{ today.temp.min }}°</dd>
              </dl>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  <div class="ds-card" v-else>
    <div class="ds-card-body">
      <div class="wheather">
        <div class="temperature" style="max-width: 170px"></div>
        <div class="wheather-img"></div>
      </div>
      <div class="wheather-desc">
        <div></div>
        <div></div>
      </div>
    </div>
  </div>
</template>
<script lang="ts" src="./ds-card-weather.component.ts"></script>
<style scoped>
.dropdown-item {
  padding: 0.25rem 1.5rem !important;
  font-weight: 400 !important;
}
</style>
