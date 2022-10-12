import { Component, Vue } from 'vue-property-decorator';
import addHome from '@/entities/new-learning-center/add-collection/add/add-home.vue';
import collection from '@/entities/new-learning-center/add-collection/collection/collection.vue';

@Component({
  components: {
    addHome: addHome,
    collection: collection,
  },
})
export default class addCollection extends Vue {
  mounted() {
    document.body.setAttribute('data-menu', 'connectome');
  }
}
