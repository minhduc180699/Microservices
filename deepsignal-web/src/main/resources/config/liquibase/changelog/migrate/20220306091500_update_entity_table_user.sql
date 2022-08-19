ALTER TABLE `user` ADD COLUMN IF NOT EXISTS `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT 'Logical delete user';
