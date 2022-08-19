SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for authority
-- ----------------------------
DROP TABLE IF EXISTS `authority`;
CREATE TABLE `authority` (
  `name` varchar(50) NOT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of authority
-- ----------------------------
BEGIN;
INSERT INTO `authority` VALUES ('ROLE_ADMIN');
INSERT INTO `authority` VALUES ('ROLE_USER');
COMMIT;

-- ----------------------------
-- Table structure for code_language
-- ----------------------------
DROP TABLE IF EXISTS `code_language`;
CREATE TABLE `code_language` (
  `language` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`language`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='������������';

-- ----------------------------
-- Records of code_language
-- ----------------------------
BEGIN;
INSERT INTO `code_language` VALUES ('CHINESE', NULL);
INSERT INTO `code_language` VALUES ('ENGLISH', NULL);
INSERT INTO `code_language` VALUES ('JAPANESE', NULL);
INSERT INTO `code_language` VALUES ('KOREAN', NULL);
INSERT INTO `code_language` VALUES ('VIETNAMESE', NULL);
COMMIT;

-- ----------------------------
-- Table structure for code_service_crawler_type
-- ----------------------------
DROP TABLE IF EXISTS `code_service_crawler_type`;
CREATE TABLE `code_service_crawler_type` (
  `service_crawler_type` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `is_disabled` tinyint(4) NOT NULL DEFAULT 0,
  `description` varchar(512) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `registered_at` datetime DEFAULT NULL,
  `channel_code` int(11) DEFAULT NULL COMMENT 'this field use for cassandra statistic. make sure each row has value.\nFor example: all DE_... has channel code =1',
  PRIMARY KEY (`service_crawler_type`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of code_service_crawler_type
-- ----------------------------
BEGIN;
INSERT INTO `code_service_crawler_type` VALUES ('DE', 0, 'run on DE', NULL, 1);
INSERT INTO `code_service_crawler_type` VALUES ('DE_COMMUNITY_BLOG', 0, 'Blog run on DE', NULL, 1);
INSERT INTO `code_service_crawler_type` VALUES ('DE_COMMUNITY_NEWS', 0, 'News run on DE', NULL, 1);
INSERT INTO `code_service_crawler_type` VALUES ('DE_DAUM_SEARCH', 0, 'Daum search run on DE', NULL, 1);
INSERT INTO `code_service_crawler_type` VALUES ('DE_FACEBOOK_FRIEND', 0, 'Facebook friend run on DE', NULL, 1);
INSERT INTO `code_service_crawler_type` VALUES ('DE_FACEBOOK_GROUP', 0, 'Facebook group run on DE', NULL, 1);
INSERT INTO `code_service_crawler_type` VALUES ('DE_FACEBOOK_PAGE', 0, 'Facebook page run on DE', NULL, 1);
INSERT INTO `code_service_crawler_type` VALUES ('DE_FACEBOOK_PROFILE', 0, 'Facebook profile run on DE', NULL, 1);
INSERT INTO `code_service_crawler_type` VALUES ('DE_FB_FOLLOWER', 0, 'Facebook follower run on DE', NULL, 1);
INSERT INTO `code_service_crawler_type` VALUES ('DE_FB_FOLLOWING', 0, 'Facebook following run on DE', NULL, 1);
INSERT INTO `code_service_crawler_type` VALUES ('DE_FB_PER_FEED', 0, 'Create by data source', NULL, NULL);
INSERT INTO `code_service_crawler_type` VALUES ('DE_FB_PER_FEED_WALL', 0, 'Create by data source', NULL, NULL);
INSERT INTO `code_service_crawler_type` VALUES ('DE_FB_PER_FOLLOWER', 0, 'Create by data source', NULL, NULL);
INSERT INTO `code_service_crawler_type` VALUES ('DE_FB_PER_FOLLOWING', 0, 'Create by data source', NULL, NULL);
INSERT INTO `code_service_crawler_type` VALUES ('DE_FB_PER_FRIEND', 0, 'Create by data source', NULL, NULL);
INSERT INTO `code_service_crawler_type` VALUES ('DE_FB_PER_GROUP_INFO', 0, 'Create by data source', NULL, NULL);
INSERT INTO `code_service_crawler_type` VALUES ('DE_FB_PER_PROFILE', 0, 'Create by data source', NULL, NULL);
INSERT INTO `code_service_crawler_type` VALUES ('DE_GGSCHOLAR_AUTHOR', 0, 'Google Scholar run on DE', NULL, 1);
INSERT INTO `code_service_crawler_type` VALUES ('DE_GGSCHOLAR_SEARCH', 0, 'ggscholar search run on DE', NULL, 1);
INSERT INTO `code_service_crawler_type` VALUES ('DE_INS_AI_FOLLOWER', 0, 'Instagram ai_startup follower run on DE', NULL, 1);
INSERT INTO `code_service_crawler_type` VALUES ('DE_INS_AI_FOLLOWING', 0, 'Instagram ai_startup following run on DE', NULL, 1);
INSERT INTO `code_service_crawler_type` VALUES ('DE_INS_FOLLOWER', 0, 'Instagram follower run on DE', NULL, 1);
INSERT INTO `code_service_crawler_type` VALUES ('DE_INS_FOLLOWING', 0, 'Instagram following run on DE', NULL, 1);
INSERT INTO `code_service_crawler_type` VALUES ('DE_INSTAGRAM_POST', 0, 'Instagram post run on DE', NULL, 1);
INSERT INTO `code_service_crawler_type` VALUES ('DE_INSTAGRAM_PROFILE', 0, 'Instagram profile run on DE', NULL, 1);
INSERT INTO `code_service_crawler_type` VALUES ('DE_INSTAGRAM_TAG', 0, 'Instagram tag run on DE', NULL, 1);
INSERT INTO `code_service_crawler_type` VALUES ('DE_LINKEDIN_ACTIVITY', 0, 'Linkedin run on DE', NULL, 1);
INSERT INTO `code_service_crawler_type` VALUES ('DE_LINKEDIN_POST', 0, 'linkedin post run on DE', NULL, 1);
INSERT INTO `code_service_crawler_type` VALUES ('DE_LINKEDIN_PROFILE', 0, 'Linkedin run on DE', NULL, 1);
INSERT INTO `code_service_crawler_type` VALUES ('DE_NAVER_CAFE', 0, 'Naver cafe on DE', NULL, 1);
INSERT INTO `code_service_crawler_type` VALUES ('DE_NAVER_SEARCH', 0, 'Naver search run on DE', NULL, 1);
INSERT INTO `code_service_crawler_type` VALUES ('DE_NEWS', 0, 'collect data on DE', NULL, 1);
INSERT INTO `code_service_crawler_type` VALUES ('DE_NEWS_american', 0, 'News - site americanbanker run on DE', NULL, 1);
INSERT INTO `code_service_crawler_type` VALUES ('DE_NEWS_analytics', 0, 'News - site analytics on DE', NULL, 1);
INSERT INTO `code_service_crawler_type` VALUES ('DE_NEWS_communities', 0, 'News - site communities run on DE', NULL, 1);
INSERT INTO `code_service_crawler_type` VALUES ('DE_NEWS_entrepreneur', 0, 'News - site entrepreneur on DE', NULL, 1);
INSERT INTO `code_service_crawler_type` VALUES ('DE_NEWS_etnews', 0, 'run on DE', NULL, 1);
INSERT INTO `code_service_crawler_type` VALUES ('DE_NEWS_eustartups', 0, 'News - site eustartups run on DE', NULL, 1);
INSERT INTO `code_service_crawler_type` VALUES ('DE_NEWS_inc', 0, 'News - site inc run on DE', NULL, 1);
INSERT INTO `code_service_crawler_type` VALUES ('DE_NEWS_neuroscien', 0, 'News - site neurosciencenews run on DE', NULL, 1);
INSERT INTO `code_service_crawler_type` VALUES ('DE_NEWS_newcientist', 0, 'News - site newcientist run on DE', NULL, 1);
INSERT INTO `code_service_crawler_type` VALUES ('DE_NEWS_newsunorg', 0, 'News - site newsunorg run on DE', NULL, 1);
INSERT INTO `code_service_crawler_type` VALUES ('DE_NEWS_rsipvision', 0, 'News - site rsipvision on DE', NULL, 1);
INSERT INTO `code_service_crawler_type` VALUES ('DE_NEWS_sas.com', 0, 'News - site sas on DE', NULL, 1);
INSERT INTO `code_service_crawler_type` VALUES ('DE_NEWS_sciencemag', 0, 'News - site sciencemag run on DE', NULL, 1);
INSERT INTO `code_service_crawler_type` VALUES ('DE_NEWS_startticker', 0, 'News - site startupticker run on DE', NULL, 1);
INSERT INTO `code_service_crawler_type` VALUES ('DE_NEWS_techradar', 0, 'News - site techradar on DE', NULL, 1);
INSERT INTO `code_service_crawler_type` VALUES ('DE_NEWS_techxplore', 0, 'News - site techxplore run on DE', NULL, 1);
INSERT INTO `code_service_crawler_type` VALUES ('DE_NEWS_venturebeat', 0, 'News - site venturebeat run on DE', NULL, 1);
INSERT INTO `code_service_crawler_type` VALUES ('DE_SEARCH_NAVER_BLOG', 0, 'Naver search run on DE', NULL, 1);
INSERT INTO `code_service_crawler_type` VALUES ('DE_TW_PER_FOLLOWER', 0, 'Create by data source', NULL, NULL);
INSERT INTO `code_service_crawler_type` VALUES ('DE_TW_PER_FOLLOWING', 0, 'Create by data source', NULL, NULL);
INSERT INTO `code_service_crawler_type` VALUES ('DE_TW_PER_PROFILE', 0, 'Create by data source', NULL, NULL);
INSERT INTO `code_service_crawler_type` VALUES ('DE_TW_PER_TWEET', 0, 'Create by data source', NULL, NULL);
INSERT INTO `code_service_crawler_type` VALUES ('DE_TW_PER_TWEET_WALL', 0, 'Create by data source', NULL, NULL);
INSERT INTO `code_service_crawler_type` VALUES ('LINKEDIN', 0, 'Linkedin run on DE', NULL, 2);
INSERT INTO `code_service_crawler_type` VALUES ('METASEARCH_BING', 0, 'Metasearch Bing  search engine', NULL, 3);
INSERT INTO `code_service_crawler_type` VALUES ('METASEARCH_DAUM', 0, 'Metasearch Daum  search engine', NULL, 3);
INSERT INTO `code_service_crawler_type` VALUES ('METASEARCH_GOOGLE', 0, 'Metasearch Google search engine', NULL, 3);
INSERT INTO `code_service_crawler_type` VALUES ('METASEARCH_NAVER', 0, 'Metasearch Naver  search engine', NULL, 3);
INSERT INTO `code_service_crawler_type` VALUES ('METASEARCH_YAHOO', 0, 'Metasearch Yahoo  search engine', NULL, 3);
INSERT INTO `code_service_crawler_type` VALUES ('RSS', 0, 'RSS', NULL, 4);
INSERT INTO `code_service_crawler_type` VALUES ('TWITER_PROFILE', 0, 'Twitter profile', NULL, 5);
INSERT INTO `code_service_crawler_type` VALUES ('TWITER_TREND', 0, 'Twitter Trend', NULL, 5);
INSERT INTO `code_service_crawler_type` VALUES ('TWITER_TWEET', 0, 'Twitter Tweet', NULL, 5);
INSERT INTO `code_service_crawler_type` VALUES ('UNKNOW', 0, 'No data collection scenario has been developed', NULL, 0);
INSERT INTO `code_service_crawler_type` VALUES ('YOUTUBE', 0, 'youtube', NULL, 6);
COMMIT;

-- ----------------------------
-- Table structure for code_service_type
-- ----------------------------
DROP TABLE IF EXISTS `code_service_type`;
CREATE TABLE `code_service_type` (
  `service_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `code_order` tinyint(3) unsigned NOT NULL,
  `is_disabled` tinyint(1) unsigned NOT NULL DEFAULT 0,
  `description` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `registered_at` datetime NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`service_type`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Value to distinguish data type for indexing.';

-- ----------------------------
-- Records of code_service_type
-- ----------------------------
BEGIN;
INSERT INTO `code_service_type` VALUES ('BLOG', 2, 0, NULL, '2020-06-03 14:27:12');
INSERT INTO `code_service_type` VALUES ('COMMUNITY', 3, 0, 'COMMUNITY, QnA, FORUM, search.naver?where=article', '2020-06-03 14:27:13');
INSERT INTO `code_service_type` VALUES ('FACEBOOK', 11, 0, 'https://www.facebook.com/', '2020-06-03 14:27:16');
INSERT INTO `code_service_type` VALUES ('INSTAGRAM', 12, 0, 'https://www.instagram.com/', '2020-06-03 14:27:17');
INSERT INTO `code_service_type` VALUES ('LINKEDIN', 15, 0, 'https://www.linkedin.com/', '2020-06-03 16:23:40');
INSERT INTO `code_service_type` VALUES ('MAGAZINE', 5, 1, 'MAGAZINE, JOURNAL, Reserved Code, Do not use,', '2020-06-03 14:27:15');
INSERT INTO `code_service_type` VALUES ('NAVER', 22, 1, 'https://www.naver.com/', '2020-09-01 12:08:09');
INSERT INTO `code_service_type` VALUES ('NEWS', 1, 0, 'naver.com,where=news', '2020-06-03 14:27:05');
INSERT INTO `code_service_type` VALUES ('PAPER', 20, 0, 'researchgate.net,google.com,sementaicscholar.org', '2020-06-03 14:27:18');
INSERT INTO `code_service_type` VALUES ('PATENT', 21, 0, NULL, '2020-06-03 14:27:18');
INSERT INTO `code_service_type` VALUES ('POST', 5, 1, 'Article, POST, Review, Reserved Code Do not use.', '2020-06-03 14:27:14');
INSERT INTO `code_service_type` VALUES ('TWITTER', 10, 0, 'https://twitter.com/', '2020-06-03 14:27:15');
INSERT INTO `code_service_type` VALUES ('VIDEO', 23, 0, 'Youtube video, https://www.bing.com/videos/search?q=deepfake&FORM=HDRSC4', '2020-06-03 14:27:15');
COMMIT;

-- ----------------------------
-- Table structure for connectome
-- ----------------------------
DROP TABLE IF EXISTS `connectome`;
CREATE TABLE `connectome` (
  `connectome_id` varchar(40) CHARACTER SET utf8 NOT NULL COMMENT 'connectome id, CT-UUID',
  `user_id` varchar(40) CHARACTER SET utf8 NOT NULL COMMENT 'connectome user id',
  `connectome_name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'connectome name',
  `connectome_job` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'INVESTOR|RESEARCHER',
  `description` varchar(512) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'description',
  PRIMARY KEY (`connectome_id`) USING BTREE,
  KEY `user_id` (`user_id`),
  CONSTRAINT `connectome_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='connectome list\r\nUsers can create multiple connectomes.';

-- ----------------------------
-- Records of connectome
-- ----------------------------
BEGIN;
INSERT INTO `connectome` VALUES ('CID-4507f712-66e1-43da-91c2-08e33821ae78', 'UID_850e30ae-ca28-4537-af0e-7fab38168550', 'Warren', 'INVERTOR', NULL);
INSERT INTO `connectome` VALUES ('CID-e975c1a6-7d41-4077-a01b-86a7422fe022', 'UID_850e30ae-ca28-4537-af0e-7fab38168550', 'Buffett', 'INVERTOR', NULL);
COMMIT;

-- ----------------------------
-- Table structure for connectome_file_upload_detail
-- ----------------------------
DROP TABLE IF EXISTS `connectome_file_upload_detail`;
CREATE TABLE `connectome_file_upload_detail` (
  `connectome_id` varchar(40) NOT NULL,
  `file_id` bigint(20) NOT NULL,
  PRIMARY KEY (`connectome_id`,`file_id`),
  KEY `file_id` (`file_id`),
  CONSTRAINT `connectome_file_upload_detail_ibfk_2` FOREIGN KEY (`file_id`) REFERENCES `file_upload` (`id`),
  CONSTRAINT `connectome_file_upload_detail_ibfk_3` FOREIGN KEY (`connectome_id`) REFERENCES `connectome` (`connectome_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of connectome_file_upload_detail
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for connectome_social_media
-- ----------------------------
DROP TABLE IF EXISTS `connectome_social_media`;
CREATE TABLE `connectome_social_media` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(40) CHARACTER SET utf8 DEFAULT NULL COMMENT 'owner_id',
  `social_type` int(11) DEFAULT NULL,
  `login_username` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `login_password` varchar(60) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `user_token` varchar(512) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `login_cookies` text COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `registered_at` datetime DEFAULT NULL COMMENT 'The Timestamp value at the customer enters the social network account into the system.',
  `last_updated_at` datetime DEFAULT NULL,
  `status` int(11) DEFAULT 1 COMMENT '0: Disable (Lock)\r\n1: Enable\r\n-1: deleted',
  `social_account_state` int(11) DEFAULT NULL COMMENT '1. Avaiable\r\n2. Checkpoint\r\n3. Die',
  `user_token_state` int(11) DEFAULT NULL COMMENT 'Value depend on Social Network',
  `social_account_state_description` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'Value depend on Social Network',
  `user_token_state_description` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'Value depend on Social Network',
  `uuid` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'SNS UUID',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `customer_account_FK_1` (`user_id`) USING BTREE,
  KEY `social_type` (`social_type`),
  CONSTRAINT `connectome_social_media_ibfk_1` FOREIGN KEY (`social_type`) REFERENCES `social_network` (`id`),
  CONSTRAINT `connectome_social_media_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=51752 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of connectome_social_media
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for connectome_social_media_detail
-- ----------------------------
DROP TABLE IF EXISTS `connectome_social_media_detail`;
CREATE TABLE `connectome_social_media_detail` (
  `connectome_id` varchar(40) NOT NULL,
  `social_media_id` bigint(20) NOT NULL,
  PRIMARY KEY (`connectome_id`,`social_media_id`),
  KEY `social_media_id` (`social_media_id`),
  CONSTRAINT `connectome_social_media_detail_ibfk_2` FOREIGN KEY (`social_media_id`) REFERENCES `connectome_social_media` (`id`),
  CONSTRAINT `connectome_social_media_detail_ibfk_3` FOREIGN KEY (`connectome_id`) REFERENCES `connectome` (`connectome_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of connectome_social_media_detail
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for connectome_web_source_detail
-- ----------------------------
DROP TABLE IF EXISTS `connectome_web_source_detail`;
CREATE TABLE `connectome_web_source_detail` (
  `connectome_id` varchar(40) NOT NULL,
  `web_source_id` varchar(40) NOT NULL,
  PRIMARY KEY (`connectome_id`,`web_source_id`),
  KEY `web_source_id` (`web_source_id`),
  CONSTRAINT `connectome_web_source_detail_ibfk_3` FOREIGN KEY (`connectome_id`) REFERENCES `connectome` (`connectome_id`),
  CONSTRAINT `connectome_web_source_detail_ibfk_4` FOREIGN KEY (`web_source_id`) REFERENCES `web_source` (`web_source_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of connectome_web_source_detail
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for file_upload
-- ----------------------------
DROP TABLE IF EXISTS `file_upload`;
CREATE TABLE `file_upload` (
  `id` bigint(20) NOT NULL,
  `user_id` varchar(40) DEFAULT NULL,
  `name` varchar(100) NOT NULL,
  `size` double DEFAULT NULL,
  `path` varchar(512) DEFAULT NULL,
  `mine_type` varchar(150) DEFAULT NULL,
  `download_url` varchar(512) DEFAULT NULL,
  `description` varchar(512) DEFAULT NULL,
  `created_date` timestamp NULL DEFAULT NULL,
  `last_modified_date` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `file_upload_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of file_upload
-- ----------------------------
BEGIN;
COMMIT;


-- ----------------------------
-- Table structure for social_media_agent
-- ----------------------------
DROP TABLE IF EXISTS `social_media_agent`;
CREATE TABLE `social_media_agent` (
  `social_media_id` bigint(20) NOT NULL,
  `agentid` bigint(20) NOT NULL,
  `service_crawler_type` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`social_media_id`,`agentid`,`service_crawler_type`),
  KEY `service_crawler_type` (`service_crawler_type`),
  CONSTRAINT `social_media_agent_ibfk_1` FOREIGN KEY (`social_media_id`) REFERENCES `connectome_social_media` (`id`),
  CONSTRAINT `social_media_agent_ibfk_2` FOREIGN KEY (`service_crawler_type`) REFERENCES `code_service_crawler_type` (`service_crawler_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of social_media_agent
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for social_network
-- ----------------------------
DROP TABLE IF EXISTS `social_network`;
CREATE TABLE `social_network` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `social_network_name` varchar(25) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of social_network
-- ----------------------------
BEGIN;
INSERT INTO `social_network` VALUES (1, 'FACEBOOK');
INSERT INTO `social_network` VALUES (2, 'LINKEDIN');
INSERT INTO `social_network` VALUES (3, 'TWITTER');
INSERT INTO `social_network` VALUES (4, 'BLOG');
INSERT INTO `social_network` VALUES (5, 'COMMUNITY');
INSERT INTO `social_network` VALUES (6, 'INSTAGRAM');
INSERT INTO `social_network` VALUES (7, 'MAGAZINE');
INSERT INTO `social_network` VALUES (8, 'NAVER');
INSERT INTO `social_network` VALUES (9, 'NEWS');
INSERT INTO `social_network` VALUES (10, 'PAPER');
INSERT INTO `social_network` VALUES (11, 'PATENT');
INSERT INTO `social_network` VALUES (12, 'POST');
INSERT INTO `social_network` VALUES (13, 'VIDEO');
COMMIT;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` varchar(40) NOT NULL,
  `login` varchar(50) NOT NULL,
  `password_hash` varchar(60) NOT NULL,
  `first_name` varchar(50) DEFAULT NULL,
  `last_name` varchar(50) DEFAULT NULL,
  `email` varchar(191) DEFAULT NULL,
  `phone_country` varchar(7) DEFAULT NULL COMMENT 'Country Calling Code ',
  `phone_number` varchar(20) DEFAULT NULL COMMENT 'Phone Number',
  `image_url` varchar(256) DEFAULT NULL,
  `activated` bit(1) NOT NULL,
  `lang_key` varchar(10) DEFAULT NULL,
  `activation_key` varchar(20) DEFAULT NULL,
  `reset_key` varchar(20) DEFAULT NULL,
  `created_by` varchar(50) NOT NULL,
  `created_date` timestamp NULL DEFAULT NULL,
  `reset_date` timestamp NULL DEFAULT NULL,
  `last_modified_by` varchar(50) DEFAULT NULL,
  `last_modified_date` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ux_user_login` (`login`),
  UNIQUE KEY `ux_user_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
BEGIN;
INSERT INTO `user` VALUES ('UID_380e30ae-ca28-4837-bf0e-7fab38178260', 'admin', '$2a$10$gSAhZrxMllrbgj/kkK9UceBPpChGWJA7SYIb1Mqo.n5aNLq1/oRrC', 'Administrator', 'Administrator', 'admin@localhost', NULL, NULL, '', b'1', 'en', NULL, NULL, 'system', NULL, NULL, 'system', NULL);
INSERT INTO `user` VALUES ('UID_460e30ae-ca28-2737-cf0e-7fab38148220', 'user', '$2a$10$VEjxo0jq2YG9Rbk2HmX9S.k1uZBGYUHdUcid3g/vfiEl7lwWgOH/K', 'User', 'User', 'user@localhost', NULL, NULL, '', b'1', 'en', NULL, NULL, 'system', NULL, NULL, 'admin', '2021-06-24 07:42:48');
INSERT INTO `user` VALUES ('UID_850e30ae-ca28-4537-af0e-7fab38168550', 'thienha', '$2a$10$iii3DlEC.vWQj.OtGNapyO0BkTdrP7.e0u560AFNURCUcCv.0zWm6', 'thien', 'ha', 'thienhv@saltlux.com', NULL, NULL, NULL, b'1', 'en', NULL, NULL, 'admin', '2021-07-02 01:39:19', NULL, 'system', '2021-06-24 07:42:48');
COMMIT;

-- ----------------------------
-- Table structure for user_authority
-- ----------------------------
DROP TABLE IF EXISTS `user_authority`;
CREATE TABLE `user_authority` (
  `user_id` varchar(40) NOT NULL,
  `authority_name` varchar(50) NOT NULL,
  PRIMARY KEY (`user_id`,`authority_name`),
  KEY `fk_authority_name` (`authority_name`),
  CONSTRAINT `fk_authority_name` FOREIGN KEY (`authority_name`) REFERENCES `authority` (`name`),
  CONSTRAINT `user_authority_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_authority
-- ----------------------------
BEGIN;
INSERT INTO `user_authority` VALUES ('UID_380e30ae-ca28-4837-bf0e-7fab38178260', 'ROLE_ADMIN');
INSERT INTO `user_authority` VALUES ('UID_380e30ae-ca28-4837-bf0e-7fab38178260', 'ROLE_USER');
INSERT INTO `user_authority` VALUES ('UID_460e30ae-ca28-2737-cf0e-7fab38148220', 'ROLE_USER');
INSERT INTO `user_authority` VALUES ('UID_850e30ae-ca28-4537-af0e-7fab38168550', 'ROLE_ADMIN');
INSERT INTO `user_authority` VALUES ('UID_850e30ae-ca28-4537-af0e-7fab38168550', 'ROLE_USER');
COMMIT;

-- ----------------------------
-- Table structure for web_source
-- ----------------------------
DROP TABLE IF EXISTS `web_source`;
CREATE TABLE `web_source` (
  `web_source_id` varchar(40) CHARACTER SET utf8 NOT NULL,
  `web_url` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `service_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `service_language` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `category` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'keyword list by comma',
  `condition` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'QUERY=\nKEY1=VALUE1\nKEY2=VALUE2',
  `is_attachment` tinyint(1) NOT NULL DEFAULT 0,
  `is_comment` tinyint(1) NOT NULL DEFAULT 0,
  `last_updated_at` datetime NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `registered_at` datetime NOT NULL DEFAULT current_timestamp(),
  `state` int(11) DEFAULT 0 COMMENT '-1: Deleted\r\n0: add New\r\n1: Processing\r\n2: Processed\r\n\r\n',
  `service_crawler_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `web_source_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`web_source_id`) USING BTREE,
  UNIQUE KEY `web_url` (`web_url`) USING BTREE,
  KEY `FK_web_site_code_service_type` (`service_type`) USING BTREE,
  KEY `FK_web_site_code_language` (`service_language`) USING BTREE,
  CONSTRAINT `FK_web_source_code_language` FOREIGN KEY (`service_language`) REFERENCES `code_language` (`language`),
  CONSTRAINT `FK_web_source_code_service_type` FOREIGN KEY (`service_type`) REFERENCES `code_service_type` (`service_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of web_source
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for web_source_template
-- ----------------------------
DROP TABLE IF EXISTS `web_source_template`;
CREATE TABLE `web_source_template` (
  `template_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `site_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `section_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `web_url` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `service_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `service_language` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `service_crawler_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`template_id`) USING BTREE,
  KEY `FK_web_source_template_code_service_type` (`service_type`) USING BTREE,
  KEY `FK_web_source_template_code_language` (`service_language`) USING BTREE,
  KEY `service_crawler_type` (`service_crawler_type`),
  CONSTRAINT `FK_web_source_template_code_language` FOREIGN KEY (`service_language`) REFERENCES `code_language` (`language`),
  CONSTRAINT `FK_web_source_template_code_service_type` FOREIGN KEY (`service_type`) REFERENCES `code_service_type` (`service_type`),
  CONSTRAINT `web_source_template_ibfk_1` FOREIGN KEY (`service_crawler_type`) REFERENCES `code_service_crawler_type` (`service_crawler_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of web_source_template
-- ----------------------------
BEGIN;
INSERT INTO `web_source_template` VALUES ('bing_search_news_en', 'bing.com', 'Search News', 'https://bing.com/news/search?q=$query$', 'NEWS', 'ENGLISH', 'METASEARCH_BING');
INSERT INTO `web_source_template` VALUES ('bing_search_video_en', 'bing.com', 'Search News', 'https://www.bing.com/videos/search?q=$query$', 'NEWS', 'ENGLISH', 'METASEARCH_BING');
INSERT INTO `web_source_template` VALUES ('facebook_profile_alias', 'facebook.com', 'Profile alias', 'https://mbasic.facebook.com/$query$/about', 'FACEBOOK', NULL, 'DE_FACEBOOK_PROFILE');
INSERT INTO `web_source_template` VALUES ('facebook_profile_id', 'facebook.com', 'Profile id', 'https://mbasic.facebook.com/profile.php?id=$query$&v=about', 'FACEBOOK', NULL, 'DE_FACEBOOK_PROFILE');
INSERT INTO `web_source_template` VALUES ('facebook_timeline', 'facebook.com', 'timeline', 'https://mbasic.facebook.com/$query$?v=timeline', 'FACEBOOK', NULL, 'DE_FACEBOOK_PAGE');
INSERT INTO `web_source_template` VALUES ('google_search_news_en', 'google.com', 'Search News', 'https://www.google.com/search?hl=en&tbm=nws&q=$query$', 'NEWS', 'ENGLISH', 'METASEARCH_GOOGLE');
INSERT INTO `web_source_template` VALUES ('google_search_news_kr', 'google.com', 'Search News', 'https://www.google.com/search?hl=kr&tbm=nws&q=$query$', 'NEWS', 'KOREAN', 'METASEARCH_GOOGLE');
INSERT INTO `web_source_template` VALUES ('hygall_hy', 'hygall.com', '������������', 'https://hygall.com/?mid=hy&search_target=title_content&search_keyword=$query$&best=10', 'COMMUNITY', NULL, 'DE_COMMUNITY_NEWS');
INSERT INTO `web_source_template` VALUES ('linedin', 'linkedin.com', 'recent-activity', 'https://www.linkedin.com/in/$query$/detail/recent-activity/shares/', 'LINKEDIN', NULL, 'LINKEDIN');
INSERT INTO `web_source_template` VALUES ('naver_blog_search', 'naver.com', 'blog', 'https://section.blog.naver.com/Search/Post.nhn?pageNo=1&rangeType=ALL&orderBy=sim&keyword=$query$', 'BLOG', 'KOREAN', 'METASEARCH_NAVER');
INSERT INTO `web_source_template` VALUES ('scholar.google_search', 'scholar.google.co.kr', 'PAPER', 'https://scholar.google.co.kr/scholar?hl=en&q=$query$', 'PAPER', 'KOREAN', 'DE_GGSCHOLAR_SEARCH');
COMMIT;

-- ----------------------------
-- View structure for connectome_agentid
-- ----------------------------
DROP VIEW IF EXISTS `connectome_agentid`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `connectome_agentid` AS select `t1`.`connectome_id` AS `connectome_id`,`t1`.`social_media_id` AS `social_media_id`,`t2`.`agentid` AS `agentid` from (`connectome_social_media_detail` `t1` join `social_media_agent` `t2`) where `t1`.`social_media_id` = `t2`.`social_media_id`;

SET FOREIGN_KEY_CHECKS = 1;
