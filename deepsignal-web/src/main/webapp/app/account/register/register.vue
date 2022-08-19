<template>
  <div>
    <div id="container-block">
      <div class="container">
        <div class="sign-wrap">
          <div class="form-wrap form-signup">
            <div class="form-header">
              <ol class="nav nav-step">
                <li class="nav-item">
                  <a
                    class="nav-link"
                    @click="skipStep(1)"
                    :class="{ active: step == 1 }"
                    id="signup-step1-tab"
                    aria-controls="signup-step1"
                    aria-selected="true"
                    >1<span class="sr-only">Agree to terms and conditions</span></a
                  >
                </li>
                <li class="nav-item">
                  <a
                    class="nav-link"
                    @click="skipStep(2)"
                    :class="{ active: step == 2 }"
                    id="signup-step2-tab"
                    aria-controls="signup-step2"
                    aria-selected="true"
                    >2<span class="sr-only">본인인증</span></a
                  >
                </li>
                <li class="nav-item">
                  <a
                    class="nav-link"
                    @click="skipStep(3)"
                    :class="{ active: step == 3 }"
                    id="signup-step3-tab"
                    aria-controls="signup-step3"
                    aria-selected="true"
                    :disabled="step < 3"
                    >3<span class="sr-only">가입완료</span></a
                  >
                </li>
              </ol>
            </div>
            <div class="form-body">
              <div class="tab-content">
                <div class="tab-pane" :class="{ active: step == 1 }">
                  <div class="pain-inner step-wrap">
                    <div class="guide">
                      <strong>DeepSignal</strong>
                      <p v-html="$t('register.desc')">
                        세상의 모든 정보와 의미를 재연결하여 <br />
                        사용자에게 통찰을 제공하는 <br />
                        증강지능 서비스
                      </p>
                    </div>
                    <div class="agree-item agree-item-all">
                      <div class="custom-control custom-checkbox">
                        <input type="checkbox" class="custom-control-input" id="agreeAll" @click="toggleAll" v-model="isSelectAll" />
                        <label class="custom-control-label" for="agreeAll" v-text="$t('register.terms.desc')"
                          >DeepSignal 서비스 이용약관, 개인정보 수집 및 이용약관, 뉴스레터 및 메시지 수신(선택)에 모두 동의합니다.</label
                        >
                      </div>
                    </div>
                    <div class="agree-item">
                      <div class="custom-control custom-checkbox mr-2">
                        <input
                          type="checkbox"
                          class="custom-control-input"
                          id="agree1"
                          value="option1"
                          @change="toggleSelect"
                          v-model="policies"
                          required
                        />
                        <label class="custom-control-label" for="agree1" v-text="$t('register.terms.service')"
                          >서비스 이용약관 (필수)</label
                        >
                      </div>
                      <button
                        type="button"
                        class="btn btn-outline-lightgray btn-sm"
                        v-text="$t('register.terms.read')"
                        @click="$bvModal.show('termOfUse')"
                      >
                        보기
                      </button>
                    </div>
                    <div class="agree-item">
                      <div class="custom-control custom-checkbox mr-2">
                        <input
                          type="checkbox"
                          class="custom-control-input"
                          id="agree2"
                          value="option2"
                          @change="toggleSelect"
                          v-model="policies"
                          required
                        />
                        <label class="custom-control-label" for="agree2" v-text="$t('register.terms.personal')"
                          >개인정보 수집 및 이용약관 (필수)</label
                        >
                      </div>
                      <button type="button" class="btn btn-outline-lightgray btn-sm" v-text="$t('register.terms.read')">보기</button>
                    </div>
                    <div class="agree-item">
                      <div class="custom-control custom-checkbox mr-2">
                        <input
                          type="checkbox"
                          class="custom-control-input"
                          id="agree3"
                          v-model="policies"
                          @change="toggleSelect"
                          value="option3"
                        />
                        <label class="custom-control-label" for="agree3" v-text="$t('register.terms.newsletters')"
                          >뉴스레터 및 메시지 수신 (선택)</label
                        >
                      </div>
                      <button type="button" class="btn btn-outline-lightgray btn-sm" v-text="$t('register.terms.read')">보기</button>
                    </div>
                    <div class="btn-area">
                      <button
                        type="button"
                        class="btn btn-primary btn-lg btn-block"
                        :disabled="!(policies.includes('option1') && policies.includes('option2'))"
                        id="js-btn-next2"
                        @click="nextStep"
                        v-text="$t('register.form.next')"
                      >
                        Next
                      </button>
                    </div>
                  </div>
                </div>
                <div class="tab-pane" :class="{ active: step == 2 }">
                  <div class="pain-inner step-wrap">
                    <div class="guide">
                      <strong>DeepSignal</strong>
                      <p v-html="$t('register.desc')">
                        세상의 모든 정보와 의미를 재연결하여 <br />
                        사용자에게 통찰을 제공하는 <br />
                        증강지능 서비스
                      </p>
                    </div>
                    <ul class="nav nav-login">
                      <li class="nav-item">
                        <a
                          class="nav-link"
                          :class="{ active: selectedLogin }"
                          :aria-selected="selectedLogin"
                          @click="handleTypeLogin(true)"
                          v-text="$t('register.phone.phone')"
                          ><i class="bi bi-phone"></i> 핸드폰 로그인</a
                        >
                      </li>
                      <li class="nav-item">
                        <a
                          class="nav-link"
                          :class="{ active: !selectedLogin }"
                          :aria-selected="!selectedLogin"
                          @click="handleTypeLogin(false)"
                          v-text="$t('register.phone.email')"
                          ><i class="bi bi-envelope"></i> 이메일 로그인</a
                        >
                      </li>
                    </ul>
                    <div class="form-group">
                      <label class="group-label">Name</label>
                      <input type="text" class="form-control" v-model="formModel.lastName" :placeholder="$t('register.phone.name')" />
                      <div class="invalid-feedback" style="display: block" v-if="checkName">You have not entered your name yet!</div>
                    </div>
                    <div class="tab-content">
                      <ds-phone-valid-auth
                        :formModel="formModel"
                        :isShowConfirmBtn="true"
                        :isValid.sync="isValid"
                        :selectedLogin="selectedLogin"
                        ref="confirmPhone"
                      ></ds-phone-valid-auth>
                      <ds-email-valid-auth
                        ref="confirmEmail"
                        :formModel="formModel"
                        :isValid.sync="isValid"
                        :selectedLogin="!selectedLogin"
                      ></ds-email-valid-auth>
                    </div>
                    <div class="btn-area">
                      <button
                        type="button"
                        class="btn btn-primary btn-lg btn-block"
                        id="js-btn-next1"
                        @click="receive"
                        v-text="$t('register.form.next')"
                        :disabled="!isValid"
                      >
                        Next
                      </button>
                    </div>
                  </div>
                </div>
                <div class="tab-pane" :class="{ active: step == 3 }">
                  <div class="pane-inner step-wrap">
                    <div class="guide">
                      <strong v-html="$t('register.purposeChosen.register-success-notice')"
                        >Welcome!<br />DeepSignal You are subscribed to the service.</strong
                      >
                      <p class="guide-desc m-b-30" v-text="$t('register.purposeChosen.description')">
                        Please select the purpose of registration and log in.
                      </p>
                    </div>
                    <div class="form-group">
                      <label class="group-label">Purpose of Joining</label>
                      <div class="purpose-check-list btn-group btn-group-toggle" data-toggle="buttons">
                        <label
                          class="btn list-item"
                          :class="{ active: formModel.purposeSet.indexOf(item.id) > -1 }"
                          v-for="item in purposes"
                          :key="item.id"
                          @click="selectPurposes(item.id)"
                          >{{ item.purposeName }}</label
                        >
                      </div>
                      <div
                        class="valid-feedback"
                        style="display: block"
                        v-if="loginDeny"
                        v-text="$t('register.purposeChosen.notice-deny-to-login')"
                      >
                        Register successfully! Please contact Admin to login.
                      </div>
                      <div
                        v-if="buttonPurposes"
                        class="invalid-feedback"
                        style="display: block"
                        v-text="$t('register.messages.isChosePurpose')"
                      >
                        You must choose at least one purpose!
                      </div>
                    </div>
                    <div class="btn-area">
                      <button type="button" class="btn btn-primary btn-lg btn-block" @click="handleCheckbox">Login</button>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!--      Modal    -->
        <b-modal id="modal-register-success" centered title="Register" hide-header hide-footer hide-backdrop>
          <div class="naming-layer active">
            <div class="dim"></div>
            <div class="layer-wrap" style="opacity: 1; visibility: visible">
              <div class="layer-top">
                <div class="tit">
                  <strong v-text="$t('register.modal.title')">이름을 지어주세요!</strong>
                </div>
              </div>
              <div class="layer-body">
                <div class="learning-create">
                  <div class="inner">
                    <div class="row">
                      <div class="col">
                        <p class="hello" v-html="$t('register.modal.welcome')">
                          DeepSignal에 오신 것을 환영합니다. <br />
                          저에게 이름을 지어주세요!
                        </p>
                        <div class="naming-input">
                          <div class="row row-15">
                            <div class="col">
                              <div class="naming">
                                <a href="" aria-label="새로고침" class="btn-refresh" @click="handleRefresh($event)"
                                  ><i class="icon-refresh"></i
                                ></a>
                                <input
                                  type="text"
                                  class="form-control"
                                  v-model="formModel.nameConnectome"
                                  @keyup.enter.stop.prevent="skipToLearningCenter"
                                />
                                <a href="" @click="handleDirectInput($event)" aria-label="직접입력" class="btn-direct-input"
                                  ><i class="icon-pencil"></i
                                ></a>
                              </div>
                              <div
                                v-if="validateModal"
                                class="invalid-feedback"
                                style="display: block"
                                v-html="$t('register.messages.error[\'connectomeIsNull\']')"
                              >
                                Connectome can't be null
                                <!--                v-text="$t('login.messages.error[\'phone.maxlength\']')"-->
                              </div>
                            </div>
                            <div class="col-auto">
                              <button type="button" class="btn btn-primary" @click="skipToLearningCenter">OK</button>
                            </div>
                          </div>
                        </div>
                      </div>
                      <div class="col-auto" v-if="isAiAnimation">
                        <input
                          type="file"
                          ref="uploadFile"
                          style="display: none"
                          accept="image/x-png,image/gif,image/jpeg"
                          @change="changeImageProfile($event)"
                        />
                        <div class="ai-img">
                          <img src="content/images/ai-lg-without-eyes.png" alt="" />
                          <span class="eyes">
                            <span class="left"></span>
                            <span class="right"></span>
                          </span>
                          <a aria-label="사진등록" class="btn-photo" @click="$refs.uploadFile.click()"><i class="icon-camera"></i></a>
                        </div>
                      </div>
                      <div class="col-auto" v-if="!isAiAnimation" style="border: #161616 1px">
                        <input
                          type="file"
                          ref="uploadFile"
                          style="display: none"
                          accept="image/x-png,image/gif,image/jpeg"
                          @change="changeImageProfile($event)"
                        />
                        <div class="ai-img">
                          <img
                            id="image-profile"
                            alt="this is profile image"
                            style="
                              overflow: hidden;
                              position: relative;
                              width: 300px;
                              height: 300px;
                              overflow: hidden;
                              border-radius: 50%;
                              border: 5px solid #1da1f2;
                            "
                          />
                          <a aria-label="사진등록" class="btn-photo" @click="$refs.uploadFile.click()"><i class="icon-camera"></i></a>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </b-modal>

        <b-modal id="termOfUse" hide-header hide-footer size="lg">
          <terms-of-use></terms-of-use>
        </b-modal>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./register.component.ts"></script>
<style scoped>
.agree-item {
  flex-wrap: unset;
}
</style>
<style lang="scss">
.designed-custom-form label {
  width: 150px;
  display: -webkit-box;
}

.step li {
  cursor: pointer;
}

#modal-register-success {
  padding: unset !important;

  .modal-dialog {
    max-width: 100%;
    margin: 0;
    height: 100%;

    .modal-content {
      height: 100%;
      border: unset;
    }
  }
}
</style>
