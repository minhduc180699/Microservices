<template>
  <div class="body-nav fixed-top" style="z-index: 95">
    <div class="top-area">
      <div class="dropdown dropdown-filter">
        <button class="btn btn-sm dropdown-toggle" type="button" id="dropdownMenu2" data-toggle="dropdown" aria-expanded="false">
          {{ tabActive }}
        </button>
        <div class="dropdown-menu" aria-labelledby="dropdownMenu2">
          <button class="dropdown-item" :class="{ active: tabActive.toLowerCase() === 'all' }" type="button" @click="filterPart('All')">
            All
          </button>
          <button
            class="dropdown-item"
            :class="{ active: tabActive.toLowerCase() === 'people' }"
            type="button"
            @click="filterPart('People')"
          >
            People
          </button>
          <button
            class="dropdown-item"
            :class="{ active: tabActive.toLowerCase() === 'company' }"
            type="button"
            @click="filterPart('Company')"
          >
            Company
          </button>
        </div>
      </div>
      <div class="search-group search-sub">
        <input
          @input="searchInput"
          v-model="textSearch"
          class="search-input form-control"
          type="search"
          :placeholder="$t('people.enterSearchTerm')"
        />
        <button class="search-clear" type="button" title="삭제" @click="clearSearch()"></button>
      </div>
    </div>
    <vue-custom-scrollbar class="overflow-yx-scroll csScrollPositon" :settings="scrollSettings">
      <ul class="list-group">
        <li class="list-item" v-for="(item, index) in dataSearch" :key="index">
          <a class="list-link media" data-toggle="list" :class="{ active: cardActive === index }" @click.prevent="changeItem(item, index)">
            <div class="media-body">
              <div class="rank-wrap">
                <span class="rank-num">{{ index + 1 }}</span>
              </div>
              <div :class="item.type === 'PEOPLE' ? 'img-circle' : 'img-rectangle'">
                <img :src="item.imageUrl" @error="item.imageUrl = 'content/images/avatar.png'" />
              </div>
              <!-- remove class "no-info" if uncomment <span class="info"> -->
              <div class="text-wrap no-info">
                <strong class="title">{{ item.title }}</strong>
                <!--                  <span class="info">{{ $t('people.businessmen') }}</span>-->
              </div>
            </div>
          </a>
        </li>
      </ul>
    </vue-custom-scrollbar>
  </div>
</template>

<script src="./ds-card-people-ranking.component.ts" lang="ts"></script>
<style scoped>
@media (min-width: 992px) {
  .responsive-body .body-nav .overflow-yx-scroll .scroll {
    padding-right: 24px;
    height: 520px;
  }
}

@media (max-width: 991.98px) {
  .responsive-body .body-nav .overflow-yx-scroll .scroll {
    padding-bottom: 24px;
  }
  .responsive-body .list-group li:first-child {
    margin-left: 1.5rem;
    padding-left: unset;
  }
}
.no-info {
  display: flex;
  align-items: center;
}
.img-rectangle img {
  height: 40px;
}
</style>
