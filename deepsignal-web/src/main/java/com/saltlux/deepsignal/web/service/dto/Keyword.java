package com.saltlux.deepsignal.web.service.dto;

import java.util.List;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Keyword {

    private String word;

    private String lemma;

    private Span span;

    private String wordId;

    private int status;

    private String ner;

    private Pos pos;

    private List<String> candidateIds;
}

@Data
class Span {

    private Integer start;
    private Integer end;
}

@Data
class Pos {

    private List<String> universal;
    private List<String> treebank;
}
