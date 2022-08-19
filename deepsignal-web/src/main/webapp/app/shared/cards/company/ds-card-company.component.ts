import { Component, Prop, Vue } from 'vue-property-decorator';
import DsCardFooter from '@/shared/cards/footer/ds-card-footer.vue';
import { CARD_SIZE } from '@/shared/constants/feed-constants';

@Component({
  components: {
    'ds-card-footer': DsCardFooter,
  },
})
export default class DsCardCompany extends Vue {
  @Prop(Object) readonly item: any | undefined;
  cardSize = CARD_SIZE;
}
