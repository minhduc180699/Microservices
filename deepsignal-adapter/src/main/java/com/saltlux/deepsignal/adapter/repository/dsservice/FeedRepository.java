package com.saltlux.deepsignal.adapter.repository.dsservice;

import com.saltlux.deepsignal.adapter.domain.ConnectomeFeed;
import com.saltlux.deepsignal.adapter.domain.Feed;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedRepository extends MongoRepository<Feed, ObjectId> {
    Page<Feed> findByConnectomeId(Pageable var1, String connectomeId);

    @Query(
        "{$and : [ {connectomeId : ?0}, {$or : [ {title : { $regex: ?1, $options: 'i' }}, {content : { $regex: ?1, $options: 'i' }} ]} ]}"
    )
    Page<Feed> findByConnectomeIdAndTitleOrContentContaining(Pageable var1, String connectomeId, String keyword);

    long countAllByConnectomeId(String connectomeId);

    Page<Feed> findByConnectomeIdAndTopicSimsTopic(Pageable var1, String connectomeId, String topic);

    Page<Feed> findByConnectomeIdAndTopicSimsTopicNot(Pageable var1, String connectomeId, String topic);

    List<Feed> findAllByConnectomeIdAndLangAndRecommendDateGreaterThanEqual(String connectomeId, String lang, Instant recommendDate);
    long countByConnectomeIdAndRecommendDateGreaterThanEqual(String connectomeId, Instant recommendDate);

    Optional<Feed> findByConnectomeIdAndDocId(String connectomeId, String docId);

    List<Feed> findFeedByIdInAndDeletedNot(List<ObjectId> ids, Boolean isDeleted);
    List<Feed> findAllByIdIn(List<ObjectId> ids);
}
