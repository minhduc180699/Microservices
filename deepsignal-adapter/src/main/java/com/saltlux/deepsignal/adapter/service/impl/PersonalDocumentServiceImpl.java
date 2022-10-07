package com.saltlux.deepsignal.adapter.service.impl;

import com.saltlux.deepsignal.adapter.config.Constants;
import com.saltlux.deepsignal.adapter.domain.Feed;
import com.saltlux.deepsignal.adapter.domain.PersonalDocument;
import com.saltlux.deepsignal.adapter.repository.dsservice.PersonalDocumentRepository;
import com.saltlux.deepsignal.adapter.service.IPersonalDocumentService;
import com.saltlux.deepsignal.adapter.service.dto.FilterFeedDTO;
import com.saltlux.deepsignal.adapter.util.PersonalDocumentActivity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.ObjectUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PersonalDocumentServiceImpl implements IPersonalDocumentService {

    @Autowired
    private PersonalDocumentRepository personalDocumentRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public PersonalDocument findPersonalDocumentByConnectomeIdAndId(String connectomeId, String id) {
        ObjectId objectId = new ObjectId(id);
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(objectId).and("connectomeId").is(connectomeId));
        return mongoTemplate.findOne(query, PersonalDocument.class);
    }

    @Override
    public Page<PersonalDocument> getPersonalDocumentByConnectomeIdAndType(
        String connectomeId,
        String uploadType,
        int page,
        int size,
        String orderBy,
        String sortDirection
    ) {
        Pageable pageable = PageRequest.of(
            page,
            size,
            "desc".equalsIgnoreCase(sortDirection) ? Sort.Direction.DESC : Sort.Direction.ASC,
            orderBy
        );

        if (uploadType != null && !uploadType.isEmpty()) {
            return personalDocumentRepository.findByConnectomeIdAndTypeAndIsDeleteNot(
                connectomeId,
                uploadType,
                Constants.PERSONAL_DOCUMENT_STATUS.DELETED,
                pageable
            );
        } else {
            return personalDocumentRepository.findByConnectomeIdAndIsDeleteNot(
                connectomeId,
                Constants.PERSONAL_DOCUMENT_STATUS.DELETED,
                pageable
            );
        }
    }

    @Override
    public Page<PersonalDocument> getDeletedPersonalDocuments(
        String connectomeId,
        int page,
        int size,
        String orderBy,
        String sortDirection
    ) {
        Pageable pageable = PageRequest.of(
            page,
            size,
            "desc".equalsIgnoreCase(sortDirection) ? Sort.Direction.DESC : Sort.Direction.ASC,
            orderBy
        );
        return personalDocumentRepository.findByConnectomeIdAndLanguageAndDeleted(
            connectomeId,
            "",
            PersonalDocumentActivity.deleted.getBoolValue(),
            pageable
        );
    }

    @Override
    @Transactional
    public void deleteDocuments(List<String> docIds) {
        personalDocumentRepository.deleteByIdIn(docIds);
    }

    @Override
    public boolean handleActivity(ObjectId id, Constants.activity type, boolean state, int likeState) {
        Optional<PersonalDocument> personalDocument = personalDocumentRepository.findById(id);
        if (personalDocument.isPresent()) {
            Query query = new Query();
            query.addCriteria(Criteria.where("_id").is(id));
            Update update = new Update();
            switch (type) {
                case LIKE:
                    update.set("liked", likeState);
                    break;
                case BOOKMARK:
                    update.set("bookmarked", state);
                    break;
                case DELETE:
                    update.set("deleted", state);
                    break;
            }
            mongoTemplate.updateFirst(query, update, PersonalDocument.class);
            return true;
        }
        return false;
    }

    @Override
    public Page<PersonalDocument> getPersonalDocumentByConnectomeIdAndActivity(
        String connectomeId,
        FilterFeedDTO filterFeedDTO,
        String lang,
        int page,
        int size,
        String orderBy,
        String sortDirection
    ) {
        Pageable pageable = PageRequest.of(
            page,
            size,
            "desc".equalsIgnoreCase(sortDirection) ? Sort.Direction.DESC : Sort.Direction.ASC,
            orderBy
        );
        PersonalDocumentActivity personalDocActivity = PersonalDocumentActivity.valueOf(filterFeedDTO.getField());
        try {
            switch (personalDocActivity) {
                case liked:
                    return personalDocumentRepository.findByConnectomeIdAndLanguageAndLikedEquals(
                        connectomeId,
                        lang,
                        (Integer) filterFeedDTO.getValue(),
                        pageable
                    );
                case memo:
                    return personalDocumentRepository.findByConnectomeIdAndLanguageAndMemo(
                        connectomeId,
                        lang,
                        (Integer) filterFeedDTO.getValue(),
                        pageable
                    );
                case bookmarked:
                    return personalDocumentRepository.findByConnectomeIdAndLanguageAndBookmarked(
                        connectomeId,
                        lang,
                        (Boolean) filterFeedDTO.getValue(),
                        pageable
                    );
                case deleted:
                    return personalDocumentRepository.findByConnectomeIdAndLanguageAndDeleted(
                        connectomeId,
                        lang,
                        (Boolean) filterFeedDTO.getValue(),
                        pageable
                    );
                default:
                    return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Page<PersonalDocument> findPersonalDocumentByConnectomeIdKeywordAndEntityLabelAndFilter(
        int page,
        int size,
        String orderBy,
        String sortDirection,
        String connectomeId,
        String uploadType,
        String keyword,
        String entityLabel,
        List<FilterFeedDTO> filterFeedDTOS
    ) {
        String queryStr = buildQueryFilterPersonalDocument(connectomeId, filterFeedDTOS, uploadType, keyword, entityLabel);
        System.out.println(queryStr);
        return searchPersonalDocument(page, size, orderBy, sortDirection, queryStr);
    }

    private String buildQueryFilterPersonalDocument(
        String connectomeId,
        List<FilterFeedDTO> filterFeedDTOS,
        String uploadType,
        String keyword,
        String entityLabel
    ) {
        String query = "{";
        for (FilterFeedDTO filterFeedDTO : filterFeedDTOS) {
            if (filterFeedDTO.getField().equals("shared")) {
                query =
                    query +
                    "$or:[{\"shared.facebook\":true},{\"shared.link\":true},{\"shared.linkedIn\":true},{\"shared.twitter\":true}], ";
                filterFeedDTOS.remove(filterFeedDTO);
                break;
            }
        }
        if (ObjectUtils.isNotEmpty(filterFeedDTOS)) {
            for (FilterFeedDTO filterFeed : filterFeedDTOS) {
                if (filterFeed.getField().equals(Constants.FEED_FILTER.RECOMMEND_DATE)) {
                    LinkedHashMap<String, String> dateRange = (LinkedHashMap<String, String>) filterFeed.getValue();
                    query =
                        query +
                        filterFeed.getField() +
                        ": {$gte: ISODate('" +
                        dateRange.get("from") +
                        "'), $lte: ISODate('" +
                        dateRange.get("to") +
                        "')}, ";
                } else if (filterFeed.getField().equals(Constants.FEED_FILTER.SEARCH_TYPE)) {
                    query = query + filterFeed.getField() + ": {$regex: '" + filterFeed.getValue() + "'}, ";
                } else {
                    query = query + filterFeed.getField() + ": " + filterFeed.getValue() + ", ";
                }
            }
        } else {
            List<FilterFeedDTO> filterHideConditions = new ArrayList<>(Arrays.asList(Constants.HIDE_CONDITIONS_FILTER));
            for (FilterFeedDTO hideFilter : filterHideConditions) {
                query = query + hideFilter.getField() + ": {$ne: " + hideFilter.getValue() + "}, ";
            }
        }
        query = query + "connectomeId: " + "'" + connectomeId + "'";

        if (uploadType != null && !uploadType.isEmpty()) {
            query = query + ",'type' : '" + uploadType + "'";
        }

        if (entityLabel != null && !entityLabel.isEmpty()) {
            query = query + ",'responseEntityLinking.result.entities.keyword.word' : '" + entityLabel + "'";
        }

        if ("".equals(keyword) || keyword == null) {
            query = query + "}";
        } else {
            query =
                query +
                ", $or: [ { title: {$regex: '" +
                keyword +
                "', $options: 'i'} }, { content: {$regex: '" +
                keyword +
                "', $options: 'i'} }, { keyword: {$regex: '" +
                keyword +
                "', $options: 'i'} } ]}";
        }
        return query;
    }

    @NotNull
    private Page<PersonalDocument> searchPersonalDocument(int page, int size, String orderBy, String sortDirection, String queryStr) {
        Pageable pageable = PageRequest.of(
            page,
            size,
            "desc".equalsIgnoreCase(sortDirection) ? Sort.Direction.DESC : Sort.Direction.ASC,
            orderBy,
            "_id"
        );
        BasicQuery query = new BasicQuery(queryStr);
        query.with(pageable);
        List<PersonalDocument> lstPersonalDocument = mongoTemplate.find(query, PersonalDocument.class);
        return PageableExecutionUtils.getPage(
            lstPersonalDocument,
            pageable,
            () -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), PersonalDocument.class)
        );
    }

    @Override
    public List<PersonalDocument> findPersonalDocumentsByIds(List<String> ids, Boolean... isDeleted) {
        List<ObjectId> objectIds = new ArrayList<>();
        ids.forEach(
            item -> {
                ObjectId objectId = new ObjectId(item);
                objectIds.add(objectId);
            }
        );
        if ((isDeleted != null && isDeleted.length > 0) && isDeleted[0] != null) {
            return personalDocumentRepository.findPersonalDocumentByIdInAndDeletedNot(objectIds, isDeleted[0]);
        }
        return personalDocumentRepository.findAllByIdIn(objectIds);
    }

    @Override
    public List<PersonalDocument> findPersonalDocumentsByDocIds(List<String> docIds, Boolean... isDeleted) {
        if ((isDeleted != null && isDeleted.length > 0) && isDeleted[0] != null) {
            return personalDocumentRepository.findPersonalDocumentByDocIdInAndDeletedNot(docIds, isDeleted[0]);
        }
        return personalDocumentRepository.findAllByDocIdIn(docIds);
    }
}
