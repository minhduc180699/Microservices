<template>
  <b-modal
    class="modal fade"
    id="modal-member-setting"
    data-backdrop="static"
    data-keyboard="false"
    tabindex="-1"
    aria-hidden="true"
    hide-header
    hide-footer
    centered
    @hide="onCloseSetting"
  >
    <div>
      <!-- <div class="modal-dialog modal-dialog-centered modal-dialog-scrollable"></div> -->
      <div class="modal-content modal-setting" v-if="formModel">
        <div class="modal-header">
          <h5 class="modal-title" id="exampleModalLabel"><i class="icon-common icon-setting"></i>{{ $t('feed.popup-setting.setting') }}</h5>
          <button type="button" @click="$bvModal.hide('modal-member-setting')" class="close" data-dismiss="modal" aria-label="Close">
            <i class="icon-common-lg icon-close"></i>
          </button>
        </div>
        <div class="modal-body">
          <div class="tab-column">
            <div class="tab-nav">
              <div class="nav nav-pills">
                <a class="nav-link" id="v-pills-profile-tab" data-toggle="pill" href="#set-general">{{
                  $t('feed.popup-setting.general')
                }}</a>
                <a class="nav-link active" id="v-pills-profile-tab1" data-toggle="pill" href="#set-infocard">{{
                  $t('feed.popup-setting.information-card')
                }}</a>
              </div>
            </div>
            <div class="tab-content">
              <div class="tab-pane fade" id="set-general">
                <div class="setting-content-wrap">
                  <div class="setting-content-inner overflow-y-scroll">
                    <div class="setting-group">
                      <div class="group-header">
                        <strong class="group-title">{{ $t('feed.popup-setting.language') }}</strong>
                      </div>
                      <div class="group-body">
                        <div class="form-group form-group-column">
                          <label>{{ $t('feed.popup-setting.language-content') }}</label>
                          <select class="form-control form-control-sm" v-model="language">
                            <option :value="key" :key="'language-setting-' + key" v-for="(value, key) in languages">
                              {{ value.name }}
                            </option>
                          </select>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
              <div class="tab-pane fade show active" id="set-infocard">
                <div class="setting-content-wrap">
                  <div class="setting-content-inner overflow-y-scroll">
                    <div class="setting-group">
                      <div class="group-header">
                        <strong class="group-title">{{ $t('feed.popup-setting.weather') }}</strong>
                      </div>
                      <div class="group-body">
                        <div class="form-group form-group-row">
                          <label>{{ $t('feed.popup-setting.show-weather-card') }}</label>
                          <div class="custom-control custom-switch">
                            <input
                              type="checkbox"
                              class="custom-control-input"
                              id="feed-info-weather"
                              v-model="formModel.weather.showWeatherCard"
                            />
                            <label class="custom-control-label" for="feed-info-weather"></label>
                          </div>
                        </div>
                        <div class="form-group form-group-row">
                          <label>{{ $t('feed.popup-setting.always-detect-location') }}</label>
                          <div class="custom-control custom-switch">
                            <input
                              type="checkbox"
                              class="custom-control-input"
                              id="feed-info-weather-location"
                              v-model="formModel.weather.alwaysDetectLocation"
                            />
                            <label class="custom-control-label" for="feed-info-weather-location"></label>
                          </div>
                        </div>
                        <fieldset class="form-group form-group-column">
                          <legend>{{ $t('feed.popup-setting.temperature-unit') }}</legend>
                          <div class="form-row">
                            <div class="col">
                              <div class="custom-control custom-radio">
                                <input
                                  type="radio"
                                  id="temp-unit-f"
                                  name="temp-unit"
                                  class="custom-control-input"
                                  :value="UNIT.F"
                                  v-model="formModel.weather.temperatureUnit"
                                />
                                <label class="custom-control-label" for="temp-unit-f">{{ $t('feed.popup-setting.fahrenheit') }} </label>
                              </div>
                            </div>
                            <div class="col">
                              <div class="custom-control custom-radio">
                                <input
                                  type="radio"
                                  id="temp-unit-c"
                                  name="temp-unit"
                                  class="custom-control-input"
                                  :value="UNIT.C"
                                  v-model="formModel.weather.temperatureUnit"
                                />
                                <label class="custom-control-label" for="temp-unit-c">{{ $t('feed.popup-setting.celsius') }}</label>
                              </div>
                            </div>
                          </div>
                        </fieldset>
                      </div>
                    </div>
                    <div class="setting-group">
                      <div class="group-header">
                        <strong class="group-title">{{ $t('feed.popup-setting.stock') }}</strong>
                      </div>
                      <div class="group-body">
                        <div class="form-group form-group-row">
                          <label>{{ $t('feed.popup-setting.show-stock-cards') }}</label>
                          <div class="custom-control custom-switch">
                            <input
                              type="checkbox"
                              class="custom-control-input"
                              id="feed-info-Stock"
                              v-model="formModel.stock.showStockCard"
                            />
                            <label class="custom-control-label" for="feed-info-Stock"></label>
                          </div>
                        </div>
                        <fieldset class="form-group form-group-column">
                          <legend>{{ $t('feed.popup-setting.choose-stock-style') }}</legend>
                          <div class="form-row">
                            <div class="col">
                              <div class="custom-control custom-radio">
                                <input
                                  type="radio"
                                  id="stock-view-chart"
                                  name="stock-view"
                                  class="custom-control-input"
                                  :value="STYLE.CHART"
                                  v-model="formModel.stock.cardStyle"
                                />
                                <label class="custom-control-label" for="stock-view-chart">{{
                                  $t('feed.popup-setting.view-in-chart')
                                }}</label>
                              </div>
                              <div class="example-img">
                                <img src="/content/images/common/stock-type-chart.png" alt="" />
                              </div>
                            </div>
                            <div class="col">
                              <div class="custom-control custom-radio">
                                <input
                                  type="radio"
                                  id="stock-view-list"
                                  name="stock-view"
                                  class="custom-control-input"
                                  :value="STYLE.LIST"
                                  v-model="formModel.stock.cardStyle"
                                />
                                <label class="custom-control-label" for="stock-view-list">{{
                                  $t('feed.popup-setting.view-by-list')
                                }}</label>
                              </div>
                              <div class="example-img">
                                <img src="/content/images/common/stock-type-list.png" alt="" />
                              </div>
                            </div>
                          </div>
                        </fieldset>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="modal-footer">
          <button type="button" @click="$bvModal.hide('modal-member-setting')" class="btn btn-default" data-dismiss="modal">Close</button>
          <button type="button" class="btn btn-primary" @click.prevent="saveSetting">Save</button>
        </div>
      </div>
    </div>
  </b-modal>
</template>
<script lang="ts" src="./popup-user-setting.component.ts"></script>
<style scoped>
#modal-user-setting___BV_modal_body_ {
  padding: 0;
}

#modal-user-setting___BV_modal_content_ {
  border: unset;
  border-radius: 8px;
}

#modal-user-setting .modal-dialog {
  max-width: 580px;
}
</style>
