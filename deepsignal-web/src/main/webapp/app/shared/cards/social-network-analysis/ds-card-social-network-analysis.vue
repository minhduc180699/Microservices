<template>
  <!-- [Contents Social Network Analysis] -->
  <!--  <div class="ds-card card-body-layout2" data-template="4x2">-->
  <!--    <div class="ds-card-header border-bottom">-->
  <!--      <div class="ds-card-title">Social Network</div>-->
  <!--      <div>-->
  <!--        <i class="icon-people"></i> &nbsp; <strong>{{ title }}</strong>-->
  <!--      </div>-->
  <!--      <div class="ds-card-elements"></div>-->
  <!--    </div>-->
  <!--    <div class="ds-card-body" ref="cardChart">-->
  <!--      <div class="chart-container">-->
  <!--        <div-->
  <!--          id="divInfo"-->
  <!--          class="chart-control control-fullname"-->
  <!--          style="display: none; pointer-events: none; border: 2px solid #516eff; background: #f6f8fb; color: black; z-index: 3"-->
  <!--        >-->
  <!--          &lt;!&ndash; <i class="bi bi-file-earmark-text"></i> &ndash;&gt;-->
  <!--        </div>-->
  <!--        <div class="chart-control control-filter" style="z-index: 3">-->
  <!--          <div class="btn-group-vertical">-->
  <!--            <button type="button" class="btn btn btn-outline-lightgray" value="twitter" @click="updateChart('twitter')">-->
  <!--              <i class="bi bi-people-fill"></i>-->
  <!--              <span>Following</span>-->
  <!--            </button>-->
  <!--            <button type="button" class="btn btn btn-outline-lightgray" value="executive" @click="updateChart('executive')">-->
  <!--              <i class="bi bi-building"></i>-->
  <!--              <span>Executives</span>-->
  <!--            </button>-->
  <!--            <button type="button" class="btn btn btn-outline-lightgray" value="d2" @click="updateDepth('d2')">-->
  <!--              <i class="bi bi-node-plus-fill"></i>-->
  <!--              <span>2 Depth</span>-->
  <!--            </button>-->
  <!--          </div>-->
  <!--        </div>-->
  <!--        <div id="social-network-chart"></div>-->
  <!--      </div>-->
  <!--    </div>-->
  <!--  </div>-->

  <div class="col-xl-12">
    <div class="panel-default panel-chart panel-collapse">
      <div class="panel-header">
        <div class="panel-title">
          <strong class="title"> <i class="bi bi-person-fill"></i>{{ title + ' ' }}</strong
          >Social Network
        </div>
        <!--        <div class="panel-elements">-->
        <!--          <button type="button" class="btn btn-light element-item" id="panel-collapse-btn">Analysis</button>-->
        <!--        </div>-->
      </div>
      <div class="panel-body">
        <div class="body-contents">
          <div class="network-chart-container">
            <div id="inforBox" class="chart-control control-fullname" style="display: none; pointer-events: none; z-index: 3">
              <i class="bi bi-file-earmark-text"></i>
              <span id="divInfo"></span>
            </div>
            <!-- <div class="chart-control control-filter">
              <div class="btn-group-vertical">
                <button
                  type="button"
                  class="btn btn btn-outline-lightgray"
                  value="twitter"
                  @click="updateChart('twitter')"
                  v-b-tooltip.hover.left="$t('network-chart.tooltip.twitter-following')"
                >
                  <i class="bi bi-people-fill"></i>
                </button>
                <button
                  type="button"
                  class="btn btn btn-outline-lightgray"
                  value="executive"
                  @click="updateChart('executive')"
                  v-b-tooltip.hover.left="$t('network-chart.tooltip.executive')"
                >
                  <i class="bi bi-building"></i>
                </button>
                <button
                  type="button"
                  class="btn btn btn-outline-lightgray"
                  value="d2"
                  @click="updateDepth('d2')"
                  v-b-tooltip.hover.left="$t('network-chart.tooltip.2-depth')"
                >
                  <i class="bi bi-node-plus-fill"></i>
                </button>
              </div>
            </div> -->
            <div class="chart-area" ref="cardChart" style="min-height: 450px">
              <div id="social-network-chart"></div>
            </div>
          </div>
        </div>
        <div class="body-sidebar">
          <nav>
            <ul class="nav nav-tabs-style2 nav-fill">
              <li class="nav-item">
                <a class="nav-link active" data-toggle="tab" href="#nc-analysis-item01">매기<br />중심 분석</a>
              </li>
              <li class="nav-item">
                <a class="nav-link" data-toggle="tab" href="#analysis-item02">연결<br />중심 분석</a>
              </li>
              <li class="nav-item">
                <a class="nav-link" data-toggle="tab" href="#analysis-item03">근접<br />중심 분석</a>
              </li>
            </ul>
          </nav>
          <div class="tab-content">
            <div class="tab-pane fade show active" id="analysis-item-one">
              <div class="network-analysis">
                <ol class="ranking-list">
                  <li class="list-item">
                    <div class="text">
                      <div class="wrap">
                        <span class="num">1</span>
                        <span class="name">송혜승</span>
                      </div>
                      <div class="wrap">
                        <span class="score">1.00</span>
                      </div>
                    </div>
                    <div class="progress">
                      <div
                        class="progress-bar"
                        role="progressbar"
                        style="width: 100%"
                        aria-valuenow="100"
                        aria-valuemin="0"
                        aria-valuemax="100"
                      ></div>
                    </div>
                  </li>
                  <li class="list-item">
                    <div class="text">
                      <div class="wrap">
                        <span class="num">2</span>
                        <span class="name">한양대학교</span>
                      </div>
                      <div class="wrap">
                        <span class="score">0.90</span>
                      </div>
                    </div>
                    <div class="progress">
                      <div
                        class="progress-bar"
                        role="progressbar"
                        style="width: 90%"
                        aria-valuenow="90"
                        aria-valuemin="0"
                        aria-valuemax="100"
                      ></div>
                    </div>
                  </li>
                  <li class="list-item">
                    <div class="text">
                      <div class="wrap">
                        <span class="num">3</span>
                        <span class="name">조민정</span>
                      </div>
                      <div class="wrap">
                        <span class="score">0.80</span>
                      </div>
                    </div>
                    <div class="progress">
                      <div
                        class="progress-bar"
                        role="progressbar"
                        style="width: 80%"
                        aria-valuenow="80"
                        aria-valuemin="0"
                        aria-valuemax="100"
                      ></div>
                    </div>
                  </li>
                  <li class="list-item">
                    <div class="text">
                      <div class="wrap"><span class="num">4</span> <span class="name">곽은미</span></div>
                      <div class="wrap">
                        <span class="score">0.50</span>
                      </div>
                    </div>
                    <div class="progress">
                      <div
                        class="progress-bar"
                        role="progressbar"
                        style="width: 50%"
                        aria-valuenow="50"
                        aria-valuemin="0"
                        aria-valuemax="100"
                      ></div>
                    </div>
                  </li>
                  <li class="list-item">
                    <div class="text">
                      <div class="wrap">
                        <span class="num">5</span>
                        <span class="name">오정학</span>
                      </div>
                      <div class="wrap">
                        <span class="score">0.30</span>
                      </div>
                    </div>
                    <div class="progress">
                      <div
                        class="progress-bar"
                        role="progressbar"
                        style="width: 30%"
                        aria-valuenow="30"
                        aria-valuemin="0"
                        aria-valuemax="100"
                      ></div>
                    </div>
                  </li>
                </ol>
              </div>
            </div>
            <div class="tab-pane fade" id="analysis-item-two">
              <div class="network-analysis">
                <ol class="ranking-list">
                  <li class="list-item">
                    <div class="text">
                      <div class="wrap">
                        <span class="num">1</span>
                        <span class="name">송혜승</span>
                      </div>
                      <div class="wrap">
                        <span class="score">1.00</span>
                      </div>
                    </div>
                    <div class="progress">
                      <div
                        class="progress-bar"
                        role="progressbar"
                        style="width: 100%"
                        aria-valuenow="100"
                        aria-valuemin="0"
                        aria-valuemax="100"
                      ></div>
                    </div>
                  </li>
                  <li class="list-item">
                    <div class="text">
                      <div class="wrap">
                        <span class="num">2</span>
                        <span class="name">한양대학교</span>
                      </div>
                      <div class="wrap">
                        <span class="score">0.90</span>
                      </div>
                    </div>
                    <div class="progress">
                      <div
                        class="progress-bar"
                        role="progressbar"
                        style="width: 90%"
                        aria-valuenow="90"
                        aria-valuemin="0"
                        aria-valuemax="100"
                      ></div>
                    </div>
                  </li>
                  <li class="list-item">
                    <div class="text">
                      <div class="wrap">
                        <span class="num">3</span>
                        <span class="name">조민정</span>
                      </div>
                      <div class="wrap">
                        <span class="score">0.80</span>
                      </div>
                    </div>
                    <div class="progress">
                      <div
                        class="progress-bar"
                        role="progressbar"
                        style="width: 80%"
                        aria-valuenow="80"
                        aria-valuemin="0"
                        aria-valuemax="100"
                      ></div>
                    </div>
                  </li>
                  <li class="list-item">
                    <div class="text">
                      <div class="wrap"><span class="num">4</span> <span class="name">곽은미</span></div>
                      <div class="wrap">
                        <span class="score">0.50</span>
                      </div>
                    </div>
                    <div class="progress">
                      <div
                        class="progress-bar"
                        role="progressbar"
                        style="width: 50%"
                        aria-valuenow="50"
                        aria-valuemin="0"
                        aria-valuemax="100"
                      ></div>
                    </div>
                  </li>
                  <li class="list-item">
                    <div class="text">
                      <div class="wrap">
                        <span class="num">5</span>
                        <span class="name">오정학</span>
                      </div>
                      <div class="wrap">
                        <span class="score">0.30</span>
                      </div>
                    </div>
                    <div class="progress">
                      <div
                        class="progress-bar"
                        role="progressbar"
                        style="width: 30%"
                        aria-valuenow="30"
                        aria-valuemin="0"
                        aria-valuemax="100"
                      ></div>
                    </div>
                  </li>
                </ol>
              </div>
            </div>
            <div class="tab-pane fade" id="analysis-item-three">
              <div class="network-analysis">
                <ol class="ranking-list">
                  <li class="list-item">
                    <div class="text">
                      <div class="wrap">
                        <span class="num">1</span>
                        <span class="name">송혜승</span>
                      </div>
                      <div class="wrap">
                        <span class="score">1.00</span>
                      </div>
                    </div>
                    <div class="progress">
                      <div
                        class="progress-bar"
                        role="progressbar"
                        style="width: 100%"
                        aria-valuenow="100"
                        aria-valuemin="0"
                        aria-valuemax="100"
                      ></div>
                    </div>
                  </li>
                  <li class="list-item">
                    <div class="text">
                      <div class="wrap">
                        <span class="num">2</span>
                        <span class="name">한양대학교</span>
                      </div>
                      <div class="wrap">
                        <span class="score">0.90</span>
                      </div>
                    </div>
                    <div class="progress">
                      <div
                        class="progress-bar"
                        role="progressbar"
                        style="width: 90%"
                        aria-valuenow="90"
                        aria-valuemin="0"
                        aria-valuemax="100"
                      ></div>
                    </div>
                  </li>
                  <li class="list-item">
                    <div class="text">
                      <div class="wrap">
                        <span class="num">3</span>
                        <span class="name">조민정</span>
                      </div>
                      <div class="wrap">
                        <span class="score">0.80</span>
                      </div>
                    </div>
                    <div class="progress">
                      <div
                        class="progress-bar"
                        role="progressbar"
                        style="width: 80%"
                        aria-valuenow="80"
                        aria-valuemin="0"
                        aria-valuemax="100"
                      ></div>
                    </div>
                  </li>
                  <li class="list-item">
                    <div class="text">
                      <div class="wrap"><span class="num">4</span> <span class="name">곽은미</span></div>
                      <div class="wrap">
                        <span class="score">0.50</span>
                      </div>
                    </div>
                    <div class="progress">
                      <div
                        class="progress-bar"
                        role="progressbar"
                        style="width: 50%"
                        aria-valuenow="50"
                        aria-valuemin="0"
                        aria-valuemax="100"
                      ></div>
                    </div>
                  </li>
                  <li class="list-item">
                    <div class="text">
                      <div class="wrap">
                        <span class="num">5</span>
                        <span class="name">오정학</span>
                      </div>
                      <div class="wrap">
                        <span class="score">0.30</span>
                      </div>
                    </div>
                    <div class="progress">
                      <div
                        class="progress-bar"
                        role="progressbar"
                        style="width: 30%"
                        aria-valuenow="30"
                        aria-valuemin="0"
                        aria-valuemax="100"
                      ></div>
                    </div>
                  </li>
                </ol>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script lang="ts" src="./ds-card-social-network-analysis.component.ts"></script>
