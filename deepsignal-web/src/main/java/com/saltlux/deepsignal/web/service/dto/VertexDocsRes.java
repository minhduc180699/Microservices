package com.saltlux.deepsignal.web.service.dto;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class VertexDocsRes extends CommonRes {

    private List<PersonalDocument> personalDocuments;
    private List<Feed> feeds;
}
