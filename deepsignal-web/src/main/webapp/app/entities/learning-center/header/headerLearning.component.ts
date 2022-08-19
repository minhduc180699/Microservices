import { Component, Vue } from 'vue-property-decorator';
@Component
export default class headerLearning extends Vue {
  closeModal() {
    this.$emit('closeModal');
  }
}
