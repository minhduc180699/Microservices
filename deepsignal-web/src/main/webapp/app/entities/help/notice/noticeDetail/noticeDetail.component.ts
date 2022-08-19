import { Vue } from 'vue-property-decorator';
import Component from 'vue-class-component';
import * as events from 'events';

@Component
export default class NoticeDetail extends Vue {
  id = this.$route.params.id;
  isListFile = true;
  even: events;

  toggleFile(e) {
    e.preventDefault();
    this.isListFile = !this.isListFile;
  }
}
