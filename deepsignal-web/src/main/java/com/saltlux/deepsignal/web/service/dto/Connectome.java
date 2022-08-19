package com.saltlux.deepsignal.web.service.dto;

import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Connectome {

    private String id;

    private String connectomeId;

    private String connectomeUrl;

    private String connectomeDebugUrl;

    private List<Topic> topics;

    private List<Edge> edges;

    private List<Entitie> entities;
}

@Getter
@Setter
class Topic {

    private String topicId;

    private String label;

    private Integer level;

    private Integer size;

    private Double weight;

    private Boolean hide;

    private Date publishedAt;

    private Date lastUpdatedAt;
}

@Getter
@Setter
class Edge {

    private String source;

    private String target;
}

@Getter
@Setter
class Entitie {

    private String label;

    private String entityId;

    private Integer docFreq;

    private Date lastUpdatedAt;

    private List<DocumentModel> documents;
}

@Getter
@Setter
class DocumentModel {

    private String docId;

    private String sourceType;

    private Integer termFreq;

    private Boolean like;

    private Boolean disLike;
}
