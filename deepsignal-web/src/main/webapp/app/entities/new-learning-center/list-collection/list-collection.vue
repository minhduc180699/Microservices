<template>
  <div class="lc-sidebar lc-collection-home">
    <!-- panel-main -->
    <div class="lc-panel-main">
      <div class="panel-collection">
        <div class="panel-header">
          <div class="filter-top">
            <div class="form-group search-group">
              <input class="form-control" type="text" placeholder="키워드를 입력하세요." />
              <button class="form-btn" type="button">
                <i class="icon-common icon-search-pirmary"></i>
              </button>
            </div>
            <button type="button" class="btn btn-default btn-sm" data-toggle="collapse" href="#collapseExample" aria-expanded="false">
              <i class="icon-common-lg icon-fillter"></i>
            </button>
            <button type="button" id="btn-collection-add-open" class="btn btn-primary btn-sm" @click.prevent="isAddCollectionActive = true">
              <i class="go-icon-common icon-collection-add-white"></i>
            </button>
          </div>
        </div>
        <div class="panel-body">
          <div class="filter-add">
            <div class="collapse" id="collapseExample">
              <div class="filter-collection">
                <div class="filter-body">
                  <div class="form-group">
                    <label class="form-label">Date</label>
                    <div class="form-input-wrap">
                      <input type="text" class="form-control" value="2022-01-01~2022-10-12" />
                    </div>
                    <button type="button" class="btn btn-icon btn-close"><i class="icon-common icon-close"></i></button>
                  </div>
                  <div class="form-group form-group-source">
                    <label class="form-label">Source</label>
                    <div class="form-input-wrap">
                      <div class="filter-select-btn">
                        <ul class="tag-list">
                          <!--                          <% for(var i = 0; i < 2; i++){ %>-->
                          <li class="list-item">
                            source<a href="#"><i class="icon-common icon-close"></i></a>
                          </li>
                          <li class="list-item">
                            sourcee<a href="#"><i class="icon-common icon-close"></i></a>
                          </li>
                          <!--                          <% } %>-->
                          <li class="list-add">
                            <input type="text" />
                          </li>
                        </ul>
                        <button type="button" class="btn btn-toggle-arrow"></button>
                      </div>
                      <div class="filter-option-content">
                        <div class="filter-option-list overflow-y-scroll">
                          <div class="option-item">
                            <div class="form-check">
                              <input type="checkbox" class="form-check-input" id="source-bookmark" />
                              <label class="form-check-label" for="source-bookmark">Bookmark </label>
                            </div>
                          </div>
                          <div class="option-item">
                            <div class="form-check">
                              <input type="checkbox" class="form-check-input" id="source-share" />
                              <label class="form-check-label" for="source-share">Share </label>
                            </div>
                          </div>
                          <div class="option-item">
                            <div class="form-check">
                              <input type="checkbox" class="form-check-input" id="source-websearch" />
                              <label class="form-check-label" for="source-websearch">Web Search </label>
                            </div>
                          </div>
                          <div class="option-item">
                            <div class="form-check">
                              <input type="checkbox" class="form-check-input" id="source-text" />
                              <label class="form-check-label" for="source-text">Text </label>
                            </div>
                          </div>
                          <div class="option-item">
                            <div class="form-check">
                              <input type="checkbox" class="form-check-input" id="source-url" />
                              <label class="form-check-label" for="source-url">URL </label>
                            </div>
                          </div>
                          <div class="option-item">
                            <div class="form-check">
                              <input type="checkbox" class="form-check-input" id="source-documents" />
                              <label class="form-check-label" for="source-documents">Documents </label>
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                    <button type="button" class="btn btn-icon btn-close"><i class="icon-common icon-close"></i></button>
                  </div>
                  <div class="form-group form-group-type">
                    <label class="form-label">Type</label>
                    <div class="form-input-wrap">
                      <div class="filter-select-btn">
                        <ul class="tag-list">
                          <!--                          <% for(var i = 0; i < 2; i++){ %>-->
                          <li class="list-item">
                            type<a href="#"><i class="icon-common icon-close"></i></a>
                          </li>
                          <li class="list-item">
                            type<a href="#"><i class="icon-common icon-close"></i></a>
                          </li>
                          <!--                          <% } %>-->
                          <li class="list-add">
                            <input type="text" />
                          </li>
                        </ul>
                        <button type="button" class="btn btn-toggle-arrow"></button>
                      </div>
                      <div class="filter-option-content">
                        <div class="filter-option-list overflow-y-scroll">
                          <div class="option-item">
                            <div class="form-check">
                              <input type="checkbox" class="form-check-input" id="type-article" />
                              <label class="form-check-label" for="type-article">Article </label>
                            </div>
                          </div>
                          <div class="option-item">
                            <div class="form-check">
                              <input type="checkbox" class="form-check-input" id="type-documents" />
                              <label class="form-check-label" for="type-documents">Documents </label>
                            </div>
                          </div>
                          <div class="option-item">
                            <div class="form-check">
                              <input type="checkbox" class="form-check-input" id="type-images" />
                              <label class="form-check-label" for="type-images">Images </label>
                            </div>
                          </div>
                          <div class="option-item">
                            <div class="form-check">
                              <input type="checkbox" class="form-check-input" id="type-videos" />
                              <label class="form-check-label" for="type-videos">Videos </label>
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                    <button type="button" class="btn btn-icon btn-close"><i class="icon-common icon-close"></i></button>
                  </div>
                  <div class="form-group form-group-channel">
                    <label class="form-label">Channel</label>
                    <div class="form-input-wrap">
                      <div class="filter-select-btn show">
                        <ul class="tag-list">
                          <li class="list-item">
                            channel<a href="#"><i class="icon-common icon-close"></i></a>
                          </li>
                          <li class="list-add">
                            <input type="text" />
                          </li>
                        </ul>
                      </div>
                      <div class="filter-option-content">
                        <div class="filter-option-list overflow-y-scroll">
                          <div class="option-item">
                            <div class="form-check">
                              <input type="checkbox" class="form-check-input" id="channel-item1" />
                              <label class="form-check-label" for="channel-item1">Google News </label>
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                    <button type="button" class="btn btn-icon btn-close"><i class="icon-common icon-close"></i></button>
                  </div>
                </div>
                <div class="filter-footer">
                  <button type="button" class="btn btn-default btn-sm">전체 삭제</button>
                  <button type="button" class="btn btn-primary btn-sm">적용</button>
                </div>
              </div>
            </div>
          </div>
          <div class="filter-selected">
            <div class="filter-pic">
              <span>2022.08.06~2022.07.26</span>
              <a class="btn-close" href="#"><i class="icon-common icon-close"></i></a>
            </div>
            <div class="filter-pic">
              <span>Article</span>
              <a class="btn-close" href="#"><i class="icon-common icon-close"></i></a>
            </div>
            <div class="filter-clear">
              <a class="btn-close" href="#"><i class="icon-common icon-refresh"></i></a>
            </div>
          </div>
          <div class="lc-list-top">
            <div class="elements-left">
              <div class="check-group">
                <button type="button" class="btn btn-check" id="list-check-all1" data-toggle="button" aria-pressed="false"></button>
                <label for="list-check-all1">전체 선택</label>
              </div>
              <div class="list-info">(<strong>0</strong>/20)</div>
            </div>
            <div class="elements-right">
              <button type="button" class="btn btn-default btn-sm"><i class="icon-common icon-close"></i>선택 삭제</button>
            </div>
          </div>
          <div class="lc-list-wrap">
            <div class="list-inner overflow-y-scroll">
              <ul class="lc-card-list">
                <li class="lc-card-group" aria-selected="false">
                  <div class="group-wrap">
                    <div class="content-top">
                      <div class="lc-check">
                        <button type="button" class="btn btn-check" data-toggle="button" aria-pressed="false"></button>
                      </div>
                      <div class="media-info">
                        <a class="info-item info-title" @click.prevent="isGroupCollectionActive = true">Group<small>(4)</small> </a>
                        <div class="info-item">3일전</div>
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
                    <div class="content-box">
                      <div class="lc-media">
                        <a class="media-gird" href="#">
                          <div><img src="content/images/sample/@445x290.png" /></div>
                          <div><img src="content/images/sample/@434x290.png" /></div>
                          <div><img src="content/images/sample/@435x290.png" /></div>
                          <div><img src="content/images/sample/@362x290.png" /></div>
                        </a>
                        <div class="media-body"></div>
                      </div>
                    </div>
                    <div class="tag-box" :class="isShowAllTag ? 'show' : ''">
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
                        <button type="button" class="btn btn-list-more" @click="isShowAllTag = !isShowAllTag"></button>
                      </div>
                    </div>
                  </div>
                </li>
                <li class="lc-card-group" aria-selected="false">
                  <div class="group-wrap">
                    <div class="content-top">
                      <div class="lc-check">
                        <button type="button" class="btn btn-check" data-toggle="button" aria-pressed="false"></button>
                      </div>
                      <div class="media-info">
                        <a class="info-item info-title" @click.prevent="isGroupCollectionActive = true"> Group<small>(4)</small> </a>
                        <div class="info-item">3일전</div>
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
                    <div class="content-box">
                      <div class="lc-media">
                        <a class="media-gird" href="#">
                          <div><img src="content/images/sample/@445x290.png" /></div>
                          <div><img src="content/images/sample/@434x290.png" /></div>
                          <div><img src="content/images/sample/@445x290.png" /></div>
                          <div><img src="content/images/sample/@434x290.png" /></div>
                        </a>
                        <div class="media-body"></div>
                      </div>
                    </div>
                    <div class="tag-box" :class="isShowAllTag ? 'show' : ''">
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
                        <button type="button" class="btn btn-list-more" @click="isShowAllTag = !isShowAllTag"></button>
                      </div>
                    </div>
                  </div>
                </li>
                <li class="lc-card-item" aria-selected="false">
                  <div class="item-wrap">
                    <div class="content-top">
                      <div class="lc-check">
                        <button type="button" class="btn btn-check" data-toggle="button" aria-pressed="false"></button>
                      </div>
                      <div class="media-info">
                        <div class="info-item">
                          <div class="source">내 컴퓨터</div>
                        </div>
                        <div class="info-item">100MB</div>
                        <div class="info-item">3일전</div>
                      </div>
                      <div class="lc-btn">
                        <a class="btn-close" href="#"><i class="icon-common icon-close"></i></a>
                      </div>
                    </div>
                    <div class="content-box">
                      <div class="lc-media">
                        <div class="media-body">
                          <a class="media-title" href="#">행복스럽고 구하기 광야에서 목숨이 피어나기 고행을 돋고.pdf</a>
                        </div>
                        <a class="media-file" href="#">
                          <img src="content/images/common/file/file-pdf-fill.svg" />
                        </a>
                      </div>
                    </div>
                  </div>
                </li>
                <li class="lc-card-item" aria-selected="false">
                  <div class="item-wrap">
                    <div class="content-top">
                      <div class="lc-check">
                        <button type="button" class="btn btn-check" data-toggle="button" aria-pressed="false"></button>
                      </div>
                      <div class="media-info">
                        <div class="media-info">
                          <div class="info-item">
                            <div class="source">
                              <div class="source-img">
                                <img src="content/images/icon-google-drive.png" />
                              </div>
                              Google Drive
                            </div>
                          </div>
                          <div class="info-item">100MB</div>
                          <div class="info-item">3일전</div>
                        </div>
                      </div>
                      <div class="lc-btn">
                        <a class="btn-close" href="#"><i class="icon-common icon-close"></i></a>
                      </div>
                    </div>
                    <div class="content-box">
                      <div class="lc-media">
                        <div class="media-body">
                          <a class="media-title" href="#">행복스럽고 구하기 광야에서 목숨이 피어나기 고행을 돋고.pdf</a>
                        </div>
                        <a class="media-file" href="#">
                          <img src="content/file-excel-fill.svg" />
                        </a>
                      </div>
                    </div>
                  </div>
                </li>
                <li class="lc-card-item" aria-selected="false">
                  <div class="item-wrap">
                    <div class="content-top">
                      <div class="lc-check">
                        <button type="button" class="btn btn-check" data-toggle="button" aria-pressed="false"></button>
                      </div>
                      <div class="media-info">
                        <div class="info-item">
                          <div class="badge">Text</div>
                        </div>
                        <div class="info-item">3일전</div>
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
                    <div class="content-box">
                      <div class="lc-media">
                        <div class="media-body">
                          <a class="media-title" href="#">아니한 안고, 이상의 우리의 청춘의 석가는 커다란 온갖 청춘은 있다.</a>
                          <p class="media-desc">
                            행복스럽고 구하기 광야에서 목숨이 피어나기 고행을 돋고, 만물은 뿐이다. 하여도 심장은 용감하고 교향악이다. 곳이
                            있는 사랑의 쓸쓸한 꽃이 위하여, 그러므로 가치를 곧 칼이다.
                          </p>
                        </div>
                      </div>
                    </div>
                  </div>
                </li>
                <li class="lc-card-item" aria-selected="false">
                  <div class="item-wrap">
                    <div class="content-top">
                      <div class="lc-check">
                        <button type="button" class="btn btn-check" data-toggle="button" aria-pressed="false"></button>
                      </div>
                      <div class="media-info">
                        <div class="info-item">
                          <div class="source">
                            <div class="source-img"><img src="content/images/sample/media-aju.png" /></div>
                            아시아경제
                          </div>
                        </div>
                        <div class="info-item">3일전</div>
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
                          <img src="content/images/sample/@445x290.png" />
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
                      <div class="lc-check">
                        <button type="button" class="btn btn-check" data-toggle="button" aria-pressed="false"></button>
                      </div>
                      <div class="media-info">
                        <div class="info-item">
                          <div class="source">
                            <div class="source-img" src="content/images/sample/media-aju.png"></div>
                            아시아경제
                          </div>
                        </div>
                        <div class="info-item">3일전</div>
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
                    <div class="content-box">
                      <div class="lc-media">
                        <div class="media-body">
                          <a class="media-title" href="#">아니한 안고, 이상의 우리의 청춘의 석가는 커다란 온갖 청춘은 있다.</a>
                          <p class="media-desc">
                            행복스럽고 구하기 광야에서 목숨이 피어나기 고행을 돋고, 만물은 뿐이다. 하여도 심장은 용감하고 교향악이다. 곳이
                            있는 사랑의 쓸쓸한 꽃이 위하여, 그러므로 가치를 곧 칼이다.
                          </p>
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
          </div>
        </div>
      </div>
    </div>
    <!-- panel-sub -->
    <div class="lc-panel-sub lc-panel-collection-group" :class="isGroupCollectionActive ? 'active' : ''">
      <div class="panel-collection panel-collection-group">
        <div class="panel-header">
          <div class="elements-left">
            <button type="button" class="btn btn-icon btn-back" @click.prevent="isGroupCollectionActive = false">
              <i class="icon-common-lg icon-arrow-left"></i>
            </button>
            <div class="panel-title">
              <i class="icon-common-lg icon-folder"></i>
              <strong class="title">Group</strong>
            </div>
          </div>
          <div class="elements-right">
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
          <div class="lc-list-top">
            <div class="elements-left">
              <div class="check-group">
                <button type="button" class="btn btn-check" id="list-check-all3" data-toggle="button" aria-pressed="false"></button>
                <label for="list-check-all3">전체 선택</label>
              </div>
              <div class="list-info">(<strong>0</strong>/20)</div>
            </div>
            <div class="elements-right">
              <button type="button" class="btn btn-default btn-sm"><i class="icon-common icon-close"></i>선택 삭제</button>
            </div>
          </div>
          <div class="lc-list-wrap">
            <div class="list-inner overflow-y-scroll">
              <ul class="lc-card-list">
                <li class="lc-card-item" aria-selected="false">
                  <div class="item-wrap">
                    <div class="content-top">
                      <div class="lc-check">
                        <button type="button" class="btn btn-check" data-toggle="button" aria-pressed="false"></button>
                      </div>
                      <div class="media-info">
                        <div class="info-item">
                          <div class="source">
                            <div class="source-img"><img src="content/images/sample/media-aju.png" /></div>
                            아시아경제
                          </div>
                        </div>
                        <div class="info-item">3일전</div>
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
                          <img src="content/images/sample/@445x290.png" />
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
      </div>
    </div>
    <div class="lc-panel-sub lc-panel-collection-add" :class="isAddCollectionActive ? 'active' : ''">
      <div class="panel-collection panel-collection-add">
        <div class="panel-header">
          <div class="elements-left">
            <div class="panel-title">
              <i class="go-icon-common icon-collection-add"></i>
              <strong>Add new Document to Collection</strong>
            </div>
          </div>
          <div class="elements-right">
            <button type="button" class="btn btn-icon btn-close" @click="isAddCollectionActive = false" id="btn-collection-add-close">
              <i class="icon-common icon-close"></i>
            </button>
          </div>
        </div>
        <div class="panel-body">
          <div class="tab-wrap">
            <div class="tab-nav lc-nav">
              <ul class="nav">
                <li class="nav-item">
                  <a class="nav-link active" data-toggle="tab" href="#lc-add-search" aria-selected="false">
                    <span class="text">Web Search</span>
                  </a>
                </li>
                <li class="nav-item">
                  <a class="nav-link" data-toggle="tab" href="#lc-add-memo" aria-selected="false">
                    <span class="text">Text</span>
                  </a>
                </li>
                <li class="nav-item">
                  <a class="nav-link" data-toggle="tab" href="#lc-add-url" aria-selected="false">
                    <span class="text">URL</span>
                  </a>
                </li>
                <li class="nav-item">
                  <a class="nav-link" data-toggle="tab" href="#lc-add-document" aria-selected="false">
                    <span class="text">Documents</span>
                  </a>
                </li>
              </ul>
            </div>
            <div class="tab-content">
              <div class="tab-pane fade show active" id="lc-add-search">
                <div class="panel-collection">
                  <div class="panel-header">
                    <div class="panel-title">
                      <!-- <i class="go-icon-common icon-websearch"></i> -->
                      <strong class="title">Web Search</strong>
                    </div>
                  </div>
                  <div class="panel-body">
                    <div class="add-input-area">
                      <div class="form-group search-group">
                        <input class="form-control" type="text" placeholder="키워드를 입력하세요." />
                        <button class="form-btn" type="button">
                          <i class="icon-common icon-search-pirmary"></i>
                        </button>
                      </div>
                    </div>
                    <div class="search-tab">
                      <div class="storage-filter">
                        <div class="filter-main">
                          <ul class="filter-list">
                            <li class="filter-item">
                              <a class="filter-link active" href="#">All</a>
                            </li>
                            <li class="filter-item">
                              <a class="filter-link" href="#">News</a>
                            </li>
                            <li class="filter-item">
                              <a class="filter-link" href="#">Videos</a>
                            </li>
                            <li class="filter-item">
                              <a class="filter-link" href="#">Documents</a>
                            </li>
                          </ul>
                        </div>
                      </div>
                    </div>
                    <div class="lc-list-top">
                      <div class="elements-left">
                        <div class="check-group">
                          <button
                            type="button"
                            class="btn btn-check"
                            id="list-check-all2"
                            data-toggle="button"
                            aria-pressed="false"
                          ></button>
                          <label for="list-check-all2">전체 선택</label>
                        </div>
                        <div class="list-info">(<strong>2</strong>/20)</div>
                      </div>
                      <div class="elements-right">
                        <button type="button" class="btn btn-default btn-sm"><i class="icon-common icon-close"></i>선택 삭제</button>
                      </div>
                    </div>
                    <div class="lc-list-wrap">
                      <div class="list-inner overflow-y-scroll">
                        <ul class="lc-card-list">
                          <li class="lc-card-item" aria-selected="false">
                            <div class="item-wrap">
                              <div class="content-top">
                                <div class="lc-check">
                                  <button type="button" class="btn btn-check" data-toggle="button" aria-pressed="false"></button>
                                </div>
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
                              <div class="content-box">
                                <div class="lc-media">
                                  <div class="media-body">
                                    <a class="media-title" href="#">아니한 안고, 이상의 우리의 청춘의 석가는 커다란 온갖 청춘은 있다.</a>
                                    <p class="media-desc">
                                      행복스럽고 구하기 광야에서 목숨이 피어나기 고행을 돋고, 만물은 뿐이다. 하여도 심장은 용감하고
                                      교향악이다. 곳이 있는 사랑의 쓸쓸한 꽃이 위하여, 그러므로 가치를 곧 칼이다.
                                    </p>
                                  </div>
                                  <a class="media-img" href="#">
                                    <img src="content/images/sample/@445x290.png" />
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
                    <div class="elements-right">
                      <button type="button" class="btn btn-secondary">스토리지에 추가</button>
                    </div>
                  </div>
                </div>
              </div>
              <div class="tab-pane fade" id="lc-add-memo">
                <div class="panel-collection">
                  <div class="panel-header">
                    <div class="panel-title">
                      <!-- 	<i class="go-icon-common icon-memo-write"></i> -->
                      <strong class="title">Text</strong>
                    </div>
                  </div>
                  <div class="panel-body">
                    <div class="add-input-area memo-area">
                      <div class="memo-title">
                        <input type="text" class="form-control" placeholder="Title" />
                      </div>
                      <div class="memo-desc">
                        <textarea class="form-control" placeholder="Write a text"></textarea>
                      </div>
                    </div>
                  </div>
                  <div class="panel-footer">
                    <div class="elements-right">
                      <button type="button" class="btn btn-secondary">스토리지에 추가</button>
                    </div>
                  </div>
                </div>
              </div>
              <div class="tab-pane fade" id="lc-add-url">
                <div class="panel-collection">
                  <div class="panel-header">
                    <div class="panel-title">
                      <!-- <i class="go-icon-common icon-url"></i> -->
                      <strong class="title">URL</strong>
                    </div>
                  </div>
                  <div class="panel-body">
                    <div class="add-input-area">
                      <div class="form-group url-group">
                        <input class="form-control" type="url" placeholder="URL을 입력하거나 복사한 URL을 붙여 넣으세요." />
                        <button class="form-btn" type="button">
                          <i class="icon-common icon-link-pirmary"></i>
                        </button>
                      </div>
                    </div>
                    <div class="lc-list-top">
                      <div class="elements-left">
                        <div class="check-group">
                          <button
                            type="button"
                            class="btn btn-check"
                            id="list-check-all5"
                            data-toggle="button"
                            aria-pressed="false"
                          ></button>
                          <label for="list-check-all5">전체 선택</label>
                        </div>
                        <div class="list-info">(<strong>2</strong>/20)</div>
                      </div>
                      <div class="elements-right">
                        <button type="button" class="btn btn-default btn-sm"><i class="icon-common icon-close"></i>선택 삭제</button>
                      </div>
                    </div>
                    <div class="lc-list-wrap">
                      <div class="list-inner overflow-y-scroll">
                        <ul class="lc-card-list">
                          <li class="lc-card-item" aria-selected="false">
                            <div class="item-wrap">
                              <div class="content-top">
                                <div class="lc-check">
                                  <button type="button" class="btn btn-check" data-toggle="button" aria-pressed="false"></button>
                                </div>
                                <div class="media-info">
                                  <div class="info-item">
                                    <div class="source">
                                      <div class="source-img"><img src="content/images/sample/media-aju.png" /></div>
                                      아시아경제
                                    </div>
                                  </div>
                                  <div class="info-item">3일전</div>
                                </div>
                                <div class="lc-btn">
                                  <div class="more-area">
                                    <a class="btn-close" href="#"><i class="icon-common icon-close"></i></a>
                                  </div>
                                </div>
                              </div>
                              <div class="content-box">
                                <div class="lc-media">
                                  <div class="media-body">
                                    <a class="media-title" href="#">아니한 안고, 이상의 우리의 청춘의 석가는 커다란 온갖 청춘은 있다.</a>
                                    <p class="media-desc">
                                      행복스럽고 구하기 광야에서 목숨이 피어나기 고행을 돋고, 만물은 뿐이다. 하여도 심장은 용감하고
                                      교향악이다. 곳이 있는 사랑의 쓸쓸한 꽃이 위하여, 그러므로 가치를 곧 칼이다.
                                    </p>
                                  </div>
                                  <a class="media-img" href="#">
                                    <img src="content/images/sample/@445x290.png" />
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
                    <div class="elements-right">
                      <button type="button" class="btn btn-secondary">스토리지에 추가</button>
                    </div>
                  </div>
                </div>
              </div>
              <div class="tab-pane fade" id="lc-add-document">
                <div class="panel-collection">
                  <div class="panel-header">
                    <div class="panel-title">
                      <!-- <i class="go-icon-common icon-document-add"></i> -->
                      <strong class="title">Documents</strong>
                    </div>
                  </div>
                  <div class="panel-body">
                    <div class="add-input-area">
                      <div class="learning-tool">
                        <ul class="learning-list">
                          <li class="item-pc">
                            <a href="#">
                              <span class="icon-area"><i class="bi bi-tv"></i></span>
                              <span class="name">내 컴퓨터</span>
                            </a>
                          </li>
                          <li class="item-channel">
                            <a class="item-link" href="#">
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
                            type="button"
                            class="btn btn-check"
                            id="list-check-all4"
                            data-toggle="button"
                            aria-pressed="false"
                          ></button>
                          <label for="list-check-all4">전체 선택</label>
                        </div>
                        <div class="list-info">(<strong>2</strong>/20)</div>
                      </div>
                      <div class="elements-right">
                        <button type="button" class="btn btn-default btn-sm"><i class="icon-common icon-close"></i>선택 삭제</button>
                      </div>
                    </div>
                    <div class="lc-list-wrap">
                      <div class="list-inner overflow-y-scroll">
                        <ul class="lc-card-list">
                          <li class="lc-card-item" aria-selected="false">
                            <div class="item-wrap">
                              <div class="content-top">
                                <div class="lc-check">
                                  <button type="button" class="btn btn-check" data-toggle="button" aria-pressed="false"></button>
                                </div>
                                <div class="media-info">
                                  <div class="info-item">
                                    <div class="source">내 컴퓨터</div>
                                  </div>
                                  <div class="info-item">100MB</div>
                                  <div class="info-item">3일전</div>
                                </div>
                                <div class="lc-btn">
                                  <a class="btn-close" href="#"><i class="icon-common icon-close"></i></a>
                                </div>
                              </div>
                              <div class="content-box">
                                <div class="lc-media">
                                  <div class="media-body">
                                    <a class="media-title" href="#">행복스럽고 구하기 광야에서 목숨이 피어나기 고행을 돋고.pdf</a>
                                  </div>
                                  <a class="media-file" href="#">
                                    <img src="content/images/common/file/file-pdf-fill.svg" />
                                  </a>
                                </div>
                              </div>
                            </div>
                          </li>
                          <li class="lc-card-item" aria-selected="false">
                            <div class="item-wrap">
                              <div class="content-top">
                                <div class="lc-check">
                                  <button type="button" class="btn btn-check" data-toggle="button" aria-pressed="false"></button>
                                </div>
                                <div class="media-info">
                                  <div class="media-info">
                                    <div class="info-item">
                                      <div class="source">
                                        <div class="source-img">
                                          <img src="content/images/channel/icon-google-drive.png" />
                                        </div>
                                        Google Drive
                                      </div>
                                    </div>
                                    <div class="info-item">100MB</div>
                                    <div class="info-item">3일전</div>
                                  </div>
                                </div>
                                <div class="lc-btn">
                                  <a class="btn-close" href="#"><i class="icon-common icon-close"></i></a>
                                </div>
                              </div>
                              <div class="content-box">
                                <div class="lc-media">
                                  <div class="media-body">
                                    <a class="media-title" href="#">행복스럽고 구하기 광야에서 목숨이 피어나기 고행을 돋고.pdf</a>
                                  </div>
                                  <a class="media-file" href="#">
                                    <img src="content/images/common/file/file-excel-fill.svg" />
                                  </a>
                                </div>
                              </div>
                            </div>
                          </li>
                        </ul>
                        <!--
                        <div class="no-content">
                          <i class="bi bi-file-earmark-text"></i>
                          <strong>새로운 Document 가 없습니다.</strong>
                          <p>내 컴퓨터 및 공유 드라이브에서 파일을 선택하거나,<br>추가할 파일을 마우스로 끌어서 추가할 수 있습니다.</p>
                        </div>
                        -->
                      </div>
                    </div>
                  </div>
                  <div class="panel-footer">
                    <div class="elements-right">
                      <button type="button" class="btn btn-secondary">스토리지에 추가</button>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./list-collection.component.ts"></script>
