ALTER TABLE `user_activity_log`
    ADD COLUMN IF NOT EXISTS `training_data` text NULL DEFAULT NULL COMMENT 'Info of training documents';
