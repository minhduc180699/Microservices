package com.saltlux.deepsignal.web.api.consumer;

import static com.saltlux.deepsignal.web.util.TemplateConstant.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saltlux.deepsignal.web.api.ConnectomeFeedResource;
import com.saltlux.deepsignal.web.api.websocket.dto.MessageWrapper;
import com.saltlux.deepsignal.web.config.Constants;
import com.saltlux.deepsignal.web.domain.Connectome;
import com.saltlux.deepsignal.web.repository.ConnectomeRepository;
import com.saltlux.deepsignal.web.service.INotificationService;
import com.saltlux.deepsignal.web.util.Utils;
import io.netty.util.internal.StringUtil;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;
import org.redisson.api.RMapCache;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class RabbitmqConsumer {

    private final String DATE_FORMAT_PATTERN = "yyyy-MM-dd hh:mm:ss.SSS'Z'";

    private final SimpMessageSendingOperations messagingTemplate;
    private final ConnectomeRepository connectomeRepository;
    private final INotificationService notificationService;
    private final RMapCache<String, List<String>> mapNotification;
    private final ConnectomeFeedResource connectomeFeedResource;

    @Autowired
    @Qualifier("queueNotification")
    Queue queueNotification;

    public RabbitmqConsumer(
        INotificationService notificationService,
        ConnectomeRepository connectomeRepository,
        SimpMessageSendingOperations messagingTemplate,
        RMapCache<String, List<String>> mapNotification,
        ConnectomeFeedResource connectomeFeedResource
    ) {
        this.notificationService = notificationService;
        this.connectomeRepository = connectomeRepository;
        this.messagingTemplate = messagingTemplate;
        this.mapNotification = mapNotification;
        this.connectomeFeedResource = connectomeFeedResource;
    }

    /*
     * This is method to receive message from rabbitmq with queue default.
     * After receive message from rabbitmq, it will send data to websocket
     * */
    @RabbitListener(queues = "#{queueNotification.getName()}")
    public void receiveMessageNotification(final Message message) {
        try {
            String str = new String(message.getBody(), StandardCharsets.UTF_8);
            MessageWrapper messageWrapper = new ObjectMapper().readValue(str, MessageWrapper.class);
            log.info("Received message from rabbitmq, body: " + messageWrapper.getData());
            List<String> olderIds = mapNotification.get(WEBSOCKET_URL_NOTIFICATION);
            List<String> ids = new ArrayList<>();
            if (null != olderIds) {
                ids.addAll(olderIds);
            }
            List<String> userIds = (List<String>) messageWrapper.getData();
            ids.addAll(userIds);
            mapNotification.put(WEBSOCKET_URL_NOTIFICATION, ids);
            messagingTemplate.convertAndSend(messageWrapper.getUrlMessage(), new MessageWrapper(messageWrapper.getData()));
        } catch (Exception e) {
            e.getMessage();
        }
    }

    /*
     * This is method to receive message from rabbitmq of training status queue.
     * After receive message from rabbitmq, it will send data to websocket
     * */
    @RabbitListener(
        bindings = @QueueBinding(
            value = @org.springframework.amqp.rabbit.annotation.Queue("${application.rabbit-config.connectome-training.queue-name}"),
            exchange = @Exchange("${application.rabbit-config.connectome-training.exchange-name}"),
            key = "${application.rabbit-config.connectome-training.router-key}"
        )
    )
    public void receiveMessageTraining(final Message message) {
        try {
            String str = new String(message.getBody(), StandardCharsets.UTF_8);
            str = str.replace("\\", "");
            //str = str.substring(1, str.length() - 1);
            JSONObject jsonObject = new JSONObject(str);
            String connectomeId = jsonObject.getString("connectomeId");
            String status = jsonObject.getString("status");
            log.info("Received message training connectome from RabbitMQ, body: " + str);

            //            mapTrainingStatus.remove(connectomeId);
            Optional<Connectome> optionalConnectome = connectomeRepository.findById(connectomeId);
            List<String> receiverIds = Collections.singletonList(optionalConnectome.get().getUser().getId());
            notificationService.saveNotificationByAdminWithBasicType(receiverIds, Constants.NotificationName.TRAINING_COMPLETED);
            messagingTemplate.convertAndSend("/topic/updateConnectome", true);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            log.error(e.getMessage());
        }
    }

    /*
     * This is method to receive message from rabbitmq of training status queue.
     * After receive message from rabbitmq, it will send data to websocket
     * */
    @RabbitListener(
        bindings = @QueueBinding(
            value = @org.springframework.amqp.rabbit.annotation.Queue("${application.rabbit-config.feed-training.queue-name}"),
            exchange = @Exchange("${application.rabbit-config.feed-training.exchange-name}"),
            key = "${application.rabbit-config.feed-training.router-key}"
        )
    )
    public void receiveMessageRecommend(final Message message) {
        try {
            String str = new String(message.getBody(), StandardCharsets.UTF_8);
            str = str.replace("\\", "");
            JSONObject jsonObject = new JSONObject(str);
            String connectomeId = jsonObject.getString("connectomeId");
            String recommendDate = jsonObject.getString("recommendDate");
            log.info("Received message recommend connectome from RabbitMQ, body: " + str);
            SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT_PATTERN);
            Date dateParse = formatter.parse(recommendDate);
            Long newFeed = connectomeFeedResource.countNewFeedByDate(connectomeId, dateParse.toInstant());
            Optional<Connectome> optionalConnectome = connectomeRepository.findById(connectomeId);
            if (optionalConnectome.isPresent() && newFeed > 0) {
                List<String> receiverIds = new ArrayList<>(Collections.singletonList(optionalConnectome.get().getUser().getId()));
                notificationService.saveNotificationByAdminWithInterpolationType(
                    receiverIds,
                    Constants.NotificationName.LEARNING_COMPLETE,
                    null,
                    Utils.appendStyle(newFeed.toString(), "text-danger")
                );
                messagingTemplate.convertAndSend(WEBSOCKET_URL_UPDATE_FEED, new WebLoggingReceiver(connectomeId, recommendDate));
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /*
     * This is method to receive message from rabbitmq of signal tracking issue queue.
     * After receive message from rabbitmq, it will send data to websocket
     * */
    @RabbitListener(
        bindings = @QueueBinding(
            value = @org.springframework.amqp.rabbit.annotation.Queue("${application.rabbit-config.signal-tracking.queue-name}"),
            exchange = @Exchange("${application.rabbit-config.signal-tracking.exchange-name}"),
            key = "${application.rabbit-config.signal-tracking.router-key}"
        )
    )
    public void receiveMessageSignalTracking(final Message message) {
        try {
            String str = new String(message.getBody(), StandardCharsets.UTF_8);
            str = str.replace("\\", "");
            JSONObject jsonObject = new JSONObject(str);
            String connectomeId = jsonObject.getString("connectome_id");
            if (!StringUtil.isNullOrEmpty(connectomeId)) {
                messagingTemplate.convertAndSend(WEBSOCKET_URL_SIGNAL_TRACKING, new BaseWebsocketInfo(connectomeId));
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WebLoggingReceiver extends BaseWebsocketInfo {

        private String recommendDate;

        public WebLoggingReceiver(String connectomeId, String recommendDate) {
            super(connectomeId);
            this.recommendDate = recommendDate;
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BaseWebsocketInfo {

        private String connectomeId;
    }
}
