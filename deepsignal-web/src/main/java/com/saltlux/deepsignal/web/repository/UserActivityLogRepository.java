package com.saltlux.deepsignal.web.repository;

import com.saltlux.deepsignal.web.domain.UserActivityLog;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the {@link UserActivityLog} entity.
 */
@Repository
public interface UserActivityLogRepository extends JpaRepository<UserActivityLog, String> {
    List<UserActivityLog> findAllByFeedIdAndActivityName(String feedId, String activityName);

    @Query(
        value = "SELECT * FROM user_activity_log " +
        "WHERE connectome_id = ?1 " +
        "AND JSON_CONTAINS(training_data, ?2) " +
        "AND DATE(logged_time) >= ?3 AND DATE(logged_time) <= ?4 " +
        "AND HOUR(logged_time) = ?5 ",
        nativeQuery = true
    )
    List<UserActivityLog> findUserActivityLogByTrainingKeywordOrType(
        String connectomeId,
        String keyword,
        String dateFrom,
        String dateTo,
        Integer hour
    );

    @Query(
        value = "SELECT * FROM user_activity_log " +
        "WHERE connectome_id = ?1 " +
        "AND DATE(logged_time) >= ?2 AND DATE(logged_time) <= ?3 " +
        "AND HOUR(logged_time) = ?4 " +
        "AND training_data IS NOT NULL ",
        nativeQuery = true
    )
    List<UserActivityLog> findUserActivityLogByConnectomeIdAndLoggedTime(String connectomeId, String dateFrom, String dateTo, Integer hour);

    @Query(
        value = "SELECT * FROM user_activity_log " +
        "WHERE connectome_id = ?1 " +
        "AND JSON_CONTAINS(training_data, ?2) " +
        "AND DATE(logged_time) >= ?3 AND DATE(logged_time) <= ?4",
        nativeQuery = true
    )
    List<UserActivityLog> findUserActivityLogByTrainingKeywordOrType(String connectomeId, String keyword, String dateFrom, String dateTo);

    @Query(
        value = "SELECT * FROM user_activity_log " +
        "WHERE connectome_id = ?1 " +
        "AND DATE(logged_time) >= ?2 AND DATE(logged_time) <= ?3 " +
        "AND training_data IS NOT NULL ",
        nativeQuery = true
    )
    List<UserActivityLog> findUserActivityLogByConnectomeIdAndLoggedTime(String connectomeId, String dateFrom, String dateTo);
}
