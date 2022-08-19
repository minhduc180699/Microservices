<template>
  <div>
    <!--    <ds-cards :cardItems="cardItems" />-->
    <!--    <ds-chart class="chart" :option="option" />-->
    <page-top :isCheck="true" @selectedDate="selectedDate">Signal</page-top>
    <div class="row">
      <signal-today-issue :workDay="workDay"></signal-today-issue>
      <!--  component for register signal tracking issue    -->
      <register-signal-tracking-issue @onSaved="getAllSignalKeywords"></register-signal-tracking-issue>
      <div class="col-xl-6" v-for="signalKeyword of signalKeywords" :key="signalKeyword.id">
        <div class="panel-default" v-if="!signalKeyword.isEdit">
          <div class="panel-header">
            <div class="panel-title">
              <em class="text-primary">{{ signalKeyword.mainKeyword }}</em>
              <small class="subtitle">{{ $t('signal-tracking.issue-tracking') }}</small>
            </div>
            <div class="panel-elements">
              <div class="dropdown">
                <button class="btn btn-icon dropdown-toggle" type="button" data-toggle="dropdown">
                  <i class="icon-common icon-more"></i>
                </button>
                <div class="dropdown-menu">
                  <a class="dropdown-item" href="#" @click.prevent="onEdit(signalKeyword)" v-text="$t('signal-tracking.correction')"
                    >Correction</a
                  >
                  <a class="dropdown-item" href="#" @click.prevent="confirmDelete(signalKeyword)" v-text="$t('signal-tracking.delete')"
                    >Delete</a
                  >
                </div>
              </div>
            </div>
          </div>
          <div class="panel-body" style="height: 490px">
            <div class="issue-content">
              <div class="is-keyword-wrap">
                <div class="scroll-area">
                  <ul class="tag-list" v-if="signalKeyword.keywords">
                    <li class="list-item" v-for="(keyword, index) of signalKeyword.keywords.split(',')" :key="index">{{ keyword }}</li>
                  </ul>
                </div>
                <div class="btn-area">
                  <button type="button" class="btn btn-list-more"></button>
                </div>
              </div>
              <div class="is-analysis-wrap pb-0">
                <ul class="nav">
                  <li class="nav-item" @click="activeTab(signalKeyword, 'home')">
                    <a class="nav-link active" data-toggle="tab" v-text="$t('signal.relationshipDiagram')">Relationship Diagram</a>
                  </li>
                  <li class="nav-item" @click="activeTab(signalKeyword, 'profile')">
                    <a class="nav-link" data-toggle="tab" v-text="$t('signal.associatedWordAnalysis')">Associated Word</a>
                  </li>
                  <li class="nav-item" @click="activeTab(signalKeyword, 'messages')">
                    <a class="nav-link" data-toggle="tab" v-text="$t('signal.peopleOrCompany')">People Or Company</a>
                  </li>
                </ul>
                <div class="tab-content">
                  <div
                    class="tab-pane"
                    :class="{ active: signalKeyword.tabNameActive === 'home' + signalKeyword.id }"
                    v-if="signalKeyword.tabNameActive === 'home' + signalKeyword.id"
                  >
                    <div class="preview-area">
                      <line-chart :id="'line-chart' + signalKeyword.id" :data="signalKeyword.timeSeries"></line-chart>
                    </div>
                  </div>
                  <div
                    class="tab-pane"
                    :class="{ active: signalKeyword.tabNameActive === 'profile' + signalKeyword.id }"
                    v-if="signalKeyword.tabNameActive === 'profile' + signalKeyword.id"
                  >
                    <div class="preview-area">
                      <signal-word-cloud :idChart="'word-cloud' + signalKeyword.id" :currentSignal="signalKeyword"></signal-word-cloud>
                    </div>
                  </div>
                  <div
                    class="tab-pane"
                    :class="{ active: signalKeyword.tabNameActive === 'messages' + signalKeyword.id }"
                    v-if="signalKeyword.tabNameActive === 'messages' + signalKeyword.id"
                  >
                    <div class="preview-area">
                      <!--                      <img src="content/images/@social-analysis.png" alt="" />-->
                      <signal-people-company-chart
                        :currentSignal="signalKeyword"
                        :idChart="'people-company' + signalKeyword.id"
                      ></signal-people-company-chart>
                    </div>
                  </div>
                </div>
              </div>
              <div class="is-media-wrap">
                <div class="top-bar">
                  <div class="top-title">
                    <strong class="title"
                      ><span class="num-box">{{ signalKeyword.pageableClusterDocuments.totalElements || 0 }}</span
                      >{{ $t('signal-tracking.related-contents') }}</strong
                    >
                    <!--                    <a class="more-link" href="#">전체 보기</a>-->
                  </div>
                  <div class="top-elements">
                    <div class="elements-item page-control">
                      <div class="page-num">
                        <b class="current">{{ signalKeyword.pageableClusterDocuments.page }}</b
                        >/<span class="all">
                          {{ signalKeyword.pageableClusterDocuments.totalPages }}
                        </span>
                      </div>
                      <div class="btn-group">
                        <a class="btn btn-prev" aria-label="이전" @click="signalKeyword.pageableClusterDocuments.previousPage()"
                          ><i class="icon-arrow-left"></i
                        ></a>
                        <a class="btn btn-next" aria-label="다음" @click="signalKeyword.pageableClusterDocuments.nextPage()"
                          ><i class="icon-arrow-right"></i
                        ></a>
                      </div>
                    </div>
                  </div>
                </div>
                <ul class="ds-media-line-list">
                  <li class="list-item" v-for="(cluster, index) of signalKeyword.pageableClusterDocuments.contents" :key="index">
                    <div class="item-inner">
                      <span class="info">{{ cluster.writerName }}</span>
                      <router-link :to="{ name: 'Detail', params: { connectomeId: cluster.connectomeId, feedId: cluster.objId } }">
                        <a class="title">{{ cluster.title }}</a>
                      </router-link>
                    </div>
                  </li>
                </ul>
              </div>
            </div>
          </div>
        </div>
        <div class="panel-default" v-else>
          <div class="panel-header">
            <div class="panel-title" v-text="$t('signal-tracking.title-fix')">Fix issue tracking</div>
            <div class="panel-elements">
              <button type="button" class="btn btn-default" @click="confirmDelete(signalKeyword)">delete</button>
            </div>
          </div>
          <div class="panel-body" style="height: 490px">
            <div class="issue-content-add">
              <div class="group">
                <div class="top-bar">
                  <div class="top-title">
                    <strong class="title" v-text="$t('signal-tracking.issue-keyword')">Issue keywords</strong>
                    <a
                      tabindex="0"
                      class="btn-popover"
                      data-toggle="popover"
                      data-trigger="focus"
                      data-placement="top"
                      data-content="이슈 키워드는 정확한 이슈 트래킹을 위한 수집 키워드입니다."
                      ><i class="bi bi-question-circle-fill"></i
                    ></a>
                  </div>
                </div>
                <div class="tag-input-area overflow-y-scroll" style="max-height: 112px" :class="{ invalid: signalKeyword.isValidKeyword }">
                  <ul class="tag-list">
                    <li class="list-item" v-for="(keyword, index) of signalKeyword.keywordsToShow" :key="index">
                      {{ keyword }}
                      <a href="#" @click.prevent="removeKeywords(signalKeyword, keyword)"><i class="icon-common icon-close"></i></a>
                    </li>
                    <li class="list-add">
                      <input type="text" v-model="signalKeyword.messageKeyword" @keydown.enter.prevent="addToKeywords(signalKeyword)" />
                    </li>
                  </ul>
                </div>
                <div class="info-area">
                  <p>
                    At least {{ signalKeyword.minNumKeyword }} issue keywords must be entered, and up to
                    {{ signalKeyword.maxNumKeyword }} can be registered.
                  </p>
                </div>
              </div>
              <div class="group">
                <div class="top-bar">
                  <div class="top-title">
                    <strong class="title" v-text="$t('signal-tracking.representative-keyword')">Representative keywords</strong>
                    <a
                      tabindex="0"
                      class="btn-popover"
                      data-toggle="popover"
                      data-trigger="focus"
                      data-placement="top"
                      data-content="대표 키워드는 등록된 이슈 키워드의 대표 키워드로 이슈 트래킹 카드 제목에 명시됩니다."
                      ><i class="bi bi-question-circle-fill"></i
                    ></a>
                  </div>
                </div>
                <div class="tag-input-area" :class="{ invalid: signalKeyword.isValidMainKeyword }">
                  <ul class="tag-list">
                    <li class="list-item" v-if="signalKeyword.mainKeyword">
                      {{ signalKeyword.mainKeyword }}
                      <a href="#" @click.prevent="signalKeyword.mainKeyword = ''"><i class="icon-common icon-close"></i></a>
                    </li>
                    <form v-if="!signalKeyword.mainKeyword" @submit.prevent="addToMainKeyword($event, signalKeyword)">
                      <li class="list-add">
                        <input type="text" v-model="signalKeyword.messageMainKeyword" />
                      </li>
                    </form>
                  </ul>
                </div>
                <div class="info-area">
                  <p v-text="$t('signal-tracking.info')">
                    If the representative keyword is not entered, the first keyword of the registered related keyword is automatically
                    registered.
                  </p>
                </div>
              </div>
              <div class="btn-area">
                <div class="col">
                  <button
                    class="btn btn-default"
                    :disabled="loading"
                    v-text="$t('signal-tracking.cancel')"
                    @click="onCancel(signalKeyword)"
                  >
                    Cancel
                  </button>
                </div>
                <div class="col">
                  <button class="btn btn-primary" v-if="!loading" v-text="$t('signal-tracking.apply')" @click="onApply(signalKeyword)">
                    Apply
                  </button>
                  <button class="btn btn-primary" disabled v-if="loading" v-text="$t('signal-tracking.applying')">Apply...</button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script lang="ts" src="./ds-signals.component.ts"></script>
<style scoped></style>
