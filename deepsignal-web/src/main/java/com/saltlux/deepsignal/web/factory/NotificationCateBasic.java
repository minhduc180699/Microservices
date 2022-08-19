package com.saltlux.deepsignal.web.factory;

import io.netty.util.internal.StringUtil;

public class NotificationCateBasic extends NotificationTypeParent {

    @Override
    public String getNotificationContent(String... valToPass) {
        return super.getTemplateContent();
    }

    @Override
    public String getNotificationTitle(String... valToPass) {
        if (!StringUtil.isNullOrEmpty(super.getTemplateTitle())) {
            return super.getTemplateTitle();
        }
        return null;
    }

    @Override
    public String getNotificationUrl(String... valToPass) {
        if (!StringUtil.isNullOrEmpty(super.getTemplateUrl())) {
            return super.getTemplateUrl();
        }
        return null;
    }

    @Override
    public String getNotificationBtn(String... valToPass) {
        if (!StringUtil.isNullOrEmpty(super.getTemplateBtn())) {
            return super.getTemplateBtn();
        }
        return null;
    }

    @Override
    public String getNotificationIcon(String... valToPass) {
        if (!StringUtil.isNullOrEmpty(super.getTemplateIcon())) {
            return super.getTemplateIcon();
        }
        return null;
    }
}
