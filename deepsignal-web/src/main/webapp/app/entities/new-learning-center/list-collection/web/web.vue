<template>
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
          <button type="button" class="btn btn-check" id="list-check-all5" data-toggle="button" aria-pressed="false"></button>
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
                      행복스럽고 구하기 광야에서 목숨이 피어나기 고행을 돋고, 만물은 뿐이다. 하여도 심장은 용감하고 교향악이다. 곳이 있는
                      사랑의 쓸쓸한 꽃이 위하여, 그러므로 가치를 곧 칼이다.
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
</template>
