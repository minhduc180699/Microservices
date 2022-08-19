package com.saltlux.deepsignal.web.schedule;

import com.saltlux.deepsignal.web.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
@Slf4j
public class DeleteTemporaryAccountScheduler {

    @Autowired
    private UserService userService;

    @Value("${deepsignal.scheduled.enable}")
    private Boolean enable;

    @Scheduled(cron = "${delete.user.cron}")
    public void scheduleCronTask() {
        if (!enable) {
            log.info("ALERT is not enable");
            return;
        }
        userService.deleteTemporaryAccount();
    }
}
