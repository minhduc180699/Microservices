<template>
  <div class="col-xl-12">
    <section v-show="isShow" class="detail-social">
      <div class="panel panel-content panel-default panel-collapse" style="padding-left: 1.25rem; padding-right: 1.25rem">
        <div class="panel-header">
          <ul class="nav">
            <li class="nav-item" v-for="(item, index) of serviceTypes" :key="index">
              <a
                class="nav-link"
                @click.prevent="choseServiceType(item.serviceType)"
                :class="{ active: index === currentServiceType ? 'active' : '' }"
                data-toggle="tab"
              >
                {{ item.serviceType }}
              </a>
            </li>
          </ul>
        </div>
        <div class="panel-body">
          <div class="tab-content">
            <div class="top-bar">
              <div class="wrap">
                <i><img :src="peopleOrCompany.imageUrl" width="20px" height="20px" style="border-radius: 50%" /></i>
                <div class="title">
                  <router-link :to="{ path: '/peopleDetail', query: { name: peopleOrCompany.title } }" class="title">
                    <a>{{ peopleOrCompany.title }}</a>
                  </router-link>
                </div>
                <!-- <div class="info">
                  <span><a href="#">@follow_leejunho</a></span>
                </div> -->
              </div>
              <div class="wrap">
                <div class="info">
                  <span v-text="$t('people.detail.last20Post')">Last 20 Posts</span>
                </div>
              </div>
            </div>
            <div class="list-wrap overflow-y-scroll">
              <vue-custom-scrollbar
                class="customScrollSocialPeople csScrollPositon"
                :settings="scrollSettings"
                @ps-scroll-y="scrollHandle($event)"
              >
                <ul class="social-media-list">
                  <li v-for="(item, index) of socialPeoplesLoad" :key="index" class="media" style="padding-right: 2rem">
                    <a class="media-thumb" :href="item.link">
                      <img :src="peopleOrCompany.imageUrl" />
                    </a>
                    <div class="media-body">
                      <div class="top-wrap">
                        <div class="title">
                          <a :href="item.link">{{ item.author }}</a>
                        </div>
                        <!-- <div class="info">
                          <span class="id"><a href="#">@follow_leejunho</a></span>
                        </div> -->
                      </div>
                      <div class="content-wrap">
                        <a :href="item.link"><p v-html="item.content"></p></a>
                      </div>
                      <div class="info">
                        <span class="time">{{ item.orgDate | onlyTime }}</span>
                        <span class="date">{{ item.orgDate | onlyDate }}</span>
                      </div>
                    </div>
                  </li>
                </ul>
              </vue-custom-scrollbar>
            </div>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>
<script lang="ts" src="./ds-card-people-social.component.ts"></script>
<style>
.detail-social .panel-content .panel-body {
  border-bottom: none !important;
  display: unset;
}
.customScrollSocialPeople {
  max-height: 400px;
}
.media-thumb > img {
  object-fit: contain;
}
ps--active-x > .ps__rail-x,
.ps--active-y > .ps__rail-y {
  z-index: 50 !important;
}
</style>
