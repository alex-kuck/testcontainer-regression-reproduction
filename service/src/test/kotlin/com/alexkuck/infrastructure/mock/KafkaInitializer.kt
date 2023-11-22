package com.alexkuck.infrastructure.mock

import mu.KotlinLogging
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.testcontainers.containers.KafkaContainer
import org.testcontainers.containers.output.Slf4jLogConsumer
import org.testcontainers.utility.DockerImageName

private val log = KotlinLogging.logger {}

const val kafkaUsername = "admin"
const val kafkaPassword = "admin-secret"
const val kafkaSecurityProtocol = "SASL_PLAINTEXT"

class KafkaInitializer : ApplicationContextInitializer<ConfigurableApplicationContext> {

	private val kafkaContainer = KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:6.2.1"))
		.withExposedPorts(9093)
		.withEnv(
			mapOf(
				"KAFKA_LISTENER_SECURITY_PROTOCOL_MAP" to "BROKER:PLAINTEXT,PLAINTEXT:$kafkaSecurityProtocol",
				"KAFKA_LISTENER_NAME_PLAINTEXT_SASL_ENABLED_MECHANISMS" to "PLAIN",
				"KAFKA_LISTENER_NAME_PLAINTEXT_PLAIN_SASL_JAAS_CONFIG" to
					"org.apache.kafka.common.security.plain.PlainLoginModule required " +
					"username=\"$kafkaUsername\" " +
					"password=\"$kafkaPassword\"" +
					"user_admin=\"$kafkaPassword\"; ",
			),
		)
		.withReuse(true)
		.apply { start() }
		.apply { println("Kafka starting on http://$host:$firstMappedPort") }
		.apply { this.followOutput(Slf4jLogConsumer(log)) }

	override fun initialize(configurableApplicationContext: ConfigurableApplicationContext) {
		TestPropertyValues.of(
			"spring.kafka.consumer.bootstrap-servers=${kafkaContainer.bootstrapServers}",
			"spring.kafka.consumer.properties.sasl.jaas.config=org.apache.kafka.common.security.plain.PlainLoginModule required username=\"$kafkaUsername\" password=\"$kafkaPassword\";",
			"spring.kafka.consumer.properties.sasl.mechanism=PLAIN",
			"spring.kafka.consumer.properties.security.protocol=$kafkaSecurityProtocol",
			"spring.kafka.consumer.properties.value.deserializer=org.springframework.kafka.support.serializer.ErrorHandlingDeserializer",
			"spring.kafka.consumer.properties.spring.deserializer.value.delegate.class=org.springframework.kafka.support.serializer.JsonDeserializer",

			"spring.kafka.producer-notification.bootstrap-servers=${kafkaContainer.bootstrapServers}",
			"spring.kafka.producer-notification.properties.sasl.jaas.config=org.apache.kafka.common.security.plain.PlainLoginModule required username=\"$kafkaUsername\" password=\"$kafkaPassword\";",
			"spring.kafka.producer-notification.properties.security.protocol=$kafkaSecurityProtocol",
		).applyTo(configurableApplicationContext.environment)
	}
}
