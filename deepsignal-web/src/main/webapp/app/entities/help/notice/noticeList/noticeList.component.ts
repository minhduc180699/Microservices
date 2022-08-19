import { Vue } from 'vue-property-decorator';
import Component from 'vue-class-component';

@Component({
  name: 'noticeList',
})
export default class NoticeList extends Vue {
  isNoticeList = true;
  isNoticeEmptyList = false;
  isNoticeResultList = false;
  id = 1;
  link = 'notice/' + this.id;
}
