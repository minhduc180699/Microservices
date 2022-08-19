ALTER TABLE `user_activity_log`
    ADD COLUMN IF NOT EXISTS `feed_title` varchar(500) NULL DEFAULT NULL COMMENT 'Title of articles',
    ADD COLUMN IF NOT EXISTS `feed_original_url` text NULL DEFAULT NULL COMMENT 'Original URL of articles';
