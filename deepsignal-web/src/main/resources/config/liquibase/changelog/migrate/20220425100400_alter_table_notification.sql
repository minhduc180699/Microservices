ALTER TABLE `notification` ADD COLUMN IF NOT EXISTS `is_checked` tinyint(1) NOT NULL DEFAULT 0
COMMENT 'When user select to notification icon from ui, this field to checked';

ALTER TABLE `notification` ADD COLUMN IF NOT EXISTS `btn_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL
COMMENT 'This field passed from template_btn from table notification_type';

ALTER TABLE `notification` ADD COLUMN IF NOT EXISTS `icon_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL
COMMENT 'This field passed from template_icon from table notification_type';

ALTER TABLE `notification` ADD COLUMN IF NOT EXISTS `type` varchar(255) DEFAULT NULL
COMMENT 'Type of notification. Passed from table notification_type';

ALTER TABLE `notification_type` MODIFY COLUMN `type` varchar(255) DEFAULT NULL
COMMENT 'Type of notification';

ALTER TABLE `notification_type` ADD COLUMN IF NOT EXISTS `category` enum('basic','interpolation')
COMMENT 'If category is interpolation, you must pass content like {0}, {1}, {2}..., if it is basic, content is static';

ALTER TABLE `notification_type` ADD COLUMN IF NOT EXISTS `template_btn` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL
COMMENT 'If content of notification has button, please add template to this field';

ALTER TABLE `notification_type` ADD COLUMN IF NOT EXISTS `template_icon` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL
COMMENT 'Icon template for each notification type';

