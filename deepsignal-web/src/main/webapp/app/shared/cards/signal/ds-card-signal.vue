<template>
  <div class="ds-card">
    <div class="ds-card-header">
      <div class="ds-card-title">Today's Issue</div>
    </div>
    <div class="ds-card-body">
      <div class="tab-content">
        <div class="tab-pane fade show active" id="pills-3" role="tabpanel">
          <ol class="ds-rank-list" v-if="isLoadingSignal">
            <li v-for="i in 10" :key="i">
              <b-skeleton width="260px"></b-skeleton>
            </li>
          </ol>
          <ol class="ds-rank-list">
            <li v-for="(signal, index) of signals" :key="index">
              <span class="rank-num">{{ index + 1 }}</span>
              <strong
                ><a @click="toSignal('#' + (index + 1))" class="text">{{ signal.keywords }}</a></strong
              >
            </li>
          </ol>
        </div>
      </div>
      <div
        v-if="signals && signals.length > 0"
        style="text-align: center; font-size: 14px; position: absolute; margin: auto; left: 0; right: 0; bottom: 5px"
      >
        <router-link to="/signals" v-slot="{ href, navigate }" custom>
          <a :href="href" @click="navigate" style="color: darkgray">
            {{ $t('feed.card.card-issue') }}
          </a>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script src="./ds-card-signal.component.ts"></script>

<style scoped lang="scss">
.ds-rank-list li {
  height: 40px;
}
</style>
