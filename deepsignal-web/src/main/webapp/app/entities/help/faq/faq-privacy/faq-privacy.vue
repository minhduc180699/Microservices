<template>
  <div>
    <div class="accordion-list">
      <ul>
        <li v-for="item in faqs" :key="item.faq.faqId" :class="{ active: item.isActive }">
          <a @click="item.isActive = !item.isActive">
            <div class="q">
              <!--              <div>{{ index + 1 }}</div>-->
              <div class="cate">{{ item.faq.category.code }}</div>
              <div class="tit">{{ item.faq.question }}</div>
            </div>
            <div class="a">
              {{ item.faq.answer }}
            </div>
          </a>
        </li>
      </ul>
    </div>
    <div class="board-more">
      <a @click="loadMore()" href="javascript:;">회원관리 더보기</a>
    </div>
    <!--    <h1>{{}}</h1>-->
  </div>
</template>

<script lang="ts" src="./faq-privacy.component.ts"></script>
