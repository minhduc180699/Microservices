<template>
  <div>
    <page-top>
      Feed
      <template #feedSearch>
        <span v-if="label" class="connected-keyword"
          ><a class="active"
            >{{ label }}<button type="button" class="close" aria-label="Close" @click="closeHashTag"><i class="bi bi-x"></i></button></a
        ></span>
        <span v-if="!label && isSearching && keyword.length > 0" class="keyword">
          <i class="bi bi-search"></i>
          <text-highlight :queries="keyword.split('/')" highlightStyle="padding: 0 0.2em">{{ keyword }}</text-highlight>
          <button @click="closeSearch()" type="button" class="close" style="padding: 0 6px" aria-label="Close">
            <i class="bi bi-x"></i>
          </button>
        </span>
        <!--        <span v-if="!label && isSearching && isNoResult" class="keyword"><i class="bi bi-search"></i>{{ $t('search.noResultSearch') }}</span>-->
      </template>
      <template #feedBtnFilter>
        <div class="top-elements">
          <button type="button" class="btn btn-icon" data-toggle="collapse" @click="openSearchFilter">
            <i class="icon-common-lg icon-search"></i>
          </button>
        </div>
      </template>
      <template #feedFilterBox>
        <div class="filter-box">
          <div class="filter-finder collapse" :class="{ show: searchFilter }">
            <div class="filter-item">
              <strong class="filter-title" v-text="$t('feed.filter.keyword')"></strong>
              <div class="filter-option option-keyword">
                <input v-model="keyword" type="text" class="form-control" placeholder="keyword" @keyup.enter="searchFeed()" />
              </div>
            </div>
            <div class="filter-item">
              <strong class="filter-title" v-text="$t('feed.filter.period.name')"></strong>
              <div class="filter-option option-period">
                <ul class="option-list">
                  <li v-for="period in periods" :key="period.text" @click="filterFeed('period', period)">
                    <a :class="{ active: period.selected }">{{ $t('feed.filter.period.' + period.text) }}</a>
                  </li>
                  <li>
                    <a
                      class="link-toggle collapsed"
                      data-toggle="collapse"
                      data-target="#period-directInput"
                      v-text="$t('feed.filter.period.choosePeriod')"
                    ></a>
                  </li>
                </ul>
                <div class="collapse" id="period-directInput">
                  <div class="directInput-wrap" style="display: flex">
                    <div class="period-input">
                      <!--                      <div class="input-group">-->
                      <!--                        <div class="input-group-prepend">-->
                      <!--                          <div class="input-group-text"><i class="bi bi-calendar-check"></i></div>-->
                      <!--                        </div>-->
                      <!--                        <input type="text" class="form-control" placeholder="2022.02.19">-->
                      <date-picker
                        :class="isDatePicker ? 'datePickerErr' : ''"
                        v-model="datePicker.from"
                        type="date"
                        format="YYYY-MM-DD"
                        valueType="format"
                        :clearable="false"
                      ></date-picker>
                      <!--                      </div>-->
                      <div class="input-group-between">~</div>
                      <!--                      <div class="input-group">-->
                      <!--                        <div class="input-group-prepend">-->
                      <!--                          <div class="input-group-text"><i class="bi bi-calendar-check"></i></div>-->
                      <!--                        </div>-->
                      <!--                        <input type="text" class="form-control" placeholder="2022.02.19">-->
                      <date-picker
                        :class="isDatePicker ? 'datePickerErr' : ''"
                        v-model="datePicker.to"
                        type="date"
                        format="YYYY-MM-DD"
                        valueType="format"
                        :clearable="false"
                      ></date-picker>
                      <!--                      </div>-->
                    </div>
                    <b-button class="ml-5" variant="primary" size="sm" style="font-size: 14px" @click="choosePeriod()">Confirm</b-button>
                    <div class="period-calendar">
                      <div class="calendar-wrap calendar-from active">
                        <div id="datepicker"></div>
                      </div>
                      <div class="calendar-wrap calendar-to">
                        <div id="datepicker2"></div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="filter-item">
              <strong class="filter-title" v-text="$t('feed.filter.category.name')"></strong>
              <div class="filter-option option-type">
                <ul class="option-list">
                  <li v-for="category in categories" :key="category.text" @click="filterFeed('category', category)">
                    <a :class="{ active: category.selected }">{{ $t('feed.filter.category.' + category.text) }}</a>
                  </li>
                </ul>
              </div>
            </div>
          </div>
        </div>
      </template>
    </page-top>

    <ds-cards
      :cardItems="isSearching ? (isNoResult ? [] : cardItemSearch) : cardItems"
      :tab="'feed'"
      :loaderDisable="loaderDisable"
      :keyword="keyword"
      v-on:scrollLoader="scrollLoader"
      v-on:handleActivity="handleActivity"
    />
    <div class="no-content" v-show="isNoResult || goToLearningCenter">
      <i class="bi bi-search"></i>
      <strong v-text="$t('feed.no-content.no-results')">There are no results</strong>
      <p v-text="$t('feed.no-content.go-to-my-ai')">Please go to My AI and training data or try another keyword and filter.</p>
      <b-button variant="light" style="margin-top: 15px; color: #0097f6" @click="learningKeyword" v-text="$t('feed.no-content.learn-more')"
        ><b>Learn more</b></b-button
      >
    </div>
    <ds-toast-new-feed @feedUpdated="feedUpdated($event)"></ds-toast-new-feed>
  </div>
</template>

<script lang="ts" src="./ds-feed.component.ts"></script>
<style lang="scss">
.datePickerErr {
  .mx-input-wrapper {
    .mx-input {
      border: 1px solid red;
    }
  }
}

.share-link .but-share {
  margin-top: 10px;
  float: right;
}

div.result-search-email:hover {
  background-color: #e9ecef;
  cursor: pointer;
}

.btn-refresh button {
  float: right;
}

.icon-close {
  vertical-align: baseline !important;
}

.close-btn {
  padding: 8px;
  float: right;
}
</style>
