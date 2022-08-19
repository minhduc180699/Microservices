<template>
  <div class="selected-item-bar" :class="isShow ? 'show' : ''" v-if="itemsSelected">
    <div class="cart-status">
      <button type="button" @click="isShow = !isShow" aria-label="Cart">
        <i class="icon-check-primary"></i>
        <span class="num">{{
          itemsSelected.length + itemsSelectedSearch.length + itemsSelectedWeb.length + this.itemSelectedMetaSearch.length || 0
        }}</span>
      </button>
    </div>
    <div class="bar" v-if="!hasLearning">
      <div class="row justify-content-between">
        <div class="col">
          <div class="selected-item">
            <ul>
              <li v-for="(item, index) in itemsSelected" :key="'itemSelected' + index">
                <a class="tit" data-toggle="modal">{{ item.title }}</a>
                <a @click="removeItem(item)" role="button" aria-label="삭제" class="del"><i class="icon-close"></i></a>
              </li>
              <li v-for="(item, index) in itemsSelectedSearch" :key="'itemsSelectedSearch' + index">
                <a class="tit" data-toggle="modal">{{ item.fields.title[0] || item.fields.writer[0] }}</a>
                <a @click="removeItemSearch(item)" role="button" aria-label="삭제" class="del"><i class="icon-close"></i></a>
              </li>
              <li v-for="(item, index) in itemSelectedMetaSearch" :key="'itemSelectedMetaSearch' + index">
                <a class="tit" data-toggle="modal">{{ item.title || item.author }}</a>
                <a @click="removeItemSearch(item)" role="button" aria-label="삭제" class="del"><i class="icon-close"></i></a>
              </li>
              <li v-for="(item, index) in itemsSelectedWeb" :key="'itemsSelectedWeb' + index">
                <a class="tit" data-toggle="modal" v-if="item.url">{{ item.url }}</a>
                <a @click="removeItemWeb(item)" role="button" aria-label="삭제" class="del"><i class="icon-close"></i></a>
              </li>
            </ul>
          </div>
        </div>
        <div class="col-auto">
          <div class="d-flex">
            <button type="button" class="btn btn-outline-dark min-width-130px" @click="handleNext">Next</button>
            <button type="button" class="btn btn-dark min-width-130px m-l-10" @click="learning" v-if="isloading">Learn</button>
            <b-button disabled v-if="!isloading">
              <b-spinner small type="grow"></b-spinner>
              Learning...
            </b-button>
          </div>
        </div>
      </div>
    </div>
    <meta-search-learning v-else></meta-search-learning>
  </div>
</template>

<script lang="ts" src="./meta-search-selected.component.ts"></script>
