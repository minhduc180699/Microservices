<template>
  <div class="tab-pane" :class="{ active: selectedLogin }" id="login-phone">
    <div class="pane-inner">
      <div class="form-group">
        <label class="group-label">Phone number</label>
        <!--        <select class="form-control" v-model="countrySelect">-->
        <!--          <option selected v-text="$t('register.phone.selectCountries')"></option>-->
        <!--          <option v-for="country in countries" :value="country" :key="country.name">{{ country.name }}</option>-->
        <!--        </select>-->
        <multiselect
          v-model="countrySelect"
          :options="countries"
          :searchable="true"
          :close-on-select="true"
          :allow-empty="false"
          label="name"
          class="input-group"
          :select-label="''"
          placeholder="Select one"
          deselect-label="This value has been selected"
        >
          <template slot="noResult">No elements found!</template>
        </multiselect>
        <div class="input-group">
          <input
            type="tel"
            class="form-control"
            :placeholder="$t('register.phone.phoneNumber')"
            v-model="$v.formModel.phoneNumber.$model"
            ref="txtCode"
            @input="checkRegexPhoneNumber"
            v-on:keydown.enter.prevent="sendCodeEnter()"
            maxlength="20"
          />
          <button
            type="button"
            class="btn btn-light"
            @click="sendCode()"
            :disabled="!formModel.phoneNumber || !checkRegex"
            v-text="$t('register.phone.sendCode')"
          >
            인증 요청
          </button>
        </div>
        <div class="valid-feedback" style="display: block" v-if="!readonlyInput">The verification code has been sent.</div>
        <div class="invalid-feedback" style="display: block" v-if="!checkRegex">The phone number you entered is not correct.</div>
        <div
          class="invalid-feedback"
          style="display: block"
          v-if="checkPhoneNumberIsNotExisted"
          v-html="$t('register.messages.error[\'phoneIsNotExisted\']')"
        >
          The phone number you entered doesn't exist. Please choose another one!
        </div>
        <div
          class="invalid-feedback"
          style="display: block"
          v-if="checkPhoneNumberIsExisted"
          v-html="$t('register.messages.error[\'phoneExisted\']')"
        >
          The phone number you entered already existed. Please choose another one!
        </div>
      </div>
      <div class="form-group">
        <label class="group-label">Code</label>
        <div class="input-timer">
          <input
            type="tel"
            class="form-control control-lg"
            :placeholder="$t('register.phone.confirmCode')"
            v-model="$v.formModel.code.$model"
            ref="txtCode"
            :disabled="readonlyInput"
            @keyup.enter.stop.prevent="nextOrLogin()"
            maxlength="6"
          />
          <span class="timer">{{ minLeft }}:{{ secLeft }}</span>
        </div>
        <div class="invalid-feedback" style="display: block" v-if="confirmCode">The password is incorrect. Try again.</div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./ds-phone-valid-auth.component.ts"></script>
<style>
.multiselect {
  border: 1px solid #ced4da;
  border-radius: 0.25rem;
  display: block;
}
.multiselect__tags {
  padding-left: 1rem !important;
}
</style>
