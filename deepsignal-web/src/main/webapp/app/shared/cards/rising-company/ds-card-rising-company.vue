<template>
  <!-- [Rising Company] -->
  <div class="card">
    <div class="card-header">
      <div class="badge">{{ item.title }}</div>
    </div>
    <div class="card-body">
      <a href="" class="d-flex flex-column align-items-center h-100">
        <div class="img">
          <img src="content/images/@people-ci1.png" alt="" />
        </div>
        <div class="company">Naver</div>
        <div class="info">IT Company</div>
      </a>
    </div>
    <ds-card-footer :item="item" />
  </div>
</template>
<script lang="ts" src="./ds-card-rising-company.component.ts"></script>
