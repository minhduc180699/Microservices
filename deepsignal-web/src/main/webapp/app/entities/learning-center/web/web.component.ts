// import { Component, Prop, Vue } from 'vue-property-decorator';
//
// @Component
// export default class web extends Vue {
//   @Prop(Number) readonly display;
//
//   mounted() {
//     $('#learning-center').on('scroll', () => {
//       $('html').addClass('is-scrolled');
//     });
//   }
// }
import { Inject, Vue, Prop } from 'vue-property-decorator';
import Component from 'vue-class-component';
import PublicService from '@/service/public.service';

@Component
export default class web extends Vue {
  @Prop(Number) readonly display;
  searchValue;
  @Inject('publicService')
  private publicService: () => PublicService;
  previewModels;
  loading = false;
  numberOfWeb = 0;
  isSelected = [];
  isSelectAll = false;

  data(): any {
    return {
      searchValue: this.searchValue,
      previewModels: this.previewModels,
      loading: this.loading,
    };
  }

  created() {
    this.$parent.$on('uncheckedItemWeb', this.uncheckedItemWeb);
  }

  mounted() {
    // $('#learning-center').on('scroll', () => {
    //   $('html').addClass('is-scrolled');
    // });
    const modal = document.getElementById('learning-center');
    if (modal) {
      modal.addEventListener('scroll', () => {
        if (modal.scrollTop <= 100) {
          document.documentElement.classList.remove('is-scrolled');
        } else {
          document.documentElement.classList.add('is-scrolled');
        }
      });
    }
  }

  toggleSearch(e) {
    e.preventDefault();
    if ($('.favorite-list').css('display') == 'none') {
      $('.favorite-list').css('display', 'block');
    }
  }

  clearToggle(e) {
    e.preventDefault();
    $('.favorite-list').css('display', 'none');
  }

  // pasted(e) {
  //   this.searchValue = e.clipboardData.getData('text');
  //   this.preview();
  // }

  preview() {
    this.loading = true;
    if (!this.previewModels) {
      this.previewModels = [];
    }
    this.publicService()
      .previewInfoByUrl(this.searchValue, JSON.parse(localStorage.getItem('ds-connectome')).connectomeId)
      .then(res => {
        if (this.previewModels.length > 0 && res.data.url && this.previewModels.find(item => item.url == res.data.url)) {
          this.resetPreview();
          return;
        }
        const obj = {
          ...res.data,
          favicon: this.getFavicon(res.data.url),
        };
        this.previewModels.push(obj);
        this.numberOfWeb = this.previewModels.length;
        this.resetPreview();
      })
      .catch(() => this.resetPreview());
  }

  resetPreview() {
    this.loading = false;
    this.searchValue = null;
  }

  selectCard(index) {
    let selected = false;
    this.isSelected.forEach((item, num) => {
      if (index == item) {
        selected = true;
      }
    });
    if (selected) {
      this.$emit('removeItemWeb', this.previewModels[index]);
    } else {
      this.isSelected.push(index);
      this.$emit('showSelectedWeb', this.previewModels[index]);
    }
  }

  selectAll() {
    this.isSelectAll = !this.isSelectAll;
    this.isSelected = [];
    if (this.isSelectAll) {
      this.previewModels.forEach((item, index) => {
        this.isSelected.push(index);
        this.$emit('showSelectedWeb', this.previewModels[index]);
      });
    } else {
      this.previewModels.forEach((item, index) => {
        this.$emit('removeItemWeb', this.previewModels[index]);
      });
    }
  }

  uncheckedItemWeb(item) {
    for (let i = 0; i < this.previewModels.length; i++) {
      if (item.url == this.previewModels[i].url) {
        const index = this.previewModels.indexOf(item);
        const num = this.isSelected.indexOf(index);
        this.isSelected.splice(num, 1);
      }
    }
  }

  getFavicon(url) {
    const domain = new URL(url);
    domain.hostname.replace('www.', '');
    return domain.protocol + '//' + domain.hostname + '/favicon.ico';
  }

  getDateTimeUTC() {
    const date = new Date();
    return (
      date.getUTCFullYear() +
      '-' +
      (date.getUTCMonth().valueOf() + 1) +
      '-' +
      date.getUTCDate() +
      ' ' +
      date.getUTCHours() +
      ':' +
      date.getUTCMinutes()
    );
  }
}
