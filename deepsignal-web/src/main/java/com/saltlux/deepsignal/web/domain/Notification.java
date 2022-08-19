package com.saltlux.deepsignal.web.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.saltlux.deepsignal.web.config.Constants;
import com.saltlux.deepsignal.web.util.HttpRequestResponseUtils;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Entity
@Builder
@Table(name = "notification")
public class Notification implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    private String title;

    @NotNull
    private String content;

    @NotNull
    @Column(name = "content_i18n")
    private String contentI18n;

    @NotNull
    @Column(name = "title_i18n")
    private String titleI18n;

    @CreatedDate
    @Column(updatable = false, name = "created_date")
    private Instant createdDate = Instant.now();

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @JoinColumn(name = "username")
    private String username;

    @NotNull
    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private Integer isMarkedRead;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "type_id", nullable = false)
    private NotificationType notificationType;

    @OneToMany(mappedBy = "notification", orphanRemoval = true, cascade = CascadeType.PERSIST)
    @JsonManagedReference
    private Set<NotificationReceiver> notificationReceivers = new HashSet<>();

    private String url;
    private String btnContent;
    private String iconContent;

    @JoinColumn(name = "type")
    private String type;

    @NotNull
    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private Integer isChecked;

    @JsonIgnore
    private transient List<String> receiverIds;

    public interface MARK_READ {
        Integer READ = 1;
        Integer UNREAD = 0;
    }

    private transient Constants.NotificationCategory category;

    @PostLoad
    public void onLoad() {
        init();
        this.category = this.notificationType.getCategory();
    }

    @PrePersist
    public void onPersist() {
        initUsername();
        initType();
    }

    private void init() {
        this.initUsername();
        //        this.initUrl();
        this.initType();
    }

    private void initUrl() {
        String uriProtocol = HttpRequestResponseUtils.getUrlProtocol();
        this.url = uriProtocol + this.url;
    }

    private void initUsername() {
        this.username = this.user.getLogin();
    }

    private void initType() {
        this.type = this.notificationType.getType();
    }

    public void removeNotificationReceiver(NotificationReceiver notificationReceiver) {
        notificationReceivers.remove(notificationReceiver);
        notificationReceiver.setNotification(null);
    }
}
