<template>
  <div class="container">
    <!--    <div class="item-list-top">-->
    <!--      <div class="row align-items-center justify-content-between">-->
    <!--        <div class="col-auto">-->
    <!--          <div class="count">-->
    <!--            <strong># Total</strong>-->
    <!--            <p><i>281</i> feeds were collected. There is <i>10</i> new feed.</p>-->
    <!--            <p></p>-->
    <!--          </div>-->
    <!--        </div>-->
    <!--        <div class="col-auto">-->
    <!--          <div class="btn-group" role="group">-->
    <!--            <button type="button" class="btn btn-outline-lightgray btn-item-refrash" aria-label="리스트 새로고침 자동 설정" data-auto-refresh="true"><i></i></button>-->
    <!--            <button type="button" class="btn btn-outline-lightgray btn-item-filter btn-tooltip-toggler" aria-label="필터"><i class="icon-setting"></i>-->
    <!--              <div class="tooltip-menu-group" style="display: none;">-->
    <!--                <div class="tooltip-menu">-->
    <!--                  <a href="#none" class="selected">추천순</a>-->
    <!--                  <a href="#none">최신순</a>-->
    <!--                  <a href="#none">오래된 순</a>-->
    <!--                </div>-->
    <!--                <div class="tooltip-menu">-->
    <!--                  <a href="#none">전체</a>-->
    <!--                  <a href="#none"><span class="icon-area"><i class="icon-bookmark-sm"></i></span>북마크</a>-->
    <!--                  <a href="#none"><span class="icon-area"><i class="icon-thumb-up"></i></span>좋아요</a>-->
    <!--                  <a href="#none"><span class="icon-area"><i class="icon-window"></i></span>공유</a>-->
    <!--                </div>-->
    <!--              </div>-->
    <!--            </button>-->
    <!--          </div>-->
    <!--        </div>-->
    <!--      </div>-->
    <!--    </div>-->

    <!-- include only accept component name -->
    <keep-alive include="ds-feed,ds-people,ds-signal">
      <router-view></router-view>
    </keep-alive>
  </div>
</template>

<script lang="ts" src="./home.component.ts"></script>
