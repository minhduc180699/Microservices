<template>
  <div class="panel-sidebar connectome-home active">
    <div class="panel-inner">
      <div class="panel-main">
        <div class="main-inner">
          <div class="panel-header">
            <div class="header-content">
              <div class="search-connectome-wrap">
                <div class="form-group style1">
                  <input
                    id="inputFieldKeywordSearch"
                    class="form-control"
                    type="text"
                    v-model="filterTerms"
                    v-on:input="onSearchInput($event)"
                    :placeholder="$t('map-side-bar.entity-search-tab.search-placeholder')"
                    aria-label="search_input"
                  />
                  <button class="btn-search" type="button" aria-label="search_button" aria-disabled="true">
                    <i class="bi bi-search"></i>
                  </button>
                </div>
              </div>
            </div>
          </div>
          <div class="panel-body section-style1">
            <div class="panel-section">
              <div class="cnt-list">
                <div class="tab-content">
                  <div class="tab-pane fade show active" id="cnt-allList">
                    <div class="search-keyword-box">
                      <div class="top-bar">
                        <div class="top-title">
                          <span class="total" v-text="$t('map-side-bar.entity-search-tab.result-count', [documentCardItems.length])"></span>
                        </div>
                      </div>
                      <div class="list-scrollable">
                        <div class="list-wrap overflow-y-scroll">
                          <ul class="search-keyword-list favorites">
                            <li>
                              <div class="panel-body section-style2 overflow-y-scroll">
                                <div style="margin: 5px">
                                  <b-form-input v-model="noteTitle" placeholder="title"></b-form-input>
                                  <b-form-input v-model="noteContent" style="margin-top: 2px" placeholder="content"></b-form-input>
                                  <b-button variant="outline-primary" style="margin-top: 2px" @click="addNote">Add</b-button>
                                </div>
                              </div>
                            </li>
                            <li v-for="doc in documentCardItems" :key="doc.id">
                              <b-card :header="doc.author" :bg-variant="doc.isAdded ? 'Success' : 'Primary'">
                                <b-card-text v-on:click="onBtnAddPersonalDocumentsToConnectomeClick(doc)">
                                  {{ doc.title }}
                                </b-card-text>
                                <b-card-text class="small text-muted">{{ convertGMTToLocalTime(doc.addedAt) }}</b-card-text>
                              </b-card>
                            </li>
                          </ul>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <button
      type="button"
      id="btn-sidebar-folding"
      class="btn btn-toggle btn-sidebar-folding"
      v-on:click="onBtnSidebarFoldingClick"
    ></button>
  </div>
</template>
<script lang="ts" src="./builder-map-side-bar.component.ts"></script>
<style></style>
