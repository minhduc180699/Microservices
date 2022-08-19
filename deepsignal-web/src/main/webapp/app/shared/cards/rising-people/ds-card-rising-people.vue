<template>
  <!-- [Rising People] -->
  <div class="ds-card">
    <div class="ds-card-body">
      <div class="badge">{{ item.dataTemplate === 'people' ? 'Trend People' : 'Rising Company' }}</div>
      <router-link :to="{ path: '/peopleDetail', query: { name: item.name, stockCode: item.stockCode } }">
        <a>
          <div class="people-wrap">
            <a :class="item.dataTemplate === 'people' ? 'img-people' : 'img-company'">
              <img :src="item.image" @error="$event.target.src = 'content/images/empty-image.png'" :alt="item.name" />
            </a>
            <a class="title"> {{ item.name }} </a>
            <div class="info"></div>
          </div>
        </a>
      </router-link>
      <div class="desc" style="white-space: pre-line">
        {{ item.content }}
      </div>
      <div class="source text-overflow">
        {{ $t('people.source') }} : <a :href="item.source" target="_blank" rel="noopener noreferrer">{{ item.source }}</a>
      </div>
    </div>
    <!--    <ds-card-footer :item="item" />-->
  </div>
</template>
<script lang="ts" src="./ds-card-rising-people.component.ts"></script>
<style scoped>
.text-overflow {
  display: block;
  white-space: nowrap;
  overflow-x: hidden;
  text-overflow: ellipsis;
  line-height: unset !important;
}
</style>
