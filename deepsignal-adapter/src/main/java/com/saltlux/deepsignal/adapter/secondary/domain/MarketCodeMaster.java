package com.saltlux.deepsignal.adapter.secondary.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.Instant;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "market_code_master")
@EntityListeners(AuditingEntityListener.class)
public class MarketCodeMaster {

    @Id
    @Column(name = "code_id")
    private Long codeId;

    @Column(name = "market_type")
    private String marketType;

    @Column(name = "market_country")
    private String marketCountry;

    @Column(name = "market_name")
    private String marketName;

    @Column(name = "symbol_code")
    private String symbolCode;

    @Column(name = "symbol_yahoo")
    private String symbolYahoo;

    @Column(name = "symbol_name_kr")
    private String symbolNameKr;

    @Column(name = "symbol_name_en")
    private String symbolNameEn;

    @Column(name = "created_by", length = 50)
    @CreatedBy
    private String createdBy;

    @Column(name = "created_date", nullable = false, updatable = false)
    @CreatedDate
    private Instant createdDate = Instant.now();

    @LastModifiedBy
    @Column(name = "last_modified_by", length = 50)
    private String lastModifiedBy;

    @LastModifiedDate
    @Column(name = "last_modified_date")
    private Instant lastModifiedDate = Instant.now();
}
