package com.saltlux.deepsignal.web.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "inquiry_question")
@EntityListeners(AuditingEntityListener.class)
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class InquiryQuestion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_code", referencedColumnName = "code")
    private Category category;

    @Column(length = 1000)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "file", length = 1000)
    private String file;

    @Column(nullable = false)
    private int status;

    @Column(name = "is_public", nullable = false, columnDefinition = "TINYINT(1)")
    private boolean isPublic;

    @Column(name = "is_email", nullable = false, columnDefinition = "TINYINT(1)")
    private boolean isEmail;

    @Column(name = "email", nullable = false, length = 100)
    private String emailName;

    @Column(nullable = false, length = 100)
    private String name;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @CreatedDate
    @Column(name = "created_date", updatable = false)
    private Instant createdDate;
    //    @OneToOne(mappedBy = "inquiryQuestion")
    //    private InquiryAnswer inquiryAnswer;
    //
    //    @OneToOne(mappedBy = "inquiryQuestion")
    //    private InquiryAnswerEmail inquiryAnswerEmail;

}
