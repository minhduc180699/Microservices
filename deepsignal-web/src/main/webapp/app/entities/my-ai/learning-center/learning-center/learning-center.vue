<template>
  <div>
    <div class="container">
      <div class="lc-page">
        <div class="lc-nav">
          <ul class="nav">
            <li class="nav-item" v-for="(tab, index) in tabs" :key="index" @click="changeTab(tab, $event)">
              <a :class="['nav-link', { active: tab.active }]" data-toggle="tab" aria-selected="true"
                ><i :class="tab.icon"></i>{{ tab.name }}</a
              >
            </li>
          </ul>
        </div>
        <div class="lc-content tab-content">
          <!-- dynamic component -->
          <keep-alive>
            <component
              :is="currentTab"
              :displayMode.sync="displayMode"
              v-on:handleSelectedSearch="handleSelectedSearch"
              v-on:handleSelectedDocument="handleSelectedDocument"
              v-on:handleSelectedWeb="handleSelectedWeb"
            ></component>
          </keep-alive>
        </div>
      </div>
    </div>
    <div :class="['learning-cart-wrap cart-fill', { 'cart-show': isShowSelected }]">
      <div class="learning-cart-btn">
        <button type="button" class="btn" aria-label="Cart" @click="showSelected">
          <span class="empty"><i class="bi bi-bag"></i></span>
          <span class="fill"><i class="bi bi-bag-fill"></i></span>
          <span class="show"><i class="bi bi-x-lg"></i></span>
          <span v-if="!isShowSelected" class="num">{{ selectedSearch.length + selectedWeb.length + selectedDocument.length }}</span>
        </button>
      </div>
      <div class="learning-cart-area">
        <div class="container">
          <div class="container-inner">
            <div class="learning-cart-inner">
              <div class="cart-item">
                <div class="top-bar">
                  <div class="top-title">
                    <strong><i class="bi bi-bag"></i>{{ $t('learningCenter.learningList') }}</strong>
                  </div>
                  <div class="top-elements">
                    <span class="total"
                      >{{ $t('learningCenter.total') }}<em>{{ selectedSearch.length + selectedWeb.length + selectedDocument.length }}</em
                      >{{ $t('learningCenter.items') }}</span
                    >
                    <a
                      class="text-link"
                      @click="DeleteAllSelected"
                      :style="
                        selectedSearch.length + selectedWeb.length + selectedDocument.length == 0
                          ? { color: '#e2e2e2', pointerEvents: 'none' }
                          : {}
                      "
                      >{{ $t('learningCenter.deleteAll') }}</a
                    >
                  </div>
                </div>
                <div class="cart-item-wrap overflow-y-scroll">
                  <vue-custom-scrollbar class="scroll" :settings="scrollSettings">
                    <ul class="cart-item-list">
                      <li class="learning-item" v-for="(item, index) in selectedSearchShow" :key="index">
                        <div class="item-inner">
                          <strong class="title"
                            ><i class="bi bi-search"></i>{{ $t('learningCenter.search') }}<small>'{{ item.key }}'</small></strong
                          >
                          <span class="count"
                            ><a>{{ item.arr.length }}</a>
                            {{ $t('learningCenter.items') }}
                          </span>
                        </div>
                        <a class="btn-del" @click="removeSelectedSearch(item, index)"><i class="bi bi-x-lg"></i></a>
                      </li>
                      <li class="learning-item" v-if="selectedWeb.length > 0">
                        <div class="item-inner">
                          <strong class="title"><i class="bi bi-window"></i>{{ $t('learningCenter.web') }}</strong>
                          <span class="count"
                            ><a>{{ selectedWeb.length }}</a
                            >{{ $t('learningCenter.items') }}</span
                          >
                        </div>
                        <a class="btn-del" @click="removeSelectedWeb"><i class="bi bi-x-lg"></i></a>
                      </li>
                      <li class="learning-item" v-if="selectedDocument.length > 0">
                        <div class="item-inner">
                          <strong class="title"><i class="bi bi-file-earmark-text"></i>{{ $t('learningCenter.document') }}</strong>
                          <span class="count"
                            ><a>{{ selectedDocument.length }}</a
                            >{{ $t('learningCenter.items') }}</span
                          >
                        </div>
                        <a class="btn-del" @click="removeSelectedDocument"><i class="bi bi-x-lg"></i></a>
                      </li>
                    </ul>
                  </vue-custom-scrollbar>
                </div>
                <div class="btn-area">
                  <button type="button" class="btn btn-secondary btn-block" @click="learning" :disabled="!isLearning">
                    {{ isLearning ? $t('learningCenter.toLearn') : $t('learningCenter.learning') }}
                  </button>
                </div>
              </div>
              <div class="cart-check">
                <div class="info-box">
                  <strong><i class="bi bi-info-circle"></i>{{ $t('learningCenter.learningGuide') }}</strong>
                  <ul class="list-dot">
                    <li>
                      {{ $t('learningCenter.learningGuide1') }}
                    </li>
                    <li @click="changeTab(null, $event, true)">
                      <p v-html="$t('learningCenter.learningGuide2')"></p>
                    </li>
                  </ul>
                </div>
                <!--                <hr class="" />-->
                <!--                <div class="form-group form-check">-->
                <!--                  <input type="checkbox" class="form-check-input" id="exampleCheck1" />-->
                <!--                  <label class="form-check-label" for="exampleCheck1">{{ $t('learningCenter.receiveLearningCompleteNotify') }}</label>-->
                <!--                </div>-->
                <!--                <div class="btn-area">-->
                <!--                  <button type="button" class="btn btn-secondary btn-block" @click="learning" v-if="isLearning">-->
                <!--                    {{ $t('learningCenter.toLearn') }}-->
                <!--                  </button>-->
                <!--                  <b-button disabled v-if="!isLearning" class="btn btn-secondary btn-block">-->
                <!--                    <b-spinner small type="grow"></b-spinner>-->
                <!--                    {{ $t('learningCenter.learning') }}-->
                <!--                  </b-button>-->
                <!--                </div>-->
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./learning-center.component.ts"></script>
<style scoped>
.scroll {
  height: 80px;
  padding-right: 30px;
}
</style>
