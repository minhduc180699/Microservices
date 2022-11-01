package com.ds.dssearcher.service;

import com.ds.dssearcher.entity.*;
import com.ds.dssearcher.model.*;
import com.ds.dssearcher.response.ResponseDocument;
import com.ds.dssearcher.response.ResponseListData;

import java.util.List;

public interface ISearchSevice {
    DetailDocumentEntity getById(String id);

    SearchResult searchKeyword(String keyWord, int page, String searchType, String language, String searchDate, Integer size, List<String> authors, String channels, String sortBy, String reverseSort,
                               Integer score, String from, String util, String entityLinkingType) throws Exception;

    SearchResultPeople searchPeople(String channels, List<String> authors, List<String> authorIds, int page, int size, String language);

    SearchResultDataSource searchSourceData(String dataSourceIds, String channels, int page, int size, String serviceType, String lang);

    ResultCountDatasource countDataSourceById(String dataSourceIds, String channels, String serviceType, String lang);

    SearchResultPeople searchKeyword(String channels, String keyword, int page, int size, String language);

    SearchResult searchContent(String keyWord, int page, String searchType, String language, String searchDate, Integer size, List<String> authors, String channels, String sortBy, String reverseSort,
                               Integer score, String from, String util) throws Exception;

    ResponseListData<FeedModel> getListFeed(String connectomeId, Integer page, Integer size);

    ResponseListData<FeedModel> searchFeed(String connectomeId, String keyword, String from, String until, String searchType, Integer page, Integer size, String channels, String lang, String type, String sortBy, Float score, List<String> writer);

    DocContentModel getFeedContent(String feedId);

    FeedModel getFeed(String docId, String connectomeId);

    List<DocContentModel> getListFeedContent(List<String> feedContentId);

    ResponseListData<?> getListDocumentByIds(List<String> docIds, String connectomeId, Integer page, Integer size);

    ResponseListData<FeedModel> getListFilterFeed(String connectomeId, Integer page, Integer size, String type);

    ResponseDocument<?> getDocumentById(RequestBodyGetDocument requestBody);

    ResponseListData<?> getListDocumentByIds(RequestBodyGetListDoc requestBody);
}
