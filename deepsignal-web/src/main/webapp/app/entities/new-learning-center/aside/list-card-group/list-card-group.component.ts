import { Component, Prop, Vue } from 'vue-property-decorator';
import singleCard from '@/entities/new-learning-center/aside/single-card/single-card.vue';

@Component({
  components: {
    singleCard: singleCard,
  },
})
export default class listCardGroup extends Vue {
  @Prop(Boolean) readonly showList: false | any;
  @Prop(Array) readonly listData: [] | any;

  backToGroup() {
    this.$emit('backToList', false);
  }
}
