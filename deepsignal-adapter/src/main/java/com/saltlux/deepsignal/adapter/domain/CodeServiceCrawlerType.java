package com.saltlux.deepsignal.adapter.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "code_service_crawler_type")
public class CodeServiceCrawlerType extends AbstractAuditingEntity implements Serializable {

    @Id
    @Column(name = "service_crawler_type", nullable = false, length = 50)
    private String serviceCrawlerType;

    @Column(name = "is_disabled", nullable = false, columnDefinition = "TINYINT(4)")
    private Integer isDisabled;

    @Column(name = "description", length = 512)
    private String description;

    @Column(name = "channel_code")
    private Integer channelCode;
}
