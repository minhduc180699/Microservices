const html = document.querySelector('html');
const body = document.querySelector('body');

var Common = {
  init: function () {
    this.scrolling();
    this.event();
    this.search();
    this.moblng();
    window.addEventListener('mousewheel', Common.scrolling);
    window.addEventListener('touchmove', Common.scrolling);
    $(window).scroll(function () {
      Common.scrolling();
    });
  },
  scrolling: function (e) {
    var scrollTop = $(window).scrollTop();
    var gnbTop = $('#header').height();
    gnbTop = Number(gnbTop);

    // scrollbar top check
    if (scrollTop > 50) {
      $('html').addClass('is-scrolled');
    } else {
      $('html').removeClass('is-scrolled');
    }

    // scrollbar bottom check
    if ($(window).scrollTop() + $(window).height() > $(document).height() - 100) {
      $('html').addClass('is-bottom');
    } else {
      $('html').removeClass('is-bottom');
    }

    // mCustomScrollbar
    $(window).on('load', function () {
      $.mCustomScrollbar.defaults.theme = 'minimal-dark'; //set "dark" as the default theme
      // $.mCustomScrollbar.defaults.scrollButtons.enable=true;

      $('.overflow-y-scroll').mCustomScrollbar(); // vertical scrollbar

      $('.overflow-x-scroll').mCustomScrollbar({
        // horizontal scrollbar
        axis: 'x',
        advanced: { autoExpandHorizontalScroll: true },
      });
      $('.overflow-yx-scroll').mCustomScrollbar({
        // vertical and horizontal scrollbar
        axis: 'yx',
      });
    });
  },
  event: function () {
    //tooltip
    $('[data-toggle="tooltip"]').tooltip();
    /* popover */
    $('[data-toggle="popover"]').popover();

    //좋아요
    // $('[aria-label="like"]').on('click', function() {
    // 	if ($(this).hasClass('active')) {
    // 		$(this).removeClass('active');
    // 		$(this).attr('aria-pressed', false)
    // 	} else {
    // 		$(this).addClass('active');
    // 		$(this).attr('aria-pressed', true)
    // 	}
    // });

    //툴팁 메뉴
    $('.tooltip-menu a').on('click', function (e) {
      if ($(this).parent().hasClass('has-treeview')) {
        e.preventDefault();
        $(this).parent().addClass('show');
      }
    });

    //툴팁 버튼
    $('.btn-tooltip-toggler').on('click', function (e) {
      e.preventDefault();
      $(this).toggleClass('active');
    });
    $(document).on('mouseup', function (e) {
      e.preventDefault();
      if (
        $('.btn-tooltip-toggler button').has(e.target).length === 0 &&
        $('.btn-tooltip-toggler .tooltip-menu a').has(e.target).length === 0
      ) {
        $('.btn-tooltip-toggler').removeClass('active');
      }
    });

    //datepicker
    $('.btn-datepicker').datepicker({
      monthNames: ['01', '02', '03', '04', '05', '06', '07', '08', '09', '10', '11', '12'],
      monthNamesShort: ['01', '02', '03', '04', '05', '06', '07', '08', '09', '10', '11', '12'],
      dayNamesMin: ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'],
      showMonthAfterYear: true,
      showOtherMonths: true,
      changeMonth: true,
      changeYear: true,
      dateFormat: 'yy.mm.dd',
      beforeShow: function (input, inst) {
        $('#ui-datepicker-div').addClass('datepicker-wrapper');
      },
    });
    //datepicker
    $('#datepicker, #datepicker2').datepicker({
      monthNames: ['01', '02', '03', '04', '05', '06', '07', '08', '09', '10', '11', '12'],
      monthNamesShort: ['01', '02', '03', '04', '05', '06', '07', '08', '09', '10', '11', '12'],
      dayNamesMin: ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'],
      showMonthAfterYear: true,
      showOtherMonths: true,
      changeMonth: true,
      changeYear: true,
      dateFormat: 'yy.mm.dd',
      beforeShow: function (input, inst) {
        $('#ui-datepicker-div').addClass('datepicker-wrapper');
      },
    });
    $('#datepicker').datepicker({
      onSelect: function (dateText) {
        $('#datepicker2').datepicker('setDate', $(this).datepicker('getDate'));
      },
    });
    $('#datepicker2').datepicker();
    $('#datepicker2').datepicker();
  },
  search: function (e) {
    // input 텍스트 입력시 [X] 버튼
    var $searchInput = $('.search-input'),
      $searchClear = $('.search-clear');
    $searchInput.keyup(function () {
      $('.search-clear').toggle(Boolean($(this).val()));
    });
    $searchClear.toggle(Boolean($searchInput.val()));
    $searchClear.click(function () {
      $('.search-input').val('').focus();
      $(this).hide();
    });

    // 최근 검색어 & 인기검색어
    $('.search-main-wrap input')
      .on('focus', function () {
        if ($(this).val().length > 0) {
          $(this).addClass('have-value');
          $('.search-keyword').hide();
          $('.search-autocomplete').show();
        } else {
          $('.search-keyword').show();
          $('.search-autocomplete').hide();
        }
      })
      .on('blur', function () {
        $(this).removeClass('have-value');
        $('.search-keyword').hide();
        $('.search-autocomplete').hide();
      });

    // 검색어 자동완성
    $('.search-main-wrap input').on('propertychange change keyup paste input', function () {
      if ($(this).val().length > 0) {
        $(this).addClass('have-value');
        $('.search-keyword').hide();
        $('.search-autocomplete').show();
      } else {
        $(this).removeClass('have-value');
        $('.search-autocomplete').hide();
      }
    });

    // 즐겨찾기
    $('.btn-favorite').on('click', function (e) {
      e.preventDefault();
      $('.btn-favorite').addClass('active');
      $('.search-favorite').fadeIn(150);
    });
    $(document).on('mouseup', function (e) {
      e.preventDefault();
      if ($('.btn-favorite').has(e.target).length === 0 && $('.search-favorite a').has(e.target).length === 0) {
        $('.search-favorite').fadeOut(100);
        $('.btn-favorite').removeClass('active');
      }
    });
  },
  moblng: function (e) {
    $('.mobile-lnb-layer-btn').on('click', function (e) {
      e.preventDefault();
      $('.mobile-lnb-layer').toggleClass('lnb-show');
      $('.mobile-lnb-layer-btn').toggleClass('active');
    });
  },
};

var Header = {
  init: function () {
    this.util();
    this.sitemap();
    this.alarm();
    this.my();
    this.myinfo();
    this.fullscreen();
  },
  util: function (e) {
    /* 내부 클릭시 드롭다운 메뉴 닫기 방지 (Avoid dropdown menu close on click inside) */
    $('.nav-util .dropdown-menu').click(function (e) {
      e.stopPropagation();
    });
  },
  sitemap: function (e) {
    $('.ds-snb-wrap').on('click', function () {
      $('html').addClass('ds-snb-panel-show');
      $('.ds-snb-panel').addClass('active');
    });
    $('#js-btn-sitemap-close, .ds-snb-wrap .dim').on('click', function () {
      $('html').removeClass('ds-snb-panel-show');
      $('.ds-snb-panel').removeClass('active');
    });
  },
  myinfo: function (e) {
    $('#js-btn-myinfo').on('click', function () {
      //$('html').removeClass('my-layer-show');
      $('html').addClass('myinfo-layer-show');
    });
    $('#js-btn-myinfo-close').on('click', function () {
      $('html').removeClass('myinfo-layer-show');
    });
  },
  fullscreen: function (e) {
    $('.fullscreen > a').on('click', function (e) {
      e.preventDefault();
      if (!$('html').hasClass('is-fullscreen')) {
        openFullScreenMode();
        $('html').addClass('is-fullscreen');
        $(this).find('i').text('fullscreen_exit');
      } else {
        closeFullScreenMode();
        $('html').removeClass('is-fullscreen');
        $(this).find('i').text('fullscreen');
      }
    });
    var docV = document.documentElement;
    //전체화면 설정
    function openFullScreenMode() {
      if (docV.requestFullscreen) docV.requestFullscreen();
      else if (docV.webkitRequestFullscreen) docV.webkitRequestFullscreen();
      else if (docV.mozRequestFullScreen) docV.mozRequestFullScreen();
      else if (docV.msRequestFullscreen) docV.msRequestFullscreen();
    }
    //전체화면 해제
    function closeFullScreenMode() {
      if (document.exitFullscreen) document.exitFullscreen();
      else if (document.webkitExitFullscreen) document.webkitExitFullscreen();
      else if (document.mozCancelFullScreen) document.mozCancelFullScreen();
      else if (document.msExitFullscreen) document.msExitFullscreen();
    }
  },
};

var Sidebar = {
  init: function () {
    this.gnb();
    this.information();
  },
  gnb: function (e) {
    $('.gnb .dep1 > li > a').on('mouseenter focus', function (e) {
      $(this).parent().siblings().find('a').removeClass('hover');
      $(this).addClass('hover');
    });
    $('.gnb').on('mouseleave', function (e) {
      $('.gnb .hover').removeClass('hover');
    });
  },
  information: function (e) {
    var btnInfo = $('.btn-information');
    $(btnInfo).on('click', function (e) {
      $(this).toggleClass('active');
    });
    $(btnInfo).on('focus', function (e) {
      $('.gnb a').removeClass('hover');
    });
    $(document).on('click', function (e) {
      if ($('.btn-information-wrap').has(e.target).length === 0) {
        $(btnInfo).removeClass('active');
      }
    });
  },
};

Common.init();
Header.init();
Sidebar.init();
