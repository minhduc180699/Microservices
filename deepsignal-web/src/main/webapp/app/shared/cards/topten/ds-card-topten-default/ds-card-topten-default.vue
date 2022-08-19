<template>
  <!-- [Top 10] -->
  <div class="ds-card">
    <div class="ds-card-header">
      <div class="ds-card-title">This is card top ten default</div>
    </div>
    <div class="ds-card-body">
      <div class="tab-custom tab-item-2">
        <b-tabs pills justified>
          <b-tab title="일간" active>
            <ol class="ranking-basic">
              <li>
                <span class="rank-num">1</span>
                <strong><a href="#">Samsung</a></strong>
                <div class="rank-changes rank-same">
                  <span class="change-icon"></span>
                </div>
              </li>
              <li>
                <span class="rank-num">2</span>
                <strong><a href="#">Hyundai Hyundai Hyundai</a></strong>
                <div class="rank-changes rank-same">
                  <span class="change-icon"></span>
                </div>
              </li>
              <li>
                <span class="rank-num">3</span>
                <strong><a href="#">SK</a></strong>
                <div class="rank-changes rank-up">
                  <span class="change-icon"></span>
                  <span class="change-num">10</span>
                </div>
              </li>
              <li>
                <span class="rank-num">4</span>
                <strong><a href="#">LG</a></strong>
                <div class="rank-changes rank-up">
                  <span class="change-icon"></span>
                  <span class="change-num">10</span>
                </div>
              </li>
              <li>
                <span class="rank-num">5</span>
                <strong><a href="#">Lotte</a></strong>
                <div class="rank-changes rank-up">
                  <span class="change-icon"></span>
                  <span class="change-num">10</span>
                </div>
              </li>
              <li>
                <span class="rank-num">6</span>
                <strong><a href="#">Posco</a></strong>
                <div class="rank-changes rank-up">
                  <span class="change-icon"></span>
                  <span class="change-num">10</span>
                </div>
              </li>
              <li>
                <span class="rank-num">7</span>
                <strong><a href="#">Hanhwa</a></strong>
                <div class="rank-changes rank-down">
                  <span class="change-icon"></span>
                  <span class="change-num">10</span>
                </div>
              </li>
              <li>
                <span class="rank-num">8</span>
                <strong><a href="#">GS</a></strong>
                <div class="rank-changes rank-down">
                  <span class="change-icon"></span>
                  <span class="change-num">10</span>
                </div>
              </li>
              <li>
                <span class="rank-num">9</span>
                <strong><a href="#">Kakao</a></strong>
                <div class="rank-changes rank-down">
                  <span class="change-icon"></span>
                  <span class="change-num">10</span>
                </div>
              </li>
              <li>
                <span class="rank-num">10</span>
                <strong><a href="#">NHN</a></strong>
                <div class="rank-changes rank-down">
                  <span class="change-icon"></span>
                  <span class="change-num">10</span>
                </div>
              </li>
            </ol>
            <p class="rank-date">2021.06.17 기준</p>
          </b-tab>
          <b-tab title="주간">
            <ol class="ranking-basic">
              <li>
                <span class="rank-num">11</span>
                <strong><a href="#">Samsung</a></strong>
                <div class="rank-changes rank-down">
                  <span class="change-icon"></span>
                  <span class="change-num">10</span>
                </div>
              </li>
              <li>
                <span class="rank-num">12</span>
                <strong><a href="#">Hyundai</a></strong>
                <div class="rank-changes rank-up">
                  <span class="change-icon"></span>
                  <span class="change-num">10</span>
                </div>
              </li>
              <li>
                <span class="rank-num">13</span>
                <strong><a href="#">SK</a></strong>
                <div class="rank-changes rank-same">
                  <span class="change-icon"></span>
                </div>
              </li>
              <li>
                <span class="rank-num">14</span>
                <strong><a href="#">LG</a></strong>
                <div class="rank-changes rank-up">
                  <span class="change-icon"></span>
                  <span class="change-num">10</span>
                </div>
              </li>
              <li>
                <span class="rank-num">15</span>
                <strong><a href="#">Lotte</a></strong>
                <div class="rank-changes rank-down">
                  <span class="change-icon"></span>
                  <span class="change-num">10</span>
                </div>
              </li>
              <li>
                <span class="rank-num">16</span>
                <strong><a href="#">Posco</a></strong>
                <div class="rank-changes rank-down">
                  <span class="change-icon"></span>
                  <span class="change-num">10</span>
                </div>
              </li>
              <li>
                <span class="rank-num">17</span>
                <strong><a href="#">Hanhwa</a></strong>
                <div class="rank-changes rank-down">
                  <span class="change-icon"></span>
                  <span class="change-num">10</span>
                </div>
              </li>
              <li>
                <span class="rank-num">18</span>
                <strong><a href="#">GS</a></strong>
                <div class="rank-changes rank-up">
                  <span class="change-icon"></span>
                  <span class="change-num">10</span>
                </div>
              </li>
              <li>
                <span class="rank-num">19</span>
                <strong><a href="#">Kakao</a></strong>
                <div class="rank-changes rank-up">
                  <span class="change-icon"></span>
                  <span class="change-num">10</span>
                </div>
              </li>
              <li>
                <span class="rank-num">20</span>
                <strong><a href="#">NHN</a></strong>
                <div class="rank-changes rank-same">
                  <span class="change-icon"></span>
                </div>
              </li>
            </ol>
            <p class="rank-date">2021.06.17 기준</p>
          </b-tab>
        </b-tabs>
      </div>
    </div>
    <ds-card-footer :item="item" />
  </div>
</template>
<script lang="ts" src="./ds-card-topten-default.component.ts"></script>
