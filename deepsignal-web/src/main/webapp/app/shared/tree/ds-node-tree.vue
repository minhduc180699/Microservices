<template>
  <li>
    <div class="profile">
      <img :src="node.userDTO.imageUrl" @error="node.userDTO.imageUrl = 'content/images/profile-sz40.png'" :alt="node.name" />
    </div>
    <div class="text pr-6">
      <div class="name">{{ node.userDTO.fullName }}</div>
      <div class="comment">{{ node.content | lmTo(256) }}</div>
      <!--        <div class="comment">-->
      <!--          <b-button variant="link">Reply</b-button>-->
      <!--          <div class="search-main-wrap w-100">-->
      <!--            <div class="search-inner">-->
      <!--              <input class="form-control" type="text" v-model="contentReplyComment" v-autotrim />-->
      <!--              <button class="btn btn-search" aria-label="search_button" @click="reply(item)">-->
      <!--                <i class="bi bi-send"></i>-->
      <!--              </button>-->
      <!--            </div>-->
      <!--          </div>-->
      <!--        </div>-->
      <div class="d-flex align-items-baseline">
        <b-button variant="link" @click="onReply(node)">Reply</b-button>
        <span class="date">{{ node.createdDate | formatDate }}</span>
      </div>
      <!--            <div class="response-btns">-->
      <!--              <button type="button" aria-label="댓글 찬성">-->
      <!--                <i class="icon-thumb-up"></i>-->
      <!--                <span class="num">80</span>-->
      <!--              </button>-->
      <!--              <button type="button" aria-label="댓글 비추천">-->
      <!--                <i class="icon-thumb-down"><span class="sr-only"></span></i>-->
      <!--                <span class="num">80</span>-->

      <!--              </button>-->
      <!--            </div>-->
      <!--      <div :class="['re-more', item.showMoreActionMemo ? 'active' : '']">-->
      <!--        <button type="button" aria-label="more" @click="item.showMoreActionMemo = !item.showMoreActionMemo">-->
      <!--          <i class="icon-more"></i>-->
      <!--        </button>-->
      <!--        <div class="tooltip-menu" v-show="item.showMoreActionMemo">-->
      <!--          <ul>-->
      <!--            <li><a href="#none">Follow this User</a></li>-->
      <!--            <li><a href="#none">Block this User</a></li>-->
      <!--          </ul>-->
      <!--        </div>-->
      <!--      </div>-->
      <ul v-if="node.children && node.children.length" class="reply-list">
        <ds-node v-for="(child, index) in node.children" :node="child" :key="index" @onReply="onReply($event)"></ds-node>
      </ul>
    </div>
  </li>
  <!--  </ul>-->
</template>

<script lang="ts">
import { Component, Prop, Vue } from 'vue-property-decorator';

@Component({
  name: 'ds-node',
})
export default class DsNodeTree extends Vue {
  @Prop(Object) readonly node: any | undefined;

  onReply(node) {
    this.$emit('onReply', node);
  }
}
</script>
