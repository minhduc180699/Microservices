<template>
  <li class="lc-card-item">
    <b-card v-if="loading">
      <b-skeleton animation="fade" width="85%"></b-skeleton>
      <b-skeleton animation="fade" width="55%"></b-skeleton>
      <b-skeleton animation="fade" width="70%"></b-skeleton>
    </b-card>
    <div class="item-wrap" v-else>
      <div class="content-box">
        <div class="lc-check">
          <button type="button" class="btn btn-check"></button>
        </div>
        <div class="lc-media" style="overflow: hidden">
          <a class="media-img">
            <img
              :src="data.imageUrl[0]"
              v-if="data.imageUrl && data.imageUrl.length > 0"
              @error="data.imageUrl = ['content/images/empty-image.png']"
            />
            <img
              :src="data.imageBase64[0]"
              v-else-if="data.imageBase64 && data.imageBase64.length > 0"
              @error="data.imageBase64 = ['content/images/empty-image.png']"
            />
            <img
              :src="data.ogImageBase64"
              v-else-if="data.ogImageBase64 && data.ogImageBase64 != ''"
              @error="data.ogImageBase64 = ['content/images/empty-image.png']"
            />
            <img
              :src="data.ogImageUrl"
              v-else-if="data.ogImageUrl && data.ogImageUrl != ''"
              @error="data.ogImageUrl = ['content/images/empty-image.png']"
            />
            <img src="content/images/empty-image.png" v-else />
          </a>
          <div class="media-body">
            <router-link :to="{ name: 'Detail', params: { connectomeId: data.connectomeId, feedId: data.id, data: data } }" custom>
              <a class="media-title">{{ data.title ? data.title : '' }}</a>
            </router-link>
            <p class="media-desc">{{ data.content ? data.content : data.contentSummary }}</p>
            <div class="media-info">
              <div class="info-item">
                <div class="source">
                  <div class="source-img">
                    <img
                      :src="data.faviconUrl ? data.faviconUrl : data.faviconBase64"
                      @error="data.faviconUrl = '/content/images/icon-resources-web.png'"
                    />
                  </div>
                  {{ data.author ? data.author : '' }}
                </div>
              </div>
              <div class="info-item">{{ data.publishedAt ? data.publishedAt.slice(0, 10) : '' }}</div>
            </div>
          </div>
        </div>
        <div class="lc-btn">
          <div class="more-area">
            <a class="btn-more" href="#" role="button" data-toggle="dropdown" aria-expanded="false"
              ><i class="icon-common icon-more"></i
            ></a>
            <div class="dropdown-menu dropdown-menu-right">
              <a class="dropdown-item" href="#"><i class="icon-common icon-trash"></i>Delete</a>
            </div>
          </div>
        </div>
      </div>
      <div class="tag-box" v-if="false">
        <div class="scroll-area">
          <div class="tag-list">
            <!--                      <a class="tag-item" href="#"></a>-->
          </div>
        </div>
        <div class="btn-wrap">
          <button type="button" class="btn btn-list-more"></button>
        </div>
      </div>
    </div>
  </li>
</template>

<script src="./single-card.component.ts" lang="ts"></script>
