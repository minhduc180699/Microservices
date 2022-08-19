<template>
  <!--    <div class="collapse navbar-collapse" id="search-main">-->
  <!--      <div class="search-main-wrap">-->
  <!--        <div class="search-inner">-->
  <!--          <input-->
  <!--            class="form-control"-->
  <!--            type="text"-->
  <!--            placeholder=""-->
  <!--            aria-label="search_input"-->
  <!--            v-model="searchKeyword"-->
  <!--            @focusin="onFocusIn"-->
  <!--            @focusout="onFocusOut"-->
  <!--            @input="onInput"-->
  <!--            ref="searchInput"-->
  <!--            v-on:keyup.enter="onEnter"-->
  <!--            v-autotrim-->
  <!--          />-->
  <!--          <button class="btn btn-search" aria-label="search_button" @click="onEnter">-->
  <!--            &lt;!&ndash;                <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill="currentColor" class="bi bi-search" viewBox="0 0 16 16">&ndash;&gt;-->
  <!--            &lt;!&ndash;                  <path&ndash;&gt;-->
  <!--            &lt;!&ndash;                    d="M11.742 10.344a6.5 6.5 0 1 0-1.397 1.398h-.001c.03.04.062.078.098.115l3.85 3.85a1 1 0 0 0 1.415-1.414l-3.85-3.85a1.007 1.007 0 0 0-.115-.1zM12 6.5a5.5 5.5 0 1 1-11 0 5.5 5.5 0 0 1 11 0z"&ndash;&gt;-->
  <!--            &lt;!&ndash;                  />&ndash;&gt;-->
  <!--            &lt;!&ndash;                </svg>&ndash;&gt;-->
  <!--            <i class="bi bi-search"></i>-->
  <!--          </button>-->
  <!--          <button class="btn btn-favorite" aria-label="favorite" @click="showFavorites($event)" @focusout="closeFavorite()" tabindex="-1">-->
  <!--            <i class="bi bi-star-fill"></i>-->
  <!--          </button>-->
  <!--        </div>-->
  <!--        <div-->
  <!--          class="search-keyword search-layer"-->
  <!--          v-if="searchKeywordLayer"-->
  <!--          @mousemove="isMoving = true"-->
  <!--          @mouseout="isMoving = false"-->
  <!--          @focusout="closeSearchLayer()"-->
  <!--          tabindex="-1"-->
  <!--        >-->
  <!--          <div class="layer-header">-->
  <!--            <div class="layer-title">-->
  <!--              <i class="bi bi-clock-history" style="margin-right: 10px"></i>{{ $t('search-layer.recent-search') }}-->
  <!--            </div>-->
  <!--            <div class="layer-elements">-->
  <!--              <a-->
  <!--                v-show="recentSearches && recentSearches.length > 0"-->
  <!--                type="button"-->
  <!--                class="btn btn-link btn-allclose text-link"-->
  <!--                v-text="$t('search-layer.delete-all')"-->
  <!--                @click="deleteAllRecentSearchByUserId"-->
  <!--              >-->
  <!--              </a>-->
  <!--            </div>-->
  <!--          </div>-->
  <!--          <div class="layer-body">-->
  <!--            <ul v-if="!loading" class="search-keyword-list history">-->
  <!--              <li v-for="keyword of recentSearches" :key="keyword.id">-->
  <!--                <a class="keword" @click="choseRecentSearch(keyword)" v-if="!loading">{{ keyword.content }}</a>-->
  <!--                <span class="date">{{ keyword.lastModifiedDate | formatDate('YYYY-MM-DD') }}</span>-->
  <!--                <button type="button" class="btn btn-close" @click="deleteRecentSearchById(keyword.id)">-->
  <!--                  <i class="icon-history-del"></i>-->
  <!--                </button>-->
  <!--              </li>-->
  <!--            </ul>-->
  <!--            <ul v-if="loading" class="search-keyword-list history">-->
  <!--              <li v-for="item of dataFakes" :key="item.key">-->
  <!--                <a class="keword"><b-skeleton animation="wave" width="70%"></b-skeleton></a>-->
  <!--                <button type="button" class="btn btn-close"><i class="icon-history-del"></i></button>-->
  <!--              </li>-->
  <!--            </ul>-->
  <!--            <div class="no-content" v-show="recentSearches && recentSearches.length === 0">-->
  <!--              &lt;!&ndash; 최근 검색어가 없는 경우 &ndash;&gt;-->
  <!--              <strong>{{ $t('search.noResultRecent') }}</strong>-->
  <!--            </div>-->
  <!--          </div>-->
  <!--          <div class="layer-footer">-->
  <!--            <button type="button" class="btn btn-link btn-close text-link" v-text="$t('search.close')" @click="closeSearchLayer"></button>-->
  <!--          </div>-->
  <!--        </div>-->

  <!--        <div-->
  <!--          class="search-layer search-favorite"-->
  <!--          v-if="isFavorites"-->
  <!--          @mousemove="isMoving = true"-->
  <!--          @mouseout="isMoving = false"-->
  <!--          @focusout="closeFavorite()"-->
  <!--          tabindex="-1"-->
  <!--        >-->
  <!--          <div class="layer-header">-->
  <!--            <div class="layer-title"><i class="bi bi-star"></i>{{ $t('favorite-search.favorites') }}</div>-->
  <!--            <div class="layer-elements">-->
  <!--              <button type="button" class="btn btn-link btn-close text-link">-->
  <!--                {{ $t('favorite-search.setting') }}<i class="bi bi-chevron-right"></i>-->
  <!--              </button>-->
  <!--            </div>-->
  <!--          </div>-->
  <!--          <div class="layer-body">-->
  <!--            <div class="overflow-y-scroll">-->
  <!--              &lt;!&ndash;                  <ul class="search-keyword-list favorites">&ndash;&gt;-->
  <!--              <ul class="search-keyword-list favorites">-->
  <!--                <li v-for="key in vertices.filter(elem => elem.favorite)" :key="key.favorite">-->
  <!--                  <a class="keword pl-3" @click="chooseFavoriteKeyword(key)">{{ key.label }}</a>-->
  <!--                </li>-->
  <!--              </ul>-->
  <!--            </div>-->
  <!--            <div class="no-content" style="display: none">-->
  <!--              &lt;!&ndash; 즐겨찾기 없는 경우 &ndash;&gt;-->
  <!--              <strong>즐겨찾기하신 목록이 없네요.</strong>-->
  <!--              <p><b>My Connectome</b>에서 <br />자주 찾으시는 <b>Topic</b>을 즐겨찾기 하시면 <br />빠르게 검색하실 수 있어요.</p>-->
  <!--            </div>-->
  <!--          </div>-->
  <!--          <div class="layer-footer">-->
  <!--            <button type="button" class="btn btn-link btn-close text-link" v-text="$t('search.close')" @click="isFavorites = false"></button>-->
  <!--          </div>-->
  <!--        </div>-->

  <!--      </div>-->
  <!--    </div>-->
  <!--  -->

  <!-- +++++++++++ Search input  ++++++++++++++-->

  <!--  <div class="search-group search-sm-gray">-->
  <!--    <input-->
  <!--      class="search-input form-control"-->
  <!--      v-model="searchKeyword"-->
  <!--      type="search"-->
  <!--      :placeholder="$t('feed.placeholder-search-input')"-->
  <!--      v-on:keyup.enter="onEnter"-->
  <!--    />-->
  <!--    <button class="search-clear" type="button" title="삭제" style="display: none"></button>-->
  <!--  </div>-->

  <div class="dropdown-menu">
    <div class="util-content favorites-content">
      <div class="top-bar">
        <strong class="top-title" v-text="$t('favorites.favorites')"></strong>
      </div>
      <div class="list-wrap overflow-y-scroll">
        <ul class="search-keyword-list favorites">
          <li v-for="key in vertices.filter(elem => elem.favorite)" :key="key.label">
            <a class="keword" @click="chooseFavoriteKeyword(key)"
              >{{ key.label }}<b-icon icon="star-fill" variant="warning" style="margin-left: 0.5rem"></b-icon
            ></a>
          </li>
        </ul>
      </div>
      <!-- <div class="btn-area">
        <a class="btn btn-block" v-text="$t('favorites.all-favorites')"><i class="icon-common icon-arrow-right"></i></a>
      </div> -->
    </div>
  </div>
</template>
<script lang="ts" src="./search-main.component.ts"></script>
