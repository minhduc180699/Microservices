<template>
  <div class="ds-card">
    <div class="ds-card-header">
      <div class="ds-card-title">
        Today's Topic<small>{{ workDay }}</small>
      </div>
      <div class="panel-elements">
        <!--        <button type="button" class="btn btn-icon"><i class="icon-common icon-refresh"></i></button>-->
      </div>
    </div>
    <div class="ds-card-body">
      <div class="cluster">
        <signal-today-issue-common ref="cardSignalTodayIssueCommon" :sizeCluster="6"></signal-today-issue-common>
      </div>
      <div class="list-more">
        <button type="button" class="btn-more" @click="toSignal">
          <em>Today's Issue</em><i class="icon-common icon-chevron-right"></i>
        </button>
      </div>
    </div>
  </div>
</template>
<script lang="ts" src="./signal-issue.component.ts"></script>
