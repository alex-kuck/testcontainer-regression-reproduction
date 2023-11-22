package com.alexkuck.notification

import com.alexkuck.infrastructure.messaging.NOTIFICATION_KAFKA
import mu.KLogging
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class NotificationService(
	private val configuration: NotificationConfiguration,
	@Qualifier(NOTIFICATION_KAFKA) private val kafkaTemplate: KafkaTemplate<String, Notification>,
) {

	companion object : KLogging()

	fun publish(notification: Notification) {
		notification
			.let { kafkaTemplate.send(configuration.topic, it) }
			.also { it.handle { _, exception -> exception?.also { logger.error { exception } } } }
	}

}
