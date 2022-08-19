import { Component, Prop, Vue } from 'vue-property-decorator';
import FeedCommentComponent from '@/core/home/detail/feed-comment/feed-comment.vue';

@Component({
  components: {},
})
export default class DetailModuleTitle extends Vue {
  @Prop(String) readonly feedId: any | undefined;
}
