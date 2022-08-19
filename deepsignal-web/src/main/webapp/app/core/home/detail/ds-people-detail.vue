<template>
  <div id="container-block">
    <div class="container">
      <div class="card-detail">
        <div class="detail-header dt-primary-nav">
          <button type="button" class="btn btn-icon">
            <i class="icon-common-lg icon-arrow-left" @click="goBack()"></i>
            <span class="tooltip" v-text="$t('people.detail.back-people')">이전 페이지</span>
          </button>
          <!--          <div class="wrap">-->
          <!--            <button type="button" class="btn btn-icon btn-like" data-toggle="button">-->
          <!--              <i class="icon-common icon-like"></i>-->
          <!--              <span class="tooltip">좋아요</span>-->
          <!--            </button>-->
          <!--            <button type="button" class="btn btn-icon btn-dislike" data-toggle="button">-->
          <!--              <i class="icon-common icon-dislike"></i>-->
          <!--              <span class="tooltip">싫어요</span>-->
          <!--            </button>-->
          <!--            <button type="button" class="btn btn-icon btn-bookmark" data-toggle="button">-->
          <!--              <i class="icon-common icon-bookmark"></i>-->
          <!--              <span class="tooltip">북마크</span>-->
          <!--            </button>-->
          <!--            <button type="button" class="btn btn-icon btn-share" data-toggle="button">-->
          <!--              <i class="icon-common icon-share"></i>-->
          <!--              <span class="tooltip">공유</span>-->
          <!--            </button>-->
          <!--            <div class="dropdown">-->
          <!--              <button type="button" class="btn btn-icon btn-visibility" data-toggle="dropdown">-->
          <!--                <i class="icon-common icon-more"></i>-->
          <!--              </button>-->
          <!--              <div class="dropdown-menu">-->
          <!--                <a class="dropdown-item" href="#"><i class="icon-common icon-visibility"></i>카드 숨기기</a>-->
          <!--              </div>-->
          <!--            </div>-->
          <!--          </div>-->
        </div>
        <div class="detail-body">
          <section class="detail-main detail-people">
            <div class="section-header">
              <h1 class="title" v-if="people">
                {{ people.label }}<small>{{ people.googleInfobox.subtitle }}</small>
              </h1>
              <b-skeleton width="50%" height="80%" v-else></b-skeleton>
              <div v-if="people && people.wikiUrl" class="info info-list">
                <!--                <span class="date">wikipedia</span>-->
                <!--                <span class="link">-->
                <!--                  <a :href="people.wikiUrl" target="_blank">-->
                <!--                    <button type="button" class="btn btn-link" v-text="$t('feed.detail.view-original-text')">원문 바로가기</button>-->
                <!--                  </a>-->
                <!--                </span>-->
              </div>
            </div>
            <div class="section-body">
              <div class="people-content">
                <div class="content-body">
                  <div v-if="people" class="people-info-detail">
                    <p>{{ people.googleInfobox.description }}</p>
                    <dl>
                      <template v-for="(item, index) in wikiList">
                        <dt :key="index + '_n'">{{ item.name }}</dt>
                        <dd :key="index + '_v'">{{ item.value }}</dd>
                      </template>
                    </dl>
                    <!--                    <p>한국 및 CE, IM부문 해외 9개 지역총괄과 DS부문 해외 5개 지역총괄, Harman 등 234개의 종속기업으로 구성된 글로벌 전자기업임. 세트사업에는 TV, 냉장고 등을 생산하는 CE부문과 스마트폰, 네트워크시스템, 컴퓨터 등을 생산하는 IM부문이 있음. 부품사업(DS부문)에서는 D램, 낸드 플래쉬, 모바일AP 등의 제품을 생산하는 반도체 사업과 TFT-LCD 및 OLED 디스플레이 패널을 생산하는 DP사업으로 구성됨.</p>-->
                  </div>
                  <div v-else class="people-info-detail">
                    <div v-for="i in 3" :key="i">
                      <b-skeleton width="1000px"></b-skeleton>
                    </div>
                  </div>
                </div>
                <div class="content-thumb">
                  <div v-show="image" class="img-thumb">
                    <img :src="image" />
                  </div>
                  <div class="social-area" v-if="people && people.social">
                    <a v-if="people.social.facebook" class="social-link" title="Facebook" target="_blank" :href="people.social.facebook">
                      <img src="content/images/common/media/social-facebook.png" />
                    </a>
                    <a v-if="people.social.twitter" class="social-link" title="Twitter" target="_blank" :href="people.social.twitter">
                      <img src="content/images/common/media/social-twitter.png" />
                    </a>
                    <a v-if="people.social.youtube" class="social-link" title="Youtube" target="_blank" :href="people.social.youtube">
                      <img src="content/images/common/media/social-youtube.png" />
                    </a>
                    <a v-if="people.social.instagram" class="social-link" title="Instagram" target="_blank" :href="people.social.instagram">
                      <img src="content/images/common/media/social-instagram.png" />
                    </a>
                    <a v-if="people.social.linkedin" class="social-link" title="Linkedin" target="_blank" :href="people.social.linkedin">
                      <img src="content/images/common/media/social-linkedin.png" />
                    </a>
                  </div>
                </div>
              </div>
              <div class="content-more" v-if="people && people.wikiUrl">
                <a :href="people.wikiUrl" target="_blank">
                  <button type="button" class="btn-link">
                    {{ $t('feed.detail.view-original-text') }}
                    <!--                    <i class="icon-common icon-newwindow"></i>-->
                  </button>
                </a>
                <!--                <button type="button" class="btn-more">-->
                <!--                  <em>{{ $t('feed.detail.view-all') }}</em-->
                <!--                  ><i class="icon-common icon-chevron-down"></i>-->
                <!--                </button>-->
              </div>
            </div>
          </section>
          <!--          <div class="detail-nav">-->
          <!-- flexd -->
          <!--            <nav class="dt-secondary-nav" id="detail-secondary-nav">-->
          <!--              <div class="container">-->
          <!--                <ul class="nav nav-tabs-line">-->
          <!--                  <li class="nav-item"><a class="nav-link active" href="#detail-related">Related Contents</a></li>-->
          <!--                  <li class="nav-item"><a class="nav-link" href="#detail-analysis">Analysis</a></li>-->
          <!--                  <li class="nav-item"><a class="nav-link" href="#detail-comment">Comment</a></li>-->
          <!--                </ul>-->
          <!--              </div>-->
          <!--            </nav>-->
          <!--          </div>-->
          <!--          <div data-spy="scroll" data-target="#detail-secondary-nav" data-offset="0">-->
          <!--            <section class="detail-related" id="detail-related">-->
          <!--              <detail-related v-if="true"></detail-related>-->
          <!--            </section>-->
          <!--            <section class="detail-analysis" id="detail-analysis">-->
          <!--              <detail-analysis v-if="true"></detail-analysis>-->
          <!--            </section>-->
          <!--            <section class="detail-comment" id="detail-comment"></section>-->
          <!--          </div>-->
        </div>
      </div>
    </div>
  </div>
  <!--  <div>-->
  <!--    <div class="container">-->
  <!--      <article class="article">-->
  <!--        <section>-->
  <!--          <div class="detail">-->
  <!--            <div class="detail-module" data-detail-module="profile">-->
  <!--              <h1 class="detail-module-title">Profile</h1>-->
  <!--              <div class="detail-module-title-line"></div>-->
  <!--              <div class="info">-->
  <!--                <dl>-->
  <!--                  <template v-for="(item, index) in wikiList">-->
  <!--                    <dt :key="index + '_n'">{{ item.name }}</dt>-->
  <!--                    <dd :key="index + '_v'">{{ item.value }}</dd>-->
  <!--                  </template>-->
  <!--                </dl>-->
  <!--                <div class="img">-->
  <!--                  <img :src="image" />-->
  <!--                </div>-->
  <!--              </div>-->
  <!--            </div>-->

  <!--            <b-spinner v-if="isShowSpinner" style="position: absolute; top: 50%; left: 50%" small label="Small Spinner"></b-spinner>-->
  <!--            <div v-if="stockCode && stockCodes.length > 0" style="margin-top: 70px">-->
  <!--              <swiper ref="mySwiper" :options="swiperOptions" @slideChange="onSlideChange">-->
  <!--                <swiper-slide v-for="(stockCode, index) in stockCodes" :key="index">-->
  <!--                  <ds-stock-chart v-if="stockCode.dataChart" :stockCode="stockCode" :dataZoom="true"></ds-stock-chart>-->
  <!--                </swiper-slide>-->
  <!--                &lt;!&ndash; <div class="swiper-pagination d-flex justify-content-center" slot="pagination"></div> &ndash;&gt;-->
  <!--                &lt;!&ndash;              <div class="swiper-button-next" style="position: static" slot="button-next"></div>&ndash;&gt;-->
  <!--                &lt;!&ndash;              <div class="swiper-button-prev" style="position: static" slot="button-prev"></div>&ndash;&gt;-->
  <!--              </swiper>-->
  <!--            </div>-->
  <!--          </div>-->
  <!--        </section>-->
  <!--        <section style="padding: 0">-->
  <!--          <ds-cards :cardItems="cardItems" :loaderDisable="loaderDisable" v-on:scrollLoader="scrollLoader" />-->
  <!--        </section>-->
  <!--      </article>-->
  <!--    </div>-->
  <!--  </div>-->
</template>

<script lang="ts" src="./ds-people-detail.component.ts"></script>
