<template>
  <div>
    <div class="ds-detail-wrap">
      <div class="ds-detail-content">
        <div class="detail-header">
          <button type="button" class="btn btn-icon">
            <i class="icon-common-lg icon-arrow-left" @click="backFeed()"></i>
            <span class="tooltip" v-text="$t('feed.detail.back-feed')"></span>
          </button>
          <detail-activity :item="connectomeFeed"></detail-activity>
        </div>
        <div class="detail-body">
          <section class="detail-main detail-article">
            <div class="section-header">
              <div class="source">
                <div class="source-img">
                  <img
                    style="max-width: 40px; max-height: 40px; min-height: 20px; min-width: 20px"
                    v-if="connectomeFeed.favicon_url || connectomeFeed.favicon_base64"
                    :src="connectomeFeed.favicon_url ? connectomeFeed.favicon_url : connectomeFeed.favicon_base64"
                    alt=""
                  />
                </div>
                <div class="source-text">{{ connectomeFeed.writer }}</div>
              </div>
              <h1 class="title" v-text="connectomeFeed.title"></h1>
              <div class="info info-list">
                <span class="date">{{ new Date(connectomeFeed.created_date) | formatDate }}</span>
                <span class="writer">{{ connectomeFeed.writer }}</span>
              </div>
            </div>
            <div class="section-body">
              <div
                v-if="isShowHtmlContent && connectomeFeed.html_content"
                class="html-content-area"
                v-html="connectomeFeed.html_content"
              ></div>
              <div v-else class="content-box">
                <div v-if="connectomeFeed.og_image_url || connectomeFeed.og_image_base64" class="img-area mb-0 mr-3">
                  <img :src="connectomeFeed.og_image_base64 ? connectomeFeed.og_image_base64 : connectomeFeed.og_image_url" alt="" class="w-100" />
                </div>
                <div class="text-area">
                  {{ connectomeFeed.content }}
                </div>
              </div>
              <iframe
                ref="docsEmbed"
                :onLoad="onIframeLoaded"
                v-if="connectomeFeed.search_type && connectomeFeed.search_type === 'PDF' || connectomeFeed.search_type && connectomeFeed.search_type === 'PPT'"
                :src="'https://docs.google.com/viewer?url=' + encodeURIComponent(connectomeFeed.url) + '&embedded=true'"
                embedded="true"
                width="100%"
                height="800px"
                frameborder="1"
              >
              </iframe>
              <div class="img">
                <iframe
                  v-if="connectomeFeed.url && isYoutube(connectomeFeed.url)"
                  style="min-height: 500px"
                  width="100%"
                  height="100%"
                  :src="getEmbedLink(connectomeFeed.url)"
                  title="YouTube video player"
                  frameborder="0"
                  allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
                  allowfullscreen
                ></iframe>
              </div>
              <!-- <div class="connectome-container" style="min-height: 450px">
                <mini-connectome-map :propFeedIds="[connectomeFeed.id]"></mini-connectome-map>
              </div> -->
              <div class="content-more">
                <button type="button" class="btn-link" @click="viewPost(connectomeFeed.url)">
                  {{ $t('feed.detail.view-original-text') }}
                </button>
                <button
                  v-if="connectomeFeed.html_content && connectomeFeed.html_content.length > 0"
                  type="button"
                  class="btn-more"
                  @click="isShowHtmlContent = !isShowHtmlContent"
                >
                  <em v-if="isShowHtmlContent">{{ $t('feed.detail.view-less') }}</em>
                  <em v-else>{{ $t('feed.detail.view-more') }}</em>
                </button>
              </div>
              <div v-if="connectomeFeed.stockCodes && connectomeFeed.stockCodes.length > 0" style="margin-top: 40px">
                <ds-chart :item="connectomeFeed"></ds-chart>
              </div>
            </div>
          </section>
          <section class="detail-related">
            <detail-related
              v-if="connectomeFeed.docId"
              :feedDocId="connectomeFeed.docId"
              :dataSwiper="swiperDataMobile"
              v-on:hideDetailSide="hideDetailSide"
            ></detail-related>
            <related-people-company
              :dataRelated="relatedPeopleCompany"
              v-if="relatedPeopleCompany && relatedPeopleCompany.length > 0"
              :dataSwiper="peopleCompanyMobile"
            ></related-people-company>
          </section>
          <section class="detail-analysis" id="detail-analysis">
            <detail-analysis :chartData="connectomeFeed.neuronNetworkChart" :signalKeyword="connectomeFeed"></detail-analysis>
          </section>
        </div>
      </div>
      <div
        :class="[
          isFixedDetailSide == 1 ? 'is_fixing_at_bottom' : '',
          isFixedDetailSide == 2 ? 'is_fixing_at_bottom is_stop_fixing' : '',
          (!isShowDetailSideRelated && !isShowDetailSidePeople) || (!connectomeFeed.docId && !isShowDetailSidePeople)
            ? 'ds-target-side'
            : 'ds-detail-side',
        ]"
      >
        <div class="side-inner">
          <section class="detail-related">
            <detail-related
              v-if="connectomeFeed.docId"
              :feedDocId="connectomeFeed.docId"
              :dataSwiper="swiperDataDesktop"
              v-on:hideDetailSide="hideDetailSide"
            ></detail-related>
            <!--            <detail-related v-if="connectomeFeed.docId" :feedDocId="connectomeFeed.docId" :dataSwiper="swiperDataDesktop2"></detail-related>-->
            <related-people-company
              :dataRelated="relatedPeopleCompany"
              v-if="relatedPeopleCompany && relatedPeopleCompany.length > 0"
              :dataSwiper="peopleCompanyDesktop"
            ></related-people-company>
          </section>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./ds-feed-detail.component.ts"></script>
<style>
.content-box {
  width: 100%;
  overflow: auto;
  margin-right: 15px;
  box-sizing: border-box;
  margin-bottom: 10px;
}
.content-box .img-area {
  float: left;
  width: 15%;
}
.text-area p {
  text-align: justify;
}
.text-area img {
  text-align: center;
}
</style>
