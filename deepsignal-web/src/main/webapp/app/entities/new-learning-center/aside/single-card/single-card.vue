<template>
  <div class="item-wrap">
    <div class="content-top">
      <div class="lc-check" id="customInput" v-show="!isHideCheck">
        <input
          type="checkbox"
          name="selected-items"
          :aria-pressed="isChecked ? 'true' : 'false'"
          v-model="internalSelectedItems"
          @change="internalItemsChange"
          :value="document"
        />
      </div>
      <div class="media-info">
        <div class="info-item">
          <div class="source">
            <div class="source-img">
              <img v-if="document.favicon" :src="document.favicon" alt="" />
              <img v-else :src="`https://www.google.com/s2/favicons?domain=${document.url}`" alt="" />
            </div>
            {{ document.author + '' }}
          </div>
        </div>
        <div class="info-item" v-if="document.addedAt && !document.noConvertTime">
          {{ checkRegexDate(document.addedAt) | formatDate }}
        </div>
        <div class="info-item" v-if="document.addedAt && document.noConvertTime">{{ document.addedAt }}</div>
      </div>
      <div class="lc-btn" v-if="isHideCheck">
        <a class="btn-close" @click="removeCard(document)">
          <i class="icon-common icon-close"></i>
        </a>
      </div>
      <div v-else class="lc-btn">
        <div class="more-area position-relative">
          <a class="btn-more" href="#" role="button" data-toggle="dropdown" aria-expanded="false"><i class="icon-common icon-more"></i></a>
          <div class="dropdown-menu dropdown-menu-right" style="">
            <a class="dropdown-item" href="#"><i class="icon-common icon-pin"></i>Top fixed</a>
            <a class="dropdown-item" @click="handleDelete"> <i class="icon-common icon-trash"></i>Delete </a>
          </div>
        </div>
        dropdown-menu-right
      </div>
    </div>
    <div class="content-box">
      <div class="lc-media">
        <div class="media-body">
          <a class="media-title" href="#"><p v-html="document.title" style="max-width: 170px"></p></a>
          <p class="media-desc" v-html="document.content"></p>
        </div>
        <a class="media-img" href="#" id="image">
          <div v-if="document.searchType === 'VIDEO'" class="icon-play"></div>
          <img
            v-if="document.images && document.images.length > 0 && document.images[0]"
            :src="document.images[0]"
            alt=""
            style="width: 100%"
            @error="['content/images/empty-image.png']"
          />
          <img v-else src="content/images/empty-image.png" alt="" style="width: 100%" @error="['content/images/empty-image.png']" />
        </a>
      </div>
    </div>
    <div class="tag-box" v-if="document.keyword || (document.tags && document.tags.length > 0)">
      <div class="scroll-area">
        <div class="tag-list" v-if="document.tags && document.tags.length > 0">
          <a class="tag-item" v-for="(element, index) in document.tags" :key="index" :href="element.tag_link"> {{ element.tag_name }}</a>
        </div>
        <div class="tag-list" v-else>
          <a class="tag-item" v-for="(element, index) in document.keyword.split(',')" :key="index">{{ element }}</a>
        </div>
      </div>
      <div class="btn-wrap">
        <button type="button" class="btn btn-list-more" @click="showTag($event)"></button>
      </div>
    </div>
  </div>
</template>

<script src="./single-card.component.ts" lang="ts"></script>

<!-- <div class="item-wrap">
    <div class="content-top">
      <div class="lc-check">
        <button
          type="button"
          class="btn btn-check active focus"
          data-toggle="button"
          aria-pressed='true'
          @click="handleClickSingleCard"
        ></button>
      </div>
      <div class="media-info">
        <div class="info-item">
          <div class="source">
            <div class="source-img">
              <img v-if="document.favicon" :src="document.favicon" />
            </div>
            {{ document.author ? document.author : '' }}
          </div>
        </div>
        <div class="info-item">3??????</div>
      </div>
      <div class="lc-btn">
        <div class="more-area">
          <a class="btn-more" href="#" role="button" data-toggle="dropdown" aria-expanded="false"><i class="icon-common icon-more"></i></a>
          <div class="dropdown-menu dropdown-menu-right">
            <a class="dropdown-item" href="#"><i class="icon-common icon-pin"></i>Top fixed</a>
            <a class="dropdown-item" href="#"><i class="icon-common icon-trash"></i>Delete</a>
          </div>
        </div>
      </div>
    </div>
    <div class="content-box">
      <div class="lc-media">
        <div class="media-body">
          <a class="media-title" href="#" v-if="document.title" v-text="document.title">
            ????????? ??????, ????????? ????????? ????????? ????????? ????????? ?????? ????????? ??????.
          </a>
          <p class="media-desc" v-if="document.content" v-text="document.content">
            ??????????????? ????????? ???????????? ????????? ???????????? ????????? ??????, ????????? ?????????. ????????? ????????? ???????????? ???????????????. ?????? ?????? ?????????
            ????????? ?????? ?????????
          </p>
        </div>
        <a class="media-img" href="#">
          <img v-if="document.images&&document.images.length > 0" :src="document.images" />
          <img v-else-if="document.img&&document.img.length > 0" :src="document.img" />
        </a>
      </div>
    </div>
    <div class="tag-box">
      <div class="scroll-area">
        <div class="tag-list" v-for="item in document.tags" :key="item">
          <a class="tag-item" href="#">{{ item }}</a>
        </div>
      </div>
      <div class="btn-wrap">
        <button type="button" class="btn btn-list-more"></button>
      </div>
    </div>
  </div> -->
