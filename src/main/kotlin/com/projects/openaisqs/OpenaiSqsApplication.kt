package com.projects.openaisqs

import com.projects.openaisqs.queue.awssqs.AwsConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching

@SpringBootApplication
@EnableCaching
@EnableConfigurationProperties(AwsConfig::class)
class OpenaiSqsApplication

fun main(args: Array<String>) {
	runApplication<OpenaiSqsApplication>(*args)
}
