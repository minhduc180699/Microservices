import { Prop, Vue, Watch } from 'vue-property-decorator';
import Component from 'vue-class-component';
import any = jasmine.any;

@Component
export default class Pagination extends Vue {
  @Prop(Number) readonly total: 1 | any;
  private firstPageVal = true;
  private previousPageVal = true;
  private nextPageVal = true;
  public lastPageVal = true;
  private page = 1;
  private prev = false;
  private next = false;

  created() {
    console.log('total', this.total);
    if (this.total > 2) {
      this.next = true;
    }
    if (this.total == 1) {
      this.lastPageVal = false;
      this.nextPageVal = false;
    }
    this.firstPageVal = false;
    this.previousPageVal = false;
    $('#page1').addClass('active');
  }

  public firstPage(e) {
    e.preventDefault();
    if (this.page > 1) {
      this.page = 1;
      $('.page-item').removeClass('active');
      $('#page' + 1).addClass('active');
      //   ---> Call api here
      this.$emit('pagination', this.page);

      this.previousPageVal = false;
      this.firstPageVal = false;
      this.nextPageVal = true;
      this.lastPageVal = true;

      if (this.page >= 2) {
        this.prev = true;
      } else {
        this.prev = false;
      }
      if (this.page >= this.total - 2) {
        this.next = false;
      } else {
        this.next = true;
      }
      if (this.page == 1) {
        if (this.page < this.total - 1) {
          this.next = true;
        }
      }
      $('#firstPage').addClass('disabled');
    }
  }

  public lastPage(e) {
    e.preventDefault();
    if (this.page < this.total) {
      this.page = this.total;
      $('.page-item').removeClass('active');
      $('#page' + this.total).addClass('active');
      //  ---> Call api here
      this.$emit('pagination', this.page);
      this.previousPageVal = true;
      this.firstPageVal = true;
      this.nextPageVal = false;
      this.lastPageVal = false;
      if (this.page >= 3) {
        this.prev = true;
      } else {
        this.prev = false;
      }
      if (this.page >= this.total - 1) {
        this.next = false;
      } else {
        this.next = true;
      }
      $('#lastPage').addClass('disabled');
    }
  }

  public nextPage(e) {
    $('.page-item').removeClass('active');
    e.preventDefault();
    if (this.page < this.total) {
      this.page = this.page + 1;
      $('#page' + this.page).addClass('active');
      // ---> Call api here
      this.$emit('pagination', this.page);
    } else {
      $('#page' + this.total).addClass('active');
    }

    if (this.page > 1) {
      this.firstPageVal = true;
      this.previousPageVal = true;
    }
    if (this.page == this.total) {
      this.lastPageVal = false;
      this.nextPageVal = false;
    }

    if (this.page >= 3) {
      this.prev = true;
    } else {
      this.prev = false;
    }
    if (this.page >= this.total - 1) {
      this.next = false;
    } else {
      this.next = true;
    }
  }

  public previousPage(e) {
    e.preventDefault();
    $('.page-item').removeClass('active');
    if (this.page > 1) {
      this.page = this.page - 1;
      $('#page' + this.page).addClass('active');

      if (this.page > 1) {
        this.previousPageVal = true;
        this.firstPageVal = true;
      } else {
        this.previousPageVal = false;
        this.firstPageVal = false;
      }
      this.lastPageVal = true;
      this.nextPageVal = true;
      //---> Call api here
      this.$emit('pagination', this.page);
    } else {
      $('#page' + 1).addClass('active');
    }
    if (this.page >= 3) {
      this.prev = true;
    } else {
      this.prev = false;
    }
    if (this.page >= this.total - 1) {
      this.next = false;
    } else {
      this.next = true;
    }
  }

  public changePage(page, e) {
    e.preventDefault();
    $('.page-item').removeClass('active');
    $('#page' + page).addClass('active');

    if (page != this.page) {
      if (page > 1) {
        this.previousPageVal = true;
        this.firstPageVal = true;
      } else {
        this.previousPageVal = false;
        this.firstPageVal = false;
      }

      if (page < this.total) {
        this.lastPageVal = true;
        this.nextPageVal = true;
      } else {
        this.nextPageVal = false;
        this.lastPageVal = false;
      }

      if (page == this.page + 1) {
        this.page = this.page + 1;
      }
      if (page == this.page - 1 && page > 1) {
        this.page = this.page - 1;
      }
      if (this.page >= 3) {
        this.prev = true;
      } else {
        this.prev = false;
      }
      if (this.page >= this.total - 1) {
        this.next = false;
      } else {
        this.next = true;
      }
      if (page == 1) {
        this.page = 1;
        if (this.page < this.total - 1) {
          this.next = true;
        }
      }
      //---> Call api here
      this.$emit('pagination', this.page);
    }
  }
}
