DROP TABLE IF EXISTS `signal_keywords`;

CREATE TABLE `signal_keywords` (
   `id` bigint(20) NOT NULL AUTO_INCREMENT,
   `connectome_id` varchar(40) NOT NULL,
   `keywords` varchar(200) NOT NULL COMMENT 'keywords for issue tracking or abnormalty detection. input separate keywords with (,comma) ex> AI,ML',
   `main_keyword` varchar(100) NOT NULL COMMENT 'main keyword of keywords',
   `type` char(2) NOT NULL DEFAULT 'IT' COMMENT 'IT or AD - means IT-issue_tracking or AD-abnormally_detection',
   `language` char(2) NOT NULL DEFAULT 'en' COMMENT 'Language for meta searcher',
   `display_order` int(11) DEFAULT 100,
   `created_date` timestamp NOT NULL DEFAULT current_timestamp(),
   `last_modified_date` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
   PRIMARY KEY (`id`),
   INDEX(`connectome_id`,`type`,`keywords`),
   CONSTRAINT `signal_keywords_fk_1` FOREIGN KEY (`connectome_id`) REFERENCES `connectome` (`connectome_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
