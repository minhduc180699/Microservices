<template>
  <div>
    <div id="lc-wrap">
      <header-learning v-on:closeModal="closeModal"></header-learning>
      <div id="lc-container">
        <aside-learning v-on:changeTab="changeTab" ref="handleNext"></aside-learning>
        <main id="lc-content">
          <div class="tab-content" id="nav-tabContent">
            <search-learning
              :display="display"
              v-on:showSelectedSearch="showSelectedSearch"
              v-on:removeItemSearch="removeItemSearch"
              :keywords="keywords"
            ></search-learning>
            <web-learning :display="display" v-on:showSelectedWeb="showSelectedWeb" v-on:removeItemWeb="removeItemWeb"></web-learning>
            <document :display="display" v-on:showSelectedDoc="showSelectedDoc" v-on:removeItemDoc="removeItemDoc"></document>
          </div>
        </main>
      </div>
    </div>
    <selected
      v-on:removeItemSearch="removeItemSearch"
      :itemSelectedMetaSearch="itemSelectedMetaSearch"
      :itemsSelectedInternalSearch="itemsSelectedInternalSearch"
      v-on:removeItemWeb="removeItemWeb"
      :itemsSelectedWeb="itemsSelectedWeb"
      v-on:handleNext="handleNext"
      v-on:removeItemDoc="removeItemDoc"
      :itemSelectedDocument="itemSelectedDocument"
      v-on:showToastSuccess="showToastSuccess"
      v-on:showToastFailure="showToastFailure"
    ></selected>
  </div>
</template>

<script lang="ts" src="./layout.component.ts"></script>
