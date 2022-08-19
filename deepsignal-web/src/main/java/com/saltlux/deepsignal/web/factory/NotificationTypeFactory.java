package com.saltlux.deepsignal.web.factory;

import com.saltlux.deepsignal.web.config.Constants;
import com.saltlux.deepsignal.web.domain.NotificationType;
import com.saltlux.deepsignal.web.exception.NotificationTypeNotFoundException;
import com.saltlux.deepsignal.web.repository.NotificationTypeRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NotificationTypeFactory {

    private static NotificationTypeRepository notificationTypeRepository;

    @Autowired
    public NotificationTypeFactory(NotificationTypeRepository notificationTypeRepository) {
        NotificationTypeFactory.notificationTypeRepository = notificationTypeRepository;
    }

    public static final NotificationTypeParent getTemplateNotification(
        Constants.NotificationCategory notificationCategory,
        Constants.NotificationName notificationName
    ) {
        Optional<NotificationType> notificationTypeOptional = notificationTypeRepository.findByCategoryAndName(
            notificationCategory,
            notificationName.name
        );
        if (!notificationTypeOptional.isPresent()) {
            throw new NotificationTypeNotFoundException();
        }
        NotificationType notificationTypeModel = notificationTypeOptional.get();
        switch (notificationCategory) {
            case basic:
                NotificationCateBasic notificationTypeParent = new NotificationCateBasic();
                notificationTypeParent.setTemplateContent(notificationTypeModel.getTemplateContent());
                notificationTypeParent.setTemplateTitle(notificationTypeModel.getTemplateTitle());
                notificationTypeParent.setTemplateUrl(notificationTypeModel.getTemplateUrl());
                notificationTypeParent.setTemplateBtn(notificationTypeModel.getTemplateBtn());
                notificationTypeParent.setTemplateIcon(notificationTypeModel.getTemplateIcon());
                notificationTypeParent.setTemplateContentI18n(notificationTypeModel.getTemplateContentI18n());
                notificationTypeParent.setTemplateTitleI18n(notificationTypeModel.getTemplateTitleI18n());
                return notificationTypeParent;
            case interpolation:
                NotificationCateInterpolation notificationCateInterpolation = new NotificationCateInterpolation();
                notificationCateInterpolation.setTemplateContent(notificationTypeModel.getTemplateContent());
                notificationCateInterpolation.setTemplateTitle(notificationTypeModel.getTemplateTitle());
                notificationCateInterpolation.setTemplateUrl(notificationTypeModel.getTemplateUrl());
                notificationCateInterpolation.setTemplateBtn(notificationTypeModel.getTemplateBtn());
                notificationCateInterpolation.setTemplateIcon(notificationTypeModel.getTemplateIcon());
                notificationCateInterpolation.setTemplateContentI18n(notificationTypeModel.getTemplateContentI18n());
                notificationCateInterpolation.setTemplateTitleI18n(notificationTypeModel.getTemplateTitleI18n());
                return notificationCateInterpolation;
            default:
                throw new IllegalArgumentException("This notification type is not define");
        }
    }
}
