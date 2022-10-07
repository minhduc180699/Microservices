<template>
  <div class="lc-panel-sub" :class="showList ? 'active' : ''">
    <div class="lc-sub-inner">
      <div class="panel-header">
        <div class="header-left">
          <button type="button" class="btn btn-icon btn-back" @click="backToGroup"><i class="icon-common-lg icon-arrow-left"></i></button>
          <strong class="title"><i class="icon-common-lg icon-folder"></i>Group</strong>
        </div>
        <div class="header-right">
          <button type="button" class="btn btn-icon btn-more"><i class="icon-common icon-pin-on"></i></button>
          <div class="more-area">
            <button type="button" class="btn btn-more" data-toggle="dropdown" aria-expanded="false">
              <i class="icon-common icon-more"></i>
            </button>
            <div class="dropdown-menu dropdown-menu-right">
              <a class="dropdown-item" href="#"><i class="icon-common icon-pin-off"></i>unpin the top</a>
              <a class="dropdown-item" href="#"><i class="icon-common icon-folder-edit"></i>Edit</a>
              <a class="dropdown-item" href="#"><i class="icon-common icon-trash"></i>Delete</a>
            </div>
          </div>
        </div>
      </div>
      <div class="panel-body">
        <div class="list-wrap overflow-y-scroll">
          <ul class="lc-card-list lc-context-list" v-for="(data, index) in listData" :key="index">
            <single-card :document="data"></single-card>
          </ul>
        </div>
      </div>
    </div>
  </div>
</template>

<script src="./list-card-group.component.ts" lang="ts"></script>
