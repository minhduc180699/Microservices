<template>
  <div class="ds-card-footer">
    <div>
      <button type="button" class="btn btn-like" :class="{ active: isLike }" aria-label="like" :aria-pressed="isLike" @click="handleLike">
        <i class="icon-common icon-like"></i>
      </button>
      <button
        type="button"
        class="btn btn-dislike"
        :class="{ active: isDislike }"
        aria-label="dislike"
        :aria-pressed="isDislike"
        @click="handleDislike"
      >
        <i class="icon-common icon-dislike"></i>
      </button>
      <button
        type="button"
        class="btn btn-bookmark"
        :class="{ active: isBookmark }"
        :aria-pressed="isBookmark"
        @click="handleBookmark"
        aria-label="bookmark"
      >
        <i class="icon-common icon-bookmark"></i>
      </button>
      <button type="button" class="btn btn-bookmark" @click="handleShare()" aria-label="bookmark">
        <i class="icon-common icon-share"></i>
      </button>
      <button type="button" class="btn btn-bookmark" @click="handleHidden">
        <i class="icon-common icon-visibility"></i>
      </button>
    </div>
    <!--    <div>-->
    <!--      <button type="button" class="btn btn-card-more" aria-label="more" ref="footerMore" @click="handleMore()">-->
    <!--        <i class="icon-common icon-more"></i>-->
    <!--      </button>-->
    <!--    </div>-->
  </div>
</template>
<script lang="ts" src="./ds-card-footer.component.ts"></script>
