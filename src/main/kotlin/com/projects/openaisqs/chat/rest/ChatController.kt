package com.projects.openaisqs.chat.rest

import aws.smithy.kotlin.runtime.util.length
import com.projects.openaisqs.chat.domain.ChatService
import com.projects.openaisqs.chat.rest.models.ChatMessageDTO
import com.projects.openaisqs.queue.MessageId
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/chat")
class ChatController(private val chatService: ChatService) {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    @PostMapping
    suspend fun sendChatMessage(message: ChatMessageDTO): MessageId {
        logger.info("Received message: ${message.message}")
        val messageId = chatService.sendChatMessage(message.message)
        logger.info("Message posted sent to queue with id: $messageId")
        return messageId
    }

    @GetMapping
    suspend fun getChatMessages(@RequestParam maxNumberOfMessages: Int?): List<ChatMessageDTO> {
        logger.info("Getting chat messages")
        val chatMessages = chatService.getChatMessages(maxNumberOfMessages).map { ChatMessageDTO(it.body ?: "") }
        logger.info("Returning ${chatMessages.length} chat messages")
        return chatMessages
    }
}