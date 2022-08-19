<template>
  <div class="tab-pane" :class="{ active: selectedLogin }" id="login-email">
    <div class="pane-inner">
      <div class="form-group">
        <label class="group-label">Email address</label>
        <div class="input-group">
          <input
            type="email"
            class="form-control"
            :placeholder="$t('register.messages.enterEmail')"
            v-model="email"
            @input="checkEmail(email)"
            v-on:keydown.enter.prevent="sendCodeEnter()"
            maxlength="191"
          />
          <button
            type="button"
            class="btn btn-light"
            @click="sendEmail"
            :disabled="disableSend || validateEmail || !email"
            v-text="$t('register.phone.sendCode')"
          >
            Send
          </button>
        </div>
        <div class="valid-feedback" style="display: block" v-if="isCodeSent">The verification code has been sent.</div>
        <div
          class="invalid-feedback"
          style="display: block"
          v-if="checkEmailIsExisted"
          v-html="$t('register.messages.error[\'emailexists\']')"
        ></div>
        <div
          class="invalid-feedback"
          style="display: block"
          v-if="validateEmail"
          v-html="$t('register.messages.error[\'malformed\']')"
        ></div>
        <div class="invalid-feedback" style="display: block" v-if="checkEmailIsNotExisted">
          Your email acount doesn't exist. Please register!
        </div>
        <div class="invalid-feedback" style="display: block" v-if="checkEmailCorrect">Your email acount is incorrect!</div>
      </div>

      <div class="form-group">
        <label class="group-label">Code</label>
        <div class="input-timer">
          <input
            type="tel"
            class="form-control"
            :placeholder="$t('register.messages.verification')"
            v-model="confirmEmailCode"
            :disabled="disabledConfirm"
            @keyup.enter.stop.prevent="nextOrLogin()"
            maxlength="6"
            ref="emailCode"
          />
          <span class="timer">{{ minLeft }}:{{ secLeft }}</span>
        </div>
        <div class="invalid-feedback" style="display: block" v-if="confirmCode">The password is incorrect. Try again.</div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./ds-email-valid-auth.component.ts"></script>
