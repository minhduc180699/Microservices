import Vue from 'vue';
import Component from 'vue-class-component';
import DatePicker from 'vue2-datepicker';
import { Prop } from 'vue-property-decorator';
import moment from 'moment';
import { cvLocaldateToUtcDate } from '@/shared/constants/ds-constants';
@Component({
  components: {
    'date-picker': DatePicker,
  },
})
export default class PageTopComponent extends Vue {
  @Prop(Boolean) readonly isCheck: boolean | false;
  dateDefault = moment().format('YYYY.MM.DD');
  today = new Date(moment().format('yyyy.MM.DD hh:mm:ss a'));
  yesterday = new Date(this.today.getTime() - 24 * 60 * 60 * 1000);
  bforeYesterday = new Date(this.today.getTime() - 2 * 24 * 60 * 60 * 1000);
  actived = 1;
  selectedDate(e, tab?, typePicker?) {
    tab ? (this.actived = tab) : (this.actived = -1);
    let res;
    if (typePicker) res = cvLocaldateToUtcDate(new Date(moment(e).format('yyyy.MM.DD') + ' ' + moment().format('hh:mm:ss a')));
    else res = cvLocaldateToUtcDate(e);
    this.$emit('selectedDate', res);
  }
  disableDate(date) {
    return date > new Date();
  }
}
