<template>
  <header v-show="authenticated">
    <nav class="navbar navbar-main">
      <div class="desktop">
        <div class="container-fluid">
          <div class="wrap">
            <a class="logo" href="/feed"></a>
          </div>
          <ul class="nav nav-gnb">
            <li class="nav-item">
              <router-link to="/feed" v-slot="{ href, navigate, isActive, isExactActive }" custom>
                <a
                  :href="href"
                  @click="navigate"
                  :class="[isActive || isDetailFeed ? 'nav-link active' : 'nav-link', isExactActive && 'nav-link active']"
                >
                  <i class="icon-mainmenu icon-feed"></i>
                  <span class="text" v-text="$t('global.menu.feed')">Feed</span>
                </a>
              </router-link>
            </li>
            <li class="nav-item">
              <router-link to="/people" v-slot="{ href, navigate, isActive, isExactActive }" custom>
                <a :href="href" @click="navigate" :class="[isActive ? 'nav-link active' : 'nav-link', isExactActive && 'active']">
                  <i class="icon-mainmenu icon-people"></i>
                  <span class="text" v-text="$t('global.menu.people')">People</span>
                </a>
              </router-link>
            </li>
            <li class="nav-item">
              <router-link to="/signals" v-slot="{ href, navigate, isActive, isExactActive }" custom>
                <a :href="href" @click="navigate" :class="[isActive ? 'nav-link active' : 'nav-link', isExactActive && 'active']">
                  <i class="icon-mainmenu icon-signal"></i>
                  <span class="text" v-text="$t('global.menu.signal')">Signal</span>
                </a>
              </router-link>
            </li>
          </ul>
          <div class="wrap">
            <ul class="nav nav-util">
              <li class="nav-item" v-if="hasAnyAuthority('ROLE_ADMIN')">
                <a class="nav-link link-notice new" data-toggle="dropdown">
                  <a style="text-align: center">
                    <div>
                      <font-awesome-icon icon="users-cog" style="color: #a5a5a5; height: auto; width: 20px" />
                    </div>
                    <span class="tooltip">Administration</span>
                  </a>
                </a>
                <div class="dropdown-menu">
                  <div class="util-content lnb-content">
                    <nav class="nav nav-lnb">
                      <router-link to="/admin/user-management" v-slot="{ isActive, isExactActive }" custom>
                        <a
                          @click="changeTab('/admin/user-management')"
                          :class="[isActive ? 'nav-link active' : 'nav-link', isExactActive && 'nav-link active']"
                        >
                          <font-awesome-icon
                            icon="users"
                            style="margin-right: 0.5rem; margin-left: 0.3rem"
                            :style="isActive ? { color: '#0097f6' } : { color: '#a5a5a5' }"
                          />
                          <span v-text="$t('global.menu.admin.userManagement')" class="text">User management</span>
                        </a>
                      </router-link>
                      <router-link to="/admin/metrics" v-slot="{ isActive, isExactActive }" custom>
                        <a
                          @click="changeTab('/admin/metrics')"
                          :class="[isActive ? 'nav-link active' : 'nav-link', isExactActive && 'nav-link active']"
                        >
                          <font-awesome-icon
                            icon="tachometer-alt"
                            style="margin-right: 0.5rem; margin-left: 0.3rem"
                            :style="isActive ? { color: '#0097f6' } : { color: '#a5a5a5' }"
                          />
                          <span v-text="$t('global.menu.admin.metrics')">Metrics</span>
                        </a>
                      </router-link>
                      <router-link to="/admin/health" v-slot="{ isActive, isExactActive }" custom>
                        <a
                          @click="changeTab('/admin/health')"
                          :class="[isActive ? 'nav-link active' : 'nav-link', isExactActive && 'nav-link active']"
                        >
                          <font-awesome-icon
                            icon="heart"
                            style="margin-right: 0.5rem; margin-left: 0.3rem"
                            :style="isActive ? { color: '#0097f6' } : { color: '#a5a5a5' }"
                          />
                          <span v-text="$t('global.menu.admin.health')">Health</span>
                        </a>
                      </router-link>
                      <router-link to="/admin/configuration" v-slot="{ isActive, isExactActive }" custom>
                        <a
                          @click="changeTab('/admin/configuration')"
                          :class="[isActive ? 'nav-link active' : 'nav-link', isExactActive && 'nav-link active']"
                        >
                          <font-awesome-icon
                            icon="cogs"
                            style="margin-right: 0.5rem; margin-left: 0.3rem"
                            :style="isActive ? { color: '#0097f6' } : { color: '#a5a5a5' }"
                          />
                          <span v-text="$t('global.menu.admin.configuration')">Configuration</span>
                        </a>
                      </router-link>
                      <router-link to="/admin/logs" v-slot="{ isActive, isExactActive }" custom>
                        <a
                          @click="changeTab('/admin/logs')"
                          :class="[isActive ? 'nav-link active' : 'nav-link', isExactActive && 'nav-link active']"
                        >
                          <font-awesome-icon
                            icon="tasks"
                            style="margin-right: 0.5rem; margin-left: 0.3rem"
                            :style="isActive ? { color: '#0097f6' } : { color: '#a5a5a5' }"
                          />
                          <span v-text="$t('global.menu.admin.logs')">Logs</span>
                        </a>
                      </router-link>
                      <router-link to="/admin/docs" v-slot="{ isActive, isExactActive }" custom>
                        <a
                          @click="changeTab('/admin/docs')"
                          :class="[isActive ? 'nav-link active' : 'nav-link', isExactActive && 'nav-link active']"
                        >
                          <font-awesome-icon
                            icon="book"
                            style="margin-right: 0.5rem; margin-left: 0.3rem"
                            :style="isActive ? { color: '#0097f6' } : { color: '#a5a5a5' }"
                          />
                          <span v-text="$t('global.menu.admin.apidocs')">API</span>
                        </a>
                      </router-link>
                      <a class="nav-link heatmap">
                        <font-awesome-icon icon="user" style="margin-right: 0.5rem; margin-left: 0.3rem; color: #a5a5a5" />
                        <span v-text="$t('global.menu.admin.heatmap')">Heatmap</span>
                        <VueToggles
                          height="20"
                          width="60"
                          style="position: absolute; right: 15px"
                          checkedText="On"
                          uncheckedText="Off"
                          checkedBg="#34baeb"
                          uncheckedBg="lightgrey"
                          :value="isShowHeatMap"
                          @click="changeIsShowHeatMap"
                        />
                      </a>
                    </nav>
                  </div>
                </div>
              </li>
              <li class="nav-item">
                <a class="nav-link link-notice new" data-toggle="dropdown">
                  <div class="animation-myai"></div>
                  <span class="tooltip">My AI</span>
                </a>
                <div class="dropdown-menu">
                  <div class="util-content lnb-content">
                    <nav class="nav nav-lnb">
                      <router-link to="/my-ai/learning-center" v-slot="{ isActive, isExactActive }" custom>
                        <a
                          @click="changeTab('/my-ai/learning-center')"
                          :class="[isActive ? 'nav-link active' : 'nav-link', isExactActive && 'nav-link active']"
                        >
                          <i class="icon-mainmenu icon-learning"></i>
                          <span class="text" v-text="$t('global.menu.learningCenter')">LearningCenter</span>
                        </a>
                      </router-link>
                      <router-link to="/my-ai/connectome/2dnetwork" v-slot="{ isActive, isExactActive }" custom>
                        <a
                          @click="changeTab('/my-ai/connectome/2dnetwork')"
                          :class="[isActive ? 'nav-link active' : 'nav-link', isExactActive && 'nav-link active']"
                        >
                          <i class="icon-mainmenu icon-connectome"></i>
                          <span class="text" v-text="$t('global.menu.connectomes.connectome')">Connectome</span>
                        </a>
                      </router-link>
                    </nav>
                  </div>
                </div>
              </li>
              <!--              <li class="nav-item">-->
              <!--                <a-->
              <!--                  class="nav-link link-notice new"-->
              <!--                  @click="openFavorite()"-->
              <!--                  data-toggle="dropdown"-->
              <!--                  v-b-tooltip.hover.bottom="$t('favorites.favorites')"-->
              <!--                >-->
              <!--                  <i class="icon-common-lg icon-favorites"></i>-->
              <!--                </a>-->
              <!--                <search-main></search-main>-->
              <!--              </li>-->

              <!--              <li class="nav-item">-->
              <!--                <a class="nav-link link-notice new" data-toggle="dropdown" @click="closeShowNotification">-->
              <!--                  <i class="icon-common-lg" :class="{ 'icon-noti-fill': isShowNotification }"></i>-->
              <!--                  <span class="tooltip" v-text="$t('global.menu.notification')">Notification</span>-->
              <!--                </a>-->
              <!--                <notification @newNotification="checkShowNotification"></notification>-->
              <!--              </li>-->
              <li class="nav-item">
                <a id="openUserMenu" class="nav-link link-my" @click="openUserMenu">
                  <div class="my-thumb">
                    <img
                      :src="avatar ? avatar : 'content/images/sample/avatar-default.png'"
                      @error="$event.target.src = 'content/images/sample/avatar-default.png'"
                      style="border-radius: 50%; height: 100%"
                      alt="avatar"
                    />
                  </div>
                </a>
                <div
                  ref="userMenu"
                  v-if="showUserMenu"
                  class="dropdown-menu show"
                  tabindex="0"
                  v-click-outside="closeUserMenu"
                  style="outline: none"
                >
                  <div class="util-content my-content">
                    <div class="my-info">
                      <div class="my-thumb">
                        <img
                          :src="avatar ? avatar : 'content/images/sample/avatar-default.png'"
                          @error="$event.target.src = 'content/images/sample/avatar-default.png'"
                          alt="avatar"
                          style="height: 100%"
                        />
                      </div>
                      <br />
                      <div v-if="connectome" class="my-name mb-1" style="padding-right: unset; display: flex; padding-left: 1rem">
                        <input
                          v-if="isEditing"
                          v-model="connectomeName"
                          type="text"
                          ref="editConnectomeBtn"
                          class="form-control ml-auto mr-auto max-width-150 max-height-30"
                          @keyup.enter="editConnectomeName()"
                        />
                        <span v-else style="margin: auto; text-overflow: ellipsis; overflow: hidden; white-space: nowrap">
                          {{ connectome.connectomeName }}
                        </span>
                        <button style="all: unset; cursor: pointer" @click="handleEditConnectomeName()">
                          <i :class="['bi', isEditing ? 'bi-x-circle' : 'bi-pencil']"></i>
                        </button>
                      </div>
                      <div class="my-id" v-if="connectome">
                        <div class="id-phone" v-if="connectome.user">
                          {{ connectome.user.phoneNumber ? connectome.user.phoneNumber : 'no phone' }}
                        </div>
                      </div>
                      <button
                        type="button"
                        class="btn btn-light btn-pill"
                        data-toggle="modal"
                        data-target="#staticBackdrop"
                        @click="openPopUpUserInfo"
                        v-text="$t('userMenu.userInfo')"
                      ></button>
                    </div>
                    <div class="btn-area">
                      <a class="btn btn-item" @click="openPopUpUserSetting">
                        {{ $t('userMenu.preferences') }} <i class="icon-common icon-setting"></i>
                      </a>
                      <a class="btn btn-item" @click="$bvModal.show('modal-alert-logout')">
                        {{ $t('userMenu.logout') }} <i class="icon-common icon-logout"></i>
                      </a>
                    </div>
                  </div>
                </div>
              </li>
            </ul>
          </div>
        </div>
      </div>
      <div class="mobile">
        <div class="container-fluid">
          <div class="wrap">
            <ul class="nav nav-util">
              <li class="nav-item">
                <a id="openUserMenuMobile" class="nav-link link-my" @click="openUserMenu">
                  <div class="my-thumb">
                    <img
                      :src="avatar ? avatar : 'content/images/sample/avatar-default.png'"
                      @error="$event.target.src = 'content/images/sample/avatar-default.png'"
                      style="border-radius: 50%; height: 100%"
                      alt="avatar"
                    />
                  </div>
                </a>
                <div
                  ref="userMenuMobile"
                  v-if="showUserMenu"
                  class="dropdown-menu show"
                  tabindex="0"
                  v-click-outside="closeUserMenu"
                  style="outline: none"
                >
                  <div class="util-content my-content">
                    <div class="my-info">
                      <div class="my-thumb">
                        <img
                          :src="avatar ? avatar : 'content/images/sample/avatar-default.png'"
                          @error="$event.target.src = 'content/images/sample/avatar-default.png'"
                          alt="avatar"
                          style="height: 100%"
                        />
                      </div>
                      <br />
                      <div v-if="connectome" class="my-name mb-1" style="padding-right: unset; display: flex; padding-left: 1rem">
                        <input
                          v-if="isEditing"
                          v-model="connectomeName"
                          type="text"
                          ref="editConnectomeBtn"
                          class="form-control ml-auto mr-auto max-width-150 max-height-30"
                          @keyup.enter="editConnectomeName()"
                        />
                        <span v-else style="margin: auto; text-overflow: ellipsis; overflow: hidden; white-space: nowrap">
                          {{ connectome.connectomeName }}
                        </span>
                        <button style="all: unset; cursor: pointer" @click="handleEditConnectomeName()">
                          <i :class="['bi', isEditing ? 'bi-x-circle' : 'bi-pencil']"></i>
                        </button>
                      </div>
                      <div class="my-id" v-if="connectome">
                        <div class="id-phone" v-if="connectome.user">
                          {{ connectome.user.phoneNumber ? connectome.user.phoneNumber : 'no phone' }}
                        </div>
                      </div>
                      <button
                        type="button"
                        class="btn btn-light btn-pill"
                        data-toggle="modal"
                        data-target="#staticBackdrop"
                        @click="openPopUpUserInfo"
                        v-text="$t('userMenu.userInfo')"
                      >
                        User Information
                      </button>
                    </div>
                    <div class="btn-area">
                      <a class="btn btn-item" @click="openPopUpUserSetting">
                        {{ $t('userMenu.preferences') }} <i class="icon-common icon-setting"></i>
                      </a>
                      <a class="btn btn-item" @click="$bvModal.show('modal-alert-logout')">
                        {{ $t('userMenu.logout') }} <i class="icon-common icon-logout"></i>
                      </a>
                    </div>
                  </div>
                </div>
              </li>
            </ul>
          </div>
          <div class="wrap">
            <h2 class="page-title">{{ page }}</h2>
          </div>

          <div class="wrap">
            <ul class="nav nav-util">
              <!--              <li class="nav-item">-->
              <!--                <a-->
              <!--                  class="nav-link link-notice new"-->
              <!--                  @click="openFavorite()"-->
              <!--                  data-toggle="dropdown"-->
              <!--                  v-b-tooltip.hover.top="$t('favorites.favorites')"-->
              <!--                >-->
              <!--                  <i class="icon-common-lg icon-favorites"></i>-->
              <!--                </a>-->
              <!--                <search-main></search-main>-->
              <!--              </li>-->

              <!--              <li class="nav-item">-->
              <!--                <a-->
              <!--                  class="nav-link link-notice new"-->
              <!--                  data-toggle="dropdown"-->
              <!--                  @click="closeShowNotification"-->
              <!--                  v-b-tooltip.hover.top="$t('alarmNotice.notification')"-->
              <!--                >-->
              <!--                  <i class="icon-common-lg icon-noti-fill"></i>-->
              <!--                </a>-->
              <!--                <notification @newNotification="checkShowNotification"></notification>-->
              <!--              </li>-->
            </ul>
          </div>
        </div>
      </div>
    </nav>

    <popup-user-info ref="userInfo" :avatar.sync="avatar" :isDeleteUser.sync="isDeleteUser" v-on:delete="logout()"></popup-user-info>

    <b-modal hide-footer hide-header centered id="modal-alert-logout">
      <div class="modal-content modal-alert">
        <div class="modal-header">
          <button type="button" class="close" @click="$bvModal.hide('modal-alert-logout')">
            <i class="icon-common icon-close"></i>
          </button>
        </div>
        <div class="modal-body">
          <div class="alert-content">
            <div>
              <span class="icon-circle"><i class="icon-common-lg icon-login"></i></span>
              <strong v-text="$t('userMenu.logoutTitle')">Are you sure you want to logout?</strong>
            </div>
            <P v-text="$t('userMenu.logoutText')">Placeholder text for this demonstration of a vertically centered modal dialog.</P>
          </div>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-default" @click="$bvModal.hide('modal-alert-logout')" v-text="$t('userMenu.close')">
            Close
          </button>
          <button type="button" class="btn btn-primary" @click="logout" v-text="$t('userMenu.logout')">Logout</button>
        </div>
      </div>
    </b-modal>

    <popup-user-setting></popup-user-setting>

    <!-- layer : 일일알림 -->
    <div class="ai-daily-layer" :class="[authenticated && isShowDaily && 'show']" v-if="false">
      <div class="layer-wrap">
        <button type="button" class="layer-btn-close" aria-label="Close" @click="isShowDaily = !isShowDaily">
          <i class="icon-close"></i>
        </button>
        <div class="layer-body">
          <div class="today">2021.09.06</div>
          <div class="hello">Hello, VW!</div>
          <div class="row align-items-center">
            <div class="col">
              <p class="ai">안녕하세요. <br /><strong>Warren</strong>입니다.</p>
            </div>
            <div class="col-auto">
              <div class="bot">
                <i class="icon-bot"></i>
              </div>
            </div>
          </div>
          <div class="today-news">
            <div class="tit">Today’s News</div>
            <div class="row row-15">
              <a href="#none" class="col">
                <dl>
                  <dt>
                    <i><img src="content/images/icon-menu-signal.png" alt="Signal" /></i>Signal
                  </dt>
                  <dd>42</dd>
                </dl>
              </a>
              <a href="#none" class="col">
                <dl>
                  <dt>
                    <i><img src="content/images/icon-menu-feed.png" alt="Feed" /></i>Feed
                  </dt>
                  <dd>42</dd>
                </dl>
              </a>
              <a href="#none" class="col">
                <dl>
                  <dt>
                    <i><img src="content/images/icon-menu-people.png" alt="People" /></i>People
                  </dt>
                  <dd>42</dd>
                </dl>
              </a>
            </div>
          </div>
          <div class="message">
            <p>학습소스를 추가해주세요.</p>
            <p>수집용량을 초과하였습니다.</p>
          </div>
          <div class="btn-more" :class="[isShowMore && 'active']">
            <button type="button" @click="isShowMore = !isShowMore"><i class="icon-more-active"></i></button>
            <div class="tooltip-menu">
              <router-link :to="{ name: 'MyAi' }" custom v-slot="{ href, navigate }">
                <a :href="href" @click="navigate">
                  <span class="icon-area"><img src="content/images/ai-icon-1.png" alt="" /></span> 제 이름은 Warren 입니다
                </a>
              </router-link>
              <a href="">
                <span class="icon-area"><img src="content/images/ai-icon-2.png" alt="" /></span> 최근 학습내역이 궁금하세요?
              </a>
              <a href="">
                <span class="icon-area"><img src="content/images/ai-icon-3.png" alt="" /></span> Warren의 활동 통계입니다
              </a>
              <a href="">
                <span class="icon-area"><img src="content/images/ai-icon-4.png" alt="" /></span> 저를 학습시켜주세요
              </a>
              <a href="">
                <span class="icon-area"><img src="content/images/ai-icon-5.png" alt="" /></span> 수집 용량이 부족하세요?
              </a>
            </div>
          </div>
        </div>
      </div>
    </div>
  </header>
</template>

<script lang="ts" src="./ds-navbar.component.ts"></script>
<style>
.lang .dropdown-toggle::after {
  display: none !important;
}

.my-layer,
.myinfo-layer,
.alarm-layer {
  visibility: visible !important;
  opacity: 1 !important;
  margin: 0 auto !important;
}

.myinfo-layer .layer-wrap {
  position: relative !important;
  width: 100% !important;
  top: auto !important;
  left: auto !important;
  transform: none !important;
}

#modal-alert-logout .modal-body,
#modal-member-setting .modal-body {
  padding: 0 !important;
}

#modal-alert-logout .modal-body .modal-body,
#modal-member-setting .modal-body .modal-body {
  padding: 0 1.5rem !important;
}

#modal-alert-logout,
#modal-member-setting {
  padding: 0 !important;
}

.my-layer .modal-body,
.myinfo-layer .modal-body,
.alarm-layer .modal-body {
  padding: 0 !important;
}

.modal .my-info .buttons {
  padding-top: 0;
  border: none;
}

.max-width460 {
  max-width: 460px !important;
}

.nav-tab li.active a {
  color: #000 !important;
  background: white !important;
  border-bottom: 3px solid #516eff;
}

.container-router > div {
  float: right;
}

#admin-menu {
  margin-right: 20px;
}
#admin-menu li ul {
  width: 215px;
}
</style>

<style lang="scss" scoped>
.number-notification {
  left: 20px;
  bottom: 20px;
  padding: 0 5px;
  border-radius: 25px;
  z-index: 1000;
  font-size: 12px;
  color: white;
}

.heatmap::before {
  content: none !important;
}
</style>
