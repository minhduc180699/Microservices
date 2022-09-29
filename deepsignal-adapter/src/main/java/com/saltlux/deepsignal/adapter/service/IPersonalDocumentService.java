package com.saltlux.deepsignal.adapter.service;

import com.saltlux.deepsignal.adapter.config.Constants;
import com.saltlux.deepsignal.adapter.domain.PersonalDocument;
import com.saltlux.deepsignal.adapter.service.dto.FilterFeedDTO;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;

public interface IPersonalDocumentService {
    PersonalDocument findPersonalDocumentByConnectomeIdAndId(String connectomeId, String id);

    Page<PersonalDocument> getPersonalDocumentByConnectomeIdAndType(
        String connectomeId,
        String uploadType,
        int page,
        int size,
        String orderBy,
        String sortDirection
    );

    Page<PersonalDocument> getDeletedPersonalDocuments(String connectomeId, int page, int size, String orderBy, String sortDirection);

    void deleteDocuments(List<String> docIds);

    boolean handleActivity(ObjectId id, Constants.activity type, boolean state, int likeState);

    Page<PersonalDocument> getPersonalDocumentByConnectomeIdAndActivity(
        String connectomeId,
        FilterFeedDTO filterFeedDTO,
        String lang,
        int page,
        int size,
        String orderBy,
        String sortDirection
    );

    Page<PersonalDocument> findPersonalDocumentByConnectomeIdKeywordAndEntityLabelAndFilter(
        int page,
        int size,
        String orderBy,
        String sortDirection,
        String connectomeId,
        String uploadType,
        String keyword,
        String entityLabel,
        List<FilterFeedDTO> filterFeedDTOS
    );

    List<PersonalDocument> findPersonalDocumentsByIds(List<String> ids, Boolean... optionalFlag);

    List<PersonalDocument> findPersonalDocumentsByDocIds(List<String> docIds, Boolean... optionalFlag);
}
