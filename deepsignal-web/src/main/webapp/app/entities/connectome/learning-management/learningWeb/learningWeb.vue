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
              <a href="/myconnectome/learning-management/learning-web" class="active"
                ><i class="icon-resources-web"></i><span>Search/Web</span></a
              >
              <a href="/myconnectome/learning-management/learning-social"><i class="icon-resources-social"></i><span>Social</span></a>
            </div>
          </div>
          <div class="col-auto">
            <div class="d-flex align-items-center">
              <div class="item-search">
                <input type="text" class="form-control" title="검색어 입력" @click="clickButtonSearch($event)" />
                <a href="javascript:;" role="button" aria-label="삭제"><i class="icon-history-del"></i></a>
                <a href=""><i class="icon-search"></i></a>
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
                  <b-calendar hide-header style="margin-top: 7px" locale="en-US"></b-calendar>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="card">
          <button
            type="button"
            class="btn-delete-all"
            style="color: grey; display: inline; text-align: right; padding-right: 1.25rem"
            @click="delateAll"
          >
            <i class="icon-resources-del"></i>
            Delete all
          </button>
          <div class="card-body" v-for="data in dataList" :key="data">
            <div class="resources-date m-t-40" v-if="list[data - 1]">{{ list[data - 1].date }}</div>
            <div class="resources-date m-t-40" v-else>&#160;</div>
            <div class="resources row row-20" v-if="list[data - 1]">
              <div class="col-6" v-for="value in list[data - 1].fileInfoList" :key="value.id">
                <div class="resource box-shadow-none">
                  <div class="row row-20 align-items-center">
                    <div class="col">
                      <div class="tit">
                        <a href="">{{ value.name }}</a>
                      </div>
                      <div class="info">{{ value.path }}</div>
                    </div>
                  </div>
                  <button type="button" class="btn-del" aria-label="삭제" @click="deleteFile(value)">
                    <i class="icon-resources-del"></i>
                  </button>
                </div>
              </div>
            </div>
          </div>
          <div class="row" style="margin-bottom: 10px">
            <div class="col-8"></div>
            <div class="col-auto">
              <nav aria-label="Page navigation">
                <div>
                  <ul class="pagination justify-content-center">
                    <li class="page-item" v-if="firstPageVal">
                      <a class="page-link" href="#" @click="firstPage($event)">
                        <svg width="13.5" height="12.5">
                          <path stroke="#516eff" fill="none" d="M10.858 11.498 5.499 5.997l5.359-5.5" />
                          <path stroke="#516eff" fill="none" d="M5.858 11.498.499 5.997l5.359-5.5" />
                        </svg>
                      </a>
                    </li>
                    <li class="page-item disabled" v-if="!firstPageVal">
                      <a class="page-link" href="#" @click="firstPage($event)">
                        <svg width="13.5" height="12.5">
                          <path stroke="#516eff" fill="none" d="M10.858 11.498 5.499 5.997l5.359-5.5" />
                          <path stroke="#516eff" fill="none" d="M5.858 11.498.499 5.997l5.359-5.5" />
                        </svg>
                      </a>
                    </li>
                    <li class="page-item" v-if="previousPageVal">
                      <a class="page-link" href="#" @click="previousPage($event)">
                        <svg width="8.5" height="12.5">
                          <path stroke="#516eff" fill="none" d="M5.858 11.498.499 5.997l5.359-5.5" />
                        </svg>
                      </a>
                    </li>
                    <li class="page-item disabled" v-if="!previousPageVal">
                      <a class="page-link" href="#" @click="previousPage($event)">
                        <svg width="8.5" height="12.5">
                          <path stroke="#516eff" fill="none" d="M5.858 11.498.499 5.997l5.359-5.5" />
                        </svg>
                      </a>
                    </li>
                    <li class="page-item" aria-current="page"><a class="page-link" v-if="prev">...</a></li>
                    <div v-for="index in total" :key="index">
                      <li v-if="index <= page + 1 && index >= page - 1" class="page-item" aria-current="page" :id="'page' + index">
                        <a class="page-link" href="#" @click="changePage(index, $event)">{{ index }}</a>
                      </li>
                    </div>
                    <li class="page-item" aria-current="page"><a class="page-link" v-if="next">...</a></li>
                    <li class="page-item" v-if="nextPageVal">
                      <a class="page-link" href="#" @click="nextPage($event)">
                        <svg width="8.5" height="12.5">
                          <path stroke="#516eff" fill="none" d="m2.141.497 5.359 5.5-5.359 5.501" />
                        </svg>
                      </a>
                    </li>
                    <li class="page-item disabled" v-if="!nextPageVal">
                      <a class="page-link" href="#" @click="nextPage($event)">
                        <svg width="8.5" height="12.5">
                          <path stroke="#516eff" fill="none" d="m2.141.497 5.359 5.5-5.359 5.501" />
                        </svg>
                      </a>
                    </li>
                    <li class="page-item" v-if="lastPageVal">
                      <a class="page-link" href="#" @click="lastPage($event)">
                        <svg width="13.5" height="12.5">
                          <path stroke="#516eff" fill="none" d="m2.141.497 5.358 5.5-5.358 5.501" />
                          <path stroke="#516eff" fill="none" d="m7.141.497 5.358 5.5-5.358 5.501" />
                        </svg>
                      </a>
                    </li>
                    <li class="page-item disabled" v-if="!lastPageVal">
                      <a class="page-link" href="#" @click="lastPage($event)">
                        <svg width="13.5" height="12.5">
                          <path stroke="#516eff" fill="none" d="m2.141.497 5.358 5.5-5.358 5.501" />
                          <path stroke="#516eff" fill="none" d="m7.141.497 5.358 5.5-5.358 5.501" />
                        </svg>
                      </a>
                    </li>
                  </ul>
                </div>
              </nav>
            </div>
          </div>
        </div>
      </div>
    </div>
    <b-modal id="deleteData" centered header-bg-variant="info" footer-bg-variant="white" @ok="deleteOk">
      <h3 style="margin-top: 30px">Are you sure to delete file: {{ fileName }}?</h3>
    </b-modal>
    <b-modal id="deleteAllData" centered header-bg-variant="info" footer-bg-variant="white" @ok="deleteAllOk">
      <h3 style="margin-top: 30px">Are you sure to delete all files?</h3>
    </b-modal>
    <b-toast variant="success" title="Delete file: " id="toastSuccess" toaster="b-toaster-bottom-right">
      <h5>Delete successfully!</h5>
    </b-toast>
    <b-toast variant="danger" title="Delete file: " id="toastFailure" toaster="b-toaster-bottom-right">
      <h5>Delete failed!</h5>
    </b-toast>
  </div>
</template>

<script lang="ts" src="./learningWeb.component.ts"></script>

<style>
.b-calendar-grid-caption {
  height: 50px;
}
.modal-footer {
  margin-top: 10px;
}
</style>
