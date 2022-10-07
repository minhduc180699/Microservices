<template>
  <div>
    <div class="connectome-control">
      <div class="control-screen">
        <div class="control-screen">
          <div class="btn-group-vertical">
            <button type="button" class="btn btn-dark" v-on:click="fitScreen">
              <b-icon icon="aspect-ratio"></b-icon>
              <span><span v-text="$t('connectome-map.fit-to-screen')" /></span>
            </button>
          </div>
          <!-- <div style="margin: 5px">
            <b-form-input v-model="cmpMinNodeWeight" placeholder="title"></b-form-input>
            <b-form-input v-model="cmpMinRelatedDocuments" style="margin-top: 2px" placeholder="content"></b-form-input>
            <b-form-input v-model="cmpMinLinkedNodes" style="margin-top: 2px" placeholder="content"></b-form-input>
            <b-button variant="outline-primary" style="margin-top: 2px" @click="networkFiltersOnChange">Apply</b-button>
          </div> -->
        </div>
      </div>
      <div class="control-topick">
        <b-button v-b-toggle.sidebar-footer><b-icon icon="layout-sidebar" aria-hidden="true" variant="outline-primary"></b-icon></b-button>
      </div>
      <b-sidebar id="sidebar-footer" aria-label="Sidebar with custom footer">
        <template #footer="{}">
          <div class="d-flex bg-dark text-light align-items-center px-3 py-2">
            <b-button size="sm" @click="createEmptyContext" style="margin-right: 5px"
              ><b-icon icon="folder-plus" aria-hidden="true" variant="outline-primary"></b-icon> Create collection</b-button
            >
            <b-button size="sm" v-b-modal.add-document-tool style="margin-right: 5px"
              ><b-icon icon="plus-circle" aria-hidden="true" variant="outline-primary"></b-icon> Add document</b-button
            >
            <b-button size="sm" @click="saveContext"
              ><b-icon icon="save2" aria-hidden="true" variant="outline-primary"></b-icon> Update collection</b-button
            >
          </div>
        </template>
        <map-side-bar></map-side-bar>
        <b-modal id="add-document-tool" hide-header hide-footer title="Add document to collection">
          <map-add-document-tool></map-add-document-tool>
        </b-modal>
      </b-sidebar>
    </div>
    <div class="connectome-area"></div>
  </div>
</template>
<script lang="ts" src="./builder-map.component.ts"></script>
<style scoped>
#connectome-network {
  background-color: black;
}
</style>
