package com.projects.openaisqs.openai.api

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "openai")
data class OpenAiApiConfig(
    val apiKey: String,
)