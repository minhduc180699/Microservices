<template>
  <div class="tab-pane fade show active" id="lc-add-search">
    <div class="panel-collection">
      <div class="panel-header">
        <div class="panel-title">
          <strong class="title">Web Search</strong>
        </div>
      </div>
      <div class="panel-body">
        <div class="add-input-area">
          <div class="form-group search-group">
            <input
              class="form-control"
              type="text"
              placeholder="키워드를 입력하세요."
              v-model="queries"
              v-on:keydown.enter.prevent="onEnterSearch(), (isAddCondition = true)"
              maxlength="2048"
            />
            <button class="form-btn" type="button">
              <i class="icon-common icon-search-pirmary" v-if="!isShowSpinner"></i>
              <b-spinner v-else variant="primary" style="width: 1.5rem; height: 1.5rem" label="Spinning"></b-spinner>
            </button>
          </div>
        </div>
        <div class="search-tab">
          <div class="storage-filter">
            <div class="filter-main">
              <ul class="filter-list">
                <li class="filter-item" v-for="tab in tabs" :key="tab.name" @click="activeTab(tab.value, tab.searchType)">
                  <a :class="['filter-link', { active: tabActive === tab.value }]" data-toggle="tab">{{ tab.name }}</a>
                </li>
              </ul>
            </div>
          </div>
        </div>
        <div class="lc-list-top">
          <div class="elements-left">
            <div class="check-group">
              <button
                id="btnSelect"
                type="button"
                :class="['btn btn-check', { active: checked == true }]"
                data-toggle="button"
                aria-pressed="false"
                @click="selectAll"
              ></button>
              <label for="list-check-all2">전체 선택</label>
            </div>
            <div class="list-info">
              (<strong>{{ totalSelected }}</strong
              >/{{ this.allData.length }})
            </div>
          </div>
          <div class="elements-right">
            <button type="button" class="btn btn-default btn-sm"><i class="icon-common icon-close"></i>선택 삭제</button>
          </div>
        </div>
        <div class="lc-list-wrap">
          <vue-custom-scrollbar class="list-inner overflow-y-scroll customScroll csScrollPosition ps" :settings="scrollSettings">
            <ul class="lc-card-list">
              <li
                class="lc-card-item"
                :aria-selected="selectedItems.indexOf(item) > -1 ? 'true' : 'false'"
                v-for="(item, index) in allData"
                :key="index"
              >
                <single-card @setSelectedItems="setSelectedItems" :document="item" :selectedItems="selectedItems"></single-card>
              </li>
            </ul>
          </vue-custom-scrollbar>
        </div>
      </div>
      <div class="panel-footer">
        <div class="elements-right">
          <button type="button" class="btn btn-secondary" @click="insertToMemory">Add to current collection</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./search.component.ts"></script>

<style>
#image .icon-play {
  height: 25px;
  width: 25px;
  position: absolute;
  top: 20px;
  left: 30px;
  background: rgba(0, 0, 0, 0.1) url('../../../../../content/images/icon-circled-play-40.png') no-repeat center center;
  z-index: 1;
  background-size: contain;
}
#customInput input[type='checkbox']:before {
  content: '';
  display: inline-block;
  width: 20px;
  height: 20px;
  margin-top: -5px;
  border: 1px solid #ddd;
  border-radius: 2px;
  background-color: #fff;
}

#customInput input[type='checkbox']:checked:before {
  color: #fff;
  text-align: center;
  border: 1px solid #2196f3;
  background-color: #0097f6;
  background-image: url('../../../../../content/images/common/icon-common-set.png');
  background-position: -40px -40px;
  background-repeat: no-repeat;
}
</style>
