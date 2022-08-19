<template>
  <!-- [Contents Keyword Analyzer] -->
  <div>
    <b-container class="bv-example-row" style="padding-top: 10px">
      <b-row cols="3">
        <b-col sm="3">
          <label>Request ID:</label>
        </b-col>
        <b-col sm="2">
          <b-form-input v-model="requestId"></b-form-input>
        </b-col>
        <b-col sm="4"> </b-col>
      </b-row>
      <b-row cols="3" style="padding-top: 20px">
        <b-col sm="3">
          <label>Language:</label>
        </b-col>
        <b-col sm="2">
          <b-form-select v-model="languageSelected" :options="languageOptions" size="sm" class="mt-3"></b-form-select>
        </b-col>
        <b-col sm="4"> </b-col>
      </b-row>
      <b-row cols="1" style="padding-top: 20px">
        <b-col>
          <label>Text to analyze:</label>
        </b-col>
      </b-row>
      <b-row cols="1">
        <b-col>
          <b-form-textarea id="textarea" v-model="textToAnalyze" placeholder="Enter something..." rows="6" max-rows="10"></b-form-textarea>
        </b-col>
      </b-row>
      <b-row cols="1" style="padding-bottom: 20px">
        <b-col>
          <b-button variant="primary" v-on:click="onDataToAnalyze()">Analyse</b-button>
        </b-col>
      </b-row>
      <b-row cols="1" v-if="textAnalyzed" class="result">
        <b-col>
          <p v-html="textAnalyzed"></p>
        </b-col>
      </b-row>
      <b-row cols="1" v-if="textAnalyzed" style="padding-top: 20px">
        <b-col>
          <b-table :items="rows" :fields="fields" bordered :table-variant="headVariant" sticky-header="480px"></b-table>
        </b-col>
      </b-row>
    </b-container>
  </div>
</template>
<script lang="ts" src="./keyword-analyzer.component.ts"></script>
<style scoped>
.maxHeight {
  padding-top: 20px;
  height: 480px;
  overflow-y: scroll;
}
.maxInsideHeight {
  padding-top: 20px;
  width: 100%;
  height: 100%;
  overflow-y: scroll;
}
.result {
  padding-top: 20px;
  height: 480px;
  overflow-y: scroll;
  background: whitesmoke;
}

.highlight {
  color: red;
}
</style>
