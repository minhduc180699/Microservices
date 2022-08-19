<template>
  <div>
    <div id="wrap">
      <div id="container-block">
        <div class="container p-t-60">
          <div class="row row-40" data-folding="false">
            <div class="col folder">
              <div class="title-area">
                <div class="row align-items-center justify-content-between">
                  <div class="col-auto">
                    <div class="tit">
                      <strong>'Korea Fund'</strong>
                      <span>에 대한 학습 정보입니다.</span>
                    </div>
                  </div>
                  <div class="col-auto">
                    <div class="folding-toggler">
                      <a href="javascript:;" class="btn-hide"><span>접어두기</span><i class="icon-folding"></i></a>
                      <a href="javascript:;" class="btn-together"><span>함께보기</span><i class="icon-folding"></i></a>
                    </div>
                  </div>
                </div>
              </div>
              <div class="search-item-info" v-show="!noSearchResult">
                <div class="row row-0 align-items-center justify-content-between">
                  <div class="col-auto"></div>
                  <div class="col-auto">
                    <div class="count">검색결과 (<i>118</i>)</div>
                  </div>
                </div>
              </div>
              <div class="ai-message" v-show="noSearchResult">
                <div class="row row-10">
                  <div class="col-auto">
                    <i class="icon-bot"></i>
                  </div>
                  <div class="col">
                    <div class="message-card">
                      <div class="d-flex justify-content-between align-items-center">
                        <div class="keyword">“Korea Fund”</div>
                        <a href="#none" class="btn btn-light btn-round text-primary">Training Center</a>
                      </div>
                      <p>학습 정보가 부족해요. <br />관심 정보를 조금 더 알려주실 수 있으세요?</p>
                      <strong>웹에서 관심 정보를 찾아 오른쪽에 추천해 드렸어요. <br />살펴보시겠어요?</strong>
                      <div class="row row-10">
                        <div class="col-auto">
                          <button type="button" class="btn btn-white min-width-130px">아니오</button>
                          <button type="button" class="btn btn-primary min-width-130px m-l-5" id="btn-yes">예</button>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>

              <div v-show="!noSearchResult">
                <div class="row row-20 align-items-center mb-3" style="margin-top: -37px">
                  <div class="col-auto">
                    <h2 class="source-title">Topics</h2>
                  </div>
                  <div class="col-auto">
                    <p class="source-desc">검색어를 포함하거나 연관된 토픽 입니다.</p>
                  </div>
                </div>
                <div class="card border">
                  <div class="card-body">
                    <div class="topics">
                      <strong>추천하는 토픽 목록</strong>
                      <vue-custom-scrollbar class="customScroll" :settings="scrollSettings">
                        <div>
                          <ul v-for="n in 6" :key="n">
                            <!-- 클릭시 토픽 상세 모달 열림 -->
                            <li>
                              <a href="javascript:;" @click="topicClicked($event)"
                                >Hedge_<span>fund_</span>firms Hedge_fund_firmsHedge_fund_firms</a
                              ><i class="icon-favorite"></i><span class="sr-only">즐겨찾기</span>
                            </li>
                            <li>
                              <a href="javascript:;" @click="topicClicked($event)">EU <span>fund</span></a
                              ><i class="increase"><span class="sr-only">급상승</span></i>
                            </li>
                            <li>
                              <a href="javascript:;" @click="topicClicked($event)">US <span>fund</span></a>
                            </li>
                            <li>
                              <a href="javascript:;" @click="topicClicked($event)"><span>Fund</span>_for_Nature</a>
                            </li>
                          </ul>
                        </div>
                      </vue-custom-scrollbar>
                    </div>
                  </div>
                </div>
                <div class="source-more"><i class="icon-plus"></i><a href="">더보기</a></div>

                <div class="row row-20 align-items-center mb-3 m-t-80">
                  <div class="col-auto">
                    <h2 class="source-title">Entities</h2>
                  </div>
                  <div class="col-auto">
                    <p class="source-desc">검색어를 포함하거나 연관된 엔티티 입니다.</p>
                  </div>
                </div>
                <div class="card border">
                  <div class="card-body">
                    <div class="entities">
                      <a href="" class="btn btn-outline-lightgray">American_Hedge_<span class="text-primary">fund</span></a>
                      <a href="" class="btn btn-outline-lightgray">British <span class="text-primary">fund</span>raising</a>
                      <a href="" class="btn btn-outline-lightgray">Germany <span class="text-primary">fund</span>raising</a>
                      <a href="" class="btn btn-outline-lightgray">World Wide <span class="text-primary">Fund</span>_for_Nature</a>
                      <a href="" class="btn btn-outline-lightgray">Spain <span class="text-primary">fund</span>raising</a>
                    </div>
                  </div>
                </div>
                <div class="source-more"><i class="icon-plus"></i><a href="">더보기</a></div>

                <h2 class="source-title mb-2 m-t-80">Learning Resources</h2>
                <div class="row row-20 align-items-center justify-content-between mb-3">
                  <div class="col-auto">
                    <p class="source-desc">검색어를 포함하거나 연관된 엔티티 입니다.</p>
                  </div>
                  <div class="col-auto">
                    <div class="source-admin">
                      <a href="" class="text-primary"><strong>Training Center &gt;</strong></a>
                      <a href="learningResolurcesAdminDoc.html" aria-label="설정"><i class="icon-gear"></i> 학습소스 관리</a>
                    </div>
                  </div>
                </div>
                <div class="resources">
                  <div class="resource">
                    <div class="row row-20 align-items-center">
                      <div class="col-auto">
                        <img src="content/images/icon-excel-lg.png" alt="" />
                      </div>
                      <div class="col">
                        <div class="tit"><a href="">유안타증권_미국증시현황_최신.xlsx</a></div>
                        <div class="info">내 파일 | 84.53M</div>
                      </div>
                    </div>
                    <button type="button" class="btn-del" aria-label="삭제"><i class="icon-resources-del"></i></button>
                  </div>
                  <div class="resource">
                    <div class="row row-20 align-items-center">
                      <div class="col-auto">
                        <img src="content/images/icon-ppt.png" alt="" />
                      </div>
                      <div class="col">
                        <div class="tit"><a href="">20210920_키움증권_2022리포트.pptx</a></div>
                        <div class="info">내 파일 | 84.53M</div>
                      </div>
                    </div>
                  </div>
                  <div class="resource">
                    <div class="row row-20 align-items-center">
                      <div class="col">
                        <div class="tit"><a href="">주식 갤러리</a></div>
                        <div class="info">https://news.sbs.co.kr > news > newsflash</div>
                      </div>
                    </div>
                  </div>
                  <div class="resource">
                    <div class="row row-20 align-items-center">
                      <div class="col">
                        <div class="tit"><a href="">재테크 갤러리 – 주식, 선물, 옵션, 코인, 부동산</a></div>
                        <div class="info">https://news.sbs.co.kr > news > newsflash</div>
                      </div>
                    </div>
                  </div>
                  <div class="resource">
                    <div class="row row-20 align-items-center">
                      <div class="col">
                        <div class="tit"><a href="">부동산 지식iN</a></div>
                        <div class="info">https://news.sbs.co.kr > news > newsflash</div>
                      </div>
                    </div>
                  </div>
                </div>
                <div class="source-more"><i class="icon-plus"></i><a href="">더보기</a></div>

                <h2 class="source-title mb-2 m-t-80">Collected Channels</h2>
                <div class="row row-20 align-items-center justify-content-between mb-3">
                  <div class="col-auto">
                    <p class="source-desc">학습 정보에 맞춰 Feed를 수집할 채널을 설정하였습니다.</p>
                  </div>
                  <div class="col-auto">
                    <div class="source-admin">
                      <a href="collectedChannelsAdmin.html" aria-label="설정"><i class="icon-gear"></i> 수집채널 관리</a>
                    </div>
                  </div>
                </div>
                <div class="resources">
                  <div class="resource">
                    <div class="row row-20 align-items-center">
                      <div class="col">
                        <div class="tit"><a href="" class="text-primary">SBS 뉴스 최신/속보</a></div>
                        <div class="info">내 파일 | 84.53M</div>
                      </div>
                    </div>
                  </div>
                  <div class="resource">
                    <div class="row row-20 align-items-center">
                      <div class="col">
                        <div class="tit"><a href="" class="text-primary">부동산 지식iN</a></div>
                        <div class="info">https://news.sbs.co.kr > news > newsflash</div>
                      </div>
                    </div>
                  </div>
                </div>
                <div class="source-more"><i class="icon-plus"></i><a href="">더보기</a></div>
              </div>
            </div>
            <div class="col folder">
              <div class="title-area">
                <div class="row align-items-center justify-content-between">
                  <div class="col-auto">
                    <div class="tit">
                      <strong>'Korea Fund'</strong>
                      <span>웹 검색결과</span>
                      <a
                        href="javascript:;"
                        data-toggle="tooltip"
                        data-placement="bottom"
                        data-html="true"
                        title="추가 학습이 필요한 웹 검색결과입니다.  <br>학습에 필요한 컨텐츠를 선택하여 학습시켜주세요."
                        ><i class="icon-info"></i
                      ></a>
                    </div>
                  </div>
                  <div class="col-auto">
                    <div class="folding-toggler">
                      <a href="javascript:;" class="btn-hide"><span>접어두기</span><i class="icon-folding"></i></a>
                      <a href="javascript:;" class="btn-together"><span>함께보기</span><i class="icon-folding"></i></a>
                    </div>
                  </div>
                </div>
              </div>
              <div class="search-item-info">
                <div class="row row-0 align-items-center" :class="noSearchResult ? 'justify-content-end' : 'justify-content-between'">
                  <div class="col-auto" v-show="!noSearchResult">
                    <div class="radio-button-type">
                      <span class="radio-button">
                        <input type="radio" id="fType1" name="fType" checked />
                        <label for="fType1">전체</label>
                      </span>
                      <span class="radio-button">
                        <input type="radio" id="fType2" name="fType" />
                        <label for="fType2">뉴스</label>
                      </span>
                      <span class="radio-button">
                        <input type="radio" id="fType3" name="fType" />
                        <label for="fType3">소셜</label>
                      </span>
                      <span class="radio-button">
                        <input type="radio" id="fType4" name="fType" />
                        <label for="fType4">사이트</label>
                      </span>
                      <span class="radio-button">
                        <input type="radio" id="fType5" name="fType" />
                        <label for="fType5">문서</label>
                      </span>
                      <span class="radio-button">
                        <input type="radio" id="fType6" name="fType" />
                        <label for="fType6">이미지</label>
                      </span>
                    </div>
                  </div>
                  <div class="col-auto">
                    <div class="count">검색결과 (<i>118</i>)</div>
                  </div>
                </div>
              </div>
              <div class="item-list only-1x1" id="grid-item">
                <div v-for="n in 10" :key="n" class="item" data-template="news" data-size="1x1" data-type="01">
                  <div class="card" aria-selected="false">
                    <div class="card-header">
                      <div class="media">
                        <div class="logo"><i class="media-aju"></i></div>
                        <div class="name">아주경제</div>
                      </div>
                    </div>
                    <div class="card-body">
                      <div class="title max-line-4">SolarEdge 주가는 분기별 실적 최고치 예상 후 급등</div>
                      <div class="desc max-line-2">
                        태양광 인버터 제조업체인 SolarEdge의 주가는 2분기에 예상을 뛰어넘고 낙관적인 전망을 내놓은 후 15%이상 급등했습니다.
                      </div>
                      <div class="date">2021.07.20 오후 15:12</div>
                    </div>
                    <div class="card-dim">
                      <button type="button" aria-label="선택" class="btn-select"><i class="icon-check-dark"></i></button>
                      <button type="button" aria-label="보기" class="btn-detail" data-toggle="modal" data-target="#modalContentsDetail">
                        <i class="icon-search-dark"></i>
                      </button>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- selected card bar -->
    <div class="selected-item-bar">
      <div class="cart-status">
        <button type="button" aria-label="Cart"><i class="icon-check-primary"></i> <span class="num">0</span></button>
      </div>
      <div class="bar">
        <div class="row justify-content-between">
          <div class="col">
            <div class="selected-item">
              <ul>
                <li v-for="n in 30" :key="n">
                  <a href="#modalFeedDetail" class="tit" data-toggle="modal">연준 총재 10월부터 채권 매입 축소 전망</a>
                  <a href="" role="button" aria-label="삭제" class="del"><i class="icon-close"></i></a>
                </li>
              </ul>
            </div>
          </div>
          <div class="col-auto">
            <div class="d-flex">
              <button type="button" class="btn btn-outline-dark min-width-130px">다음</button>
              <button type="button" class="btn btn-dark min-width-130px m-l-10" onclick="document.location.href='learning.html'">
                학습합니다
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- modal : contents detail -->
    <div class="modal fade" id="modalContentsDetail" tabindex="-1" aria-hidden="true">
      <div class="modal-dialog modal-dialog-centered modal-xl">
        <div class="modal-content">
          <div class="modal-body">
            <div class="contents-detail">
              <div class="contents-head">
                <div class="row">
                  <div class="col">
                    <div class="tit">미 연준, 경제 회복 전제로 11월부터 자산 매입 축소 가닥</div>
                    <div class="date">등록 2021-08-17 09:31 / 수정 2021-07-03 13:28</div>
                  </div>
                  <div class="col-auto">
                    <div class="controller">
                      <a href="javascript:;" aria-label="Add"><i class="icon-cart-add"></i></a>
                      <a href="javascript:;" aria-label="Minimization" disabled><i class="icon-minimization"></i></a>
                      <a href="javascript:;" aria-label="Close" data-dismiss="modal"><i class="icon-close"></i></a>
                    </div>
                  </div>
                </div>
              </div>
              <div class="contents-body">
                <div class="overflow-y-scroll">
                  <div style="text-align: center">
                    <img src="content/images/@img-55.png" alt="" />
                  </div>
                  <div style="margin-top: 40px">
                    아스트라제네카(AZ)와 화이자의 코로나19 백신을 교차 접종하면 AZ 백신을 2회 접종한 경우보다 더 높은 중화항체가(항체량
                    측정값)를 나타낸다는 연구 결과가 나왔다. 중화항체는 바이러스 감염을 중화해 예방 효과를 유도하는 항체를 말한다.
                    <br /><br />
                    26일 질병관리청 국립보건연구원 국립감염병연구소는 AZ 백신 동일 접종(동일 백신으로 1·2차 접종), 화이자 백신 동일 접종,
                    AZ(1차)-화이자(2차) 백신 교차 접종의 효과 비교 연구 결과를 발표했다. 연구는 수도권 10개 의료기관 의료인 499명을 대상으로
                    진행됐다. <br /><br />
                    이번 연구에서는 교차 접종군 100명과 아스트라제네카 백신 2회 접종군 199명, 화이자 백신 2회 접종군 200명에 대500해
                    중화항체가, 변이에 대한 중화능(바이러스 무력화 능력), 이상 반응 등을 조사했다. <br /><br />
                    연구 결과 1차 접종 후 중화항체 생성률은 AZ 접종군에서 96%, 화이자 접종군에서 99%였다. 2회 접종 시 동일 백신 접종군과
                    교차 접종군 모두에서 100% 중화항체가 생성됐다. 특히 교차 접종군의 중화항체가는 AZ 접종군에 비해 6배 높았고 화이자 백신
                    접종군과는 유사하게 나타났다. 바이러스를 무력화하는 능력을 말하는 '중화능' 연구에서는, 교차 접종군 및 동일 백신 접종군
                    모두에서 알파 변이 바이러스에 대한 중화능이 감소하지 않았다. 그러나 베타, 감마, 델타 변이에 대한 중화능은 2.5~6배 감소한
                    것으로 나타났다. <br /><br />
                    한편 AZ 백신 접종자의 이상 반응은 2차 접종 시 1차 접종 때보다 적게 발생했다. 반면 화이자 백신 접종자의 이상 반응은 1차
                    접종 때보다 2차 접종 후에 더 많이 발생한 것으로 확인됐다. 국립감염병연구소는 모더나, 얀센, 노바백스 백신에 대해서도 이상
                    반응과 항체 형성에 대한 비교 연구를 진행 중이다. 코로나19 백신 접종자에 대한 추가 접종(부스터 샷) 연구도 진행할
                    계획이라고 밝혔다. <br /><br />
                    장희창 소장은 "국립감염병연구소에서는 국민이 안심하고 예방접종에 참여할 수 있도록 코로나19 백신에 대한 과학적 근거를
                    지속적으로 축적하고 투명하게 안내할 것"이라고 말했다.
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- topick detail layer -->
    <div class="topick-detail-layer">
      <div class="dim"></div>
      <div id="topick-detail-wrap" class="topick-detail-wrap" :class="showTopicDetail ? 'active' : ''">
        <div class="topick-detail-head">
          <div class="btns">
            <a href="#none" class="btn btn-sm btn-round btn-primary">Feed 보기</a>
            <a href="#none" role="button" aria-label="즐겨찾기"><i class="icon-star-white"></i></a>
            <a href="#none" role="button" aria-label="학습제외"><i class="icon-hidden"></i></a>
            <a href="#none" role="button" aria-label="닫기" id="btn-topick-detail-close" @click="closeTopicDetail($event)"
              ><i class="icon-close-white"></i
            ></a>
          </div>
          <div class="tit"><strong>fund</strong>/Hedge_fund_firm</div>
          <div class="info">
            <span>최초 생성일 : <i>2021.06.21</i></span>
            <span>수집 용량 : <i>1.2GB</i></span>
            <span>학습된 컨텐츠 : <i>256개</i></span>
          </div>
        </div>
        <div class="topick-detail-body">
          <div class="overflow-y-scroll">
            <div class="row row-20 align-items-center justify-content-between mb-3">
              <div class="col-auto">
                <h2 class="source-title">Entities</h2>
              </div>
              <div class="col-auto">
                <div class="source-more mt-0"><i class="icon-plus"></i><a href="">더보기</a></div>
              </div>
            </div>
            <div class="card border">
              <div class="card-body">
                <div class="entities">
                  <a href="" class="btn btn-outline-lightgray">American_Hedge_<span class="text-primary">fund</span></a>
                  <a href="" class="btn btn-outline-lightgray">British <span class="text-primary">fund</span>raising</a>
                  <a href="" class="btn btn-outline-lightgray">Germany <span class="text-primary">fund</span>raising</a>
                  <a href="" class="btn btn-outline-lightgray">World Wide <span class="text-primary">Fund</span>_for_Nature</a>
                  <a href="" class="btn btn-outline-lightgray">Spain <span class="text-primary">fund</span>raising</a>
                </div>
              </div>
            </div>

            <div class="row row-20 align-items-center justify-content-between mb-3 m-t-80">
              <div class="col">
                <h2 class="source-title mb-2">Learning Resources</h2>
              </div>
              <div class="col-auto">
                <div class="source-admin">
                  <a href="" class="text-primary"><strong>Training Center &gt;</strong></a>
                  <a href="" aria-label="설정"><i class="icon-gear"></i> 학습소스 관리</a>
                </div>
              </div>
            </div>
            <div class="row row-20 align-items-center justify-content-between mb-3">
              <div class="col">
                <div class="radio-button-type mb-0">
                  <span class="radio-button">
                    <input type="radio" id="fType14" name="fType2" checked />
                    <label for="fType14">사이트 url</label>
                  </span>
                  <span class="radio-button">
                    <input type="radio" id="fType15" name="fType2" />
                    <label for="fType15">문서</label>
                  </span>
                  <span class="radio-button">
                    <input type="radio" id="fType13" name="fType2" />
                    <label for="fType13">소셜</label>
                  </span>
                </div>
              </div>
              <div class="col-auto">
                <div class="source-more mt-0"><i class="icon-plus"></i><a href="">더보기</a></div>
              </div>
            </div>
            <div class="card">
              <div class="card-body">
                <div class="resources row row-20">
                  <div class="col-6">
                    <div class="resource">
                      <div class="row row-20 align-items-center">
                        <div class="col-auto">
                          <img src="content/images/icon-excel-lg.png" alt="" />
                        </div>
                        <div class="col">
                          <div class="tit">
                            <a href=""
                              >유안타증권_미국증시현황_최신.xlsx20210920_키움증권_2022리포트.pptx20210920_키움증권_2022리포트.pptx</a
                            >
                          </div>
                          <div class="info">내 파일 | 84.53M</div>
                        </div>
                      </div>
                      <button type="button" class="btn-del" aria-label="삭제"><i class="icon-resources-del"></i></button>
                    </div>
                  </div>
                  <div class="col-6">
                    <div class="resource">
                      <div class="row row-20 align-items-center">
                        <div class="col-auto">
                          <img src="content/images/icon-ppt.png" alt="" />
                        </div>
                        <div class="col">
                          <div class="tit"><a href="">20210920_키움증권_2022리포트.pptx</a></div>
                          <div class="info">내 파일 | 84.53M</div>
                        </div>
                      </div>
                    </div>
                  </div>
                  <div class="col-6">
                    <div class="resource">
                      <div class="row row-20 align-items-center">
                        <div class="col">
                          <div class="tit"><a href="">주식 갤러리</a></div>
                          <div class="info">https://news.sbs.co.kr > news > newsflash</div>
                        </div>
                      </div>
                    </div>
                  </div>
                  <div class="col-6">
                    <div class="resource">
                      <div class="row row-20 align-items-center">
                        <div class="col">
                          <div class="tit"><a href="">재테크 갤러리 – 주식, 선물, 옵션, 코인, 부동산</a></div>
                          <div class="info">https://news.sbs.co.kr > news > newsflash</div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <div class="row row-20 align-items-center justify-content-between mb-3 m-t-80">
              <div class="col">
                <h2 class="source-title mb-2">Collected Channels</h2>
              </div>
              <div class="col-auto">
                <div class="source-admin">
                  <a href="" aria-label="설정"><i class="icon-gear"></i> 수집채널 관리</a>
                </div>
              </div>
              <div class="col-auto">
                <div class="source-more mt-0"><i class="icon-plus"></i><a href="">더보기</a></div>
              </div>
            </div>
            <div class="card">
              <div class="card-body">
                <div class="resources row row-20">
                  <div class="col-6">
                    <div class="resource">
                      <div class="row row-20 align-items-center">
                        <div class="col">
                          <div class="tit"><a href="" class="text-primary">SBS 뉴스 최신/속보</a></div>
                          <div class="info">내 파일 | 84.53M</div>
                        </div>
                      </div>
                    </div>
                  </div>
                  <div class="col-6">
                    <div class="resource">
                      <div class="row row-20 align-items-center">
                        <div class="col">
                          <div class="tit"><a href="" class="text-primary">부동산 지식iN</a></div>
                          <div class="info">https://news.sbs.co.kr > news > newsflash</div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <div class="row row-20 align-items-center justify-content-between mb-3 m-t-80">
              <div class="col">
                <h2 class="source-title mb-2">History</h2>
              </div>
              <div class="col-auto">
                <div class="source-more mt-0"><i class="icon-plus"></i><a href="../myconnectome/history.html">더보기</a></div>
              </div>
            </div>
            <div class="card">
              <div class="card-body">
                <div class="my-history">
                  <div class="init-date"><span>최초 생성일 : </span> <strong>2021.06.21</strong></div>
                  <div class="row row-20">
                    <div class="col-6">
                      <div class="card-small-template">
                        <strong>학습기록</strong>
                        <div class="overflow-y-scroll" style="max-height: 120px">
                          <ul v-for="n in 5" :key="n">
                            <li class="d-flex align-items-center">
                              <div class="tit">User Feedback 반영</div>
                              <div class="date ml-2">2021.07.30</div>
                            </li>
                          </ul>
                        </div>
                      </div>
                    </div>
                    <div class="col-6">
                      <div class="card-small-template">
                        <strong>수집기록</strong>
                        <div class="overflow-y-scroll" style="max-height: 120px">
                          <ul v-for="n in 5" :key="n">
                            <li class="d-flex align-items-center">
                              <div class="tit"><i class="icon-desc mr-1"></i>User Feedback 반영</div>
                              <div class="date ml-2">2021.07.30</div>
                            </li>
                          </ul>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <div class="row row-20 align-items-center justify-content-between mb-3 m-t-80">
              <div class="col">
                <h2 class="source-title mb-2">Statistics</h2>
              </div>
              <div class="col-auto">
                <div class="source-more mt-0"><i class="icon-plus"></i><a href="../myconnectome/statistics.html">더보기</a></div>
              </div>
            </div>
            <div class="card">
              <div class="card-body">
                <div class="my-history">
                  <div class="init-date"><span>최초 생성일 : </span> <strong>2021.06.21</strong></div>
                  <div class="row row-20">
                    <div class="col-6">
                      <div class="card-small-template">
                        <strong>학습 통계</strong>
                        <div class="text-center">
                          <img src="content/images/@chart-mini.png" alt="" />
                        </div>
                      </div>
                    </div>
                    <div class="col-6">
                      <div class="card-small-template">
                        <strong>수집 통계</strong>
                        <div class="text-center">
                          <img src="content/images/@chart-mini.png" alt="" />
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
    </div>
  </div>
</template>

<script lang="ts" src="./search-connectome.component.ts"></script>

<style>
.customScroll {
  max-height: 200px;
}
</style>
