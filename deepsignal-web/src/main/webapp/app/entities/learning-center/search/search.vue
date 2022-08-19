<template>
  <div :class="display == 0 ? 'tab-pane fade active show' : 'tab-pane fade'" id="resource-search" aria-labelledby="resource-search-tab">
    <div class="content-top">
      <div class="container">
        <strong class="title"
          ><i class="bi bi-search"></i>Search<small v-text="$t('search.description')"
            >관심있는 컨텐츠를 검색해서 컨텐치를 학습시켜주세요.</small
          ></strong
        >
        <div class="search-group">
          <input
            class="form-control"
            type="text"
            placeholder=""
            aria-label="search_input"
            v-model="queries"
            v-on:keydown.enter.prevent="searching(), (isAddCondition = true)"
          />
          <button class="btn-search" type="button" aria-label="search_button" @click="searching()">
            <i class="bi bi-search"></i>
          </button>
        </div>
        <div class="search-layer search-connectome-autocomplete" style="display: none">
          <div class="layer-body">
            <ul class="search-keyword-list autocomplete">
              <li><a class="keword" href="#">Hedge_<b>fund</b>_firms</a></li>
              <li>
                <a class="keword" href="#">EU <b>fund</b></a>
              </li>
              <li>
                <a class="keword" href="#">US <b>fund</b></a>
              </li>
              <li>
                <a class="keword" href="#"><b>Fund</b>_for_Nature</a>
              </li>
            </ul>
            <div class="no-content" style="display: none">
              <strong>일치하는 검색어가 없습니다.</strong>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="container" v-if="!isShowSpinner">
      <div class="resource-wrap">
        <div class="resource-top">
          <strong class="title"
            >Search Word<small><i class="bi bi-search"></i>{{ queries }}</small></strong
          >
          <div class="top-elements">
            <div class="btn-group">
              <button
                type="button"
                :class="homeInterface ? 'btn btn-outline-lightgray btn-sm active' : 'btn btn-outline-lightgray btn-sm'"
                @click="displayCard"
              >
                <i class="bi bi-view-stacked"></i>
              </button>
              <button
                type="button"
                :class="homeInterface ? 'btn btn-outline-lightgray btn-sm' : 'btn btn-outline-lightgray btn-sm active'"
                @click="displayCard"
              >
                <i class="bi bi-grid"></i>
              </button>
            </div>
          </div>
        </div>
        <div id="list-type-column" v-if="!homeInterface">
          <ul class="resource-list list-column">
            <li
              class="list-item"
              :aria-selected="isSelected.indexOf(index) > -1 ? 'true' : 'false'"
              v-for="(item, index) in allData"
              :key="index"
            >
              <div class="resource-card type-search" v-if="item.img">
                <div class="card-thumb">
                  <div class="imageCard">
                    <div v-if="item.searchType === 'searchVideo'" class="icon-play"></div>
                    <img :src="item.img" alt="" style="width: 100%" @error="item.img = ['content/images/empty-image.png']" />
                  </div>
                </div>
                <div class="card-content">
                  <div class="info">
                    <div class="source-thumb" v-if="item.favicon">
                      <!--                      <i v-bind:style="{ 'background-image': 'url(' + item.favicon + ')' }"></i>-->
                      <img :src="item.favicon" alt="" />
                    </div>
                    <span class="source-text" v-if="item.author">{{ item.author }}</span>
                    <span class="source-text" v-if="!item.author">&#160;</span>
                    <span class="date">{{ getDateTimeUTC() }}</span>
                  </div>
                  <a class="title" target="_blank" :href="item.link" v-if="item.title">{{ item.title }}</a>
                  <a class="title" target="_blank" :href="item.link" v-if="!item.title">&#160;</a>
                  <p class="desc" v-if="item.description">{{ item.description }}</p>
                  <p class="desc" v-if="!item.description">&#160;</p>
                </div>
              </div>
              <div class="resource-card type-search" v-else>
                <div class="card-content">
                  <div class="info">
                    <div class="source-thumb" v-if="item.favicon">
                      <!--                      <i v-bind:style="{ 'background-image': 'url(' + item.favicon + ')' }"></i>-->
                      <img :src="item.favicon" alt="" />
                    </div>
                    <span class="source-text" v-if="item.author">{{ item.author }}</span>
                    <span class="source-text" v-if="!item.author">&#160;</span>
                  </div>
                  <a class="title" target="_blank" :href="item.link" v-if="item.title">{{ item.title }}</a>
                  <a class="title" target="_blank" :href="item.link" v-if="!item.title">&#160;</a>
                  <p class="desc" v-if="item.description">{{ item.description }}</p>
                  <p class="desc" v-if="!item.description">&#160;</p>
                </div>
              </div>
              <button type="button" class="btn btn-check" @click="selectCard(index)"><i class="icon-check-gray"></i></button>
            </li>
          </ul>
        </div>
        <div id="list-type-row" v-else>
          <ul class="resource-list list-row">
            <li
              class="list-item"
              :aria-selected="isSelected.indexOf(index) > -1 ? 'true' : 'false'"
              v-for="(item, index) in allData"
              :key="index"
            >
              <div class="resource-card type-search">
                <div class="card-thumb" v-if="item.img">
                  <!--                  || item.searchType.includes('searchFileType')-->
                  <div id="image">
                    <div v-if="item.searchType === 'searchVideo'" class="icon-play"></div>
                    <img v-if="item.img" :src="item.img" alt="" @error="item.img = ['content/images/empty-image.png']" />
                    <!--                    <img v-else src="content/images/icon-excel-lg.png" style="width: 100%">-->
                  </div>
                </div>
                <div class="card-content">
                  <div class="info">
                    <div class="source-thumb" v-if="item.favicon">
                      <img :src="item.favicon" alt="" />
                    </div>
                    <span class="source-text" v-if="item.author">{{ item.author }}</span>
                    <span class="source-text" v-if="!item.author">&#160;</span>
                    <span class="date">{{ getDateTimeUTC() }}</span>
                  </div>
                  <a class="title" target="_blank" :href="item.link" v-if="item.title">{{ item.title }}</a>
                  <a class="title" target="_blank" :href="item.link" v-if="!item.title">&#160;</a>
                  <p class="desc" v-if="item.description">{{ item.description }}</p>
                  <p class="desc" v-if="!item.description">&#160;</p>
                </div>
              </div>
              <button type="button" class="btn btn-check" @click="selectCard(index)"><i class="icon-check-gray"></i></button>
            </li>
          </ul>
        </div>
      </div>
      <scroll-loader :loader-method="scrollLoader" :loader-disable="loaderDisable"></scroll-loader>
    </div>
    <scroll-loader v-if="isShowSpinner" :loader-method="methodSkipScroll" :loader-disable="false"></scroll-loader>
    <b-modal class="modal fade" id="qrModal" dialog-class="modalQR" hide-footer hide-header no-close-on-backdrop size="sm">
      <div class="modal-dialog modal-dialog-centered modal-custom-style1">
        <div class="modal-content">
          <div class="modal-body">
            <div class="login-qrcode-wrap">
              <div class="guide">
                <strong>Learning Connectome...</strong>
                <p>Check your feed on mobile.</p>
              </div>
              <div class="qrcode-area">
                <qr-code></qr-code>
              </div>
            </div>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-secondary col-5" @click="close()">Close</button>
            <button type="button" class="btn btn-primary col-7" @click="goFeed()">Feed<i class="bi bi-arrow-right ml-2"></i></button>
          </div>
        </div>
      </div>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./search.component.ts"></script>

<style>
#image .icon-play {
  height: 100%;
  width: 100%;
  position: absolute;
  top: 0;
  left: 0;
  background: rgba(0, 0, 0, 0.1) url('../../../../content/images/icon-circled-play-40.png') no-repeat center center;
}

.imageCard .icon-play {
  height: 50%;
  width: 100%;
  position: absolute;
  top: 0;
  left: 0;
  background: rgba(0, 0, 0, 0.1) url('../../../../content/images/icon-circled-play-40.png') no-repeat center center;
}
</style>
