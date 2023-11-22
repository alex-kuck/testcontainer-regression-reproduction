package com.alexkuck.notification

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "com.alexkuck.notification")
data class NotificationConfiguration(
	val topic: String,
)
