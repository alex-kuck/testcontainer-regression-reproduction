package com.alexkuck.notification

import com.alexkuck.testsupport.IntegrationTest
import org.assertj.core.api.Assertions.assertThat
import org.awaitility.kotlin.await
import org.awaitility.kotlin.untilAsserted
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@IntegrationTest
class NotificationServiceTest {
	@Autowired
	private lateinit var testNotificationListener: TestNotificationListener

	@Autowired
	private lateinit var notificationService: NotificationService

	@Test
	fun publish() {
		val notification = Notification(message = "Hello World")

		notificationService.publish(notification)

		await untilAsserted {
			assertThat(testNotificationListener.log).anyMatch {
				it.message == notification.message
			}
		}
	}
}
