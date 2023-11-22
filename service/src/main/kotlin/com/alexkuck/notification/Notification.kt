package com.alexkuck.notification

import com.fasterxml.jackson.annotation.JsonProperty

data class Notification(
	@JsonProperty("message") val message: String,
)
