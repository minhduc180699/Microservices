-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `faqs`;
DROP TABLE IF EXISTS `inquiry_answer`;
DROP TABLE IF EXISTS `inquiry_answer_email`;
DROP TABLE IF EXISTS `inquiry_question`;
DROP TABLE IF EXISTS `category`;

CREATE TABLE `category` (
  `code` varchar(50) NOT NULL,
  `type` integer NULL,
  PRIMARY KEY (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for faqs
-- ----------------------------

CREATE TABLE `faqs` (
  `faq_id` bigint(20) NOT NULL,
  `category_code` varchar(50) NOT NULL,
  `title` varchar(1000) DEFAULT NULL,
  `question` text NOT NULL ,
  `answer` text NOT NULL ,
  `file` varchar(1000) DEFAULT NULL,
  `view_count` int(11) DEFAULT 0,
  `created_date` datetime DEFAULT NULL,
  `note` text DEFAULT NULL,
  PRIMARY KEY (`faq_id`),
  CONSTRAINT `fk_category_code` FOREIGN KEY (`category_code`) REFERENCES `category` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for inquiry_question
-- ----------------------------
CREATE TABLE `inquiry_question` (
  `id` bigint(20) NOT NULL,
  `category_code` varchar(50) NOT NULL,
  `title` varchar(1000) NOT NULL,
  `content` text NOT NULL ,
  `email` varchar(100) NOT NULL ,
  `name` varchar(100) NOT NULL ,
  `file` varchar(1000) DEFAULT NULL,
  `status` int(11) DEFAULT 1,
  `is_public` tinyint(1) unsigned NOT NULL DEFAULT 0,
  `is_email` tinyint(1) unsigned NOT NULL DEFAULT 0,
  `user_id` varchar(50) NOT NULL,
  `created_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `inquiry_question_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `inquiry_question_ibfk_2` FOREIGN KEY (`category_code`) REFERENCES `category` (`code`)

) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for inquiry_answer
-- ----------------------------
CREATE TABLE `inquiry_answer` (
  `id` bigint(20) NOT NULL,
  `inquiry_question_id` bigint(20) NOT NULL,
  `content` text NOT NULL ,
  `file` varchar(1000) DEFAULT NULL,
  `user_id` varchar(50) NOT NULL,
  `created_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `inquiry_answer_ibfk_1` FOREIGN KEY (`inquiry_question_id`) REFERENCES `inquiry_question` (`id`),
  CONSTRAINT `inquiry_answer_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for inquiry_answer_email
-- ----------------------------
CREATE TABLE `inquiry_answer_email` (
  `id` bigint(20) NOT NULL,
  `inquiry_question_id` bigint(20) NOT NULL,
  `title` varchar(1000) NOT NULL,
  `content` text NOT NULL ,
  `email` varchar(100) NOT NULL ,
  `name` varchar(100) NOT NULL ,
  `created_date` datetime DEFAULT NULL,
  `send_date` datetime  DEFAULT NULL,
  `file_question` varchar(1000) DEFAULT NULL,
  `file_answer` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `inquiry_answer_email_ibfk_1` FOREIGN KEY (`inquiry_question_id`) REFERENCES `inquiry_question` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
