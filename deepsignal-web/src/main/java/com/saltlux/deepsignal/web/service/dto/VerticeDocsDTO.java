package com.saltlux.deepsignal.web.service.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.*;

@Getter
@Setter
public class VerticeDocsDTO {

    private String label;

    private String type;

    private List<ObjectId> documentIds;

    private List<ObjectId> feedIds;
}
