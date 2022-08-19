import { Vue } from 'vue-property-decorator';
import Component from 'vue-class-component';

@Component
export default class learningSocial extends Vue {
  private calendar = false;

  clickButtonSearch(e) {
    e.preventDefault();
    $('.item-search').addClass('show');
  }

  clickButtonCalendar(e) {
    this.calendar = !this.calendar;
    e.preventDefault();
    if (this.calendar) {
      $('.item-search-calendar').addClass('show');
      $('.b-calendar-grid-help').css('display', 'none');
    } else {
      $('.item-search-calendar').removeClass('show');
    }
  }

  //tạm thời, trước khi thêm tính năng search
  mouseUp(e) {
    e.preventDefault();
    $('.item-search').removeClass('show');
    // $('.item-search-calendar').removeClass('show')
  }

  mounted() {
    //
  }
}
