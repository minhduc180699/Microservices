BEGIN;
SET foreign_key_checks = 0;
DELETE FROM `notification_type`;
SET foreign_key_checks = 1;
COMMIT;

BEGIN;
INSERT INTO `notification_type` VALUES (1, 'feed', 'Great! Your feed updated, let''s see what''s new!', 'Feed Updated', '/feed', 'learning completed', 'basic', null, '<i class="icon-noti-type icon-learning"></i>', 'notification.basic.content.learning-complete', 'notification.basic.title.learning-complete');
INSERT INTO `notification_type` VALUES (2, 'comment', '{0} also commented on {1}', 'Comment Feed', '', 'comment feed', 'interpolation', null, '<i class="icon-noti-type icon-signal"></i>', 'notification.interpolation.content.comment-feed', 'notification.interpolation.title.comment-feed');
INSERT INTO `notification_type` VALUES (3, 'comment', '{1} replied to your comment', 'Reply comment', null, 'reply comment', 'interpolation', null, '<i class="icon-noti-type icon-signal"></i>', 'notification.interpolation.content.reply-comment', 'notification.interpolation.title.reply-comment');
INSERT INTO `notification_type` VALUES (4, 'connectome', 'The training is finished!', 'training completed', '/my-ai/connectome', 'training completed', 'basic', null, '<i class="icon-noti-type icon-signal"></i>', null, null);
COMMIT;
