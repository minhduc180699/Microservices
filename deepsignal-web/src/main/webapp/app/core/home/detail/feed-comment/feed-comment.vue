<template>
  <section>
    <h1 class="detail-module-title">Comment</h1>
    <div class="post">
      <textarea name="comment" rows="" class="form-control" v-model="contentComment" v-autotrim ref="comment"></textarea>
      <div class="position-absolute mt-3" v-if="fromComment">
        <div class="">
          <b-badge>You are replying @{{ fromComment.userDTO.fullName }}</b-badge>
          <b-button class="btn btn-close" variant="link" @click="clearReply"><i class="bi bi-x-circle"></i></b-button>
        </div>
      </div>
      <button type="button" class="btn btn-dark" @click="saveComment">Post</button>
    </div>
    <div class="reply-count">
      <div class="row justify-content-between align-items-center">
        <div class="col-auto">
          <div class="count">
            <span v-text="$t('feed.detail.comment')">Comment</span>
            <strong>{{ totalComments }}</strong>
            <!--            <button type="button" aria-label="새로고침" @click="getCommentsByFeed"><i class="icon-refresh"></i></button>-->
          </div>
        </div>
        <div class="col-auto">
          <div class="toggler">
            <button
              type="button"
              aria-label="확장/축소"
              data-toggle="collapse"
              data-target="#memo"
              aria-expanded="false"
              aria-controls="memo"
              @click="isShow = !isShow"
            >
              <i :class="[isShow ? 'icon-arrow-up' : 'icon-arrow-down']"></i>
            </button>
            <!-- 댓글 갯수가 1개 이상일때  aria-expanded="true" -->
          </div>
        </div>
      </div>
    </div>
    <div :class="['memo collapse', isShow ? 'show' : '']" id="memo">
      <p class="empty" v-if="!comments || comments.length === 0">
        <i class="icon-empty-primary"></i>{{ $t('feed.detail.no-registered-note') }}
      </p>
      <ds-tree :tree-data="comments" @onReply="onReply($event)"></ds-tree>
      <button
        type="button"
        class="btn btn-lg btn-outline-lightgray btn-block"
        v-text="$t('feed.detail.see-all-comment')"
        @click="viewMoreComment"
        :disabled="isLast"
      >
        View more
      </button>
    </div>
  </section>
</template>
<script lang="ts" src="./feed-comment.component.ts"></script>
<style>
.btn-close {
  position: absolute;
  right: -35px;
  top: -25px;
}
</style>
