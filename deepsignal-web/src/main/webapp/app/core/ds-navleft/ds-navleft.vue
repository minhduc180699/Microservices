<template>
  <b-sidebar id="sidebar-nav-left" title="Sidebar" no-header backdrop-variant="darkgray" backdrop shadow width="640px">
    <div class="sitemap-layer">
      <!--      <div class="dim"></div>-->
      <div class="layer-wrap">
        <!--        <button v-b-toggle.sidebar-nav-left type="button" class="layer-btn-close" aria-label="사이트맵 닫기" id="js-btn-sitemap-close">-->
        <!--          <i class="icon-close-white"></i>-->
        <!--        </button>-->
        <div class="layer-top">
          <div class="row align-items-center">
            <div class="col">
              <div class="logo"><span class="sr-only">DeepSignal</span></div>
            </div>
            <div class="col-auto">
              <!--              <button v-b-modal.modal-user-info type="button" aria-label="설정" class="btn-setting"><i class="icon-gear"></i></button>-->
              <button type="button" aria-label="설정" class="btn-setting" v-b-modal.modal-user-info><i class="icon-gear"></i></button>
              <button
                type="button"
                class="layer-btn-close"
                aria-label="사이트맵 닫기"
                id="js-btn-sitemap-close"
                v-b-toggle.sidebar-nav-left
              >
                <i class="icon-close"></i>
              </button>
            </div>
          </div>
        </div>
        <div class="layer-body">
          <div class="connectome-list row row-20 align-items-center">
            <div class="col">
              <div class="d-inline-flex align-items-center">
                <div class="bot">
                  <i class="icon-bot"></i>
                </div>
                <div>
                  <p class="hello">Hello!</p>
                  <strong class="ai" v-if="connectome != null"
                    >I’m <span class="text-primary">{{ connectome.connectomeName }}</span></strong
                  >
                </div>
              </div>
            </div>
            <!--<div class="col-auto">
              <div class="others">
                <a href=""><img src="content/images/@profile-travel.png" alt="" /></a>
                <a href=""><img src="content/images/@profile-car.png" alt="" /></a>
                <a href="" class="add" aria-label="추가"><i class="icon-plus-gray"></i></a>
              </div>
            </div>-->
          </div>
          <nav class="gnb">
            <ul>
              <li>
                <router-link to="/feed" v-slot="{ href, navigate }" custom>
                  <a :href="href" @click="navigate"><span v-text="$t('global.menu.feed')">Feed</span><i class="icon-new">N</i></a>
                </router-link>
              </li>
              <li>
                <router-link to="/people" v-slot="{ href, navigate }" custom>
                  <a :href="href" @click="navigate"><span v-text="$t('global.menu.people')">People</span></a>
                </router-link>
              </li>
              <!--              <li>-->
              <!--                <a href=""><span v-text="$t('global.menu.signal')">Signal</span></a>-->
              <!--              </li>-->
              <li class="mt-4">
                <router-link v-if="connectome" to="/my-ai" v-slot="{ href, navigate }" custom>
                  <a :href="href" @click="navigate"><span v-text="$t('global.menu.connectomes.connectome')">My Connectome</span></a>
                </router-link>
              </li>
              <!--              <li>-->
              <!--                <a href=""><span v-text="$t('global.menu.learningCenter')">Learning Center</span></a>-->
              <!--              </li>-->
              <!--              <li>-->
              <!--                <a href=""><span v-text="$t('global.menu.archives.archive')">Archive</span></a>-->
              <!--              </li>-->
            </ul>
          </nav>
          <nav class="sub-menu">
            <strong>Support</strong>
            <ul>
              <li>
                <router-link to="/faq">FAQ</router-link>
              </li>
              <!--              <li><a href="" v-text="$t('nav-left.notice')">공지사항</a></li>-->
              <!--              <li><a href="" v-text="$t('nav-left.information')">이용안내</a></li>-->
              <li>
                <router-link to="/qna">1:1 {{ $t('nav-left.question') }}</router-link>
              </li>
            </ul>
            <!--            <strong>My Page</strong>-->
            <!--            <ul>-->
            <!--              <li><a href="" v-text="$t('nav-left.my-membership')">나의 멤버십</a></li>-->
            <!--              <li><a href="" v-text="$t('nav-left.connectome-management')">커넥톰 관리</a></li>-->
            <!--              <li><a href="" v-text="$t('nav-left.payment-history')">결제내역</a></li>-->
            <!--              <li><a href="" v-text="$t('nav-left.inquiries')">문의내역</a></li>-->
            <!--              <li><a href="" v-text="$t('nav-left.user-account-info')">사용자 계정 정보</a></li>-->
            <!--            </ul>-->
          </nav>
          <div class="social">
            <a href=""><img src="content/images/sns-facebook-light.png" alt="" /></a>
            <a href=""><img src="content/images/sns-instagram-light.png" alt="" /></a>
            <a href=""><img src="content/images/sns-youtube-light.png" alt="" /></a>
          </div>
          <div class="logout">
            <a href="" @click.prevent="logout">
              <i class="icon-logout"></i>
              <span>Logout</span>
            </a>
          </div>
        </div>
      </div>
    </div>
  </b-sidebar>
</template>

<script lang="ts" src="./ds-navleft.component.ts"></script>
<style lang="scss" scoped>
.sitemap-layer {
  visibility: visible;
  opacity: 1;
}
</style>
<style lang="scss">
#app > div.b-sidebar-outer > div.b-sidebar-backdrop {
  opacity: 1;
}
</style>
