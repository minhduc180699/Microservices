<template>
  <div class="ds-card">
    <div class="ds-card-header">
      <div class="ds-card-title">People/Companies</div>
    </div>
    <div class="ds-card-body" v-if="topTenInfos && topTenInfos.length > 0">
      <ol class="ds-media-list">
        <li class="media" v-for="(data, index) of topTenInfos" :key="index" @click.prevent="handleMoveToPeople(index, data.label)">
          <div class="media-body">
            <div class="rank-wrap">
              <span class="rank-num">{{ index + 1 }}</span>
            </div>
            <div class="text-wrap">
              <a class="title" target="_blank">{{ data.label }}</a>
              <span class="info" :title="data.info">{{ data.info | lmTo(15) }}</span>
            </div>
          </div>
          <a class="img-thumb">
            <img :src="data.imageUrl" alt="" />
          </a>
        </li>
      </ol>
      <div style="text-align: center; font-size: 14px; position: absolute; margin: auto; left: 0; right: 0; bottom: 5px">
        <router-link to="/people" v-slot="{ href, navigate }" custom>
          <a :href="href" @click="navigate" style="color: darkgray">
            {{ $t('feed.card.card-people') }}
          </a>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./ds-card-topten-content.component.ts"></script>

<style>
.text-wrap .info {
  cursor: pointer;
}
</style>
