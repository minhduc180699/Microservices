<template>
  <div id="wrap" @mouseup="mouseUp($event)">
    <div id="container-block">
      <div class="container p-t-60">
        <div class="title-area">
          <div class="tit">
            <strong># fund</strong>
            <em>Entities</em>
            <p v-text="$t('learningManagement.title')">토픽이 가지고 있는 상세 의미를 엔티티라고 합니다.</p>
          </div>
        </div>
        <div class="row row-20 align-items-center mb-3">
          <div class="col">
            <div class="resources-category">
              <a href="/myconnectome/learning-management/learning-doc"><i class="icon-resources-doc"></i><span>Document</span></a>
              <!--              <a href="/myconnectome/learning-management/learning-tag"><i class="icon-resources-tag"></i><span>Search</span></a>-->
              <a href="/myconnectome/learning-management/learning-web"><i class="icon-resources-web"></i><span>Search/Web</span></a>
              <a href="/myconnectome/learning-management/learning-social" class="active"
                ><i class="icon-resources-social"></i><span>Social</span></a
              >
            </div>
          </div>
          <div class="col-auto">
            <div class="d-flex align-items-center">
              <div class="item-search" id="item-search">
                <input type="text" class="form-control" title="검색어 입력" @click="clickButtonSearch($event)" />
                <a href="javascript:;" role="button" aria-label="삭제"><i class="icon-history-del"></i></a>
                <a href="#"><i class="icon-search"></i></a>
              </div>
              <div class="item-search-calendar">
                <input type="text" class="form-control" title="검색 기간 입력" readonly @click="clickButtonCalendar($event)" />
                <a href="javascript:;" aria-label="날짜선택" role="button"><i class="icon-calendar-gray"></i></a>
                <div class="datepicker-layer">
                  <p>검색 기간을 선택해주세요.</p>
                  <div class="radio-button-type">
                    <span class="radio-button mr-0">
                      <input type="radio" id="fType1" name="fType" checked="" />
                      <label for="fType1">오늘</label>
                    </span>
                    <span class="radio-button mr-0">
                      <input type="radio" id="fType2" name="fType" />
                      <label for="fType2">1주일</label>
                    </span>
                    <span class="radio-button mr-0">
                      <input type="radio" id="fType3" name="fType" />
                      <label for="fType3">1개월</label>
                    </span>
                    <span class="radio-button mr-0">
                      <input type="radio" id="fType4" name="fType" />
                      <label for="fType4">3개월 </label>
                    </span>
                  </div>
                  <div class="row row-10 align-items-center">
                    <div class="col">
                      <input type="text" class="form-control d-block" readonly />
                    </div>
                    <div class="col-auto">~</div>
                    <div class="col">
                      <input type="text" class="form-control d-block" readonly />
                    </div>
                  </div>
                  <!--                  <div id='datepicker' ref='datepicker'></div>-->
                  <b-calendar hide-header style="margin-top: 7px" locale="en-US"></b-calendar>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="card">
          <div class="card-body">
            <div class="learning-tool m-t-40">
              <ul>
                <li class="checked">
                  <a href="">
                    <span class="ico"><img src="content/images/resrouces-social-facebook.png" alt="" /></span>
                    <span class="name">Facebook</span>
                  </a>
                </li>
                <li class="locked">
                  <a href="">
                    <span class="ico"><img src="content/images/resrouces-social-twitter.png" alt="" /></span>
                    <span class="name">twitter</span>
                  </a>
                </li>
                <li class="locked">
                  <a href="">
                    <span class="ico"><img src="content/images/resrouces-social-linkedin.png" alt="" /></span>
                    <span class="name">Linkedin</span>
                  </a>
                </li>
                <li class="locked">
                  <a href="">
                    <span class="ico"><img src="content/images/resrouces-social-instagram.png" alt="" /></span>
                    <span class="name">Instagram</span>
                  </a>
                </li>
                <li class="locked">
                  <a href="">
                    <span class="ico"><img src="content/images/resrouces-social-naver.png" alt="" /></span>
                    <span class="name">Naver</span>
                  </a>
                </li>
              </ul>
            </div>
            <div class="resources-date m-t-40">2021.09.15</div>
            <div class="resources row row-20">
              <div class="col-6">
                <div class="resource box-shadow-none">
                  <div class="row row-20 align-items-center">
                    <div class="col">
                      <div class="tit"><a href="">주식 갤러리</a> <i class="icon-check-resource"></i></div>
                      <div class="info">https://news.sbs.co.kr > news > newsflash</div>
                    </div>
                  </div>
                  <button type="button" class="btn-del" aria-label="삭제"><i class="icon-resources-del"></i></button>
                </div>
              </div>
              <div class="col-6">
                <div class="resource box-shadow-none">
                  <div class="row row-20 align-items-center">
                    <div class="col">
                      <div class="tit"><a href="">재테크 갤러리 – 주식, 선물, 옵션, 코인, 부동산</a> <i class="icon-lock"></i></div>
                      <div class="info">https://news.sbs.co.kr > news > newsflash</div>
                    </div>
                  </div>
                  <button type="button" class="btn-del" aria-label="삭제"><i class="icon-resources-del"></i></button>
                </div>
              </div>
            </div>
            <div class="resources-date m-t-60">2021.09.14</div>
            <div class="resources row row-20">
              <div class="col-6">
                <div class="resource box-shadow-none">
                  <div class="row row-20 align-items-center">
                    <div class="col">
                      <div class="tit"><a href="">부동산 지식iN</a> <i class="icon-lock"></i> <i class="icon-warning"></i></div>
                      <div class="info">https://news.sbs.co.kr > news > newsflash</div>
                    </div>
                    <button type="button" class="btn-del" aria-label="삭제"><i class="icon-resources-del"></i></button>
                  </div>
                </div>
              </div>
            </div>
            <div class="resources-date m-t-60">2021.09.10</div>
            <div class="resources row row-20">
              <div class="col-6">
                <div class="resource box-shadow-none">
                  <div class="row row-20 align-items-center">
                    <div class="col">
                      <div class="tit"><a href="">부동산 지식iN</a></div>
                      <div class="info">https://news.sbs.co.kr > news > newsflash</div>
                    </div>
                  </div>
                  <button type="button" class="btn-del" aria-label="삭제"><i class="icon-resources-del"></i></button>
                </div>
              </div>

              <div class="col-6">
                <div class="resource box-shadow-none">
                  <div class="row row-20 align-items-center">
                    <div class="col">
                      <div class="tit"><a href="">부동산 지식iN</a></div>
                      <div class="info">https://news.sbs.co.kr > news > newsflash</div>
                    </div>
                  </div>
                  <button type="button" class="btn-del" aria-label="삭제"><i class="icon-resources-del"></i></button>
                </div>
              </div>

              <div class="col-6">
                <div class="resource box-shadow-none">
                  <div class="row row-20 align-items-center">
                    <div class="col">
                      <div class="tit"><a href="">부동산 지식iN</a></div>
                      <div class="info">https://news.sbs.co.kr > news > newsflash</div>
                    </div>
                  </div>
                  <button type="button" class="btn-del" aria-label="삭제"><i class="icon-resources-del"></i></button>
                </div>
              </div>

              <div class="col-6">
                <div class="resource box-shadow-none">
                  <div class="row row-20 align-items-center">
                    <div class="col">
                      <div class="tit"><a href="">부동산 지식iN</a></div>
                      <div class="info">https://news.sbs.co.kr > news > newsflash</div>
                    </div>
                  </div>
                  <button type="button" class="btn-del" aria-label="삭제"><i class="icon-resources-del"></i></button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <!-- floating buttons -->
    <div class="floating-btns">
      <div class="feed-functions">
        <a href="#" class="btn-icon btn-top" role="button">TOP</a>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./learningSocial.component.ts"></script>

<style>
.b-calendar-grid-caption {
  height: 50px;
}
</style>
