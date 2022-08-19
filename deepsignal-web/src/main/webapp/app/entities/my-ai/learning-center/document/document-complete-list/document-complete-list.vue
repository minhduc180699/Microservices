<template>
  <div class="lc-content-body">
    <div class="top-bar">
      <div class="top-title">
        <strong>{{ $t('learningCenter.learningCompleteList') }}</strong>
        <span class="total"
          >{{ $t('learningCenter.total') }}<em>{{ (docLists && docLists.length) || 0 }}</em
          >{{ $t('learningCenter.items') }}</span
        >
      </div>
      <div class="top-elements">
        <div class="btn-group btn-view btn-group-toggle" data-toggle="buttons">
          <label class="btn btn-icon"> <input type="radio" name="options" id="option1" checked="" /><i class="bi bi-grid-fill"></i> </label>
          <label class="btn btn-icon active"> <input type="radio" name="options" id="option2" /><i class="bi bi-list"></i> </label>
        </div>
      </div>
    </div>
    <div class="resource-list-wrap">
      <doc-wrap-list
        :docLists="docLists"
        :loading="loading"
        @removeFile="removeFile($event)"
        @downloadFile="downloadFile($event)"
        :parent="parent"
      ></doc-wrap-list>
    </div>
  </div>
</template>
<script lang="ts" src="./document-complete-list.component.ts"></script>
