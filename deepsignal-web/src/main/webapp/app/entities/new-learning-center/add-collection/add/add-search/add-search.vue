<template>
  <div class="tab-pane fade show active" id="lc-add-search">
    <div class="panel-LS-content">
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
          <ul class="nav nav-tabs">
            <li class="nav-item" v-for="tab in tabs" :key="tab.name" @click="activeTab(tab.value, tab.searchType)">
              <a :class="['nav-link', { active: tabActive === tab.value }]" data-toggle="tab">{{ tab.name }}</a>
            </li>
          </ul>
        </div>
        <div class="list-top">
          <div class="elements-left">
            <div class="form-group">
              <button type="button" class="btn btn-check" id="list-check-all" data-toggle="button" aria-pressed="false"></button>
              <label for="list-check-all">전체 선택</label>
            </div>
            <div class="list-info">(<strong>2</strong>/20)</div>
          </div>
          <div class="elements-right">
            <button type="button" class="btn btn-default btn-sm"><i class="icon-common icon-close"></i>선택 삭제</button>
          </div>
        </div>
        <div class="list-wrap">
          <vue-custom-scrollbar class="list-inner overflow-y-scroll customScroll csScrollPosition ps" :settings="scrollSettings">
            <ul class="lc-card-list lc-resource-list" v-for="index in 10" :key="index">
              <li class="lc-card-item" aria-selected="false">
                <div class="item-wrap">
                  <div class="content-box">
                    <div class="lc-check">
                      <button type="button" class="btn btn-check" data-toggle="button" aria-pressed="false"></button>
                    </div>
                    <div class="lc-media">
                      <a class="media-img" href="#">
                        <img src="content/images/sample/@445x290.png" />
                      </a>
                      <div class="media-body">
                        <a class="media-title" href="#">아니한 안고, 이상의 우리의 청춘의 석가는 커다란 온갖 청춘은 있다.</a>
                        <p class="media-desc">
                          행복스럽고 구하기 광야에서 목숨이 피어나기 고행을 돋고, 만물은 뿐이다. 하여도 심장은 용감하고 교향악이다. 곳이
                          있는 사랑의 쓸쓸한 꽃이 위하여, 그러므로 가치를 곧 칼이다.
                        </p>
                        <div class="media-info">
                          <div class="info-item">
                            <div class="source">
                              <div class="source-img"><img src="content/images/sample/media-aju.png" /></div>
                              아시아경제
                            </div>
                          </div>
                          <div class="info-item">3일전</div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </li>
            </ul>
          </vue-custom-scrollbar>
        </div>
      </div>
      <div class="panel-footer">
        <div class="elements-right">
          <button type="button" class="btn btn-secondary">Add to collection</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./add-search.component.ts"></script>

<style>
.panel-body {
  padding-bottom: 88px !important;
}
.panel-footer {
  bottom: 80px !important;
}
</style>
