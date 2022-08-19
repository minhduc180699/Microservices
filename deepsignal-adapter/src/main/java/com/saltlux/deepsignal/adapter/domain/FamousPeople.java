package com.saltlux.deepsignal.adapter.domain;

import javax.persistence.Id;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document("famous_people")
public class FamousPeople {

    @Id
    @Field("_id")
    private String id;

    @Field("title")
    private String title;

    @Field("image_url")
    private String imageUrl;

    @Field("cdn")
    private String cdn;
}

