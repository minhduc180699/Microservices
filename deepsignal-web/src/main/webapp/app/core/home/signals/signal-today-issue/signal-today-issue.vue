<template>
  <div class="col-xl-12">
    <div class="panel-default">
      <div class="panel-header">
        <div class="panel-title">Today's Issue</div>
        <div class="panel-title">
          <h5 v-if="currentSignal">{{ currentSignal.keywords | lmTo(63) }}</h5>
        </div>
        <div class="panel-elements"></div>
      </div>
      <div class="panel-body responsive-body" v-show="!isNoData">
        <div class="body-nav">
          <vue-custom-scrollbar class="overflow-yx-scroll customScroll csScrollPositon" :settings="scrollSettings">
            <ul class="list-group">
              <div v-if="isLoadingSignal">
                <li class="list-item" v-for="i in 6" :key="i">
                  <b-skeleton height="80px"></b-skeleton>
                </li>
              </div>
              <li class="list-item" v-for="(item, index) of signals" :key="index">
                <a
                  class="list-link media"
                  v-show="currentSignal"
                  :class="{ active: currentSignal.id === item.id }"
                  data-toggle="list"
                  @click="changeHash(index + 1)"
                >
                  <div class="media-body">
                    <div class="rank-wrap">
                      <span class="rank-num">{{ index + 1 }}</span>
                    </div>
                    <div class="text-wrap d-flex align-items-center">
                      <div class="text-inner" :title="item.keywords">{{ item.keywords }}</div>
                    </div>
                  </div>
                </a>
              </li>

              <li class="list-more" v-if="totalPages > pageable.page">
                <a class="list-link" @click="viewMore">
                  <i class="icon-common"></i>
                  <span>View more</span>
                  <span class="page-num"
                    ><b class="current">{{ pageable.page }}</b
                    >/ {{ totalPages }}</span
                  >
                </a>
              </li>
            </ul>
          </vue-custom-scrollbar>
        </div>
        <div class="body-content">
          <div class="tab-content">
            <div class="tab-pane fade show active" id="today-new-01">
              <div class="cluster">
                <signal-today-issue-common ref="signalTodayIssueCommon" :hasPageable="true" :sizeCluster="8"></signal-today-issue-common>
                <div class="analysis-preview">
                  <div class="preview-box">
                    <div class="top-bar">
                      <strong class="title" v-text="$t('signal.relationshipDiagram')"></strong>
                    </div>
                    <div class="preview-area">
                      <b-skeleton-img v-if="isLoadingSignal"></b-skeleton-img>
                      <signal-relationship-diagram-analysis
                        v-if="currentSignal['neuronNetworkChart']"
                        :chartData="currentSignal['neuronNetworkChart']"
                        :idChart="'chartRelationshipIssue'"
                      ></signal-relationship-diagram-analysis>
                    </div>
                  </div>
                  <div class="preview-box">
                    <div class="top-bar">
                      <strong class="title" v-text="$t('signal.associatedWordAnalysis')">연관어 분석</strong>
                    </div>
                    <div class="preview-area">
                      <b-skeleton-img v-if="isLoadingSignal"></b-skeleton-img>
                      <signal-word-cloud-chart :idChart="'todayIssue'" :currentSignal="currentSignal"></signal-word-cloud-chart>
                    </div>
                  </div>
                  <div class="preview-box">
                    <div class="top-bar">
                      <strong class="title" v-text="$t('signal.peopleOrCompany')">인물/기업</strong>
                    </div>
                    <div class="preview-area">
                      <b-skeleton-img v-if="isLoadingSignal"></b-skeleton-img>
                      <signal-people-company-chart
                        :currentSignal="currentSignal"
                        :idChart="'signalPeopleCompany'"
                      ></signal-people-company-chart>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="tab-pane fade" id="today-new-02">품으며, 곳으로 든 주며, 싸인 발휘하기 품고 길을 듣는다.</div>
          </div>
        </div>
      </div>
      <div class="no-content col-xl-12" v-show="isNoData">
        <i class="bi bi-search"></i>
        <strong v-text="$t('feed.no-content.no-results')">There are no results</strong>
        <p v-text="$t('feed.no-content.go-to-my-ai')">Please go to My AI and training data or try another keyword and filter.</p>
        <b-button
          variant="light"
          style="margin-top: 15px; color: #0097f6"
          @click="learningKeyword"
          v-text="$t('feed.no-content.learn-more')"
          ><b>Learn more</b></b-button
        >
      </div>
    </div>
  </div>
</template>
<script lang="ts" src="./signal-today-issue.component.ts"></script>
<style>
.customScroll {
  max-height: 500px;
  padding-right: 24px;
}
</style>
