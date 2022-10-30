<template>
  <fragment>
    <div class="ds-card">
      <div class="ds-card-body" v-if="tab !== 'myai'">
        <router-link
          :to="{ name: 'Detail', params: { connectomeId: connectomeId, feedId: item.docId, item: item } }"
          custom
          v-if="item.dataSize === cardSide._1_2"
        >
          <a @click="saveToLocalStorage()" v-if="tab === 'feed'">
            <div class="img" v-if="item.url.includes('youtube')">
              <!--            <iframe :src="item.sourceId" width='100%' height='100%'></iframe>-->
              <iframe width="100%" height="100%" :src="item.url.replace('watch?v=', 'embed/')" allowfullscreen></iframe>
            </div>
            <div class="img" v-else-if="image">
              <img :src="item.og_image_base64 ? item.og_image_base64 : item.og_image_url" alt="" @error="onErrorImage" />
            </div>
            <div
              class="img"
              v-else-if="
                item.og_image_base64 || item.og_image_url || (!imageFull && item.og_image_base64 ? item.og_image_base64 : item.og_image_url)
              "
            >
              <img :src="(item.og_image_base64 ? item.og_image_base64 : item.og_image_url)" alt=""  @error="item.og_image_url = ['content/images/empty-image.png']" />
              <!--              <img-->
<!--                v-for="(banner, index) in item.og_image_base64 ? item.og_image_base64 : item.og_image_url"-->
<!--                :key="index"-->
<!--                :src="banner"-->
<!--                alt=""-->
<!--                @error="item.og_image_url = ['content/images/empty-image.png']"-->
<!--              />-->
            </div>
            <div class="source">
              <div class="source-img">
                <i v-bind:style="{ 'background-image': 'url(' + (item.favicon_url ? item.favicon_url : item.favicon_base64) + ')' }"></i>
              </div>
              <div class="source-text">{{ item.writer }}</div>
            </div>
            <div :class="item.description ? 'title max-line-2' : 'title max-line-3'">
              <text-highlight :queries="highlightedWord" highlightStyle="padding: 0 0.2em">
                {{ item.title }}
              </text-highlight>
            </div>
            <div v-if="item.description" class="desc max-line-4">
              <text-highlight :queries="highlightedWord" highlightStyle="padding: 0 0.2em">
                {{ item.description }}
              </text-highlight>
            </div>
            <div class="info" v-if="item.created_date != undefined">{{ checkRegexDate(item.created_date) | formatDate }}</div>
          </a>
          <a @click="saveToLocalStorage()" v-else-if="tab === 'people'">
            <div class="img" v-if="image">
              <!--              <img :src="item.og_image_base64 ? item.og_image_base64[0] : item.og_image_url[0]" alt="" @error="onErrorImage" />-->
            </div>
            <div
              class="img"
              v-else-if="
                item.og_image_base64 || item.og_image_url || (!imageFull && item.og_image_base64 ? item.og_image_base64 : item.og_image_url)
              "
            >
              <img
                v-for="(banner, index) in item.og_image_base64 ? item.og_image_base64 : item.og_image_url"
                :key="index"
                :src="banner"
                alt=""
                @error="item.og_image_url = ['content/images/empty-image.png']"
              />
            </div>
            <div class="source">
              <div class="source-img">
                <i v-bind:style="{ 'background-image': 'url(' + item.favicon_base64 ? item.favicon_base64 : item.favicon_url + ')' }"></i>
              </div>
              <div class="source-text">{{ item.writer }}</div>
            </div>
            <!--          <div :class="item.content ? 'title max-line-2' : 'title max-line-3'">-->
            <!--            {{ item.title === item.writer ? ' ' : item.title }}-->
            <!--          </div>-->
            <div v-if="item.title" class="desc max-line-2">
              {{ item.title }}
            </div>
            <div class="info" v-if="item.created_date != undefined">{{ checkRegexDate(item.created_date) | formatDate }}</div>
          </a>
        </router-link>
        <router-link
          :to="{ name: 'Detail', params: { connectomeId: connectomeId, feedId: item.docId, item: item } }"
          custom
          v-if="item.dataSize === cardSide._1_1"
        >
          <a @click="saveToLocalStorage()">
            <div class="source">
              <div class="source-img">
                <img
                  :src="item.favicon_base64 ? item.favicon_base64 : item.favicon_url"
                  @error="item.favicon_url = '/content/images/icon-resources-web.png'"
                />
                <!--                <i :style="{backgroundImage: 'url(' + item.favicon + ')'}"></i>-->
              </div>
              <div class="source-text">{{ item.writer | lmTo(30) }}</div>
            </div>
            <template v-if="isFile">
              <div class="row-wrap">
                <div class="text-wrap">
                  <div class="title max-line-4" v-if="item.description || item.title">
                    <text-highlight :queries="highlightedWord" highlightStyle="padding: 0 0.2em">
                      {{item.title? item.title : item.description }}
                    </text-highlight>
                  </div>
                </div>
                <div class="icon-wrap">
                  <img :src="isImageFile" />
                </div>
              </div>
            </template>
            <template v-if="(item.og_image_base64 && !isFile) || (item.og_image_url && !isFile)">
              <text-highlight :queries="highlightedWord" highlightStyle="padding: 0 0.2em" class="title max-line-1">
                {{ item.title }}
              </text-highlight>
              <div class="row-wrap">
                <div class="text-wrap">
                  <text-highlight
                    :queries="highlightedWord"
                    highlightStyle="padding: 0 0.2em"
                    class="desc max-line-3"
                    v-if="item.description || item.title"
                  >
                    {{ item.description ? item.description : item.title }}
                  </text-highlight>
                </div>
                <div class="img-wrap">
                  <div class="img" style="width: 40px">
                    <img
                      :src="item.og_image_base64 ? item.og_image_base64 : item.og_image_url"
                      alt=""
                      @error="item.og_image_url = ['/content/images/empty-image.png']"
                    />
                  </div>
                </div>
              </div>
            </template>
            <template
              v-if="
                (item.og_image_base64 == null && item.og_image_url == null && !isFile) ||
                (item.og_image_base64 == '' && item.og_image_url == '' && !isFile)
              "
            >
              <text-highlight :queries="highlightedWord" highlightStyle="padding: 0 0.2em" class="title max-line-2">
                {{ item.title }}
              </text-highlight>
              <text-highlight :queries="highlightedWord" highlightStyle="padding: 0 0.2em" class="desc max-line-2">
                {{ item.description }}
              </text-highlight>
            </template>
            <div class="info" v-if="item.created_date != undefined">{{ checkRegexDate(item.created_date) | formatDate }}</div>
          </a>
        </router-link>
        <router-link
          :to="{ name: 'Detail', params: { connectomeId: connectomeId, feedId: item.docId, item: item } }"
          custom
          v-if="item.dataSize !== cardSide._1_1 && item.dataSize !== cardSide._1_2"
        >
          <a @click="saveToLocalStorage()">
            <div class="source">
              <div class="source-img">
                <i v-bind:style="{ 'background-image': 'url(' + (item.favicon_base64 ? item.favicon_base64 : item.favicon_url) + ')' }"></i>
              </div>
              <div class="source-text">{{ item.writer }}</div>
            </div>
            <div :class="item.description ? 'title max-line-2' : 'title max-line-3'">
              <text-highlight :queries="highlightedWord" highlightStyle="padding: 0 0.2em">
                {{ item.title }}
              </text-highlight>
            </div>
            <div v-if="item.description" class="desc max-line-1">
              <text-highlight :queries="highlightedWord" highlightStyle="padding: 0 0.2em">
                {{ item.description }}
              </text-highlight>
            </div>
            <div class="info" v-if="item.created_date != undefined">{{ checkRegexDate(item.created_date) | formatDate }}</div>
            <div class="img" v-if="item.og_image_base64 || item.og_image_url">
              <iframe
                v-if="item.url && isYoutube(item.url)"
                width="100%"
                height="100%"
                :src="getEmbedLink(item.url)"
                title="YouTube video player"
                frameborder="0"
                allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
                allowfullscreen
              ></iframe>
              <img v-else :src="item.og_image_base64 ? item.og_image_base64 : item.og_image_url" alt="" @error="onErrorImage" />
            </div>
            <div
              class="img"
              v-else-if="
                item.og_image_base64 || item.og_image_url || (!imageFull && item.og_image_base64 ? item.og_image_base64 : item.og_image_url)
              "
            >
              <div v-swiper>
                <div class="swiper-pagination"></div>
                <div class="swiper-wrapper">
                  <div
                    class="swiper-slide"
                    v-for="(banner, index) in item.og_image_base64 ? item.og_image_base64 : item.og_image_url"
                    :key="index"
                  >
                    <img :src="banner" />
                  </div>
                </div>
              </div>
            </div>
          </a>
        </router-link>
      </div>

      <!--    myai-->
      <div class="ds-card-body" v-else>
        <a @click="saveToLocalStorage()" :href="item.url" v-if="item.dataSize === cardSide._1_2" target="_blank">
          <div class="img" v-if="item.search_type == 'VIDEO' && item.url.indexOf('youtube') > -1">
            <!--            <iframe :src="item.sourceId" width='100%' height='100%'></iframe>-->
            <iframe width="100%" height="100%" :src="item.url.replace('watch?v=', 'embed/')" allowfullscreen></iframe>
          </div>
          <div class="img" v-else-if="image">
            <img :src="item.og_image_base64 ? item.og_image_base64 : item.og_image_url" alt="" @error="onErrorImage" />
          </div>
          <div
            class="img"
            v-else-if="
              item.og_image_base64
                ? item.og_image_base64
                : item.og_image_url || (!imageFull && item.og_image_base64 ? item.og_image_base64 : item.og_image_url)
            "
          >
            <img
              v-for="(banner, index) in item.og_image_base64 ? item.og_image_base64 : item.og_image_url"
              :key="index"
              :src="banner"
              alt=""
              @error="item.og_image_url = ['content/images/empty-image.png']"
            />
          </div>
          <div class="source">
            <div class="source-img">
              <i v-bind:style="{ 'background-image': 'url(' + item.favicon_base64 ? item.favicon_base64 : item.favicon_url + ')' }"></i>
            </div>
            <div class="source-text">{{ item.writer }}</div>
          </div>
          <div :class="item.description ? 'title max-line-2' : 'title max-line-3'">
            {{ item.title }}
          </div>
          <div v-if="item.description" class="desc max-line-4">
            {{ item.description }}
          </div>
          <div class="info" v-if="item.created_date">{{ checkRegexDate(item.created_date) | formatDate('YYYY-DD-MM') }}</div>
        </a>
        <a @click="saveToLocalStorage()" :href="item.url" v-if="item.dataSize === cardSide._1_1" target="_blank">
          <div class="source">
            <div class="source-img">
              <img
                :src="item.favicon_base64 ? item.favicon_base64 : item.favicon_url"
                @error="item.favicon_url = '/content/images/icon-resources-web.png'"
              />
            </div>
            <div class="source-text">{{ item.writer | lmTo(30) }}</div>
          </div>
          <div class="title" v-if="item.title">
            {{ item.title | lmTo(20) }}
          </div>
          <div class="row" style="margin-top: 10px">
            <div class="col-5" style="max-height: 73px; width: 100%" v-if="item.og_image_base64 || item.og_image_url">
              <img
                :src="item.og_image_base64 ? item.og_image_base64 : item.og_image_url"
                alt=""
                style="height: 100%; width: 100%"
                @error="$event.target.src = errorImage(item)"
              />
            </div>
            <div
              :class="['desc max-line-3', item.og_image_base64 || item.og_image_url ? 'col-6 pad-left0' : 'col-12 pad-left15']"
              style="height: 30%"
              v-if="item.description || item.title"
            >
              {{ item.description ? item.description : item.title }}
            </div>
          </div>
          <div class="info" v-if="item.created_date">{{ checkRegexDate(item.created_date) | formatDate('YYYY-DD-MM') }}</div>
        </a>

        <a
          @click="saveToLocalStorage()"
          :href="item.url"
          v-if="item.dataSize !== cardSide._1_1 && item.dataSize !== cardSide._1_2"
          target="_blank"
        >
          <div class="source">
            <div class="source-img">
              <i v-bind:style="{ 'background-image': 'url(' + item.favicon_base64 ? item.favicon_base64 : item.favicon_url + ')' }"></i>
            </div>
            <div class="source-text">{{ item.writer }}</div>
          </div>
          <div :class="item.description ? 'title max-line-2' : 'title max-line-3'">
            {{ item.title }}
          </div>
          <div v-if="item.description" class="desc max-line-1">
            {{ item.description }}
          </div>
          <div class="info" v-if="item.created_date">{{ checkRegexDate(item.created_date) | formatDate('YYYY-DD-MM') }}</div>
          <div class="img" v-if="item.og_image_base64 || item.og_image_url">
            <iframe
              v-if="item.url && isYoutube(item.url)"
              width="100%"
              height="100%"
              :src="getEmbedLink(item.url)"
              title="YouTube video player"
              frameborder="0"
              allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
              allowfullscreen
            ></iframe>
            <img v-else :src="item.og_image_base64 ? item.og_image_base64 : item.og_image_url" alt="" @error="onErrorImage" />
          </div>
          <div
            class="img"
            v-else-if="
              item.og_image_base64 || item.og_image_url || (!imageFull && item.og_image_base64 ? item.og_image_base64 : item.og_image_url)
            "
          >
            <div v-swiper>
              <div class="swiper-pagination"></div>
              <div class="swiper-wrapper">
                <div
                  class="swiper-slide"
                  v-for="(banner, index) in item.og_image_base64 ? item.og_image_base64 : item.og_image_url"
                  :key="index"
                >
                  <img :src="banner" />
                </div>
              </div>
            </div>
          </div>
        </a>
      </div>

      <ds-card-footer :item="item" ref="handleHideCard" v-on:handleActivity="handleActivity" v-on:handleHiddenCard="handleHiddenCard" />
    </div>

    <ds-card-not-useful v-show="isHidden && stateDelete" :isLoadingDelete="isLoadingDelete" :isMyAi="isMyAi"></ds-card-not-useful>
  </fragment>
</template>
<script lang="ts" src="./ds-card-default.component.ts"></script>
