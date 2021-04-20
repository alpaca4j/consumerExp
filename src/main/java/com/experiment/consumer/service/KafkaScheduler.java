package com.experiment.consumer.service;

import com.experiment.consumer.ConsumerExpApp;
import com.experiment.consumer.config.KafkaProperties;
import com.experiment.consumer.domain.KafkaNotification;
import com.experiment.consumer.domain.Notification;
import com.experiment.consumer.repository.NotificationRepository;
import com.experiment.consumer.service.dto.NotificationDTO;
import com.experiment.consumer.web.rest.errors.BadRequestAlertException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import tech.jhipster.web.util.HeaderUtil;

@Component
public class KafkaScheduler {

    private static final Logger log = LoggerFactory.getLogger(KafkaScheduler.class);
    private ExecutorService sseExecutorService = Executors.newCachedThreadPool();
    ObjectMapper objectMapper = new ObjectMapper();

    private NotificationService notificationService;

    private NotificationRepository notificationRepository;

    public KafkaScheduler(
        NotificationService notificationService,
        NotificationRepository notificationRepository,
        KafkaProperties kafkaProperties
    ) {
        this.notificationService = notificationService;
        this.notificationRepository = notificationRepository;
        this.kafkaProperties = kafkaProperties;
    }

    private final KafkaProperties kafkaProperties;

    @Scheduled(fixedDelay = 1000)
    public void pullEvents() {
        log.debug("REST request to consume records from Kafka topics {}", "notifications");
        Map<String, Object> consumerProps = kafkaProperties.getConsumerProps();
        consumerProps.remove("topic");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(consumerProps);
        consumer.subscribe(Collections.singleton("notifications"));
        boolean exitLoop = false;
        while (!exitLoop) {
            try {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(5));
                for (ConsumerRecord<String, String> record : records) {
                    System.out.println(record.value());
                    KafkaNotification notification = objectMapper.readValue(record.value(), KafkaNotification.class);
                    System.out.println(notification);

                    NotificationDTO dto = new NotificationDTO();
                    dto.setAcknowledged(false);
                    dto.setLevel(notification.getLevel());
                    dto.setMessage(notification.getMessage());
                    dto.setOrigin(notification.getOrigin());
                    dto.setTitle(notification.getTitle());
                    dto.setUser(notification.getUser());
                    dto.setTime(Instant.ofEpochMilli(Long.parseLong(notification.getTime())));

                    NotificationDTO result = notificationService.save(dto);
                }
                //                        emitter.send(SseEmitter.event().comment(""));
            } catch (Exception ex) {
                log.info("Complete with error {}", ex.getMessage(), ex);
                //                        emitter.completeWithError(ex);
                exitLoop = true;
            }
        }
        consumer.close();
        //                emitter.complete();

        log.warn("Hello! Scheduler");
    }
}
