<template>
  <div class="bar">
    <div class="row justify-content-between align-items-center">
      <div class="col">
        <div class="d-flex align-items-center justify-content-center p-l-100">
          <i class="icon-bot"></i>
          <strong class="text-primary ml-4"
            >Learning is in progress. We will notify you when the feed is collected after learning is completed!</strong
          >
        </div>
      </div>
      <div class="col-auto">
        <button type="button" class="btn btn-light text-primary">Won't you get notifications</button>
      </div>
    </div>
    <div class="progress">
      <div class="progress-bar" role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100"></div>
    </div>
  </div>
</template>

<script lang="ts" src="./selected-learning.component.ts"></script>
