<template>
  <div class="wrap">
    <button type="button" class="btn btn-icon btn-like" :class="{ active: isLiked }" @click="handleLike">
      <i class="icon-common icon-like"></i>
      <span class="tooltip" v-text="$t('feed.detail.like')">좋아요</span>
    </button>
    <button type="button" class="btn btn-icon btn-dislike" :class="{ active: isDisliked }" @click="handleDislike">
      <i class="icon-common icon-dislike"></i>
      <span class="tooltip" v-text="$t('feed.detail.dislike')">싫어요</span>
    </button>
    <button type="button" class="btn btn-icon btn-bookmark" :class="{ active: isBookmarked }" @click="handleBookmark">
      <i class="icon-common icon-bookmark"></i>
      <span class="tooltip" v-text="$t('feed.detail.bookmark')">북마크</span>
    </button>
    <button type="button" class="btn btn-icon btn-share" @click="handleShare">
      <i class="icon-common icon-share"></i>
      <span class="tooltip" v-text="$t('feed.detail.share')">공유</span>
    </button>
    <div class="dropdown">
      <button type="button" class="btn btn-icon btn-visibility" @click="showMore">
        <i class="icon-common icon-more"></i>
      </button>
      <div class="dropdown-menu" :class="{ show: isShowMore }">
        <a class="dropdown-item" @click="handleHide"><i class="icon-common icon-visibility"></i>{{ $t('feed.detail.hide') }}</a>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./activity-detail.component.ts"></script>
