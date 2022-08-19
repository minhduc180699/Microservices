<template>
  <div>
    <div class="lc-content-add">
      <div class="learning-tool">
        <ul class="learning-list">
          <li class="item-pc">
            <a @click="openChooseFile">
              <span class="icon-area"><i class="bi bi-tv"></i></span>
              <span class="name"> {{ $t('learningCenter.myComputer') }} </span>
            </a>
            <input
              type="file"
              id="fileUpload"
              name="fileUpload"
              @input="uploadFromPC($refs.file['files'])"
              ref="file"
              class="d-none"
              multiple
              :accept="acceptUploadFile"
            />
          </li>
          <li class="item-channel">
            <a class="item-link" @click="googleSignin">
              <span class="icon-area"><i class="icon-google-drive"></i></span>
              <span class="name">{{ $t('learningCenter.googleDrive') }}</span>
            </a>
            <div class="dropdown">
              <button type="button" class="btn" data-toggle="dropdown" aria-expanded="false" id="dropdownMenuButton">
                <i class="bi bi-three-dots-vertical"></i>
              </button>
              <div class="dropdown-menu" aria-labelledby="dropdownMenuButton">
                <a class="dropdown-item" href="#">{{ $t('learningCenter.modify') }}</a>
                <a class="dropdown-item" href="#">{{ $t('learningCenter.delete') }}</a>
              </div>
            </div>
          </li>
          <li class="item-add dropdown" @click="showAddition">
            <a type="button" class="btn" data-toggle="dropdown" aria-expanded="false">
              <span class="icon-area"><i class="bi bi-plus-lg"></i></span>
              <span class="name">{{ $t('learningCenter.add') }}</span>
            </a>
            <div :class="addition ? 'dropdown-menu show' : 'dropdown-menu'">
              <ul>
                <!--                <li class="item-channel">-->
                <!--                  <a class="item-link" href="#">-->
                <!--                    <span class="icon-area"><i class="icon-google-drive"></i></span>-->
                <!--                    <span class="name">Google Drive</span>-->
                <!--                  </a>-->
                <!--                </li>-->
                <li class="item-channel">
                  <a href="#">
                    <span class="icon-area"><i class="icon-dropbox"></i></span>
                    <span class="name">Dropbox</span>
                  </a>
                </li>
                <li class="item-channel">
                  <a href="#">
                    <span class="icon-area"><i class="icon-one-drive"></i></span>
                    <span class="name">One Drive</span>
                  </a>
                </li>
                <li class="item-channel">
                  <a href="#">
                    <span class="icon-area"><i class="icon-icloud"></i></span>
                    <span class="name">iCloud</span>
                  </a>
                </li>
              </ul>
            </div>
          </li>
        </ul>
      </div>
    </div>
    <div class="lc-content-body">
      <div class="top-bar">
        <div class="top-title">
          <strong>{{ $t('learningCenter.learnMoreList') }}</strong>
          <span class="total"
            >{{ $t('learningCenter.total') }}<em>{{ (uploadQueueFiles && uploadQueueFiles.length) || 0 }}</em
            >{{ $t('learningCenter.items') }}</span
          >
          <button v-if="uploadQueueFiles && uploadQueueFiles.length > 0" @click="deleteAllDoc" class="btn btn-text" type="button">
            {{ $t('learningCenter.deleteAll') }}<i class="bi bi-x-lg"></i>
          </button>
        </div>
        <div class="top-elements">
          <!--          <button class="btn btn-primary" type="button" :disabled="!uploadQueueFiles || uploadQueueFiles.length === 0" @click="addLearning">-->
          <!--            <i class="bi bi-bag-plus"></i> {{ $t('learningCenter.addLearning') }}-->
          <!--          </button>-->
          <button
            class="btn btn-secondary"
            type="button"
            :disabled="uploadQueueFiles.length <= 0 && !this.$store.getters.getLearning ? true : false"
            @click="startLearning"
          >
            <i class="bi bi-file-play"></i> {{ isLearning ? $t('learningCenter.learning') : $t('learningCenter.toLearn') }}
          </button>
        </div>
      </div>
      <div class="resource-list-wrap">
        <div class="no-content" @dragover.prevent="dragover" @dragleave="dragleave" @drop.prevent="drop">
          <i class="bi bi-file-earmark-text"></i>
          <strong v-if="!uploadQueueFiles || uploadQueueFiles.length === 0">
            {{ $t('learningCenter.noNewDocument') }}
          </strong>
          <p v-html="$t('learningCenter.addFileFromComputerOrDraggingFile')"></p>
        </div>
        <div style="color: red" v-if="isShowNotice">
          <span
            ><p>{{ $t('learningCenter.unsupportedFileType') }}</p>
            {{ acceptUploadFile }}
          </span>
        </div>
        <doc-wrap-list
          :docLists="uploadQueueFiles"
          @removeFile="removeFile($event)"
          :parent="parent"
          v-on:handleSelectedDocument="handleSelectedDocument"
        ></doc-wrap-list>
      </div>
    </div>
  </div>
</template>
<script lang="ts" src="./document-add-new.component.ts"></script>
