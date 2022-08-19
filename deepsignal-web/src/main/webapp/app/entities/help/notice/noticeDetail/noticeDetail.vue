<template>
  <div>
    <!--    <h1>this is noticeDetail - id: {{ id }}</h1>-->
    <div id="container-block">
      <div class="container">
        <h1 class="page-title">공지사항</h1>
        <div class="board-detail">
          <div class="board-detail-header">
            <span class="badge badge-outline-primary badge-square">공지</span>
            <span class="date">2021.09.24</span>
            <h2 class="tit">‘상반기 최대실적’ 솔트룩스, 하반기엔 혁신적 AI 신사업 및 서비스 출시</h2>
          </div>
          <div class="board-detail-body">
            <div>에디터 내용</div>
          </div>
          <div class="board-detail-file">
            <div class="d-flex justify-content-between">
              <div class="file-count">
                <i class="icon-file"></i>
                <span>첨부파일</span>
                <a href="#" @click="toggleFile($event)" role="button">2개</a>
              </div>
              <button class="btn btn-sm btn-outline-lightgray">모두저장</button>
            </div>
            <ul class="file-list" v-if="isListFile">
              <li>
                <div class="type">
                  <img src="content/images/file-excel.png" alt="" />
                </div>
                <div class="tit">
                  <a href="#none">개인정보동의서_파일명이 길었을 때 말 줌임 표시를 … 합니다.doc</a>
                </div>
                <div class="vol">31.5KB</div>
                <div class="down">
                  <a href="#none" aria-label="Download"><i class="icon-download"></i></a>
                </div>
              </li>
              <li>
                <div class="type">
                  <img src="content/images/file-pdf.png" alt="" />
                </div>
                <div class="tit">
                  <a href="#none">모집공고.pdf</a>
                </div>
                <div class="vol">31.5KB</div>
                <div class="down">
                  <a href="#none" aria-label="Download"><i class="icon-download"></i></a>
                </div>
              </li>
            </ul>
          </div>
          <div class="board-detail-footer">
            <div class="board-pager">
              <div class="item">
                <div class="icon"><i class="icon-arrow-up"></i></div>
                <div class="dir">이전글</div>
                <div class="category">공지사항</div>
                <div class="tit">
                  <a href="">Chatbot 서비스 이용약관 개정 안내</a>
                </div>
                <div class="date">2021.09.24</div>
              </div>
              <div class="item">
                <div class="icon"><i class="icon-arrow-down"></i></div>
                <div class="dir">다음글</div>
                <div class="category">이벤트</div>
                <div class="tit">
                  <a href="">서버 유지보수 작업에 따른 홈페이지 이용제한 안내</a>
                </div>
                <div class="date">2021.09.24</div>
              </div>
            </div>
          </div>
        </div>
        <div class="d-flex justify-content-center m-t-50">
          <a href="noticeList.html" class="btn btn-outline-lightgray btn-lg h-50px min-width-186px">목록</a>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./noticeDetail.component.ts"></script>
