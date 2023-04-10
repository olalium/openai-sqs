package com.projects.openaisqs.openai.api

import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import java.time.Duration

@Component
class OpenAiRestTemplateComponent(
    private val openAiApiConfig: OpenAiApiConfig,
) {

    @Bean("openAiRestTemplate")
    fun openAiRestTemplate(): RestTemplate {
        return RestTemplateBuilder()
            .rootUri("https://api.openai.com/v1")
            .defaultHeader("Authorization", "Bearer ${openAiApiConfig.apiKey}")
            .setConnectTimeout(Duration.ofSeconds(30))
            .build()
    }
}