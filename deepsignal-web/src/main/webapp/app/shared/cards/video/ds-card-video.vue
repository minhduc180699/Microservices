<template>
  <!-- [YouTube] -->
  <div class="ds-card">
    <div class="ds-card-body">
      <div class="video">
        <iframe
          width="100%"
          height="100%"
          src="https://www.youtube.com/embed/99BnZ8js1_k"
          title="YouTube video player"
          frameborder="0"
          allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
          allowfullscreen
        ></iframe>
      </div>
      <router-link to="/detail" custom>
        <a>
          <div class="source">
            <div class="source-img"><i class="source-youtube"></i></div>
            <div class="source-text">Youtube</div>
          </div>
          <div class="title max-line-2">{{ item.title }}</div>
          <div class="desc">{{ item.desc }}</div>
        </a>
      </router-link>
    </div>
    <ds-card-footer :item="item" />
  </div>
</template>
<script lang="ts" src="./ds-card-video.component.ts"></script>
