package com.projects.openaisqs.openai.rest

import aws.smithy.kotlin.runtime.util.length
import com.projects.openaisqs.openai.api.OpenAiService
import com.projects.openaisqs.openai.api.models.Message
import com.projects.openaisqs.openai.api.models.Model
import com.projects.openaisqs.openai.rest.models.MessageDTO
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/openai")
class OpenAiController(private val openAiService: OpenAiService) {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    @GetMapping("/models")
    fun getOpenAiModels(): List<Model> {
        logger.info("Getting openAi models")
        val models = openAiService.listModels()
        logger.info("Returning ${models.length} models")
        return models
    }

    @PostMapping("/chat/{modelId}")
    fun chat(@RequestBody messages: List<MessageDTO>, @PathVariable modelId: String): Message {
        val domainMessages = messages.map { Message(it.role, it.content) }
        val chatCompletionResult = openAiService.createChatCompletion(modelId, domainMessages)
        return chatCompletionResult.choices[0].message
    }
}