ALTER TABLE `notification_type` ADD COLUMN IF NOT EXISTS `template_content_i18n` varchar(255) DEFAULT NULL
COMMENT 'Template content for i18n';

ALTER TABLE `notification_type` ADD COLUMN IF NOT EXISTS `template_title_i18n` varchar(255) DEFAULT NULL
COMMENT 'Template title for i18n';

ALTER TABLE `notification` ADD COLUMN IF NOT EXISTS `content_i18n` varchar(255) DEFAULT NULL
COMMENT 'Content for i18n';

ALTER TABLE `notification` ADD COLUMN IF NOT EXISTS `title_i18n` varchar(255) DEFAULT NULL
COMMENT 'Title for i18n';
