package com.saltlux.deepsignal.web.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "inquiry_answer_email")
@EntityListeners(AuditingEntityListener.class)
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class InquiryAnswerEmail implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "inquiry_question_id", referencedColumnName = "id")
    private InquiryQuestion inquiryQuestion;

    @Column(length = 1000)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "file_question", length = 1000)
    private String fileQuestion;

    @Column(name = "file_answer", length = 1000)
    private String fileAnswer;

    @CreatedDate
    @Column(name = "created_date", updatable = false)
    private Instant createdDate;

    @LastModifiedDate
    @Column(name = "send_date")
    @JsonIgnore
    private Instant sendDate;
}
