<template>
  <div class="sitemap-layer">
    <div class="dim" @click="closeSidebar"></div>
    <div class="panel-sidebar sidebar-gnb active">
      <div class="panel-header">
        <div class="header-top">
          <div class="logo"><span class="sr-only">DeepSignal</span></div>
          <div class="col-auto">
            <button type="button" class="layer-btn-close" aria-label="사이트맵 닫기" id="js-btn-sitemap-close">
              <i class="icon-close"></i>
            </button>
          </div>
        </div>
        <div class="header-content">
          <!--          <div class='login-before'>-->
          <!--            <p>로그인하시면<br>더 많은 혜택이 가득</p>-->
          <!--            <b-button variant='info'>회원가입</b-button>-->
          <!--            <b-button variant='info'>로그인</b-button>-->
          <!--          </div>-->
          <!--          <div class="login-after">-->
          <!--            <a href="#">Warren</a>-->
          <!--            <p v-text="$t('feed.responsive-mobile.header.message')">상쾌한 하루 시작하세요</p>-->
          <!--            <div>-->
          <!--              <dl>-->
          <!--                <dt>Followers</dt>-->
          <!--                <dd>219</dd>-->
          <!--              </dl>-->
          <!--              <dl>-->
          <!--                <dt>Following</dt>-->
          <!--                <dd>129</dd>-->
          <!--              </dl>-->
          <!--            </div>-->
          <!--          </div>-->
        </div>
      </div>
      <div class="panel-body overflow-y-scroll">
        <div style="overflow-y: scroll; width: 100%; height: 100%; scrollbar-width: none; margin-top: 32px; max-height: 580px">
          <div class="panel-section">
            <ul class="sidebar-nav-grid">
              <li>
                <a @click="changePage(1)"><i class="bi bi-columns-gap"></i><span>Feed</span></a>
              </li>
              <li>
                <a @click="changePage(2)"><i class="bi bi-people"></i><span>People</span></a>
              </li>
              <li>
                <a @click="changePage(3)"><i class="bi bi-lightbulb"></i><span>Signal</span></a>
              </li>
              <!--            <li>-->
              <!--              <a href="#"><i class="bi bi-clipboard-data"></i><span>Intelligence</span></a>-->
              <!--            </li>-->
              <li>
                <a @click="changePage(5)"><i class="bi bi-calendar3-week"></i><span>My Connectome</span></a>
              </li>
            </ul>
          </div>
          <div class="panel-section">
            <div class="banner">
              <strong>Training Center</strong>
              <p v-text="$t('feed.responsive-mobile.learning-center')">선호하는 취향을 학습시키면, 취향에 맞는 컨텐츠를 추천해 드립니다.</p>
              <div class="btn-area">
                <button type="button" class="btn btn-sm" @click="changePage(6)"><span>Training Center</span></button>
                <!--              <button type="button" class="btn btn-sm"><span>Learning Center</span></button>-->
              </div>
            </div>
          </div>
          <!--          <div class="panel-section">-->
          <!--            <div class="section-header">-->
          <!--              <strong>Archive</strong>-->
          <!--            </div>-->
          <!--            <div class="section-body">-->
          <!--              <ul class="sidebar-nav-list">-->
          <!--&lt;!&ndash;                <li>&ndash;&gt;-->
          <!--&lt;!&ndash;                  <a href="#" v-text="$t('feed.responsive-mobile.archive.history')"><i class="bi bi-clock-history"></i>최근 본 콘텐츠</a>&ndash;&gt;-->
          <!--&lt;!&ndash;                </li>&ndash;&gt;-->
          <!--                <li>-->
          <!--                  <a href="#" v-text="$t('feed.responsive-mobile.archive.like')"><i class="bi bi-heart"></i>좋아하는 콘텐츠</a>-->
          <!--                </li>-->
          <!--                <li>-->
          <!--                  <a href="#" v-text="$t('feed.responsive-mobile.archive.bookmark')"><i class="bi bi-bookmark"></i>저장한 콘텐츠</a>-->
          <!--                </li>-->
          <!--                <li>-->
          <!--                  <a href="#" v-text="$t('feed.responsive-mobile.archive.hide')"><i class="bi bi-eye-slash"></i>제외 콘텐츠</a>-->
          <!--                </li>-->
          <!--              </ul>-->
          <!--            </div>-->
          <!--          </div>-->
          <!--          <div class="panel-section">-->
          <!--            <div class="section-header">-->
          <!--              <strong>Support</strong>-->
          <!--            </div>-->
          <!--            <div class="section-body">-->
          <!--              <ul class="sidebar-nav-list">-->
          <!--                <li>-->
          <!--                  <a href="#" v-text="$t('feed.responsive-mobile.support.announcement')"><i class="bi bi-clipboard"></i>공지사항</a>-->
          <!--                </li>-->
          <!--                <li>-->
          <!--                  <a href="#" v-text="$t('feed.responsive-mobile.support.faq')"><i class="bi bi-question-circle"></i>자주하는 질문</a>-->
          <!--                </li>-->
          <!--                <li>-->
          <!--                  <a href="#" v-text="$t('feed.responsive-mobile.support.user-guide')"><i class="bi bi-info-circle"></i>이용안내</a>-->
          <!--                </li>-->
          <!--                <li>-->
          <!--                  <a href="#" v-text="$t('feed.responsive-mobile.support.inquiry')"><i class="bi bi-envelope"></i>1:1 문의</a>-->
          <!--                </li>-->
          <!--              </ul>-->
          <!--            </div>-->
          <!--          </div>-->
          <!--          <div class="panel-section">-->
          <!--            <div class="section-header">-->
          <!--              <strong>My Page</strong>-->
          <!--            </div>-->
          <!--            <div class="section-body">-->
          <!--              <ul class="sidebar-nav-list">-->
          <!--                <li>-->
          <!--                  <a href="#" v-text="$t('feed.responsive-mobile.my-page.membership')"><i class="bi bi-card-list"></i>나의 멤버십</a>-->
          <!--                </li>-->
          <!--                <li>-->
          <!--                  <a href="#" v-text="$t('feed.responsive-mobile.my-page.connectome-management')"><i class="bi bi-gear"></i>커넥톰 관리</a>-->
          <!--                </li>-->
          <!--                <li>-->
          <!--                  <a href="#" v-text="$t('feed.responsive-mobile.my-page.payment-history')"><i class="bi bi-credit-card"></i>결제내역</a>-->
          <!--                </li>-->
          <!--                <li>-->
          <!--                  <a href="#" v-text="$t('feed.responsive-mobile.my-page.inquiries')"><i class="bi bi-card-list"></i>문의내역</a>-->
          <!--                </li>-->
          <!--                <li>-->
          <!--                  <a href="#" v-text="$t('feed.responsive-mobile.my-page.user-account')"-->
          <!--                    ><i class="bi bi-person-lines-fill"></i>사용자 계정</a-->
          <!--                  >-->
          <!--                </li>-->
          <!--              </ul>-->
          <!--            </div>-->
          <!--          </div>-->
          <div class="social">
            <a href="#"><img src="content/images/sns-facebook-light.png" alt="" /></a>
            <a href="#"><img src="content/images/sns-instagram-light.png" alt="" /></a>
            <a href="#"><img src="content/images/sns-youtube-light.png" alt="" /></a>
          </div>
          <div class="logout">
            <a @click="logout">
              <i class="icon-logout"></i>
              <span>Logout</span>
            </a>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./ds-sidebar.component.ts"></script>
