<template>
  <div class="tab-pane fade show active" id="lc-add-document">
    <div class="panel-collection">
      <div class="panel-header">
        <div class="panel-title">
          <strong class="title">Documents</strong>
        </div>
      </div>
      <div class="panel-body">
        <div class="add-input-area">
          <div class="learning-tool">
            <ul class="learning-list">
              <li class="item-pc">
                <a @click="openChooseFile">
                  <span class="icon-area"><i class="bi bi-tv"></i></span>
                  <span class="name">내 컴퓨터</span>
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
                  <span class="name">Google Drive</span>
                </a>
                <div class="dropdown">
                  <button type="button" class="btn" data-toggle="dropdown" aria-expanded="false" id="dropdownMenuButton">
                    <i class="bi bi-three-dots-vertical"></i>
                  </button>
                  <div class="dropdown-menu" aria-labelledby="dropdownMenuButton">
                    <a class="dropdown-item" href="#">수정</a>
                    <a class="dropdown-item" href="#">삭제</a>
                  </div>
                </div>
              </li>
              <li class="item-add dropdown">
                <a type="button" class="btn" data-toggle="dropdown" aria-expanded="false">
                  <span class="icon-area"><i class="bi bi-plus-lg"></i></span>
                  <span class="name">추가</span>
                </a>
                <div class="dropdown-menu">
                  <ul>
                    <li class="item-channel">
                      <a class="item-link" href="#">
                        <span class="icon-area"><i class="icon-google-drive"></i></span>
                        <span class="name">Google Drive</span>
                      </a>
                    </li>
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
        <div class="lc-list-top">
          <div class="elements-left">
            <div class="check-group">
              <button
                id="btnSelect"
                type="button"
                :class="['btn btn-check', { active: checked == true }]"
                data-toggle="button"
                aria-pressed="false"
                @click="selectAll"
              ></button>
              <label for="list-check-all4">전체 선택</label>
            </div>
            <div class="list-info">
              (<strong>{{ totalSelected }}</strong
              >/{{ uploadQueueFiles.length }})
            </div>
          </div>
          <div class="elements-right">
            <button type="button" class="btn btn-default btn-sm"><i class="icon-common icon-close"></i>선택 삭제</button>
          </div>
        </div>
        <div class="lc-list-wrap">
          <vue-custom-scrollbar class="list-inner overflow-y-scroll customScroll csScrollPosition ps" :settings="scrollSettings">
            <ul class="lc-card-list">
              <li class="lc-card-item" aria-selected="false" v-for="(file, key) of uploadQueueFiles" :key="key">
                <single-card :document="file" :selectedItems="selectedItems" @setSelectedItems="setSelectedItems"></single-card>
              </li>
            </ul>
          </vue-custom-scrollbar>
        </div>
      </div>
      <div class="panel-footer">
        <div class="elements-right">
          <button type="button" class="btn btn-secondary">Add to current collection</button>
        </div>
      </div>
    </div>
  </div>
</template>
<script lang="ts" src="./document.component.ts"></script>
