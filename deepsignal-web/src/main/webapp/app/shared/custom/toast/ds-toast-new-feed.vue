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
      <div class="toast-body">
        <button type="button" class="btn btn-link" @click="scrollTop">
          <i class="icon-common-lg icon-arrow-up"></i>
          {{ $t('toast.new-feed') }}
        </button>
      </div>
    </div>
  </div>
</template>
<script lang="ts">
import { Component, Inject, Vue } from 'vue-property-decorator';
import { CacheService } from '@/service/cache.service';
import { PrincipalService } from '@/service/principal.service';
import WebsocketService from '@/service/websocket.service';
import { Subscription } from 'rxjs';

@Component({
  name: 'ds-toast-new-feed',
})
export default class DsToastNewFeed extends Vue {
  @Inject('cacheService') private cacheService: () => CacheService;
  @Inject('principalService') private principalService: () => PrincipalService;
  @Inject('websocketService') private websocketService: () => WebsocketService;
  private subscription?: Subscription;
  connectomeId;
  isShow = false;

  mounted(): void {
    this.connectomeId = this.principalService().getConnectomeInfo().connectomeId;
    this.initWebsocket();
  }

  onCloseToast() {
    this.isShow = false;
  }

  scrollTop() {
    document.documentElement.scrollTop = 0;
    this.isShow = false;
  }

  // subscribe for learning feed status from websocket
  public initWebsocket(): void {
    this.subscription = this.websocketService().subscribeUpdateFeed(activity => {
      if (activity) {
        if (!activity.connectomeId) {
          return;
        }
        if (activity.connectomeId == this.connectomeId) {
          this.isShow = true;
          this.autoHideToast();
          this.$emit('feedUpdated', activity);
        }
      }
    });
  }

  autoHideToast() {
    setTimeout(() => {
      this.onCloseToast();
    }, 10000);
  }

  destroyed() {
    if (this.subscription) {
      try {
        this.subscription.unsubscribe();
        this.subscription = undefined;
      } catch (error) {
        console.log(error);
      }
    }
  }
}
</script>
