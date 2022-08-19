package com.saltlux.deepsignal.adapter.domain.PeopleDTO;

import com.saltlux.deepsignal.adapter.domain.EntityLinking.EntityLinkingDTO;
import java.time.Instant;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentDTO {

    private String id;

    @Field("connectome_id")
    private String connectomeId;

    @Field("doc_id")
    private String docId;

    private String title;
    private String content;

    @Field("writer_name")
    private String writerName;

    @Field("source_id")
    private String sourceId;

    @Field("image_links")
    private List<String> imageLinks;

    private String favicon;
    private String lang;
    private List<EntityLinkingDTO> entitylinking;

    @Field("search_keyword")
    private String searchKeyword;

    @Field("org_date")
    private String orgDate;

    @Field("search_type")
    private String searchType;

    @Field("recommend_date")
    private Instant recommendDate;

    private boolean bookmarked;

    private boolean deleted;

    private int liked;

    public void setDocument(DocumentDTO documentDTO) {
        this.setId(documentDTO.getId());
        this.setConnectomeId(documentDTO.getConnectomeId());
        this.setTitle(documentDTO.getTitle());
        this.setDocId(documentDTO.getDocId());
        this.setTitle(documentDTO.getTitle());
        this.setContent(documentDTO.getContent());
        this.setWriterName(documentDTO.getWriterName());
        this.setSourceId(documentDTO.getSourceId());
        this.setImageLinks(documentDTO.getImageLinks());
        this.setFavicon(documentDTO.getFavicon());
        this.setLang(documentDTO.getLang());
        this.setEntitylinking(documentDTO.getEntitylinking());
        this.setSearchKeyword(documentDTO.getSearchKeyword());
        this.setOrgDate(documentDTO.getOrgDate());
        this.setSearchType(documentDTO.getSearchType());
        this.setRecommendDate(documentDTO.getRecommendDate());
        this.setBookmarked(documentDTO.isBookmarked());
        this.setDeleted(documentDTO.isDeleted());
        this.setLiked(documentDTO.getLiked());
    }
}
