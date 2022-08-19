<template>
  <b-modal id="meta-search-detail" size="xl" centered hide-footer hide-header>
    <div class="modal-dialog modal-dialog-centered modal-xl">
      <div class="modal-content">
        <!--        <div class="modal-body">-->
        <div class="contents-detail">
          <div class="contents-head">
            <div class="row">
              <div class="col">
                <div class="tit">{{ metaSearchItem.title }}</div>
                <div class="date">{{ metaSearchItem.date }}</div>
              </div>
              <div class="col-auto">
                <div class="controller">
                  <a href="javascript:;" aria-label="Add"><i class="icon-cart-add"></i></a>
                  <a href="javascript:;" aria-label="Minimization" disabled><i class="icon-minimization"></i></a>
                  <a @click="$bvModal.hide('meta-search-detail')" aria-label="Close" data-dismiss="modal" class="close"
                    ><i class="icon-close"></i
                  ></a>
                </div>
              </div>
            </div>
          </div>
          <div class="contents-body" style="height: 540px !important">
            <vue-scroll :ops="ops">
              <!--                <div class="overflow-y-scroll">-->
              <div class="margin-right-content">
                <div class="px-5" style="text-align: center">
                  <img :src="metaSearchItem.image" alt="" />
                </div>
                <div style="margin-top: 40px; white-space: pre-line">
                  {{ metaSearchItem.content }}
                </div>
              </div>
              <!--                </div>-->
            </vue-scroll>
          </div>
        </div>
        <!--        </div>-->
      </div>
    </div>
  </b-modal>
</template>

<script lang="ts" src="./meta-search-detail.component.ts"></script>

<style lang="scss" scoped>
.modal-content .modal-dialog {
  margin: 0 auto !important;
}
.margin-right-content {
  margin-right: 24px;
}
</style>
