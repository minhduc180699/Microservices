<template>
  <div class="tab-pane show active" id="lc-search">
    <div class="lc-content-top">
      <div class="title">
        <h1>{{ $t('learningCenter.title') }}</h1>
        <h2><i class="bi bi-search"></i>{{ $t('learningCenter.search') }}</h2>
      </div>
    </div>
    <div class="tab-content">
      <div :class="'tab-pane show active'" id="learning-search-add">
        <div class="lc-content-add">
          <div class="form-group style1">
            <input
              class="form-control"
              type="text"
              :placeholder="$t('learningCenter.enterKeyword')"
              v-model="queries"
              v-on:keydown.enter.prevent="onEnterSearch(), (isAddCondition = true)"
              maxlength="2048"
            />
            <button class="form-btn" type="button" @click="searching()">
              <i class="bi bi-search" v-if="!isShowSpinner"></i>
              <b-spinner v-else variant="primary" style="width: 1.5rem; height: 1.5rem" label="Spinning"></b-spinner>
            </button>
          </div>
          <!-- 검색어 자동완성 -->
          <div class="search-layer search-connectome-autocomplete" style="display: none">
            <div class="layer-body">
              <ul class="search-keyword-list autocomplete">
                <li><a class="keword" href="#">Hedge_<b>fund</b>_firms</a></li>
                <li>
                  <a class="keword" href="#">EU <b>fund</b></a>
                </li>
                <li>
                  <a class="keword" href="#">US <b>fund</b></a>
                </li>
                <li>
                  <a class="keword" href="#"><b>Fund</b>_for_Nature</a>
                </li>
              </ul>
              <div class="no-content" style="display: none">
                <!-- 일치하는 키워드가 없는 경우 -->
                <strong>일치하는 검색어가 없습니다.</strong>
              </div>
            </div>
          </div>
        </div>
        <div class="lc-content-body">
          <div class="top-bar" v-if="allData.length > 0">
            <div class="top-title">
              <span class="searchResult"
                ><i class="bi bi-search"></i><em>'{{ textSearch + ' ' }}'</em>Found<b>{{ ' ' + allData.length + ' ' }}</b
                >documents</span
              >
              <span class="total">
                <b>{{ postSelectedSearch.length }}</b> {{ $t('learningCenter.choices') }}
              </span>
              <button
                class="btn btn-toggle btn-text focus"
                :class="{ active: isSelectAll }"
                type="button"
                :aria-pressed="isSelectAll"
                @click="selectAll"
              >
                <em class="on" v-if="!isSelectAll">{{ $t('learningCenter.selectAll') }}<i class="bi bi-check-circle"></i></em>
                <em class="off" v-else>{{ $t('learningCenter.unselectAll') }}<i class="bi bi-check-circle-fill"></i></em>
              </button>
            </div>
            <div class="top-elements">
              <!--              <button class="btn btn-primary" type="button" :disabled="allData.length > 0 ? false : true" @click="addLearning">-->
              <!--                <i class="bi bi-bag-plus"></i>{{ $t('learningCenter.addLearning') }}-->
              <!--              </button>-->
              <button
                class="btn btn-secondary"
                type="button"
                :disabled="postSelectedSearch.length > 0 && !this.$store.getters.getLearning ? false : true"
                @click="startLearning"
              >
                <i class="bi bi-file-play"></i> {{ isLearning ? $t('learningCenter.learning') : $t('learningCenter.toLearn') }}
              </button>
              <div class="btn-group btn-view btn-group-toggle" data-toggle="buttons">
                <!--                <label-->
                <!--                  :class="['btn btn-icon', { active: displayModeSync === 'grid' }]"-->
                <!--                  style="font-size: 100% !important"-->
                <!--                  @click="changeDisplayMode('grid')"-->
                <!--                >-->
                <!--                  <input type="radio" name="list-view" id="type-grid" /><i class="bi bi-grid-fill"></i>-->
                <!--                </label>-->
                <!--                <label-->
                <!--                  :class="['btn btn-icon', { active: displayModeSync === 'list' }]"-->
                <!--                  style="font-size: 100% !important"-->
                <!--                  @click="changeDisplayMode('list')"-->
                <!--                >-->
                <!--                  <input type="radio" name="list-view" id="type-list" checked="checked" /><i class="bi bi-list"></i>-->
                <!--                </label>-->
              </div>
            </div>
          </div>
          <div class="top-bar">
            <ul class="nav nav-tabs-style2 nav-fill">
              <li class="nav-item" v-for="tab in tabs" :key="tab.name" @click="activeTab(tab.value, tab.searchType)">
                <a :class="['nav-link', { active: tabActive === tab.value }]" data-toggle="tab">{{ tab.name }}</a>
              </li>
            </ul>
          </div>
          <div class="resource-list-wrap">
            <div v-if="allData.length > 0">
              <!--              <div style="margin-bottom: 15px">-->
              <!--                <a-->
              <!--                  v-for="tab in tabs"-->
              <!--                  :key="tab.name"-->
              <!--                  style="margin-right: 15px"-->
              <!--                  :style="tabActive == tab.value ? { color: '#516eff' } : {}"-->
              <!--                  @click="activeTab(tab.value, tab.searchType)"-->
              <!--                  >{{ tab.name }}</a-->
              <!--                >-->
              <!--              </div>-->
              <ul :class="['resource-list', displayModeSync === 'grid' ? 'type-grid' : 'type-list']" v-if="!isShowSpinner">
                <li
                  class="list-item"
                  v-for="(item, index) in allData"
                  :key="index"
                  :aria-selected="cardSelected.indexOf(index) > -1 ? 'true' : 'false'"
                >
                  <div class="resource-card type-search" v-if="item.searchType === 'searchVideo' || item.searchType === 'searchNews'">
                    <div class="card-thumb" v-if="item.img" style="position: relative">
                      <div v-if="item.searchType === 'searchVideo'" class="icon-play"></div>
                      <img :src="item.img" alt="" style="width: 100%" @error="$event.target.src = 'content/images/empty-image.png'" />
                    </div>
                    <div class="card-content">
                      <div class="info">
                        <div class="source-thumb" v-if="item.favicon">
                          <img :src="item.favicon" alt="" />
                        </div>
                        <span class="source-text"
                          ><a v-if="item.author">{{ item.author | lmTo(20) }}</a></span
                        >
                        <span class="source-text" v-if="!item.author">&#160;</span>
                        <span class="date">{{ item.create_date | formatDate('YYYY-MM-DD') }}</span>
                      </div>
                      <a class="title" target="_blank" :href="item.link" v-if="item.title" style="line-height: 22px">
                        <text-highlight :queries="queries" highlightStyle="padding: 0 0.2em">
                          {{ item.title }}
                        </text-highlight>
                      </a>
                      <a class="title" target="_blank" :href="item.link" v-if="!item.title">&#160;</a>
                      <p class="desc" v-if="item.description" style="line-height: 20px">
                        <text-highlight :queries="queries" highlightStyle="padding: 0 0.2em">
                          {{ item.description }}
                        </text-highlight>
                      </p>
                      <p class="desc" v-if="!item.description">&#160;</p>
                    </div>
                  </div>
                  <div class="resource-card type-document" v-else>
                    <div class="card-content">
                      <div class="info">
                        <div class="source-thumb" v-if="item.favicon">
                          <img :src="item.favicon" alt="" />
                        </div>
                        <span class="source-text"
                          ><a v-if="item.author">{{ item.author | lmTo(15) }}</a></span
                        >
                        <span class="source-text" v-if="!item.author">&#160;</span>
                        <span v-if="item.create_date" class="date">{{ item.create_date | formatDate('YYYY-MM-DD') }}</span>
                      </div>
                      <div class="file-inner">
                        <span class="file-icon" v-if="item.searchType === 'searchFileType:pdf'"
                          ><img src="content/images/common/file/file-pdf-fill.svg"
                        /></span>
                        <span class="file-icon" v-if="item.searchType === 'searchFileType:ppt'"
                          ><img src="content/images/common/file/file-ppt-fill.svg"
                        /></span>
                        <span class="file-icon" v-if="item.searchType === 'searchFileType:docx'"
                          ><img src="content/images/common/file/file-word-fill.svg"
                        /></span>
                        <strong class="title" v-if="item.title"
                          ><a :href="item.link" target="_blank">
                            <text-highlight :queries="queries" highlightStyle="padding: 0 0.2em">
                              {{ item.title }}
                            </text-highlight>
                          </a></strong
                        >
                        <strong class="title" v-if="!item.title">&#160;</strong>
                        <strong class="title">&#160;</strong>
                      </div>
                    </div>
                  </div>
                  <div class="btn-area">
                    <button type="button" class="btn btn-check" :title="$t('learningCenter.select')" @click="selectCard(item, index)">
                      <i class="bi bi-check2"></i>
                    </button>
                  </div>
                </li>
              </ul>
            </div>
          </div>
          <div v-if="isMoreResults" class="last-more" style="text-align: center; margin-top: 20px">
            <span class="connected-keyword">
              <a class="active" v-if="isShowMore" style="border-color: #e2e2e2; font-size: 0.85rem; color: #e2e2e2; border-radius: 4px"
                >Loading...</a
              >
              <a class="active" v-else style="border-color: #6a7681; font-size: 0.85rem; border-radius: 4px" @click="showMore">Show more</a>
            </span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./search.component.ts"></script>

<style>
.card-thumb .icon-play {
  height: 85%;
  width: 100%;
  position: absolute;
  top: 0;
  left: 0;
  background: rgba(0, 0, 0, 0.1) url('../../../../../content/images/icon-circled-play-40.png') no-repeat center center;
}
</style>
