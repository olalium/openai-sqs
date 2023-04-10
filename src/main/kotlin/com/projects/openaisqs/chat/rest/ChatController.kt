package com.projects.openaisqs.chat.rest

import aws.smithy.kotlin.runtime.util.length
import com.projects.openaisqs.chat.rest.models.ChatMessageDTO
import com.projects.openaisqs.queue.MessageId
import com.projects.openaisqs.queue.QueueService
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/chat")
class ChatController(private val queueService: QueueService) {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    @PostMapping("/{queueName}")
    suspend fun sendChatMessage(@PathVariable queueName: String, @RequestParam message: ChatMessageDTO): MessageId {
        logger.info("Sending message to queue: $queueName")
        val chatQueueUrl = queueService.getQueueUrl(queueName)
        val messageId = queueService.sendMessage(chatQueueUrl, message.message)
        logger.info("Message sent to queue with id: $messageId")
        return messageId
    }

    @GetMapping("/{queueName}")
    suspend fun getChatMessages(@PathVariable queueName: String, @RequestParam maxNumberOfMessages: Int?): List<ChatMessageDTO> {
        logger.info("Getting chat messages from queue: $queueName")
        val chatQueueUrl = queueService.getQueueUrl(queueName)
        val chatMessages = queueService.receiveBatchMessages(chatQueueUrl, maxNumberOfMessages)
        if (chatMessages.isNotEmpty()) {
            chatMessages.forEach{ queueService.deleteMessage(chatQueueUrl, it.receiptHandle)}
        }
        logger.info("Returning ${chatMessages.length} chat messages")
        return chatMessages.map { ChatMessageDTO(it.body ?: "") }
    }
}