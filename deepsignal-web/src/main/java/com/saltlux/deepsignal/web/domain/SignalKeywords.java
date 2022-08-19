package com.saltlux.deepsignal.web.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "signal_keywords")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SignalKeywords extends AbstractBaseEntity implements Serializable {

    @Column(name = "keywords", nullable = false, length = 200)
    private String keywords;

    @Column(name = "main_keyword", nullable = false, length = 100)
    private String mainKeyword;

    @Column(name = "type", nullable = false, length = 2)
    private String type;

    @Column(name = "language", nullable = false, length = 2)
    private String language;

    @Column(name = "display_order")
    private Integer displayOrder = 100;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "connectome_id")
    private Connectome connectome;

    @Column(name = "status", nullable = false)
    private Integer status;
}
