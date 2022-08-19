<template>
  <div>
    <div class="page-top">
      <div class="top-title">
        <strong><slot></slot></strong><slot name="feedSearch"></slot>
      </div>
      <div v-if="isCheck" class="top-elements">
        <div class="date-wrap">
          <button
            type="button"
            @click.prevent="selectedDate(bforeYesterday, 3)"
            :class="['btn btn-day cstomfont ', { active: actived == 3 }]"
            v-text="$t('search.justYesterday')"
          >
            그제
          </button>
          <button
            type="button"
            @click.prevent="selectedDate(yesterday, 2)"
            :class="['btn btn-day cstomfont ', { active: actived == 2 }]"
            v-text="$t('search.yesterday')"
          >
            어제
          </button>
          <button
            type="button"
            @click.prevent="selectedDate(today, 1)"
            :class="['btn btn-day cstomfont ', { active: actived == 1 }]"
            v-text="$t('search.today')"
          >
            오늘
          </button>
          <div class="date-directInput-wrap">
            <div class="input-group">
              <date-picker
                v-model="dateDefault"
                @pick="selectedDate($event, false, true)"
                type="date"
                format="YYYY.MM.DD"
                valueType="format"
                :clearable="false"
                :disabled-date="disableDate"
              ></date-picker>
            </div>
          </div>
        </div>
      </div>
      <slot name="feedBtnFilter"></slot>
    </div>
    <slot name="feedFilterBox"></slot>
  </div>
</template>

<script src="./page-top.component.ts"></script>

<style>
@media screen and (min-width: 360px) and (max-width: 600px) {
  .cstomfont {
    font-size: 5px !important;
  }
}
</style>
