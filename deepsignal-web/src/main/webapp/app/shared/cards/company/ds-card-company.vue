<template>
  <div class="ds-card">
    <div class="ds-card-body">
      <div class="badge">Rising People</div>
      <div class="people-wrap">
        <a href="#" class="img-people">
          <img src="content/images/@people-img1.png" alt="" />
        </a>
        <a href="#" class="title">Kim Bum Su</a>
        <div class="info">기업인</div>
      </div>
      <div v-if="item.dataSize === cardSize._1_2 || item.content" class="desc">{{ item.content }}</div>
      <div v-if="item.dataSize === cardSize._1_2" class="source">출처 : <a href="#">위키백과</a></div>
    </div>
    <ds-card-footer :item="item" />
  </div>
</template>

<script lang="ts" src="./ds-card-company.component.ts"></script>
