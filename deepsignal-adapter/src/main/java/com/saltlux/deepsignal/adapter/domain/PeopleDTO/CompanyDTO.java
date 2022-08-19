package com.saltlux.deepsignal.adapter.domain.PeopleDTO;

import java.util.List;
import javax.persistence.PostLoad;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
public class CompanyDTO {

    private String title;
    private String twitter;
    private List<String> documentIds;
    private List<String> feedIds;
    private List<SocialDTO> social;

    @Field("dfCnt")
    private Integer dfCnt;

    @Field("deleted")
    private Boolean deleted = false;

    @Getter
    @Setter
    @Document("social")
    class SocialDTO {

        private String id;

        private String description;

        private String content;

        private String author;

        @Field("authorId")
        private String authorId;

        @Field("org_date")
        private String orgDate;

        private String link;

        private String lang;

        private String favicon;

        @Field("serviceType")
        private String serviceType;
    }
}
