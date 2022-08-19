<template>
  <!-- [Ranking] -->
  <div class="card">
    <div class="card-header">
      <div class="badge">Ranking</div>
    </div>
    <div class="card-body">
      <a href="">
        <div class="tit">테크기업인 '경제계 新주류' 부상</div>
        <div class="desc">
          신개념 기업인, 그들은 누구인가, 이해진·김범수·김택진·김봉진 등 외환 위기 전후 창업세대 디지털 시대 성공신화 쓰며 영향력
          확대하였으며 차세대 경제단체에 입성하였다. 신개념 기업인, 그들은 누구인가, 이해진·김범수·김택진·김봉진 등 외환 위기 전후 창업세대
          디지털 시대 성공신화 쓰며 영향력 확대하였으며 차세대 경제단체에 입성하였다. 대표 IT 포털 사이트인 네이버부터 카카오까지...
        </div>
      </a>
      <div class="grid">
        <div class="row row-10">
          <div class="col-auto first">
            <a href="" class="box">
              <div class="d-flex flex-column justify-content-between">
                <i class="num">1</i>
                <div>
                  <div class="name">이해진</div>
                  <div class="job">기업인</div>
                </div>
              </div>
              <div class="img">
                <img src="content/images/@people-img1.png" alt="" />
              </div>
            </a>
          </div>
          <div class="col">
            <div class="row row-10">
              <div class="col">
                <a href="" class="box">
                  <div class="d-flex flex-column justify-content-between">
                    <i class="num">2</i>
                    <div>
                      <div class="name">김범수</div>
                      <div class="job">기업인</div>
                    </div>
                  </div>
                  <div class="img">
                    <img src="content/images/@people-img1.png" alt="" />
                  </div>
                </a>
              </div>
              <div class="col">
                <a href="" class="box">
                  <div class="d-flex flex-column justify-content-between">
                    <i class="num">3</i>
                    <div>
                      <div class="name">김택진</div>
                      <div class="job">기업인</div>
                    </div>
                  </div>
                  <div class="img">
                    <img src="content/images/@people-img1.png" alt="" />
                  </div>
                </a>
              </div>
            </div>
            <div class="row row-10 m-t-10">
              <div class="col">
                <a href="" class="box">
                  <div class="d-flex flex-column justify-content-between">
                    <i class="num">4</i>
                    <div>
                      <div class="name">김정주</div>
                      <div class="job">기업인</div>
                    </div>
                  </div>
                  <div class="img">
                    <img src="content/images/@people-img1.png" alt="" />
                  </div>
                </a>
              </div>
              <div class="col">
                <a href="" class="box">
                  <div class="d-flex flex-column justify-content-between">
                    <i class="num">5</i>
                    <div>
                      <div class="name">김봉진</div>
                      <div class="job">기업인</div>
                    </div>
                  </div>
                  <div class="img">
                    <img src="content/images/@people-img1.png" alt="" />
                  </div>
                </a>
              </div>
            </div>
          </div>
        </div>
      </div>
      <a href="" class="btn btn-lightgray">More People +</a>
    </div>
    <ds-card-footer :item="item" />
  </div>
</template>
<script lang="ts" src="./ds-card-ranking.component.ts"></script>
