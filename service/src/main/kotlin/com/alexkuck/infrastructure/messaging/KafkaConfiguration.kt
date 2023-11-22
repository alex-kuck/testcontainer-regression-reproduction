package com.alexkuck.infrastructure.messaging

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate

const val NOTIFICATION_KAFKA = "notification-kafka"

@Configuration
@ConfigurationProperties(prefix = "spring.kafka.producer-notification")
class NotificationKafkaConfiguration : KafkaProperties.Producer() {
	@Autowired
	private lateinit var commonProperties: KafkaProperties

	private fun <K, V> notificationProducerFactory() =
		DefaultKafkaProducerFactory<K, V>((commonProperties.buildProducerProperties() + buildProperties()))

	@Qualifier(NOTIFICATION_KAFKA)
	@Bean
	fun <K, V> notificationKafkaTemplate() = KafkaTemplate(notificationProducerFactory<K, V>())
}
