<template>
  <div class="tab-pane fade show active" id="lc-add-url">
    <div class="panel-LS-content">
      <div class="panel-body">
        <div class="add-input-area">
          <div class="form-group url-group">
            <input
              class="form-control"
              type="url"
              placeholder="URL을 입력하거나 복사한 URL을 붙여 넣으세요."
              v-model="searchValue"
              v-on:keydown.enter.prevent="preview"
            />
            <button class="form-btn" type="button">
              <i class="icon-common icon-link-pirmary" v-if="!isShowSpinner" @click="preview"></i>
              <b-spinner v-else variant="primary" label="Spinning"></b-spinner>
            </button>
          </div>
        </div>
        <div class="list-wrap">
          <div class="list-inner">
            <div class="overflow-y-scroll">
              <ul class="lc-card-list lc-resource-list" :key="updateList">
                <li class="lc-card-item" aria-selected="true" v-for="(model, index) of previewModels" :key="index">
                  <div class="item-wrap">
                    <div class="content-box">
                      <div class="lc-check">
                        <button
                          type="button"
                          :class="['btn btn-check', model.selected ? 'focus active' : '']"
                          data-toggle="button"
                          aria-pressed="false"
                          @click="selectCard(index)"
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
              </ul>
            </div>
          </div>
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

<script lang="ts" src="./add-url.component.ts"></script>
