package com.saltlux.deepsignal.web.service;

import com.saltlux.deepsignal.web.domain.FavoriteKeyword;
import java.util.List;

public interface IFavoriteKeywordService extends GeneralService<FavoriteKeyword, Long> {
    List<FavoriteKeyword> findAllByUserId(String userId);

    void deleteByUserId(String userId);
}
