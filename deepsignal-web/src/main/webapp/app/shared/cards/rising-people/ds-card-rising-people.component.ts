import { Component, Vue, Prop } from 'vue-property-decorator';
import DsCardFooter from '@/shared/cards/footer/ds-card-footer.vue';

@Component({
  components: {
    'ds-card-footer': DsCardFooter,
  },
})
export default class DsCardRisingPeople extends Vue {
  @Prop(Object) readonly item: any | undefined;
}
