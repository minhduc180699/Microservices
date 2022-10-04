package com.saltlux.deepsignal.adapter.service.impl;

import com.saltlux.deepsignal.adapter.config.Constants;
import com.saltlux.deepsignal.adapter.domain.ConnectomeFeed;
import com.saltlux.deepsignal.adapter.domain.Feed;
import com.saltlux.deepsignal.adapter.repository.dsservice.ConnectomeFeedRepository;
import com.saltlux.deepsignal.adapter.repository.dsservice.FeedRepository;
import com.saltlux.deepsignal.adapter.repository.dsservice.PeopleRepository;
import com.saltlux.deepsignal.adapter.service.IConnectomeFeedService;
import com.saltlux.deepsignal.adapter.service.IPersonalDocumentService;
import com.saltlux.deepsignal.adapter.service.dto.FilterFeedDTO;
import com.saltlux.deepsignal.adapter.service.dto.SharingMethod;
import lombok.SneakyThrows;
import org.apache.commons.lang3.ObjectUtils;
import org.bson.types.ObjectId;
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

import java.time.Instant;
import java.util.*;

@Service
public class ConnectomeFeedServiceImpl implements IConnectomeFeedService {

    private ConnectomeFeedRepository connectomeFeedRepository;

    private FeedRepository feedRepository;

    private final PeopleRepository peopleRepository;

    private final MongoTemplate mongoTemplate;

    private final IPersonalDocumentService personalDocumentService;

    public ConnectomeFeedServiceImpl(
        ConnectomeFeedRepository connectomeFeedRepository,
        FeedRepository feedRepository,
        PeopleRepository peopleRepository,
        MongoTemplate mongoTemplate,
        IPersonalDocumentService personalDocumentService
    ) {
        this.connectomeFeedRepository = connectomeFeedRepository;
        this.feedRepository = feedRepository;
        this.peopleRepository = peopleRepository;
        this.mongoTemplate = mongoTemplate;
        this.personalDocumentService = personalDocumentService;
    }

    @Override
    public Page<ConnectomeFeed> findAll(int page, int size, String orderBy, String sortDirection) {
        Pageable pageable = PageRequest.of(
            page,
            size,
            "desc".equalsIgnoreCase(sortDirection) ? Sort.Direction.DESC : Sort.Direction.ASC,
            orderBy
        );
        return connectomeFeedRepository.findAll(pageable);
    }

    @Override
    public Page<ConnectomeFeed> findByConnectomId(int page, int size, String orderBy, String sortDirection, String connectomeId) {
        Pageable pageable = PageRequest.of(
            page,
            size,
            "desc".equalsIgnoreCase(sortDirection) ? Sort.Direction.DESC : Sort.Direction.ASC,
            orderBy
        );
        return connectomeFeedRepository.findByConnectomeId(pageable, connectomeId);
    }

    @Override
    public Page<Feed> findFeedByConnectomeId(int page, int size, String orderBy, String sortDirection, String connectomeId) {
        Pageable pageable = PageRequest.of(
            page,
            size,
            "desc".equalsIgnoreCase(sortDirection) ? Sort.Direction.DESC : Sort.Direction.ASC,
            orderBy,
            "_id"
        );
        return feedRepository.findByConnectomeId(pageable, connectomeId);
    }

    @Override
    public Page<Feed> findByConnectomeIdAndKeyword(
        int page,
        int size,
        String orderBy,
        String sortDirection,
        String connectomeId,
        String keyword
    ) {
        Pageable pageable = PageRequest.of(
            page,
            size,
            "desc".equalsIgnoreCase(sortDirection) ? Sort.Direction.DESC : Sort.Direction.ASC,
            orderBy,
            "_id"
        );
        return feedRepository.findByConnectomeIdAndTitleOrContentContaining(pageable, connectomeId, keyword);
    }

    @Override
    public Page<Feed> findFeedByConnectomeIdAndInteraction(
        int page,
        int size,
        String orderBy,
        String sortDirection,
        String connectomeId,
        String interaction,
        String interactionValue
    ) {
        String queryStr = "{ connectomeId : '" + connectomeId + "', " + interaction + ": " + interactionValue + " }";
        return searchFeed(page, size, orderBy, sortDirection, queryStr);
    }

    @Override
    public Page<Feed> findFeedByConnectomeIdKeywordAndInteraction(
        int page,
        int size,
        String orderBy,
        String sortDirection,
        String connectomeId,
        String keyword,
        String interaction,
        String interactionValue
    ) {
        String queryStr =
            "{$and : [ {connectomeId : '" +
            connectomeId +
            "'}, {" +
            interaction +
            ": " +
            interactionValue +
            "}, {$or : [ {title : { $regex: '" +
            keyword +
            "', $options: 'i' }}, {content : { $regex: '" +
            keyword +
            "', $options: 'i' }} ]} ]}";
        return searchFeed(page, size, orderBy, sortDirection, queryStr);
    }

    @Override
    public Page<Feed> findFeedByConnectomeIdKeywordAndFilter(
        int page,
        int size,
        String orderBy,
        String sortDirection,
        String connectomeId,
        String keyword,
        List<FilterFeedDTO> filterFeedDTOS
    ) {
        String queryStr = buildQueryFilterFeed(connectomeId, filterFeedDTOS, keyword);
        return searchFeed(page, size, orderBy, sortDirection, queryStr);
    }

    private String buildQueryFilterFeed(String connectomeId, List<FilterFeedDTO> filterFeedDTOS, String keyword) {
        String query = "{recommendationType: 'system', ";
        for (FilterFeedDTO filterFeedDTO : filterFeedDTOS) {
            if (filterFeedDTO.getField().equals("shared")) {
                query =
                    query +
                    "$or:[{\"shared.facebook\":true},{\"shared.link\":true},{\"shared.linkedIn\":true},{\"shared.twitter\":true}], ";
                filterFeedDTOS.remove(filterFeedDTO);
                break;
            }
        }
        //        List<FilterFeedDTO> filterHideConditions = new ArrayList<>(Arrays.asList(Constants.HIDE_CONDITIONS_FILTER));
        if (ObjectUtils.isNotEmpty(filterFeedDTOS)) {
            for (FilterFeedDTO filterFeed : filterFeedDTOS) {
                //                Optional<FilterFeedDTO> redundantFilter = filterHideConditions
                //                    .stream()
                //                    .filter(hideCondition -> hideCondition.getField().equals(filterFeed.getField()))
                //                    .findFirst();
                //                redundantFilter.ifPresent(filterHideConditions::remove);

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
                }
                //fix for demo
                else if (filterFeed.getField().equals("lang") && filterFeed.getValue().equals("'en'")) {
                    query = query + "$or:[{lang:null},{lang:" + filterFeed.getValue() + "},{lang:''}]" + ", ";
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
        if ("".equals(keyword) || keyword == null || keyword.split("/").length == 0) {
            query = query + "}";
        } else {
            String[] keywordArray = keyword.split("/");
            query = query + ", $or: [ { title: {$in: [";
            for (int i = 0; i < keywordArray.length; i++) {
                if (!keywordArray[i].trim().isEmpty()) {
                    query = query + "/" + keywordArray[i].trim() + "/i";
                    if (i < keywordArray.length - 1) {
                        query = query + ",";
                    }
                } else {
                    continue;
                }
            }
            query = query + "]} }, { content: {$in: [";
            for (int i = 0; i < keywordArray.length; i++) {
                if (!keywordArray[i].trim().isEmpty()) {
                    query = query + "/" + keywordArray[i].trim() + "/i";
                    if (i < keywordArray.length - 1) {
                        query = query + ",";
                    }
                } else {
                    continue;
                }
            }
            query = query + "]} } ]}";
        }
        return query;
    }

    private Page<Feed> searchFeed(int page, int size, String orderBy, String sortDirection, String queryStr) {
        Pageable pageable = PageRequest.of(
            page,
            size,
            "desc".equalsIgnoreCase(sortDirection) ? Sort.Direction.DESC : Sort.Direction.ASC,
            orderBy,
            "_id"
        );
        BasicQuery query = new BasicQuery(queryStr);
        query.allowDiskUse(true);
        query.with(pageable);
        List<Feed> lstFeed = mongoTemplate.find(query, Feed.class);
        return PageableExecutionUtils.getPage(lstFeed, pageable, () -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), Feed.class));
    }

    @Override
    public Page<Feed> findFeedByConnectomIdAndTopic(
        int page,
        int size,
        String orderBy,
        String sortDirection,
        String connectomeId,
        String topic,
        boolean excepted
    ) {
        Pageable pageable = PageRequest.of(
            page,
            size,
            "desc".equalsIgnoreCase(sortDirection) ? Sort.Direction.DESC : Sort.Direction.ASC,
            orderBy,
            "_id"
        );
        if (!excepted) {
            return feedRepository.findByConnectomeIdAndTopicSimsTopic(pageable, connectomeId, topic);
        } else {
            return feedRepository.findByConnectomeIdAndTopicSimsTopicNot(pageable, connectomeId, topic);
        }
    }

    @Override
    public Optional<ConnectomeFeed> findById(String id) {
        return connectomeFeedRepository.findById(new ObjectId(id));
    }

    @Override
    public long countAllFeedByConnectomeId(String connectomeId) {
        return feedRepository.countAllByConnectomeId(connectomeId);
    }

    @Override
    public boolean handleActivity(String docId, boolean state, String activity, String connectomeId, String page, int likeState) {
        if (activity.equals(Constants.activity.BOOKMARK.type)) {
            return handleBookMark(docId, state, connectomeId, page);
        }
        if (activity.equals(Constants.activity.DELETE.type)) {
            return handleHidden(docId, state, connectomeId, page);
        }
        if (activity.equals(Constants.activity.LIKE.type)) {
            return handleLike(docId, connectomeId, page, likeState);
        }
        return false;
    }

    public boolean handleLike(String docId, String connectomeId, String page, int likeState) {
        if (page.equals(Constants.page.FEED.type)) {
            ObjectId objectId = new ObjectId(docId);
            Optional<Feed> feed = feedRepository.findById(objectId);
            if (feed.isPresent()) {
                Query query = new Query();
                query.addCriteria(Criteria.where("_id").is(objectId));
                Update update = new Update();
                update.set("liked", likeState);
                try {
                    mongoTemplate.updateFirst(query, update, Feed.class);
                    return true;
                } catch (Exception e) {
                    return false;
                }
            } else {
                return false;
            }
        } else if (page.equals(Constants.page.PERSONAL_DOCUMENT.type)) {
            return personalDocumentService.handleActivity(new ObjectId(docId), Constants.activity.LIKE, false, likeState);
        }
        return false;
    }

    public boolean handleBookMark(String docId, boolean state, String connectomeId, String page) {
        if (page.equals(Constants.page.FEED.type)) {
            ObjectId objectId = new ObjectId(docId);
            Optional<Feed> feed = feedRepository.findById(objectId);
            if (feed.isPresent()) {
                Query query = new Query();
                query.addCriteria(Criteria.where("_id").is(objectId));
                Update update = new Update();
                update.set("bookmarked", state);
                try {
                    mongoTemplate.updateFirst(query, update, Feed.class);
                    return true;
                } catch (Exception e) {
                    return false;
                }
            }
        } else if (page.equals(Constants.page.PERSONAL_DOCUMENT.type)) {
            return personalDocumentService.handleActivity(new ObjectId(docId), Constants.activity.BOOKMARK, state, 0);
        }
        return false;
    }

    public boolean handleHidden(String docId, boolean state, String connectomeId, String page) {
        if (page.equals(Constants.page.FEED.type)) {
            ObjectId objectId = new ObjectId(docId);
            Optional<Feed> feed = feedRepository.findById(objectId);
            if (feed.isPresent()) {
                Query query = new Query();
                query.addCriteria(Criteria.where("_id").is(objectId));
                Update update = new Update();
                update.set("deleted", state);
                try {
                    mongoTemplate.updateFirst(query, update, Feed.class);
                    return true;
                } catch (Exception e) {
                    return false;
                }
            }
        } else if (page.equals(Constants.page.PERSONAL_DOCUMENT.type)) {
            return personalDocumentService.handleActivity(new ObjectId(docId), Constants.activity.DELETE, state, 0);
        }
        return false;
    }

    @Override
    public boolean handleShare(String id, String platform) {
        ObjectId objectId = new ObjectId(id);
        Optional<Feed> feed = feedRepository.findById(objectId);
        if (feed.isPresent()) {
            SharingMethod sharingMethod = new SharingMethod();
            if (ObjectUtils.isNotEmpty(feed.get().getShared())) {
                sharingMethod = feed.get().getShared();
            }
            switch (platform) {
                case "FACEBOOK":
                    sharingMethod.setFacebook(true);
                    break;
                case "TWITTER":
                    sharingMethod.setTwitter(true);
                    break;
                case "LINKEDIN":
                    sharingMethod.setLinkedIn(true);
                    break;
                case "LINK":
                    sharingMethod.setLink(true);
                    break;
            }
            Query query = new Query();
            query.addCriteria(Criteria.where("_id").is(objectId));
            Update update = new Update();
            update.set("shared", sharingMethod);
            try {
                mongoTemplate.updateFirst(query, update, Feed.class);
                return true;
            } catch (Exception e) {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public Optional<Feed> findDetailCardById(String id) {
        try {
            Optional<Feed> feed = feedRepository.findById(new ObjectId(id));
            if (feed.isPresent()) {
                return feed;
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    @SneakyThrows
    @Override
    public boolean handleMemo(String feedId, int status) {
        Optional<Feed> feed = feedRepository.findById(new ObjectId(feedId));
        if (!feed.isPresent()) {
            return false;
        }
        try {
            ObjectId objectId = new ObjectId(feed.get().getId());
            Query query = new Query();
            query.addCriteria(Criteria.where("_id").is(objectId));
            Update update = new Update();
            update.set("memo", status);
            mongoTemplate.updateFirst(query, update, Feed.class);
            return true;
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    @SneakyThrows
    @Override
    public List<Feed> findFeedByConnectomeIdAndLangAndRecommendDate(String connectomeId, String lang, Instant recommendDate) {
        return feedRepository.findAllByConnectomeIdAndLangAndRecommendDateGreaterThanEqual(connectomeId, lang, recommendDate);
    }

    @Override
    public long countFeedByConnectomeIdAndRecommendDate(String connectomeId, Instant datetime) {
        return feedRepository.countByConnectomeIdAndRecommendDateGreaterThanEqual(connectomeId, datetime);
    }

    @Override
    public Page<Feed> findFeedByConnectomeIdAndActivity(
        String connectomeId,
        FilterFeedDTO filterFeedDTO,
        String lang,
        int page,
        int size,
        String orderBy,
        String sortDirection
    ) {
        String queryStr = "{recommendationType: 'system', ";
        queryStr = queryStr + "connectomeId: " + "'" + connectomeId + "', lang: '" + lang + "', ";
        queryStr = queryStr + filterFeedDTO.getField() + ": " + filterFeedDTO.getValue() + "}";
        return searchFeed(page, size, orderBy, sortDirection, queryStr);
    }

    @Override
    public List<Feed> findFeedByIds(List<String> ids, Boolean... isDeleted) {
        List<ObjectId> objectIds = new ArrayList<>();
        ids.forEach(
            item -> {
                ObjectId objectId = new ObjectId(item);
                objectIds.add(objectId);
            }
        );
        if ((isDeleted != null && isDeleted.length > 0) && isDeleted[0] != null) {
            return feedRepository.findFeedByIdInAndDeletedNot(objectIds, isDeleted[0]);
        }
        return feedRepository.findAllByIdIn(objectIds);
    }
}
