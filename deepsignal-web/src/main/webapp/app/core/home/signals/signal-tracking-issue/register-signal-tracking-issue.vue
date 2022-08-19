<template>
  <div class="col-xl-6">
    <div class="panel-default">
      <div class="panel-header">
        <div class="panel-title" v-text="$t('signal-tracking.title-register')">Register for issue tracking</div>
      </div>
      <div class="panel-body" style="height: 490px">
        <div class="issue-content-add">
          <div class="alert alert-style1">
            <strong v-text="$t('signal-tracking.start-tracking')">Start tracking issues!</strong>
            <p v-text="$t('signal-tracking.register')">Register a keyword and check it.</p>
            <button type="button" class="close" data-dismiss="alert" aria-label="Close" title="닫기">
              <i class="icon-common icon-close"></i>
            </button>
          </div>
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
            <div class="tag-input-area overflow-y-scroll" style="max-height: 112px" :class="{ invalid: model.isValidKeyword }">
              <ul class="tag-list">
                <li class="list-item" v-for="(keyword, index) of model.keywordsToShow" :key="index">
                  {{ keyword }}
                  <a href="#" @click.prevent="removeIssueKeywords(keyword)"><i class="icon-common icon-close"></i></a>
                </li>
                <li class="list-add">
                  <form @submit.prevent="addToListText">
                    <input type="text" v-model="model.messageKeyword" />
                  </form>
                </li>
              </ul>
            </div>
            <div class="info-area">
              <p v-text="$t('signal-tracking.register-issue-tracking')">
                At least {{ model.minNumKeyword }} issue keywords must be entered, and up to {{ model.maxNumKeyword }} can be registered.
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
            <div class="tag-input-area" :class="{ invalid: model.isValidMainKeyword }">
              <ul class="tag-list">
                <li class="list-item" v-if="isShowMainKeyword">
                  {{ model.mainKeyword }}
                  <a @click.prevent="removeMainKeyword"><i class="icon-common icon-close"></i></a>
                </li>
                <li class="list-add" v-if="!isShowMainKeyword">
                  <input type="text" v-model="model.messageMainKeyword" @keydown.enter.prevent="addToRepresentative" />
                </li>
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
              <button class="btn btn-default" :disabled="loading" v-text="$t('signal-tracking.cancel')" @click="cancel()">Cancel</button>
            </div>
            <div class="col">
              <button class="btn btn-primary" v-if="!loading" v-text="$t('signal-tracking.apply')" @click="apply()">Apply</button>
              <button class="btn btn-primary" disabled v-if="loading" @click="apply()">Apply...</button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script lang="ts" src="./register-signal-tracking-issue.component.ts"></script>
