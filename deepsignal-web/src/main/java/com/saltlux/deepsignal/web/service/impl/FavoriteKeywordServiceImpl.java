package com.saltlux.deepsignal.web.service.impl;

import com.saltlux.deepsignal.web.api.errors.BadRequestException;
import com.saltlux.deepsignal.web.domain.FavoriteKeyword;
import com.saltlux.deepsignal.web.domain.User;
import com.saltlux.deepsignal.web.repository.FavoriteKeywordRepository;
import com.saltlux.deepsignal.web.repository.UserRepository;
import com.saltlux.deepsignal.web.service.IFavoriteKeywordService;
import com.saltlux.deepsignal.web.service.dto.FavoriteKeywordDTO;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FavoriteKeywordServiceImpl implements IFavoriteKeywordService {

    private FavoriteKeywordRepository favoriteKeywordRepository;

    private UserRepository userRepository;

    public FavoriteKeywordServiceImpl(FavoriteKeywordRepository favoriteKeywordRepository, UserRepository userRepository) {
        this.favoriteKeywordRepository = favoriteKeywordRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<FavoriteKeyword> findAll() {
        return favoriteKeywordRepository.findAll();
    }

    @Override
    public Optional<FavoriteKeyword> findById(Long id) {
        return favoriteKeywordRepository.findById(id);
    }

    @Override
    @Transactional
    public FavoriteKeyword save(FavoriteKeyword favoriteKeyword) {
        try {
            User user = userRepository.findById(favoriteKeyword.getUserId()).get();

            FavoriteKeyword oldKeyword = favoriteKeywordRepository.findByContentAndUser_Id(favoriteKeyword.getContent(), user.getId());
            if (oldKeyword != null) {
                oldKeyword.setLastModifiedDate(Instant.now());
                return favoriteKeywordRepository.save(oldKeyword);
            } else {
                favoriteKeyword.setUser(user);
                return favoriteKeywordRepository.save(favoriteKeyword);
            }
        } catch (NoSuchElementException ex) {
            throw new BadRequestException("Invalid User Id");
        }
    }

    @Override
    public void remove(Long id) {
        favoriteKeywordRepository.deleteById(id);
    }

    @Override
    public List<FavoriteKeyword> findAllByUserId(String userId) {
        return favoriteKeywordRepository.findAllByUser_Id(userId, Sort.by(Sort.Direction.DESC, "lastModifiedDate"));
    }

    @Override
    @Transactional
    public void deleteByUserId(String userId) {
        favoriteKeywordRepository.deleteByUser_Id(userId);
    }
}
