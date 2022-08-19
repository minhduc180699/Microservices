<template>
  <div class="row">
    <div class="col-xl-12" v-show="!goToLearningCenter">
      <div class="panel-default panel-relatedContents">
        <div class="panel-header">
          <div class="panel-title">
            <strong class="title"> <i class="bi bi-person-fill"></i>{{ peopleRelated.title + ' ' }}</strong>
            Related contents
          </div>
        </div>
        <div class="panel-body responsive-body">
          <ds-card-people-ranking :dataTransfer="peopleRanking" @changeItem="changeItem" ref="peopleRanking"></ds-card-people-ranking>
          <ds-carousel-people-feeds
            ref="carouselPeople"
            v-on:handleActivity="handleActivity"
            :cardPeople="cardPeople"
          ></ds-carousel-people-feeds>
        </div>
      </div>
    </div>
    <ds-card-social-network-analysis v-show="!goToLearningCenter" :tab="'people'" />
    <ds-card-people-social v-show="!goToLearningCenter" :peopleOrCompany="peopleOrCompany" />
    <div class="no-content col-xl-12" v-show="goToLearningCenter">
      <i class="bi bi-search"></i>
      <strong v-text="$t('feed.no-content.no-results')">There are no results</strong>
      <p v-text="$t('feed.no-content.go-to-my-ai')">Please go to My AI and training data or try another keyword and filter.</p>
      <b-button variant="light" style="margin-top: 15px; color: #0097f6" @click="learningKeyword" v-text="$t('feed.no-content.learn-more')"
        ><b>Learn more</b></b-button
      >
    </div>
  </div>
</template>
<script lang="ts" src="./ds-card-people.component.ts"></script>
<style lang="scss">
.card-body-layout1 .ds-card-body > .sidebar {
  border: none;
}
</style>
