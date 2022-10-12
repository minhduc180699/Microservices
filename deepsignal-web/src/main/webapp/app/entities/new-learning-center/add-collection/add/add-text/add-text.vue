<template>
  <div class="tab-pane fade show active" id="lc-add-memo">
    <div class="panel-LS-content">
      <div class="panel-body">
        <div class="add-input-area memo-area">
          <div class="memo-title">
            <input type="text" class="form-control" placeholder="Title" />
          </div>
          <div class="memo-desc">
            <textarea class="form-control" placeholder="Write a text"></textarea>
          </div>
        </div>
      </div>
      <div class="panel-footer">
        <div class="elements-right">
          <button type="button" class="btn btn-secondary">Add to collection</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./add-text.component.ts"></script>
