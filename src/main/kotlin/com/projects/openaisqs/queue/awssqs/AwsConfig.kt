package com.projects.openaisqs.queue.awssqs

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "aws")
data class AwsConfig(
    val accessKey: String,
    val secretKey: String,
    val region: String,
)