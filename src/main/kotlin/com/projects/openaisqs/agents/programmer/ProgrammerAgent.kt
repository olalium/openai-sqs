package com.projects.openaisqs.agents.programmer

import com.projects.openaisqs.agents.Agent
import com.projects.openaisqs.agents.NoMessageException
import com.projects.openaisqs.openai.api.OpenAiService
import com.projects.openaisqs.openai.api.models.createSystemMessage
import com.projects.openaisqs.openai.api.models.createUserMessage
import com.projects.openaisqs.queue.MessageId
import com.projects.openaisqs.queue.QueueMessage
import com.projects.openaisqs.queue.QueueService
import com.projects.openaisqs.queue.QueueUrl
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class ProgrammerAgent (private val openAiService: OpenAiService, private val queueService: QueueService): Agent {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    override val inputQueueName = "programmer-input"
    override val modelId = "gpt-3.5-turbo-0301"
    override val outputQueueName = "programmer-output"
    override val systemMessageContent = "Act as an experienced kotlin spring boot programmer"


    @Scheduled(fixedDelay = 1000)
    override fun processQueueMessage() {
        try {
            runBlocking {
                val queueUrl = queueService.getQueueUrl(inputQueueName)
                val queueMessage = getMessageFromQueue(queueUrl)
                val chatCompletionMessage = getChatCompletionResponse(queueMessage)
                sendChatCompletionMessageToQueue(queueMessage.messageId!!, chatCompletionMessage)

                logger.info("Deleting message ${queueMessage.messageId} from queue: $inputQueueName")
                queueService.deleteMessage(queueUrl, queueMessage.receiptHandle)
            }
        } catch (e: NoMessageException) {
            logger.info("No message to process")
        } catch (e: Exception) {
            logger.error("Failed to process queue message", e)
        }

    }

    private suspend fun getMessageFromQueue(queueUrl: QueueUrl): QueueMessage {
        logger.info("Polling single message from queue: $inputQueueName")
        val queueMessage = queueService.receiveBatchMessages(queueUrl, 1)
        if (queueMessage.isEmpty() || queueMessage[0].body == null) {
            throw NoMessageException("Failed to get message from queue: $inputQueueName")
        }
        logger.info("Got message ${queueMessage[0].messageId} from queue: $inputQueueName")
        return queueMessage[0]
    }

    private suspend fun getChatCompletionResponse(queueMessage: QueueMessage): String {
        val messageContent = queueMessage.body!!
        val chatCompletionMessages =
            listOf(createSystemMessage(systemMessageContent), createUserMessage(messageContent))
        val chatCompletionResponse = openAiService.createChatCompletion(modelId, chatCompletionMessages)
        return chatCompletionResponse.choices[0].message.content
    }

    private suspend fun sendChatCompletionMessageToQueue(messageId: MessageId, chatCompletionMessage: String) {
        logger.info("Sending ai response from $messageId to queue: $outputQueueName")
        val outputQueueUrl = queueService.getQueueUrl(outputQueueName)
        queueService.sendMessage(outputQueueUrl, chatCompletionMessage)
    }
}