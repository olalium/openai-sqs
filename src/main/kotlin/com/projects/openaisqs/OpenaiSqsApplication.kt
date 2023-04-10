package com.projects.openaisqs

import com.projects.openaisqs.openai.api.OpenAiApiConfig
import com.projects.openaisqs.queue.awssqs.AwsConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableCaching
@EnableScheduling
@EnableConfigurationProperties(AwsConfig::class, OpenAiApiConfig::class)
class OpenaiSqsApplication

fun main(args: Array<String>) {
	runApplication<OpenaiSqsApplication>(*args)
}
