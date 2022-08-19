<template>
  <div :class="display == 2 ? 'tab-pane fade active show' : 'tab-pane fade'" id="resource-document" aria-labelledby="resource-document-tab">
    <div class="content-top">
      <div class="container">
        <strong class="title"
          ><i class="bi bi-folder2"></i>Document<small v-text="$t('document.description')"
            >문서를 등록하여 커넴톰을 학습 시켜주세요.</small
          ></strong
        >
        <div class="learning-tool">
          <div class="list-group list-group-checkable">
            <ul>
              <li class="active">
                <a @click="$refs.file.click()">
                  <span class="ico"
                    ><span class="circle"><i class="icon-pc"></i></span
                  ></span>
                  <span class="name">My PC</span>
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
              <li class="checked">
                <a href="">
                  <span class="ico"
                    ><span class="circle"><i class="icon-google-drive"></i></span
                  ></span>
                  <span class="name">Google Drive</span>
                </a>
              </li>
              <li class="locked">
                <a href="">
                  <span class="ico"
                    ><span class="circle"><i class="icon-dropbox"></i></span
                  ></span>
                  <span class="name">Dropbox</span>
                </a>
              </li>
              <li class="locked">
                <a href="">
                  <span class="ico"
                    ><span class="circle"><i class="icon-one-drive"></i></span
                  ></span>
                  <span class="name">One Drive</span>
                </a>
              </li>
              <li class="locked">
                <a href="">
                  <span class="ico"
                    ><span class="circle"><i class="icon-icloud"></i></span
                  ></span>
                  <span class="name">icloud</span>
                </a>
              </li>
            </ul>
          </div>
        </div>
      </div>
    </div>
    <div class="container">
      <div class="resource-wrap">
        <div class="resource-top">
          <strong class="title"
            >List to add<small>{{ '(' + numberOfFile + ')' }}</small></strong
          >
          <div class="top-elements">
            <div>
              <button
                type="button"
                class="btn btn-outline-lightgray btn-sm btn-toggle btn-allcheck"
                :class="isSelectAll ? 'active' : ''"
                data-toggle="button"
                aria-pressed="false"
                @click="selectAll"
              >
                <i class="bi bi-check2 mr-1" v-text="$t('web.all')"></i><span class="on" v-text="$t('web.select')"></span
                ><span class="off" v-text="$t('web.remove')">해제</span>
              </button>
            </div>
          </div>
        </div>
        <ul class="resource-list list-row">
          <li
            class="list-item"
            :aria-selected="isSelected.indexOf(index) > -1 ? 'true' : 'false'"
            v-for="(file, index) of uploadQueueFiles"
            :key="index"
          >
            <div class="resource-card type-document">
              <div class="card-content">
                <div class="info">
                  <div class="source-thumb">
                    <img src="content/images/media-chosun.png" alt="" />
                  </div>
                  <span class="source-text"><em>My PC</em></span>
                </div>
                <div class="file-inner">
                  <span class="file-icon"><img v-bind:src="file.name | fileExtension" alt="" /></span>
                  <a class="title">{{ file.name }}</a>
                  <small class="size">{{ file.size | fileSize }}</small>
                </div>
              </div>
            </div>
            <button type="button" class="btn btn-check" @click="selectCard(index)"><i class="icon-check-gray"></i></button>
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./document.component.ts"></script>
