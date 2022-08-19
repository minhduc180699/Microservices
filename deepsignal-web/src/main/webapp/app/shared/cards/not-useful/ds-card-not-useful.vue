<template>
  <div>
    <div
      class="not-useful"
      :style="[!isReport && isLoadingDelete ? { alignItems: 'center' } : {}, isMyAi ? { border: '1px solid #e9ecef' } : {}]"
      style="padding: 0 20px"
    >
      <div v-show="isReport">
        <p style="font-size: 16px">Report problem</p>
        <b-form-textarea
          id="textarea-small"
          size="sm"
          placeholder="Enter your report"
          style="height: 100px; border: 2px solid #6a7681"
          v-model="reportMessages"
        ></b-form-textarea>
        <div style="text-align: right; margin-top: 10px">
          <b-button variant="light" style="margin-left: 10px; border-radius: 10%; background-color: #e8e8e8" @click="isReport = false"
            >Back
          </b-button>
          <b-button
            variant="light"
            style="margin-left: 10px; border-radius: 10%; background-color: #e8e8e8"
            @click="handleReport(messages.REPORT_SEND)"
            >Send
          </b-button>
        </div>
      </div>
      <div v-show="!isReport && isLoadingDelete">
        <b-spinner style="width: 2rem; height: 2rem" label="Spinning"></b-spinner>
      </div>
      <div v-show="!isReport && !isLoadingDelete">
        <p>Let us know <br />what you dislike about this story?</p>
        <button
          type="button"
          class="btn btn-block btn-outline-lightgray"
          @click="handleReport(messages.NOT_INTERESTING)"
          style="color: #2c2d30 !important"
        >
          Not interesting!
        </button>
        <button
          type="button"
          class="btn btn-block btn-outline-lightgray"
          @click="handleReport(messages.DO_NOT_LIKE)"
          style="color: #2c2d30 !important"
        >
          I don't like it!
        </button>
        <button
          type="button"
          class="btn btn-block btn-outline-lightgray"
          @click="handleReport(messages.REPORT)"
          style="color: #2c2d30 !important"
        >
          Report this story
        </button>
        <button
          type="button"
          class="btn btn-block btn-outline-lightgray"
          @click="handleReport(messages.CANCEL)"
          style="color: #2c2d30 !important"
        >
          Cancel
        </button>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./ds-card-not-useful.component.ts"></script>

<style>
.not-useful .btn {
  height: 36px;
}
</style>
