<template>
  <li class="lc-card-group" aria-selected="false">
    <b-card v-if="loading">
      <b-skeleton animation="fade" width="85%"></b-skeleton>
      <b-skeleton animation="fade" width="55%"></b-skeleton>
      <b-skeleton animation="fade" width="70%"></b-skeleton>
    </b-card>
    <div class="group-wrap" v-else>
      <div class="content-box">
        <div class="lc-check">
          <button type="button" class="btn btn-check"></button>
        </div>
        <div class="lc-media">
          <a class="media-gird">
            <div v-for="(img, index) in images" :key="'image' + index">
              <div><img :src="img" /></div>
            </div>
          </a>
          <div class="media-body">
            <a class="media-title" @click="showListCard()" style="-webkit-line-clamp: 3"
              >{{ item.collectionId }}
              <small>{{ '(' + item.documentIdList.length + ')' }}</small>
            </a>
            <div class="media-info">
              <div class="info-item">3 days ago</div>
            </div>
          </div>
        </div>
        <div class="lc-btn">
          <button type="button" class="btn btn-icon btn-more"><i class="icon-common icon-pin-on"></i></button>
          <div class="more-area">
            <a class="btn-more" href="#" role="button" data-toggle="dropdown" aria-expanded="false"
              ><i class="icon-common icon-more"></i
            ></a>
            <div class="dropdown-menu dropdown-menu-right">
              <a class="dropdown-item" href="#"><i class="icon-common icon-pin-off"></i>Unpin the top</a>
              <a class="dropdown-item" href="#"><i class="icon-common icon-folder-edit"></i>Edit</a>
              <a class="dropdown-item" href="#"><i class="icon-common icon-trash"></i>Delete</a>
            </div>
          </div>
        </div>
      </div>
      <div class="tag-box">
        <div class="scroll-area">
          <div class="tag-list">
            <a class="tag-item" href="#" v-for="(tag, index) in item.keywordList" :key="'tag' + index">{{ tag }}</a>
          </div>
        </div>
        <div class="btn-wrap">
          <button type="button" class="btn btn-list-more"></button>
        </div>
      </div>
    </div>
  </li>
</template>

<script src="./group-card.component.ts" lang="ts"></script>
