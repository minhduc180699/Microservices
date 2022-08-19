<template>
  <!-- [survey] -->
  <div class="ds-card">
    <div class="ds-card-header">
      <div class="ds-card-title">인기 검색 종목</div>
    </div>
    <div class="ds-card-body">
      <table class="table-finance">
        <colgroup>
          <col style="width: auto" />
          <col style="width: 60px" />
          <col style="width: 70px" />
        </colgroup>
        <tbody>
          <tr class="up">
            <th scope="row"><span class="rank-num">1</span>말줄임 테스트입니다.</th>
            <td>70,300</td>
            <td>
              <div class="rank-changes rank-up">
                <span class="change-icon"></span>
                <span class="change-num">20.64</span>
              </div>
            </td>
          </tr>
          <tr class="down">
            <th scope="row"><span class="rank-num">2</span>삼성전자</th>
            <td>70,300</td>
            <td>
              <div class="rank-changes rank-down">
                <span class="change-icon"></span>
                <span class="change-num">20.64</span>
              </div>
            </td>
          </tr>
          <tr class="same">
            <th scope="row"><span class="rank-num">3</span>삼성전자</th>
            <td>70,300</td>
            <td>
              <div class="rank-changes rank-same">
                <span class="change-icon"></span>
              </div>
            </td>
          </tr>
          <tr class="up">
            <th scope="row"><span class="rank-num">4</span>삼성전자</th>
            <td>70,300</td>
            <td>
              <div class="rank-changes rank-up">
                <span class="change-icon"></span>
                <span class="change-num">20.64</span>
              </div>
            </td>
          </tr>
          <tr class="down">
            <th scope="row"><span class="rank-num">5</span>삼성전자</th>
            <td>70,300</td>
            <td>
              <div class="rank-changes rank-down">
                <span class="change-icon"></span>
                <span class="change-num">20.64</span>
              </div>
            </td>
          </tr>
          <tr class="same">
            <th scope="row"><span class="rank-num">6</span>삼성전자</th>
            <td>70,300</td>
            <td>
              <div class="rank-changes rank-same">
                <span class="change-icon"></span>
              </div>
            </td>
          </tr>
          <tr class="up">
            <th scope="row"><span class="rank-num">7</span>삼성전자</th>
            <td>70,300</td>
            <td>
              <div class="rank-changes rank-up">
                <span class="change-icon"></span>
                <span class="change-num">20.64</span>
              </div>
            </td>
          </tr>
          <tr class="down">
            <th scope="row"><span class="rank-num">8</span>삼성전자</th>
            <td>70,300</td>
            <td>
              <div class="rank-changes rank-down">
                <span class="change-icon"></span>
                <span class="change-num">20.64</span>
              </div>
            </td>
          </tr>
          <tr class="same">
            <th scope="row"><span class="rank-num">9</span>삼성전자</th>
            <td>70,300</td>
            <td>
              <div class="rank-changes rank-same">
                <span class="change-icon"></span>
              </div>
            </td>
          </tr>
          <tr class="same">
            <th scope="row"><span class="rank-num">10</span>삼성전자</th>
            <td>70,300</td>
            <td>
              <div class="rank-changes rank-same">
                <span class="change-icon"></span>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <ds-card-footer :item="item" />
  </div>
</template>
<script lang="ts" src="./ds-card-survey.component.ts"></script>
