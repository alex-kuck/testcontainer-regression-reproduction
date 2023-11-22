import org.gradle.api.JavaVersion.VERSION_17
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	kotlin("jvm") version "1.9.20"
	kotlin("kapt") version "1.9.20"
	kotlin("plugin.allopen") version "1.9.20"
	kotlin("plugin.spring") version "1.9.20"
	id("org.springframework.boot") version "3.1.5"
	id("io.spring.dependency-management") version "1.1.4"
	id("org.openapi.generator") version "7.1.0"
}

group = rootProject.group
version = rootProject.version

java.sourceCompatibility = VERSION_17

repositories {
	mavenCentral()
}

val testContainersVersion = "1.18.3" // working
//val testContainersVersion = "1.19.0" // not working
//val testContainersVersion = "1.19.2" // not working
//val testContainersVersion = "1.19.3" // working 😅

dependencies {
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("io.github.microutils:kotlin-logging:3.0.5")

	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("org.springframework.kafka:spring-kafka")

	implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.16.0")

	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

	testImplementation("org.testcontainers:testcontainers:$testContainersVersion")
	testImplementation("org.testcontainers:kafka:$testContainersVersion")

	testImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(module = "junit")
		exclude(module = "junit-vintage-engine")
		exclude(module = "mockito-core")
	}
	testImplementation("org.springframework.security:spring-security-test")
	testImplementation("com.ninja-squad:springmockk:4.0.2")
	testImplementation("org.junit.jupiter:junit-jupiter-api")
	testImplementation("com.squareup.okhttp3:mockwebserver:4.12.0")
	testImplementation("org.awaitility:awaitility-kotlin:4.2.0")
	testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

kapt {
	correctErrorTypes = true
	arguments {
		arg("mapstruct.defaultComponentModel", "spring")
	}
}

springBoot {
	buildInfo()
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "17"
	}
}

tasks.getByName<Jar>("jar") {
	enabled = false
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.register("prepareKotlinBuildScriptModel") {}
