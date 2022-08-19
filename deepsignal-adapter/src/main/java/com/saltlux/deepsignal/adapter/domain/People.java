package com.saltlux.deepsignal.adapter.domain;

import com.saltlux.deepsignal.adapter.domain.PeopleDTO.CompanyDTO;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@Document("people")
public class People {

    @Id
    @Field("_id")
    private String id;

    @Field("connectome_id")
    private String connectomeId;

    @Field("company")
    private List<CompanyDTO> company = new ArrayList<>();

    @Field("people")
    private List<CompanyDTO> people = new ArrayList<>();

    @Field("stock")
    private List<StockCodes> stock;

    @Field("created_date")
    private Date createdDate;

    @Field("lang")
    private String lang;
}
