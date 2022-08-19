package com.saltlux.deepsignal.web.factory;

import io.netty.util.internal.StringUtil;
import java.text.MessageFormat;

public class NotificationCateInterpolation extends NotificationTypeParent {

    @Override
    public String getNotificationContent(String... valToPass) {
        //        for (String val : valToPass) {
        //            Utils.appendStyle(val);
        //        }
        String template = super.getTemplateContent();
        return MessageFormat.format(template, valToPass);
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
