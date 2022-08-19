<template>
  <div class="selected-item-bar" :class="isShow ? 'show' : ''">
    <div class="cart-status">
      <button type="button" @click="isShow = !isShow" aria-label="Cart">
        <i class="icon-check-primary"></i>
        <span class="num">{{ data.length }}</span>
      </button>
    </div>
    <div class="bar" v-if="!hasLearning">
      <div class="row justify-content-between">
        <div class="col">
          <div class="selected-item">
            <ul>
              <li v-for="(item, index) in data" :key="index">
                <a class="tit" data-toggle="modal">{{ item.title || item.author || item.url || item.name }}</a>
                <a @click="removeItem(item)" role="button" aria-label="삭제" class="del"><i class="icon-close"></i></a>
              </li>
            </ul>
          </div>
        </div>
        <div class="col-auto">
          <div class="d-flex">
            <button type="button" class="btn btn-outline-dark min-width-130px" @click="handleNext">Next</button>
            <button type="button" class="btn btn-dark min-width-130px m-l-10" @click="learning" v-if="isLearning">Learn</button>
            <b-button disabled v-if="!isLearning">
              <b-spinner small type="grow"></b-spinner>
              Learning...
            </b-button>
          </div>
        </div>
      </div>
    </div>
    <selected-learning v-else></selected-learning>
  </div>
</template>

<script lang="ts" src="./selected.component.ts"></script>
