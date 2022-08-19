<template>
  <div>
    <div id="container-block">
      <div class="container">
        <div class="form-wrap form-login">
          <div class="form-header">
            <div class="brand-area">
              <a class="logo" href="/"></a>
            </div>
            <ul class="nav nav-login">
              <li class="nav-item w-50">
                <a
                  class="nav-link"
                  :class="{ active: selectedLogin }"
                  data-toggle="tab"
                  :aria-selected="selectedLogin"
                  @click="handleTypeLogin(true)"
                  ><i class="bi bi-phone"></i> <span v-text="$t('register.phone.phone')">Phone</span></a
                >
              </li>
              <li class="nav-item w-50">
                <a class="nav-link" :class="{ active: !selectedLogin }" :aria-selected="!selectedLogin" @click="handleTypeLogin(false)"
                  ><i class="bi bi-envelope"></i> <span v-text="$t('register.phone.email')">Email</span></a
                >
              </li>
            </ul>
          </div>
          <div class="form-body">
            <div class="tab-content">
              <ds-phone-valid-auth
                :page="'login'"
                ref="dsPhoneComponent"
                :formModel="formModel"
                :loginFailedAttempt.sync="loginFailedAttempt"
                :isValid.sync="isValid"
                :selectedLogin="selectedLogin"
              ></ds-phone-valid-auth>
              <ds-email-valid-auth
                :page="'login'"
                ref="dsEmailComponent"
                :formModel="formModel"
                :loginFailedAttempt.sync="loginFailedAttempt"
                :isValid.sync="isValid"
                :selectedLogin="!selectedLogin"
              ></ds-email-valid-auth>
              <div class="keep-check">
                <div class="custom-control custom-checkbox">
                  <input type="checkbox" class="custom-control-input" id="keepMe" v-model="rememberMe" @click="rememberMe = !rememberMe" />
                  <label class="custom-control-label" for="keepMe" v-text="$t('login.form.rememberme')"></label>
                </div>
              </div>
              <div class="btn-area">
                <button
                  type="button"
                  class="btn btn-primary btn-lg btn-block"
                  @click="confirm()"
                  :disabled="!isValid"
                  v-text="$t('login.form.button')"
                >
                  Login
                </button>
              </div>
              <div
                class="invalid-feedback"
                style="display: block"
                v-if="loginFailedAttempt > 4"
                v-text="$t('login.messages.error.lockedAccount')"
              ></div>
            </div>
          </div>
          <div class="form-footer">
            <div class="signup-info">
              <p><i class="icon-common-lg icon-signup"></i>Not Joined yet?</p>
              <router-link to="/register" v-slot="{ navigate }" custom>
                <button type="button" @click="routeToRegister(navigate, $event)" class="btn btn-outline-success">Sign up for free</button>
              </router-link>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./login.component.ts"></script>
<style>
#login-phone > div > div > label {
  cursor: pointer;
}
#login-background .swiper-wrapper {
  overflow: unset;
}
</style>
