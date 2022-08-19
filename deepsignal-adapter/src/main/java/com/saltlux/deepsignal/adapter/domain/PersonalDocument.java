package com.saltlux.deepsignal.adapter.domain;

import com.saltlux.deepsignal.adapter.config.Constants;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import javax.persistence.*;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A File upload
 */
@Getter
@Setter
@NoArgsConstructor
@Document("personal_document_v01")
public class PersonalDocument implements Serializable {

    @Id
    @Field("_id")
    private String id;

    @Field("title")
    private String title;

    @Field("url")
    private String url;

    @Field("idHash")
    private String idHash;

    @Field("filePath")
    private String filePath;

    @Field("createdDate")
    private String createdDate;

    @Field("author")
    private String author;

    @Field("content")
    private String content;

    @Field("connectomeId")
    private String connectomeId;

    @Field("language")
    private String language;

    @Field("originDate")
    private String originDate;

    @Field("searchType")
    private String searchType;

    @Field("type")
    private String type;

    @Field("favicon_url")
    private String faviconUrl;

    @Field("favicon_base64")
    private String faviconBase64;

    @Field("og_image_base64")
    private String ogImageBase64;

    @Field("og_image_url")
    private String ogImageUrl;

    @Field("image_url")
    private List<String> imageUrl;

    @Field("image_base64")
    private List<String> imageBase64;

    @Field("__unique__key")
    private Long uniqueKey;

    @Field("published_at")
    private Instant publishedAt;

    // @Field("responseEntityLinking")
    // private String responseEntityLinking;

    @Field("isDelete")
    private Integer isDelete;

    private String collectionType = Constants.page.PERSONAL_DOCUMENT.type;

    private int liked = 0;
    private boolean bookmarked;
    private boolean memo;
    private boolean deleted;
}
