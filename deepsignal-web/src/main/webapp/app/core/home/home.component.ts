// import Component from 'vue-class-component';
import { Component, Inject, Vue } from 'vue-property-decorator';
import LoginService from '@/account/login.service';
import DsFeed from '@/core/home/feed/ds-feed.vue';

@Component({
  components: {
    'ds-feed': DsFeed,
  },
})
export default class Home extends Vue {
  @Inject('loginService')
  private loginService: () => LoginService;

  public openLogin(): void {
    this.loginService().openLogin((<any>this).$root);
  }

  public get authenticated(): boolean {
    return this.$store.getters.authenticated;
  }

  public get username(): string {
    return this.$store.getters.account ? this.$store.getters.account.login : '';
  }

  mounted() {
    document.body.setAttribute('data-menu', 'feed');

    //유용하지 않아요 show/hide
    // const notUsefulHtml = $('#js-not-useful').html();
    // const notUsefulHtml2 = $('#js-not-useful2').html();
    // $('.item .btn-close').on('click', function () {
    //   $(this).closest('.item').append(notUsefulHtml);
    // });
    // $(document).on('click', '#js-btn-cancel, #js-btn-cancel2', function () {
    //   $(this).closest('.item').find('.not-useful').remove();
    // });
    // $(document).on('click', '#js-btn-no-interest, #js-btn-no-like', function (e) {
    //   e.preventDefault();
    //   $(this).closest('.item').append(notUsefulHtml2);
    // });
    //
    // //메뉴 더보기 show/hide
    // const $moreMenu = $('#js-more-menu');
    // $(document).on('click', '[aria-label="more"]', function () {
    //   const $myItem = $(this).closest('.item');
    //   //hide
    //   if ($myItem.hasClass('show-menu')) {
    //     $('.item-list .item').removeClass('show-menu');
    //     $moreMenu.hide();
    //   }
    //   //show
    //   else {
    //     const moreMenuWidth = $moreMenu.outerWidth();
    //     const moreMenuHeight = $moreMenu.outerHeight();
    //     const moreOffsetTop = $myItem.find('[aria-label="more"]').position().top;
    //     const moreOffsetLeft = $myItem.find('[aria-label="more"]').position().left;
    //     const itemOffsetTop = $myItem.offset().top;
    //     const itemOffsetLeft = $myItem.offset().left;
    //     $('.item-list .item').removeClass('show-menu');
    //     $('#js-more-menu li').removeClass('show');
    //     //console.log(targetOffsetBottom, targetOffsetRight, itemHeight);
    //     $moreMenu.css({
    //       top: itemOffsetTop + moreOffsetTop - moreMenuHeight - 12,
    //       left: itemOffsetLeft + moreOffsetLeft - moreMenuWidth + 34,
    //     });
    //     $myItem.addClass('show-menu');
    //     $moreMenu.show();
    //   }
    // });
    //
    // function closeMoreMenu(e) {
    //   //바깥 클릭시 메뉴 더보기 hide
    //   e.preventDefault();
    //   if ($moreMenu.has(e.target).length === 0 && $('[aria-label="more"]').has(e.target).length === 0) {
    //     $('.item-list .item').removeClass('show-menu');
    //     $moreMenu.hide();
    //   }
    // }
    // $(document).on('mouseup', closeMoreMenu);
  }
}
