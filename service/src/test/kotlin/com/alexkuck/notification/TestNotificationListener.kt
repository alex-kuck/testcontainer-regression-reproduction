package com.alexkuck.notification

import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.annotation.TopicPartition
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

@Component
class TestNotificationListener {

	val log = mutableListOf<Notification>()

	@KafkaListener(
		topics = ["\${com.alexkuck.notification.topic}"],
		groupId = "notification",
		properties = ["spring.json.value.default.type=com.alexkuck.notification.Notification"],
		topicPartitions = [TopicPartition(topic = "\${com.alexkuck.notification.topic}", partitions = ["0"])],
	)
	fun consume(@Payload notification: Notification) {
		log += notification
	}

}
