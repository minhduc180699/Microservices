package com.saltlux.deepsignal.web.service.dto;

import java.util.List;
import lombok.Data;

@Data
public class ClusterDocument {

    private String id;

    private String connectomeId;

    private String docId;

    private String title;

    private String writerName;

    private String content;

    private String sourceId;

    private List<String> imageLinks;

    private String favicon;

    private String lang;

    private String words;
}
