<template>
  <div class="lc-sidebar">
    <div class="lc-panel">
      <!-- panel-main -->
      <div class="lc-panel-main">
        <div class="lc-main-inner">
          <div class="panel-body">
            <vue-custom-scrollbar class="overflow-yx-scroll csScrollPositon" :settings="scrollSettings">
              <ul class="lc-card-list lc-context-list">
                <li class="lc-card-group" aria-selected="false">
                  <div class="group-wrap">
                    <div class="content-box">
                      <div class="lc-check">
                        <button type="button" class="btn btn-check"></button>
                      </div>
                      <div class="lc-media">
                        <a class="media-gird" href="#">
                          <div><img src="content/images/sample/@445x290.png" /></div>
                          <div><img src="content/images/sample/@434x290.png" /></div>
                          <div><img src="content/images/sample/@435x290.png" /></div>
                          <div><img src="content/images/sample/@445x290.png" /></div>
                        </a>
                        <div class="media-body">
                          <a class="media-title" href="#">Group<small>(12)</small></a>
                          <div class="media-info">
                            <div class="info-item">3일전</div>
                          </div>
                        </div>
                      </div>
                      <div class="lc-btn">
                        <button type="button" class="btn btn-icon btn-more"><i class="icon-common icon-pin-on"></i></button>
                        <div class="more-area">
                          <a class="btn-more" href="#" role="button" data-toggle="dropdown" aria-expanded="false"
                            ><i class="icon-common icon-more"></i
                          ></a>
                          <div class="dropdown-menu dropdown-menu-right">
                            <a class="dropdown-item" href="#"><i class="icon-common icon-pin-off"></i>Unpin the top</a>
                            <a class="dropdown-item" href="#"><i class="icon-common icon-folder-edit"></i>Edit</a>
                            <a class="dropdown-item" href="#"><i class="icon-common icon-trash"></i>Delete</a>
                          </div>
                        </div>
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
                <li class="lc-card-group" aria-selected="false">
                  <div class="group-wrap">
                    <div class="content-box">
                      <div class="lc-check">
                        <button type="button" class="btn btn-check"></button>
                      </div>
                      <div class="lc-media">
                        <a class="media-gird" href="#">
                          <div><img src="content/images/sample/@445x290.png" /></div>
                          <div><img src="content/images/sample/@434x290.png" /></div>
                          <div><img src="content/images/sample/@435x290.png" /></div>
                          <div><img src="content/images/sample/@445x290.png" /></div>
                        </a>
                        <div class="media-body">
                          <a class="media-title" href="#">Group<small>(4)</small></a>
                          <div class="media-info">
                            <div class="info-item">3일전</div>
                          </div>
                        </div>
                      </div>
                      <div class="lc-btn">
                        <div class="more-area">
                          <a class="btn-more" href="#" role="button" data-toggle="dropdown" aria-expanded="false"
                            ><i class="icon-common icon-more"></i
                          ></a>
                          <div class="dropdown-menu dropdown-menu-right">
                            <a class="dropdown-item" href="#"><i class="icon-common icon-pin"></i>Top fixed</a>
                            <a class="dropdown-item" href="#"><i class="icon-common icon-folder-edit"></i>Edit</a>
                            <a class="dropdown-item" href="#"><i class="icon-common icon-trash"></i>Delete</a>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </li>
                <li class="lc-card-item" aria-selected="false">
                  <div class="item-wrap">
                    <div class="content-box">
                      <div class="lc-check">
                        <button type="button" class="btn btn-check"></button>
                      </div>
                      <div class="lc-media">
                        <a class="media-img" href="#">
                          <img src="content/images/sample/@445x290.png" />
                        </a>
                        <div class="media-body">
                          <a class="media-title" href="#">아니한 안고, 이상의 우리의 청춘의 석가는 커다란 온갖 청춘은 있다.</a>
                          <p class="media-desc">
                            행복스럽고 구하기 광야에서 목숨이 피어나기 고행을 돋고, 만물은 뿐이다. 하여도 심장은 용감하고 교향악이다. 곳이
                            있는 사랑의 쓸쓸한 꽃이 위하여, 그러므로 가치를 곧 칼이다.
                          </p>
                          <div class="media-info">
                            <div class="info-item">
                              <div class="source">
                                <div class="source-img"><img src="content/images/sample/media-aju.png" /></div>
                                아시아경제
                              </div>
                            </div>
                            <div class="info-item">3일전</div>
                          </div>
                        </div>
                      </div>
                      <div class="lc-btn">
                        <div class="more-area">
                          <a class="btn-more" href="#" role="button" data-toggle="dropdown" aria-expanded="false"
                            ><i class="icon-common icon-more"></i
                          ></a>
                          <div class="dropdown-menu dropdown-menu-right">
                            <a class="dropdown-item" href="#"><i class="icon-common icon-pin"></i>Top fixed</a>
                            <a class="dropdown-item" href="#"><i class="icon-common icon-trash"></i>Delete</a>
                          </div>
                        </div>
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
                    <div class="content-box">
                      <div class="lc-check">
                        <button type="button" class="btn btn-check"></button>
                      </div>
                      <div class="lc-media">
                        <a class="media-file" href="#">
                          <img src="content/images/common/file/file-pdf-fill.svg" />
                        </a>
                        <div class="media-body">
                          <a class="media-title" href="#">행복스럽고 구하기 광야에서 목숨이 피어나기 고행을 돋고.pdf</a>
                          <div class="media-info">
                            <div class="info-item">
                              <div class="source">내 컴퓨터</div>
                            </div>
                            <div class="info-item">3일전</div>
                          </div>
                        </div>
                      </div>
                      <div class="lc-btn">
                        <div class="more-area">
                          <a class="btn-more" href="#" role="button" data-toggle="dropdown" aria-expanded="false"
                            ><i class="icon-common icon-more"></i
                          ></a>
                          <div class="dropdown-menu dropdown-menu-right">
                            <a class="dropdown-item" href="#"><i class="icon-common icon-pin"></i>Top fixed</a>
                            <a class="dropdown-item" href="#"><i class="icon-common icon-trash"></i>Delete</a>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </li>
                <li class="lc-card-item" aria-selected="false">
                  <div class="item-wrap">
                    <div class="content-box">
                      <div class="lc-check">
                        <button type="button" class="btn btn-check"></button>
                      </div>
                      <div class="lc-media">
                        <a class="media-file" href="#">
                          <img src="content/images/common/file/file-excel-fill.svg" />
                        </a>
                        <div class="media-body">
                          <a class="media-title" href="#">아니한 안고, 이상의 우리의 청춘의 석가는 커다란.xls</a>
                          <div class="media-info">
                            <div class="info-item">
                              <div class="source">
                                <div class="source-img"><img src="content/images/sample/media-aju.png" /></div>
                                아시아경제
                              </div>
                            </div>
                            <div class="info-item">3일전</div>
                          </div>
                        </div>
                      </div>
                      <div class="lc-btn">
                        <div class="more-area">
                          <a class="btn-more" href="#" role="button" data-toggle="dropdown" aria-expanded="false"
                            ><i class="icon-common icon-more"></i
                          ></a>
                          <div class="dropdown-menu dropdown-menu-right">
                            <a class="dropdown-item" href="#"><i class="icon-common icon-pin"></i>Top fixed</a>
                            <a class="dropdown-item" href="#"><i class="icon-common icon-trash"></i>Delete</a>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </li>
                <li class="lc-card-item" aria-selected="false">
                  <div class="item-wrap">
                    <div class="content-box">
                      <div class="lc-check">
                        <button type="button" class="btn btn-check"></button>
                      </div>
                      <div class="lc-media">
                        <div class="media-body">
                          <a class="media-title" href="#">아니한 안고, 이상의 우리의 청춘의 석가는 커다란 온갖 청춘은 있다.</a>
                          <p class="media-desc">
                            행복스럽고 구하기 광야에서 목숨이 피어나기 고행을 돋고, 만물은 뿐이다. 하여도 심장은 용감하고 교향악이다. 곳이
                            있는 사랑의 쓸쓸한 꽃이 위하여, 그러므로 가치를 곧 칼이다.
                          </p>
                          <div class="media-info">
                            <div class="info-item">
                              <div class="badge">Text</div>
                            </div>
                            <div class="info-item">3일전</div>
                          </div>
                        </div>
                      </div>
                      <div class="lc-btn">
                        <div class="more-area">
                          <a class="btn-more" href="#" role="button" data-toggle="dropdown" aria-expanded="false"
                            ><i class="icon-common icon-more"></i
                          ></a>
                          <div class="dropdown-menu dropdown-menu-right">
                            <a class="dropdown-item" href="#"><i class="icon-common icon-pin"></i>Top fixed</a>
                            <a class="dropdown-item" href="#"><i class="icon-common icon-trash"></i>Delete</a>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </li>
                <li class="lc-card-item" aria-selected="false">
                  <div class="item-wrap">
                    <div class="content-box">
                      <div class="lc-check">
                        <button type="button" class="btn btn-check"></button>
                      </div>
                      <div class="lc-media">
                        <a class="media-img" href="#">
                          <img src="content/images/sample/@445x290.png" />
                        </a>
                        <div class="media-body">
                          <div class="media-info">
                            <div class="info-item">
                              <div class="source">
                                <div class="source-img"><img src="content/images/sample/media-aju.png" /></div>
                                아시아경제
                              </div>
                            </div>
                            <div class="info-item">3일전</div>
                          </div>
                          <a class="media-title" href="#">아니한 안고, 이상의 우리의 청춘의 석가는 커다란 온갖 청춘은 있다.</a>
                          <p class="media-desc">
                            행복스럽고 구하기 광야에서 목숨이 피어나기 고행을 돋고, 만물은 뿐이다. 하여도 심장은 용감하고 교향악이다. 곳이
                            있는 사랑의 쓸쓸한 꽃이 위하여, 그러므로 가치를 곧 칼이다.
                          </p>
                        </div>
                      </div>
                      <div class="lc-btn">
                        <div class="more-area">
                          <a class="btn-more" href="#" role="button" data-toggle="dropdown" aria-expanded="false"
                            ><i class="icon-common icon-more"></i
                          ></a>
                          <div class="dropdown-menu dropdown-menu-right">
                            <a class="dropdown-item" href="#"><i class="icon-common icon-pin"></i>Top fixed</a>
                            <a class="dropdown-item" href="#"><i class="icon-common icon-trash"></i>Delete</a>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </li>
                <li class="lc-card-item" aria-selected="false">
                  <div class="item-wrap">
                    <div class="content-box">
                      <div class="lc-check">
                        <button type="button" class="btn btn-check"></button>
                      </div>
                      <div class="lc-media">
                        <div class="media-body">
                          <div class="media-info">
                            <div class="info-item">
                              <div class="source">
                                <div class="source-img"><img src="content/images/sample/media-aju.png" /></div>
                                아시아경제
                              </div>
                            </div>
                            <div class="info-item">3일전</div>
                          </div>
                          <a class="media-title" href="#">아니한 안고, 이상의 우리의 청춘의 석가는 커다란 온갖 청춘은 있다.</a>
                          <p class="media-desc">
                            행복스럽고 구하기 광야에서 목숨이 피어나기 고행을 돋고, 만물은 뿐이다. 하여도 심장은 용감하고 교향악이다. 곳이
                            있는 사랑의 쓸쓸한 꽃이 위하여, 그러므로 가치를 곧 칼이다.
                          </p>
                        </div>
                      </div>
                      <div class="lc-btn">
                        <div class="more-area">
                          <a class="btn-more" href="#" role="button" data-toggle="dropdown" aria-expanded="false"
                            ><i class="icon-common icon-more"></i
                          ></a>
                          <div class="dropdown-menu dropdown-menu-right">
                            <a class="dropdown-item" href="#"><i class="icon-common icon-pin"></i>Top fixed</a>
                            <a class="dropdown-item" href="#"><i class="icon-common icon-trash"></i>Delete</a>
                          </div>
                        </div>
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
            </vue-custom-scrollbar>
            <div class="list-add">
              <button type="button" class="btn btn-add">+ Add Learning Source</button>
            </div>
          </div>
        </div>
        <div class="panel-footer">
          <div class="list-count">
            <span class="all">전체 <em>20</em>개</span>
            <span class="select">선택 <em>10</em>개</span>
          </div>
        </div>
      </div>
    </div>
    <!-- panel-sub -->
    <div class="lc-panel-sub">
      <div class="lc-sub-inner">
        <div class="panel-header">
          <div class="header-left">
            <button type="button" class="btn btn-icon btn-back"><i class="icon-common-lg icon-arrow-left"></i></button>
            <strong class="title"><i class="icon-common-lg icon-folder"></i>Group</strong>
          </div>
          <div class="header-right">
            <button type="button" class="btn btn-icon btn-more"><i class="icon-common icon-pin-on"></i></button>
            <div class="more-area">
              <button type="button" class="btn btn-more" data-toggle="dropdown" aria-expanded="false">
                <i class="icon-common icon-more"></i>
              </button>
              <div class="dropdown-menu dropdown-menu-right">
                <a class="dropdown-item" href="#"><i class="icon-common icon-pin-off"></i>unpin the top</a>
                <a class="dropdown-item" href="#"><i class="icon-common icon-folder-edit"></i>Edit</a>
                <a class="dropdown-item" href="#"><i class="icon-common icon-trash"></i>Delete</a>
              </div>
            </div>
          </div>
        </div>
        <div class="panel-body">
          <div class="list-wrap overflow-y-scroll">
            <ul class="lc-card-list lc-context-list">
              <li class="lc-card-item">
                <div class="item-wrap">
                  <div class="content-box">
                    <div class="lc-check">
                      <button type="button" class="btn btn-check"></button>
                    </div>
                    <div class="lc-media">
                      <a class="media-img" href="#">
                        <img src="content/images/sample/@445x290.png" />
                      </a>
                      <div class="media-body">
                        <a class="media-title" href="#">아니한 안고, 이상의 우리의 청춘의 석가는 커다란 온갖 청춘은 있다.</a>
                        <p class="media-desc">
                          행복스럽고 구하기 광야에서 목숨이 피어나기 고행을 돋고, 만물은 뿐이다. 하여도 심장은 용감하고 교향악이다. 곳이
                          있는 사랑의 쓸쓸한 꽃이 위하여, 그러므로 가치를 곧 칼이다. 자신과 구할 방황하여도, 노년에게서 때까지 봄바람이다.
                          설레는 소리다.이것은 열락의 투명하되 만천하의 영원히 수 따뜻한 봄바람이다. 수 붙잡아 보내는 크고 것이 하는 온갖
                          거친 사막이다. 투명하되 가슴에 착목한는 바로 쓸쓸하랴? 트고, 오직 있음으로써 위하여, 안고, 피고 것이다. 피어나는
                          얼음 이상은 쓸쓸하랴?
                        </p>
                        <div class="media-info">
                          <div class="info-item">
                            <div class="source">
                              <div class="source-img"><img src="content/images/sample/media-aju.png" /></div>
                              아시아경제
                            </div>
                          </div>
                          <div class="info-item">3일전</div>
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
          <div class="list-add">
            <button type="button" class="btn btn-add">+ Add Learning Source</button>
          </div>
        </div>
        <div class="panel-footer">
          <div class="list-count">
            <span class="all">전체 <em>20</em>개</span>
            <span class="select">선택 <em>10</em>개</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./learning-center-aside.component.ts"></script>
