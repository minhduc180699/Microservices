<template>
  <div>
    <div class="panel panel-content" v-if="signalKeyword && miniConnectomeDisplayed">
      <div class="panel-header">
        <div class="panel-title" v-text="$t('signal.relationshipDiagram')"></div>
      </div>
      <div class="panel-body" style="min-height: 450px">
        <div class="connectome-container" style="min-height: 450px">
          <mini-connectome-map :propIds="[signalKeyword.id]" @onMiniConnectomeDisplayed="onMiniConnectomeDisplayed"></mini-connectome-map>
        </div>
      </div>
    </div>
    <div class="panel panel-content" v-if="signalKeyword && signalKeyword.wordClouds">
      <div class="panel-header">
        <div class="panel-title" v-text="$t('signal.associatedWordAnalysis')"></div>
      </div>
      <div class="panel-body" style="min-height: 450px">
        <signal-word-cloud :idChart="'word_cloud'" :currentSignal="signalKeyword" :tab="'detail'"></signal-word-cloud>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./detail-analysis.component.ts"></script>
