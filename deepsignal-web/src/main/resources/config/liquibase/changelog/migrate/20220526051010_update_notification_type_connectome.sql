BEGIN;
UPDATE deepsignal.notification_type SET template_icon='<i class="icon-noti-type icon-connectome"></i>' WHERE name='training completed';
UPDATE deepsignal.notification SET icon_content='<i class="icon-noti-type icon-connectome"></i>' WHERE type_id=4;
COMMIT;