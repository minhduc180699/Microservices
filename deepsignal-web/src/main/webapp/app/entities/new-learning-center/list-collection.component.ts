import { Component, Vue } from 'vue-property-decorator';
import learningCenterAside from '@/entities/new-learning-center/aside/learning-center-aside.vue';

@Component({
  name: 'new-learning-center',
  components: {
    learningCenterAside: learningCenterAside,
  },
})
export default class ListCollectionComponent extends Vue {
  mounted(): void {
    document.body.setAttribute('data-menu', 'connectome');
  }
}
