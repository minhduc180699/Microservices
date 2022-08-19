<template>
  <ul class="reply-list">
    <node-tree v-for="node of treeData" :key="node.id" :node="node" @onReply="onReply($event)"></node-tree>
  </ul>
</template>

<script lang="ts">
import { Component, Vue, Prop } from 'vue-property-decorator';
import DsNodeTree from '@/shared/tree/ds-node-tree.vue';

@Component({
  components: {
    'node-tree': DsNodeTree,
  },
  name: 'ds-tree',
})
export default class DsTree extends Vue {
  @Prop(Array) readonly treeData: any | undefined;

  onReply(node) {
    this.$emit('onReply', node);
  }
}
</script>
