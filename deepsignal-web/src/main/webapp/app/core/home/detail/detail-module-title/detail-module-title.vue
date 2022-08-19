<template>
  <article class="article">
    <section>
      <div class="detail-module" data-detail-module="analysis">
        <h1 class="detail-module-title">Analysis</h1>
        <div class="analysis row">
          <div class="col-4">
            <div class="box">
              <h2>Contents Reliability</h2>
              <p class="desc">출처, 주목도, 콘텐츠 작성자등을 분석하여 콘텐츠의 신뢰도를 나타냅니다.</p>
              <div class="num">
                <div class="d-flex align-items-center justify-content-end">
                  <i class="icon-bar-chart"></i>
                  <strong class="text-primary">96</strong>
                  <span>/100</span>
                </div>
              </div>
            </div>
          </div>
          <div class="col-4">
            <div class="box">
              <h2>Positive Negative Index</h2>
              <p class="desc">콘텐츠 내 단어들을 분석하여 긍부정 지수를 나타냅니다.</p>
              <div class="num">
                <div class="d-flex align-items-center justify-content-end">
                  <i class="icon-smile"></i>
                  <strong class="text-success">81</strong>
                  <span>/100</span>
                </div>
              </div>
            </div>
          </div>
          <div class="col-4">
            <div class="box">
              <h2>Contents Reliability</h2>
              <p class="desc">나의 커넥톰이 학습한 관심사를 바탕으로 중요도 점수를 나타냅니다.</p>
              <div class="num">
                <div class="d-flex align-items-center justify-content-end">
                  <i class="icon-star"></i>
                  <strong class="text-danger">45</strong>
                  <span>/100</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
    <section>
      <div class="detail-module" data-detail-module="related-contents">
        <h1 class="detail-module-title">{{ $t('signal-tracking.related-contents') }}</h1>
        <div class="module-box">
          <div class="item-list">
            <div class="item" data-template="stock" data-size="1x1">
              <!-- [Stock] -->
              <div class="card">
                <div class="card-header">
                  <div class="card-title">Kospi</div>
                </div>
                <div class="card-body">
                  <div class="title">
                    <span>2,602.90</span>
                    <span class="text-primary ml-3">49.09(-1.92%)</span>
                  </div>
                  <div class="graph mt-6">
                    <img src="content/images/@card-graph.png" alt="" />
                  </div>
                </div>
              </div>
              <button type="button" aria-label="유용하지 않아요" class="btn-close"><i class="icon-x"></i></button>
            </div>
            <div class="item" data-template="wheather" data-size="1x1">
              <!-- [Wheather] -->
              <div class="card">
                <div class="card-header">
                  <div class="card-title">Wheather</div>
                </div>
                <div class="card-body">
                  <div class="d-flex justify-content-between">
                    <div class="temperature">
                      <strong>17</strong><span>°</span>
                      <p>서울, 맑음</p>
                    </div>
                    <div class="wheather-img">
                      <img src="content/images/icon-sun.png" alt="" />
                    </div>
                  </div>
                  <div class="wheather-desc">
                    <div>최고 <span>18°</span> / 최저 <span>2°</span></div>
                    <div>미세 좋음, 초미세 좋음</div>
                  </div>
                </div>
              </div>
              <button type="button" aria-label="유용하지 않아요" class="btn-close"><i class="icon-x"></i></button>
            </div>
            <div class="item" data-template="top-10" data-size="1x2">
              <!-- [Top 10] -->
              <div class="card">
                <div class="card-header">
                  <div class="card-title">ESG Consensus Top10</div>
                </div>
                <div class="card-body">
                  <ol class="top">
                    <li>
                      <span>1</span>
                      <strong>Samsung</strong>
                      <p>7.71</p>
                    </li>
                    <li>
                      <span>2</span>
                      <strong>Hyundai</strong>
                      <p>7.55</p>
                    </li>
                    <li>
                      <span>2</span>
                      <strong>SK</strong>
                      <p>7.54</p>
                    </li>
                    <li>
                      <span>2</span>
                      <strong>LG</strong>
                      <p>7.50</p>
                    </li>
                    <li>
                      <span>2</span>
                      <strong>Lotte</strong>
                      <p>6.87</p>
                    </li>
                    <li>
                      <span>2</span>
                      <strong>Posco</strong>
                      <p>6.34</p>
                    </li>
                    <li>
                      <span>2</span>
                      <strong>Hanhwa</strong>
                      <p>6.12</p>
                    </li>
                    <li>
                      <span>2</span>
                      <strong>GS</strong>
                      <p>5.83</p>
                    </li>
                    <li>
                      <span>2</span>
                      <strong>Kakao</strong>
                      <p>5.54</p>
                    </li>
                    <li>
                      <span>2</span>
                      <strong>NHN</strong>
                      <p>5.42</p>
                    </li>
                  </ol>
                </div>
                <div class="card-footer">
                  <div>
                    <button type="button" aria-label="like" aria-pressed="false"><i class="icon-like"></i></button>
                    <button type="button" aria-label="boormark"><i class="icon-bookmark"></i></button>
                  </div>
                  <div>
                    <button type="button" aria-label="more"><i class="icon-more"></i></button>
                  </div>
                </div>
              </div>
              <button type="button" aria-label="유용하지 않아요" class="btn-close"><i class="icon-x"></i></button>
            </div>
            <div class="item" data-template="stock" data-size="2x1">
              <!-- [Stock] -->
              <div class="card">
                <div class="card-header">
                  <div class="card-title">Stocks : Apple inc.</div>
                </div>
                <div class="card-body">
                  <div class="title">
                    <span>AAPL 129.16</span>
                    <span class="text-primary ml-3">8:00 PM EDT +1.87(+1.69%)</span>
                  </div>
                  <div class="graph mt-6">
                    <img src="content/images/@card-graph-lg.png" alt="" />
                  </div>
                </div>
              </div>
              <button type="button" aria-label="유용하지 않아요" class="btn-close"><i class="icon-x"></i></button>
            </div>
          </div>
          <div class="module-more">
            <a href="#none" class="btn btn-lg btn-outline-lightgray btn-block" v-text="$t('feed.detail.see-more')">더보기</a>
          </div>
        </div>
      </div>
    </section>
    <section>
      <div class="detail-module" data-detail-module="related-people-company">
        <h1 class="detail-module-title">Related People&amp;Company</h1>
        <div class="module-box">
          <!-- <div class="item-list">

          </div>
          <div class="module-more">
              <a href="#none" class="btn btn-lg btn-outline-lightgray btn-block">더보기</a>
          </div> -->
        </div>
      </div>
    </section>
    <section>
      <div class="detail-module" data-detail-module="document">
        <h1 class="detail-module-title">AI 딥러닝 성공사례_4월 1주차 주간 세미나</h1>
        <div class="detail-module-date">인공지능 발표자료 PDF / 2021-03-16 00:00 업로드</div>
        <div class="module-box p-0">
          <div class="document-area">
            <div class="swiper-container">
              <div class="swiper-wrapper">
                <div class="swiper-slide">
                  <div class="doc"><img src="content/images/@doc.png" alt="" /></div>
                </div>
                <div class="swiper-slide">
                  <div class="doc"><img src="content/images/@doc2.png" alt="" /></div>
                </div>
                <div class="swiper-slide">
                  <div class="doc"><img src="content/images/@doc.png" alt="" /></div>
                </div>
              </div>
              <div class="swiper-pagination"></div>
              <div class="swiper-button-prev"></div>
              <div class="swiper-button-next"></div>
              <div class="page">1/1</div>
            </div>
          </div>
        </div>
      </div>
    </section>
    <section>
      <div class="detail-module" data-detail-module="graph">
        <h1 class="detail-module-title">코로나 바이러스 19</h1>
        <div class="detail-module-date">질병관리청 / 2021-03-16 00:00 집계기준</div>
        <div class="module-box p-0 bg-transparent">
          <div class="graph-area">
            <div class="graph-head">
              <div class="d-flex align-items-center justify-content-between">
                <strong>월별 신규 확진자수</strong>
                <div class="func">
                  <a href="" aria-label="설정"><i class="icon-gear-gray"></i></a>
                  <a href="" aria-label="전체화면"><i class="icon-fullscreen-gray"></i></a>
                </div>
              </div>
            </div>
            <div class="graph-body">
              <div class="option">
                <select name="" id="" class="custom-select d-inline-block w-auto border-0">
                  <option value="">차트설정</option>
                </select>
                <select name="" id="" class="custom-select d-inline-block w-auto border-0">
                  <option value="">지표</option>
                </select>
              </div>
              <div class="text-center">
                <img src="content/images/@chart-module.png" alt="" />
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
  </article>
</template>
<script lang="ts" src="./detail-module-title.component.ts"></script>
