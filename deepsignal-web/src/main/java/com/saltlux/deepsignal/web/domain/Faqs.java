package com.saltlux.deepsignal.web.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "faqs")
@EntityListeners(AuditingEntityListener.class)
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Faqs implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "faq_id")
    private Long faqId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_code", referencedColumnName = "code")
    private Category category;

    @Column(length = 1000)
    private String title;

    @Column(nullable = false)
    private String question;

    @Column(nullable = false)
    private String answer;

    @Column(length = 1000)
    private String file;

    @CreatedDate
    @Column(name = "created_date", updatable = false, nullable = false)
    private Instant createdDate;

    @Column(name = "view_count")
    private int viewCount;

    private String note;
}
