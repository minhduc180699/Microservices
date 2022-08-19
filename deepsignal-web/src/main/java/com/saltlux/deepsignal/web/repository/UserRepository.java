package com.saltlux.deepsignal.web.repository;

import com.saltlux.deepsignal.web.domain.Authority;
import com.saltlux.deepsignal.web.domain.Connectome;
import com.saltlux.deepsignal.web.domain.Purpose;
import com.saltlux.deepsignal.web.domain.User;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Spring Data JPA repository for the {@link User} entity.
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {
    String USERS_BY_LOGIN_CACHE = "usersByLogin";

    String USERS_BY_EMAIL_CACHE = "usersByEmail";

    Optional<User> findOneByActivationKey(String activationKey);

    List<User> findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(Instant dateTime);

    Optional<User> findOneByResetKey(String resetKey);

    Optional<User> findOneByEmailIgnoreCase(String email);

    Optional<User> findOneByLogin(String login);

    Optional<User> findOneByLoginAndActivated(String login, Integer activated);

    Optional<User> findByIdAndActivated(String id, Integer activated);

    List<User> findAllByIdInAndActivated(List<String> id, Integer activated);

    Optional<User> findOneByPhoneNumber(String phoneNumber);

    @EntityGraph(attributePaths = "authorities")
    @Cacheable(cacheNames = USERS_BY_LOGIN_CACHE)
    Optional<User> findOneWithAuthoritiesByLogin(String login);

    @EntityGraph(attributePaths = "authorities")
    @Cacheable(cacheNames = USERS_BY_EMAIL_CACHE)
    Optional<User> findOneWithAuthoritiesByEmailIgnoreCase(String email);

    List<User> findAllByAuthoritiesIs(Authority authority);

    Page<User> findAllByIdNotNullAndActivatedIsTrue(Pageable pageable);

    List<User> findAllByActivatedAndCreatedDateBefore(Integer activated, Instant dateTime);

    Optional<User> findOneByEmail(String email);

    Boolean existsUserByEmail(String email);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO user_purpose_detail (user_id, purpose_id) VALUES(:userId, :purposeId)", nativeQuery = true)
    void saveUserPurposeDetail(@Param("userId") String userId, @Param("purposeId") String purposeId);

    Page<User> findByEmailContains(String key, Pageable pageable);

    @Query(
        "SELECT u FROM User u WHERE LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%'))" +
        "OR LOWER(u.phoneNumber) LIKE LOWER(CONCAT('%', :keyword, '%'))"
    )
    Page<User> searchUser(@Param("keyword") String keyword, Pageable pageable);

    @Query(
        "SELECT u FROM User u WHERE LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%'))" +
        "OR LOWER(u.phoneNumber) LIKE LOWER(CONCAT('%', :keyword, '%'))"
    )
    List<User> getCountSearchUser(@Param("keyword") String keyword);

    @Transactional
    Integer deleteUserById(String id);
}
