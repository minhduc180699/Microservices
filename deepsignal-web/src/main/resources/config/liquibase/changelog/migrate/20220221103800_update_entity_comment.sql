ALTER TABLE `comment` ADD COLUMN IF NOT EXISTS `parent_comment_id` bigint(20) NULL DEFAULT NULL;
ALTER TABLE `comment` ADD KEY `comment_comment_FK` (`parent_comment_id`);
ALTER TABLE `comment` ADD CONSTRAINT `comment_comment_FK_1` FOREIGN KEY (`parent_comment_id`) REFERENCES `comment` (`id`);
