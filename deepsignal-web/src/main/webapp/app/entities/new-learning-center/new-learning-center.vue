<template>
  <div id="lc-container">
    <!--    <learning-center-aside></learning-center-aside>-->
    <list-collection @changeBookmarkItems="changeBookmarkItems"></list-collection>
    <collection-cart :bookmarkCardItems="bookmarkCardItems" :currentCollection="getCurrentCollection"></collection-cart>
    <!--    <collection-discovery></collection-discovery>-->
    <connectome></connectome>
  </div>
</template>

<script lang="ts" src="./new-learning-center.component.ts"></script>
