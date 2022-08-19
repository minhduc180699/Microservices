<template>
  <div class="dropdown" v-if="options">
    <!-- Dropdown Input -->
    <input
      class="dropdown-input"
      :name="name"
      @focus="showOptions()"
      @blur="exit()"
      @keyup="keyMonitor"
      v-model="searchFilter"
      :disabled="disabled"
      :placeholder="placeholder"
      :spellcheck="false"
      autocomplete="off"
    />

    <!-- Dropdown Menu -->
    <div class="dropdown-content" v-show="optionsShown">
      <div class="dropdown-item" @mousedown="selectOption(option)" v-for="(option, index) in filteredOptions" :key="index">
        {{ option.name || option.id || '-' }}
      </div>
    </div>
  </div>
</template>
<script lang="ts" src="./ds-search-dropdown.component.ts"></script>
<style lang="scss" scoped>
.dropdown {
  position: relative;
  display: block;
  margin: auto;
  .dropdown-input {
    background: #fff;
    cursor: pointer;
    border: 1px solid #e7ecf5;
    border-radius: 3px;
    color: #333;
    display: block;
    font-size: 0.8em;
    padding: 6px;
    min-width: 250px;
    max-width: 350px;
    &:hover {
      background: #f8f8fa;
    }
  }
  .dropdown-content {
    position: absolute;
    background-color: #fff;
    min-width: 248px;
    max-width: 248px;
    max-height: 496px;
    border: 1px solid #e7ecf5;
    box-shadow: 0px -8px 34px 0px rgba(0, 0, 0, 0.05);
    overflow: auto;
    z-index: 1;
    .dropdown-item {
      color: black;
      font-size: 0.7em;
      line-height: 1em;
      padding: 8px;
      text-decoration: none;
      display: block;
      cursor: pointer;
      &:hover {
        background-color: #e7ecf5;
      }
    }
  }
  .dropdown:hover .dropdowncontent {
    display: block;
  }
}
</style>
