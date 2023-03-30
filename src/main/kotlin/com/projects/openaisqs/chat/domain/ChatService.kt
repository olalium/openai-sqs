package com.projects.openaisqs.chat.domain

import com.projects.openaisqs.queue.MessageId
import com.projects.openaisqs.queue.QueueMessage
import com.projects.openaisqs.queue.QueueService
import com.projects.openaisqs.queue.QueueUrl
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component

@Component
class ChatService(private val queueService: QueueService) {
    private val logger = LoggerFactory.getLogger(this.javaClass)
    private final val CHAT_QUEUE_NAME = "chat"

    suspend fun getChatQueueUrl(): QueueUrl {
        return try {
            queueService.getQueueUrl(CHAT_QUEUE_NAME)
        } catch (e: Exception) {
            logger.info("Chat queue not found, creating new queue")
            queueService.createQueue(CHAT_QUEUE_NAME)
        }
    }

    suspend fun sendChatMessage(message: String): MessageId {
        val chatQueueUrl = getChatQueueUrl()
        return queueService.sendMessage(chatQueueUrl, message)
    }

    suspend fun getChatMessages(maxNumberOfMessages: Int?): List<QueueMessage> {
        val chatQueueUrl = getChatQueueUrl()
        return queueService.receiveBatchMessages(chatQueueUrl, maxNumberOfMessages)
    }
}