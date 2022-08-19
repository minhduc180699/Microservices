<template>
  <div class="container">
    <faq-category></faq-category>
    <div class="d-flex justify-content-between align-items-center">
      <div class="sub-category">
        <ul>
          <li v-for="tab in tabs" :key="tab.value" :class="{ active: currentTab === tab.value }">
            <a :href="'faq/' + tab.value">{{ tab.name }}</a>
          </li>
        </ul>
      </div>
      <div class="font-14px font-weight-medium">
        회원관리 (<i class="text-primary">{{ totalQuestion }}</i
        >)
      </div>
    </div>
    <router-view @total-question="updateTotalQuestion"></router-view>
    <!--    <component v-bind:is="currentTabComponent"></component>-->
    <div class="faq-info">
      <div class="row align-items-center">
        <div class="col-auto">
          <img src="content/images/icon-help.png" alt="" />
        </div>
        <div class="col">
          <strong>원하시는 답변을 찾지 못하셨다면, 1:1문의를 통해 문의해주세요.</strong>
          <p>최대한 빠른 시일내에 담당자가 답변 드리도록 하겠습니다.</p>
        </div>
        <div class="col-auto">
          <a href="#none" class="btn btn-outline-primary">1:1 문의 바로가기</a>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./faq.component.ts"></script>
