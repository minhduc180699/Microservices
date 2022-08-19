<template>
  <div class="ds-card" data-template="company">
    <div class="ds-card-body">
      <!--      <div class="badge">{{item.type === "PEOPLE" ? "Rising People" : "Rising Company"}}</div>-->
      <div class="people-wrap">
        <a :class="item.type === 'PEOPLE' ? 'img-people' : 'img-company'">
          <img v-if="item.imageUrl" :src="item.imageUrl" :alt="item.label" @error="item.imageUrl = 'content/images/avatar.png'" />
          <img v-else src="content/images/avatar.png" />
        </a>
        <p class="title" style="height: 23px; overflow: hidden">
          {{ item.label }}
        </p>
        <div class="social-area" v-if="item.social">
          <a class="social-link" v-if="item.social.facebook" :href="item.social.facebook" title="Facebook" target="_blank">
            <img src="content/images/common/media/social-facebook.png" />
            <span class="tooltip">Facebook</span>
          </a>
          <a class="social-link" v-if="item.social.twitter" :href="item.social.twitter" title="Twitter" target="_blank">
            <img src="content/images/common/media/social-twitter.png" />
            <span class="tooltip">Twitter</span>
          </a>
          <a class="social-link" v-if="item.social.youtube" :href="item.social.youtube" title="Youtube" target="_blank">
            <img src="content/images/common/media/social-youtube.png" />
            <span class="tooltip">Youtube</span>
          </a>
          <a class="social-link" v-if="item.social.instagram" :href="item.social.instagram" title="Instagram" target="_blank">
            <img src="content/images/common/media/social-instagram.png" />
            <span class="tooltip">Instagram</span>
          </a>
          <a class="social-link" v-if="item.social.linkedin" :href="item.social.linkedin" title="Linkedin" target="_blank">
            <img src="content/images/common/media/social-linkedin.png" />
            <span class="tooltip">Linkedin</span>
          </a>
        </div>
        <div class="social-area" v-else style="min-height: 44px"></div>
      </div>
    </div>
    <div class="ds-card-footer justify-content-center">
      <div class="btn-area">
        <button type="button" class="btn btn-icon" @click.prevent="searchInFeed(item.label)">
          <i class="icon-common icon-feed"></i>
          <span class="tooltip" v-text="$t('map-side-bar.details-tab.go-to-feed')"></span>
        </button>
        <button type="button" :class="peopleTabToggleClass" @click="goToPeople(item.label)">
          <i class="icon-common icon-people"></i>
          <span class="tooltip" v-text="peopleTabToggleToolTipText"></span>
        </button>
        <button type="button" :class="connectomeToggleClass" @click="goToConnectome(item.label)">
          <i class="icon-common icon-connectome"></i>
          <span class="tooltip" v-text="connectomeToggleToolTipText"></span>
        </button>
        <button type="button" class="btn btn-icon" @click.prevent="searchInMyAI(item.label)">
          <i class="icon-common icon-learning"></i>
          <span class="tooltip" v-text="$t('map-side-bar.details-tab.go-to-learning')"></span>
        </button>
        <button
          type="button"
          :class="favoriteToggleClass"
          data-toggle="button"
          @click.prevent="onSetAsFavoriteClick(item.label)"
          aria-pressed="true"
        >
          <i class="icon-common icon-favorites"></i>
          <span class="tooltip" v-text="favoriteToggleToolTipText"></span>
        </button>
        <button
          type="button"
          id="map-side-bar-set-as-favorite-toggle"
          :class="disableToggleClass"
          @click.prevent="onHideClick(item.label)"
          aria-pressed="true"
          style="margin-left: 0.25rem"
        >
          <i class="icon-common icon-visibility"></i>
          <span class="tooltip" v-text="disableToggleToolTipText"></span>
        </button>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./trend-people-rising.component.ts"></script>
