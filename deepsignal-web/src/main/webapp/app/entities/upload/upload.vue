<template>
  <div class="container">
    <!--    <div class="item-list-top"></div>-->
    <figure class="tabBlock">
      <ul class="tabBlock-tabs">
        <li :key="0" :aria-setsize="2" :aria-posinet="1">
          <a
            href="javascript:void(0);"
            class="tabBlock-tab"
            :class="active_tab === 0 ? 'is-active' : ''"
            :aria-selected="active_tab === 0"
            @click="changeTab(0)"
          >
            Upload File
          </a>
        </li>
        <li :key="1" :aria-setsize="2" :aria-posinet="2">
          <a
            href="javascript:void(0);"
            class="tabBlock-tab"
            :class="active_tab === 1 ? 'is-active' : ''"
            :aria-selected="active_tab === 1"
            @click="changeTab(1)"
          >
            Upload URL
          </a>
        </li>
      </ul>
      <div class="tabBlock-content">
        <div :key="0" :aria-current="active_tab === 0" class="tabBlock-pane" v-show="active_tab === 0">
          <div class="form-area">
            <form name="frmUploadURL" v-on:submit.prevent="submitFile()" no-validate>
              <div class="form-area-body">
                <div class="form-group">
                  <label for="file">File</label>
                  <div class="group">
                    <div class="row row-10 align-items-center">
                      <div class="col">
                        <input
                          type="file"
                          id="file"
                          ref="file"
                          class="form-control"
                          :class="{ valid: !$v.uploadFile.file.$invalid, invalid: $v.uploadFile.file.$invalid }"
                          v-on:change="handleFileUpload()"
                        />
                      </div>
                    </div>
                  </div>
                  <div v-if="$v.uploadFile.file.$anyDirty && $v.uploadFile.file.$invalid" class="invalid-feedback" style="display: block">
                    The File is required!
                  </div>
                </div>
                <div class="btn-area">
                  <button type="submit" :disabled="$v.uploadFile.$invalid || isSaving" class="btn btn-primary min-width-100px">
                    Upload
                  </button>
                </div>
              </div>
            </form>
          </div>
        </div>
        <div :key="1" :aria-current="active_tab === 1" class="tabBlock-pane" v-show="active_tab === 1">
          <div class="form-area">
            <form name="frmUploadURL" v-on:submit.prevent="submitURL()" no-validate>
              <div class="form-area-body">
                <div class="form-group">
                  <label for="txtName">Name</label>
                  <div class="group">
                    <div class="row row-10 align-items-center">
                      <div class="col">
                        <input
                          type="text"
                          class="form-control"
                          v-model="$v.uploadURL.name.$model"
                          :class="{ valid: !$v.uploadURL.name.$invalid, invalid: $v.uploadURL.name.$invalid }"
                          id="urlName"
                          name="urlName"
                          placeholder="Enter URL name"
                        />
                      </div>
                    </div>
                  </div>
                  <div v-if="$v.uploadURL.name.$anyDirty && $v.uploadURL.name.$invalid" class="invalid-feedback" style="display: block">
                    The URL name is required!
                  </div>
                  <div v-if="!$v.uploadURL.name.maxLength" class="invalid-feedback" style="display: block">
                    This field cannot be longer than 254 characters.
                  </div>
                </div>
                <div class="form-group">
                  <label for="txtURL">URL</label>
                  <div class="group">
                    <div class="row row-10 align-items-center">
                      <div class="col">
                        <input
                          type="text"
                          v-model="$v.uploadURL.url.$model"
                          class="form-control"
                          :class="{ valid: !$v.uploadURL.url.$invalid, invalid: $v.uploadURL.url.$invalid }"
                          id="txtURL"
                          name="url"
                          placeholder="Enter URL"
                        />
                      </div>
                    </div>
                  </div>
                  <div v-if="$v.uploadURL.url.$anyDirty && $v.uploadURL.url.$invalid" class="invalid-feedback" style="display: block">
                    The URL is required!
                  </div>
                </div>
                <div class="btn-area">
                  <button type="submit" :disabled="$v.uploadURL.$invalid || isSaving" class="btn btn-primary min-width-100px">
                    Upload
                  </button>
                </div>
              </div>
            </form>
          </div>
        </div>
      </div>
    </figure>
  </div>
</template>

<script lang="ts" src="./upload.component.ts"></script>

<style lang="scss" scoped>
*,
::before,
::after {
  box-sizing: border-box;
}

body {
  color: #222;
  font-family: 'Source Sans Pro', 'Helvetica Neue', 'Helvetica', 'Arial', sans-serif;
  line-height: 1.5;
  margin: 0 auto;
  max-width: 50rem;
  padding: 2.5rem 1.25rem;
}

/* ===================================================== */

$primary: #516eff;
$primary-faint: mix($primary, #fff, 40%);
$border: #d8d8d8;

.tabBlock {
  margin: 0 0 2.5rem;

  &-tabs {
    list-style: none;
    margin: 0;
    padding: 0;

    &::after {
      clear: both;
      content: '';
      display: table;
    }
  }

  &-tab {
    background-color: #fff;
    border-color: $border;
    border-left-style: solid;
    border-top: solid;
    border-width: 1px;
    color: $primary-faint;
    cursor: pointer;
    display: flex;
    align-items: center;
    font-weight: 600;
    float: left;
    padding: 0.625rem 1.25rem;
    position: relative;
    transition: 0.1s ease-in-out;
    text-decoration: none;

    &:last-of-type {
      border-right-style: solid;
    }

    &::before,
    &::after {
      content: '';
      display: block;
      height: 2px;
      position: absolute;
      transition: 0.1s ease-in-out;
    }

    &::before {
      background-color: $primary-faint;
      left: -2px;
      right: -2px;
      top: -2px;
    }

    &::after {
      background-color: transparent;
      bottom: -2px;
      left: 0;
      right: 0;
    }

    &:hover,
    &:focus {
      color: $primary;
    }

    @media screen and (min-width: 700px) {
      padding-left: 2.5rem;
      padding-right: 2.5rem;
    }

    &.is-active {
      position: relative;
      color: $primary;
      z-index: 1;

      &::before {
        background-color: $primary;
        height: 4px;
        top: -4px;
      }

      &::after {
        background-color: #fff;
      }
    }

    svg {
      height: 1.2rem;
      width: 1.2rem;
      margin-right: 0.5rem;
      pointer-events: none;
      fill: currentcolor;
    }
  }

  &-content {
    background-color: #fff;
    border: 1px solid $border;
    padding: 1.25rem;
    min-height: 650px;

    a {
      color: dodgerblue;
      font-weight: 700;
      transition: color 200ms ease;

      &:hover,
      &:focus {
        color: orangered;
      }
    }

    hr {
      margin: 3rem 0;
      border: 1px solid transparent;
      border-radius: 50%;
      border-top-color: $border;
    }
  }
}
</style>
