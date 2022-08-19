<template>
  <div class="panel-sidebar connectome-home active">
    <div class="panel-inner">
      <div class="panel-main">
        <div class="main-inner">
          <div class="panel-header">
            <div class="header-content">
              <div class="search-connectome-wrap">
                <div class="form-group style1">
                  <input
                    id="inputFieldKeywordSearch"
                    class="form-control"
                    type="text"
                    v-model="filterTerms"
                    v-on:input="onSearchInput($event)"
                    :placeholder="$t('map-side-bar.entity-search-tab.search-placeholder')"
                    aria-label="search_input"
                  />
                  <button class="btn-search" type="button" aria-label="search_button" aria-disabled="true">
                    <i class="bi bi-search"></i>
                  </button>
                </div>
              </div>
            </div>
          </div>
          <div class="panel-body section-style1">
            <div class="panel-section">
              <div class="cnt-list">
                <div class="tab-content">
                  <div class="tab-pane fade show active" id="cnt-allList">
                    <div class="search-keyword-box">
                      <div class="top-bar">
                        <div class="top-title">
                          <span class="total" v-text="$t('map-side-bar.entity-search-tab.result-count', [searchResult.length])"></span>
                        </div>
                        <div>
                          <span class="total" v-text="$t('map-side-bar.entity-search-tab.result-filter-title')"></span>
                          <b-dropdown id="dropdown-filter-by" right size="sm" class="btn" variant="light">
                            <template #button-content>
                              <span>{{ searchResultFilterLabel }}</span></template
                            >
                            <b-dropdown-item-button v-on:click="setSearchResultFilterName('visible')">
                              <span class="total" v-text="$t('map-side-bar.entity-search-tab.result-all-visible')"></span>
                            </b-dropdown-item-button>
                            <b-dropdown-item-button v-on:click="setSearchResultFilterName('favorites')">
                              <span class="total" v-text="$t('map-side-bar.entity-search-tab.result-filter-by-favorite')"></span>
                            </b-dropdown-item-button>
                            <b-dropdown-item-button v-on:click="setSearchResultFilterName('black-entity')">
                              <span class="total" v-text="$t('map-side-bar.entity-search-tab.result-filter-by-black-entity')"></span>
                            </b-dropdown-item-button>
                            <b-dropdown-divider></b-dropdown-divider>
                            <b-dropdown-item-button v-on:click="setSearchResultFilterName('all')">
                              <span class="total" v-text="$t('map-side-bar.entity-search-tab.result-all')"></span>
                            </b-dropdown-item-button>
                            <b-dropdown-item-button v-on:click="setSearchResultFilterName('hidden')">
                              <span class="total" v-text="$t('map-side-bar.entity-search-tab.result-filter-by-disable')"></span>
                            </b-dropdown-item-button>
                          </b-dropdown>
                        </div>
                      </div>
                      <div class="list-scrollable">
                        <div class="list-wrap overflow-y-scroll">
                          <ul class="search-keyword-list favorites">
                            <li v-for="(item, index) in searchResult" :key="index">
                              <div :style="getClusterBoxStyle(item, index)" v-on:click="moveEntitySelectedTo(item.label)">
                                <b-icon icon="circle-fill" />
                              </div>
                              <a class="keword" id="btn-sub-open" v-on:click="moveEntitySelectedTo(item.label)">{{ item.label }}</a>
                              <template v-if="item.type === 'BLACK_ENTITY'">
                                <b-icon icon="award-fill" variant="dark" style="margin-left: 0.5rem"></b-icon>
                              </template>
                              <template v-if="item.favorite">
                                <b-icon icon="star-fill" variant="warning" style="margin-left: 0.5rem"></b-icon>
                              </template>
                              <template v-if="item.disable">
                                <b-icon icon="eye-slash-fill" variant="dark" style="margin-left: 0.5rem"></b-icon>
                              </template>
                            </li>
                          </ul>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div :class="panelSub">
        <div class="sub-inner" v-if="isDetailsPanelExist">
          <div class="panel-header">
            <div class="header-top">
              <button
                type="button"
                class="btn btn-icon"
                id="btn-panelSub-close"
                v-b-tooltip.hover.bottomright="$t('map-side-bar.details-tab.list-topics')"
                v-on:click="onBtnPanelSubCloseClick"
              >
                <i class="icon-common icon-list"></i>
              </button>
              <div class="wrap">
                <div class="btn-area">
                  <button
                    type="button"
                    class="btn btn-icon"
                    v-on:click="onFeedsClick"
                    v-b-tooltip.hover.bottomright="$t('map-side-bar.details-tab.keywords-linked')"
                  >
                    <i class="icon-common icon-feed"></i>
                  </button>
                  <button
                    v-if="snsFacebook || snsInstagram || snsTwitter || snsYoutube || snsLinkedIn"
                    type="button"
                    class="btn btn-icon"
                    v-b-tooltip.hover.bottomright="$t('map-side-bar.details-tab.go-to-people')"
                    v-on:click="onPeopleClick"
                  >
                    <i class="icon-common icon-people"></i>
                  </button>
                  <button
                    type="button"
                    class="btn btn-icon"
                    v-on:click="onLearnMoreClick"
                    v-b-tooltip.hover.bottomright="$t('my-ai-side-bar.tooltip.learning-center')"
                  >
                    <i class="icon-common icon-learning" />
                  </button>
                  <template v-if="isFavorite">
                    <button
                      type="button"
                      id="map-side-bar-set-as-favorite-toggle"
                      class="btn btn-icon btn-toggle active"
                      v-on:click="onSetAsFavoriteClick"
                      v-b-tooltip.hover.bottomright="$t('map-side-bar.details-tab.unset-as-favorite')"
                      aria-pressed="true"
                    >
                      <i class="icon-common icon-favorites"></i>
                    </button>
                  </template>
                  <template v-else>
                    <button
                      type="button"
                      id="map-side-bar-set-as-favorite-toggle"
                      class="btn btn-icon btn-toggle"
                      v-on:click="onSetAsFavoriteClick"
                      v-b-tooltip.hover.bottomright="$t('map-side-bar.details-tab.set-as-favorite')"
                      data-toggle="button"
                      aria-pressed="false"
                    >
                      <i class="icon-common icon-favorites"></i>
                    </button>
                  </template>
                  <template v-if="isDisable">
                    <button
                      type="button"
                      id="map-side-bar-set-as-favorite-toggle"
                      class="btn btn-icon btn-toggle active"
                      v-on:click="onHideClick"
                      v-b-tooltip.hover.bottomright="$t('map-side-bar.details-tab.display')"
                      aria-pressed="true"
                    >
                      <i class="icon-common icon-visibility"></i>
                    </button>
                  </template>
                  <template v-else>
                    <button
                      type="button"
                      id="map-side-bar-set-as-favorite-toggle"
                      class="btn btn-icon btn-toggle"
                      v-on:click="onHideClick"
                      v-b-tooltip.hover.bottomright="$t('map-side-bar.details-tab.hide')"
                      data-toggle="button"
                      aria-pressed="false"
                    >
                      <i class="icon-common icon-visibility"></i>
                    </button>
                  </template>
                </div>
              </div>
            </div>
            <div class="header-content">
              <div class="cnt-info-header">
                <div class="top-area">
                  <a v-on:click="centerEntitySelectedTo(title)" v-b-tooltip.hover.bottom="$t('map-side-bar.details-tab.locate-on-map')"
                    ><strong class="info-title" v-text="title"></strong
                  ></a>
                  <div class="info-img" v-if="imageUrl">
                    <img :src="imageUrl" :alt="title" />
                  </div>
                </div>
                <div class="social-area">
                  <a v-if="snsFacebook" :href="snsFacebook" class="social-link" target="_blank">
                    <img class="sns" src="content/images/common/media/social-facebook.png" />
                    <span class="tooltip">Facebook</span>
                  </a>
                  <a v-if="snsInstagram" :href="snsInstagram" class="social-link" target="_blank">
                    <img class="sns" src="content/images/common/media/social-instagram.png" />
                    <span class="tooltip">Instagram</span>
                  </a>
                  <a v-if="snsTwitter" :href="snsTwitter" class="social-link" target="_blank">
                    <img class="sns" src="content/images/common/media/social-twitter.png" />
                    <span class="tooltip">Twitter</span>
                  </a>
                  <a v-if="snsYoutube" :href="snsYoutube" class="social-link" target="_blank">
                    <img class="sns" src="content/images/common/media/social-youtube.png" />
                    <span class="tooltip">Youtube</span>
                  </a>
                  <a v-if="snsLinkedIn" :href="snsLinkedIn" class="social-link" target="_blank">
                    <img class="sns" src="content/images/common/media/social-linkedin.png" />
                    <span class="tooltip">LinkedIn</span>
                  </a>
                </div>
              </div>
            </div>
          </div>
          <div class="panel-body section-style2 overflow-y-scroll">
            <div class="list-scrollable">
              <div class="panel-section" v-if="description || wikiUrl || wikiInfoList.length > 0">
                <div class="section-body">
                  <div class="cnt-info-detail">
                    <div style="display: inline-block; padding-bottom: 0.5em">
                      <p>
                        <span v-if="description">{{ description }}</span
                        ><a v-if="wikiUrl" class="detail-source-link" :href="wikiUrl" target="_blank">{{
                          $t('map-side-bar.details-tab.source')
                        }}</a>
                      </p>
                    </div>
                    <div id="wiki-info-list" v-if="iswikiInfoListExist">
                      <div v-for="(item, index) in wikiInfoList" :key="index">
                        <span style="font-weight: bold; padding-right: 0.5em; padding-bottom: 0.5em">{{ item.key }}:</span
                        ><span style="padding-bottom: 0.5em">{{ item.value }}</span>
                      </div>
                    </div>
                    <div class="bar" v-if="wikiFullInfoList.length > 5">
                      <a v-on:click="resizeInfoBox()" class="btn btn-toggle detail-more" aria-pressed="false">
                        <span v-text="labelWikiDescriptionExpanded"></span>
                        <b-icon :icon="iconWikiDescriptionExpanded" size="lg"></b-icon>
                      </a>
                    </div>
                  </div>
                </div>
              </div>
              <div v-if="entitiesLinked.length > 0" class="panel-section">
                <div class="section-header">
                  <div class="section-title">
                    <span v-text="$t('map-side-bar.details-tab.keywords-linked')" /><small class="conunt"
                      ><span v-text="entitiesLinkedFull.length"
                    /></small>
                  </div>
                </div>
                <div class="section-body py-0">
                  <div class="cnt-info-detail">
                    <div class="connected-keywords Entities">
                      <div class="connected-keyword-list">
                        <a
                          v-for="(node, idx) in entitiesLinked"
                          v-b-tooltip.hover.bottomright="$t('map-side-bar.details-tab.select-this-topic')"
                          :key="idx"
                          v-on:click="moveEntitySelectedTo(node)"
                          >{{ node }}</a
                        >
                      </div>
                      <div class="bar" v-if="entitiesLinkedFull.length > 5">
                        <a v-on:click="resizeEntitiesLinked()" class="btn btn-toggle detail-more" aria-pressed="false">
                          <span v-text="labelEntitiesLinkedExpanded"></span>
                          <b-icon :icon="iconEntitiesLinkedExpanded" size="lg"></b-icon>
                        </a>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
              <div v-if="trainingDocumentCount > 0" class="panel-section">
                <div class="section-header">
                  <div class="section-title">
                    <span v-text="$t('map-side-bar.details-tab.resources-related')" /><small class="conunt"
                      ><span v-text="trainingDocumentCount"
                    /></small>
                  </div>
                </div>
                <div class="section-body py-0">
                  <div class="cnt-info-detail">
                    <template v-for="doc in entityPersonalDocuments">
                      <router-link
                        :to="{ name: 'Detail', params: { connectomeId: doc.connectomeId, feedId: doc.id } }"
                        :key="doc.id"
                        custom
                      >
                        <b-card :sub-title="doc.title">
                          <b-card-text class="small"> {{ getShortContent(doc.content) }} </b-card-text>
                          <b-card-text class="small text-muted">{{ convertGMTToLocalTime(doc.publishedAt) }}</b-card-text>
                        </b-card>
                      </router-link>
                    </template>
                    <template v-for="feed in entityFeeds">
                      <router-link
                        :to="{ name: 'Detail', params: { connectomeId: feed.connectomeId, feedId: feed.id } }"
                        :key="feed.id"
                        custom
                      >
                        <b-card :sub-title="feed.title">
                          <b-card-text class="small"> {{ getShortContent(feed.content) }} </b-card-text>
                          <b-card-text class="small text-muted">{{ convertGMTToLocalTime(feed.publishedAt) }}</b-card-text>
                        </b-card>
                      </router-link>
                    </template>
                    <b-card v-if="trainingDocumentCount > 0" body-class="text-center">
                      <b-card-text class="small"> {{ $t('map-side-bar.details-tab.go-to-personal-document-title') }} </b-card-text>
                      <b-button class="small" v-on:click="onBtnPersonalDocumentsRelatedClick()" variant="primary">{{
                        $t('map-side-bar.details-tab.go-to-personal-document')
                      }}</b-button>
                    </b-card>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <button
      type="button"
      id="btn-sidebar-folding"
      class="btn btn-toggle btn-sidebar-folding"
      v-on:click="onBtnSidebarFoldingClick"
    ></button>
  </div>
</template>
<script lang="ts" src="./map-side-bar.component.ts"></script>
<style></style>
