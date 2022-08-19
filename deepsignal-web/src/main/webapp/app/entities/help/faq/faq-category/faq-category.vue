<template>
  <div>
    <h1 class="page-title">자주하는 질문</h1>
    <div class="board-search">
      <div class="row row-10">
        <div class="col">
          <div class="input-area">
            <input
              @keyup.enter="searchKeyWord()"
              type="text"
              class="form-control"
              placeholder="무엇을 도와드릴까요?"
              v-model="faqModel"
              required
            />
            <!-- <button type="button" aria-label="키워드 삭제"><i class="icon-x-black"></i></button> -->
          </div>
        </div>
        <div class="col-auto">
          <button type="button" class="btn btn-primary" @click="searchKeyWord()">질문하기</button>
        </div>
      </div>
    </div>
    <div class="board-result" v-if="searched">
      <p>
        <strong>“{{ faqModel }}”</strong> 에 대해 <i>{{ totalFaqSearch }}</i> 건이 검색되었습니다.
      </p>
    </div>
    <div class="faq-category">
      <ul>
        <!--        <li><a href="#none">전체</a></li>-->
        <!--        <li class="active"><a href="#none">회원관리</a></li>-->
        <!--        <li><a href="#none">유료서비스</a></li>-->
        <!--        <li><a href="#none">제휴 및 파트너</a></li>-->
        <!--        <li><a href="#none">카테고리</a></li>-->
        <!--        <li><a href="#none">카테고리</a></li>-->
        <!--        <li><a href="#none">카테고리</a></li>-->
        <!--        <li><a href="#none">기타</a></li>-->
        <li v-for="cate in categories" :key="cate.category.code" :class="{ active: cate.isActive }" @click="searchCategory(cate)">
          <a>{{ cate.category.code }}</a>
        </li>
      </ul>
    </div>
  </div>
</template>

<script lang="ts" src="./faq-category.component.ts"></script>
