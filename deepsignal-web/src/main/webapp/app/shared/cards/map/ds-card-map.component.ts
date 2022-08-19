import { Component, Vue, Prop } from 'vue-property-decorator';

@Component
export default class DsCardMap extends Vue {
  @Prop(Object) readonly item: any | undefined;
}
