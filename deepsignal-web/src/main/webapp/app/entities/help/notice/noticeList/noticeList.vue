<template>
  <div>
    <div id="container-block">
      <div class="container">
        <h1 class="page-title" v-text="$t('notice.notice')">공지사항</h1>
        <div class="board-search">
          <div class="row row-10">
            <div class="col">
              <div class="col">
                <div class="input-area">
                  <input type="text" class="form-control" :placeholder="$t('notice.placeHolder')" />
                </div>
              </div>
            </div>
            <div class="col-auto">
              <button
                type="button"
                class="btn btn-primary"
                onclick="document.location='noticeListResult.html'"
                v-text="$t('notice.search')"
              >
                검색하기
              </button>
            </div>
          </div>
        </div>
        <div>
          <div class="board-result" v-if="isNoticeResultList">
            <p><strong>“검색 키워드”</strong> 에 대해 <i>79</i>건이 검색되었습니다.</p>
          </div>
          <div class="board-result" v-else-if="isNoticeEmptyList">
            <p><strong>“검색 키워드”</strong> 에 대해 <i>0</i>건이 검색되었습니다.</p>
          </div>
        </div>
        <div class="sub-category">
          <ul>
            <li class="active"><a href="#none" v-text="$t('notice.all')">전체</a></li>
            <li><a href="#none" v-text="$t('notice.notification')">공지</a></li>
            <li><a href="#none" v-text="$t('notice.event')">이벤트</a></li>
          </ul>
        </div>
        <div v-if="isNoticeList">
          <div class="board-list">
            <h3>This is noticeList</h3>
            <ul>
              <li>
                <div class="cate">공지사항</div>
                <div class="tit">
                  <a href="noticeDetail.html">고객센터 실시간상담 시스템 점검</a>

                  <span class="badge badge-dark">Top</span>
                </div>
                <div class="date">2021.09.24</div>
              </li>

              <li>
                <div class="cate">공지사항</div>
                <div class="tit">
                  <a href="noticeDetail.html">고객센터 실시간상담 시스템 점검</a>

                  <span class="badge badge-danger">New</span>
                </div>
                <div class="date">2021.09.24</div>
              </li>

              <li>
                <div class="cate">공지사항</div>
                <div class="tit">
                  <a href="noticeDetail.html">고객센터 실시간상담 시스템 점검</a>
                </div>
                <div class="date">2021.09.24</div>
              </li>

              <li>
                <div class="cate">공지사항</div>
                <div class="tit">
                  <a href="noticeDetail.html">고객센터 실시간상담 시스템 점검</a>
                </div>
                <div class="date">2021.09.24</div>
              </li>

              <li>
                <div class="cate">공지사항</div>
                <div class="tit">
                  <a href="noticeDetail.html">고객센터 실시간상담 시스템 점검</a>
                </div>
                <div class="date">2021.09.24</div>
              </li>

              <li>
                <div class="cate">공지사항</div>
                <div class="tit">
                  <a href="noticeDetail.html">고객센터 실시간상담 시스템 점검</a>
                </div>
                <div class="date">2021.09.24</div>
              </li>

              <li>
                <div class="cate">공지사항</div>
                <div class="tit">
                  <a href="noticeDetail.html">고객센터 실시간상담 시스템 점검</a>
                </div>
                <div class="date">2021.09.24</div>
              </li>

              <li>
                <div class="cate">공지사항</div>
                <div class="tit">
                  <a href="noticeDetail.html">고객센터 실시간상담 시스템 점검</a>
                </div>
                <div class="date">2021.09.24</div>
              </li>

              <li>
                <div class="cate">공지사항</div>
                <div class="tit">
                  <a href="noticeDetail.html">고객센터 실시간상담 시스템 점검</a>
                </div>
                <div class="date">2021.09.24</div>
              </li>

              <li>
                <div class="cate">공지사항</div>
                <div class="tit">
                  <a href="noticeDetail.html">고객센터 실시간상담 시스템 점검</a>
                </div>
                <div class="date">2021.09.24</div>
              </li>
            </ul>
          </div>
          <div class="board-more">
            <a href="javascript:;">전체 검색 결과 더보기 (1/8) </a>
          </div>
        </div>
        <div v-else-if="isNoticeEmptyList">
          <div class="board-list">
            <div class="board-empty">
              <p>검색된 결과가 없습니다.</p>
            </div>
          </div>
          <div class="d-flex justify-content-center m-t-50">
            <a href="noticeList.html" class="btn btn-outline-lightgray btn-lg h-50px min-width-186px">목록</a>
          </div>
        </div>
        <div v-else-if="isNoticeResultList">
          <div class="board-list">
            <ul>
              <li>
                <div class="cate">공지사항</div>
                <div class="tit">
                  <a href="noticeDetail.html">고객센터 실시간상담 시스템 점검</a>

                  <span class="badge badge-dark">Top</span>
                </div>
                <div class="date">2021.09.24</div>
              </li>

              <li>
                <div class="cate">공지사항</div>
                <div class="tit">
                  <a href="noticeDetail.html">고객센터 실시간상담 시스템 점검</a>

                  <span class="badge badge-danger">New</span>
                </div>
                <div class="date">2021.09.24</div>
              </li>

              <li>
                <div class="cate">공지사항</div>
                <div class="tit">
                  <a href="noticeDetail.html">고객센터 실시간상담 시스템 점검</a>
                </div>
                <div class="date">2021.09.24</div>
              </li>

              <li>
                <div class="cate">공지사항</div>
                <div class="tit">
                  <a href="noticeDetail.html">고객센터 실시간상담 시스템 점검</a>
                </div>
                <div class="date">2021.09.24</div>
              </li>

              <li>
                <div class="cate">공지사항</div>
                <div class="tit">
                  <a href="noticeDetail.html">고객센터 실시간상담 시스템 점검</a>
                </div>
                <div class="date">2021.09.24</div>
              </li>

              <li>
                <div class="cate">공지사항</div>
                <div class="tit">
                  <a href="noticeDetail.html">고객센터 실시간상담 시스템 점검</a>
                </div>
                <div class="date">2021.09.24</div>
              </li>

              <li>
                <div class="cate">공지사항</div>
                <div class="tit">
                  <a href="noticeDetail.html">고객센터 실시간상담 시스템 점검</a>
                </div>
                <div class="date">2021.09.24</div>
              </li>

              <li>
                <div class="cate">공지사항</div>
                <div class="tit">
                  <a href="noticeDetail.html">고객센터 실시간상담 시스템 점검</a>
                </div>
                <div class="date">2021.09.24</div>
              </li>

              <li>
                <div class="cate">공지사항</div>
                <div class="tit">
                  <a href="noticeDetail.html">고객센터 실시간상담 시스템 점검</a>
                </div>
                <div class="date">2021.09.24</div>
              </li>

              <li>
                <div class="cate">공지사항</div>
                <div class="tit">
                  <a href="noticeDetail.html">고객센터 실시간상담 시스템 점검</a>
                </div>
                <div class="date">2021.09.24</div>
              </li>
            </ul>
          </div>
          <div class="board-more">
            <a href="javascript:;">전체 검색 결과 더보기 (1/8) </a>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./noticeList.component.ts"></script>
