package com.saltlux.deepsignal.web.domain;

import com.saltlux.deepsignal.web.config.Constants;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Entity
@Table(name = "notification_type")
public class NotificationType implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "type")
    private String type;

    @Column(name = "template_content", nullable = false)
    private String templateContent;

    @Column(name = "template_url")
    private String templateUrl;

    @Column(name = "template_title")
    private String templateTitle;

    @Column(name = "template_btn")
    private String templateBtn;

    @Column(name = "template_icon")
    private String templateIcon;

    @NotNull
    @Column(name = "template_content_i18n")
    private String templateContentI18n;

    @NotNull
    @Column(name = "template_title_i18n")
    private String templateTitleI18n;

    @Enumerated(EnumType.STRING)
    private Constants.NotificationCategory category;
}
