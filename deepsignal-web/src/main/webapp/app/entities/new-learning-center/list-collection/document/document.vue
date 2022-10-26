<template>
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
          <button type="button" class="btn btn-check" id="list-check-all4" data-toggle="button" aria-pressed="false"></button>
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
      </div>
    </div>
  </div>
</template>
