package com.projects.openaisqs.openai.api

import com.projects.openaisqs.openai.api.models.*
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class OpenAiService(
    private val openAiRestTemplateComponent: OpenAiRestTemplateComponent,
) {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    fun createChatCompletion(modelId: String, messages: List<Message>): ChatCompletionResponse {
        logger.info("Chatting with openAi model $modelId")
        val requestObject = ChatCompletionRequest(modelId, messages)
        try {
            val result = openAiRestTemplateComponent
                .openAiRestTemplate()
                .postForEntity(
                    "/chat/completions",
                    requestObject,
                    ChatCompletionResponse::class.java
                )
            if(result.statusCode.isError || result.body == null) {
                throw Exception("Failed to chat with openAi: $result")
            }
            logger.info("Finished chat with openAi, finish reason ${result.body!!.choices[0].finishReason}")
            return result.body!!
        } catch (e: Exception) {
            logger.warn("Failed to chat with openAi: $e")
            throw e
        }
    }

    fun listModels(): List<Model> {
        try {
            val models = openAiRestTemplateComponent
                .openAiRestTemplate()
                .getForEntity(
                    "/models",
                    ModelResponse::class.java
                )
            if (models.statusCode.isError || models.body == null) {
                throw Exception("Failed to get models: $models")
            }
            return models.body!!.data
        } catch (e: Exception) {
            logger.warn("Failed to get models: $e")
            throw e
        }
    }
}