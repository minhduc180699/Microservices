package com.saltlux.deepsignal.web.service.impl;

import com.saltlux.deepsignal.web.api.errors.BadRequestException;
import com.saltlux.deepsignal.web.domain.Memo;
import com.saltlux.deepsignal.web.domain.User;
import com.saltlux.deepsignal.web.repository.MemoRepository;
import com.saltlux.deepsignal.web.repository.UserRepository;
import com.saltlux.deepsignal.web.security.SecurityUtils;
import com.saltlux.deepsignal.web.service.IMemoService;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MemoServiceImpl implements IMemoService {

    private final MemoRepository memoRepository;

    private final UserRepository userRepository;

    public MemoServiceImpl(MemoRepository memoRepository, UserRepository userRepository) {
        this.memoRepository = memoRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Memo> findAll() {
        return memoRepository.findAll();
    }

    @Override
    public Optional<Memo> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    @Transactional
    public Memo save(Memo memo) {
        //        if (Objects.nonNull(memo.getId())) {
        //            memo.setCreatedDate(Instant.now());
        //        }
        Optional<User> user = SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneWithAuthoritiesByLogin);
        if (!user.isPresent()) {
            throw new BadRequestException("Save Failed! User not exist");
        } else {
            memo.setUser(user.get());
        }
        return memoRepository.save(memo);
    }

    @Override
    public void remove(Long id) {
        memoRepository.deleteById(id);
    }

    @Override
    public Page<Memo> findMemosByUserIdAndFeedId(String userId, String feedId, Pageable pageable) {
        Page<Memo> memosList = memoRepository.findMemosByUserIdAndFeedId(userId, feedId, pageable);
        return memosList;
    }
}
