ALTER TABLE `user_activity_log` ADD COLUMN IF NOT EXISTS `user_feedback` varchar(255) DEFAULT NULL
    COMMENT 'feedback when user hidden card';
