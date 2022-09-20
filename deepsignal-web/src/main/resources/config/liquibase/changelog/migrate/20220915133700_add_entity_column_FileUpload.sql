ALTER TABLE `file_upload`
    ADD COLUMN IF NOT EXISTS `chrome_type` tinyint(1) DEFAULT 0  COMMENT 'training style:0|system,1|chrome extension';
