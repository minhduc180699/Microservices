package com.saltlux.deepsignal.web.factory;

public abstract class NotificationTypeParent {

    private String templateContent;
    private String templateUrl;
    private String templateTitle;
    private String templateBtn;
    private String templateIcon;
    private String templateContentI18n;
    private String templateTitleI18n;

    // don't using public access modifier in getter setter for all this variable
    void setTemplateContent(String templateContent) {
        this.templateContent = templateContent;
    }

    String getTemplateContent() {
        return templateContent;
    }

    void setTemplateTitle(String templateTitle) {
        this.templateTitle = templateTitle;
    }

    String getTemplateTitle() {
        return templateTitle;
    }

    void setTemplateUrl(String templateUrl) {
        this.templateUrl = templateUrl;
    }

    String getTemplateUrl() {
        return templateUrl;
    }

    void setTemplateBtn(String templateBtn) {
        this.templateBtn = templateBtn;
    }

    String getTemplateBtn() {
        return templateBtn;
    }

    void setTemplateIcon(String templateIcon) {
        this.templateIcon = templateIcon;
    }

    String getTemplateIcon() {
        return templateIcon;
    }

    void setTemplateContentI18n(String templateContentI18n) {
        this.templateContentI18n = templateContentI18n;
    }

    void setTemplateTitleI18n(String templateTitleI18n) {
        this.templateTitleI18n = templateTitleI18n;
    }

    public abstract String getNotificationContent(String... valToPass);

    public abstract String getNotificationTitle(String... valToPass);

    public abstract String getNotificationUrl(String... valToPass);

    public abstract String getNotificationBtn(String... valToPass);

    public abstract String getNotificationIcon(String... valToPass);

    public String getTemplateContentI18n() {
        return this.templateContentI18n;
    }

    public String getTemplateTitleI18n() {
        return this.templateTitleI18n;
    }
}
