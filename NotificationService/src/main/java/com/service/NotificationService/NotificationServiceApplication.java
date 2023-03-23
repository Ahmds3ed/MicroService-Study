package com.service.NotificationService;

import com.service.NotificationService.dto.OrderEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;

@SpringBootApplication
@Slf4j
public class NotificationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotificationServiceApplication.class, args);
	}

	@KafkaListener(topics = "notificationTopic")
	public void handelNotification(OrderEvent orderEvent) {
		log.info("Notification has been recivied :: order {} " + orderEvent.getOrderNumber());
	}
}
