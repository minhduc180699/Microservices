DROP TABLE IF EXISTS `notification_type`;

CREATE TABLE IF NOT EXISTS `notification_type` (
  `id` bigint(20) NOT NULL,
  `type` enum('basic','interpolation'),
  `template_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `template_title` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `template_url` varchar(255) DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8
COMMENT 'This is table to config type of notification.
When you have new config notification, you must pass type of it.
If type = interpolation, please pass number from 0(like index)';

ALTER TABLE `notification` DROP COLUMN IF EXISTS `type`;

ALTER TABLE `notification`
    ADD COLUMN IF NOT EXISTS `type_id` bigint(20) NOT NULL AFTER `user_id`;

ALTER TABLE `notification` ADD CONSTRAINT `notification_fk_type` FOREIGN KEY (`type_id`) REFERENCES `notification_type` (`id`);

ALTER TABLE `notification` ADD COLUMN IF NOT EXISTS username varchar(40) NOT NULL;

CREATE TABLE IF NOT EXISTS `notification_receiver` (
  `notification_id` bigint(20) NOT NULL,
  `receiver_id` varchar(40) NOT NULL,
  PRIMARY KEY (`notification_id`, `receiver_id`),
  CONSTRAINT `notification_notification_FK_1` FOREIGN KEY (`notification_id`) REFERENCES `notification` (`id`),
  CONSTRAINT `notification_user_FK_2` FOREIGN KEY (`receiver_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- init data to notification_type
INSERT INTO deepsignal.notification_type (id, type, template_content, template_title, template_url, name) VALUES (1, 'basic', 'Great! Your feed updated, let''s see what''s new!', 'Feed Updated', '/feed', 'feed update');
INSERT INTO deepsignal.notification_type (id, type, template_content, template_title, template_url, name) VALUES (2, 'interpolation', '{0} also commented on {1}', 'Comment Feed', '', 'comment feed');
INSERT INTO deepsignal.notification_type (id, type, template_content, template_title, template_url, name) VALUES (3, 'interpolation', '{1} replied to your comment', 'Reply comment', null, 'reply comment');
