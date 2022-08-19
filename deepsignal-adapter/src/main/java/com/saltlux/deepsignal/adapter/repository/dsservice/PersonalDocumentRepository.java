package com.saltlux.deepsignal.adapter.repository.dsservice;

import com.saltlux.deepsignal.adapter.domain.PersonalDocument;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonalDocumentRepository extends MongoRepository<PersonalDocument, ObjectId> {
    Page<PersonalDocument> findByConnectomeIdAndTypeAndIsDeleteNot(String connectomeId, String type, int isDelete, Pageable pageable);

    Page<PersonalDocument> findByConnectomeIdAndIsDeleteNot(String connectomeId, int isDelete, Pageable pageable);

    //    Page<PersonalDocument> findByConnectomeIdAndIsDelete(String connectomeId, boolean isDelete, Pageable pageable);

    Page<PersonalDocument> findByConnectomeIdAndLanguageAndDeleted(
        String connectomeId,
        String language,
        boolean deleted,
        Pageable pageable
    );

    Page<PersonalDocument> findByConnectomeIdAndLanguageAndLikedEquals(String connectomeId, String language, int liked, Pageable pageable);

    Page<PersonalDocument> findByConnectomeIdAndLanguageAndMemo(String connectomeId, String language, int memo, Pageable pageable);

    Page<PersonalDocument> findByConnectomeIdAndLanguageAndBookmarked(
        String connectomeId,
        String language,
        boolean bookmarked,
        Pageable pageable
    );

    void deleteByIdIn(List<String> docIds);

    List<PersonalDocument> findPersonalDocumentByIdInAndDeletedNot(List<ObjectId> ids, Boolean isDeleted);
    List<PersonalDocument> findAllByIdIn(List<ObjectId> ids);
}
