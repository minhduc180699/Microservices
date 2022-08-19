<template>
  <b-collapse
    :visible="isDetailsPanelExist"
    id="collapse-panel-info-node"
    style="position: absolute; z-index: 2; background-color: #fdfdfd"
  >
    <div class="panel-header">
      <div class="header-content">
        <div class="cnt-info-header">
          <div class="btn-area">
            <button
              type="button"
              :class="connectomeToggleClass"
              v-b-tooltip.hover.bottom="connectomeToggleToolTipText"
              v-on:click="onConnectomeClick"
            >
              <i class="icon-common icon-connectome"></i>
            </button>
            <button
              type="button"
              v-if="hasSNS"
              :class="peopleTabToggleClass"
              v-b-tooltip.hover.bottom="peopleTabToggleToolTipText"
              v-on:click="onPeopleClick"
            >
              <i class="icon-common icon-people"></i>
            </button>
            <button
              type="button"
              class="btn btn-icon"
              v-on:click="onFeedsClick"
              v-b-tooltip.hover.bottom="$t('map-side-bar.details-tab.keywords-linked')"
            >
              <i class="icon-common icon-feed"></i>
            </button>
            <button
              type="button"
              class="btn btn-icon"
              v-on:click="onLearnMoreClick"
              v-b-tooltip.hover.bottom="$t('my-ai-side-bar.tooltip.learning-center')"
            >
              <i class="icon-common icon-learning" />
            </button>
            <button
              type="button"
              id="map-side-bar-set-as-favorite-toggle"
              :class="favoriteToggleClass"
              v-on:click="onSetAsFavoriteClick"
              v-b-tooltip.hover.bottom="favoriteToggleToolTipText"
              aria-pressed="true"
            >
              <i class="icon-common icon-favorites"></i>
            </button>
            <button
              type="button"
              id="map-side-bar-set-as-favorite-toggle"
              :class="disableToggleClass"
              v-on:click="onHideClick"
              v-b-tooltip.hover.bottom="disableToggleToolTipText"
              aria-pressed="true"
            >
              <i class="icon-common icon-visibility"></i>
            </button>
          </div>
          <div class="top-area">
            <strong class="info-title">
              <a v-on:click="centerEntitySelectedTo(title)" v-b-tooltip.hover.bottom="$t('map-side-bar.details-tab.locate-on-map')">{{
                title
              }}</a></strong
            >
          </div>
        </div>
      </div>
    </div>
  </b-collapse>
</template>
<script lang="ts" src="./mini-map-bottom-bar.component.ts"></script>
<style scoped></style>
