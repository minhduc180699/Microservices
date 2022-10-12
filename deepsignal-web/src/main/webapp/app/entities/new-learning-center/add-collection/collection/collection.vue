<template>
  <div class="col-5">
    <div class="go-panel panel-LS-content">
      <div class="panel-header" style="border-bottom: 1px solid #efeff0; height: 56px">
        <div class="panel-title">
          <i class="go-icon-common icon-storage"></i>
          <strong style="color: #999; font-weight: 600">Storage</strong>
        </div>
      </div>
      <div class="panel-body" style="padding-top: 88px; padding-bottom: 20px !important">
        <div class="list-top">
          <div class="elements-left">
            <div class="form-group">
              <button
                type="button"
                :class="['btn btn-check', countActive == urlCards.length + documentCards.length ? 'focus active' : '']"
                id="list-check-all"
                data-toggle="button"
                aria-pressed="false"
              ></button>
              <label for="list-check-all">전체 선택</label>
            </div>
            <div class="list-info">
              (<strong>{{ countActive }}</strong
              >/{{ urlCards.length + documentCards.length }})
            </div>
          </div>
          <div class="elements-right">
            <button type="button" class="btn btn-default btn-sm"><i class="icon-common icon-close"></i>선택 삭제</button>
          </div>
        </div>
        <div class="list-wrap">
          <vue-custom-scrollbar class="list-inner overflow-y-scroll customScroll csScrollPosition ps" :settings="scrollSettings">
            <ul class="lc-card-list lc-resource-list" :key="updateData">
              <li class="lc-card-item" aria-selected="true" v-for="(model, index) of urlCards" :key="'url' + index">
                <div class="item-wrap">
                  <div class="content-box">
                    <div class="lc-check">
                      <button
                        type="button"
                        :class="['btn btn-check', model.selected ? 'focus active' : '']"
                        data-toggle="button"
                        aria-pressed="false"
                        @click="selectUrlCard(index)"
                      ></button>
                    </div>
                    <div class="lc-media">
                      <a class="media-img" href="#">
                        <img :src="model.image" @error="model.image = ['content/images/empty-image.png']" />
                      </a>
                      <div class="media-body">
                        <a class="media-title" :href="model.url" target="_blank">{{ model.title ? model.title : '' }}.</a>
                        <div class="media-url">{{ model.url }}</div>
                        <div class="media-info"></div>
                      </div>
                    </div>
                  </div>
                </div>
              </li>
              <li class="lc-card-item" aria-selected="true" v-for="(file, key) of documentCards" :key="'document' + key">
                <div class="item-wrap">
                  <div class="content-box">
                    <div class="lc-check">
                      <button
                        type="button"
                        :class="['btn btn-check', file.selected ? 'focus active' : '']"
                        data-toggle="button"
                        aria-pressed="true"
                        @click="selectDocumentCard(key)"
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
      <div class="panel-footer" style="bottom: 0px !important">
        <button type="button" class="btn btn-primary btn-block" :disabled="disableButton">Save collection</button>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./collection.component.ts"></script>
