<template>
  <div class="tab-pane show active" id="lc-document">
    <div class="lc-content-top">
      <div class="title">
        <h1>{{ $t('learningCenter.title') }}</h1>
        <h2><i class="bi bi-file-earmark-text"></i>{{ $t('learningCenter.document') }}</h2>
      </div>
      <!--      <ul class="nav">-->
      <!--        <li class="nav-item">-->
      <!--          <a :class="showSearch ? 'nav-link active' : 'nav-link'" data-toggle="tab" @click="showSearchLayer">-->
      <!--            {{ $t('learningCenter.learningCompleteList') }}-->
      <!--          </a>-->
      <!--        </li>-->
      <!--        <li class="nav-item">-->
      <!--          <a :class="showSearch ? 'nav-link' : 'nav-link active'" data-toggle="tab" @click="showSearchLayer"-->
      <!--            ><i class="bi bi-plus-lg"></i> {{ $t('learningCenter.addNewDocument') }}-->
      <!--          </a>-->
      <!--        </li>-->
      <!--      </ul>-->
    </div>
    <div class="tab-content">
      <div class="tab-pane show active" id="learning-document-add">
        <doc-add-new v-on:handleSelectedDocument="handleSelectedDocument"></doc-add-new>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./document.component.ts"></script>
