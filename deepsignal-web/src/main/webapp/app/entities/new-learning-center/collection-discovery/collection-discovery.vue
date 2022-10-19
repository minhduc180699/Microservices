<template>
  <div class="lc-sidebar lc-collection-discovery" :class="isDiscoveryActive ? 'active' : null">
    <div class="panel-collection panel-collection-discovery">
      <div class="panel-header">
        <div class="elements-left">
          <div class="panel-title">
            <i class="go-icon-common icon-discovery"></i>
            <strong class="title">Discovery</strong>
          </div>
        </div>
        <div class="elements-right">
          <button type="button" class="btn btn-close" id="btn-collection-discovery-close" data-toggle="dropdown" aria-expanded="false">
            <i class="icon-common icon-close"></i>
          </button>
        </div>
      </div>
      <div class="panel-body">
        <div class="lc-list-wrap">
          <div class="list-inner overflow-y-scroll">
            <ul class="lc-card-list">
              <li class="lc-card-item" aria-selected="false">
                <div class="item-wrap">
                  <div class="content-top">
                    <div class="lc-bookmark">
                      <button type="button" class="btn btn-bookmark" data-toggle="button" aria-pressed="false"></button>
                    </div>
                    <div class="media-info">
                      <!--                      <div class="info-item"><div class="source"><div class="source-img"><img src="../assets/images/sample/media-aju.png"></div>아시아경제</div></div>-->
                      <div class="info-item">3일전</div>
                    </div>
                  </div>
                  <div class="content-box">
                    <div class="lc-media">
                      <div class="media-body">
                        <a class="media-title" href="#">아니한 안고, 이상의 우리의 청춘의 석가는 커다란 온갖 청춘은 있다.</a>
                        <p class="media-desc">
                          행복스럽고 구하기 광야에서 목숨이 피어나기 고행을 돋고, 만물은 뿐이다. 하여도 심장은 용감하고 교향악이다. 곳이
                          있는 사랑의 쓸쓸한 꽃이 위하여, 그러므로 가치를 곧 칼이다.
                        </p>
                      </div>
                      <a class="media-img" href="#">
                        <!--                        <img src="../assets/images/sample/@445x290.png">-->
                      </a>
                    </div>
                  </div>
                  <div class="tag-box">
                    <div class="scroll-area">
                      <div class="tag-list">
                        <a class="tag-item" href="#">keyword 1</a>
                        <a class="tag-item" href="#">keyword 2</a>
                        <a class="tag-item" href="#">web</a>
                        <a class="tag-item" href="#">web</a>
                        <a class="tag-item" href="#">responsive mobile</a>
                        <a class="tag-item" href="#">keyword 1</a>
                        <a class="tag-item" href="#">keyword 2</a>
                        <a class="tag-item" href="#">web</a>
                        <a class="tag-item" href="#">web</a>
                        <a class="tag-item" href="#">responsive mobile</a>
                      </div>
                    </div>
                    <div class="btn-wrap">
                      <button type="button" class="btn btn-list-more"></button>
                    </div>
                  </div>
                </div>
              </li>
              <li class="lc-card-item" aria-selected="false">
                <div class="item-wrap">
                  <div class="content-top">
                    <div class="lc-bookmark">
                      <button type="button" class="btn btn-bookmark" data-toggle="button" aria-pressed="false"></button>
                    </div>
                    <div class="media-info">
                      <!--                      <div class="info-item"><div class="source"><div class="source-img"><img src="../assets/images/sample/media-aju.png"></div>아시아경제</div></div>-->
                      <div class="info-item">3일전</div>
                    </div>
                  </div>
                  <div class="content-box">
                    <div class="lc-media">
                      <div class="media-body">
                        <a class="media-title" href="#">아니한 안고, 이상의 우리의 청춘의 석가는 커다란 온갖 청춘은 있다.</a>
                        <p class="media-desc">
                          행복스럽고 구하기 광야에서 목숨이 피어나기 고행을 돋고, 만물은 뿐이다. 하여도 심장은 용감하고 교향악이다. 곳이
                          있는 사랑의 쓸쓸한 꽃이 위하여, 그러므로 가치를 곧 칼이다.
                        </p>
                      </div>
                      <a class="media-img" href="#">
                        <!--                        <img src="../assets/images/sample/@445x290.png">-->
                      </a>
                    </div>
                  </div>
                  <div class="tag-box">
                    <div class="scroll-area">
                      <div class="tag-list">
                        <a class="tag-item" href="#">keyword 1</a>
                        <a class="tag-item" href="#">keyword 2</a>
                        <a class="tag-item" href="#">web</a>
                        <a class="tag-item" href="#">web</a>
                        <a class="tag-item" href="#">responsive mobile</a>
                        <a class="tag-item" href="#">keyword 1</a>
                        <a class="tag-item" href="#">keyword 2</a>
                        <a class="tag-item" href="#">web</a>
                        <a class="tag-item" href="#">web</a>
                        <a class="tag-item" href="#">responsive mobile</a>
                      </div>
                    </div>
                    <div class="btn-wrap">
                      <button type="button" class="btn btn-list-more"></button>
                    </div>
                  </div>
                </div>
              </li>
            </ul>
          </div>
        </div>
      </div>
      <div class="panel-footer">
        <button type="button" class="btn btn-primary btn-block">Save Collection</button>
      </div>
    </div>
    <button
      type="button"
      class="btn btn-toggle btn-sidebar-toggle btn-sidebar-discovery-open"
      id="btn-sidebar-discovery-open"
      :class="isDiscoveryActive ? '' : 'active'"
      @click.prevent="isDiscoveryActive = !isDiscoveryActive"
    >
      {{ isDiscoveryActive ? '접기' : '펼치기' }}
    </button>
  </div>
</template>

<script lang="ts" src="./collection-discovery.component.ts"></script>

<style scoped></style>
