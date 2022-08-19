package com.saltlux.deepsignal.web.api.websocket.dto;

import com.saltlux.deepsignal.web.config.Constants;
import com.saltlux.deepsignal.web.domain.Notification;
import com.saltlux.deepsignal.web.service.dto.UserDTO;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class NotificationDTO extends BaseSocketDTO {

    private Long id;

    @NotNull
    private String content;

    private Instant createdDate = Instant.now();

    @NotBlank
    private String title;

    private Integer isMarkedRead;
    private String url;
    private UserDTO userDTO;
    private String btnContent;
    private String iconContent;
    private String titleI18n;
    private String contentI18n;

    @NotNull
    private List<String> receiverIds = new ArrayList<>();

    // type and name of notification type
    @NotNull
    @Enumerated(EnumType.ORDINAL)
    private Constants.NotificationCategory category;

    @NotNull
    private String typeName;

    public NotificationDTO(Notification notification) {
        this.id = notification.getId();
        this.content = notification.getContent();
        this.createdDate = notification.getCreatedDate();
        this.title = notification.getTitle();
        this.isMarkedRead = notification.getIsMarkedRead();
        this.url = notification.getUrl();
        this.userDTO = new UserDTO(notification.getUser());
        this.category = notification.getNotificationType().getCategory();
        this.btnContent = notification.getBtnContent();
        this.iconContent = notification.getIconContent();
        this.titleI18n = notification.getTitleI18n();
        this.contentI18n = notification.getContentI18n();
    }
}
