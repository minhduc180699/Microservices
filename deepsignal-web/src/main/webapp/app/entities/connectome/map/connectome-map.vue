<template>
  <div class="connectome-container">
    <!-- <div class="connectome-widget">
      <div class="connectome-control">
        <div class="control-topick">
          <div class="btn-group-vertical">
            <button type="button" class="btn btn-dark" id="btn-topickSearch-open" v-on:click="onTopicSearchOpen">
              <b-icon icon="search"></b-icon>
              <span><label v-text="$t('connectome-map.search')" /></span>
            </button>
            <button type="button" class="btn btn-dark" id="btn-topickDetail-open" v-on:click="onTopicDetailOpen">
              <b-icon icon="info-circle"></b-icon>
              <span><label v-text="$t('connectome-map.detail')" /></span>
            </button>
          </div>
        </div>
        <div class="control-screen">
          <div class="btn-group-vertical">
            <button type="button" class="btn btn-dark btn-toggle" data-toggle="button" aria-pressed="false">
              <b-icon icon="fullscreen" v-on:click="setFullScreen"></b-icon>
              <span
                ><em><label v-text="$t('connectome-map.leave')" /></em><label v-text="$t('connectome-map.fullscreen')"
              /></span>
            </button>
            <button type="button" class="btn btn-dark" v-on:click="fitScreen">
              <b-icon icon="aspect-ratio"></b-icon>
              <span><label v-text="$t('connectome-map.fit-to-screen')" /></span>
            </button>
          </div>
          <div class="btn-group-vertical">
            <button type="button" class="btn btn-dark" v-on:click="zoomIn">
              <b-icon icon="plus" size="lg"></b-icon>
              <span><label v-text="$t('connectome-map.zoom-in')" /></span>
            </button>
            <button type="button" class="btn btn-dark" v-on:click="zoomOut">
              <b-icon icon="dash" size="lg"></b-icon>
              <span><label v-text="$t('connectome-map.zoom-out')" /></span>
            </button>
          </div>
          <div class="btn-group-vertical">
            <button type="button" class="btn btn-dark" v-on:click="expandNodes">
              <b-icon icon="arrows-angle-expand"></b-icon>
              <span><label v-text="$t('connectome-map.expand-nodes')" /></span>
            </button>
            <button type="button" class="btn btn-dark" v-on:click="contractNodes">
              <b-icon icon="arrows-angle-contract"></b-icon>
              <span><label v-text="$t('connectome-map.contract-nodes')" /></span>
            </button>
          </div>
        </div>
      </div>
      <div class="panel-sidebar connectome-search">
        <div class="panel-header">
          <div class="header-top">
            <strong><label v-text="$t('connectome-map-search-bar.title')" /></strong>
            <a href="#" role="button" aria-label="닫기" class="btn-close" id="btn-topickSearch-close" v-on:click="onTopicSearchClose"
              ><b-icon icon="x" size="lg"></b-icon
            ></a>
          </div>
          <div class="search-wrap">
            <div class="search-group">
              <ds-search-dropdown
                id="connectome-map-search-bar-input"
                v-bind:options="listNodesByIdAndLabel"
                v-on:selected="validateSelection"
                :disabled="false"
                :maxItem="30"
                placeholder="Please select a node"
                class="form-control"
              ></ds-search-dropdown>
            </div>
          </div>
        </div>
      </div>
      <div class="panel-sidebar connectome-detail">
        <div class="panel-header">
          <div class="header-top">
            <strong v-text="$t('map-side-bar.title')"></strong>
            <a role="button" aria-label="닫기" class="btn-close" id="btn-topickDetail-close" v-on:click="onTopicDetailClose"
              ><b-icon icon="x" size="lg"></b-icon
            ></a>
          </div>
          <div class="header-content">
            <div class="entity-info-wrap">
              <div class="info-top">
                <div class="col-9 px-0 text-break">
                  <strong class="info-title" v-text="topicBarTitle"></strong>
                </div>
                <div class="col-2 info-img px-1">
                  <img :src="topicBarImageUrl" :alt="topicBarTitle" />
                </div>
                <div class="col-1 px-1 d-flex justify-content-center">
                  <button class="btn btn-favorite px-0" aria-label="favorite" @click="addFavorites(topicBarTitle)">
                    <i class="bi bi-star-fill"></i>
                  </button>
                </div>
              </div>
            </div>
            <div class="btn-area">
              <button type="button" class="btn" v-on:click="onFeedsClick">
                <b-icon icon="columns"></b-icon><label v-text="$t('map-side-bar.feeds')" />
              </button>
            </div>
          </div>
        </div>
        <div class="panel-body overflow-y-scroll">
          <div id="mCSB_3" style="max-height: none" tabindex="0">
            <div id="mCSB_3_container" class="mCSB_container" style="position: relative; top: 0; left: 0">
              <div v-if="topicBarWikiInfoList.length > 1 || (topicBarDescription && topicBarDescription.length > 1)" class="panel-section">
                <div class="section-body">
                  <div class="entity-info-wrap">
                    <p v-text="topicBarDescription"></p>
                    <dl>
                      <div v-for="(item, index) in topicBarWikiInfoList" :key="index">
                        <dt>{{ item.key }}</dt>
                        <dd>{{ item.value }}</dd>
                      </div>
                    </dl>
                    <div class="text-right">
                      <a
                        v-on:click="() => (isWikiDescriptionExpanded = !isWikiDescriptionExpanded)"
                        class="btn btn-toggle btn-more-text"
                        aria-pressed="false"
                        ><span class="on" v-text="labelWikiDescriptionExpanded"></span>
                        <b-icon :icon="iconWikiDescriptionExpanded" size="lg"></b-icon
                      ></a>
                    </div>
                  </div>
                </div>
              </div>
              <div class="panel-section">
                <div class="section-body py-3">
                  <div class="connected-keywords Topics">
                    <strong class="title" v-text="$t('map-side-bar.topics')"><small class="count">12</small></strong>
                    <div class="connected-keyword-list">
                      <a v-if="topicBarNetworkTopicFocused" v-on:click="focusFromNetwork(topicBarNetworkTopicFocused)" class="active">{{
                        topicBarNetworkTopicFocused.name
                      }}</a>
                      <a v-for="(node, idx) in topicBarTopicsList" :key="idx" v-on:click="focusFromNetwork(node)">{{ node.name }}</a>
                    </div>
                  </div>
                  <div class="connected-keywords Entities">
                    <strong class="title" v-text="$t('map-side-bar.entities')"><small class="count">12</small></strong>
                    <div class="connected-keyword-list">
                      <a v-if="topicBarNetworkEntityFocused" v-on:click="focusFromNetwork(topicBarNetworkEntityFocused)" class="active">{{
                        topicBarNetworkEntityFocused.name
                      }}</a>
                      <a v-for="(node, idx) in topicBarEntitiesList" :key="idx" v-on:click="focusFromNetwork(node)">{{ node.name }}</a>
                    </div>
                  </div>
                </div>
              </div>
              <div class="panel-section" v-if="topicBarDocumentsPresent">
                <div class="section-header">
                  <strong class="section-title" v-text="$t('map-side-bar.learning-resources')"></strong>
                </div>
                <div class="section-body">
                  <ul class="resource-list list-row" v-for="(document, idx) in topicBarDocumentsList" :key="idx">
                    <li class="list-item" v-for="(document, idx) in topicBarDocumentsList" :key="idx">
                      <div class="resource-card type-web">
                        <div class="card-content">
                          <div class="info" v-if="document.url">
                            <span class="source-url"
                              ><a :href="document.url" target="_blank">{{ document.url }}</a></span
                            >
                          </div>
                          <a class="title" :href="document.url" target="_blank">{{ document.title }}</a>
                        </div>
                      </div>
                    </li>
                  </ul>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <router-view
      class="connectome-area"
      @onLeftClickOnNetworkNode="leftClickOnNetworkNode"
      @onRightClickOnNetworkNode="rightClickOnNetworkNode"
      @onLeftClickOnMapNode="leftClickOnMapNode"
      @onRightClickOnMapNode="rightClickOnMapNode"
      @onGetUpdatedData="getConnectomeData"
      @onClearSelection="clearSelection"
      @onNetworkMapInitiate="networkMapInitiate"
    >
    </router-view> -->
  </div>
</template>
<script lang="ts" src="./connectome-map.component.ts"></script>
<style scoped></style>
