import { Vue } from 'vue-property-decorator';
import Component from 'vue-class-component';
import axios from 'axios';

@Component
export default class learningWeb extends Vue {
  private calendar = false;
  private list = [];
  private local: any;
  private dataList = 1;
  private total = 1;
  private page = 1;
  private prev = false;
  private next = false;
  private lastPageVal = true;
  private nextPageVal = true;
  private previousPageVal = true;
  private firstPageVal = true;
  private fileName = '';
  private id = 0;

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

  getApi() {
    axios
      .get('api/file-storage/getAllUrl/' + this.local.user.id + '/' + this.page)
      .then(res => {
        this.list = res.data;
        this.dataList = this.list.length;
        this.total = this.list[0].totalPage;
      })
      .catch(error => {
        const err = error.response.data;
        this.$bvToast.toast(err.message, {
          toaster: 'b-toaster-bottom-right',
          title: err.errorKey,
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
      });
  }

  async mounted() {
    if (localStorage.getItem('ds-connectome')) {
      this.local = JSON.parse(localStorage.getItem('ds-connectome'));
    } else {
      if (sessionStorage.getItem('ds-connectome')) {
        this.local = JSON.parse(sessionStorage.getItem('ds-connectome'));
      }
    }
    axios
      .get('api/file-storage/getAllUrl/' + this.local.user.id + '/' + this.page)
      .then(res => {
        this.list = res.data;
        this.dataList = this.list.length;
        this.total = this.list[0].totalPage;
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
      })
      .catch(error => {
        const err = error.response.data;
        this.$bvToast.toast(err.message, {
          toaster: 'b-toaster-bottom-right',
          title: err.errorKey,
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
      });
  }

  deleteFile(value) {
    this.$bvModal.show('deleteData');
    this.fileName = value.name;
    this.id = value.id;
  }

  deleteOk() {
    if (this.id) {
      axios
        .delete('api/file-storage/deleteData/' + this.id)
        .then(res => {
          this.$bvToast.show('toastSuccess');
          this.getApi();
          this.$bvToast.show('toastSuccess');
        })
        .catch(error => {
          const err = error.response.data;
          this.$bvToast.toast(err.message, {
            toaster: 'b-toaster-bottom-right',
            title: err.errorKey,
            variant: 'danger',
            solid: true,
            autoHideDelay: 5000,
          });
        });
    } else {
      this.$bvToast.show('toastFailure');
    }
  }

  changePage(page, e) {
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
      this.getApi();
    }
  }

  previousPage(e) {
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
      this.getApi();
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

  nextPage(e) {
    $('.page-item').removeClass('active');
    e.preventDefault();
    if (this.page < this.total) {
      this.page = this.page + 1;
      $('#page' + this.page).addClass('active');
      this.getApi();
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

  lastPage(e) {
    e.preventDefault();
    if (this.page < this.total) {
      this.page = this.total;
      $('.page-item').removeClass('active');
      $('#page' + this.total).addClass('active');
      this.getApi();
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

  firstPage(e) {
    e.preventDefault();
    if (this.page > 1) {
      this.page = 1;
      $('.page-item').removeClass('active');
      $('#page' + 1).addClass('active');
      this.getApi();

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

  delateAll() {
    this.$bvModal.show('deleteAllData');
  }

  deleteAllOk() {
    axios
      .delete('api/file-storage/deleteAll/' + this.local.user.id + '/URL')
      .then(res => {
        this.getApi();
      })
      .catch(error => {
        const err = error.response.data;
        this.$bvToast.toast(err.message, {
          toaster: 'b-toaster-bottom-right',
          title: err.errorKey,
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
      });
  }
}
