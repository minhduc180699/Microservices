package com.ds.dssearcher.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

public class ViewSearchResult {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class SearchResultLinking extends ResponseResult{
        private List<ViewDocument.DocumentLinkingEntity> data;

        public SearchResultLinking covert(SearchResult searchResult) throws JsonProcessingException {
            SearchResultLinking searchResultLinking = new SearchResultLinking();
            searchResultLinking.setResult(searchResult.getResult());
            searchResultLinking.setResult_code(searchResult.getResult_code());
            ViewDocument.DocumentLinkingEntity documentLinkingEntity = new ViewDocument.DocumentLinkingEntity();
            List<ViewDocument.DocumentLinkingEntity> linkingEntities = documentLinkingEntity.covert(searchResult.getData());
            searchResultLinking.setData(linkingEntities);
            return searchResultLinking;
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class SearchResultSummary extends ResponseResult{
        private List<ViewDocument.DocumentSummaryEntity> data;
        public SearchResultSummary covert(SearchResult searchResult) throws JsonProcessingException {
            SearchResultSummary searchResultLinking = new SearchResultSummary();
            searchResultLinking.setResult(searchResult.getResult());
            searchResultLinking.setResult_code(searchResult.getResult_code());
            ViewDocument.DocumentSummaryEntity documentLinkingEntity = new ViewDocument.DocumentSummaryEntity();
            List<ViewDocument.DocumentSummaryEntity> linkingEntities = documentLinkingEntity.covert(searchResult.getData());
            searchResultLinking.setData(linkingEntities);
            return searchResultLinking;
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class SearchResultNone extends ResponseResult{
        private List<ViewDocument.DocumentNoneEntity> data;
        public SearchResultNone covert(SearchResult searchResult) throws JsonProcessingException {
            SearchResultNone searchResultLinking = new SearchResultNone();
            searchResultLinking.setResult(searchResult.getResult());
            searchResultLinking.setResult_code(searchResult.getResult_code());
            ViewDocument.DocumentNoneEntity documentLinkingEntity = new ViewDocument.DocumentNoneEntity();
            List<ViewDocument.DocumentNoneEntity> linkingEntities = documentLinkingEntity.covert(searchResult.getData());
            searchResultLinking.setData(linkingEntities);
            return searchResultLinking;
        }
    }

}
