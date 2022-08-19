<template>
  <b-modal id="modal-user-info" hide-header hide-footer centered @hide="closePopup()">
    <div class="layer-wrap myinfo-layer">
      <div class="layer-content" style="max-width: 580px">
        <div class="layer-header">
          <div class="d-flex align-items-center justify-content-between">
            <div class="tit">
              <strong v-text="$t('userInfo.title')">User Info</strong>
            </div>
            <button
              @click="$bvModal.hide('modal-user-info')"
              type="button"
              class="layer-btn-close"
              aria-label="Close"
              id="js-btn-myinfo-close"
            >
              <i class="icon-close"></i>
            </button>
          </div>
        </div>
        <div class="layer-body">
          <div class="profile-area">
            <div class="profile" style="cursor: pointer" @click="openPopupUpload()">
              <!-- 이미지 O -->
              <img v-if="imageUrl" id="avatar" :src="imageUrl" @error="$event.target.src = 'content/images/avatar.png'" alt="" />
              <!-- 이미지 X -->
              <span v-else v-text="$t('userInfo.avatar')"></span>
            </div>
            <input type="file" accept="image/*" id="fileUpload" name="fileUpload" @input="uploadAvatar($event)" class="d-none" />
            <div class="d-flex align-items-center">
              <!-- <button type="button" class="btn btn-primary min-width-80px" v-text="$t('userInfo.cancelImage')"></button> -->
              <!--              <button type="button" class="btn btn-lightgray min-width-80px" v-text="$t('userInfo.registration')"></button>-->
            </div>
          </div>
          <div class="float-right" style="width: 100%">
            <ul class="nav nav-util">
              <div class="lang">
                <b-navbar-nav class="ml-auto">
                  <b-nav-item-dropdown id="languagesnavBarDropdown" right v-if="languages && Object.keys(languages).length > 1">
                    <span slot="button-content">
                      <i class="bi bi-globe" style="vertical-align: middle; font-size: 1rem"></i>
                      <span class="no-bold">{{ language }}</span>
                    </span>
                    <b-dropdown-item
                      v-for="(value, key) in languages"
                      :key="`lang-${key}`"
                      v-on:click="changeLanguage(key)"
                      :class="{ active: isActiveLanguage(key) }"
                    >
                      {{ value.name }}
                    </b-dropdown-item>
                  </b-nav-item-dropdown>
                </b-navbar-nav>
              </div>
            </ul>
          </div>
          <div class="form-myinfo">
            <!--            <div class="form-group">-->
            <!--              <label for="fInput1" v-text="$t('userInfo.name')"></label>-->
            <!--              <input v-model="firstName" type="text" class="form-control" id="fInput1" />-->
            <!--            </div>-->

            <!--            <div class="form-group">-->
            <!--              <div class="row">-->
            <!--                <div class="col">-->
            <!--                  <label for="firstName" v-text="$t('userInfo.firstName')"></label>-->
            <!--                  <input v-model="$v.firstName.$model" type="text" class="form-control" id="firstName"/>-->
            <!--                </div>-->
            <!--                <div class="col" id="lastNameBefore">-->
            <!--                  <label for="lastName" v-text="$t('userInfo.lastName')"></label>-->
            <!--                  <input v-model="$v.lastName.$model" type="text" class="form-control" id="lastName"/>-->
            <!--                </div>-->
            <!--                <div-->
            <!--                  v-if="$v.firstName.$anyDirty && $v.firstName.$invalid"-->
            <!--                  class="invalid-feedback"-->
            <!--                  style="display: block"-->
            <!--                  v-text="$t('userInfo.error[\'firstName.maxLength\']')"-->
            <!--                ></div>-->
            <!--                <div-->
            <!--                  v-if="$v.lastName.$anyDirty && $v.lastName.$invalid"-->
            <!--                  class="invalid-feedback"-->
            <!--                  style="display: block"-->
            <!--                  v-text="$t('userInfo.error[\'lastName.maxLength\']')"-->
            <!--                ></div>-->
            <!--              </div>-->
            <!--            </div>-->

            <div class="form-group">
              <label for="firstName" v-text="$t('userInfo.firstName')"></label>
              <input v-model="$v.firstName.$model" type="text" class="form-control" id="firstName" />
              <div
                v-if="$v.firstName.$anyDirty && $v.firstName.$invalid"
                class="invalid-feedback"
                style="display: block"
                v-text="$t('userInfo.error[\'firstName.maxLength\']')"
              ></div>
            </div>

            <div class="form-group">
              <label for="lastName" v-text="$t('userInfo.lastName')"></label>
              <input v-model="$v.lastName.$model" type="text" class="form-control" id="lastName" />
              <div
                v-if="$v.lastName.$anyDirty && $v.lastName.$invalid"
                class="invalid-feedback"
                style="display: block"
                v-text="$t('userInfo.error[\'lastName.maxLength\']')"
              ></div>
            </div>

            <div class="form-group">
              <label for="fInput2" v-text="$t('userInfo.phone')"></label>
              <div class="row">
                <div class="col-auto">
                  <select v-model="countrySelect" name="" id="fInput2" class="custom-select">
                    <option v-for="(country, index) in countries" :value="country" :key="index">{{ country.name }}</option>
                  </select>
                </div>
                <div class="col">
                  <input v-model="$v.formModel.phoneNumber.$model" type="tel" class="form-control" title="전화번호 입력" />
                </div>
                <!--                <div class="col-auto">-->
                <!--                  <button-->
                <!--                    @click="sendCode()"-->
                <!--                    :disabled="$v.formModel.phoneNumber.$invalid"-->
                <!--                    type="button"-->
                <!--                    class="btn btn-outline-lightgray min-width-120px"-->
                <!--                    v-text="$t('userInfo.sendCode')"-->
                <!--                  >-->
                <!--                    Send code-->
                <!--                  </button>-->
                <!--                </div>-->
                <div
                  v-if="$v.formModel.phoneNumber.$anyDirty && $v.formModel.phoneNumber.$invalid"
                  class="invalid-feedback"
                  style="display: block"
                  v-text="$t('login.messages.error.phone')"
                >
                  The phone number you entered is not correct.
                </div>
              </div>
            </div>

            <div :class="{ 'd-none': !formModel.code }" class="form-group">
              <label>Code</label>
              <div class="row">
                <div class="col">
                  <input v-model="$v.formModel.code.$model" ref="txtCode" type="tel" class="form-control" title="전화번호 입력" />
                </div>
                <div class="col-auto">
                  <span class="timer">{{ minLeft }}:{{ secLeft }}</span>
                </div>
                <div class="col-auto">
                  <button
                    @click="changePhoneNumber()"
                    type="button"
                    class="btn btn-outline-lightgray min-width-120px"
                    :disabled="$v.formModel.code.$invalid || readonlyInput"
                    v-text="$t('userInfo.changePhone')"
                  >
                    Change phone
                  </button>
                </div>
                <div
                  v-if="$v.formModel.code.$anyDirty && $v.formModel.code.$invalid"
                  class="invalid-feedback"
                  style="display: block"
                  v-text="$t('login.messages.error.code')"
                >
                  The code you entered is not correct.
                </div>
                <div
                  v-if="!$v.formModel.code.maxLength"
                  class="invalid-feedback"
                  style="display: block"
                  v-text="$t('login.messages.error[\'code.maxlength\']')"
                >
                  This field cannot be longer than 10 characters.
                </div>
              </div>
            </div>

            <div class="form-group">
              <label for="fInput3" v-text="$t('userInfo.email')"></label>
              <div class="row">
                <div class="col">
                  <input v-model="email" type="email" class="form-control" id="fInput3" />
                </div>
                <!--                <div class="col-auto">-->
                <!--                  <button type="button" class="btn btn-outline-lightgray min-width-120px" v-text="$t('userInfo.changeEmail')"></button>-->
                <!--                </div>-->
              </div>
            </div>
          </div>
        </div>
        <div class="layer-footer">
          <div class="buttons justify-content-between">
            <div>
              <button
                v-b-modal.confirmDelete
                type="button"
                class="btn btn-outline-lightgray min-width-100px"
                v-text="$t('userInfo.delete')"
              ></button>
              <b-modal
                id="confirmDelete"
                hide-header
                centered
                :ok-title="$t('userInfo.dialog.ok')"
                :cancel-title="$t('userInfo.dialog.cancel')"
                @ok="deleteUser()"
              >
                <p class="my-5" v-text="$t('userInfo.dialog.message')"></p>
              </b-modal>
            </div>
            <div>
              <button
                @click="$bvModal.hide('modal-user-info')"
                type="button"
                class="btn btn-dark min-width-120px"
                v-text="$t('userInfo.cancel')"
              ></button>
              <button @click="updateUser()" type="button" class="btn btn-primary min-width-120px" v-text="$t('userInfo.confirm')"></button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </b-modal>
</template>

<script lang="ts" src="./popup-user-info.component.ts"></script>

<style>
#modal-user-info___BV_modal_body_ {
  padding: 0;
}

#modal-user-info___BV_modal_content_ {
  border: unset;
  border-radius: 8px;
}

#modal-user-info .modal-dialog {
  max-width: 580px;
}

.float-right .nav {
  float: right;
}

#confirmDelete div div footer {
  background: unset;
  margin-top: 0;
}

#confirmDelete .modal-dialog .modal-content .modal-body {
  padding: 0 0 0 15px;
}

#lastNameBefore::before {
  content: '';
  width: 1px;
  height: 20px;
  background: rgba(0, 0, 0, 0.1);
  position: absolute;
  left: 0;
  top: 60%;
}
#languagesnavBarDropdown .nav-link {
  width: unset;
  height: unset;
}
#languagesnavBarDropdown ul {
  left: unset !important;
}
</style>
