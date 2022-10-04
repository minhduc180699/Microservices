package com.saltlux.deepsignal.web.repository;

import com.saltlux.deepsignal.web.domain.ChromeBookmarkTrained;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChromeBookmarkTrainedRepository extends JpaRepository<ChromeBookmarkTrained, Integer> {
    List<ChromeBookmarkTrained> findAllByUserId(String userId);
    ChromeBookmarkTrained findByPathAndUserId(String path, String userId);
}
