import { Component, Vue, Prop } from 'vue-property-decorator';
import DsCardTopTenDefault from '@/shared/cards/topten/ds-card-topten-default/ds-card-topten-default.vue';
import DsCardTopTenTitle from '@/shared/cards/topten/ds-card-topten-title/ds-card-topten-title.vue';
import DsCardTopTenContent from '@/shared/cards/topten/ds-card-topten-content/ds-card-topten-content.vue';

@Component({
  components: {
    'ds-card-top-ten-default': DsCardTopTenDefault,
    'ds-card-top-ten-title': DsCardTopTenTitle,
    'ds-card-top-ten-content': DsCardTopTenContent,
  },
})
export default class DsCardTopTenParent extends Vue {
  @Prop(Object) readonly item: any | undefined;
}
