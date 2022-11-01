<template>
  <div class="tab-pane fade show active" id="lc-add-url">
    <div class="panel-collection">
      <div class="panel-header">
        <div class="panel-title">
          <strong class="title">URL</strong>
        </div>
      </div>
      <div class="panel-body">
        <div class="add-input-area">
          <div class="form-group url-group">
            <input
              class="form-control"
              type="url"
              :placeholder="$t('learningCenter.insertUrlKeyword')"
              v-model="searchValue"
              v-on:keydown.enter.prevent="preview"
            />
            <button class="form-btn" type="button">
              <i class="icon-common icon-link-pirmary" v-if="!isShowSpinner" @click="preview"></i>
              <b-spinner v-else variant="primary" label="Spinning"></b-spinner>
            </button>
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
              <label for="list-check-all5" v-text="$t('newLearningCenter.selectAll')">전체 선택</label>
            </div>
            <div class="list-info">
              (<strong>{{ totalSelected }}</strong
              >/{{ previewModels.length }})
            </div>
          </div>
          <div class="elements-right">
            <button type="button" class="btn btn-default btn-sm" @click="deleteAll">
              <i class="icon-common icon-close"></i> {{ $t('newLearningCenter.deleteSelection') }}
            </button>
          </div>
        </div>
        <div class="lc-list-wrap">
          <vue-custom-scrollbar class="list-inner overflow-y-scroll customScroll csScrollPosition ps" :settings="scrollSettings">
            <ul class="lc-card-list">
              <li class="lc-card-item" aria-selected="false" v-for="(model, index) of previewModels" :key="index">
                <single-card :document="model" :selectedItems="selectedItems" @setSelectedItems="setSelectedItems"></single-card>
              </li>
            </ul>
          </vue-custom-scrollbar>
        </div>
      </div>
      <div class="panel-footer">
        <div class="elements-right">
          <button type="button" class="btn btn-secondary" @click="insertToMemory" v-text="$t('newLearningCenter.addToStorage')">
            Add to current collection
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./web.component.ts"></script>
