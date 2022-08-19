<template>
  <div class="container">
    <div>
      <h1 class="page-title">1:1 문의</h1>
      <div class="form-area">
        <div class="form-wrap">
          <div class="row">
            <div class="col-auto">
              <h2>문의 정보</h2>
              <p>문의 내용을 작상해주세요.</p>
            </div>
            <div class="col">
              <div class="form-group">
                <label>문의 유형</label>
                <div class="radio-button-type">
                  <span v-for="(cate, index) in categories" :key="index" class="radio-button">
                    <input
                      v-model="formModel.category.code"
                      type="radio"
                      :id="'fType' + (index + 1)"
                      name="fType"
                      :value="cate.category.code"
                    />
                    <label :for="'fType' + (index + 1)">{{ cate.category.code }}</label>
                  </span>
                  <!--                  <select name="cate" id="cate" v-model="formModel.category.code">-->
                  <!--                    <option v-for="(cate, index) in categories" :key="index" :value="cate.category.code">{{cate.category.code}}</option>-->
                  <!--                  </select>-->
                </div>
              </div>
              <div class="form-group">
                <label for="fTitle">문의 제목 <i class="text-danger">*</i></label>
                <div class="group">
                  <input
                    type="text"
                    class="form-control"
                    id="fTitle"
                    required
                    v-model="$v.formModel.title.$model"
                    :class="{ 'is-invalid': $v.formModel.title.$error }"
                  />
                  <div class="invalid-feedback">
                    <span v-if="!$v.formModel.title.required"> Title is required!</span>
                  </div>
                </div>
              </div>
              <div class="form-group">
                <label for="fContent">문의 내용 <i class="text-danger">*</i></label>
                <textarea
                  class="form-control"
                  id="fContent"
                  required
                  v-model="$v.formModel.content.$model"
                  :class="{ 'is-invalid': $v.formModel.content.$error }"
                ></textarea>
                <div class="invalid-feedback">
                  <span v-if="!$v.formModel.content.required"> Content is required!</span>
                  <span v-if="!$v.formModel.content.maxLength"> Content must less than 4 characters!</span>
                </div>
              </div>
              <div class="form-group">
                <label for="fFile">파일 첨부</label>
                <button type="button" id="fFile" class="btn btn-lg btn-outline-lightgray h-50px btn-block" @click="onClick()">
                  + 파일추가<span v-if="formModel.file"> ({{ formModel.file }})</span>
                </button>
                <input type="file" id="fileUpload" class="d-none" accept=".gif,.jpg,.pdf,.docs,.zip" @change="uploadImage($event)" />
                <!--                <img :src="previewImage">-->
              </div>
              <div class="vol-max row align-items-center">
                <div class="col">
                  <!--                  <progress value="0" max="100" style="width: 500px"></progress>-->
                  <div class="progress">
                    <div
                      class="progress-bar"
                      role="progressbar"
                      :style="{ width: progress + '%' }"
                      aria-valuenow="25"
                      aria-valuemin="0"
                      aria-valuemax="100"
                    ></div>
                  </div>
                </div>
                <div class="col-auto">
                  <div class="vol">
                    <strong :style="[sizeFile > 10 ? { color: '#f25767' } : '']">{{ sizeFile }}mb</strong> / 10mb
                  </div>
                  <div class="invalid-feedback">
                    <span v-if="sizeFile > 10"> File exceeds the allowed size!</span>
                  </div>
                </div>
              </div>
              <div class="text-tip">
                <p>- 첨부한 파일의 전체 크기는 10Mbyte 미만이어야 합니다.</p>
                <p>- 용량이 초과될 경우, 문의 접수가 원활하게 진행되지 않을 수 있습니다.</p>
                <p>- 파일 첨부는 Jpg, Gif, PDF, MS Office, 아래한글만 가능합니다.</p>
              </div>
            </div>
          </div>
        </div>
        <div class="form-wrap">
          <div class="row">
            <div class="col-auto">
              <h2>사용자 정보</h2>
              <p>해당 문의에 대한 답변은 이메일로 받아볼 수 있습니다.</p>
            </div>
            <div class="col">
              <div class="form-group">
                <label for="fName">이름 <i class="text-danger">*</i></label>
                <div class="group">
                  <input
                    type="text"
                    class="form-control"
                    id="fName"
                    required
                    v-model="$v.formModel.name.$model"
                    :class="{ 'is-invalid': $v.formModel.name.$error }"
                  />
                  <div class="invalid-feedback">
                    <span v-if="!$v.formModel.name.required"> Name is required!</span>
                  </div>
                </div>
              </div>
              <div class="form-group">
                <label for="fEmail">답변 받을 이메일 <i class="text-danger">*</i></label>
                <div class="group">
                  <input
                    type="text"
                    class="form-control"
                    id="fEmail"
                    required
                    v-model="$v.formModel.email.$model"
                    :class="{ 'is-invalid': $v.formModel.email.$error }"
                  />
                  <div class="invalid-feedback">
                    <span v-if="!$v.formModel.email.required"> Email is required!</span>
                    <span v-if="!$v.formModel.email.email"> Email is invalid!</span>
                  </div>
                </div>
              </div>
              <div class="form-group">
                <div class="row justify-content-between align-items-center m-b-10">
                  <div class="col-auto">
                    <div class="custom-control custom-checkbox">
                      <input type="checkbox" class="custom-control-input" id="agree1" v-model="formModel.isPublic" />
                      <label class="custom-control-label text-666" for="agree1">개인정보 수집 및 이용에 동의합니다.</label>
                    </div>
                  </div>
                  <div class="col-auto">
                    <button type="button" class="btn btn-outline-lightgray">자세히보기</button>
                  </div>
                </div>
                <table class="table table-bordered">
                  <thead>
                    <tr>
                      <th class="text-center">수집 및 이용 목적</th>
                      <th class="text-center">수집 항목</th>
                      <th class="text-center">보유 및 이용 기간</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr>
                      <td class="text-center py-5">문의 확인 및 처리 결과 회신</td>
                      <td class="text-center py-5">이름, 이메일</td>
                      <td class="text-center py-5">수집 후 3년간 보관 후 파기</td>
                    </tr>
                  </tbody>
                </table>
              </div>
              <div class="text-tip">
                <p>- 개인정보 수집 및 이용 동의를 거부할 권리가 있습니다. 다만, 동의를 거부할 경우 문의 처리 및 결과 회신이 제한됩니다.</p>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="d-flex justify-content-center m-t-45">
        <button type="button" class="btn btn-lg btn-dark h-50px min-width-186px">취소</button>
        <button
          @click="submit()"
          type="button"
          class="btn btn-lg btn-primary h-50px min-width-186px m-l-10"
          data-target="#modalQna"
          data-toggle="modal"
        >
          문의 접수
        </button>
      </div>
      <div class="d-flex justify-content-center m-t-20">
        <p v-if="formCorrect" style="color: #f25767">Please fill the form correctly!</p>
      </div>
      <div class="qna-info">
        <div class="row">
          <div class="col">
            <h3>문의 응대</h3>
            <div class="text-tip">
              <p>- 평일 오전 9시 ~ 오후 6시</p>
              <p>- 우선 접수된 문의 건부터 순차적으로 상담 진행합니다.</p>
              <p>- 문의 신청 후 답변 완료 시까지 평균 2영업일이 소요되며, 문의량 증가 시 지연될 수 있습니다.</p>
            </div>
          </div>
          <div class="col">
            <h3>문의내역 확인</h3>
            <div class="text-tip">
              <p>- 기재하신 ‘답변 받을 이메일’ 에서 답변 내용을 확인 할 수 있습니다.</p>
              <p>
                - 로그인시 <span class="text-primary">Deep Signal 홈페이지 &gt; 사용자계정 &gt; 문의내역</span>에서 확인 할 수 있습니다.
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div id="footer-block">
      <footer>
        <div class="footer-info">
          <div>(주)솔트룩스 사업장 소재지: 서울시 강남구 언주로 538(역삼동 689-4)대웅빌딩 3층~5층[06147, (구)135-917]</div>
          <div>고객센터: 02.2193.1600 | 팩스: 02.3402.0082 | 이메일: marketing@saltlux.com</div>
          <div>대표이사 : 이경일 | 사업자등록번호 : 102-81-13061</div>
          <div>Copyright ⓒ Saltlux. All Rights Reserved.</div>
        </div>
        <div class="familysites">
          <select name="" id="" class="custom-select">
            <option value="">Family Site</option>
          </select>
        </div>
        <div class="footer-sns">
          <ul>
            <li>
              <a href=""><img src="content/images/sns-facebook.png" alt="facebook" /></a>
            </li>
            <li>
              <a href=""><img src="content/images/sns-instagram.png" alt="instagram" /></a>
            </li>
            <li>
              <a href=""><img src="content/images/sns-youtube.png" alt="youtube" /></a>
            </li>
          </ul>
        </div>
      </footer>
    </div>
  </div>
</template>

<script lang="ts" src="./qna.component.ts"></script>
