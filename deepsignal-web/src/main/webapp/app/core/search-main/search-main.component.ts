import { Component, Inject, Prop, Vue } from 'vue-property-decorator';
import { RecentSearchService } from '@/service/recent-search.service';
import { PrincipalService } from '@/service/principal.service';
import { isJwtExpired } from 'jwt-check-expiration/src/index';
import axios from 'axios';
import { DATA_FAKE } from '@/shared/constants/ds-constants';
import { namespace } from 'vuex-class';
import { ConnectomeNetworkVertex } from '@/shared/model/connectome-network-vertex.model';

const networkStore = namespace('connectomeNetworkStore');

@Component({
  watch: {
    $route(value) {
      this.searchKeyword = '';
    },
  },
})
export default class SearchMain extends Vue {
  @Inject('recentSearchService') private recentSearchService: () => RecentSearchService;
  @Inject('principalService') private principalService: () => PrincipalService;
  @Prop(Boolean) readonly isFavoriteShow: any | false;

  private isFavorites = false;
  private userId;
  public searchKeyword = '';
  private isShowSearch = true;
  public recentSearches;
  private recentSearchesClone: Readonly<[any]>;
  public isMoving = false;
  public favoriteKeywords = [];
  public loading = false;
  public isActiveStar = true;
  dataFakes = DATA_FAKE;

  @networkStore.Getter
  public favorites!: Array<{ label: string; favoriteId: string; lastModifiedDate: string }>;

  @networkStore.Getter
  public vertices!: Array<ConnectomeNetworkVertex>;

  data() {
    return {
      recentSearches: this.recentSearches,
    };
  }

  created() {
    if (this.principalService().getUserInfo()) {
      this.userId = this.principalService().getUserInfo().id;
    }
    this.$root.$on('clear-keyword', () => {
      this.searchKeyword = '';
      this.recentSearches = this.filterRecentSearchByKeyword(this.searchKeyword);
    });
  }

  destroyed(): void {
    this.$root.$off('clear-keyword');
  }

  public onFocusIn() {
    this.getAllRecentSearch();
  }

  public onFocusOut() {
    if (this.searchKeyword.length < 1) {
      $('.navbar-collapse').removeClass('show');
      this.isShowSearch = true;
      this.$emit('focusOut', this.isShowSearch);
    }
    if (!this.isMoving) {
      //
    }
  }

  public onInput() {
    this.recentSearches = this.filterRecentSearchByKeyword(this.searchKeyword);
  }

  onEnter() {
    if (this.$route.name != 'Feed') {
      this.$root.$emit('search-feed', true);
      return;
    }
    this.searchKeyword = String.prototype.trimAllSpace(this.searchKeyword);
    if (this.searchKeyword) {
      this.saveRecentSearch();
    }
    this.$root.$emit('search-feed');
  }

  closeFavorite() {
    if (!this.isMoving) {
      this.isFavorites = false;
    }
    document.getElementsByClassName('btn-favorite')[0].classList.remove('active');
  }

  public saveRecentSearch() {
    const recentSearchModel = {
      content: this.searchKeyword,
      userId: this.userId,
    };
    this.recentSearchService()
      .save(recentSearchModel)
      .then(res => {
        this.getAllRecentSearch(true);
      })
      .catch(err => {
        console.log('err save ', err);
        const token = sessionStorage.getItem('ds-authenticationToken') || localStorage.getItem('ds-authenticationToken');
        if (token) {
          if (isJwtExpired(token)) {
            history.replaceState({}, document.title, location.href.substr(0, location.href.length - location.hash.length));
            document.location.href = '/login';
          }
        } else {
          history.replaceState({}, document.title, location.href.substr(0, location.href.length - location.hash.length));
          document.location.href = '/login';
        }
      });
  }

  getAllRecentSearch(mustLoadAgain?: boolean) {
    if (this.recentSearchesClone && this.recentSearchesClone.length > 0 && !mustLoadAgain) {
      return;
    }
    this.loading = true;
    this.recentSearchService()
      .getAll(this.userId)
      .then(res => {
        if (!res || !res.data) {
          return;
        }
        this.recentSearchesClone = res.data;
        this.loading = false;
        this.recentSearches = this.filterRecentSearchByKeyword(this.searchKeyword);
      })
      .catch(() => (this.loading = false));
  }

  filterRecentSearchByKeyword(keyword) {
    if (keyword == null || !this.recentSearchesClone) {
      return;
    }
    return this.recentSearchesClone.filter(item => String.prototype.includesIgnoreCase(item.content, keyword));
  }

  public choseRecentSearch(keyword) {
    this.searchKeyword = String.prototype.trimAllSpace(keyword.content);
    this.$store.dispatch('mapNetworkStore/clearSelectedEntity');
    this.onEnter();
  }

  public deleteRecentSearchById(id) {
    if (!id) {
      return;
    }
    this.recentSearchService()
      .deleteById(id)
      .then(res => this.getAllRecentSearch(true));
  }

  public deleteAllRecentSearchByUserId() {
    this.recentSearchService()
      .deleteAllByUserId(this.userId)
      .then(res => this.getAllRecentSearch(true));
  }

  public showFavorites(event) {
    event.preventDefault();
    if (!this.isFavorites) {
      $('.btn-favorite').addClass('active');
      this.isFavorites = true;
    } else {
      $('.btn-favorite').removeClass('active');
      this.isFavorites = false;
    }

    // const connectomeId = JSON.parse(localStorage.getItem('ds-connectome')).connectomeId;
  }

  public chooseFavoriteKeyword(keyword) {
    this.isFavorites = false;
    this.searchKeyword = '';
    const connectomeLang = this.$store.getters['connectomeNetworkStore/lang'];
    this.$store.dispatch('mapNetworkStore/setSelectedEntity', { title: keyword.label, srcLang: connectomeLang });
    document.documentElement.scrollTop = 0;
    this.$root.$emit('choose-favorite', keyword.label);
    if (this.$route.name != 'Feed') {
      this.$router.push('/feed');
    }
  }
}
