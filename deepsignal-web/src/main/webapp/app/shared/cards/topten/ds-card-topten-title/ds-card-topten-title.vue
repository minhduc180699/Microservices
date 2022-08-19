<template>
  <div class="ds-card">
    <div class="ds-card-header">
      <div class="ds-card-title">This is card top ten title</div>
    </div>
    <div class="ds-card-body">
      <ol class="ds-media-list-row thumb-circle" id="media-list-aaa">
        <li class="media">
          <div class="media-body">
            <div class="rank-wrap">
              <span class="rank-num">1</span>
              <div class="rank-changes rank-up">
                <span class="change-icon"></span>
                <span class="change-num">10</span>
              </div>
            </div>
            <div class="thumb">
              <img src="content/images/@people-thumb.png" alt="이경일" />
            </div>
            <div class="text-wrap">
              <strong>이경일</strong>
              <span>기업인</span>
            </div>
          </div>
        </li>
        <li class="media">
          <div class="media-body">
            <div class="rank-wrap">
              <span class="rank-num">2</span>
              <div class="rank-changes rank-up">
                <span class="change-icon"></span>
                <span class="change-num">10</span>
              </div>
            </div>
            <div class="thumb">
              <img src="content/images/@people-thumb.png" alt="이경일" />
            </div>
            <div class="text-wrap">
              <strong>이경일</strong>
              <span>기업인</span>
            </div>
          </div>
        </li>
        <li class="media">
          <div class="media-body">
            <div class="rank-wrap">
              <span class="rank-num">3</span>
              <div class="rank-changes rank-new">
                <span class="change-num">new</span>
              </div>
            </div>
            <div class="thumb">
              <img src="content/images/@people-thumb.png" alt="이경일" />
            </div>
            <div class="text-wrap">
              <strong>이경일</strong>
              <span>기업인</span>
            </div>
          </div>
        </li>
        <li class="media">
          <div class="media-body">
            <div class="rank-wrap">
              <span class="rank-num">4</span>
              <div class="rank-changes rank-new">
                <span class="change-num">new</span>
              </div>
            </div>
            <div class="thumb">
              <img src="content/images/@people-thumb.png" alt="이경일" />
            </div>
            <div class="text-wrap">
              <strong>이경일</strong>
              <span>기업인</span>
            </div>
          </div>
        </li>
        <li class="media">
          <div class="media-body">
            <div class="rank-wrap">
              <span class="rank-num">5</span>
              <div class="rank-changes rank-down">
                <span class="change-icon"></span>
                <span class="change-num">5</span>
              </div>
            </div>
            <div class="thumb">
              <img src="content/images/@people-thumb.png" alt="이경일" />
            </div>
            <div class="text-wrap">
              <strong>이경일</strong>
              <span>기업인</span>
            </div>
          </div>
        </li>
      </ol>
      <p class="rank-date">2021.06.17 기준</p>
    </div>
    <ds-card-footer :item="item" />
  </div>
</template>

<script lang="ts" src="./ds-card-topten-title.component.ts"></script>
