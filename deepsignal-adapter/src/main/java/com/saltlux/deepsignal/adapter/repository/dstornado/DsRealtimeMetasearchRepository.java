package com.saltlux.deepsignal.adapter.repository.dstornado;

import com.saltlux.deepsignal.adapter.domain.dstornado.DsRealtimeMetaSearch;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DsRealtimeMetasearchRepository extends MongoRepository<DsRealtimeMetaSearch, ObjectId> {
    Page<DsRealtimeMetaSearch> findByKeywordLike(Pageable var1, String keyword);
}
