<template>
  <!-- [Trend People] -->
  <div class="ds-card">
    <div class="ds-card-body">
      <!--      <div class="badge">Trend People</div>-->
      <div class="people-wrap">
        <a :class="peopleTrend.type === 'PEOPLE' ? 'img-people' : 'img-company'">
          <img :src="peopleTrend.imageUrl" :alt="peopleTrend.label" @error="peopleTrend.imageUrl = 'content/images/avatar.png'" />
        </a>
        <p class="title">
          {{ peopleTrend.label }}
        </p>
        <div class="social-area" v-if="peopleTrend.social">
          <a class="social-link" v-if="peopleTrend.social.facebook" :href="peopleTrend.social.facebook" title="Facebook" target="_blank">
            <img src="content/images/common/media/social-facebook.png" />
            <span class="tooltip">Facebook</span>
          </a>
          <a class="social-link" v-if="peopleTrend.social.twitter" :href="peopleTrend.social.twitter" title="Twitter" target="_blank">
            <img src="content/images/common/media/social-twitter.png" />
            <span class="tooltip">Twitter</span>
          </a>
          <a class="social-link" v-if="peopleTrend.social.youtube" :href="peopleTrend.social.youtube" title="Youtube" target="_blank">
            <img src="content/images/common/media/social-youtube.png" />
            <span class="tooltip">Youtube</span>
          </a>
          <a class="social-link" v-if="peopleTrend.social.instagram" :href="peopleTrend.social.instagram" title="Instagram" target="_blank">
            <img src="content/images/common/media/social-instagram.png" />
            <span class="tooltip">Instagram</span>
          </a>
          <a class="social-link" v-if="peopleTrend.social.linkedin" :href="peopleTrend.social.linkedin" title="Linkedin" target="_blank">
            <img src="content/images/common/media/social-linkedin.png" />
            <span class="tooltip">Linkedin</span>
          </a>
        </div>
      </div>
      <div class="desc">
        {{ peopleTrend.wikiText }}
      </div>
      <div class="desc-list" v-if="peopleTrend.type === 'PEOPLE'">
        <div v-if="peopleTrend.googleInfobox.subtitle"><strong>Subtitle</strong>{{ peopleTrend.googleInfobox.subtitle | lmTo(45) }}</div>
        <div v-if="peopleTrend.googleInfobox.born"><strong>Date of birth</strong>{{ peopleTrend.googleInfobox.born | lmTo(45) }}</div>
        <!--        <div v-if="peopleTrend.googleInfobox.education"><strong>Education</strong>{{ peopleTrend.googleInfobox.education | lmTo(30) }}</div>-->
      </div>
      <div class="desc-list" v-if="peopleTrend.type === 'COMPANY' && peopleTrend.googleInfobox">
        <div v-if="peopleTrend.googleInfobox.headquarters">
          <strong>Headquarters</strong>{{ peopleTrend.googleInfobox.headquarters | lmTo(45) }}
        </div>
        <div v-if="peopleTrend.googleInfobox.founders"><strong>Founders</strong>{{ peopleTrend.googleInfobox.founders | lmTo(45) }}</div>
        <!--        <div v-if="peopleTrend.googleInfobox.revenue"><strong>Revenue</strong>{{ peopleTrend.googleInfobox.revenue | lmTo(45) }}</div>-->
        <!--        <div v-else-if="peopleTrend.googleInfobox.founded"><strong>Founded</strong>{{ peopleTrend.googleInfobox.founded | lmTo(40) }}</div>-->
      </div>
      <div class="info">
        <span class="link">
          <a :href="peopleTrend.wikiUrl">View 'Source'<i class="icon-common icon-chevron-right"></i></a>
        </span>
      </div>
      <div v-swiper:mySwiperTrendPeople="swiperOptions" class="swiper swiper-tag-inline" v-if="peopleTrend.googleInfobox">
        <div class="swiper-wrapper">
          <div class="swiper-slide" v-for="(item, index) of peopleTrend.googleInfobox.peopleAlsoSearchFor" :key="'peopleTrend' + index">
            <a>{{ item }}</a>
          </div>
        </div>
      </div>
    </div>
    <div class="ds-card-footer justify-content-center">
      <!--      <div class="shortcut">-->
      <!--        <button type="button" class="btn btn-sm dropdown-toggle" data-toggle="dropdown" aria-expanded="false">-->
      <!--          Shortcuts<i class="icon-common icon-chevron-down"></i>-->
      <!--        </button>-->
      <!--        <div class="dropdown-menu">-->
      <!--          <span class="dropdown-item-text"-->
      <!--            ><strong>'{{ peopleTrend.label }}'</strong></span-->
      <!--          >-->
      <!--          <a class="dropdown-item" href="#" @click.prevent="searchInFeed(peopleTrend.label)"-->
      <!--            >Feed<i class="icon-common icon-chevron-right"></i-->
      <!--          ></a>-->
      <!--          &lt;!&ndash;          <a class="dropdown-item" href="#">People 바로 가기<i class="icon-common icon-chevron-right"></i></a>&ndash;&gt;-->
      <!--          <a class="dropdown-item" href="#" @click.prevent="searchInMyAI(peopleTrend.label)"-->
      <!--            >Search<i class="icon-common icon-chevron-right"></i-->
      <!--          ></a>-->
      <!--        </div>-->
      <!--      </div>-->
      <div class="wrap btn-footer">
        <div class="btn-area">
          <button type="button" class="btn btn-icon" @click.prevent="searchInFeed(peopleTrend.label)">
            <i class="icon-common icon-feed"></i>
            <span class="tooltip" v-text="$t('map-side-bar.details-tab.keywords-linked')"></span>
          </button>
          <button v-if="!isLinkToPeopleHidden" type="button" :class="peopleTabToggleClass" @click="goToPeople(peopleTrend.label)">
            <i class="icon-common icon-people"></i>
            <span class="tooltip" v-text="peopleTabToggleToolTipText"></span>
          </button>
          <button type="button" :class="connectomeToggleClass" @click="goToConnectome(peopleTrend.label)">
            <i class="icon-common icon-connectome"></i>
            <span class="tooltip" v-text="connectomeToggleToolTipText"></span>
          </button>
          <button type="button" class="btn btn-icon" @click.prevent="searchInMyAI(peopleTrend.label)">
            <i class="icon-common icon-learning"></i>
            <span class="tooltip" v-text="$t('my-ai-side-bar.tooltip.learning-center')"></span>
          </button>
          <button
            type="button"
            id="map-side-bar-set-as-favorite-toggle"
            :class="favoriteToggleClass"
            @click.prevent="onSetAsFavoriteClick(peopleTrend.label)"
            aria-pressed="true"
          >
            <i class="icon-common icon-favorites"></i>
            <span class="tooltip" v-text="favoriteToggleToolTipText"></span>
          </button>
          <button
            type="button"
            id="map-side-bar-set-as-favorite-toggles"
            :class="disableToggleClass"
            @click.prevent="onHideClick(peopleTrend)"
            v-b-tooltip.hover.bottomright="disableToggleToolTipText"
            aria-pressed="true"
          >
            <i class="icon-common icon-visibility"></i>
          </button>
        </div>
      </div>
    </div>
  </div>
</template>
<script lang="ts" src="./ds-card-trend-people.component.ts"></script>
<style scoped>
.btn-footer {
  display: flex;
  justify-content: center;
}
.desc-list {
  text-overflow: unset !important;
  display: block !important;
}
</style>
