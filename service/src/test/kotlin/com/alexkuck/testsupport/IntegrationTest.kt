package com.alexkuck.testsupport

import com.alexkuck.infrastructure.mock.KafkaInitializer
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ExtendWith(SpringExtension::class)
@ActiveProfiles("integration-test")
@ContextConfiguration(initializers = [KafkaInitializer::class])
annotation class IntegrationTest
