<template>
  <div id="footer-block">
    <footer>
      <div class="container">
        <div class="desktop">
          <nav class="navbar navbar-footer">
            <div class="footer-nav">
              <ul class="nav nav-footer">
                <li class="nav-item">
                  <a class="nav-link" href="#" v-text="$t('footerGlobal.term-of-use')">이용약관</a>
                </li>
                <li class="nav-item">
                  <a class="nav-link" href="#" v-text="$t('footerGlobal.privacy-policy')"><strong>개인정보처리방침</strong></a>
                </li>
                <li class="nav-item">
                  <a
                    class="nav-link navbar-toggler collapsed"
                    data-toggle="collapse"
                    data-target="#footer-company-info"
                    v-text="$t('footerGlobal.business-information')"
                    >DeepSignal 사업자 정보</a
                  >
                </li>
              </ul>
            </div>
            <div class="copyright">
              <p>ⓒ <a href="#" target="_blank">DeepSignal</a>.</p>
            </div>
          </nav>
          <div class="company-info collapse navbar-collapse" id="footer-company-info">
            <div class="company-info-inner">
              <div class="group">
                <span><strong>DeepSignal</strong></span>
                <span v-text="$t('footerGlobal.business-location')">사업장 소재지 : 서울시 강남구 언주로 538 대웅빌딩 3층~5층</span>
                <span v-text="$t('footerGlobal.ceo')">대표이사 : 이경일</span>
                <span v-text="$t('footerGlobal.registration')">사업자등록번호 : 102-81-13061</span>
              </div>
              <div class="group">
                <span v-text="$t('footerGlobal.email')">이메일 : marketing@saltlux.com</span>
                <span v-text="$t('footerGlobal.fax')">팩스 : 02-3402-0082</span>
                <span v-text="$t('footerGlobal.customer-center')">고객센터 : 02-2193-1600</span>
              </div>
            </div>
          </div>
        </div>
        <div class="mobile">
          <nav class="nav nav-gnb">
            <router-link to="/feed" v-slot="{ href, navigate, isActive, isExactActive }" custom>
              <a :href="href" @click="navigate" :class="[isActive ? 'nav-link active' : 'nav-link', isExactActive && 'nav-link active']">
                <!--                <span class="tooltip" v-text="$t('global.menu.feed')">Feed</span>-->
                <i class="icon-mainmenu icon-feed"></i>
                <span class="text" v-text="$t('global.menu.feed')">Feed</span>
              </a>
            </router-link>
            <router-link to="/people" v-slot="{ href, navigate, isActive, isExactActive }" custom>
              <a :href="href" @click="navigate" :class="[isActive ? 'nav-link active' : 'nav-link', isExactActive && 'nav-link active']">
                <!--                <span class="tooltip" v-text="$t('global.menu.people')">People</span>-->
                <i class="icon-mainmenu icon-people"></i>
                <span class="text" v-text="$t('global.menu.people')">People</span>
              </a>
            </router-link>
            <router-link to="/signals" v-slot="{ href, navigate, isActive, isExactActive }" custom>
              <a :href="href" @click="navigate" :class="[isActive ? 'nav-link active' : 'nav-link', isExactActive && 'nav-link active']">
                <!--                <span class="tooltip">Signal</span>-->
                <i class="icon-mainmenu icon-signal"></i>
                <span class="text" v-text="$t('global.menu.signal')">Signal</span>
              </a>
            </router-link>
            <a @click="showUserManagement()" v-if="hasAnyAuthority('ROLE_ADMIN')" style="text-align: center">
              <div>
                <font-awesome-icon icon="users-cog" style="color: #a5a5a5" />
              </div>
              <div style="color: #97a4af; font-size: 11px; margin-bottom: 1px">Admin</div>
            </a>
            <a class="nav-link mobile-lnb-layer-btn" @click="showMobileLayer()">
              <div class="animation-myai"></div>
              <span class="text">My AI</span>
            </a>
          </nav>
        </div>
        <div class="mobile-lnb-layer" :class="{ 'lnb-show': isMobile }">
          <nav class="nav nav-lnb">
            <router-link to="/my-ai/learning-center" v-slot="{ isActive, isExactActive }" custom>
              <a
                @click="changeTab('/my-ai/learning-center')"
                :class="[isActive ? 'nav-link active' : 'nav-link', isExactActive && 'nav-link active']"
              >
                <!--                <span class="tooltip" v-text="$t('global.menu.feed')">Feed</span>-->
                <i class="icon-mainmenu icon-learning"></i>
                <span class="text" v-text="$t('global.menu.learningCenter')">LearningCenter</span>
              </a>
            </router-link>
            <router-link to="/my-ai/connectome/2dnetwork" v-slot="{ isActive, isExactActive }" custom>
              <a
                @click="changeTab('/my-ai/connectome/2dnetwork')"
                :class="[isActive ? 'nav-link active' : 'nav-link', isExactActive && 'nav-link active']"
              >
                <!--                <span class="tooltip" v-text="$t('global.menu.feed')">Feed</span>-->
                <i class="icon-mainmenu icon-connectome"></i>
                <span class="text" v-text="$t('global.menu.connectomes.connectome')">Connectome</span>
              </a>
            </router-link>
          </nav>
        </div>
        <div class="mobile-lnb-layer" :class="{ 'lnb-show': isUserManagement }">
          <nav class="nav nav-lnb">
            <router-link to="/admin/user-management" v-slot="{ isActive, isExactActive }" custom>
              <a
                @click="changeTab('/admin/user-management')"
                :class="[isActive ? 'nav-link active' : 'nav-link', isExactActive && 'nav-link active']"
              >
                <font-awesome-icon icon="users" style="color: #a5a5a5; margin-right: 0.5rem; margin-left: 0.3rem" />
                <span v-text="$t('global.menu.admin.userManagement')" class="text">User management</span>
              </a>
            </router-link>
            <router-link to="/admin/metrics" v-slot="{ isActive, isExactActive }" custom>
              <a
                @click="changeTab('/admin/metrics')"
                :class="[isActive ? 'nav-link active' : 'nav-link', isExactActive && 'nav-link active']"
              >
                <font-awesome-icon icon="tachometer-alt" style="color: #a5a5a5; margin-right: 0.5rem; margin-left: 0.3rem" />
                <span v-text="$t('global.menu.admin.metrics')">Metrics</span>
              </a>
            </router-link>
            <router-link to="/admin/health" v-slot="{ isActive, isExactActive }" custom>
              <a
                @click="changeTab('/admin/health')"
                :class="[isActive ? 'nav-link active' : 'nav-link', isExactActive && 'nav-link active']"
              >
                <font-awesome-icon icon="heart" style="color: #a5a5a5; margin-right: 0.5rem; margin-left: 0.3rem" />
                <span v-text="$t('global.menu.admin.health')">Health</span>
              </a>
            </router-link>
            <router-link to="/admin/configuration" v-slot="{ isActive, isExactActive }" custom>
              <a
                @click="changeTab('/admin/configuration')"
                :class="[isActive ? 'nav-link active' : 'nav-link', isExactActive && 'nav-link active']"
              >
                <font-awesome-icon icon="cogs" style="color: #a5a5a5; margin-right: 0.5rem; margin-left: 0.3rem" />
                <span v-text="$t('global.menu.admin.configuration')">Configuration</span>
              </a>
            </router-link>
            <router-link to="/admin/logs" v-slot="{ isActive, isExactActive }" custom>
              <a @click="changeTab('/admin/logs')" :class="[isActive ? 'nav-link active' : 'nav-link', isExactActive && 'nav-link active']">
                <font-awesome-icon icon="tasks" style="color: #a5a5a5; margin-right: 0.5rem; margin-left: 0.3rem" />
                <span v-text="$t('global.menu.admin.logs')">Logs</span>
              </a>
            </router-link>
            <router-link to="/admin/docs" v-slot="{ isActive, isExactActive }" custom>
              <a @click="changeTab('/admin/docs')" :class="[isActive ? 'nav-link active' : 'nav-link', isExactActive && 'nav-link active']">
                <font-awesome-icon icon="book" style="color: #a5a5a5; margin-right: 0.5rem; margin-left: 0.3rem" />
                <span v-text="$t('global.menu.admin.apidocs')">API</span>
              </a>
            </router-link>
          </nav>
        </div>
      </div>
    </footer>

    <div class="floating-btns" style="z-index: 2" v-if="isShow">
      <a class="btn-scroll-top" @click="scrollToTop">
        <span class="icon"></span>
      </a>
    </div>
  </div>
</template>

<script lang="ts" src="./ds-footer.component.ts"></script>
