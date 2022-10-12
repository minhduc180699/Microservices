<template>
  <div class="tab-pane fade show active" id="lc-add-document">
    <div class="panel-LS-content">
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
        <div class="list-top">
          <div class="elements-left">
            <div class="form-group">
              <button
                type="button"
                :class="['btn btn-check', countActive == uploadQueueFiles.length ? 'focus active' : '']"
                id="list-check-all"
                data-toggle="button"
                aria-pressed="false"
              ></button>
              <label for="list-check-all">Select all</label>
            </div>
            <div class="list-info">
              (<strong>{{ countActive }}</strong
              >/{{ uploadQueueFiles.length }})
            </div>
          </div>
          <div class="elements-right">
            <button type="button" class="btn btn-default btn-sm"><i class="icon-common icon-close"></i>선택 삭제</button>
          </div>
        </div>
        <div class="list-wrap">
          <vue-custom-scrollbar class="list-inner overflow-y-scroll customScroll csScrollPosition ps" :settings="scrollSettings">
            <ul class="lc-card-list lc-resource-list" :key="updateList">
              <li class="lc-card-item" aria-selected="true" v-for="(file, key) of uploadQueueFiles" :key="key">
                <div class="item-wrap">
                  <div class="content-box">
                    <div class="lc-check">
                      <button
                        type="button"
                        :class="['btn btn-check', file.selected ? 'focus active' : '']"
                        data-toggle="button"
                        aria-pressed="true"
                        @click="selectCard(key)"
                      ></button>
                    </div>
                    <div class="lc-media">
                      <a class="media-file" href="#">
                        <img v-bind:src="file.name | fileExtension" />
                      </a>
                      <div class="media-body">
                        <a class="media-title" href="#">{{ file.name }}</a>
                        <div class="media-info">
                          <div class="info-item">
                            <div class="source">
                              <div class="source-img" v-if="file.document == 'drive'">
                                <img src="content/images/icon-google-drive.png" />
                              </div>
                              {{ file.document == 'drive' ? 'Google Driver' : 'My Computer' }}
                            </div>
                          </div>
                          <div class="info-item">{{ file.size | fileSize }}</div>
                          <div class="info-item">3일전</div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </li>
            </ul>
          </vue-custom-scrollbar>
        </div>
      </div>
      <div class="panel-footer">
        <div class="elements-right">
          <button type="button" class="btn btn-secondary" :disabled="disableButton" @click="addToCollection">Add to collection</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./add-document.component.ts"></script>
