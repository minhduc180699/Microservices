<template>
  <div class="toast-header-center">
    <div
      class="toast toast-style-1"
      :class="isShow ? 'show' : 'hide'"
      role="alert"
      aria-live="assertive"
      aria-atomic="true"
      data-delay="2000"
    >
      <button type="button" class="close" data-dismiss="toast" aria-label="Close" @click="onCloseToast">
        <i class="icon-common icon-close-light"></i>
      </button>
      <div class="toast-body" v-text="$t('toast.feed-learning')">I am learning</div>
    </div>
  </div>
</template>
<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { SESSION_STORAGE_CONSTANTS } from '@/shared/constants/ds-constants';

@Component({
  components: {},
  name: 'ds-toast-learning',
})
export default class DsToastLearning extends Vue {
  isShow = false;

  mounted(): void {
    this.checkTrainingFeedStatus();
  }

  onCloseToast() {
    sessionStorage.setItem(SESSION_STORAGE_CONSTANTS.FEED_STATUS_TRAINING, 'false');
    this.isShow = false;
  }

  checkTrainingFeedStatus() {
    const feedStatusFromSessionStorage = JSON.parse(sessionStorage.getItem(SESSION_STORAGE_CONSTANTS.FEED_STATUS_TRAINING));
    if (feedStatusFromSessionStorage) {
      this.isShow = true;
      this.autoHideToast();
    }
  }

  autoHideToast() {
    setTimeout(() => {
      this.onCloseToast();
    }, 10000);
  }
}
</script>
