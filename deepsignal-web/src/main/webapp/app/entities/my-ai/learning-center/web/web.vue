<template>
  <div class="tab-pane show active" id="lc-web">
    <div class="lc-content-top">
      <div class="title">
        <h1>{{ $t('learningCenter.title') }}</h1>
        <h2><i class="bi bi-window"></i>{{ $t('learningCenter.web') }}</h2>
      </div>
    </div>
    <div class="tab-content">
      <div class="tab-pane show active" id="learning-search-add">
        <div class="lc-content-add">
          <div class="form-group style1">
            <input
              class="form-control"
              type="url"
              :placeholder="$t('learningCenter.insertUrlKeyword')"
              v-model="searchValue"
              v-on:keydown.enter.prevent="preview"
            />
            <button class="form-btn" type="button" @click="preview">
              <i class="bi bi-link-45deg" v-if="!isShowSpinner"></i>
              <b-spinner v-else variant="primary" label="Spinning"></b-spinner>
            </button>
          </div>
        </div>
        <div class="lc-content-body">
          <div class="top-bar" v-if="previewModels.length > 0">
            <div class="top-title">
              <strong>{{ $t('learningCenter.learnMoreList') }}</strong>
              <span class="total"
                >{{ $t('learningCenter.total') }}<em>{{ numberOfWeb }}</em
                >{{ $t('learningCenter.items') }}</span
              >
              <span class="total">
                <b>{{ postSelectedWeb.length }}</b> {{ $t('learningCenter.choices') }}
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
              <button
                class="btn btn-secondary"
                type="button"
                :disabled="cardSelected.length > 0 && !this.$store.getters.getLearning ? false : true"
                @click="startLearning"
              >
                <i class="bi bi-file-play"></i> {{ isLearning ? $t('learningCenter.learning') : $t('learningCenter.toLearn') }}
              </button>
              <!--              <div class="btn-group btn-view btn-group-toggle" data-toggle="buttons">-->
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
              <!--              </div>-->
            </div>
          </div>
          <div class="resource-list-wrap">
            <ul :class="['resource-list', displayModeSync === 'grid' ? 'type-grid' : 'type-list']">
              <li
                class="list-item"
                v-for="(model, index) of previewModels"
                :key="index"
                :aria-selected="cardSelected.indexOf(index) > -1 ? 'true' : 'false'"
              >
                <div class="resource-card type-search">
                  <div class="card-thumb">
                    <img :src="model.image" alt="" />
                  </div>
                  <div class="card-content">
                    <div class="info">
                      <div class="source-thumb" v-if="model.favicon">
                        <!-- Sample Images -->
                        <img :src="model.favicon" alt="" />
                      </div>
                      <span class="source-text"
                        ><a :href="model.url" target="_blank">{{ model.url }}</a></span
                      >
                      <!--                      <span class="date">2021.12.23</span>-->
                    </div>
                    <a class="title" :href="model.url" target="_blank">{{ model.title }}</a>
                    <p class="desc">{{ model.desc }}</p>
                  </div>
                </div>
                <div class="btn-area">
                  <button type="button" class="btn btn-check" :title="$t('learningCenter.select')" @click="selectCard(model, index)">
                    <i class="bi bi-check2"></i>
                  </button>
                </div>
              </li>
            </ul>
          </div>
        </div>
      </div>
      <br />
    </div>
  </div>
</template>

<script lang="ts" src="./web.component.ts"></script>
