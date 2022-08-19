<template>
  <section>
    <h1 class="detail-module-title">Memo</h1>
    <div class="post">
      <textarea name="memo" rows="" class="form-control" v-model="contentMemo" v-autotrim ref="memo"></textarea>
      <button type="button" class="btn btn-dark" @click="saveMemo">Save</button>
    </div>

    <div class="reply-count">
      <div class="row justify-content-between align-items-center">
        <div class="col-auto">
          <div class="count">
            <span>Memo</span>
            <strong>{{ totalMemos }}</strong>
            <!--            <button type="button" aria-label="새로고침" @click="getMemosByFeed"><i class="icon-refresh"></i></button>-->
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
      <div>
        <li class="mb-2" v-for="memo in memos" :key="memo.id">
          {{ memo.content }}
          <div>
            <span class="date">{{ memo.createdDate | formatDate }}</span>
          </div>
        </li>
      </div>
    </div>
  </section>
</template>

<script lang="ts" src="./feed-memo.component.ts"></script>

<style scoped>
.date {
  position: relative;
  left: 23px;
  font-weight: 500;
  font-size: 12px;
  color: #a9a9a9;
}
</style>
