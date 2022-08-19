package com.saltlux.deepsignal.web.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.*;
import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "category")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(length = 50)
    private String code;

    private Integer type;
    //
    //    @OneToMany(
    //        mappedBy = "category",
    //        cascade = CascadeType.ALL,
    //        fetch = FetchType.LAZY
    //    )
    //    private List<InquiryQuestion> inquiryQuestions = new ArrayList<>();

}
