<template>
  <!--    <b-card v-if="loading">-->
  <!--      <b-skeleton animation="fade" width="85%"></b-skeleton>-->
  <!--      <b-skeleton animation="fade" width="55%"></b-skeleton>-->
  <!--      <b-skeleton animation="fade" width="70%"></b-skeleton>-->
  <!--    </b-card>-->
  <div class="group-wrap">
    <div class="content-top">
      <div class="lc-check" id="customInput">
        <input type="checkbox" name="selected-items" @change="selectGroupCard" @click="selectGroupCard(collection)" />
      </div>
      <div class="media-info">
        <a class="info-item info-title" @click.prevent="toggleGroupCollection(collection)"
          >{{ collection.id }}<small>{{ '(' + collection.totalDocuments + ')' }}</small></a
        >
        <div class="info-item">3일전</div>
      </div>
      <div class="lc-btn">
        <div class="more-area">
          <a class="btn-more" href="#" role="button" data-toggle="dropdown" aria-expanded="false"><i class="icon-common icon-more"></i></a>
          <div class="dropdown-menu dropdown-menu-right">
            <a class="dropdown-item" href="#"><i class="icon-common icon-pin"></i>Top fixed</a>
            <a class="dropdown-item" href="#"><i class="icon-common icon-folder-edit"></i>Edit</a>
            <a class="dropdown-item" href="#"><i class="icon-common icon-trash"></i>Delete</a>
          </div>
        </div>
      </div>
    </div>
    <div class="content-box">
      <div class="lc-media">
        <a class="media-gird">
          <div v-for="image in collection.images" :key="image">
            <img :src="image" />
          </div>
        </a>
        <div class="media-body"></div>
      </div>
    </div>
    <div class="tag-box" v-if="collection.tags && collection.tags.length">
      <div class="scroll-area">
        <div class="tag-list" v-for="item in collection.tags" :key="item">
          <a class="tag-item" href="#">{{ item }}</a>
        </div>
      </div>
      <div class="btn-wrap">
        <button type="button" class="btn btn-list-more"></button>
      </div>
    </div>
  </div>
</template>

<script src="./group-card.component.ts" lang="ts"></script>
